package org.unidal.webres.helper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.converter.ConverterRuntime;
import org.unidal.webres.converter.TypeUtil;

public class Proxies {
   public static ObjectProxy forObject() {
      return new ObjectProxy();
   }

   public static interface IAccessor<T> {
      public Object getValue(T instance, Method method, Object[] args) throws Throwable;

      public boolean isEligible(T instance, Method method, Object[] args);
   }

   private static Object convertedValue(Object from, Type targetType) {
      Object value = null;
      try {
         value = ConverterRuntime.INSTANCE.getManager().convert(from, targetType);
      } catch (ConverterException e) {
         // Ignore it
      }
      return value;
   }

   public static class ListAccessor implements IAccessor<List<Object>> {
      private Map<String, Integer> m_mapping;

      public ListAccessor(Map<String, Integer> mapping) {
         m_mapping = mapping;
         if (m_mapping == null) {
            throw new RuntimeException("Mapping info cannot be null.");
         }
      }

      @Override
      public Object getValue(List<Object> instance, Method method, Object[] args) throws Throwable {
         Integer locIndex = m_mapping.get(method.getName());

         return convertedValue(instance.get(locIndex - 1), method.getReturnType());
      }

      @Override
      public boolean isEligible(List<Object> instance, Method method, Object[] args) {
         Integer locIndex = m_mapping.get(method.getName());

         Class<?> methodReturnType = method.getReturnType();
         return locIndex != null && instance.size() >= locIndex
               && convertedValue(instance.get(locIndex - 1), methodReturnType) != null;
      }
   }

   public static class MapAccessor implements IAccessor<Map<String, Object>> {
      private Map<String, String> m_mapping;

      public MapAccessor() {
         this(Collections.<String, String> emptyMap());
      }

      public MapAccessor(Map<String, String> mapping) {
         m_mapping = mapping;
         if (m_mapping == null) {
            throw new RuntimeException("Mapping info cannot be null.");
         }
      }

      @Override
      public Object getValue(Map<String, Object> map, Method method, Object[] args) {
         String key = m_mapping.get(method.getName());

         if (key == null) {
            key = method.getName();
         }

         return convertedValue(map.get(key), method.getReturnType());
      }

      @Override
      public boolean isEligible(Map<String, Object> map, Method method, Object[] args) {
         String key = m_mapping.get(method.getName());

         if (key == null) {
            key = method.getName();
         }

         return map.containsKey(key) && convertedValue(map.get(key), method.getReturnType()) != null;
      }

   }

   public static class ObjectProxy {
      public <T> T newInstance(ClassLoader classLoader, Object implementation, Class<T> firstInterface,
            Class<?>... otherInterfaces) {
         return (T) newInstance(classLoader, implementation, new PojoAccessor(), firstInterface, otherInterfaces);
      }

      @SuppressWarnings("unchecked")
      public <T> T newInstance(ClassLoader classLoader, Object implementation, IAccessor<?> accessor,
            Class<T> firstInterface, Class<?>... otherInterfaces) {
         if (implementation == null) {
            throw new RuntimeException("Implementation object should not be null!");
         } else if (accessor == null) {
            throw new RuntimeException("Accessor should not be null!");
         }

         int len = otherInterfaces.length + 1;
         Class<?>[] interfaces = new Class<?>[len];

         interfaces[0] = firstInterface;

         if (len > 1) {
            System.arraycopy(otherInterfaces, 0, interfaces, 1, len - 1);
         }
         ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
         Object instance = null;
         try {
            instance = Proxy.newProxyInstance(contextClassLoader, interfaces, new ProxyInvocationHandler(
                  implementation, (IAccessor<Object>) accessor));
         } catch (IllegalArgumentException e) {
            //fallback
            instance = Proxy.newProxyInstance(classLoader, interfaces, new ProxyInvocationHandler(implementation,
                  (IAccessor<Object>) accessor));
         }
         return (T) instance;
      }

      public <T> T newInstance(Object implementation, Class<T> firstInterface, Class<?>... otherInterfaces) {
         return (T) newInstance(Thread.currentThread().getContextClassLoader(), implementation, new PojoAccessor(),
               firstInterface, otherInterfaces);
      }

      public <T> T newInstance(Object implementation, IAccessor<?> accessor, Class<T> firstInterface,
            Class<?>... otherInterfaces) {
         return (T) newInstance(Thread.currentThread().getContextClassLoader(), implementation, accessor,
               firstInterface, otherInterfaces);
      }
   }

   public static class PojoAccessor implements IAccessor<Object> {
      private Map<String, String> m_mapping;

      public PojoAccessor() {
         this(Collections.<String, String> emptyMap());
      }

      public PojoAccessor(Map<String, String> mapping) {
         m_mapping = mapping;
         if (m_mapping == null) {
            throw new RuntimeException("Mapping info cannot be null.");
         }
      }

      /**
       * Param Types should be extactly match, think about the below cases.
       * String getName(int age);
       * String getName(String name);
       * @param args
       * @param paramTypes
       * @return
       */
      private boolean checkParamTypes(Object[] args, Class<?>[] paramTypes) {
         int len1 = (args == null ? 0 : paramTypes.length);
         int len2 = (args == null ? 0 : args.length);

         if (len1 == len2) {
            for (int i = 0; i < len1; i++) {
               Class<?> argsType = args[i].getClass();
               if (TypeUtil.isPrimaryClass(argsType)) {
                  argsType = TypeUtil.getPrimitiveClass(argsType);
               }
               if (!paramTypes[i].isAssignableFrom(argsType)) {
                  return false;
               }
            }
            return true;
         }
         return false;
      }

      private Method getMethod(Object pojo, Method method, String targetMethodName, Object[] args) {
         Method[] methods = pojo.getClass().getMethods();

         for (Method m : methods) {
            if (m.getName().equals(targetMethodName)) {
               boolean paramTypesMatch = checkParamTypes(args, method.getParameterTypes());
               boolean returnTypeMatch = false;
               try {
                  Object invokeResult = m.invoke(pojo, args);
                  returnTypeMatch = convertedValue(invokeResult, method.getReturnType()) != null;
               } catch (Exception e) {
                  //ignore it
               }

               if (paramTypesMatch && returnTypeMatch) {
                  return m;
               }
            }
         }
         return null;
      }

      private Method getMethodWithMapping(Object pojo, Method method, Object[] args) {
         String targetMethodName = method.getName();

         if (m_mapping.containsKey(targetMethodName)) {
            targetMethodName = m_mapping.get(targetMethodName);
         }

         return getMethod(pojo, method, targetMethodName, args);
      }

      @Override
      public Object getValue(Object pojo, Method method, Object[] args) throws Throwable {
         Method targetMethod = getMethodWithMapping(pojo, method, args);

         if (targetMethod == null) {
            throw new RuntimeException("Internal error, isEligible() should be called first!");
         }

         if (!targetMethod.isAccessible()) {
            targetMethod.setAccessible(true);
         }

         return convertedValue(targetMethod.invoke(pojo, args), method.getReturnType());
      }

      @Override
      public boolean isEligible(Object pojo, Method method, Object[] args) {
         return getMethodWithMapping(pojo, method, args) != null;
      }
   }

   public static class PrimitiveAccessor implements IAccessor<Object> {
      @Override
      public Object getValue(Object instance, Method method, Object[] args) throws Throwable {
         return convertedValue(instance, method.getReturnType());
      }

      @Override
      public boolean isEligible(Object instance, Method method, Object[] args) {
         return convertedValue(instance, method.getReturnType()) != null;
      }
   }

   static class ProxyInvocationHandler implements InvocationHandler {
      private IAccessor<Object> m_accessor;

      private Object m_instance;

      public ProxyInvocationHandler(Object instance, IAccessor<Object> accessor) {
         m_instance = instance;
         m_accessor = accessor;
      }

      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         String name = method.getName();

         if (m_accessor.isEligible(m_instance, method, args)) {
            return m_accessor.getValue(m_instance, method, args);
         } else {
            throw new UnsupportedOperationException(String.format("No method(%s) defined in %s", name, m_instance
                  .getClass()));
         }
      }
   }

   public static class RouterAccessor implements IAccessor<Object> {
      private IAccessor<Object> m_accessor;

      private Object m_value;

      @SuppressWarnings("unchecked")
      public RouterAccessor(Object value, Map<String, String> mapping) {
         IAccessor<?> accessor;

         m_value = value;
         if (value == null) {
            throw new RuntimeException("Value should not be null!");
         } else if (isPrimitiveValue(value)) {
            accessor = new PrimitiveAccessor();
         } else if (value instanceof Map<?, ?>) {
            m_value = convertToGetPrefixByMap((Map<String, Object>) value);
            accessor = new MapAccessor(convertToGetPrefix(mapping));
         } else if (value instanceof List<?>) {
            accessor = new ListAccessor(convertToGetPrefixByLoc(mapping));
         } else {
            accessor = new PojoAccessor(convertToGetPrefix(mapping));
         }

         m_accessor = (IAccessor<Object>) accessor;
      }

      private Map<String, String> convertToGetPrefix(Map<String, String> mapping) {
         Map<String, String> newMapping = new HashMap<String, String>();

         if (mapping != null) {
            for (Entry<String, String> e : mapping.entrySet()) {
               newMapping.put(convertAttrToMethodName(e.getValue()), convertAttrToMethodName(e.getKey()));
            }
         }
         return newMapping;
      }

      private Map<String, Object> convertToGetPrefixByMap(Map<String, Object> map) {
         Map<String, Object> newMapping = new HashMap<String, Object>();

         if (map != null) {
            for (Entry<String, Object> e : map.entrySet()) {
               newMapping.put(convertAttrToMethodName(e.getKey()), e.getValue());
            }
         }
         return newMapping;
      }

      private Map<String, Integer> convertToGetPrefixByLoc(Map<String, String> mapping) {
         Map<String, Integer> newMapping = new HashMap<String, Integer>();

         if (mapping != null) {
            for (Entry<String, String> e : mapping.entrySet()) {
               newMapping.put(convertAttrToMethodName(e.getValue()), Integer.valueOf(e.getKey()));
            }
         }

         return newMapping;
      }

      private String convertAttrToMethodName(String attr) {
         return "get" + attr.substring(0, 1).toUpperCase() + attr.substring(1);
      }

      public IAccessor<Object> getAccessor() {
         return m_accessor;
      }

      @Override
      public Object getValue(Object instance, Method method, Object[] args) throws Throwable {
         return m_accessor.getValue(m_value, method, args);
      }

      @Override
      public boolean isEligible(Object instance, Method method, Object[] args) {
         return m_accessor.isEligible(m_value, method, args);
      }

      protected boolean isPrimitiveValue(Object value) {
         Class<?> clazz = TypeUtil.getPrimitiveClass(value.getClass());

         return clazz.isPrimitive() || value instanceof String || value instanceof Date;
      }
   }
}
