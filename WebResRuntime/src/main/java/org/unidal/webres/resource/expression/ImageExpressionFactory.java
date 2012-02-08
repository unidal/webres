package org.unidal.webres.resource.expression;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.spi.IResourceRegisterable;

public class ImageExpressionFactory implements IResourceExpressionFactory<BaseResourceExpression<?, ?>>,
      IResourceRegisterable<ImageExpressionFactory> {
   @Override
   public IResourceExpression<?, ?> create(BaseResourceExpression<?, ?> parent, String key) {
      return new ImageExpression(parent.getEnv(), parent, key);
   }

   @Override
   public ImageExpressionFactory getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return SystemResourceType.Image.getName();
   }

   @Override
   public Class<? super ImageExpressionFactory> getRegisterType() {
      return IResourceExpressionFactory.class;
   }
}
