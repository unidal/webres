package org.unidal.webres.resource.scanner;

import org.unidal.webres.resource.api.INamespace;
import org.unidal.webres.resource.api.IResource;
import org.unidal.webres.resource.api.IResourceMeta;
import org.unidal.webres.resource.spi.IResourceVariation;

public interface IResourceScanner<T extends IResource<? extends IResourceMeta, ?>> {
   public INamespace getNamespace();

   public void scan(IResourceFilter<T> filter, IResourceResult<T> result, IResourceVariation... variations);
}
