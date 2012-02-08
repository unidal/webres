package org.unidal.webres.converter;

import java.lang.reflect.Method;

import org.unidal.webres.helper.Caches;

public final class ConverterUtil {

   public static String getGetMethodName(final String name) {
      final int len = name.length();
      final char[] cb = new char[len + 3];
      int index = 0;
      boolean uppercase = true;

      cb[index++] = 'g';
      cb[index++] = 'e';
      cb[index++] = 't';

      for (int i = 0; i < len; i++) {
         final char ch = name.charAt(i);

         if (ch == '-') {
            uppercase = true;
         } else if (uppercase) {
            cb[index++] = Character.toUpperCase(ch);
            uppercase = false;
         } else {
            cb[index++] = ch;
         }
      }

      return new String(cb, 0, index);
   }

   public static Method getGetMethod(final Class<?> clazz, final String methodName) {
      ConverterMethodKey key = new ConverterMethodKey(clazz, methodName, 0);

      Method method = Caches.forMethod().get(key);
      if (method == null) {
         Method[] methods = clazz.getMethods();

         for (Method e : methods) {
            if (e.getName().equalsIgnoreCase(methodName) && e.getParameterTypes().length == 0) {
               method = e;
               break;
            }
         }

         if (method != null) {
            Caches.forMethod().put(key, method);
         }
      }

      if (method == null) {
         throw new RuntimeException("Can't find get method(" + methodName + ") in " + clazz);
      } else {
         return method;
      }
   }

   public static String getSetMethodName(final String name) {
      final int len = name.length();
      final char[] cb = new char[len + 3];
      int index = 0;
      boolean uppercase = true;

      cb[index++] = 's';
      cb[index++] = 'e';
      cb[index++] = 't';

      for (int i = 0; i < len; i++) {
         final char ch = name.charAt(i);

         if (ch == '-') {
            uppercase = true;
         } else if (uppercase) {
            cb[index++] = Character.toUpperCase(ch);
            uppercase = false;
         } else {
            cb[index++] = ch;
         }
      }

      return new String(cb, 0, index);
   }

   public static Method getSetMethod(final Class<?> clazz, final String methodName) {
      return getSetMethod(clazz, methodName, null);
   }

   public static Method getSetMethod(final Class<?> clazz, final String methodName, Class<?> parameterType) {
      ConverterMethodKey key = new ConverterMethodKey(clazz, methodName, 1);

      Method method = Caches.forMethod().get(key);
      if (method == null) {
         Method[] methods = clazz.getMethods();

         for (Method e : methods) {
            if (e.getName().equalsIgnoreCase(methodName) && e.getParameterTypes().length == 1
                  && (parameterType == null || e.getParameterTypes()[0].getName().equals(parameterType.getName()))) {
               method = e;
               break;
            }
         }

         if (method != null) {
            Caches.forMethod().put(key, method);
         }
      }

      if (method == null) {
         throw new RuntimeException("Can't find set method(" + methodName + ") in " + clazz);
      } else {
         return method;
      }
   }

   static class ConverterMethodKey implements Caches.MethodCache.Key {
      private Class<?> m_clazz;

      private String m_methodName;

      private int m_parameterLength;

      private int m_hash;

      public ConverterMethodKey(Class<?> clazz, String methodName, int parameterLength) {
         m_clazz = clazz;
         m_methodName = methodName;
         m_parameterLength = parameterLength;
      }

      @Override
      public boolean equals(Object obj) {
         if (obj instanceof ConverterMethodKey) {
            ConverterMethodKey key = (ConverterMethodKey) obj;

            return key.m_clazz == m_clazz && key.m_parameterLength == m_parameterLength
                  && key.m_methodName.equals(m_methodName);
         }

         return false;
      }

      @Override
      public int hashCode() {
         if (m_hash == 0) {
            int hash = m_clazz.hashCode();

            hash = hash * 31 + m_methodName.hashCode();
            hash = hash * 31 + m_parameterLength;
            m_hash = hash;
         }

         return m_hash;
      }

   }
}
