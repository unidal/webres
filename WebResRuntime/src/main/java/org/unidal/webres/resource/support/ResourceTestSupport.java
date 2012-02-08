package org.unidal.webres.resource.support;

import org.junit.After;
import org.junit.Before;

import org.unidal.webres.resource.expression.IResourceExpressionEnv;
import org.unidal.webres.resource.runtime.ResourceConfigurator;
import org.unidal.webres.resource.runtime.ResourceInitializer;
import org.unidal.webres.resource.runtime.ResourceRuntime;
import org.unidal.webres.resource.runtime.ResourceRuntimeConfig;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceRegistry;

/**
 * Base integration test class in Jetty with Servlet and JSP support. <p>
 */
public abstract class ResourceTestSupport extends WebAppTestSupport {
   private static IResourceRegistry s_registry;
   private IResourceExpressionEnv m_env;

   protected static ResourceTestSupport setup(WebAppTestSupport instance) throws Exception {
      instance.configure();

      s_registry.lock();
      return (ResourceTestSupport) instance;
   }

   @After
   public void after() {
      ResourceRuntimeContext.reset();
   }

   @Before
   public void before() {
      ResourceRuntimeContext.setup(getContextPath());

      m_env = ResourceRuntimeContext.ctx().getContainer().getAttribute(IResourceExpressionEnv.class);
   }

   @Override
   protected void configure() throws Exception {
      super.configure();

      String contextPath = getContextPath();

      if (contextPath != null && !contextPath.startsWith("/")) {
         throw new RuntimeException(String.format("ContextPath(%s) must be null or starting with '/'.", contextPath));
      }

      ResourceRuntime.INSTANCE.removeConfig(contextPath);
      ResourceInitializer.initialize(contextPath, getWarRoot());

      ResourceRuntimeConfig config = ResourceRuntime.INSTANCE.getConfig(contextPath);
      IResourceRegistry registry = config.getRegistry();

      new ResourceConfigurator().configure(registry);

      s_registry = registry;
   }

   protected IResourceExpressionEnv getExpressionEnv() {
      return m_env;
   }

   public IResourceRegistry getRegistry() {
      return s_registry;
   }
}
