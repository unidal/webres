package org.unidal.webres.helper;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Namings {
   public static JavaKeywords forJava() {
      return JavaKeywords.INSTANCE;
   }

   public static PojoNaming forPojo() {
      return PojoNaming.INSTANCE;
   }

   public static enum JavaKeywords {
      INSTANCE;

      private static final Set<String> JAVA_KEYWORDS = new HashSet<String>(Arrays.asList(new String[] { "abstract",
            "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default",
            "do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "goto", "if", "implements",
            "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private",
            "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this",
            "throw", "throws", "transient", "try", "void", "volatile", "while" }));

      public String getTitleCase(String name) {
         return name == null || name.length() == 0 ? name : Character.toUpperCase(name.charAt(0)) + name.substring(1);
      }

      public String getVariable(String name) {
         return name == null || name.length() == 0 ? name : Character.toLowerCase(name.charAt(0)) + name.substring(1);
      }

      public boolean isJavaKeyword(String str) {
         return JAVA_KEYWORDS.contains(str);
      }
   }

   public static enum PojoNaming {
      INSTANCE;

      public String getNameFromGetMethod(Method method) {
         String methodName = method.getName();
         int len = methodName.length();

         if (len > 3 && methodName.startsWith("get")) {
            return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
         } else {
            Class<?> type = method.getReturnType();

            if ((type == Boolean.TYPE || type == Boolean.class) && len > 2 && methodName.startsWith("is")) {
               return Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
            } else {
               throw new IllegalArgumentException("Unable to get property name from get method: " + method);
            }
         }
      }

      public String getNameFromSetMethod(Method method) {
         String methodName = method.getName();
         int len = methodName.length();

         if (len > 3 && methodName.startsWith("set")) {
            return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
         } else {
            throw new IllegalArgumentException("Unable to get property name from set method: " + method);
         }
      }

      public boolean isSetMethod(Method method) {
         return Modifier.isPublic(method.getModifiers()) && method.getName().startsWith("set")
               && method.getParameterTypes().length == 1;
      }
   }
}
