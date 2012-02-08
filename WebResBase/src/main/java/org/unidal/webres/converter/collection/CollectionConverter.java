package org.unidal.webres.converter.collection;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.unidal.webres.converter.Converter;
import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.converter.ConverterFatalException;
import org.unidal.webres.converter.TypeUtil;

public class CollectionConverter<T> implements Converter<List<T>> {

   public boolean canConvert(ConverterContext ctx) {
      Class<?> fromClass = ctx.getSourceClass();

      if (fromClass.isArray()) {
         return true;
      } else if (Collection.class.isAssignableFrom(fromClass)) {
         return true;
      }

      return false;
   }

   @SuppressWarnings("unchecked")
   public List<T> convert(ConverterContext ctx) throws ConverterException {
      Class<?> clazz = ctx.getSourceClass();
      Type componentType = TypeUtil.getComponentType(ctx.getTargetType());
      Object from = ctx.getSource();
      List<T> list;

      if (clazz.isArray()) {
         int length = Array.getLength(from);

         list = new ArrayList<T>(length);

         for (int i = 0; i < length; i++) {
            Object item = Array.get(from, i);

            Object element = ctx.getManager().convert(item, componentType, true);

            list.add((T) element);
         }
      } else if (Collection.class.isAssignableFrom(clazz)) {
         List<T> fromList = (List<T>) from;

         list = new ArrayList<T>(fromList.size());

         for (T item : fromList) {
            Object element = ctx.getManager().convert(item, componentType, true);

            list.add((T) element);
         }
      } else {
         throw new ConverterFatalException("Unknown type: " + from.getClass(), this.getClass());
      }

      return list;
   }

   public Type getTargetType() {
      return Collection.class;
   }
}
