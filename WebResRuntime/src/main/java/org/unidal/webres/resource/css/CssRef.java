package org.unidal.webres.resource.css;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceMappingApplier;
import org.unidal.webres.resource.spi.IResourceResolver;

class CssRef implements ICssRef {
   private IResourceUrn m_urn;

   public CssRef(IResourceUrn urn) {
      m_urn = urn;
   }

   @Override
   public IResourceUrn getUrn() {
      return m_urn;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Css;
   }

   @Override
   @SuppressWarnings("unchecked")
   public ICss resolve(IResourceContext ctx) throws ResourceException {
      IResourceResolver<ICssRef, ICss> resolver = ctx.lookup(IResourceResolver.class, m_urn.getScheme());

      if (resolver == null) {
         throw new RuntimeException(String.format("No IResourceResolver is registered for resource type(%s) and namespace(%s)!",
               m_urn.getResourceTypeName(), m_urn.getNamespace()));
      }

      IResourceMappingApplier applier = ctx.lookup(IResourceMappingApplier.class);

      if (applier != null) {
         m_urn = applier.apply(ctx, m_urn);
      }

      ICss resource = resolver.resolve(this, ctx);

      return resource;
   }

   @Override
   public String toString() {
      return m_urn.toString();
   }
}
