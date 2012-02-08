package org.unidal.webres.resource.expression;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.spi.IResourceRegisterable;

public class CssExpressionFactory implements IResourceExpressionFactory<BaseResourceExpression<?, ?>>,
      IResourceRegisterable<CssExpressionFactory> {
   @Override
   public IResourceExpression<?, ?> create(BaseResourceExpression<?, ?> parent, String key) {
      return new CssExpression(parent.getEnv(), parent, key);
   }

   @Override
   public CssExpressionFactory getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return SystemResourceType.Css.getName();
   }

   @Override
   public Class<? super CssExpressionFactory> getRegisterType() {
      return IResourceExpressionFactory.class;
   }
}
