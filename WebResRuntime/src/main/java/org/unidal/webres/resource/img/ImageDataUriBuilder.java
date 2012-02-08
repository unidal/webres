package org.unidal.webres.resource.img;

import org.unidal.webres.helper.Encodings;
import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.api.IImage;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class ImageDataUriBuilder implements IResourceUrlBuilder<IImage>, IResourceRegisterable<ImageDataUriBuilder> {
   @Override
   public String build(IResourceContext ctx, IImage image) {
      byte[] content = image.getContent();
      int resultLen = 4 * ((content.length + 2) / 3);
      StringBuilder sb = new StringBuilder(resultLen + 32);

      sb.append("data:");
      sb.append(image.getMeta().getMimeType());
      sb.append(";base64,");
      Encodings.forBase64().encode(sb, content);

      return sb.toString();
   }

   @Override
   public ImageDataUriBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Image.DataUriBuilder;
   }

   @Override
   public Class<? super ImageDataUriBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }
}
