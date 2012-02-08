package org.unidal.webres.taglib.support;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.jasper.servlet.JspServlet;
import org.junit.Assert;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.ServletHolder;

import org.unidal.webres.helper.Files;
import org.unidal.webres.resource.support.WebAppTestSupport;

/**
 * Base integration test class in Jetty with Servlet and JSP support. <p>
 */
public abstract class JettyTestSupport extends WebAppTestSupport {
   private static JettyTestSupport s_instance;

   private static AttributeInjectFilter s_filter;

   private static Context s_ctx;

   private Server m_server;

   protected static Context getContext() {
      return s_ctx;
   }

   protected static void shutdownServer() throws Exception {
      if (s_instance != null) {
         s_instance.shutdown();
         s_instance = null;
      }
   }

   protected static JettyTestSupport startServer(JettyTestSupport instance) throws Exception {
      instance.configure();
      instance.copyResources();
      instance.startServer(getContext());
      instance.postConfigure(getContext());

      s_instance = instance;
      return instance;
   }

   protected String checkJspWithSource(String path, String source, String expected) throws IOException {
      return checkJspWithSource(path, source, expected, null, null);
   }

   protected String checkJspWithSource(String path, String source, String expected, String qs, Map<String, String> expectedHeaders)
         throws IOException {
      File file = new File(getWarRoot(), path);

      Files.forIO().writeTo(file, source);

      String pathInfo;

      if (getContextPath() != null) {
         pathInfo = new File(getContextPath(), path).getPath().replace('\\', '/');
      } else {
         pathInfo = '/' + path;
      }

      return checkPath(pathInfo, qs, expected, expectedHeaders);
   }

   protected String checkPath(String pathInfo, String expectedContent) throws IOException {
      return checkPath(pathInfo, null, expectedContent, null);
   }

   protected String checkPath(String pathInfo, String qs, String expectedContent, Map<String, String> expectedHeaders)
         throws IOException {
      URL url = new URL(String.format("http://localhost:%s%s%s", getServerPort(), pathInfo, qs == null ? "" : "?" + qs));
      URLConnection conn = url.openConnection();

      String actual = Files.forIO().readFrom(conn.getInputStream(), "utf-8");

      if (expectedContent != null) {
         try {
            Assert.assertEquals(expectedContent, actual);
         } catch (AssertionError e) {
            expectedContent = expectedContent.replaceAll("\r\n", "\n");
            actual = actual == null ? null : actual.replaceAll("\r\n", "\n");

            Assert.assertEquals(expectedContent, actual);
         }
      }

      if (expectedHeaders != null) {
         for (Map.Entry<String, String> e : expectedHeaders.entrySet()) {
            String key = e.getKey();
            boolean negative = key.startsWith("!");

            if (negative) {
               key = key.substring(1);

               String value = conn.getHeaderField(key);

               Assert.assertTrue(String.format("Response header(%s) was found.", key), value == null);
            } else {
               String ev = e.getValue();
               String av = conn.getHeaderField(key);

               if (av == null) {
                  Assert.fail(String.format("Response header(%s) not found.", key));
               } else if (ev != null) {
                  Assert.assertEquals(String.format("Response header(%s: %s) not found.", key, ev), ev, av);
               }
            }
         }
      }

      return actual;
   }

   protected void configure() throws Exception {
      super.configure();

      Context ctx = new Context(Context.SESSIONS);
      String contextPath = getContextPath();

      if (contextPath != null) {
         if (contextPath.length() == 0) {
            contextPath = null;
         } else if (!contextPath.startsWith("/")) {
            throw new RuntimeException(String.format("ContextPath(%s) must be null or starting with '/'.", contextPath));
         }
      }

      ctx.setResourceBase(getWarRoot().getPath());
      ctx.setContextPath(contextPath == null ? "/" : contextPath);

      configureJsp(ctx);
      s_ctx = ctx;
   }

   protected ServletHolder configureJsp(Context ctx) throws Exception {
      ServletHolder jsp = ctx.addServlet(JspServlet.class, "*.jsp");
      String scratchDir = getScratchDir().getCanonicalPath();

      if (scratchDir != null) {
         jsp.setInitParameter("scratchdir", scratchDir);
      }

      if (isKeepGenerated()) {
         jsp.setInitParameter("keepgenerated", "true");
      }

      jsp.setInitParameter("genStringAsCharArray", "true");

      return jsp;
   }

   @Override
   protected JettyTestSupport copyResourceFrom(String... resourceBases) {
      super.copyResourceFrom(resourceBases);
      return this;
   }

   protected File getScratchDir() {
      File work = new File(System.getProperty("java.io.tmpdir", "."), "Work");

      work.mkdirs();
      return work;
   }

   protected Server getServer() {
      return m_server;
   }

   protected int getServerPort() {
      return 3000;
   }

   protected boolean isKeepGenerated() {
      return false;
   }

   protected Map<String, String> map(String... values) {
      Map<String, String> map = new LinkedHashMap<String, String>();
      int len = values.length;

      if (len % 2 != 0) {
         throw new RuntimeException("Parameters of toMap() should be paired!");
      }

      for (int i = 0; i < len; i += 2) {
         String key = values[i];
         String value = values[i + 1];

         map.put(key, value);
      }

      return map;
   }

   /**
    * ServletContext will only be ready when Jetty server started.
    */
   protected void postConfigure(Context ctx) {
      AttributeInjectFilter filter = new AttributeInjectFilter(ctx.getServletContext());

      ctx.addFilter(new FilterHolder(filter), "/*", Handler.ALL);
      s_filter = filter;
   }

   protected void setAttribute(String name, Object value) {
      s_filter.setRequestAttribute(name, value);
   }

   protected void setGlobalAttribute(String name, Object value) {
      s_filter.setGlobalAttribute(name, value);
   }

   protected void setSessionAttribute(String name, Object value) {
      s_filter.setSessionAttribute(name, value);
   }

   protected void shutdown() throws Exception {
      if (m_server != null) {
         m_server.stop();
      }
   }

   protected void startServer(Context ctx) throws Exception {
      Server server = new Server(getServerPort());

      server.setStopAtShutdown(true);
      server.setHandler(ctx);
      server.start();

      m_server = server;
   }

   @Override
   protected JettyTestSupport syncFiles(String from, String to) {
      super.syncFiles(from, to);
      return this;
   }

   @Override
   protected JettyTestSupport wrapIntoJar(String targetJar, String... sourceDirs) {
      super.wrapIntoJar(targetJar, sourceDirs);
      return this;
   }

   protected static class AttributeInjectFilter implements Filter {
      private Map<String, Object> m_sessionAttributes = new HashMap<String, Object>();

      private Map<String, Object> m_requestAttributes = new HashMap<String, Object>();

      private ServletContext m_servletContext;

      public AttributeInjectFilter(ServletContext servletContext) {
         m_servletContext = servletContext;
      }

      @Override
      public void destroy() {
      }

      protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException,
            ServletException {
         if (!m_sessionAttributes.isEmpty()) {
            HttpSession session = request.getSession(true);

            for (Map.Entry<String, Object> e : m_sessionAttributes.entrySet()) {
               session.setAttribute(e.getKey(), e.getValue());
            }
         }

         for (Map.Entry<String, Object> e : m_requestAttributes.entrySet()) {
            m_servletContext.setAttribute(e.getKey(), e.getValue());
         }

         try {
            chain.doFilter(request, response);
         } finally {
            m_sessionAttributes.clear();
            m_requestAttributes.clear();
         }
      }

      @Override
      public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException,
            ServletException {
         if (req instanceof HttpServletRequest && res instanceof HttpServletResponse) {
            doFilter((HttpServletRequest) req, (HttpServletResponse) res, chain);
         } else {
            chain.doFilter(req, res);
         }
      }

      @Override
      public void init(FilterConfig filterConfig) throws ServletException {
      }

      public void setGlobalAttribute(String name, Object value) {
         m_servletContext.setAttribute(name, value);
      }

      public void setRequestAttribute(String name, Object value) {
         m_requestAttributes.put(name, value);
      }

      public void setSessionAttribute(String name, Object value) {
         m_sessionAttributes.put(name, value);
      }
   }
}
