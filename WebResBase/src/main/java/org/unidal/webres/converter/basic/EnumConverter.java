package org.unidal.webres.converter.basic;

import java.lang.reflect.Type;

import org.unidal.webres.converter.Converter;
import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;

public class EnumConverter<T extends Enum<T>> implements Converter<T> {
   public boolean canConvert(ConverterContext ctx) {
      return String.class.isAssignableFrom(ctx.getSourceClass());
   }

   @SuppressWarnings("unchecked")
   public T convert(ConverterContext ctx) throws ConverterException {
      String name = (String) ctx.getSource();
      Class<T> targetClass = (Class<T>) ctx.getTargetClass();

      return Enum.valueOf(targetClass, name.trim());
   }

   public Type getTargetType() {
      return Enum.class;
   }
}
