package org.unidal.webres.converter.node;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.converter.Converter;
import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.converter.TypeUtil;
import org.unidal.webres.dom.INode;
import org.unidal.webres.dom.TagNode;

public class TagNodeConstructorConverter implements Converter<Object> {
   public boolean canConvert(ConverterContext ctx) {
      Class<?> targetClass = TypeUtil.getConcreteClass(ctx.getManager(), ctx.getTargetType());

      return !ctx.getTargetClass().isPrimitive() && INode.class.isAssignableFrom(ctx.getSourceClass())
            && !hasStandardConstructor(targetClass);
   }

   private boolean hasStandardConstructor(Class<?> clazz) {
      try {
         clazz.getConstructor();
         return true;
      } catch (Exception e) {
         return false;
      }
   }

   @SuppressWarnings("rawtypes")
   public Object convert(ConverterContext ctx) throws ConverterException {
      Class<?> targetClass = TypeUtil.getConcreteClass(ctx.getManager(), ctx.getTargetType());
      TagNode node = (TagNode) ctx.getSource();
      List<INode> children = node.getChildNodes();
      int len = children.size();
      List<Constructor<?>> found = findConstructors(targetClass, len);

      for (Constructor<?> c : found) {
         Type[] types = c.getGenericParameterTypes();
         Object[] values = new Object[len];
         boolean isTargetParameterizedType = ctx.getTargetType() instanceof ParameterizedType;

         try {
            for (int i = 0; i < len; i++) {
               INode child = children.get(i);

               if (isTargetParameterizedType && types[i] instanceof TypeVariable) {
                  types[i] = TypeUtil.resolveType((ParameterizedType) ctx.getTargetType(), targetClass,
                        (TypeVariable) types[i]);
               }

               Object value = ctx.getManager().convert(child, types[i]);

               values[i] = value;
            }

            return c.newInstance(values);
         } catch (Exception e) {
            // ignore it, try next constructor
         }
      }

      throw ConverterContext.SKIP;
   }

   private List<Constructor<?>> findConstructors(Class<?> clazz, int parameterCount) {
      Constructor<?>[] constructors = clazz.getConstructors();
      List<Constructor<?>> found = new ArrayList<Constructor<?>>(constructors.length);

      for (Constructor<?> c : constructors) {
         if (c.getGenericParameterTypes().length == parameterCount) {
            found.add(c);
         }
      }

      return found;
   }

   public Type getTargetType() {
      return Type.class;
   }
}
