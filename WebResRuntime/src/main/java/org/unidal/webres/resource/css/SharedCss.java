package org.unidal.webres.resource.css;

import java.net.URL;
import java.net.URLConnection;

import org.unidal.helper.Files;
import org.unidal.webres.resource.api.ICssMeta;
import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;

class SharedCss extends CssSupport {
   private Provider m_provider;

   private CssMeta m_meta;

   public SharedCss(IResourceContext ctx, ICssRef ref, URL url) {
      super(ctx);

      m_provider = new Provider(url);
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
      private boolean m_loaded;

      private long m_lastModified;

      private String m_content;

      private long m_length;

      private URL m_url;

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
