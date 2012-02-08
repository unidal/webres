package org.unidal.webres.resource.expression;

import org.unidal.webres.resource.api.IImageRef;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.img.ImageFactory;

public class ImageExpression extends BaseResourceExpression<Object, IImageRef> {
   public ImageExpression(IResourceExpressionEnv env, BaseResourceExpression<?, ?> parent, String key) {
      super(env, parent, key);
   }

   @Override
   protected Object createChild(String key) {
      return new ImageExpression(getEnv(), this, key);
   }

   @Override
   public IImageRef evaluate() {
      IResourceUrn urn = buildUrn();

      return ImageFactory.forRef().createRef(urn.getNamespace(), urn.getResourceId());
   }

   @Override
   protected String getDefaultProperty() {
      if (getEnv().getResourceContext().isSecure()) {
         return ImagePropertyEvaluator.secureurl.name();
      } else {
         return ImagePropertyEvaluator.url.name();
      }
   }

   @Override
   protected void prepareUrn(Urn urn) {
      urn.addSection(getKey());
   }
}
