package org.unidal.webres.resource.link;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.api.ILink;
import org.unidal.webres.resource.injection.ResourceAttribute;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class PagesLinkUrlBuilder implements IResourceUrlBuilder<ILink>, IResourceRegisterable<PagesLinkUrlBuilder> {
   private String m_urlPrefix = "http://pages.unidal.org";

   private String m_secureUrlPrefix = "https://pages.unidal.org";

   @Override
   public String build(IResourceContext ctx, ILink link) {
      StringBuilder sb = new StringBuilder(128);

      if (ctx.isSecure()) {
         sb.append(m_secureUrlPrefix);
      } else {
         sb.append(m_urlPrefix);
      }

      sb.append(link.getMeta().getUrn().getPathInfo());

      return sb.toString();
   }

   @Override
   public PagesLinkUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Link.Pages;
   }

   @Override
   public Class<? super PagesLinkUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }

   @ResourceAttribute(value = ResourceConstant.Link.PagesUrlPrefix, optional = true)
   public PagesLinkUrlBuilder setUrlPrefix(String urlPrefix) {
      m_urlPrefix = urlPrefix;
      return this;
   }

   @ResourceAttribute(value = ResourceConstant.Link.PagesSecureUrlPrefix, optional = true)
   public PagesLinkUrlBuilder setSecureUrlPrefix(String secureUrlPrefix) {
      m_secureUrlPrefix = secureUrlPrefix;
      return this;
   }
}
