package org.unidal.webres.resource.scanner;

import org.unidal.webres.resource.api.IResource;
import org.unidal.webres.resource.api.IResourceMeta;

public interface IResourceResult<T extends IResource<? extends IResourceMeta, ?>> {
   public void addResource(T resource);
}
