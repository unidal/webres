package org.unidal.webres.server.taglib;

import java.io.IOException;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.aggregation.CssAggregator;
import org.unidal.webres.resource.aggregation.JsAggregator;
import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.expression.IResourceExpression;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.tag.meta.TagAttributeMeta;
import org.unidal.webres.tag.meta.TagMeta;
import org.unidal.webres.taglib.BaseJspTagHandler;

@TagMeta(name = "res", info = "resource tag for test purpose", dynamicAttributes = false)
public class MyResourceTagHandler extends BaseJspTagHandler<Object> {
   private static final long serialVersionUID = 1L;

   private String m_type;

   private String m_target;

   private Object m_value;

   @Override
   protected Object buildComponent() {
      if ("css".equals(m_type)) {
         return getResourceRef(ICssRef.class, m_value);
      } else if ("js".equals(m_type)) {
         return getResourceRef(IJsRef.class, m_value);
      } else {
         throw new RuntimeException(String.format("Type(%s) is not supported in tag %s!", m_type, getClass()));
      }
   }

   @SuppressWarnings("unchecked")
   protected <S> S getResourceRef(Class<S> expectedClass, Object value) {
      S ref = null;

      if (value == null) {
         String message = String.format("<!-- The attribute(value) of %s is null. -->", getClass().getSimpleName());

         throw new RuntimeException(message);
      } else if (value instanceof IResourceExpression) {
         IResourceExpression<?, Object> expr = (IResourceExpression<?, Object>) value;
         Object exprValue = expr.evaluate();

         if (exprValue == null) {
            String message = String.format("<!-- No resource found for EL(%s) in %s. -->", expr.toExternalForm(), getClass()
                  .getSimpleName());

            throw new RuntimeException(message);
         } else if (expectedClass.isAssignableFrom(exprValue.getClass())) {
            ref = (S) exprValue;
         } else {
            String message = String.format("<!-- Invalid value of EL(%s) within %s, Expected %s, but was %s. -->",
                  expr.toExternalForm(), getClass().getSimpleName(), expectedClass.getName(), exprValue.getClass().getName());

            throw new RuntimeException(message);
         }
      } else if (expectedClass.isAssignableFrom(value.getClass())) {
         ref = (S) value;
      } else {
         String message = String.format("<!-- Unsupported value(%s) of %s. -->", value, getClass().getSimpleName());

         throw new RuntimeException(message);
      }

      return ref;
   }

   @Override
   protected void renderComponent(Object component) {
      String result = null;

      if ("css".equals(m_type)) {
         CssAggregator aggregator = ResourceRuntimeContext.ctx().getResourceAggregator(SystemResourceType.Css);

         result = aggregator.addResource(m_target, (ICssRef) component);
      } else if ("js".equals(m_type)) {
         JsAggregator aggregator = ResourceRuntimeContext.ctx().getResourceAggregator(SystemResourceType.Js);

         result = aggregator.addResource(m_target, (IJsRef) component);
      }

      if (result != null) {
         try {
            write(result);
         } catch (IOException e) {
            // ignore it
         }
      }
   }

   @TagAttributeMeta
   public void setTarget(String target) {
      m_target = target;
   }

   @TagAttributeMeta(required = true)
   public void setType(String type) {
      m_type = type;
   }

   @TagAttributeMeta(required = true)
   public void setValue(Object value) {
      m_value = value;
   }
}
