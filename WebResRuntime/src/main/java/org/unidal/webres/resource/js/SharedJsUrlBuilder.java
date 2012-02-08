package org.unidal.webres.resource.js;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.injection.ResourceAttribute;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class SharedJsUrlBuilder implements IResourceUrlBuilder<IJs>, IResourceRegisterable<SharedJsUrlBuilder> {
   private String m_urlPrefix = "${" + ResourceConstant.Js.SharedUrlPrefix + "}";

   private String m_secureUrlPrefix = "${" + ResourceConstant.Js.SharedSecureUrlPrefix + "}";

   @Override
   public String build(IResourceContext ctx, IJs js) {
      StringBuilder sb = new StringBuilder(128);

      if (ctx.isSecure()) {
         sb.append(m_secureUrlPrefix);
      } else {
         sb.append(m_urlPrefix);
      }

      if (ctx.getPermutation() != null && !ctx.isFallbackPermutation()) {
         sb.append('/').append(ctx.getPermutation().toExternal());
      }

      sb.append(js.getMeta().getUrn().getPathInfo());

      return sb.toString();
   }

   @Override
   public SharedJsUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Js.Shared;
   }

   @Override
   public Class<? super SharedJsUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }

   @ResourceAttribute(value = ResourceConstant.Js.SharedUrlPrefix, optional = true)
   public SharedJsUrlBuilder setUrlPrefix(String urlPrefix) {
      m_urlPrefix = urlPrefix;
      return this;
   }

   @ResourceAttribute(value = ResourceConstant.Js.SharedSecureUrlPrefix, optional = true)
   public SharedJsUrlBuilder setSecureUrlPrefix(String secureUrlPrefix) {
      m_secureUrlPrefix = secureUrlPrefix;
      return this;
   }
}
