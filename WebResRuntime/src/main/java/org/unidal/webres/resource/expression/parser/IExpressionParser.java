package org.unidal.webres.resource.expression.parser;

import java.util.List;

public interface IExpressionParser {
   public IExpressionNode parse(String expression);

   public static interface IExpressionNode extends INode {
      public void addSection(INode section);

      public List<INode> getSections();
   }

   public static interface IFunctionNode extends INode {
      public void addArgument(INode argument);

      public List<INode> getArguments();

      public String getName();

      public String getPrefix();
   }

   public static interface ILiteralNode extends INode {
      public boolean isIdentifier();
   }

   public static interface IMethodNode extends INode {
      public void addArgument(INode argument);

      public List<INode> getArguments();

      public String getName();
   }

   public static interface INode {
      public void accept(INodeVisitor visitor);

      public Object evaluate(INodeContext ctx);

      public NodeType getType();
   }

   public static interface INodeContext {
      public boolean hasInstance();

      public Object invokeMethod(String name, Object[] arguments);

      public Object peekInstance();

      public Object popInstance();

      public void pushInstance(Object instance);

      public Object resolveVariable(String name);

      public void updateInstance(Object instance);
   }

   public static interface INodeVisitor {
      public void visit(INode node);
   }

   public static enum NodeType {
      LITERAL,

      EXPRESSION,

      FUNCTION,

      METHOD;

      public boolean isExpression() {
         return this == EXPRESSION;
      }

      public boolean isFunction() {
         return this == FUNCTION;
      }

      public boolean isLiteral() {
         return this == LITERAL;
      }

      public boolean isMethod() {
         return this == METHOD;
      }
   }
}
