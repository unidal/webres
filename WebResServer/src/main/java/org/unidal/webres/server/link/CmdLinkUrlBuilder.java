package org.unidal.webres.server.link;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.annotation.ContextPath;
import org.unidal.webres.resource.api.ILink;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class CmdLinkUrlBuilder implements IResourceUrlBuilder<ILink>, IResourceRegisterable<CmdLinkUrlBuilder> {
   private String m_contextPath;

   @Override
   public String build(IResourceContext ctx, ILink link) {
      StringBuilder sb = new StringBuilder(128);

      if (m_contextPath != null) {
         sb.append(m_contextPath);
      }

      sb.append(link.getMeta().getUrn().getPathInfo());

      if (ctx.hasVariation("rtm")) {
         sb.append("#rtm");
      }

      return sb.toString();
   }

   @Override
   public CmdLinkUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Link.Cmd;
   }

   @Override
   public Class<? super CmdLinkUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }

   @ContextPath
   public void setContextPath(String contextPath) {
      m_contextPath = contextPath;
   }
}
