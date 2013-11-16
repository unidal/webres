package org.unidal.webres.server.template;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.unidal.helper.Files;
import org.unidal.webres.resource.annotation.ContextPath;
import org.unidal.webres.resource.annotation.ServerUrlPrefix;
import org.unidal.webres.resource.annotation.WarRoot;
import org.unidal.webres.resource.api.ITemplate;
import org.unidal.webres.resource.api.ITemplateContext;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceContainer;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.ITemplateEvaluator;
import org.unidal.webres.resource.template.TemplateLanguage;
import org.unidal.webres.server.SimpleResourceFilter;

public class JspTemplateEvaluator implements ITemplateEvaluator, IResourceRegisterable<JspTemplateEvaluator> {
   private File m_warRoot;

   private String m_serverUrlPrefix;

   private String m_contextPath;

   private ITemplateContext m_ctx;

   protected String buildGuid(IResourceContext resourceCtx, ITemplateContext templateCtx) {
      IResourceContainer container = ResourceRuntimeContext.ctx().getConfig().getContainer();
      int hash1 = System.identityHashCode(resourceCtx);
      int hash2 = System.identityHashCode(templateCtx);
      String guid = String.valueOf(hash1 ^ hash2);

      // passing data via global container
      container.setAttribute(IResourceContext.class, guid, resourceCtx);

      if (m_ctx != null) {
         Map<String, Object> attributes = m_ctx.getAttributes();

         if (!attributes.isEmpty()) {
            container.setAttribute(Map.class, guid, attributes);
         }
      }

      return guid;
   }

   protected String buildRequestUrl(String path, String guid) {
      StringBuilder sb = new StringBuilder(256);

      sb.append(m_serverUrlPrefix);

      if (m_contextPath != null) {
         sb.append(m_contextPath);
      }

      sb.append(path);
      sb.append('?').append(SimpleResourceFilter.GUID).append('=').append(guid);

      return sb.toString();
   }

   @Override
   public String evaluate(IResourceContext ctx, ITemplate template) throws Exception {
      if (m_serverUrlPrefix == null) {
         throw new IllegalStateException("No servlet url prefix found, please register it use @ServerUrlPrefix!");
      }

      String path = saveToJspFile(template);
      URL url = new URL(buildRequestUrl(path, buildGuid(ctx, m_ctx)));
      String result = Files.forIO().readFrom(url.openStream(), "utf-8");

      return result;
   }

   @Override
   public JspTemplateEvaluator getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return TemplateLanguage.Jsp.name();
   }

   @Override
   public Class<? super JspTemplateEvaluator> getRegisterType() {
      return ITemplateEvaluator.class;
   }

   protected String saveToJspFile(ITemplate template) throws IOException {
      String path = template.getMeta().getUrn().getPathInfo();

      if (!path.endsWith(".jsp") && !path.endsWith(".jspf")) {
         path += ".jsp";
      }

      File file = new File(m_warRoot, path);

      Files.forIO().writeTo(file, template.getContent());

      return path;
   }

   @Override
   public void setContext(ITemplateContext ctx) {
      m_ctx = ctx;
   }

   @ContextPath
   public void setContextPath(String contextPath) {
      m_contextPath = contextPath;
   }

   @ServerUrlPrefix
   public void setServerUrlPrefix(String serverUrlPrefix) {
      m_serverUrlPrefix = serverUrlPrefix;
   }

   @WarRoot
   public void setWarRoot(File warRoot) {
      m_warRoot = warRoot;
   }

   public static class MockOutputStream extends ServletOutputStream {
      private ByteArrayOutputStream m_baos;

      public MockOutputStream(ByteArrayOutputStream baos) {
         m_baos = baos;
      }

      @Override
      public void write(byte[] bytes, int off, int len) throws IOException {
         m_baos.write(bytes, off, len);
      }

      @Override
      public void write(int b) throws IOException {
         m_baos.write(b);
      }
   }

   public static class MockRequest implements HttpServletRequest {
      private Map<String, Object> m_attributes = new HashMap<String, Object>();

      private ServletContext m_ctx;

      public MockRequest(ServletContext ctx) {
         m_ctx = ctx;
      }

      @Override
      public Object getAttribute(String name) {
         return m_attributes.get(name);
      }

      @Override
      public Enumeration<String> getAttributeNames() {
         return Collections.enumeration(m_attributes.keySet());
      }

      @Override
      public String getAuthType() {
         return null;
      }

      @Override
      public String getCharacterEncoding() {
         return "utf-8";
      }

      @Override
      public int getContentLength() {
         return 0;
      }

      @Override
      public String getContentType() {
         return null;
      }

      @Override
      public String getContextPath() {
         return m_ctx.getContextPath();
      }

      @Override
      public Cookie[] getCookies() {
         return null;
      }

      @Override
      public long getDateHeader(String arg0) {
         return 0;
      }

      @Override
      public String getHeader(String arg0) {
         return null;
      }

      @Override
      public Enumeration<String> getHeaderNames() {
         return null;
      }

      @Override
      public Enumeration<String> getHeaders(String arg0) {
         return null;
      }

      @Override
      public ServletInputStream getInputStream() throws IOException {
         return null;
      }

      @Override
      public int getIntHeader(String arg0) {
         return 0;
      }

      @Override
      public String getLocalAddr() {
         return null;
      }

      @Override
      public Locale getLocale() {
         return null;
      }

      @Override
      public Enumeration<String> getLocales() {
         return null;
      }

      @Override
      public String getLocalName() {
         return null;
      }

      @Override
      public int getLocalPort() {
         return 0;
      }

      @Override
      public String getMethod() {
         return null;
      }

      @Override
      public String getParameter(String arg0) {
         return null;
      }

      @Override
      public Map<String, String[]> getParameterMap() {
         return null;
      }

      @Override
      public Enumeration<String> getParameterNames() {
         return null;
      }

      @Override
      public String[] getParameterValues(String arg0) {
         return null;
      }

      @Override
      public String getPathInfo() {
         return null;
      }

      @Override
      public String getPathTranslated() {
         return null;
      }

      @Override
      public String getProtocol() {
         return null;
      }

      @Override
      public String getQueryString() {
         return null;
      }

      @Override
      public BufferedReader getReader() throws IOException {
         return null;
      }

      @Override
      public String getRealPath(String arg0) {
         return null;
      }

      @Override
      public String getRemoteAddr() {
         return null;
      }

      @Override
      public String getRemoteHost() {
         return null;
      }

      @Override
      public int getRemotePort() {
         return 0;
      }

      @Override
      public String getRemoteUser() {
         return null;
      }

      @Override
      public RequestDispatcher getRequestDispatcher(String path) {
         return m_ctx.getRequestDispatcher(path);
      }

      @Override
      public String getRequestedSessionId() {
         return null;
      }

      @Override
      public String getRequestURI() {
         return null;
      }

      @Override
      public StringBuffer getRequestURL() {
         return null;
      }

      @Override
      public String getScheme() {
         return null;
      }

      @Override
      public String getServerName() {
         return null;
      }

      @Override
      public int getServerPort() {
         return 0;
      }

      @Override
      public String getServletPath() {
         return null;
      }

      @Override
      public HttpSession getSession() {
         return null;
      }

      @Override
      public HttpSession getSession(boolean arg0) {
         return null;
      }

      @Override
      public Principal getUserPrincipal() {
         return null;
      }

      @Override
      public boolean isRequestedSessionIdFromCookie() {
         return false;
      }

      @Override
      public boolean isRequestedSessionIdFromUrl() {
         return false;
      }

      @Override
      public boolean isRequestedSessionIdFromURL() {
         return false;
      }

      @Override
      public boolean isRequestedSessionIdValid() {
         return false;
      }

      @Override
      public boolean isSecure() {
         return false;
      }

      @Override
      public boolean isUserInRole(String arg0) {
         return false;
      }

      @Override
      public void removeAttribute(String name) {
      }

      @Override
      public void setAttribute(String name, Object value) {
         m_attributes.put(name, value);
      }

      @Override
      public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
      }
   }

   public static class MockResponse implements HttpServletResponse {
      public ByteArrayOutputStream m_baos;

      private ServletOutputStream m_out;

      private boolean m_committed;

      private String m_charset;

      private String m_contentType;

      public MockResponse() {
         m_baos = new ByteArrayOutputStream();
         m_out = new MockOutputStream(m_baos);
      }

      @Override
      public void addCookie(Cookie arg0) {
      }

      @Override
      public void addDateHeader(String arg0, long arg1) {
      }

      @Override
      public void addHeader(String arg0, String arg1) {
      }

      @Override
      public void addIntHeader(String arg0, int arg1) {
      }

      @Override
      public boolean containsHeader(String arg0) {
         return false;
      }

      @Override
      public String encodeRedirectUrl(String arg0) {
         return null;
      }

      @Override
      public String encodeRedirectURL(String arg0) {
         return null;
      }

      @Override
      public String encodeUrl(String arg0) {
         return null;
      }

      @Override
      public String encodeURL(String arg0) {
         return null;
      }

      @Override
      public void flushBuffer() throws IOException {
         m_out.flush();
      }

      @Override
      public int getBufferSize() {
         return 8192;
      }

      @Override
      public String getCharacterEncoding() {
         return m_charset;
      }

      @Override
      public String getContentType() {
         return m_contentType;
      }

      @Override
      public Locale getLocale() {
         return Locale.US;
      }

      public String getOutput() {
         if (m_charset != null) {
            try {
               return m_baos.toString(m_charset);
            } catch (UnsupportedEncodingException e) {
            }
         }

         return m_baos.toString();
      }

      @Override
      public ServletOutputStream getOutputStream() throws IOException {
         return m_out;
      }

      @Override
      public PrintWriter getWriter() throws IOException {
         return new PrintWriter(m_out);
      }

      @Override
      public boolean isCommitted() {
         return m_committed;
      }

      @Override
      public void reset() {
         resetBuffer();
      }

      @Override
      public void resetBuffer() {
         m_baos.reset();
      }

      @Override
      public void sendError(int arg0) throws IOException {
      }

      @Override
      public void sendError(int arg0, String arg1) throws IOException {
      }

      @Override
      public void sendRedirect(String arg0) throws IOException {
      }

      @Override
      public void setBufferSize(int arg0) {
      }

      @Override
      public void setCharacterEncoding(String charset) {
         m_charset = charset;
      }

      public void setCommitted(boolean committed) {
         m_committed = committed;
      }

      @Override
      public void setContentLength(int arg0) {
      }

      @Override
      public void setContentType(String contentType) {
         int pos = contentType.toLowerCase().indexOf("charset=");

         if (pos > 0) {
            m_charset = contentType.substring(pos + "charset=".length()).trim();
         }

         m_contentType = contentType;
      }

      @Override
      public void setDateHeader(String arg0, long arg1) {
      }

      @Override
      public void setHeader(String arg0, String arg1) {
      }

      @Override
      public void setIntHeader(String arg0, int arg1) {
      }

      @Override
      public void setLocale(Locale locale) {
      }

      @Override
      public void setStatus(int arg0) {
      }

      @Override
      public void setStatus(int arg0, String arg1) {
      }
   }
}