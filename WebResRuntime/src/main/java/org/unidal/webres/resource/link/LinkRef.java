package org.unidal.webres.resource.link;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.ILink;
import org.unidal.webres.resource.api.ILinkRef;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceMappingApplier;
import org.unidal.webres.resource.spi.IResourceResolver;

class LinkRef implements ILinkRef {
   private IResourceUrn m_urn;

   public LinkRef(IResourceUrn urn) {
      m_urn = urn;
   }

   @Override
   public IResourceUrn getUrn() {
      return m_urn;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Link;
   }

   @Override
   @SuppressWarnings("unchecked")
   public ILink resolve(IResourceContext ctx) throws ResourceException {
      IResourceResolver<ILinkRef, ILink> resolver = ctx.lookup(IResourceResolver.class, m_urn.getScheme());

      if (resolver == null) {
         throw new RuntimeException(String.format("No IResourceResolver is registered for resource type(%s) and namespace(%s)!",
               m_urn.getResourceTypeName(), m_urn.getNamespace()));
      }

      IResourceMappingApplier applier = ctx.lookup(IResourceMappingApplier.class);

      if (applier != null) {
         m_urn = applier.apply(ctx, m_urn);
      }

      ILink link = resolver.resolve(this, ctx);

      if (link == null) {
         throw new RuntimeException(String.format("Link resource(%s) can't be resolved!", m_urn));
      }

      return link;
   }

   @Override
   public String toString() {
      return m_urn.toString();
   }
}
