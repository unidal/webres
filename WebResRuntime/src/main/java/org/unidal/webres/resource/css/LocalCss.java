package org.unidal.webres.resource.css;

import java.io.File;
import java.io.IOException;

import org.unidal.helper.Files;
import org.unidal.webres.resource.api.ICssMeta;
import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;

class LocalCss extends CssSupport {
   private Provider m_provider;

   private CssMeta m_meta;

   protected LocalCss(IResourceContext ctx, ICssRef ref, File file) {
      super(ctx);

      m_provider = new Provider(file);
      m_meta = new CssMeta(m_provider, ref.getUrn());
   }

   @Override
   public String getContent() {
      return m_provider.getContent();
   }

   @Override
   public ICssMeta getMeta() {
      return m_meta;
   }

   @Override
   public void validate() throws ResourceException {
      // do nothing here
   }

   static class Provider implements ICssProvider {
      private File m_file;

      private boolean m_loaded;

      private String m_content;

      public Provider(File file) {
         m_file = file;
      }

      @Override
      public String getContent() {
         load();
         return m_content;
      }

      @Override
      public long getLastModified() {
         return m_file.lastModified();
      }

      @Override
      public long getLength() {
         return m_file.length();
      }
      
      void load() {
         if (!m_loaded) {
            try {
               m_content = Files.forIO().readFrom(m_file, "utf-8");
               m_loaded = true;
            } catch (IOException e) {
               throw new ResourceException(String.format("Unable to read file(%s)!", m_file), e);
            }
         }
      }
   }
}
