package org.unidal.webres.resource;

import java.util.HashMap;
import java.util.Map;

import org.unidal.webres.resource.api.IResourcePermutation;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegistry;

public class ResourceContext implements IResourceContext, Cloneable {
   private static final String SECURE = "secure";

   private static final String FALLBACK_PERMUTATION = "fallbackPermutation";

   private static final String PERMUTATION = "permutation";

   private IResourceContext m_parent;

   private IResourceRegistry m_registry;

   private Map<String, Object> m_variations;

   public ResourceContext(IResourceContext parent) {
      this(parent, null);
   }

   public ResourceContext(IResourceContext parent, IResourceRegistry registry) {
      if (parent == null && registry == null) {
         throw new IllegalArgumentException("Either parent context or registry should be provided!");
      }

      m_parent = parent;
      m_registry = registry;
   }

   public ResourceContext(IResourceRegistry registry) {
      this(null, registry);
   }

   @Override
   public IResourcePermutation getPermutation() {
      return getVariation(PERMUTATION);
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> T getVariation(String name) {
      return (T) getVariation(name, null);
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> T getVariation(String name, T defaultValue) {
      Object value = m_variations == null ? null : m_variations.get(name);

      if (value == null && m_parent != null) {
         value = m_parent.getVariation(name, null);
      }

      if (value != null) {
         return (T) value;
      } else {
         return defaultValue;
      }
   }

   @Override
   public boolean isFallbackPermutation() {
      return getVariation(FALLBACK_PERMUTATION, false);
   }

   @Override
   public boolean isSecure() {
      return getVariation(SECURE, false);
   }

   @Override
   public <T> T lookup(Class<T> type) {
      if (m_registry != null) {
         return m_registry.lookup(type);
      } else {
         return m_parent.lookup(type);
      }
   }

   @Override
   public <T> T lookup(Class<T> type, String key) {
      if (m_registry != null) {
         return m_registry.lookup(type, key);
      } else {
         return m_parent.lookup(type, key);
      }
   }

   @Override
   public void setFallbackPermutation(boolean fallback) {
      setVariation(FALLBACK_PERMUTATION, fallback);
   }

   @Override
   public void setPermutation(IResourcePermutation permutation) {
      setVariation(PERMUTATION, permutation);
   }

   @Override
   public void setSecure(boolean secure) {
      setVariation(SECURE, secure);
   }

   @Override
   public void setVariation(String name, Object value) {
      if (m_variations == null) {
         m_variations = new HashMap<String, Object>(6);
      }

      m_variations.put(name, value);
   }

   @Override
   public boolean hasVariation(String name) {
      if (m_variations != null && m_variations.containsKey(name)) {
         return true;
      } else if (m_parent != null) {
         return m_parent.hasVariation(name);
      } else {
         return false;
      }
   }
}
