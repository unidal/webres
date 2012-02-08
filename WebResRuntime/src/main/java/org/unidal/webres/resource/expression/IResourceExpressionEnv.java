package org.unidal.webres.resource.expression;

import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.spi.IResourceContext;

public interface IResourceExpressionEnv {
   public boolean isResourceType(String resourceType);

   public void err(String message);

   public boolean isResourceNamespace(String resourceType, String namespace);

   public IResourceExpressionFactory<? extends IResourceExpression<?, ?>> getExpressionFactory(String resourceType);

   public IResourceContext getResourceContext();

   public IResourcePropertyEvaluator<?> getResourcePropertyEvaluator(IResourceType resourceType, String property);
}
