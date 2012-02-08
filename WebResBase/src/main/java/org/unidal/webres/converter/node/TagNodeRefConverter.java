package org.unidal.webres.converter.node;

import java.lang.reflect.Type;
import java.util.Map;

import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.converter.IRefConverter;
import org.unidal.webres.dom.INode;
import org.unidal.webres.dom.TagNode;

@SuppressWarnings("deprecation")
public class TagNodeRefConverter implements IRefConverter<Object> {
   public boolean canConvert(ConverterContext ctx) {
      Class<?> sourceClass = ctx.getSourceClass();

      return INode.class.isAssignableFrom(sourceClass);
   }

   public Object convert(ConverterContext ctx) throws ConverterException {
      String name = getRefVariableName(ctx);

      if (name == null) {
         throw ConverterContext.SKIP;
      }

      return evaluateVariable(name, ctx);
   }

   private Object evaluateVariable(String name, ConverterContext ctx) throws ConverterException {
      javax.servlet.jsp.PageContext pageContext = ConverterContext.getThreadLocal("pageContext");

      if (pageContext != null) {
         try {
            return pageContext.getVariableResolver().resolveVariable(name);
         } catch (Exception e) {
            throw new ConverterException(e);
         }
      }

      throw ConverterContext.SKIP;
   }

   private String getRefVariableName(ConverterContext ctx) {
      final TagNode node = (TagNode) ctx.getSource();

      Map<String, String> attributes = node.getAttributes();
      if (node.hasAttributes() && attributes.size() == 1) {
         for (Map.Entry<String,String> entry : attributes.entrySet()) {
            String key = entry.getKey();
            final String value = entry.getValue();
            if (isELAttribute(key, value)) {

               final String name = value.substring(2, value.length() - 1);
               return name;
            }
         }
      }

      return null;
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

   public Type getTargetType() {
      return Type.class;
   }
}
