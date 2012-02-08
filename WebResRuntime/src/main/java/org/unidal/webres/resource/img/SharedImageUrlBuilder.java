package org.unidal.webres.resource.img;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.api.IImage;
import org.unidal.webres.resource.injection.ResourceAttribute;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class SharedImageUrlBuilder implements IResourceUrlBuilder<IImage>, IResourceRegisterable<SharedImageUrlBuilder> {
   private String m_urlPrefix = "${" + ResourceConstant.Image.SharedUrlPrefix + "}";

   private String m_secureUrlPrefix = "${" + ResourceConstant.Image.SharedSecureUrlPrefix + "}";

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
   public SharedImageUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Image.Shared;
   }

   @Override
   public Class<? super SharedImageUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }

   @ResourceAttribute(value = ResourceConstant.Image.SharedUrlPrefix, optional = true)
   public SharedImageUrlBuilder setUrlPrefix(String urlPrefix) {
      m_urlPrefix = urlPrefix;
      return this;
   }

   @ResourceAttribute(value = ResourceConstant.Image.SharedSecureUrlPrefix, optional = true)
   public SharedImageUrlBuilder setSecureUrlPrefix(String secureUrlPrefix) {
      m_secureUrlPrefix = secureUrlPrefix;
      return this;
   }
}
