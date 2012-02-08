package org.unidal.webres.resource.spi;

import org.unidal.webres.resource.api.IResourceUrn;

public interface IResourceMappingApplier {
   public IResourceUrn apply(IResourceContext ctx, IResourceUrn urn);
}
