package org.unidal.webres.resource.img;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IImage;
import org.unidal.webres.resource.api.IImageMeta;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public abstract class ImageSupport implements IImage {
   private IResourceContext m_ctx;

   private IResourceUrlBuilder<IImage> m_urlBuilder;

   private IResourceUrlBuilder<IImage> m_dataUriBuilder;

   public ImageSupport(IResourceContext ctx) {
      m_ctx = ctx;
   }

   @Override
   public String getDataUri() {
      return m_dataUriBuilder.build(m_ctx, this);
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

   public void setDataUriBuilder(IResourceUrlBuilder<IImage> dataUriBuilder) {
      m_dataUriBuilder = dataUriBuilder;
   }

   public void setUrlBuilder(IResourceUrlBuilder<IImage> urlBuilder) {
      m_urlBuilder = urlBuilder;
   }

   public interface IImageProvider {
      public byte[] getContent();

      public int getHeight();

      public long getLastModified();

      public long getLength();

      public String getMimeType();

      public int getWidth();
   }

   protected static class ImageMeta implements IImageMeta {
      private IImageProvider m_provider;

      private IResourceUrn m_urn;

      public ImageMeta(IImageProvider provider, IResourceUrn urn) {
         m_provider = provider;
         m_urn = urn;
      }

      @Override
      public int getHeight() {
         return m_provider.getHeight();
      }

      @Override
      public long getLength() {
         return m_provider.getLength();
      }
      
      @Override
      public long getLastModified() {
         return m_provider.getLastModified();
      }

      @Override
      public String getMimeType() {
         return m_provider.getMimeType();
      }

      @Override
      public IResourceType getResourceType() {
         return SystemResourceType.Image;
      }

      @Override
      public IResourceUrn getUrn() {
         return m_urn;
      }

      @Override
      public int getWidth() {
         return m_provider.getWidth();
      }
   }
}
