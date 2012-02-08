package org.unidal.webres.resource.template.evaluator;

import java.util.Stack;

import org.unidal.webres.helper.Reflects;
import org.unidal.webres.resource.api.ITemplate;
import org.unidal.webres.resource.api.ITemplateContext;
import org.unidal.webres.resource.expression.IResourceExpression;
import org.unidal.webres.resource.expression.IResourceExpressionEnv;
import org.unidal.webres.resource.expression.ResourceExpressionParser;
import org.unidal.webres.resource.expression.parser.ExpressionParser;
import org.unidal.webres.resource.expression.parser.IExpressionParser.IExpressionNode;
import org.unidal.webres.resource.expression.parser.IExpressionParser.INodeContext;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.ITemplateEvaluator;
import org.unidal.webres.resource.template.TemplateLanguage;

public class SimpleTemplateEvaluator implements ITemplateEvaluator, IResourceRegisterable<SimpleTemplateEvaluator> {
   private ITemplateContext m_ctx;

   @Override
   public String evaluate(IResourceContext ctx, ITemplate template) {
      TemplateELParserHandler handler = new TemplateELParserHandler(ctx, m_ctx);
      String content = template.getContent();

      if (content == null) {
         return "";
      } else {
         return TemplateELParser.INSTANCE.parse(content, handler);
      }
   }

   @Override
   public SimpleTemplateEvaluator getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return TemplateLanguage.Simple.name();
   }

   @Override
   public Class<? super SimpleTemplateEvaluator> getRegisterType() {
      return ITemplateEvaluator.class;
   }

   @Override
   public void setContext(ITemplateContext ctx) {
      m_ctx = ctx;
   }

   static class TemplateELContext implements INodeContext {
      private ITemplateContext m_ctx;

      private Stack<Object> m_instances;

      public TemplateELContext(ITemplateContext ctx) {
         m_ctx = ctx;
         m_instances = new Stack<Object>();
      }

      @Override
      public boolean hasInstance() {
         return m_instances.size() > 0;
      }

      @Override
      public Object invokeMethod(String name, Object[] arguments) {
         throw new UnsupportedOperationException();
      }

      @Override
      public Object peekInstance() {
         return m_instances.peek();
      }

      @Override
      public Object popInstance() {
         return m_instances.pop();
      }

      @Override
      public void pushInstance(Object instance) {
         m_instances.push(instance);
      }

      @Override
      public Object resolveVariable(String name) {
         return m_ctx.getAttribute(name);
      }

      @Override
      public void updateInstance(Object instance) {
         m_instances.set(m_instances.size() - 1, instance);
      }
   }

   static enum TemplateELParser {
      INSTANCE;

      public String parse(String template, TemplateELParserHandler handler) {
         int len = template.length();
         StringBuilder sb = new StringBuilder(len + 2048);
         StringBuilder el = new StringBuilder(64);
         boolean dollar = false;
         boolean bracket = false;

         for (int i = 0; i < len; i++) {
            char ch = template.charAt(i);

            switch (ch) {
            case '$':
               dollar = true;
               el.append(ch);
               break;
            case '{':
               if (dollar) {
                  el.append(ch);
                  bracket = true;
               } else {
                  sb.append(ch);
               }
               break;
            case '}':
               if (bracket) {
                  el.append(ch);
                  sb.append(handler.handle(el.toString()));
                  el.setLength(0);
                  dollar = false;
                  bracket = false;
               } else {
                  sb.append(ch);
               }

               break;
            default:
               if (bracket) {
                  el.append(ch);
               } else {
                  if (dollar) {
                     sb.append(el);
                     dollar = false;
                  }

                  sb.append(ch);
               }
            }
         }

         if (el.length() > 0) {
            sb.append(el);
         }

         return sb.toString();
      }
   }

   static class TemplateELParserHandler {
      private ResourceExpressionParser m_parser;

      private ITemplateContext m_tc;

      public TemplateELParserHandler(IResourceContext rc, ITemplateContext tc) {
         IResourceExpressionEnv env = rc.lookup(IResourceExpressionEnv.class);

         m_parser = new ResourceExpressionParser(env);
         m_tc = tc;
      }

      protected Object getPojoProperty(Object instance, String property) {
         return Reflects.forMethod().getPropertyValue(instance, property);
      }

      public String handle(String el) {
         if (el.startsWith("${res.")) { // resource EL
            IResourceExpression<?, ?> expr = m_parser.parse(el);

            return String.valueOf(expr);
         } else {
            IExpressionNode root = new ExpressionParser().parse(el);
            Object value = root.evaluate(new TemplateELContext(m_tc));

            return value == null ? "" : String.valueOf(value);
         }
      }
   }
}
