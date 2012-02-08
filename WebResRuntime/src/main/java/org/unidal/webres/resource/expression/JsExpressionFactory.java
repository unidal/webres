package org.unidal.webres.resource.expression;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.spi.IResourceRegisterable;

public class JsExpressionFactory implements IResourceExpressionFactory<BaseResourceExpression<?, ?>>,
      IResourceRegisterable<JsExpressionFactory> {
   @Override
   public IResourceExpression<?, ?> create(BaseResourceExpression<?, ?> parent, String key) {
      return new JsExpression(parent.getEnv(), parent, key);
   }

   @Override
   public JsExpressionFactory getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return SystemResourceType.Js.getName();
   }

   @Override
   public Class<? super JsExpressionFactory> getRegisterType() {
      return IResourceExpressionFactory.class;
   }
}
