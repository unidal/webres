package org.unidal.webres.server.link;

import java.util.List;

import org.unidal.helper.Splitters;
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
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceResolver;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;
import org.unidal.webres.server.SimpleResourceNamespace;

public class CmdLinkResolver implements IResourceResolver<ILinkRef, ILink>, IResourceRegisterable<CmdLinkResolver> {
   private IResourceUrlBuilder<ILink> m_urlBuilder;

   protected String buildPathInfo(IResourceContext ctx, String path) {
      List<String> parts = Splitters.by('/').split(path);
      StringBuilder sb = new StringBuilder(path.length() + 16);
      int len = parts.size();
      boolean inQs = false;

      for (int i = 1; i < len; i++) {
         String part = parts.get(i);

         if (part.startsWith("&")) {
            if (!inQs) {
               sb.append('?');
               inQs = true;
            } else {
               sb.append('&');
            }

            if (part.indexOf('=') > 0) {
               sb.append(part.substring(1));
            } else {
               String value = i + 1 < len ? parts.get(i + 1) : null;

               sb.append(part.substring(1));
               sb.append('=');
               sb.append(value); // TODO need url encode
               i++;
            }
         } else if (part.startsWith("%")) {
            ctx.setVariation(part.substring(1), true);
         } else {
            if (!inQs) {
               sb.append('/').append(part);
            } else {
               throw new RuntimeException(String.format("%s should be starting with '&'.", part));
            }
         }
      }

      return sb.toString();
   }

   protected CmdLinkMeta getLinkMeta(IResourceContext ctx, IResourceUrn urn) {
      String path = urn.getResourceId();
      String pathInfo = buildPathInfo(ctx, path);

      return new CmdLinkMeta(path, pathInfo);
   }

   @Override
   public INamespace getNamespace() {
      return SimpleResourceNamespace.CMD;
   }

   @Override
   public CmdLinkResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Link.Cmd;
   }

   @Override
   public Class<? super CmdLinkResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Link;
   }

   @Override
   public CmdLink resolve(ILinkRef ref, IResourceContext ctx) throws ResourceException {
      IResourceUrn urn = ref.getUrn();
      CmdLinkMeta meta = getLinkMeta(ctx, urn);
      CmdLink link = new CmdLink(ctx, meta);

      link.validate();

      link.setUrlBuilder(m_urlBuilder);
      return link;
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Link.Cmd)
   public void setUrlBuilder(IResourceUrlBuilder<? extends ILink> urlBuilder) {
      m_urlBuilder = (IResourceUrlBuilder<ILink>) urlBuilder;
   }

   public static class CmdLinkMeta implements ILinkMeta {
      private IResourceUrn m_urn;

      public CmdLinkMeta(String path, String pathInfo) {
         m_urn = new ResourceUrn(SystemResourceType.Link.getName(), SimpleResourceNamespace.CMD.getName(), path);
         m_urn.setPathInfo(pathInfo);
      }

      @Override
      public IResourceType getResourceType() {
         return SystemResourceType.Link;
      }

      @Override
      public IResourceUrn getUrn() {
         return m_urn;
      }

      @Override
      public String toString() {
         return String.format("CmdLinkMeta[path=%s]", m_urn.getPathInfo());
      }
   }
}