package org.unidal.webres.resource.img;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.api.IImage;
import org.unidal.webres.resource.injection.ResourceAttribute;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class PicsImageUrlBuilder implements IResourceUrlBuilder<IImage>, IResourceRegisterable<PicsImageUrlBuilder> {
   private String m_urlPrefix = "http://pics.ebaystatic.com/aw/pics";

   private String m_secureUrlPrefix = "https://pics.ebaystatic.com/aw/pics";

   @Override
   public String build(IResourceContext ctx, IImage image) {
      StringBuilder sb = new StringBuilder(128);

      if (ctx.isSecure()) {
         sb.append(m_secureUrlPrefix);
      } else {
         sb.append(m_urlPrefix);
      }

      if (ctx.getPermutation() != null && !ctx.isFallbackPermutation()) {
         sb.append('/').append(ctx.getPermutation().toExternal());
      }

      sb.append(image.getMeta().getUrn().getPathInfo());

      return sb.toString();
   }

   @Override
   public PicsImageUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Image.Pics;
   }

   @Override
   public Class<? super PicsImageUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }

   @ResourceAttribute(value = ResourceConstant.Image.PicsUrlPrefix, optional = true)
   public PicsImageUrlBuilder setUrlPrefix(String urlPrefix) {
      m_urlPrefix = urlPrefix;
      return this;
   }

   @ResourceAttribute(value = ResourceConstant.Image.PicsSecureUrlPrefix, optional = true)
   public PicsImageUrlBuilder setSecureUrlPrefix(String secureUrlPrefix) {
      m_secureUrlPrefix = secureUrlPrefix;
      return this;
   }
}
