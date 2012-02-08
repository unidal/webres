package org.unidal.webres.resource.expression;

import java.io.UnsupportedEncodingException;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.spi.IResourceRegisterable;

public enum JsPropertyEvaluator implements IResourcePropertyEvaluator<IJs>, IResourceRegisterable<JsPropertyEvaluator> {
   text {
      @Override
      public String evaluate(IJs js) {
         return js.getContent();
      }
   },

   size {
      @Override
      public Integer evaluate(IJs js) {
         try {
            return js.getContent().getBytes("utf-8").length;
         } catch (UnsupportedEncodingException e) {
            return js.getContent().getBytes().length;
         }
      }
   },

   url {
      @Override
      public String evaluate(IJs js) {
         return js.getUrl();
      }
   },

   secureurl {
      @Override
      public String evaluate(IJs js) {
         return js.getSecureUrl();
      }
   };

   @Override
   public JsPropertyEvaluator getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return SystemResourceType.Js.getName() + ":" + IResourceExpression.PROPERTY_PREFIX + name();
   }

   @Override
   public Class<? super JsPropertyEvaluator> getRegisterType() {
      return IResourcePropertyEvaluator.class;
   }
}
