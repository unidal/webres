package org.unidal.webres.resource.expression;

public class ResourceExpression extends BaseResourceExpression<ResourceTypeExpression, Object> {
   private Cache<ResourceTypeExpression> m_cache = new Cache<ResourceTypeExpression>(8);

   public ResourceExpression(IResourceExpressionEnv env) {
      this(env, "res");
   }

   public ResourceExpression(IResourceExpressionEnv env, String name) {
      super(env, null, name);
   }

   @Override
   protected ResourceTypeExpression createChild(String resourceType) {
      IResourceExpressionEnv env = getEnv();

      if (env.isResourceType(resourceType)) {
         return new ResourceTypeExpression(env, this, resourceType);
      } else {
         env.err(String.format("Resource type(%s) is not defined!", resourceType));
         return null;
      }
   }

   @Override
   public Object evaluate() {
      // returning null means it can't be evaluated without more information
      return null;
   }

   @Override
   protected Cache<ResourceTypeExpression> getCache() {
      return m_cache;
   }

   @Override
   protected String getDefaultProperty() {
      // no default property
      return null;
   }

   @Override
   protected void prepareUrn(Urn urn) {
      // do nothing here
   }
}
