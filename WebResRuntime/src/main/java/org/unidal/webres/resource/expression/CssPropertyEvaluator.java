package org.unidal.webres.resource.expression;

import java.io.UnsupportedEncodingException;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.spi.IResourceRegisterable;

public enum CssPropertyEvaluator implements IResourcePropertyEvaluator<ICss>,
      IResourceRegisterable<CssPropertyEvaluator> {
   text {
      @Override
      public String evaluate(ICss css) {
         return css.getContent();
      }
   },

   size {
      @Override
      public Integer evaluate(ICss css) {
         try {
            return css.getContent().getBytes("utf-8").length;
         } catch (UnsupportedEncodingException e) {
            return css.getContent().getBytes().length;
         }
      }
   },

   url {
      @Override
      public String evaluate(ICss css) {
         return css.getUrl();
      }
   },

   secureurl {
      @Override
      public String evaluate(ICss css) {
         return css.getSecureUrl();
      }
   };

   @Override
   public CssPropertyEvaluator getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return SystemResourceType.Css.getName() + ":" + IResourceExpression.PROPERTY_PREFIX + name();
   }

   @Override
   public Class<? super CssPropertyEvaluator> getRegisterType() {
      return IResourcePropertyEvaluator.class;
   }
}
