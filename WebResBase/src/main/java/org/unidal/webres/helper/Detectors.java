package org.unidal.webres.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.unidal.webres.helper.Reflects.IMemberFilter;

public class Detectors {
   public static SpecDetector forSpec() {
      return SpecDetector.INSTANCE;
   }

   public static enum SpecDetector {
      INSTANCE;

      public Object detectInstance(final Class<?> clazz) {
         if (clazz == null) {
            return null;
         }

         // check public static method getInstance()
         Method method = Reflects.forMethod().getMethod(clazz, "getInstance");

         if (method != null && Reflects.forModifier().isStatic(method) && method.getReturnType() == clazz) {
            try {
               return method.invoke(null);
            } catch (Exception e) {
               // ignore it
            }
         }

         // check public static field with spec type
         List<Field> fields = Reflects.forField().getFields(clazz, new IMemberFilter<Field>() {
            public boolean filter(Field field) {
               try {
                  return field.getType() == clazz && Reflects.forModifier().isStatic(field) && field.get(null) != null;
               } catch (Exception e) {
                  return false;
               }
            }
         });

         if (fields.size() > 0) {
            try {
               return fields.get(0).get(null);
            } catch (Exception e) {
               // ignore it
            }
         }

         // try create new instance of spec type
         Object spec = Reflects.forConstructor().createInstance(clazz);

         if (spec != null) {
            return spec;
         } else {
            return null;
         }
      }
   }
}
