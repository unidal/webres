package org.unidal.webres.resource.js;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.api.IJsMeta;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public abstract class JsSupport implements IJs {
   private IResourceContext m_ctx;

   private IResourceUrlBuilder<IJs> m_urlBuilder;

   public JsSupport(IResourceContext ctx) {
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

   public void setUrlBuilder(IResourceUrlBuilder<IJs> urlBuilder) {
      m_urlBuilder = urlBuilder;
   }

   public interface IJsProvider {
      public String getContent();

      public long getLastModified();

      public long getLength();
   }

   protected static class JsMeta implements IJsMeta {
      private IJsProvider m_provider;

      private IResourceUrn m_urn;

      public JsMeta(IJsProvider provider, IResourceUrn urn) {
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
         return SystemResourceType.Js;
      }

      @Override
      public IResourceUrn getUrn() {
         return m_urn;
      }
   }
}
