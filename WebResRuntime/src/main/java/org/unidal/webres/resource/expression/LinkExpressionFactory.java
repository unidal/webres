package org.unidal.webres.resource.expression;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.spi.IResourceRegisterable;

public class LinkExpressionFactory implements IResourceExpressionFactory<BaseResourceExpression<?, ?>>,
      IResourceRegisterable<LinkExpressionFactory> {
   @Override
   public IResourceExpression<?, ?> create(BaseResourceExpression<?, ?> parent, String key) {
      return new LinkExpression(parent.getEnv(), parent, key);
   }

   @Override
   public LinkExpressionFactory getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return SystemResourceType.Link.getName();
   }

   @Override
   public Class<? super LinkExpressionFactory> getRegisterType() {
      return IResourceExpressionFactory.class;
   }
}
