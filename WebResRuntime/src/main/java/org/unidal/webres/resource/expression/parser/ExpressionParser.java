package org.unidal.webres.resource.expression.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.unidal.webres.helper.Reflects;

public class ExpressionParser implements IExpressionParser {
   @Override
   public IExpressionNode parse(String expression) {
      if (expression == null) {
         throw new IllegalArgumentException("expression can't be null!");
      }

      List<String> tokens = new ExpressionTokenizer().parse(expression, 2, expression.length() - 1);
      ExpressionNode root = new ExpressionNode();

      root.build(tokens, 0, "");
      return root;
   }

   public static abstract class BaseNode implements INode {
      private String m_value;

      public BaseNode(String value) {
         m_value = value;
      }

      @Override
      public void accept(INodeVisitor visitor) {
         visitor.visit(this);
      }

      @SuppressWarnings("unchecked")
      protected Object eval(INodeContext ctx, Object key) {
         Object instance = ctx.peekInstance();

         if (instance == null) {
            return ctx.resolveVariable(String.valueOf(key));
         }

         Object child;

         if (instance instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) instance;

            child = map.get(key);
         } else if (instance instanceof List) {
            List<Object> list = (List<Object>) instance;
            int listIndex;

            child = null;

            try {
               listIndex = Integer.parseInt(String.valueOf(key));

               if (listIndex >= 0 && listIndex < list.size()) {
                  child = list.get(listIndex);
               }
            } catch (NumberFormatException e) {
               // ignore
            }
         } else {
            child = getPojoProperty(instance, String.valueOf(key));
         }

         return child;
      }

      protected Object getPojoProperty(Object instance, String property) {
         return Reflects.forMethod().getPropertyValue(instance, property);
      }

      protected String getValue() {
         return m_value;
      }

      @Override
      public String toString() {
         DefaultStringVisitor visitor = new DefaultStringVisitor();

         visitor.visit(this);
         return visitor.toString();
      }
   }

   public static abstract class BaseNodeVisitor implements INodeVisitor {
      @Override
      public void visit(INode node) {
         switch (node.getType()) {
         case LITERAL:
            visitLiteral((ILiteralNode) node);
            break;
         case EXPRESSION:
            visitExpression((IExpressionNode) node);
            break;
         case FUNCTION:
            visitFunction((IFunctionNode) node);
            break;
         case METHOD:
            visitMethod((IMethodNode) node);
            break;
         }
      }

      protected void visitExpression(IExpressionNode expression) {
      }

      protected void visitFunction(IFunctionNode function) {
      }

      protected void visitLiteral(ILiteralNode literal) {
      }

      protected void visitMethod(IMethodNode method) {
      }
   }

   public static class DefaultStringVisitor extends BaseNodeVisitor {
      private StringBuilder m_sb = new StringBuilder(256);

      @Override
      public String toString() {
         return m_sb.toString();
      }

      @Override
      protected void visitExpression(IExpressionNode expression) {
         boolean first = true;

         for (INode section : expression.getSections()) {
            String str = section.toString();

            if (first) {
               first = false;
               m_sb.append(str);
            } else {
               if (section instanceof LiteralNode) {
                  if (((ILiteralNode) section).isIdentifier()) {
                     m_sb.append('.').append(str);
                  } else {
                     m_sb.append('[').append(str).append(']');
                  }
               } else if (section instanceof MethodNode) {
                  m_sb.append('.').append(str);
               } else {
                  m_sb.append('[').append(str).append(']');
               }
            }
         }
      }

      @Override
      protected void visitFunction(IFunctionNode function) {
         m_sb.append(function.getPrefix()).append(':').append(function.getName()).append('(');

         boolean first = true;

         for (INode argument : function.getArguments()) {
            if (first) {
               first = false;
            } else {
               m_sb.append(',');
            }

            m_sb.append(argument.toString());
         }

         m_sb.append(')');
      }

      @Override
      protected void visitLiteral(ILiteralNode literal) {
         m_sb.append(literal.toString());
      }

      @Override
      protected void visitMethod(IMethodNode method) {
         m_sb.append(method.getName()).append('(');

         boolean first = true;

         for (INode argument : method.getArguments()) {
            if (first) {
               first = false;
            } else {
               m_sb.append(',');
            }

            m_sb.append(argument.toString());
         }

         m_sb.append(')');
      }
   }

   public static class ExpressionNode extends BaseNode implements IExpressionNode {
      private List<INode> m_sections = new ArrayList<INode>();

      public ExpressionNode() {
         super(null);
      }

      @Override
      public void addSection(INode section) {
         m_sections.add(section);
      }

      /**
       * @param tokens
       * @param startIndex
       * @param endToken
       * @return number of tokens processed, -1 means an error happened
       */
      public int build(List<String> tokens, int startIndex, String endTokens) {
         int size = tokens.size();
         boolean dot = true;
         int count = -1;

         for (int i = startIndex; i < size; i++) {
            String token = tokens.get(i);
            int len = token.length();

            if (len == 1) {
               char ch = token.charAt(0);

               // found end token
               if (endTokens.indexOf(ch) >= 0) {
                  return i - startIndex;
               }

               switch (ch) {
               case '.':
                  if (dot) {
                     return -1;
                  }

                  dot = true;
                  break;
               case '[':
                  if (dot) {
                     return -1;
                  }

                  ExpressionNode item = new ExpressionNode();

                  count = item.build(tokens, i + 1, "]");

                  if (count < 0) {
                     return -1;
                  } else {
                     i += count + 1;
                  }

                  int sectionSize = item.getSections().size();

                  if (sectionSize == 0) {
                     return -1;
                  } else {
                     addSection(item);
                  }
                  break;
               case '(':
                  if (m_sections.isEmpty()) {
                     return -1;
                  }

                  INode last = m_sections.remove(m_sections.size() - 1);
                  String name;

                  if (last instanceof ILiteralNode) {
                     ILiteralNode literal = (ILiteralNode) last;

                     name = literal.toString();
                  } else {
                     return -1;
                  }

                  if (name.indexOf(':') > 0) {
                     FunctionNode function = new FunctionNode(name);

                     count = function.build(tokens, i + 1, ")");
                     addSection(function);
                  } else {
                     MethodNode method = new MethodNode(name);

                     count = method.build(tokens, i + 1, ")");
                     addSection(method);
                  }

                  if (count < 0) {
                     return -1;
                  } else {
                     i += count + 1;
                  }

                  dot = false;
                  break;
               default:
                  if (!dot) {
                     return -1;
                  }

                  if (isQuoted(token)) {
                     return -1;
                  } else {
                     addSection(new LiteralNode(token));
                  }

                  dot = false;
                  break;
               }
            } else {
               if (dot) {
                  addSection(new LiteralNode(token));
                  dot = false;
               } else {
                  return -1;
               }
            }
         }

         return size - startIndex;
      }

      @Override
      public Object evaluate(INodeContext ctx) {
         ctx.pushInstance(null);

         for (INode section : m_sections) {
            Object instance = section.evaluate(ctx);

            if (instance == null) {
               return null;
            }

            ctx.updateInstance(instance);
         }

         Object value = ctx.popInstance();

         if (!ctx.hasInstance()) { // last one is popped
            return value;
         } else {
            return eval(ctx, value);
         }
      }

      @Override
      public List<INode> getSections() {
         return m_sections;
      }

      @Override
      public NodeType getType() {
         return NodeType.EXPRESSION;
      }

      private boolean isQuoted(String token) {
         int len = token.length();

         if (len >= 2) {
            char ch1 = token.charAt(0);
            char ch2 = token.charAt(len - 1);

            return ch1 == ch2 && (ch1 == '\'' || ch1 == '"');
         } else {
            return false;
         }
      }
   }

   public static class ExpressionTokenizer {
      protected int countSpace(String str, int start, int end, boolean beginning) {
         if (beginning) {
            int i;

            for (i = start; i < end; i++) {
               char ch = str.charAt(i);

               if (!Character.isWhitespace(ch)) {
                  break;
               }
            }

            return i - start;
         } else {
            int i;

            for (i = end; i > 0; i--) {
               char ch = str.charAt(i - 1);

               if (!Character.isWhitespace(ch)) {
                  break;
               }
            }

            return end - i;
         }
      }

      /**
       * @param expression EL expression not enclosed by '${' and '}'.
       * @return list of tokens
       */
      public List<String> parse(String expression) {
         return parse(expression, 0, expression.length());
      }

      /**
       * @param expression EL expression not enclosed by '${' and '}'.
       * @param start
       * @param end
       * @return list of tokens
       */
      public List<String> parse(String expression, int start, int end) {
         List<String> tokens = new ArrayList<String>();
         int offset = start;
         int i;

         offset += countSpace(expression, start, end, true);
         end -= countSpace(expression, start, end, false);

         for (i = offset; i < end; i++) {
            char ch = expression.charAt(i);

            switch (ch) {
            case '.':
            case '(':
            case '[':
               if (i > offset) {
                  tokens.add(expression.substring(offset, i));
               }

               tokens.add(String.valueOf(ch));
               offset = i + 1;
               break;
            case '\'':
            case '"':
               if (i > offset) {
                  // invalid token, but we still add it
                  tokens.add(expression.substring(offset, i));
                  offset = i + 1;
               }

               for (i = offset + 1; i < end; i++) {
                  char ch2 = expression.charAt(i);

                  if (ch2 == ch) {
                     break;
                  }
               }

               if (i < end) {
                  tokens.add(expression.substring(offset, i + 1));
                  offset = i + 1;
               } else {
                  throw new RuntimeException(String.format("Quotes(%s) mismatch in expression(%s)", ch, expression));
               }
               break;
            case ',':
            case ')':
            case ']':
               if (i > offset) {
                  tokens.add(expression.substring(offset, i));
               }

               tokens.add(String.valueOf(ch));
               offset = i + 1;
               break;
            }
         }

         if (i > offset) {
            tokens.add(expression.substring(offset, i));
         }

         return tokens;
      }
   }

   public static class FunctionNode extends MethodNode implements IFunctionNode {
      private String m_prefix;

      private String m_name;

      public FunctionNode(String value) {
         super(value);

         int off = value.indexOf(':');

         if (off > 0) {
            m_prefix = value.substring(0, off);
            m_name = value.substring(off + 1);
         } else {
            throw new RuntimeException(String.format("Invalid function node(%s)!", value));
         }
      }

      @Override
      public String getName() {
         return m_name;
      }

      @Override
      public String getPrefix() {
         return m_prefix;
      }

      @Override
      public NodeType getType() {
         return NodeType.FUNCTION;
      }
   }

   public static class LiteralNode extends BaseNode implements ILiteralNode {
      private static Map<String, Integer> CONSTANTS = new HashMap<String, Integer>();

      private int m_type;

      static {
         CONSTANTS.put("true", 3);
         CONSTANTS.put("false", 3);
         CONSTANTS.put("null", 9);
      }

      public LiteralNode(String value) {
         super(value);

         Integer type = CONSTANTS.get(value);

         if (type != null) {
            m_type = type.intValue();
         } else if (isNumber(value)) {
            m_type = 2;
         } else if (isIdentifier(value)) {
            m_type = 1;
         } else if (isQuoted(value)) {
            m_type = 4;
         } else {
            m_type = 5;
         }
      }

      @Override
      public Object evaluate(INodeContext ctx) {
         String value = getValue();

         switch (m_type) {
         case 1:
         case 5:
            return eval(ctx, value);
         case 2:
            return (int) Double.parseDouble(value);
         case 3:
            return Boolean.valueOf(value);
         case 4:
            return value.substring(1, value.length() - 1);
         default:
            throw new UnsupportedOperationException(String.format("Unknown type(%s) for value(%s)!"));
         }
      }

      @Override
      public NodeType getType() {
         return NodeType.LITERAL;
      }

      @Override
      public boolean isIdentifier() {
         return m_type == 1;
      }

      protected boolean isIdentifier(String str) {
         int len = str.length();

         for (int i = 0; i < len; i++) {
            char ch = str.charAt(i);

            if (i == 0 && !Character.isJavaIdentifierStart(ch)) {
               return false;
            } else if (!Character.isJavaIdentifierPart(ch)) {
               return false;
            }
         }

         return true;
      }

      protected boolean isNumber(String str) {
         int len = str.length();

         for (int i = 0; i < len; i++) {
            char ch = str.charAt(i);

            if (ch != '.' && !Character.isDigit(ch)) {
               return false;
            }
         }

         return true;
      }

      protected boolean isQuoted(String value) {
         int len = value.length();

         if (len >= 2) {
            char ch1 = value.charAt(0);
            char ch2 = value.charAt(len - 1);

            return ch1 == ch2 && (ch1 == '\'' || ch1 == '"');
         } else {
            return false;
         }
      }

      @Override
      public String toString() {
         return getValue();
      }
   }

   public static class MethodNode extends BaseNode implements IMethodNode {
      private List<INode> m_arguments = new ArrayList<INode>();

      private String m_name;

      public MethodNode(String value) {
         super(value);

         m_name = value;
      }

      @Override
      public void addArgument(INode argument) {
         m_arguments.add(argument);
      }

      /**
       * @param tokens
       * @param startIndex
       * @param endToken
       * @return number of tokens processed, -1 means an error happened
       */
      public int build(List<String> tokens, int startIndex, String endToken) {
         int i = startIndex;

         while (true) {
            ExpressionNode item = new ExpressionNode();
            int count = item.build(tokens, i, ",)");

            if (count < 0) {
               return -1;
            } else if (count > 0) {
               addArgument(item);
               i += count;
            }

            if (tokens.get(i).equals(")")) {
               return i - startIndex;
            }

            i++;
         }
      }

      @Override
      public Object evaluate(INodeContext ctx) {
         int len = m_arguments.size();
         Object[] arguments = new Object[len];

         for (int i = 0; i < len; i++) {
            INode argument = m_arguments.get(i);

            arguments[i] = argument.evaluate(ctx);
         }

         Object value = ctx.invokeMethod(m_name, arguments);

         return value;
      }

      @Override
      public List<INode> getArguments() {
         return m_arguments;
      }

      @Override
      public String getName() {
         return m_name;
      }

      @Override
      public NodeType getType() {
         return NodeType.METHOD;
      }
   }
}
