package org.unidal.webres.resource.template;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ITemplate;
import org.unidal.webres.resource.api.ITemplateRef;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceMappingApplier;
import org.unidal.webres.resource.spi.IResourceResolver;

class TemplateRef implements ITemplateRef {
   private IResourceUrn m_urn;

   public TemplateRef(IResourceUrn urn) {
      m_urn = urn;
   }

   @Override
   public IResourceUrn getUrn() {
      return m_urn;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Template;
   }

   @Override
   @SuppressWarnings("unchecked")
   public ITemplate resolve(IResourceContext ctx) throws ResourceException {
      IResourceResolver<ITemplateRef, ITemplate> resolver = ctx.lookup(IResourceResolver.class, m_urn.getScheme());

      if (resolver == null) {
         throw new RuntimeException(String.format("No IResourceResolver is registered for resource type(%s) and namespace(%s)!",
               m_urn.getResourceTypeName(), m_urn.getNamespace()));
      }

      IResourceMappingApplier applier = ctx.lookup(IResourceMappingApplier.class);

      if (applier != null) {
         m_urn = applier.apply(ctx, m_urn);
      }

      ITemplate resource = resolver.resolve(this, ctx);

      return resource;
   }

   @Override
   public String toString() {
      return m_urn.toString();
   }
}
