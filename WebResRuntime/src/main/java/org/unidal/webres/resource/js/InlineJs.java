package org.unidal.webres.resource.js;

import java.io.UnsupportedEncodingException;

import org.unidal.webres.resource.api.IJsMeta;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;

class InlineJs extends JsSupport {
   private JsMeta m_meta;

   private Provider m_provider;

   public InlineJs(IResourceContext ctx, InlineJsRef ref) {
      super(ctx);

      m_provider = new Provider(ref.getContent());
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