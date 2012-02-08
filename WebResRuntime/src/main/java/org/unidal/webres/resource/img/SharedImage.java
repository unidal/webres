package org.unidal.webres.resource.img;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.net.URLConnection;

import org.unidal.webres.helper.Files;
import org.unidal.webres.helper.ImageAnalyzer;
import org.unidal.webres.resource.api.IImageMeta;
import org.unidal.webres.resource.api.IImageRef;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;

class SharedImage extends ImageSupport {
   private Provider m_provider;

   private ImageMeta m_meta;

   public SharedImage(IResourceContext ctx, IImageRef ref, URL url) {
      super(ctx);

      m_provider = new Provider(url);
      m_meta = new ImageMeta(m_provider, ref.getUrn());
   }

   @Override
   public byte[] getContent() {
      return m_provider.getContent();
   }

   @Override
   public IImageMeta getMeta() {
      return m_meta;
   }

   @Override
   public void validate() throws ResourceException {
      m_provider.analysis();
   }

   static class Provider implements IImageProvider {
      private int m_step;

      private long m_lastModified;

      private byte[] m_content;

      private int m_width;

      private int m_height;

      private String m_mimeType;

      private URL m_url;

      private long m_length;

      public Provider(URL url) {
         m_url = url;
      }

      void analysis() {
         load();

         if (m_step == 1) {
            ImageAnalyzer analyzer = new ImageAnalyzer();

            analyzer.setInput(new ByteArrayInputStream(m_content));

            if (analyzer.check()) {
               m_mimeType = analyzer.getMimeType();
               m_width = analyzer.getWidth();
               m_height = analyzer.getHeight();
            } else {
               throw new ResourceException(String.format("Not an image(%s)!", m_url));
            }

            m_step++;
         }
      }

      @Override
      public byte[] getContent() {
         load();
         return m_content;
      }

      @Override
      public int getHeight() {
         analysis();
         return m_height;
      }

      @Override
      public long getLastModified() {
         load();
         return m_lastModified;
      }

      @Override
      public long getLength() {
         load();
         return m_length;
      }

      @Override
      public String getMimeType() {
         analysis();
         return m_mimeType;
      }

      @Override
      public int getWidth() {
         analysis();
         return m_width;
      }

      void load() {
         if (m_step == 0) {
            try {
               URLConnection conn = m_url.openConnection();

               m_lastModified = conn.getLastModified();
               m_content = Files.forIO().readFrom(conn.getInputStream());
               m_length = m_content.length;
               m_step++;
            } catch (Exception e) {
               throw new ResourceException(String.format("Unable to read file(%s)!", m_url), e);
            }
         }
      }
   }
}
