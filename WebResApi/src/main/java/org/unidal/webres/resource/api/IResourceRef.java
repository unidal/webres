package org.unidal.webres.resource.api;

import org.unidal.webres.resource.spi.IResourceContext;

public interface IResourceRef<T extends IResource<?, ?>> {
   public IResourceType getResourceType();

   public IResourceUrn getUrn();

   public T resolve(IResourceContext ctx) throws ResourceException;
}