package org.unidal.webres.resource.expression;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IImage;
import org.unidal.webres.resource.spi.IResourceRegisterable;

public enum ImagePropertyEvaluator implements IResourcePropertyEvaluator<IImage>,
      IResourceRegisterable<ImagePropertyEvaluator> {
   size {
      @Override
      public Integer evaluate(IImage image) {
         return image.getContent().length;
      }
   },
   
   mimeType {
      @Override
      public String evaluate(IImage image) {
         return image.getMeta().getMimeType();
      }
   },
   
   url {
      @Override
      public String evaluate(IImage image) {
         return image.getUrl();
      }
   },

   secureurl {
      @Override
      public String evaluate(IImage image) {
         return image.getSecureUrl();
      }
   },
   
   height {
      @Override
      public Integer evaluate(IImage image) {
         return image.getMeta().getHeight();
      }
   },

   width {
      @Override
      public Integer evaluate(IImage image) {
         return image.getMeta().getWidth();
      }
   },

   datauri {
      @Override
      public String evaluate(IImage image) {
         return image.getDataUri();
      }
   };

   @Override
   public ImagePropertyEvaluator getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return SystemResourceType.Image.getName() + ":" + IResourceExpression.PROPERTY_PREFIX + name();
   }

   @Override
   public Class<? super ImagePropertyEvaluator> getRegisterType() {
      return IResourcePropertyEvaluator.class;
   }
}
