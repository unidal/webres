package org.unidal.webres.resource.img;

import org.unidal.webres.resource.api.IImageMeta;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;

class PicsImage extends ImageSupport {
   private IImageMeta m_meta;

   public PicsImage(IResourceContext ctx, IImageMeta meta) {
      super(ctx);

      m_meta = meta;
   }

   @Override
   public byte[] getContent() {
      throw new UnsupportedOperationException("No content is supported by pics Image, and thus no data uri!");
   }

   @Override
   public IImageMeta getMeta() {
      return m_meta;
   }

   @Override
   public void validate() throws ResourceException {
      // do nothing here since we can't do it for remote pics
   }
}
