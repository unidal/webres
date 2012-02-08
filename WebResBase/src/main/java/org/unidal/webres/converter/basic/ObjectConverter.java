package org.unidal.webres.converter.basic;

import java.lang.reflect.Type;

import org.unidal.webres.converter.Converter;
import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;

public class ObjectConverter implements Converter<Object> {
   public boolean canConvert(ConverterContext ctx) {
      return true;
   }

   public Object convert(ConverterContext ctx) throws ConverterException {
      return ctx.getSource();
   }

   public Type getTargetType() {
      return Object.class;
   }
}
