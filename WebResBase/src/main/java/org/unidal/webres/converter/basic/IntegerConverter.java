package org.unidal.webres.converter.basic;

import java.lang.reflect.Type;

import org.unidal.webres.converter.Converter;
import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.converter.TypeUtil;

public class IntegerConverter implements Converter<Integer> {
   public boolean canConvert(ConverterContext ctx) {
      return TypeUtil.isClassSupported(ctx.getSourceClass(), Number.class, Boolean.TYPE, Boolean.class, String.class,
            Enum.class);
   }

   public Integer convert(ConverterContext ctx) throws ConverterException {
      Object from = ctx.getSource();

      if (from instanceof Number) {
         return ((Number) from).intValue();
      } else if (from instanceof Boolean) {
         return ((Boolean) from).booleanValue() ? Integer.valueOf(1) : 0;
      } else if (from instanceof Enum<?>) {
         return Integer.valueOf(((Enum<?>) from).ordinal());
      } else {
         try {
            return Integer.valueOf(from.toString().trim());
         } catch (NumberFormatException e) {
            throw new ConverterException(e);
         }
      }
   }

   public Type getTargetType() {
      return Integer.class;
   }
}
