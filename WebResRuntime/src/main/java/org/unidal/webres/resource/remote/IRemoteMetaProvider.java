package org.unidal.webres.resource.remote;

import java.io.IOException;

import org.unidal.webres.resource.api.IResourceMeta;

public interface IRemoteMetaProvider<T extends IResourceMeta> {
   public T getMeta(String path) throws IOException;
}
