package org.unidal.webres.resource.scanner;

import org.unidal.webres.resource.api.IResource;
import org.unidal.webres.resource.api.IResourceMeta;

public interface IResourceFilter<T extends IResource<? extends IResourceMeta, ?>> {
   public boolean isEligible(T resource);
}
