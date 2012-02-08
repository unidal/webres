package org.unidal.webres.resource.expression;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.ILink;
import org.unidal.webres.resource.spi.IResourceRegisterable;

public enum LinkPropertyEvaluator implements IResourcePropertyEvaluator<ILink>,
      IResourceRegisterable<LinkPropertyEvaluator> {
   url {
      @Override
      public String evaluate(ILink link) {
         return link.getUrl();
      }
   },

   secureurl {
      @Override
      public String evaluate(ILink link) {
         return link.getSecureUrl();
      }
   };

   @Override
   public LinkPropertyEvaluator getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return SystemResourceType.Link.getName() + ":" + IResourceExpression.PROPERTY_PREFIX + name();
   }

   @Override
   public Class<? super LinkPropertyEvaluator> getRegisterType() {
      return IResourcePropertyEvaluator.class;
   }
}
