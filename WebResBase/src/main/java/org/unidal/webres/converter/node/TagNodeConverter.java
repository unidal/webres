package org.unidal.webres.converter.node;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;

import org.unidal.webres.converter.Converter;
import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.converter.ConverterUtil;
import org.unidal.webres.converter.TypeUtil;
import org.unidal.webres.dom.INode;
import org.unidal.webres.dom.ITagNode;
import org.unidal.webres.dom.TagNode;

public class TagNodeConverter implements Converter<Object> {
   public boolean canConvert(ConverterContext ctx) {
      return INode.class.isAssignableFrom(ctx.getSourceClass());
   }

   public Object convert(final ConverterContext ctx) throws ConverterException {
      final TagNode node = (TagNode) ctx.getSource();
      final Class<?> targetClass = ctx.getTargetClass();
      final Type targetType = ctx.getTargetType();

      if (targetClass.isPrimitive() || TypeUtil.getPrimitiveClass(targetClass).isPrimitive()) {
         final String text = (String) ctx.getManager().convert(node, String.class);

         return ctx.getManager().convert(text, targetType);
      } else {
         final Class<?> concreteClass = ctx.getManager().getRegistry().findType(targetClass);

         try {
            final Object instance = concreteClass.newInstance();

            if (node.hasAttributes()) {
               convertAttributes(ctx, instance, targetType, node.getAttributes());
            }

            convertNodeList(ctx, instance, targetType, node.getChildNodes());

            return instance;
         } catch (ConverterException e) {
            throw e;
         } catch (Exception e) {
            throw new ConverterException(e);
         }
      }
   }

   private void convertAttributes(final ConverterContext ctx, final Object instance, Type instanceType,
         final Map<String, String> map) throws IllegalArgumentException, IllegalAccessException,
         InvocationTargetException {
      final Class<?> clazz = instance.getClass();

      for (Map.Entry<String, String> entry : map.entrySet()) {
         String key = entry.getKey();
         final String text = entry.getValue();
         //filter out EL attr for data override case
         if (ConverterContext.isOverride() && isELAttribute(key, text)) {
            continue;
         }

         final String methodName = ConverterUtil.getSetMethodName(key);
         final Method method = ConverterUtil.getSetMethod(clazz, methodName);
         final Type parameterType = method.getGenericParameterTypes()[0];

         final Object value = ctx.getManager().convert(text, parameterType);

         method.invoke(instance, new Object[] { value });
      }
   }

   private boolean isELAttribute(String attrName, String attrValue) {
      if (attrName == null || attrValue == null) {
         return false;
      }

      if (attrName.equals("value") && attrValue.startsWith("${") && attrValue.endsWith("}")) {
         return true;
      }

      return false;
   }

   @SuppressWarnings("rawtypes")
   private void convertNode(final ConverterContext ctx, final Object instance, Type instanceType, final ITagNode child)
         throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
      final Class<?> clazz = instance.getClass();
      final String methodName = ConverterUtil.getSetMethodName(child.getNodeName());
      final Method method = ConverterUtil.getSetMethod(clazz, methodName);
      final Type parameterType = method.getGenericParameterTypes()[0];
      final Type resolvedParameterType;

      // resolve TypeVariable, so "T" can be mapped to real type
      if (instanceType instanceof ParameterizedType && parameterType instanceof TypeVariable) {
         resolvedParameterType = TypeUtil.resolveType((ParameterizedType) instanceType, clazz,
               (TypeVariable) parameterType);
      } else {
         resolvedParameterType = parameterType;
      }

      final Class<?> rawType = TypeUtil.getRawType(resolvedParameterType);
      Object value = null;

      if (rawType.isAssignableFrom(child.getClass())) {
         value = child;
      } else if (rawType.isArray() || List.class.isAssignableFrom(rawType)) {
         value = ctx.getManager().convert(child, resolvedParameterType);
      } else {
         if (rawType.isPrimitive() || TypeUtil.getPrimitiveClass(rawType).isPrimitive()) {
            String text = (String) ctx.getManager().convert(child, String.class);
            value = ctx.getManager().convert(text, resolvedParameterType);
         } else {
            try {
               value = ctx.getManager().convert(child, resolvedParameterType);
            } catch (ConverterException e) {
               // try to convert to text as possible
               String text = (String) ctx.getManager().convert(child, String.class);
               value = ctx.getManager().convert(text, resolvedParameterType);
            }
         }
      }

      method.invoke(instance, new Object[] { value });
   }

   private void convertNodeList(final ConverterContext ctx, final Object instance, Type instanceType,
         List<INode> nodeList) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
      for (INode child : nodeList) {
         convertNode(ctx, instance, instanceType, (ITagNode) child);
      }
   }

   public Type getTargetType() {
      return Type.class;
   }
}
