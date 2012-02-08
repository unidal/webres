package org.unidal.webres.resource.expression;

public class NamespaceExpression extends BaseResourceExpression<IResourceExpression<?, ?>, Object> {
   public NamespaceExpression(IResourceExpressionEnv env, ResourceTypeExpression parent, String key) {
      super(env, parent, key);
   }

   @Override
   @SuppressWarnings("unchecked")
   protected IResourceExpression<?, ?> createChild(String key) {
      String resourceType = getParent().getKey();
      IResourceExpressionFactory<BaseResourceExpression<?, ?>> factory = (IResourceExpressionFactory<BaseResourceExpression<?, ?>>) getEnv()
            .getExpressionFactory(resourceType);

      if (factory == null) {
         throw new RuntimeException(String.format("No IResourceExpressionFactory is registered for resource type(%s)!",
               resourceType));
      }

      IResourceExpression<?, ?> child = factory.create(this, key);

      return child;
   }

   @Override
   public Object evaluate() {
      // returning null means it can't be evaluated without more information
      return null;
   }

   @Override
   protected String getDefaultProperty() {
      // no default property
      return null;
   }

   @Override
   protected void prepareUrn(Urn urn) {
      urn.setNamespace(getKey());
   }
}
