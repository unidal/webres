package org.unidal.webres.resource.css;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.api.ICssMeta;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public abstract class CssSupport implements ICss {
   private IResourceContext m_ctx;

   private IResourceUrlBuilder<ICss> m_urlBuilder;

   public CssSupport(IResourceContext ctx) {
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

   public void setUrlBuilder(IResourceUrlBuilder<ICss> urlBuilder) {
      m_urlBuilder = urlBuilder;
   }

   protected static class CssMeta implements ICssMeta {
      private ICssProvider m_provider;

      private IResourceUrn m_urn;

      public CssMeta(ICssProvider provider, IResourceUrn urn) {
         m_provider = provider;
         m_urn = urn;
      }

      @Override
      public long getLastModified() {
         return m_provider.getLastModified();
      }

      @Override
      public long getLength() {
         return m_provider.getLength();
      }

      @Override
      public IResourceType getResourceType() {
         return SystemResourceType.Css;
      }

      @Override
      public IResourceUrn getUrn() {
         return m_urn;
      }
   }

   public interface ICssProvider {
      public String getContent();

      public long getLastModified();

      public long getLength();
   }
}
