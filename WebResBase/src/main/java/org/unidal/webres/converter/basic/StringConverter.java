package org.unidal.webres.converter.basic;

import java.lang.reflect.Type;

import org.unidal.webres.converter.Converter;
import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.dom.ITagNode;

public class StringConverter implements Converter<String> {
   public boolean canConvert(ConverterContext ctx) {
      return true;
   }

   public String convert(ConverterContext ctx) throws ConverterException {
      Object source = ctx.getSource();
      if (source instanceof ITagNode) {
         ITagNode node = (ITagNode) source;

         if (!node.hasAttributes() || !node.hasChildNodes()) {
            return "";
         }
      } 
      return source.toString();
   }

   public Type getTargetType() {
      return String.class;
   }
}
