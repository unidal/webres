package org.unidal.webres.resource.spi;

public interface IResourceRegistry {
   public IResourceContainer getContainer();

   public void lock();

   public <T> T lookup(Class<T> type);

   public <T> T lookup(Class<T> type, String key);

   public <T> void register(Class<? super T> type, String key, T instance);

   public <T> void register(Class<? super T> type, T instance);

   public <T> void register(IResourceRegisterable<T> registerable);
}
