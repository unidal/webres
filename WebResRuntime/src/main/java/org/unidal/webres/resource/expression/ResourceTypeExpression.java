package org.unidal.webres.resource.expression;

public class ResourceTypeExpression extends BaseResourceExpression<NamespaceExpression, Object> {
   private Cache<NamespaceExpression> m_cache = new Cache<NamespaceExpression>(8);

   public ResourceTypeExpression(IResourceExpressionEnv env, ResourceExpression parent, String key) {
      super(env, parent, key);
   }

   @Override
   protected NamespaceExpression createChild(String key) {
      IResourceExpressionEnv env = getEnv();

      if (env.isResourceNamespace(getKey(), key)) {
         return new NamespaceExpression(env, this, key);
      } else {
         env.err(String.format("Namespace(%s) is not supported for resource type(%s) in EL!", key, getKey()));
         return null;
      }
   }

   @Override
   public Object evaluate() {
      // returning null means it can't be evaluated without more information
      return null;
   }

   @Override
   protected Cache<NamespaceExpression> getCache() {
      return m_cache;
   }

   @Override
   protected String getDefaultProperty() {
      // no default property
      return null;
   }

   @Override
   protected void prepareUrn(Urn urn) {
      urn.setResourceType(getKey());
   }

}
