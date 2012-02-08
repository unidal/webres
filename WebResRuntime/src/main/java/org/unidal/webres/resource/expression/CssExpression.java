package org.unidal.webres.resource.expression;

import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.css.CssFactory;

public class CssExpression extends BaseResourceExpression<Object, ICssRef> {
   public CssExpression(IResourceExpressionEnv env, BaseResourceExpression<?, ?> parent, String key) {
      super(env, parent, key);
   }

   @Override
   protected Object createChild(String key) {
      return new CssExpression(getEnv(), this, key);
   }

   @Override
   public ICssRef evaluate() {
      IResourceUrn urn = buildUrn();

      return CssFactory.forRef().createRef(urn.getNamespace(), urn.getResourceId());
   }

   @Override
   protected String getDefaultProperty() {
      if (getEnv().getResourceContext().isSecure()) {
         return CssPropertyEvaluator.secureurl.name();
      } else {
         return CssPropertyEvaluator.url.name();
      }
   }

   @Override
   protected void prepareUrn(Urn urn) {
      urn.addSection(getKey());
   }
}
