package org.unidal.webres.resource.js;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.api.IJsMeta;
import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;

public class AggregatedJs extends JsSupport {
   private JsMeta m_meta;

   private Provider m_provider;

   private AggregatedJsRef m_ref;

   public AggregatedJs(IResourceContext ctx, AggregatedJsRef ref, boolean verbose) {
      super(ctx);

      m_ref = ref;
      m_provider = new Provider(ctx, ref, verbose);
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

   public AggregatedJsRef getRef() {
      return m_ref;
   }

   @Override
   public void validate() throws ResourceException {
      m_provider.resolveChildren();
   }

   static class Provider implements IJsProvider {
      private IResourceContext m_ctx;

      private AggregatedJsRef m_ref;

      private List<IJs> m_resources;

      private boolean m_verbose;

      private String m_content;

      public Provider(IResourceContext ctx, AggregatedJsRef ref, boolean verbose) {
         m_ctx = ctx;
         m_ref = ref;
         m_verbose = verbose;
      }

      protected void buildContent() {
         resolveChildren();

         if (m_content == null) {
            StringBuilder sb = new StringBuilder(4096);

            for (IJs resource : m_resources) {
               if (m_verbose) {
                  String urn = resource.getMeta().getUrn().toString();

                  sb.append("/**").append(spaces(urn.length())).append("**/\r\n");
                  sb.append("/* ").append(urn).append(" */\r\n");
                  sb.append("/**").append(spaces(urn.length())).append("**/\r\n");
               }

               sb.append(resource.getContent());
               sb.append("\r\n");
            }

            m_content = sb.toString();
         }
      }

      @Override
      public String getContent() {
         buildContent();
         return m_content;
      }

      @Override
      public long getLastModified() {
         return -1;
      }

      @Override
      public long getLength() {
         String content = getContent();

         try {
            return content.getBytes("utf-8").length;
         } catch (UnsupportedEncodingException e) {
            return content.getBytes().length;
         }
      }

      protected void resolveChildren() {
         if (m_resources == null) {
            List<IJsRef> childrenRefs = m_ref.getRefs();
            List<IJs> children = new ArrayList<IJs>(childrenRefs.size());

            for (IJsRef childRef : childrenRefs) {
               IJs child = childRef.resolve(m_ctx);

               children.add(child);
            }

            m_resources = children;
         }
      }

      private String spaces(int len) {
         StringBuilder sb = new StringBuilder(len);

         for (int i = 0; i < len; i++) {
            sb.append('*');
         }

         return sb.toString();
      }
   }
}