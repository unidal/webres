package org.unidal.webres.resource.remote;

import java.util.List;
import java.util.Map;

import org.unidal.webres.resource.api.IResourceMeta;

public interface IRemoteMetaBuilder<T extends IResourceMeta> {
   public boolean build(Map<String, T> metas, String path, List<String> items);

   public interface IRemoteMetaKeyBuilder {
      public String getKey(String path);
   }
}
