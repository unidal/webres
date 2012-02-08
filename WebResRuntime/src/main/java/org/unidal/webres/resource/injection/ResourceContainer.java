package org.unidal.webres.resource.injection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.unidal.webres.converter.TypeUtil;
import org.unidal.webres.resource.spi.IResourceContainer;

public class ResourceContainer implements IResourceContainer {
   private volatile Map<Class<?>, Map<String, Object>> m_attributes = new HashMap<Class<?>, Map<String, Object>>();

   private ResourceInjector m_injector = new ResourceInjector();

   private ReentrantReadWriteLock m_lock = new ReentrantReadWriteLock();

   private ReadLock m_readLock = m_lock.readLock();

   private WriteLock m_writeLock = m_lock.writeLock();

   private IResourceContainer m_fallback;

   public ResourceContainer() {
      this(null);
   }

   public ResourceContainer(IResourceContainer fallback) {
      m_fallback = fallback;
   }

   @Override
   public <T> T getAttribute(Class<T> type) {
      return getAttribute(type, DEFAULT_KEY);
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> T getAttribute(Class<T> type, String key) {
      T object = null;
      m_readLock.lock();

      try {
         Class<?> clazz = TypeUtil.getWrapClass(type);
         Map<String, Object> map = m_attributes.get(clazz);

         if (map != null) {
            object = (T) map.get(key);
         }
      } finally {
         m_readLock.unlock();
      }

      if (object == null && m_fallback != null) {
         object = m_fallback.getAttribute(type, key);
      }

      return object;
   }

   @Override
   public boolean hasAttribute(Class<?> type) {
      return hasAttribute(type, DEFAULT_KEY);
   }

   @Override
   public boolean hasAttribute(Class<?> type, String key) {
      m_readLock.lock();

      try {
         Class<?> clazz = TypeUtil.getWrapClass(type);
         Map<String, Object> map = m_attributes.get(clazz);

         if (map != null && map.containsKey(key)) {
            return true;
         } else if (m_fallback != null) {
            return m_fallback.hasAttribute(type, key);
         } else {
            return false;
         }
      } finally {
         m_readLock.unlock();
      }
   }

   @Override
   public void injectAttributes(Object injectable) {
      m_readLock.lock();

      try {
         m_injector.injectAttributes(this, injectable);
      } finally {
         m_readLock.unlock();
      }
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> T removeAttribute(Class<? super T> type, String key) {
      T object = null;
      m_writeLock.lock();

      try {
         Class<?> clazz = TypeUtil.getWrapClass(type);
         Map<String, Object> map = m_attributes.get(clazz);

         if (map != null) {
            object = (T) map.remove(key);
         }
      } finally {
         m_writeLock.unlock();
      }

      if (object == null && m_fallback != null) {
         object = m_fallback.removeAttribute(type, key);
      }

      return object;
   }

   @Override
   public <T> Object setAttribute(Class<? super T> type, String key, T object) {
      m_writeLock.lock();

      try {
         Class<?> clazz = TypeUtil.getWrapClass(type);
         Map<String, Object> map = m_attributes.get(clazz);

         if (map == null) {
            map = new HashMap<String, Object>();
            m_attributes.put(clazz, map);
         }

         return map.put(key, object);
      } finally {
         m_writeLock.unlock();
      }
   }

   @Override
   public <T> Object setAttribute(Class<? super T> type, T object) {
      return setAttribute(type, DEFAULT_KEY, object);
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> Object setAttribute(String key, T object) {
      return setAttribute((Class<T>) object.getClass(), key, object);
   }
}
