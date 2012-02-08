package org.unidal.webres.converter.node;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.unidal.webres.converter.Converter;
import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.converter.ConverterManager;
import org.unidal.webres.converter.TypeUtil;
import org.unidal.webres.dom.INode;
import org.unidal.webres.dom.ITagNode;
import org.unidal.webres.dom.NodeType;
import org.unidal.webres.dom.TagNode;

public class TagNodeMapConverter implements Converter<Object> {
   public boolean canConvert(ConverterContext ctx) {
      return INode.class.isAssignableFrom(ctx.getSourceClass());
   }

   public Object convert(ConverterContext ctx) throws ConverterException {
      TagNode node = (TagNode) ctx.getSource();
      List<INode> children = node.getChildNodes();
      Type targetType = ctx.getTargetType();
      Type keyType = TypeUtil.getActualTypeArgument(targetType, 0);
      Type valueType = TypeUtil.getActualTypeArgument(targetType, 1);
      Map<Object, Object> map = new HashMap<Object, Object>();

      for (INode child : children) {
         if (child.getNodeType() == NodeType.TAG) {
            ConverterManager manager = ctx.getManager();
            String nodeName = ((ITagNode) child).getNodeName();
            String nodeValue = (String) manager.convert(child, String.class);
            Object key = manager.convert(nodeName, keyType);
            Object value = manager.convert(nodeValue, valueType);

            map.put(key, value);
         }
      }

      return map;
   }

   public Type getTargetType() {
      return Map.class;
   }
}
