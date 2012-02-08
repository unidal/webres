package org.unidal.webres.resource.expression;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class BaseExpression<T> implements Map<String, T> {
   @Override
   public void clear() {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean containsKey(Object key) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean containsValue(Object value) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean isEmpty() {
      throw new UnsupportedOperationException();
   }

   @Override
   public Set<String> keySet() {
      throw new UnsupportedOperationException();
   }

   @Override
   public T put(String key, T value) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void putAll(Map<? extends String, ? extends T> map) {
      throw new UnsupportedOperationException();

   }

   @Override
   public T remove(Object key) {
      throw new UnsupportedOperationException();
   }

   @Override
   public int size() {
      throw new UnsupportedOperationException();
   }

   @Override
   public Collection<T> values() {
      throw new UnsupportedOperationException();
   }

}
