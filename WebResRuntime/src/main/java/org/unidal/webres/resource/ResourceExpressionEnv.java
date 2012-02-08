package org.unidal.webres.resource;

import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.expression.IResourceExpression;
import org.unidal.webres.resource.expression.IResourceExpressionEnv;
import org.unidal.webres.resource.expression.IResourceExpressionFactory;
import org.unidal.webres.resource.expression.IResourcePropertyEvaluator;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceResolver;

public class ResourceExpressionEnv implements IResourceExpressionEnv {
   private IResourceContext m_ctx;

   public ResourceExpressionEnv(IResourceContext ctx) {
      m_ctx = ctx;
   }

   @Override
   public void err(String message) {
      System.err.println(message);
   }

   @Override
   @SuppressWarnings("unchecked")
   public IResourceExpressionFactory<? extends IResourceExpression<?, ?>> getExpressionFactory(String resourceType) {
      return m_ctx.lookup(IResourceExpressionFactory.class, resourceType);
   }

   @Override
   public IResourceContext getResourceContext() {
      return m_ctx;
   }

   @Override
   public IResourcePropertyEvaluator<?> getResourcePropertyEvaluator(IResourceType resourceType, String property) {
      return m_ctx.lookup(IResourcePropertyEvaluator.class, resourceType.getName() + ":" + property);
   }

   @Override
   public boolean isResourceNamespace(String resourceType, String namespace) {
      String key = resourceType + "." + namespace;
      IResourceResolver<?, ?> resolver = m_ctx.lookup(IResourceResolver.class, key);

      return resolver != null && !resolver.getNamespace().isVirtual();
   }

   @Override
   public boolean isResourceType(String resourceType) {
      SystemResourceType type = SystemResourceType.getByName(resourceType);

      return type != null;
   }
}
