package org.unidal.webres.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.converter.ConverterRuntime;

public class Reflects {
   private Reflects() {
   }

   public static ClassReflector forClass() {
      return ClassReflector.INSTANCE;
   }

   public static ConstructorReflector forConstructor() {
      return ConstructorReflector.INSTANCE;
   }

   public static FieldReflector forField() {
      return FieldReflector.INSTANCE;
   }

   public static MethodReflector forMethod() {
      return new MethodReflector();
   }

   public static ModifierReflector forModifier() {
      return ModifierReflector.INSTANCE;
   }

   // Moved to WebResTaglib
   //   public static ResourceReflector forResource() {
   //      return ResourceReflector.INSTANCE;
   //   }

   public static enum ClassReflector {
      INSTANCE;

      /**
       * for class name like "a.b.C" or "a.b.C$D$E"
       * @param className
       * @return class from current context class loader
       */
      public Class<?> getClass(String className) {
         return getClass(className, null);
      }

      /**
       * for class name like "a.b.C" or "a.b.C$D$E"
       * @param className
       * @param classloader
       * @return class from current context class loader
       */
      public Class<?> getClass(String className, ClassLoader classloader) {
         Class<?> clazz = null;

         if (classloader != null) {
            try {
               clazz = classloader.loadClass(className);
            } catch (ClassNotFoundException e) {
               //ignore it
            }
         } else {
            //step1: try to load from caller class loader
            try {
               clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
               //step2: try to load from thread context class loader
               if (clazz == null) {
                  clazz = getClass(className, Thread.currentThread().getContextClassLoader());
               }

               //step3: try to load from current-class class loader
               if (clazz == null) {
                  clazz = getClass(className, Reflects.class.getClassLoader());
               }
            }
         }

         return clazz;
      }

      /**
       * for class name like "a.b.C" or "a.b.C.D.E"
       * @param className
       * @return class from current context class loader
       */
      public Class<?> getClass2(String className) {
         return getClass2(className, null);
      }

      /**
       * for class name like "a.b.C" or "a.b.C.D.E"
       * @param className
       * @return class from current context class loader
       */
      public Class<?> getClass2(String className, ClassLoader classloader) {
         Class<?> clazz = null;
         String name = className;

         while (true) {
            clazz = getClass(name, classloader);

            if (clazz != null) {
               break;
            }

            //try with inner class name
            int pos = name.lastIndexOf('.');
            if (pos < 0) {
               break;
            }
            name = name.substring(0, pos) + '$' + name.substring(pos + 1);
         }

         return clazz;
      }

      public Class<?> getNestedClass(Class<?> clazz, String simpleName) {
         if (clazz != null) {
            Class<?>[] subClasses = clazz.getDeclaredClasses();

            if (subClasses != null) {
               for (Class<?> subClass : subClasses) {
                  if (subClass.getSimpleName().equals(simpleName)) {
                     return subClass;
                  }
               }
            }
         }

         return null;
      }

      public Class<?> getNestedClass(String className, String simpleName) {
         return getNestedClass(getClass(className), simpleName);
      }

      public Class<?> getNestedClass(String className, String simpleName, ClassLoader classloader) {
         return getNestedClass(getClass(className, classloader), simpleName);
      }
   }

   public static enum ConstructorReflector {
      INSTANCE;

      public Object createInstance(Class<?> clazz, Object... typesAndParameters) {
         try {
            TypeArguments typeArgs = new TypeArguments(typesAndParameters);

            Constructor<?> constructor = clazz.getConstructor(typeArgs.getTypes());

            return constructor.newInstance(typeArgs.getArguments());
         } catch (Exception e) {
            // ignore it
         }

         return null;
      }
   }

   public static enum FieldFilter implements IMemberFilter<Field> {
      PUBLIC {
         public boolean filter(Field field) {
            return ModifierReflector.INSTANCE.isPublic(field);
         }
      },

      STATIC {
         public boolean filter(Field field) {
            return ModifierReflector.INSTANCE.isStatic(field);
         }
      },

      PUBLIC_STATIC {
         public boolean filter(Field field) {
            return ModifierReflector.INSTANCE.isPublic(field) && ModifierReflector.INSTANCE.isStatic(field);
         }
      };
   }

   public static enum FieldReflector {
      INSTANCE;

      public List<Field> getDeclaredFields(Class<?> clazz, IMemberFilter<Field> filter) {
         List<Field> list = new ArrayList<Field>();
         Field[] fields = clazz.getDeclaredFields();

         for (Field field : fields) {
            if (filter == null || filter.filter(field)) {
               list.add(field);
            }
         }

         return list;
      }

      @SuppressWarnings("unchecked")
      public <T> T getDeclaredFieldValue(Class<?> clazz, String fieldName, Object instance) {
         if (clazz != null) {
            try {
               Field field = clazz.getDeclaredField(fieldName);

               if (!field.isAccessible()) {
                  field.setAccessible(true);
               }

               return (T) field.get(instance);
            } catch (Exception e) {
               // ignore
            }
         }

         return null;
      }

      public List<Field> getFields(Class<?> clazz, IMemberFilter<Field> filter) {
         List<Field> list = new ArrayList<Field>();
         Field[] fields = clazz.getFields();

         for (Field field : fields) {
            if (filter == null || filter.filter(field)) {
               list.add(field);
            }
         }

         return list;
      }

      @SuppressWarnings("unchecked")
      public <T> T getFieldValue(Object instance, String fieldName) {
         if (instance != null) {
            try {
               Field field = instance.getClass().getField(fieldName);

               return (T) field.get(instance);
            } catch (Exception e) {
               // ignore
            }
         }

         return null;
      }

      @SuppressWarnings("unchecked")
      public <T> T getStaticFieldValue(Class<?> clazz, String fieldName) {
         if (clazz != null) {
            try {
               Field field = clazz.getField(fieldName);

               return (T) field.get(null);
            } catch (Exception e) {
               // ignore
            }
         }

         return null;
      }

      @SuppressWarnings("unchecked")
      public <T> T getStaticFieldValue(String className, String fieldName) {
         try {
            Class<?> clazz = forClass().getClass(className);

            if (clazz != null) {
               return (T) getStaticFieldValue(clazz, fieldName);
            }
         } catch (Exception e) {
            // ignore it
         }

         return null;
      }
   }

   public static interface IMemberFilter<T extends Member> {
      public boolean filter(T member);
   }

   public static enum MethodFilter implements IMemberFilter<Method> {
      PUBLIC {
         public boolean filter(Method method) {
            return ModifierReflector.INSTANCE.isPublic(method);
         }
      },

      STATIC {
         public boolean filter(Method method) {
            return ModifierReflector.INSTANCE.isStatic(method);
         }
      },

      PUBLIC_STATIC {
         public boolean filter(Method method) {
            return ModifierReflector.INSTANCE.isPublic(method) && ModifierReflector.INSTANCE.isStatic(method);
         }
      };
   }

   public static class MethodReflector {
      public Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
         try {
            return clazz.getDeclaredMethod(methodName, parameterTypes);
         } catch (Exception e) {
            // ignore it
         }

         return null;
      }

      public List<Method> getDeclaredMethods(Class<?> clazz, IMemberFilter<Method> filter) {
         List<Method> list = new ArrayList<Method>();

         try {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
               if (filter == null || filter.filter(method)) {
                  list.add(method);
               }
            }
         } catch (Exception e) {
            // ignore it
         }

         return list;
      }

      public String getGetMethodName(String propertyName) {
         int len = propertyName == null ? 0 : propertyName.length();

         if (len == 0) {
            throw new IllegalArgumentException(String.format("Invalid property name: %s!", propertyName));
         }

         StringBuilder sb = new StringBuilder(len + 3);

         sb.append("get");
         sb.append(Character.toUpperCase(propertyName.charAt(0)));
         sb.append(propertyName.substring(1));

         return sb.toString();
      }

      public String getSetMethodName(String propertyName) {
         int len = propertyName == null ? 0 : propertyName.length();

         if (len == 0) {
            throw new IllegalArgumentException(String.format("Invalid property name: %s!", propertyName));
         }

         StringBuilder sb = new StringBuilder(len + 3);

         sb.append("set");
         sb.append(Character.toUpperCase(propertyName.charAt(0)));
         sb.append(propertyName.substring(1));

         return sb.toString();
      }

      public Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
         try {
            return clazz.getMethod(methodName, parameterTypes);
         } catch (Exception e) {
            // ignore it
         }

         return null;
      }

      public List<Method> getMethods(Class<?> clazz, IMemberFilter<Method> filter) {
         List<Method> list = new ArrayList<Method>();

         try {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
               if (filter == null || filter.filter(method)) {
                  list.add(method);
               }
            }
         } catch (Exception e) {
            // ignore it
         }

         return list;
      }

      @SuppressWarnings("unchecked")
      public <T> T getPropertyValue(Object instance, String propertyName) {
         if (propertyName.length() > 0) {
            String suffix = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
            Method method = getMethod(instance.getClass(), "get" + suffix);

            // try isXXX() method for boolean return type
            if (method == null) {
               method = getMethod(instance.getClass(), "is" + suffix);

               if (method != null && method.getReturnType() != Boolean.TYPE) {
                  method = null;
               }
            }

            if (method != null) {
               try {
                  if (!method.isAccessible()) {
                     method.setAccessible(true);
                  }

                  return (T) method.invoke(instance);
               } catch (Exception e) {
                  // ignore it
               }
            }
         }

         return null;
      }

      public void setPropertyValue(Object instance, String propertyName, Object value) throws Exception {
         final String methodName = getSetMethodName(propertyName);

         Class<?> clazz = instance.getClass();
         //         Class<?> paramType = value != null ? value.getClass() : ;
         boolean[] invoked = new boolean[1];
         try {
            invokeMatchedMethod0(invoked, instance, clazz, methodName, value);
         } catch (Exception nse) {
            //Ignore
         }

         //Can't match try to matched by name
         if (!invoked[0]) {
            List<Method> methods = getMethods(clazz, new IMemberFilter<Method>() {
               @Override
               public boolean filter(Method member) {
                  return (member.getParameterTypes().length == 1 && methodName.equals(member.getName()));
               }
            });

            if (methods.isEmpty()) {
               throw new IllegalArgumentException("No such method:" + methodName + " for the value:" + value);
            }

            if (methods.size() > 1) {
               throw new IllegalStateException("There are " + methods.size() + " methods with name:" + methodName
                     + " for null value. Can't know invoking which one");
            } else {
               if (value == null) {
                  //Invoke the only one
                  Method methodMatched = methods.get(0);
                  methodMatched.invoke(instance, (Object) null);
               } else {
                  //Try to convert it
                  throw new IllegalArgumentException("No such method:" + methodName + " for the value:" + value);
               }
            }
         }
      }

      public Method getMatchedMethod(Class<?> clazz, String methodName, Object... paramValues) {
         Method[] methods = clazz.getMethods();
         Method method;
         Class<?>[] classes;
         for (int i = 0, len = methods.length; i < len; i++) {
            method = methods[i];
            if (methodName.equals(method.getName())) {
               classes = method.getParameterTypes();
               if (classes.length < paramValues.length) {
                  continue;
               }
               if (isParamMatch(classes, paramValues, false)) {
                  return method;
               }
            }
         }
         return null;
      }

      public Object invokeMatchedMethod(Object obj, Class<?> clazz, String methodName, Object... paramValues)
            throws InvocationTargetException, IllegalAccessException {
         Method[] methods = clazz.getMethods();
         Method method;
         Class<?>[] classes;
         for (int i = 0, len = methods.length; i < len; i++) {
            method = methods[i];
            if (Modifier.isPublic(method.getModifiers()) && methodName.equals(method.getName())) {
               classes = method.getParameterTypes();
               if (classes.length < paramValues.length) {
                  continue;
               }
               if (isParamMatch(classes, paramValues, true)) {
                  return method.invoke(obj, paramValues);
               }
            }
         }
         return null;
      }

      private Object invokeMatchedMethod0(boolean[] invoked, Object obj, Class<?> clazz, String methodName, Object... paramValues)
            throws InvocationTargetException, IllegalAccessException {
         Method[] methods = clazz.getMethods();
         Method method;
         Class<?>[] classes;
         for (int i = 0, len = methods.length; i < len; i++) {
            method = methods[i];
            if (Modifier.isPublic(method.getModifiers()) && methodName.equals(method.getName())) {
               classes = method.getParameterTypes();
               if (classes.length < paramValues.length) {
                  continue;
               }
               if (isParamMatch(classes, paramValues, true)) {
                  invoked[0] = true;
                  return method.invoke(obj, paramValues);
               }
            }
         }
         return null;
      }

      @SuppressWarnings({ "rawtypes", "unchecked" })
      public boolean isParamMatch(Class<?>[] paramTypes, Object[] paramValues, boolean convertValue) {
         //         Class<?> clazz;
         Object[] newParamValues = new Object[paramValues.length];
         for (int i = 0, len = paramTypes.length; i < len; i++) {
            if (paramValues[i] != null) {
               if (paramTypes[i].isAssignableFrom(paramValues[i].getClass())) {
                  newParamValues[i] = paramValues[i];
               } else if (Enum.class.isAssignableFrom(paramTypes[i]) && paramValues[i] instanceof String) {
                  try {
                     newParamValues[i] = Enum.valueOf((Class<? extends Enum>) paramTypes[i], (String) paramValues[i]);
                  } catch (Exception ex) {
                     return false;
                  }
               } else {
                  try {
                     newParamValues[i] = ConverterRuntime.INSTANCE.getManager().convert(paramValues[i], paramTypes[i]);
                  } catch (Exception ex) {
                     return false;
                  }
               }
            }
         }

         if (convertValue) {
            for (int i = 0, len = paramValues.length; i < len; i++) {
               paramValues[i] = newParamValues[i];
            }
         }
         return true;
      }

      @SuppressWarnings("unchecked")
      public <T> T invokeDeclaredMethod(Object instance, String methodName, Object... typesAndParameters) {
         if (instance == null) {
            return null;
         }

         TypeArguments typeArgs = new TypeArguments(typesAndParameters);
         Method method = getDeclaredMethod(instance.getClass(), methodName, typeArgs.getTypes());
         if (method != null) {
            try {
               method.setAccessible(true);
               return (T) method.invoke(instance, typeArgs.getArguments());
            } catch (Exception e) {
               // ignore it
            }
         }

         return null;
      }

      @SuppressWarnings("unchecked")
      public <T> T invokeMethod(Object instance, String methodName, Object... typesAndParameters) {
         if (instance == null) {
            return null;
         }

         TypeArguments typeArgs = new TypeArguments(typesAndParameters);
         Method method = getMethod(instance.getClass(), methodName, typeArgs.getTypes());
         if (method != null) {
            try {
               return (T) method.invoke(instance, typeArgs.getArguments());
            } catch (Exception e) {
               // ignore it
            }
         }

         return null;
      }

      @SuppressWarnings("unchecked")
      public <T> T invokeStaticMethod(Class<?> clazz, String methodName, Object... typesAndParameters) {
         if (clazz == null) {
            return null;
         }

         TypeArguments typeArgs = new TypeArguments(typesAndParameters);
         Method method = getMethod(clazz, methodName, typeArgs.getTypes());
         if (method != null) {
            try {
               return (T) method.invoke(null, typeArgs.getArguments());
            } catch (Exception e) {
               // ignore it
            }
         }

         return null;
      }
   }

   public static enum ModifierReflector {
      INSTANCE;

      public boolean isPublic(Class<?> clazz) {
         return Modifier.isPublic(clazz.getModifiers());
      }

      public boolean isVisible(Class<?> clazz) {
         int modifiers = clazz.getModifiers();

         return Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers);
      }

      public boolean isPublic(Member member) {
         return Modifier.isPublic(member.getModifiers());
      }

      public boolean isStatic(Class<?> clazz) {
         return Modifier.isStatic(clazz.getModifiers());
      }

      public boolean isStatic(Member member) {
         return Modifier.isStatic(member.getModifiers());
      }
   }

   static class TypeArguments {
      private Class<?>[] m_types;

      private Object[] m_arguments;

      public TypeArguments(Object... typesAndParameters) {
         int length = typesAndParameters.length;

         if (length % 2 != 0) {
            throw new IllegalArgumentException(String.format("Constrcutor argument types and data should be even"
                  + ", but was odd: %s.", length));
         }

         int half = length / 2;
         Class<?>[] types = new Class<?>[half];
         Object[] arguments = new Object[half];

         for (int i = 0; i < half; i++) {
            types[i] = (Class<?>) typesAndParameters[2 * i];
            arguments[i] = typesAndParameters[2 * i + 1];
         }

         m_types = types;
         m_arguments = arguments;
      }

      public Object[] getArguments() {
         return m_arguments;
      }

      public Class<?>[] getTypes() {
         return m_types;
      }
   }
}