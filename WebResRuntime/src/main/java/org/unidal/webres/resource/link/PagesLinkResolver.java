package org.unidal.webres.resource.link;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.ResourceUrn;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.ILink;
import org.unidal.webres.resource.api.ILinkMeta;
import org.unidal.webres.resource.api.ILinkRef;
import org.unidal.webres.resource.api.INamespace;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.injection.ResourceAttribute;
import org.unidal.webres.resource.remote.IRemoteMetaBuilder;
import org.unidal.webres.resource.remote.IRemoteMetaBuilder.IRemoteMetaKeyBuilder;
import org.unidal.webres.resource.remote.IRemoteMetaProvider;
import org.unidal.webres.resource.remote.RemoteMetaProvider;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceResolver;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class PagesLinkResolver implements IResourceResolver<ILinkRef, ILink>, IResourceRegisterable<PagesLinkResolver> {
   private IResourceUrlBuilder<ILink> m_urlBuilder;

   private IRemoteMetaProvider<PagesLinkMeta> m_provider = new RemoteMetaProvider<PagesLinkMeta>(PagesLinkMetaBuilder.INSTANCE,
         "links");

   protected String getFallbackPath(String path) {
      int pos1 = path.lastIndexOf('/');
      int pos2 = path.lastIndexOf('_');

      if (pos2 > pos1) {
         return path.substring(0, pos2) + '.' + path.substring(pos2 + 1);
      } else {
         return null;
      }
   }

   protected PagesLinkMeta getLinkMeta(IResourceContext ctx, IResourceUrn urn) {
      String path = urn.getResourceId();

      try {
         PagesLinkMeta meta = m_provider.getMeta(path);

         if (meta == null) {
            String pathInfo = getFallbackPath(path);

            if (pathInfo != null) {
               meta = m_provider.getMeta(pathInfo);

               if (meta != null) {
                  urn.setPathInfo(pathInfo);
               }
            }
         }

         return meta;
      } catch (IOException e) {
         throw new RuntimeException(String.format("Unable to find image(%s)!", path), e);
      }
   }

   @Override
   public INamespace getNamespace() {
      return LinkNamespace.PAGES;
   }

   @Override
   public PagesLinkResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Link.Pages;
   }

   @Override
   public Class<? super PagesLinkResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Link;
   }

   @Override
   public PagesLink resolve(ILinkRef ref, IResourceContext ctx) throws ResourceException {
      IResourceUrn urn = ref.getUrn();
      PagesLinkMeta meta = getLinkMeta(ctx, urn);

      if (meta == null) {
         throw new RuntimeException(String.format("No pages link(%s) was found!", urn));
      }

      PagesLink link = new PagesLink(ctx, meta);

      link.validate();

      link.setUrlBuilder(m_urlBuilder);
      return link;
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Link.Pages)
   public void setUrlBuilder(IResourceUrlBuilder<? extends ILink> urlBuilder) {
      m_urlBuilder = (IResourceUrlBuilder<ILink>) urlBuilder;
   }

   public static class PagesLinkMeta implements ILinkMeta, IRemoteMetaKeyBuilder {
      private String m_pageId;

      private IResourceUrn m_urn;

      public PagesLinkMeta(String id, String pathInfo) {
         m_urn = new ResourceUrn(SystemResourceType.Link.getName(), LinkNamespace.PAGES.getName(), id);
         m_urn.setPathInfo(pathInfo);
      }

      @Override
      public String getKey(String path) {
         int start = path.lastIndexOf('/');

         return m_urn.getPathInfo().substring(start + 1);
      }

      public String getPageId() {
         return m_pageId;
      }

      @Override
      public IResourceType getResourceType() {
         return SystemResourceType.Link;
      }

      @Override
      public IResourceUrn getUrn() {
         return m_urn;
      }

      public void setPageId(String pageId) {
         m_pageId = pageId;
      }

      @Override
      public String toString() {
         return String.format("PagesLinkMeta[path=%s, id=%s]", m_urn.getPathInfo(), m_pageId);
      }
   }

   public static enum PagesLinkMetaBuilder implements IRemoteMetaBuilder<PagesLinkMeta> {
      INSTANCE;

      @Override
      public boolean build(Map<String, PagesLinkMeta> metas, String path, List<String> items) {
         if (items.size() >= 3) {
            int index = 0;
            String id = items.get(index++);
            String uri = items.get(index++);
            String pageId = items.get(index++);
            PagesLinkMeta meta = new PagesLinkMeta(toPath(id), uri);
            String key = meta.getKey(path);

            meta.setPageId(pageId);
            metas.put(key, meta);
            return true;
         }

         return false;
      }

      protected String toPath(String id) {
         int len = id.length();
         StringBuilder sb = new StringBuilder(len + 1);
         int pos = -1;

         sb.append('/');

         for (int i = 0; i < len; i++) {
            char ch = id.charAt(i);

            if (ch == '.') {
               pos = -1;
               sb.append('/');
            } else {
               if (ch == '_') {
                  pos = i;
               }

               sb.append(ch);
            }
         }

         if (pos > 0) {
            sb.setCharAt(pos + 1, '.');
         }

         return sb.toString();
      }
   }
}