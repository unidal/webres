package org.unidal.webres.resource.link;

import org.unidal.webres.resource.api.ILink;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public abstract class LinkSupport implements ILink {
   private IResourceContext m_ctx;

   private IResourceUrlBuilder<ILink> m_urlBuilder;

   public LinkSupport(IResourceContext ctx) {
      m_ctx = ctx;
   }

   protected IResourceContext getResourceContext() {
      return m_ctx;
   }

   @Override
   public String getSecureUrl() {
      return getUrl(true);
   }

   @Override
   public String getUrl() {
      return getUrl(false);
   }

   String getUrl(boolean secure) {
      boolean old = m_ctx.isSecure();

      m_ctx.setSecure(secure);

      try {
         String url = m_urlBuilder.build(m_ctx, this);

         return url;
      } finally {
         m_ctx.setSecure(old);
      }
   }

   public void setUrlBuilder(IResourceUrlBuilder<ILink> urlBuilder) {
      m_urlBuilder = urlBuilder;
   }
}
