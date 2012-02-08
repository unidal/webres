package org.unidal.webres.converter.advanced;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.unidal.webres.converter.Converter;
import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.converter.TypeUtil;

/**
 * Convert from String via static field name 
 */
public class StaticFieldConverter implements Converter<Object> {
   public boolean canConvert(ConverterContext ctx) {
      return !ctx.getTargetClass().isPrimitive() && ctx.getSourceClass() == String.class;
   }

   public Object convert(ConverterContext ctx) throws ConverterException {
      String name = ((String) ctx.getSource()).trim();
      Class<?> clazz = null;

      try {
         int pos = name.lastIndexOf('.');
         String methodName;

         if (pos > 0) {
            clazz = Thread.currentThread().getContextClassLoader().loadClass(name.substring(0, pos));
            methodName = name.substring(pos + 1);
         } else {
            clazz = ctx.getTargetClass();
            methodName = name;
         }

         Field field = TypeUtil.getStaticField(clazz, methodName);
         return field.get(null);
      } catch (Exception e) {
         throw new ConverterException(e);
      }
   }

   public Type getTargetType() {
      return Type.class;
   }
}
