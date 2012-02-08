package org.unidal.webres.resource.img;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IImage;
import org.unidal.webres.resource.api.IImageRef;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceMappingApplier;
import org.unidal.webres.resource.spi.IResourceResolver;

class ImageRef implements IImageRef {
   private IResourceUrn m_urn;

   public ImageRef(IResourceUrn urn) {
      m_urn = urn;
   }

   @Override
   public IResourceUrn getUrn() {
      return m_urn;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Image;
   }

   @Override
   @SuppressWarnings("unchecked")
   public IImage resolve(IResourceContext ctx) throws ResourceException {
      IResourceResolver<IImageRef, IImage> resolver = ctx.lookup(IResourceResolver.class, m_urn.getScheme());

      if (resolver == null) {
         throw new RuntimeException(String.format("No IResourceResolver is registered for resource type(%s) and namespace(%s)!",
               m_urn.getResourceTypeName(), m_urn.getNamespace()));
      }

      IResourceMappingApplier applier = ctx.lookup(IResourceMappingApplier.class);

      if (applier != null) {
         m_urn = applier.apply(ctx, m_urn);
      }

      IImage image = resolver.resolve(this, ctx);

      if (image == null) {
         throw new RuntimeException(String.format("Image resource(%s) can't be resolved!", m_urn));
      }

      return image;
   }

   @Override
   public String toString() {
      return m_urn.toString();
   }
}
