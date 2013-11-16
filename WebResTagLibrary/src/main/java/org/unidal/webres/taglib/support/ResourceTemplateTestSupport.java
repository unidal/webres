package org.unidal.webres.taglib.support;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.ServletHolder;
import org.unidal.helper.Files;
import org.unidal.webres.resource.ResourceContext;
import org.unidal.webres.resource.WarConstant;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.runtime.ResourceInitializer;
import org.unidal.webres.resource.runtime.ResourceRuntime;
import org.unidal.webres.resource.runtime.ResourceRuntimeConfig;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegistry;
import org.unidal.webres.tag.build.TldGenerator;
import org.unidal.webres.tag.core.BaseTagLibDefinition;
import org.unidal.webres.tag.resource.ResourceTagConfigurator;
import org.unidal.webres.taglib.basic.ResourceTagLibConfigurator;
import org.unidal.webres.taglib.basic.ResourceTagLibDefinition;

public abstract class ResourceTemplateTestSupport extends JettyTestSupport {
   private static MockResourceFilter s_resourceFilter;

   private IResourceRegistry s_registry;

   @After
   public void after() {
      ResourceRuntimeContext.reset();
   }

   @Before
   public void before() {
      ResourceRuntimeContext.setup(getContextPath());
   }

   protected void checkJsp(String jspPath) throws Exception {
      String expected = getResource(jspPath + ".html");

      checkPath(jspPath, expected);
   }

   @Override
   protected void configure() throws Exception {
      super.configure();

      generateTldFile("webres", ResourceTagLibDefinition.class);

      String contextPath = getContextPath();

      ResourceRuntime.INSTANCE.removeConfig(contextPath);
      ResourceInitializer.initialize(contextPath, getWarRoot());
      ResourceRuntimeConfig config = ResourceRuntime.INSTANCE.getConfig(contextPath);
      IResourceRegistry registry = config.getRegistry();

      registry.register(String.class, WarConstant.ServerUrlPrefix, "http://localhost:" + getServerPort());

      s_registry = registry;
   }

   @Override
   protected ServletHolder configureJsp(Context ctx) throws Exception {
      MockResourceFilter filter = new MockResourceFilter(getContextPath());

      s_resourceFilter = filter;
      ctx.addFilter(new FilterHolder(filter), "*.jsp", Handler.ALL);
      return super.configureJsp(ctx);
   }

   protected void disableDeferRendering() {
      s_resourceFilter.setDeferRendering(false);
   }

   protected void enableDeferRendering() {
      s_resourceFilter.setDeferRendering(true);
   }

   protected void generateTldFile(String name, Class<? extends BaseTagLibDefinition> tldClass) {
      File webInf = new File(getWarRoot(), "WEB-INF");

      webInf.mkdirs();

      System.out.print(String.format("[%s] ", getTimestamp()));
      new TldGenerator().generateTldFile(webInf, name, tldClass.getName(), Collections.<Exception> emptyList(), null);
   }

   public IResourceRegistry getRegistry() {
      return s_registry;
   }

   protected String getResource(String resource) throws IOException {
      File html = new File(getWarRoot(), resource);
      String content = Files.forIO().readFrom(html, "utf-8");

      return content;
   }

   @Override
   protected void postConfigure(Context ctx) {
      super.postConfigure(ctx);

      new ResourceTagConfigurator().configure(s_registry);
      new ResourceTagLibConfigurator().configure(s_registry);

      s_registry.lock();
   }

   protected void setAjaxDedupToken(IResourceType resourceType, String token) {
      s_resourceFilter.setAjaxDedupToken(resourceType, token);
   }

   /**
    * Jetty will create a new thread to serve every incoming JSP request, so 
    * ResourceRuntimeContext setup can't be done at @Before method. <p>
    */
   protected static class MockResourceFilter implements Filter {
      private String m_contextPath;

      private Boolean m_deferRendering;

      private Map<IResourceType, String> m_ajaxTokens = new HashMap<IResourceType, String>();

      public MockResourceFilter(String contextPath) {
         m_contextPath = contextPath;
      }

      @Override
      public void destroy() {
      }

      protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
         ResourceRuntimeContext.setup(m_contextPath);

         try {
            ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();
            IResourceRegistry registry = ctx.getRegistry();

            ctx.getContainer().setAttribute(IResourceContext.class, new ResourceContext(registry));
            ctx.setDeferRendering(m_deferRendering);

            for (Map.Entry<IResourceType, String> e : m_ajaxTokens.entrySet()) {
               ctx.setAjaxDedupToken(e.getKey(), e.getValue());
            }

            registry.lock();

            chain.doFilter(request, response);
         } finally {
            m_ajaxTokens.clear();
            ResourceRuntimeContext.reset();
         }
      }

      @Override
      public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {
         if (req instanceof HttpServletRequest && res instanceof HttpServletResponse) {
            doFilter((HttpServletRequest) req, (HttpServletResponse) res, chain);
         } else {
            chain.doFilter(req, res);
         }
      }

      @Override
      public void init(FilterConfig filterConfig) throws ServletException {
      }

      public void setAjaxDedupToken(IResourceType resourceType, String token) {
         m_ajaxTokens.put(resourceType, token);
      }

      public void setDeferRendering(boolean deferRendering) {
         m_deferRendering = deferRendering;
      }
   }
}
