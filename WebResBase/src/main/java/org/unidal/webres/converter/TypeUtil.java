package org.unidal.webres.converter;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class TypeUtil {
   public static Type getComponentType(final Type type) {
      if (type instanceof Class<?>) {
         final Class<?> clazz = (Class<?>) type;

         if (clazz.isArray()) {
            return clazz.getComponentType();
         }
      } else if (type instanceof ParameterizedType) {
         final ParameterizedType parameterizedType = (ParameterizedType) type;
         final Type[] actuallTypes = parameterizedType.getActualTypeArguments();

         return actuallTypes[0];
      } else if (type instanceof GenericArrayType) {
         final GenericArrayType genericArrayType = (GenericArrayType) type;

         return genericArrayType.getGenericComponentType();
      } else {
         throw new ConverterException("Unknown type: " + type);
      }

      return Object.class;
   }

   public static Class<?> getPrimitiveClass(final Class<?> clazz) {
      if (clazz == String.class) {
         return clazz;
      } else if (clazz == Integer.class) {
         return Integer.TYPE;
      } else if (clazz == Long.class) {
         return Long.TYPE;
      } else if (clazz == Boolean.class) {
         return Boolean.TYPE;
      } else if (clazz == Double.class) {
         return Double.TYPE;
      } else if (clazz == Float.class) {
         return Float.TYPE;
      } else if (clazz == Short.class) {
         return Short.TYPE;
      } else if (clazz == Character.class) {
         return Character.TYPE;
      } else if (clazz == Byte.class) {
         return Byte.TYPE;
      }

      return clazz;
   }

   public static Class<?> getRawType(final Type type) {
      final Class<?> clazz;

      if (type instanceof Class<?>) {
         clazz = (Class<?>) type;
      } else if (type instanceof ParameterizedType) {
         ParameterizedType parameterizedType = (ParameterizedType) type;

         clazz = (Class<?>) parameterizedType.getRawType();
      } else if (type instanceof GenericArrayType) {
         GenericArrayType genericArrayType = (GenericArrayType) type;
         Class<?> componentType = getRawType(genericArrayType.getGenericComponentType());

         clazz = Array.newInstance(componentType, 0).getClass();
      } else if (type instanceof TypeVariable<?>) {
         Type[] bounds = ((TypeVariable<?>) type).getBounds();
         if (bounds != null && bounds.length == 1) {
            Type upperBound = bounds[0];
            clazz = ConverterRuntime.INSTANCE.getManager().getRegistry().findType(getRawType(upperBound));
            if (clazz.isInterface()) {
               throw new ConverterException("Unknown type variable: " + type);
            }
         } else {
            throw new ConverterException("Unknown type variable: " + type);
         }
      } else {
         throw new ConverterException("Unknown type: " + type);
      }

      return clazz;
   }

   public static Class<?> getWrapClass(final Class<?> clazz) {
      if (clazz == String.class) {
         return clazz;
      } else if (clazz == Integer.TYPE) {
         return Integer.class;
      } else if (clazz == Long.TYPE) {
         return Long.class;
      } else if (clazz == Boolean.TYPE) {
         return Boolean.class;
      } else if (clazz == Double.TYPE) {
         return Double.class;
      } else if (clazz == Float.TYPE) {
         return Float.class;
      } else if (clazz == Short.TYPE) {
         return Short.class;
      } else if (clazz == Character.TYPE) {
         return Character.class;
      } else if (clazz == Byte.TYPE) {
         return Byte.class;
      }

      return clazz;
   }

   public static boolean isPrimaryClass(final Class<?> clazz) {
      if (clazz.isPrimitive()) {
         return true;
      } else {
         return getPrimitiveClass(clazz).isPrimitive();
      }
   }

   public static boolean isClassSupported(final Class<?> fromClass, final Class<?>... classes) {
      for (Class<?> clazz : classes) {
         if (clazz == fromClass || clazz.isAssignableFrom(fromClass)) {
            return true;
         }
      }

      return false;
   }

   public static boolean isUserDefinedClass(final Class<?> clazz) {
      return !clazz.isPrimitive() && !clazz.getName().startsWith("java");
   }

   public static Class<?> getConcreteClass(ConverterManager manager, final Type type) {
      final Class<?> rawType = TypeUtil.getRawType(type);

      if (rawType.isInterface()) {
         return manager.getRegistry().findType(rawType);
      } else {
         return getWrapClass(rawType);
      }
   }

   public static Type getActualTypeArgument(final Type type, final int index) {
      if (type instanceof ParameterizedType) {
         final ParameterizedType parameterizedType = (ParameterizedType) type;
         final Type[] arguments = parameterizedType.getActualTypeArguments();

         if (index >= 0 && index < arguments.length) {
            return arguments[index];
         } else {
            throw new IndexOutOfBoundsException(index + " not in [0," + (arguments.length - 1) + "]");
         }
      }

      throw new UnsupportedOperationException("not implementated yet");
   }

   public static boolean hasPublicStaticFields(final Class<?> clazz) {
      for (Field field : clazz.getFields()) {
         if (Modifier.isStatic(field.getModifiers())) {
            return true;
         }
      }

      return false;
   }

   public static Field getStaticField(final Class<?> clazz, final String name) throws SecurityException,
         NoSuchFieldException {
      final Field field = clazz.getField(name);

      if (Modifier.isStatic(field.getModifiers())) {
         return field;
      } else {
         return null;
      }
   }

   public static Type resolveType(ParameterizedType instanceType, Class<?> instanceClass, TypeVariable<?> parameterType) {
      final String name = parameterType.getName();
      final TypeVariable<?>[] typeParameters = instanceClass.getTypeParameters();

      for (int i = 0; i < typeParameters.length; i++) {
         final TypeVariable<?> t = typeParameters[i];

         if (t.getName().equals(name)) {
            return instanceType.getActualTypeArguments()[i];
         }
      }

      return parameterType;
   }
}
