package org.unidal.webres.converter;

import java.lang.reflect.Type;

public interface Converter<T> {
   public boolean canConvert(ConverterContext ctx);
   
   public T convert(ConverterContext ctx) throws ConverterException;

   public Type getTargetType();
}
