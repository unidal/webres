package org.unidal.webres.converter.basic;

import java.lang.reflect.Type;

import org.unidal.webres.converter.Converter;
import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.converter.TypeUtil;

public class CharConverter implements Converter<Character> {
   public boolean canConvert(ConverterContext ctx) {
      return TypeUtil.isClassSupported(ctx.getSourceClass(), Number.class, Boolean.TYPE, Boolean.class, String.class,
            Enum.class);
   }

   public Character convert(ConverterContext ctx) throws ConverterException {
      Object from = ctx.getSource();

      if (from instanceof Number) {
         return (char) (((Number) from).intValue() & 0xFFFF);
      } else if (from instanceof Boolean) {
         return ((Boolean) from).booleanValue() ? '1' : '0';
      } else if (from instanceof Enum<?>) {
         return (char) ((Enum<?>) from).ordinal();
      } else {
         String str = from.toString().trim();

         if (str.length() > 0) {
            return str.charAt(0);
         } else {
            return '\0';
         }
      }
   }

   public Type getTargetType() {
      return Character.class;
   }
}
