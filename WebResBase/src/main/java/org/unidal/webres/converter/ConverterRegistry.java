package org.unidal.webres.converter;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConverterRegistry {
   private volatile Map<Class<?>, ConverterEntry> m_converterMap = new HashMap<Class<?>, ConverterEntry>();

   /**
    * Interface or abstract class to concrete class mapping.
    * 
    * This mapping is used to locate a concrete class when an interface or
    * abstract class is used in the setter method of data/display model, as well
    * as its referenced types.
    */
   private Map<Class<?>, Class<?>> m_typeMap = new HashMap<Class<?>, Class<?>>();

   public List<Converter<?>> findConverters(final ConverterContext ctx) {
      final boolean isClass = ctx.getTargetType() instanceof Class<?>;
      final Class<?> fromClass = ctx.getSourceClass();
      Class<?> targetClass = ctx.getTargetClass();

      // Is a primitive type? convert it to wrapper type
      if (isClass && targetClass.isPrimitive()) {
         targetClass = TypeUtil.getWrapClass(targetClass);
      }

      ConverterEntry entry = m_converterMap.get(targetClass);
      List<Converter<?>> converters = (entry == null ? null : entry.getCacheConverters(fromClass));

      if (converters == null) {
         converters = new ArrayList<Converter<?>>(5);

         if (isClass && targetClass.isArray()) {
            // is Array type?
            getConverters(ctx, converters, Array.class);
         } else {
            if (targetClass != Object.class) {
               // try specific converter
               getConverters(ctx, converters, targetClass);
            }

            // try generic one
            getConverters(ctx, converters, Type.class);
         }

         //re-order converter
         //Fix tagnoderefconverter is not first converter bug
         sortConverters(converters);

         synchronized (m_converterMap) {
            entry = m_converterMap.get(targetClass);

            if (entry == null) {
               entry = new ConverterEntry(targetClass);
               entry.putCacheConverters(fromClass, converters);

               m_converterMap.put(targetClass, entry);
            }
         }
      }

      if (converters.size() > 0) {
         return converters;
      } else {
         throw new ConverterException("No registered converter found to convert from " + ctx.getSourceClass() + " to "
               + ctx.getTargetType());
      }
   }

   private void sortConverters(List<Converter<?>> converters) {
      int pos = 0;

      for (int i = 0; i < converters.size(); i++) {
         Converter<?> c = converters.get(i);
         if (c instanceof IRefConverter<?>) {
            pos = i;
            break;
         }
      }

      if (pos != 0) {
         Converter<?> c = converters.get(pos);
         converters.remove(pos);
         converters.add(0, c);
      }
   }

   public Class<?> findType(final Class<?> abstractClass) {
      final Class<?> concreteClass = m_typeMap.get(abstractClass);

      if (concreteClass != null) {
         return concreteClass;
      } else {
         return abstractClass;
      }
   }

   private void getConverters(final ConverterContext ctx, final List<Converter<?>> converters,
         final Class<?> targetClass) {
      final List<Class<?>> classes = getSuperClassesAndInterfaces(targetClass);

      for (final Class<?> clazz : classes) {
         final ConverterEntry entry = m_converterMap.get(clazz);

         if (entry != null) {
            for (final Converter<?> c : entry.getConverters()) {
               if (c.canConvert(ctx)) {
                  converters.add(c);
               }
            }
         }
      }
   }

   private List<Class<?>> getSuperClassesAndInterfaces(final Class<?> clazz) {
      final List<Class<?>> classes = new ArrayList<Class<?>>();
      Class<?> current = clazz;

      while (true) {
         classes.add(current);

         current = current.getSuperclass();

         if (current == null || current == Object.class) {
            break;
         }
      }

      for (final Class<?> i : clazz.getInterfaces()) {
         if (!classes.contains(i)) {
            classes.add(i);
         }
      }

      return classes;
   }

   public void registerConverter(Converter<?> converter) {
      registerConverter(converter, ConverterPriority.NORMAL.getValue());
   }

   public void registerConverter(Converter<?> converter, int priority) {
      Type targetType = converter.getTargetType();
      Class<?> targetClass = TypeUtil.getRawType(targetType);
      ConverterEntry entry = m_converterMap.get(targetClass);

      if (entry == null) {
         synchronized (m_converterMap) {
            entry = m_converterMap.get(targetClass);

            if (entry == null) {
               entry = new ConverterEntry(targetClass);
               m_converterMap.put(targetClass, entry);
            }
         }
      }

      entry.addConverter(converter, priority);
   }

   public void registerType(Class<?> fromClass, Class<?> toClass) {
      Class<?> oldClass = m_typeMap.put(fromClass, toClass);

      if (oldClass != null && oldClass != toClass) {
         throw new IllegalArgumentException("Map to same " + fromClass + " from " + oldClass + " and " + toClass);
      }
   }

   private static class ConverterEntry {
      private Class<?> m_targetClass;

      // list of converters that support converting to the target class
      private List<Converter<?>> m_converters;

      // list of priorities for the converters
      private List<Integer> m_priorities;

      // cache map from fromClass to Converter
      private Map<Class<?>, List<Converter<?>>> m_cacheMap;

      public ConverterEntry(Class<?> targetClass) {
         m_targetClass = targetClass;
         m_converters = new ArrayList<Converter<?>>();
         m_priorities = new ArrayList<Integer>();
         m_cacheMap = new HashMap<Class<?>, List<Converter<?>>>();
      }

      public void addConverter(Converter<?> converter, int priority) {
         if (!m_converters.contains(converter)) {
            int size = m_priorities.size();
            int index = size;

            for (int i = 0; i < size; i++) {
               if (priority > m_priorities.get(i)) {
                  index = i;
                  break;
               }
            }

            m_priorities.add(index, priority);
            m_converters.add(index, converter);
         } else {
            System.out.println("Converter already registered: " + converter);
         }
      }

      public List<Converter<?>> getCacheConverters(Class<?> fromClass) {
         return m_cacheMap.get(fromClass);
      }

      public List<Converter<?>> getConverters() {
         return m_converters;
      }

      @SuppressWarnings("unused")
      public Class<?> getTargetClass() {
         return m_targetClass;
      }

      public void putCacheConverters(Class<?> fromClass, List<Converter<?>> converters) {
         m_cacheMap.put(fromClass, converters);
      }
   }
}
