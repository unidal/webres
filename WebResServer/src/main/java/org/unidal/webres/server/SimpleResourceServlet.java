package org.unidal.webres.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.unidal.webres.helper.Files;
import org.unidal.webres.helper.Splitters;
import org.unidal.webres.resource.ResourceFactory;
import org.unidal.webres.resource.ResourceUrn;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.resource.api.IImage;
import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.api.IResource;
import org.unidal.webres.resource.api.IResourceRef;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.css.CssFactory;
import org.unidal.webres.resource.js.JsFactory;
import org.unidal.webres.resource.runtime.ResourceInitializer;
import org.unidal.webres.resource.runtime.ResourceRuntime;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;

/**
 *  Stand-alone resource servlet for dev mode purpose.<p>
 *
 *  No cache configured and only local file based resource repository will be used.<p>
 *  
 *  This servlet needs to be configured in web.xml with following url-mapping:
 *  <xmp>
 *  <servlet>
 *     <servlet-name>ResourceServlet</servlet-name>
 *     <display-name>ResourceServlet</display-name>
 *     <servlet-class>org.unidal.webres.server.SimpleResourceServlet</servlet-class>
 *     <load-on-startup>1</load-on-startup>
 *  </servlet>
 *  <servlet-mapping>
 *     <servlet-name>ResourceServlet</servlet-name>
 *     <url-pattern>/z/*</url-pattern>
 *  </servlet-mapping>
 *  <servlet-mapping>
 *     <servlet-name>ResourceServlet</servlet-name>
 *     <url-pattern>/f/*</url-pattern>
 *  </servlet-mapping>
 *  </xmp>
 */
public class SimpleResourceServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;

   private File m_warRoot;

   @SuppressWarnings("unchecked")
   protected <T extends IResourceRef<?>> List<T> buildResourceRefs(String value) {
      List<String> urns = Splitters.by('|').split(value);
      List<T> refs = new ArrayList<T>(urns == null ? 0 : urns.size());

      if (urns != null) {
         for (String urn : urns) {
            IResourceUrn u = ResourceUrn.parse(urn);
            IResourceRef<?> ref = ResourceFactory.forRef().createRef(u.getResourceTypeName(), u.getNamespace(),
                  u.getPathInfo());

            refs.add((T) ref);
         }
      }

      return refs;
   }

   protected void handleAggregation(HttpServletRequest req, HttpServletResponse res) throws IOException {
      String pathInfo = req.getPathInfo();
      int pos1 = pathInfo.indexOf('/', 1);
      String type;

      if (pos1 > 0) {
         type = pathInfo.substring(1, pos1);
      } else {
         type = pathInfo.substring(1);
      }

      if ("js".equals(type)) {
         try {
            List<IJsRef> refs = buildResourceRefs(req.getParameter("urns"));
            IJsRef ref = JsFactory.forRef().createAggregatedRef("/js", refs);

            handleResource(req, res, ref);
         } catch (Exception e) {
            handleException(req, res, e);
         }
      } else if ("css".equals(type)) {
         try {
            List<ICssRef> refs = buildResourceRefs(req.getParameter("urns"));
            ICssRef ref = CssFactory.forRef().createAggregatedRef("/css", refs);

            handleResource(req, res, ref);
         } catch (Exception e) {
            handleException(req, res, e);
         }
      } else {
         throw new UnsupportedOperationException(String.format(
               "Unknown type(%s), only 'js', 'css' and 'json' are allowed!", type));
      }
   }

   protected void handleException(HttpServletRequest req, HttpServletResponse res, Exception e) throws IOException {
      String pattern = "Error when handling fragment request(%s)! <br><br>Exception:<pre>%s</pre>";
      PrintWriter pw = new PrintWriter(new StringWriter());

      e.printStackTrace(pw);
      e.printStackTrace();

      showPage404(req, res, String.format(pattern, req.getRequestURI(), pw.toString()));
   }

   protected void handleFragment(HttpServletRequest req, HttpServletResponse res) throws IOException {
      String pathInfo = req.getPathInfo();
      int pos1 = pathInfo.indexOf('/', 1);
      int pos2 = pathInfo.indexOf('/', pos1 + 1);

      if (pos1 > 0 && pos2 > pos1) {
         String resourceType = pathInfo.substring(1, pos1);
         String namespace = pathInfo.substring(pos1 + 1, pos2);
         String path = pathInfo.substring(pos2);

         try {
            IResourceRef<?> ref = ResourceFactory.forRef().createRef(resourceType, namespace, path);

            handleResource(req, res, ref);
         } catch (Exception e) {
            handleException(req, res, e);
         }
      }
   }

   protected void handleLocalFile(HttpServletRequest req, HttpServletResponse res) throws IOException {
      String requestUri = req.getRequestURI();
      String contextPath = req.getContextPath();
      File file;

      if (contextPath == null) {
         file = new File(m_warRoot, requestUri);
      } else {
         file = new File(m_warRoot, requestUri.substring(contextPath.length()));
      }

      if (file.isFile()) {
         try {
            byte[] content = Files.forIO().readFrom(file);

            if (file.getPath().endsWith(".js")) {
               renderPage(res, 200, "application/x-javascript", content);
            } else if (file.getPath().endsWith(".css")) {
               renderPage(res, 200, "text/css", content);
            } else if (file.getPath().endsWith(".svg")) {
               renderPage(res, 200, "images/svg-xml", content);
            }
         } catch (Exception e) {
            handleException(req, res, e);
         }
      }
   }

   protected void handleResource(HttpServletRequest req, HttpServletResponse res, IResourceRef<?> ref)
         throws IOException {
      ResourceRuntimeContext.setup(getServletContext().getContextPath());

      IResource<?, ?> resource = ref.resolve(ResourceRuntimeContext.ctx().getResourceContext());

      if (ref.getResourceType() == SystemResourceType.Js) {
         IJs js = (IJs) resource;

         renderPage(res, 200, "application/x-javascript", js.getContent());
      } else if (ref.getResourceType() == SystemResourceType.Css) {
         ICss css = (ICss) resource;

         renderPage(res, 200, "text/css", css.getContent());
      } else if (ref.getResourceType() == SystemResourceType.Image) {
         IImage image = (IImage) resource;

         renderPage(res, 200, image.getMeta().getMimeType(), image.getContent());
      } else {
         throw new UnsupportedOperationException(
               String.format("Resource type(%s) is not supported yet, please contact administrator!", ref
                     .getResourceType().getName()));
      }
   }

   @Override
   public void init() throws ServletException {
      ServletContext servletContext = getServletContext();
      String contextPath = servletContext.getContextPath();
      File warRoot = new File(servletContext.getRealPath("/"));

      if (!ResourceRuntime.INSTANCE.hasConfig(contextPath)) {
         ResourceInitializer.initialize(contextPath, warRoot);
      }

      m_warRoot = warRoot;
   }

   protected void renderPage(HttpServletResponse res, int status, String contextType, byte[] content)
         throws IOException {
      res.setStatus(status);

      if (contextType != null) {
         res.setContentType(contextType);
      }

      res.setContentLength(content.length);
      res.getOutputStream().write(content);
      res.flushBuffer();
   }

   protected void renderPage(HttpServletResponse res, int status, String contextType, String content)
         throws IOException {
      String charset = "utf-8";
      byte[] data = content.getBytes(charset);

      renderPage(res, status, contextType + "; charset=" + charset, data);
   }

   @Override
   public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      String servletPath = req.getServletPath();
      String pathInfo = req.getPathInfo();

      if (pathInfo == null) {
         showPageWelcome(req, res);
      } else if (servletPath.equals("/f")) {
         handleFragment(req, res);
      } else if (servletPath.equals("/z")) {
         handleAggregation(req, res);
      } else { // fallback
         handleLocalFile(req, res);
      }

      if (!res.isCommitted()) {
         showPage404(req, res, "Bad request - " + req.getRequestURI());
      }
   }

   protected void showPage404(HttpServletRequest req, HttpServletResponse res, String message) throws IOException {
      renderPage(res, 404, "text/html", message);
   }

   protected void showPageWelcome(HttpServletRequest req, HttpServletResponse res) throws IOException {
      ServletContext ctx = getServletContext();
      String contextPath = ctx.getContextPath();
      File warRoot = new File(ctx.getRealPath("/"));
      String message = String.format("Welcome to Resource Servlet! <br><br>War Root: %s<br>Context Path: %s",
            warRoot.getCanonicalPath(), contextPath);

      renderPage(res, 200, "text/html", message);
   }
}
