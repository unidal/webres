package org.unidal.webres.resource.js;

import java.net.URL;
import java.net.URLConnection;

import org.unidal.helper.Files;
import org.unidal.webres.resource.api.IJsMeta;
import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;

class SharedJs extends JsSupport {
   private Provider m_provider;

   private JsMeta m_meta;

   public SharedJs(IResourceContext ctx, IJsRef ref, URL url) {
      super(ctx);

      m_provider = new Provider(url);
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
      private boolean m_loaded;

      private long m_lastModified;

      private String m_content;

      private URL m_url;

      private long m_length;

      public Provider(URL url) {
         m_url = url;
      }

      @Override
      public String getContent() {
         load();
         return m_content;
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

      void load() {
         if (!m_loaded) {
            try {
               URLConnection conn = m_url.openConnection();

               m_lastModified = conn.getLastModified();

               byte[] data = Files.forIO().readFrom(conn.getInputStream());

               m_length = data.length;
               m_content = new String(data, "utf-8");
               m_loaded = true;
            } catch (Exception e) {
               throw new ResourceException(String.format("Unable to read file(%s)!", m_url), e);
            }
         }
      }
   }
}
