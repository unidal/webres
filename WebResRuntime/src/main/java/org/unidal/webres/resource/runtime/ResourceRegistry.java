package org.unidal.webres.resource.runtime;

import java.util.LinkedList;
import java.util.Queue;

import org.unidal.webres.resource.spi.IResourceContainer;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceRegistry;

public class ResourceRegistry implements IResourceRegistry {
   private IResourceContainer m_container;

   private Queue<DeferredTask> m_queue = new LinkedList<DeferredTask>();

   private boolean m_locked;

   public ResourceRegistry(IResourceContainer container) {
      m_container = container;
   }

   void checkLock() {
      if (m_locked) {
         throw new RuntimeException("The resource registry is locked now, please unlock it before access it!");
      }
   }

   @Override
   public IResourceContainer getContainer() {
      return m_container;
   }

   @Override
   public void lock() {
      m_locked = true;

      while (!m_queue.isEmpty()) {
         m_queue.remove().run(m_container);
      }
   }

   @Override
   public <T> T lookup(Class<T> type) {
      return lookup(type, IResourceContainer.DEFAULT_KEY);
   }

   @Override
   public <T> T lookup(Class<T> type, String key) {
      return (T) m_container.getAttribute(type, key);
   }

   @Override
   public <T> void register(Class<? super T> type, String key, T instance) {
      if (instance != null) {
         m_queue.add(new DeferredTask(instance));
      }

      if (key == null) {
         key = IResourceContainer.DEFAULT_KEY;
      }

      m_container.setAttribute(type, key, instance);
   }

   @Override
   public <T> void register(Class<? super T> type, T instance) {
      register(type, IResourceContainer.DEFAULT_KEY, instance);
   }

   @Override
   public <T> void register(IResourceRegisterable<T> registerable) {
      checkLock();

      T instance = registerable.getRegisterInstance();
      Class<? super T> type = registerable.getRegisterType();
      String key = registerable.getRegisterKey();

      register(type, key, instance);
   }

   public void unlock() {
      m_locked = false;
   }

   static class DeferredTask {
      private Object m_injectable;

      public DeferredTask(Object injectable) {
         m_injectable = injectable;
      }

      public void run(IResourceContainer container) {
         container.injectAttributes(m_injectable);
      }
   }
}
