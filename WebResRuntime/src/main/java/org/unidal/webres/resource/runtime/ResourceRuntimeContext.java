package org.unidal.webres.resource.runtime;

import java.util.HashMap;
import java.util.Map;

import org.unidal.webres.resource.ResourceContext;
import org.unidal.webres.resource.ResourceExpressionEnv;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.aggregation.IResourceAggregator;
import org.unidal.webres.resource.aggregation.IResourceAggregatorFactory;
import org.unidal.webres.resource.api.IResourceRef;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.expression.IResourceExpressionEnv;
import org.unidal.webres.resource.injection.ResourceContainer;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.spi.IResourceContainer;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegistry;
import org.unidal.webres.resource.spi.IResourceTokenStorage;
import org.unidal.webres.resource.variation.entity.ResourceVariation;

public class ResourceRuntimeContext {
   private static ThreadLocal<ResourceRuntimeContext> s_ctx = new ThreadLocal<ResourceRuntimeContext>() {
      @Override
      protected ResourceRuntimeContext initialValue() {
         return new ResourceRuntimeContext();
      }
   };

   private IResourceRegistry m_registry;

   private IResourceContainer m_container;

   private ResourceRuntimeConfig m_config;

   private IResourceContext m_resourceContext;

   private boolean m_initialized;

   private Boolean m_deferRendering;

   private String m_pageId;

   private Map<IResourceType, Root> m_models = new HashMap<IResourceType, Root>();

   private Map<IResourceType, IResourceAggregator<?>> m_aggregators = new HashMap<IResourceType, IResourceAggregator<?>>();

   private Map<IResourceType, String> m_ajaxTokens = new HashMap<IResourceType, String>();

   private ResourceRuntimeContext() {
   }

   public static ResourceRuntimeContext ctx() {
      ResourceRuntimeContext ctx = s_ctx.get();

      if (!ctx.m_initialized) {
         throw new IllegalStateException("Please call ResourceRuntimeContext.setup(contextPath) first!");
      }

      return ctx;
   }

   public static void reset() {
      ResourceRuntimeContext ctx = s_ctx.get();

      if (ctx.m_initialized) {
         ctx.m_models.clear();
         ctx.m_config = null;
         ctx.m_container = null;
         ctx.m_registry = null;
         ctx.m_initialized = false;
         ctx.m_aggregators.clear();
         ctx.m_ajaxTokens.clear();
         ctx.m_pageId = null;
         ctx.m_deferRendering = null;
         ctx.m_resourceContext = null;
      }
   }

   public static void setup(String contextPath) {
      ResourceRuntimeContext ctx = s_ctx.get();

      if (!ctx.m_initialized) {
         ResourceRuntimeConfig config = ResourceRuntime.INSTANCE.getConfig(contextPath);
         ResourceContainer container = new ResourceContainer(config.getContainer());
         ResourceRegistry registry = new ResourceRegistry(container);
         ResourceContext context = new ResourceContext(config.getResourceContext(), registry);

         ctx.m_config = config;
         ctx.m_container = container;
         ctx.m_registry = registry;
         ctx.m_initialized = true;
         ctx.m_resourceContext = context;

         container.setAttribute(ResourceRuntimeContext.class, ctx);
         container.setAttribute(IResourceContext.class, context);
         container.setAttribute(IResourceExpressionEnv.class, new ResourceExpressionEnv(context));

         // warm up aggregators
         ctx.getResourceAggregator(SystemResourceType.Js);
         ctx.getResourceAggregator(SystemResourceType.Css);
         ctx.getResourceAggregator(SystemResourceType.Image);

         registry.lock();
      }
   }

   public void applyProfile() {
      for (IResourceAggregator<?> aggregator : m_aggregators.values()) {
         aggregator.applyProfile();
      }
   }

   public String getAjaxToken(IResourceType resourceType) {
      String token = m_ajaxTokens.get(resourceType);

      if (token == null) {
         throw new RuntimeException(String.format("Ajax token for resource(%s) is not available at this time!",
               resourceType.getName()));
      } else {
         return token;
      }
   }

   public ResourceRuntimeConfig getConfig() {
      return m_config;
   }

   public IResourceContainer getContainer() {
      return m_container;
   }

   public Page getPage(IResourceType resourceType) {
      Root model = m_models.get(resourceType);

      if (model == null) {
         model = new Root();
         m_models.put(resourceType, model);
      }

      Page page = model.findPage(m_pageId);

      if (page == null) {
         page = new Page(m_pageId);
         model.addPage(page);
      }

      return page;
   }

   public IResourceRegistry getRegistry() {
      return m_registry;
   }

   @SuppressWarnings("unchecked")
   public <T extends IResourceAggregator<? extends IResourceRef<?>>> T getResourceAggregator(IResourceType resourceType) {
      IResourceAggregator<?> aggregator = m_aggregators.get(resourceType);

      if (aggregator == null) {
         IResourceAggregatorFactory factory = m_registry.lookup(IResourceAggregatorFactory.class);

         aggregator = factory.createAggregator(m_pageId, resourceType);
         m_aggregators.put(resourceType, aggregator);
      }

      return (T) aggregator;
   }

   public IResourceContext getResourceContext() {
      return m_resourceContext;
   }

   public ResourceVariation getResourceVariation() {
      return m_config.getResourceVariation();
   }

   public Boolean getDeferRendering() {
      return m_deferRendering;
   }

   public boolean isDeferRendering() {
      return m_deferRendering != null && m_deferRendering.booleanValue();
   }

   public String prepareAjaxToken(IResourceType resourceType) {
      IResourceTokenStorage storage = m_container.getAttribute(IResourceTokenStorage.class);
      ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();
      IResourceAggregator<? extends IResourceRef<?>> aggregator = ctx.getResourceAggregator(resourceType);
      String token = storage.storeResourceUrns(aggregator.getResourceUrns());

      m_ajaxTokens.put(resourceType, token);
      return token;
   }

   public void setAjaxDedupToken(IResourceType resourceType, String token) {
      if (token != null && token.length() > 0) {
         m_container.setAttribute(String.class, "parent.token." + resourceType.getName(), token);
      }
   }

   public void setDeferRendering(Boolean deferRendering) {
      m_deferRendering = deferRendering;
   }

   public void setPageId(String pageId) {
      m_pageId = pageId;
   }
}