package org.unidal.webres.resource.spi;

import java.util.List;

public interface IResourceTokenStorage {
   public String storeResourceUrns(List<String> urns);

   public List<String> loadResourceUrns(String token);
}