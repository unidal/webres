package org.unidal.webres.resource.css;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.injection.ResourceAttribute;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class SharedCssUrlBuilder implements IResourceUrlBuilder<ICss>, IResourceRegisterable<SharedCssUrlBuilder> {
   private String m_urlPrefix = "${" + ResourceConstant.Css.SharedUrlPrefix + "}";

   private String m_secureUrlPrefix = "${" + ResourceConstant.Css.SharedSecureUrlPrefix + "}";

   @Override
   public String build(IResourceContext ctx, ICss css) {
      StringBuilder sb = new StringBuilder(128);

      if (ctx.isSecure()) {
         sb.append(m_secureUrlPrefix);
      } else {
         sb.append(m_urlPrefix);
      }

      if (ctx.getPermutation() != null && !ctx.isFallbackPermutation()) {
         sb.append('/').append(ctx.getPermutation().toExternal());
      }

      sb.append(css.getMeta().getUrn().getPathInfo());

      return sb.toString();
   }

   @Override
   public SharedCssUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Css.Shared;
   }

   @Override
   public Class<? super SharedCssUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }

   @ResourceAttribute(value = ResourceConstant.Css.SharedUrlPrefix, optional = true)
   public SharedCssUrlBuilder setUrlPrefix(String urlPrefix) {
      m_urlPrefix = urlPrefix;
      return this;
   }

   @ResourceAttribute(value = ResourceConstant.Css.SharedSecureUrlPrefix, optional = true)
   public SharedCssUrlBuilder setSecureUrlPrefix(String secureUrlPrefix) {
      m_secureUrlPrefix = secureUrlPrefix;
      return this;
   }
}
