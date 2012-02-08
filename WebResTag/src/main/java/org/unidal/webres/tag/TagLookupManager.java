package org.unidal.webres.tag;

import org.unidal.webres.resource.injection.ResourceContainer;
import org.unidal.webres.resource.spi.IResourceContainer;

public class TagLookupManager extends ResourceContainer implements ITagLookupManager {
   public TagLookupManager() {
      super(null);
   }

   public TagLookupManager(IResourceContainer container) {
      super(container);
   }

   @Override
   public boolean hasComponent(Class<?> type) {
      return hasAttribute(type);
   }

   @Override
   public boolean hasComponent(Class<?> type, String key) {
      return hasAttribute(type, key);
   }

   @Override
   public <T> T lookupComponent(Class<T> type) {
      return getAttribute(type);
   }

   @Override
   public <T> T lookupComponent(Class<T> type, String key) {
      return getAttribute(type, key);
   }

   @Override
   public <T> Object registerComponent(Class<? super T> type, String key, T object) {
      return setAttribute(type, key, object);
   }

   @Override
   @SuppressWarnings("unchecked")
   public <T> Object registerComponent(String key, T object) {
      return setAttribute((Class<T>) object.getClass(), key, object);
   }

   @SuppressWarnings("unchecked")
   @Override
   public <T> Object registerComponent(T object) {
      return setAttribute((Class<T>) object.getClass(), object);
   }
}
