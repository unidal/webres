package org.unidal.webres.resource.img;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.unidal.webres.helper.Files;
import org.unidal.webres.helper.ImageAnalyzer;
import org.unidal.webres.resource.api.IImageMeta;
import org.unidal.webres.resource.api.IImageRef;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;

class LocalImage extends ImageSupport {
   private Provider m_provider;

   private ImageMeta m_meta;

   protected LocalImage(IResourceContext ctx, IImageRef ref, File file) {
      super(ctx);

      m_provider = new Provider(file);
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
      private File m_file;

      private int m_step;

      private byte[] m_content;

      private int m_width;

      private int m_height;

      private String m_mimeType;

      public Provider(File file) {
         m_file = file;
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
               throw new ResourceException(String.format("Not an image(%s)!", m_file));
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
         return m_file.lastModified();
      }

      @Override
      public long getLength() {
         return m_file.length();
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
               m_content = Files.forIO().readFrom(m_file);
               m_step++;
            } catch (IOException e) {
               throw new ResourceException(String.format("Unable to read file(%s)!", m_file), e);
            }
         }
      }
   }
}
