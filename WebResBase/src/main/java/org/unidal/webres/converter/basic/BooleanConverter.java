package org.unidal.webres.converter.basic;

import java.lang.reflect.Type;

import org.unidal.webres.converter.Converter;
import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.converter.TypeUtil;

public class BooleanConverter implements Converter<Object> {
   public boolean canConvert(ConverterContext ctx) {
      return TypeUtil.isClassSupported(ctx.getSourceClass(), Number.class, Boolean.TYPE, Boolean.class, String.class);
   }

   public Boolean convert(ConverterContext ctx) throws ConverterException {
      Object from = ctx.getSource();

      if (from instanceof Boolean) {
         return (Boolean) from;
      } else if (from instanceof Number) {
         return ((Number) from).intValue() > 0;
      } else {
         String text = from.toString().trim();

         if (text.length() > 0 && Character.isDigit(text.charAt(0))) {
            try {
               Double value = Double.valueOf(text);

               return value.intValue() > 0;
            } catch (NumberFormatException e) {
               // ignore it
            }
         }

         return Boolean.valueOf(text);
      }
   }

   public Type getTargetType() {
      return Boolean.class;
   }
}
