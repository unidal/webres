package org.unidal.webres.resource.template;

import java.io.File;
import java.io.IOException;

import org.unidal.helper.Files;
import org.unidal.webres.resource.api.ITemplateMeta;
import org.unidal.webres.resource.api.ITemplateRef;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.ITemplateProvider;

class LocalTemplate extends TemplateSupport {
   private Provider m_provider;

   private TemplateMeta m_meta;

   protected LocalTemplate(IResourceContext ctx, ITemplateRef ref, File file) {
      super(ctx);

      m_provider = new Provider(file);
      m_meta = new TemplateMeta(m_provider, ref.getUrn());
   }

   @Override
   public String getContent() {
      return m_provider.getContent();
   }

   @Override
   public ITemplateMeta getMeta() {
      return m_meta;
   }

   @Override
   public void validate() throws ResourceException {
      m_provider.load();
   }

   static class Provider implements ITemplateProvider {
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
      public String getLanguage() {
         return Templates.forLanguage().detectByFileName(m_file.getPath(), m_content);
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
