package org.unidal.webres.resource.js;

import java.io.File;
import java.io.IOException;

import org.unidal.helper.Files;
import org.unidal.webres.resource.api.IJsMeta;
import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;

class WarJs extends JsSupport {
   private Provider m_provider;

   private JsMeta m_meta;

   protected WarJs(IResourceContext ctx, IJsRef ref, File file) {
      super(ctx);

      m_provider = new Provider(file);
      m_meta = new JsMeta(m_provider, ref.getUrn());
   }

   @Override
   public String getContent() {
      return m_provider.getContent();
   }

   @Override
   public IJsMeta getMeta() {
      return m_meta;
   }

   @Override
   public void validate() throws ResourceException {
      // do nothing here
   }

   static class Provider implements IJsProvider {
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
