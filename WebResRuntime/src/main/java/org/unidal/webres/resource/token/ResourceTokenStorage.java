package org.unidal.webres.resource.token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.unidal.webres.resource.spi.IResourceTokenStorage;

public enum ResourceTokenStorage implements IResourceTokenStorage {
   INSTANCE;

   private Map<String, List<String>> m_map = new HashMap<String, List<String>>();

   @Override
   public String storeResourceUrns(List<String> urns) {
      String token = Long.toHexString(Math.abs(urns.hashCode()));

      m_map.put(token, urns);
      return token;
   }

   @Override
   public List<String> loadResourceUrns(String token) {
      return m_map.get(token);
   }
}
