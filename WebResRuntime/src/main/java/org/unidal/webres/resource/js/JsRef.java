package org.unidal.webres.resource.js;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceMappingApplier;
import org.unidal.webres.resource.spi.IResourceResolver;

class JsRef implements IJsRef {
   private IResourceUrn m_urn;

   public JsRef(IResourceUrn urn) {
      m_urn = urn;
   }

   @Override
   public IResourceUrn getUrn() {
      return m_urn;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Js;
   }

   @Override
   @SuppressWarnings("unchecked")
   public IJs resolve(IResourceContext ctx) throws ResourceException {
      IResourceResolver<IJsRef, IJs> resolver = ctx.lookup(IResourceResolver.class, m_urn.getScheme());

      if (resolver == null) {
         throw new RuntimeException(String.format("No IResourceResolver is registered for resource type(%s) and namespace(%s)!",
               m_urn.getResourceTypeName(), m_urn.getNamespace()));
      }

      IResourceMappingApplier applier = ctx.lookup(IResourceMappingApplier.class);

      if (applier != null) {
         m_urn = applier.apply(ctx, m_urn);
      }

      IJs resource = resolver.resolve(this, ctx);

      return resource;
   }

   @Override
   public String toString() {
      return m_urn.toString();
   }
}
