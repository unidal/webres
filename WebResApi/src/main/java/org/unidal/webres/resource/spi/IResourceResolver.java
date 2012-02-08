package org.unidal.webres.resource.spi;

import org.unidal.webres.resource.api.INamespace;
import org.unidal.webres.resource.api.IResource;
import org.unidal.webres.resource.api.IResourceRef;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.ResourceException;

public interface IResourceResolver<S extends IResourceRef<T>, T extends IResource<?, ?>> {
   public INamespace getNamespace();

   public T resolve(S ref, IResourceContext ctx) throws ResourceException;

   public IResourceType getResourceType();
}
