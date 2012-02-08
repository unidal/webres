package org.unidal.webres.converter.basic;

import java.lang.reflect.Type;

import org.unidal.webres.converter.Converter;
import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.converter.TypeUtil;

public class DoubleConverter implements Converter<Double> {
   public boolean canConvert(ConverterContext ctx) {
      return TypeUtil.isClassSupported(ctx.getSourceClass(), Number.class, Boolean.TYPE, Boolean.class, String.class,
            Enum.class);
   }

   public Double convert(ConverterContext ctx) throws ConverterException {
      Object from = ctx.getSource();

      if (from instanceof Number) {
         return ((Number) from).doubleValue();
      } else if (from instanceof Boolean) {
         return ((Boolean) from).booleanValue() ? Double.valueOf(1) : 0;
      } else if (from instanceof Enum<?>) {
         return Double.valueOf(((Enum<?>) from).ordinal());
      } else {
         try {
            return Double.valueOf(from.toString().trim());
         } catch (NumberFormatException e) {
            throw new ConverterException(e);
         }
      }
   }

   public Type getTargetType() {
      return Double.class;
   }
}
