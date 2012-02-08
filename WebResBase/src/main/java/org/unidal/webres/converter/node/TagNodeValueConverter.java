package org.unidal.webres.converter.node;

import java.lang.reflect.Type;

import org.unidal.webres.converter.Converter;
import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.dom.INode;
import org.unidal.webres.dom.NodeType;
import org.unidal.webres.dom.TagNode;
import org.unidal.webres.dom.TextNode;

public class TagNodeValueConverter implements Converter<String> {
   public boolean canConvert(ConverterContext ctx) {
      return INode.class.isAssignableFrom(ctx.getSourceClass());
   }

   public String convert(ConverterContext ctx) throws ConverterException {
      INode node = (INode) ctx.getSource();

      switch (node.getNodeType()) {
      case TAG:
         TagNode tagNode = (TagNode) node;
         if (tagNode.getChildNodes().size() == 1) {
            INode child = tagNode.getFirstChild();
            if (child != null && child.getNodeType() == NodeType.TEXT) {
               return ((TextNode) child).getNodeValue();
            }
         }
         break;
      case TEXT:
         return ((TextNode) node).getNodeValue();
      }

      throw ConverterContext.SKIP;
   }

   public Type getTargetType() {
      return String.class;
   }
}
