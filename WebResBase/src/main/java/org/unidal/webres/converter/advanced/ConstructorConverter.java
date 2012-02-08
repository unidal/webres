package org.unidal.webres.converter.advanced;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.unidal.webres.converter.Converter;
import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.converter.TypeUtil;

public class ConstructorConverter implements Converter<Object> {
   public boolean canConvert(ConverterContext ctx) {
      Class<?> fromClass = ctx.getSourceClass();
      Class<?> targetClass = TypeUtil.getConcreteClass(ctx.getManager(), ctx.getTargetType());

      return !ctx.getTargetClass().isPrimitive() && getSingleParameterConstructor(fromClass, targetClass) != null;
   }

   public Object convert(ConverterContext ctx) throws ConverterException {
      Type targetType = ctx.getTargetType();
      Class<?> targetClass = TypeUtil.getConcreteClass(ctx.getManager(), targetType);
      Constructor<?> c = getSingleParameterConstructor(ctx.getSourceClass(), targetClass);

      Object value = ctx.getManager().convert(ctx.getSource(), c.getParameterTypes()[0]);

      try {
         return c.newInstance(new Object[] { value });
      } catch (Exception e) {
         throw new ConverterException(e);
      }
   }

   private Constructor<?> getSingleParameterConstructor(Class<?> fromClass, Class<?> targetClass) {
      Constructor<?>[] constructors = targetClass.getConstructors();

      if (fromClass.isPrimitive()) {
         fromClass = TypeUtil.getWrapClass(fromClass);
      }

      Constructor<?> constructor = null;

      for (Constructor<?> c : constructors) {
         int m = c.getModifiers();
         Class<?>[] types = c.getParameterTypes();

         if (Modifier.isPublic(m) && types.length == 1) {
            Class<?> parameterType = types[0];

            if (parameterType.isPrimitive()) {
               parameterType = TypeUtil.getWrapClass(parameterType);
            }

            if (parameterType.isAssignableFrom(fromClass)) {
               return c;
            }
         }
      }

      return constructor;
   }

   public Type getTargetType() {
      return Type.class;
   }
}
