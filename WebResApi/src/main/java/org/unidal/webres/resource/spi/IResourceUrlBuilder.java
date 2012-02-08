package org.unidal.webres.resource.spi;

import org.unidal.webres.resource.api.IResource;
import org.unidal.webres.resource.api.IResourceMeta;

public interface IResourceUrlBuilder<T extends IResource<? extends IResourceMeta, ?>> {
   public String build(IResourceContext ctx, T resource);
}
