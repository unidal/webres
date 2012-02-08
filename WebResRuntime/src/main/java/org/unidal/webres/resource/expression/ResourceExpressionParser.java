package org.unidal.webres.resource.expression;

import org.unidal.webres.resource.expression.parser.ExpressionParser;
import org.unidal.webres.resource.expression.parser.IExpressionParser;
import org.unidal.webres.resource.expression.parser.IExpressionParser.IExpressionNode;
import org.unidal.webres.resource.expression.parser.IExpressionParser.INode;

public class ResourceExpressionParser {
   private IResourceExpressionEnv m_env;

   private IExpressionParser m_parser;

   public ResourceExpressionParser(IResourceExpressionEnv env) {
      m_env = env;
      m_parser = new ExpressionParser();
   }

   @SuppressWarnings("unchecked")
   public <T extends IResourceExpression<?, ?>> T parse(String el) {
      if (!el.startsWith("${") || !el.endsWith("}")) {
         throw new RuntimeException(String.format("Not a valid EL expression(%s), which should look like ${...}!", el));
      }

      IExpressionNode rootNode = m_parser.parse(el);
      IResourceExpression<?, ?> current = null;

      for (INode node : rootNode.getSections()) {
         if (current == null) {
            current = new ResourceExpression(m_env, node.toString());
         } else if (node.getType().isFunction()) {
            // TODO implement function here
         } else {
            current = (IResourceExpression<?, ?>) current.get(node.toString());
         }
      }

      return (T) current;
   }
}
