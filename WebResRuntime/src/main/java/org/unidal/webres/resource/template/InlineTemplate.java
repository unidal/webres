package org.unidal.webres.resource.template;

import java.io.UnsupportedEncodingException;

import org.unidal.webres.resource.api.ITemplateMeta;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.ITemplateProvider;

class InlineTemplate extends TemplateSupport {
   private TemplateMeta m_meta;

   private Provider m_provider;

   public InlineTemplate(IResourceContext ctx, InlineTemplateRef ref) {
      super(ctx);

      m_provider = new Provider(ref.getContent());
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
      // do nothing here
   }

   static class Provider implements ITemplateProvider {
      private String m_content;

      public Provider(String content) {
         m_content = content;
      }

      @Override
      public String getContent() {
         return m_content;
      }

      @Override
      public String getLanguage() {
         return Templates.forLanguage().detectByContent(m_content);
      }

      @Override
      public long getLastModified() {
         return -1;
      }

      @Override
      public long getLength() {
         try {
            return m_content.getBytes("utf-8").length;
         } catch (UnsupportedEncodingException e) {
            return m_content.getBytes().length;
         }
      }
   }
}