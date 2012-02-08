package org.unidal.webres.helper;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class Caches {
   public static MethodCache forMethod() {
      return MethodCache.INSTANCE;
   }

   public static enum MethodCache {
      INSTANCE;

      private final int LRU_CACHE_SIZE = 1024;

      private final Map<Key, Method> m_map = new LinkedHashMap<Key, Method>(LRU_CACHE_SIZE, 0.75f, true) {
         private static final long serialVersionUID = 1L;

         @Override
         protected boolean removeEldestEntry(Map.Entry<Key, Method> eldest) {
            return size() > LRU_CACHE_SIZE;
         }
      };

      public Method get(Key key) {
         synchronized (m_map) {
            Method method = m_map.get(key);
            return method;
         }
      }

      public Method put(Key key, Method method) {
         synchronized (m_map) {
            Method oldMethod = m_map.put(key, method);
            return oldMethod;
         }
      }

      public static interface Key {
      }
   }
}