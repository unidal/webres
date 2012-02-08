package org.unidal.webres.resource.css;

import java.io.UnsupportedEncodingException;

import org.unidal.webres.resource.api.ICssMeta;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;

class InlineCss extends CssSupport {
   private CssMeta m_meta;

   private Provider m_provider;

   public InlineCss(IResourceContext ctx, InlineCssRef ref) {
      super(ctx);

      m_provider = new Provider(ref.getContent());
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
      private String m_content;

      public Provider(String content) {
         m_content = content;
      }

      @Override
      public String getContent() {
         return m_content;
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