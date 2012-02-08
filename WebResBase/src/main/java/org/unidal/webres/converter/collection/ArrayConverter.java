package org.unidal.webres.converter.collection;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.List;

import org.unidal.webres.converter.Converter;
import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.converter.ConverterFatalException;

public class ArrayConverter implements Converter<Object> {

   public boolean canConvert(ConverterContext ctx) {
      if (!ctx.getTargetClass().isArray()) {
         return false;
      }

      Class<?> fromClass = ctx.getSourceClass();

      if (fromClass.isArray()) {
         return true;
      } else if (List.class.isAssignableFrom(fromClass)) {
         return true;
      }

      return false;
   }

   public Object convert(ConverterContext ctx) throws ConverterException {
      Class<?> fromClass = ctx.getSourceClass();
      Class<?> componentType = ctx.getTargetClass().getComponentType();
      Object from = ctx.getSource();
      Object array;

      if (fromClass.isArray()) {
         int length = Array.getLength(from);

         array = Array.newInstance(componentType, length);

         for (int i = 0; i < length; i++) {
            Object item = Array.get(from, i);
            
            Object element = ctx.getManager().convert(item, componentType, true);

            Array.set(array, i, element);
         }
      } else if (List.class.isAssignableFrom(fromClass)) {
         List<?> fromList = (List<?>) from;
         int length = fromList.size();

         array = Array.newInstance(componentType, length);

         for (int i = 0; i < length; i++) {
            Object item = fromList.get(i);
            
            Object element = ctx.getManager().convert(item, componentType, true);

            Array.set(array, i, element);
         }
      } else {
         throw new ConverterFatalException("Unknown type: " + fromClass, this.getClass());
      }

      return array;
   }

   public Type getTargetType() {
      return Array.class;
   }
}
