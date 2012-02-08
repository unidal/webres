package org.unidal.webres.resource.expression;

import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.js.JsFactory;

public class JsExpression extends BaseResourceExpression<Object, IJsRef> {
   public JsExpression(IResourceExpressionEnv env, BaseResourceExpression<?, ?> parent, String key) {
      super(env, parent, key);
   }

   @Override
   protected Object createChild(String key) {
      return new JsExpression(getEnv(), this, key);
   }

   @Override
   public IJsRef evaluate() {
      IResourceUrn urn = buildUrn();

      return JsFactory.forRef().createRef(urn.getNamespace(), urn.getResourceId());
   }

   @Override
   protected String getDefaultProperty() {
      if (getEnv().getResourceContext().isSecure()) {
         return JsPropertyEvaluator.secureurl.name();
      } else {
         return JsPropertyEvaluator.url.name();
      }
   }

   @Override
   protected void prepareUrn(Urn urn) {
      urn.addSection(getKey());
   }
}
