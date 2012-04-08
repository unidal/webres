package org.unidal.webres.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceContainer;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.tag.resource.IResourceMarkerProcessor;

/**
 * <ul>
 * Resource filter supports:
 * <li>Progressive Rendering</li>
 * <li>Resource Aggregation (JS & CSS)</li>
 * </ul>
 */
public class SimpleResourceFilter implements Filter {
	public static final String CSS_TOKEN = "css.token";

	public static final String JS_TOKEN = "js.token";

	public static final String GUID = "guid";

	@Override
	public void destroy() {
		// do nothing here
	}

	protected void doFilter(final HttpServletRequest req, final HttpServletResponse res, final FilterChain chain)
	      throws IOException, ServletException {
		try {
			ResourceRuntimeContext.setup(req.getContextPath());

			ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();
			IResourceMarkerProcessor processor = ctx.getContainer().getAttribute(IResourceMarkerProcessor.class);
			ResponseWrapper wrapper = new ResponseWrapper(res, processor);

			prepare(req, ctx);
			chain.doFilter(req, wrapper);
			wrapper.flushBuffer();
		} finally {
			ResourceRuntimeContext.reset();
		}
	}

	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
	      throws IOException, ServletException {
		if (req instanceof HttpServletRequest && res instanceof HttpServletResponse) {
			try {
				doFilter((HttpServletRequest) req, (HttpServletResponse) res, chain);
			} catch (RuntimeException e) {
				throw e;
			}
		} else {
			chain.doFilter(req, res);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// do nothing here
	}

	@SuppressWarnings("unchecked")
	protected void prepare(final HttpServletRequest req, ResourceRuntimeContext ctx) {
		String jsAjaxToken = req.getParameter(JS_TOKEN);
		String cssAjaxToken = req.getParameter(CSS_TOKEN);

		if (jsAjaxToken != null) {
			ctx.setAjaxDedupToken(SystemResourceType.Js, jsAjaxToken);
		}

		if (cssAjaxToken != null) {
			ctx.setAjaxDedupToken(SystemResourceType.Css, cssAjaxToken);
		}

		if (ctx.getDeferRendering() == null) {
			ctx.setDeferRendering(true);
		}

		// restore environment set/passed by JspTemplateEvaluator
		String guid = req.getParameter(GUID);

		if (guid != null) {
			// TODO need save and restore the environment
			IResourceContainer container = ctx.getConfig().getContainer();
			IResourceContext resourceCtx = container.removeAttribute(IResourceContext.class, guid);
			Map<String, Object> attributes = container.removeAttribute(Map.class, guid);

			if (resourceCtx != null) {
				ctx.getResourceContext().setSecure(resourceCtx.isSecure());
				ctx.getResourceContext().setPermutation(resourceCtx.getPermutation());
			}

			if (attributes != null) {
				for (Map.Entry<String, Object> e : attributes.entrySet()) {
					req.setAttribute(e.getKey(), e.getValue());
				}
			}
		}
	}

	static class OutputBuffer extends ByteArrayOutputStream {
		private ResponseWrapper m_res;

		private boolean m_chunkEnabled = false;

		public OutputBuffer(ResponseWrapper res, int size) {
			super(size);

			m_res = res;
		}

		@Override
		public void close() throws IOException {
			finish();
		}

		protected void disableChunk() {
			m_chunkEnabled = false;
		}

		protected void finish() throws IOException {
			flushChunk(true);
		}

		@Override
		public void flush() throws IOException {
			flushChunk(false);
		}

		protected void flushChunk(boolean lastChunk) throws IOException {
			if (false && m_chunkEnabled) {
				// if (size() > 0) {
				// ServletOutputStream out = m_res.getResponse().getOutputStream();
				//
				// if (!ResourceRuntimeContext.isInitialized()) {
				// byte[] data = super.toByteArray();
				//
				// if (!m_res.isCommitted() && lastChunk) {
				// m_res.setContentLength(data.length);
				// }
				//
				// super.reset();
				// out.write(data);
				// out.flush();
				// } else {
				// ResourceModelFlusher flusher = new ResourceModelFlusher();
				// String prepend = flusher.beforeFlush();
				// byte[] data = getProcessedData(prepend);
				//
				// if (!m_res.isCommitted() && lastChunk) {
				// m_res.setContentLength(data.length);
				// }
				//
				// out.write(data);
				// out.flush();
				// flusher.afterFlush();
				// }
				// }
			} else {
				ServletOutputStream out = m_res.getResponse().getOutputStream();
				byte[] data = getProcessedData();
				m_res.setContentLength(data.length);

				out.write(data);

				try {
					out.flush();
				} catch (IOException e) {
					if (e.getClass().getName().equals("org.mortbay.jetty.EofException") || e.getClass().getName().equals("java.net.SocketException")) {
						// ignore it
					} else {
						throw e;
					}
				}
			}
		}

		protected byte[] getProcessedData() throws IOException {
			String charset = m_res.getCharacterEncoding();
			String content = super.toString(charset);
			StringBuilder processContent = new StringBuilder(content);

			super.reset();

			m_res.getProcessor().process(processContent);

			return processContent.toString().getBytes(charset);
		}
	}

	public static class ResponseWrapper extends HttpServletResponseWrapper {
		private int m_capacity = 8192;

		private OutputBuffer m_buffer;

		private PrintWriter m_writer;

		private ServletOutputStream m_out;

		private IResourceMarkerProcessor m_processor;

		public ResponseWrapper(HttpServletResponse res, IResourceMarkerProcessor processor) {
			super(res);

			m_processor = processor;
		}

		@Override
		public void flushBuffer() throws IOException {
			super.flushBuffer();

			if (m_writer != null) {
				m_writer.flush();
			} else if (m_out != null) {
				m_out.flush();
			}

			if (m_buffer != null) {
				m_buffer.flush();
			}
		}

		OutputBuffer getBuffer() {
			return m_buffer;
		}

		@Override
		public int getBufferSize() {
			return m_capacity;
		}

		@Override
		public String getCharacterEncoding() {
			String charset = super.getCharacterEncoding();

			return charset == null ? "utf-8" : charset;
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			if (m_writer != null) {
				throw new IOException("Can't getOutputStream() after getWriter()!");
			}

			if (m_out == null) {
				m_buffer = new OutputBuffer(this, m_capacity);
				m_out = new ServletOutputStreamWrapper(m_buffer);
			}

			return m_out;
		}

		protected IResourceMarkerProcessor getProcessor() {
			return m_processor;
		}

		@Override
		public PrintWriter getWriter() throws IOException {
			if (m_out != null) {
				throw new IOException("Can't getWriter() after getOutputStream()!");
			}

			if (m_writer == null) {
				m_buffer = new OutputBuffer(this, m_capacity);
				m_writer = new PrintWriter(new OutputStreamWriter(m_buffer, getCharacterEncoding()));
			}

			return m_writer;
		}

		@Override
		public void reset() {
			super.reset();

			if (m_buffer != null) {
				m_buffer.reset();
			}
		}

		@Override
		public void resetBuffer() {
			super.resetBuffer();

			if (m_buffer != null) {
				m_buffer.reset();
			}
		}

		@Override
		public void setBufferSize(int size) {
			if (size > m_capacity) {
				m_capacity = size;
			}
		}

		@Override
		public void setContentLength(int len) {
			if (m_buffer != null) {
				// 'Content-Length' header can't co-exist with 'Transfer-Encoding'
				m_buffer.disableChunk();
			}

			if (len > m_capacity) {
				m_capacity = len;
			}
		}

		@Override
		public void setHeader(String name, String value) {
			// application must set this hint explicitly
			if (m_buffer != null && "Connection".equalsIgnoreCase(name) && "close".equalsIgnoreCase(value)) {
				m_buffer.disableChunk();
			}

			super.setHeader(name, value);
		}

		@Override
		public String toString() {
			if (m_buffer != null) {
				try {
					return m_buffer.toString(getCharacterEncoding());
				} catch (UnsupportedEncodingException e) {
					return m_buffer.toString();
				}
			} else {
				return "";
			}
		}
	}

	static class ServletOutputStreamWrapper extends ServletOutputStream {
		private OutputBuffer m_buffer;

		public ServletOutputStreamWrapper(OutputBuffer buffer) {
			m_buffer = buffer;
		}

		@Override
		public void close() throws IOException {
			m_buffer.finish();
		}

		@Override
		public void flush() throws IOException {
			m_buffer.flush();
		}

		@Override
		public void write(int b) throws IOException {
			m_buffer.write(b);
		}
	}
}