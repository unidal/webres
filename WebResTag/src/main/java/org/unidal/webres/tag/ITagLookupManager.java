package org.unidal.webres.tag;

public interface ITagLookupManager {
   public boolean hasComponent(Class<?> type);

   public boolean hasComponent(Class<?> type, String key);

   public <T> T lookupComponent(Class<T> type);

   public <T> T lookupComponent(Class<T> type, String key);

   public <T> Object registerComponent(Class<? super T> type, String key, T object);

   public <T> Object registerComponent(String key, T object);

   public <T> Object registerComponent(T object);
}
