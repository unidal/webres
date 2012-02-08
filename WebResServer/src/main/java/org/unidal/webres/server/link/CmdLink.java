package org.unidal.webres.server.link;

import org.unidal.webres.resource.api.ILinkMeta;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.link.LinkSupport;
import org.unidal.webres.resource.spi.IResourceContext;

class CmdLink extends LinkSupport {
   private ILinkMeta m_meta;

   public CmdLink(IResourceContext ctx, ILinkMeta meta) {
      super(ctx);

      m_meta = meta;
   }

   @Override
   public Void getContent() {
      throw new UnsupportedOperationException("No content is supported by pages Link!");
   }

   @Override
   public ILinkMeta getMeta() {
      return m_meta;
   }

   @Override
   public void validate() throws ResourceException {
      // do nothing here 
   }
}
