package org.unidal.webres.resource.expression;

import org.unidal.webres.resource.api.ILinkRef;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.link.LinkFactory;

public class LinkExpression extends BaseResourceExpression<Object, ILinkRef> {
   public LinkExpression(IResourceExpressionEnv env, BaseResourceExpression<?, ?> parent, String key) {
      super(env, parent, key);
   }

   @Override
   protected Object createChild(String key) {
      return new LinkExpression(getEnv(), this, key);
   }

   @Override
   public ILinkRef evaluate() {
      IResourceUrn urn = buildUrn();

      return LinkFactory.forRef().createRef(urn.getNamespace(), urn.getResourceId());
   }

   @Override
   protected String getDefaultProperty() {
      if (getEnv().getResourceContext().isSecure()) {
         return LinkPropertyEvaluator.secureurl.name();
      } else {
         return LinkPropertyEvaluator.url.name();
      }
   }

   @Override
   protected void prepareUrn(Urn urn) {
      urn.addSection(getKey());
   }
}
