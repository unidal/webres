package org.unidal.webres.converter.node;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.converter.Converter;
import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.converter.TypeUtil;
import org.unidal.webres.dom.INode;
import org.unidal.webres.dom.TagNode;

public class TagNodeArrayConverter implements Converter<Object> {
   public boolean canConvert(ConverterContext ctx) {
      return INode.class.isAssignableFrom(ctx.getSourceClass());
   }

   public Object convert(ConverterContext ctx) throws ConverterException {
      TagNode node = (TagNode) ctx.getSource();

      List<INode> children = node.getChildNodes();
      int size = children.size();
      List<Object> list = new ArrayList<Object>(size);
      Type componentType = TypeUtil.getComponentType(ctx.getTargetType());
      Class<?> componentClass = TypeUtil.getRawType(componentType);

      for (INode child : children) {
         if (componentClass.isAssignableFrom(child.getClass())) {
            list.add(child);
         } else {
            list.add(ctx.getManager().convert(child, componentClass));
         }
      }

      Object value = ctx.getManager().convert(list.toArray(), ctx.getTargetType());

      return value;
   }

   public Type getTargetType() {
      return Array.class;
   }
}
