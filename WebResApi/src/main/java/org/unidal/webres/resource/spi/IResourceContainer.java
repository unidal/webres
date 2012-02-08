package org.unidal.webres.resource.spi;

public interface IResourceContainer {
   public static final String DEFAULT_KEY = "default";

   public <T> T getAttribute(Class<T> type);

   public <T> T getAttribute(Class<T> type, String key);

   public boolean hasAttribute(Class<?> type);

   public boolean hasAttribute(Class<?> type, String key);

   public void injectAttributes(Object injectable);

   public <T> T removeAttribute(Class<? super T> type, String key);

   public <T> Object setAttribute(Class<? super T> type, String key, T object);

   public <T> Object setAttribute(Class<? super T> type, T object);

   public <T> Object setAttribute(String key, T object);
}
