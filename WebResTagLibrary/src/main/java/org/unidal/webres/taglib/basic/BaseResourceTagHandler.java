package org.unidal.webres.taglib.basic;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceContainer;
import org.unidal.webres.tag.ITag;
import org.unidal.webres.tag.ITagEnv;
import org.unidal.webres.tag.ITagEnvFactory;
import org.unidal.webres.tag.ITagModel;
import org.unidal.webres.tag.meta.TagAttributeMeta;
import org.unidal.webres.tag.resource.ResourceTagState;
import org.unidal.webres.taglib.BaseBodyTagHandler;
import org.unidal.webres.taglib.JspTagEnv;

public abstract class BaseResourceTagHandler<C, M extends ITagModel, T extends ITag<C, M, ResourceTagState>> extends
      BaseBodyTagHandler implements DynamicAttributes {
   private static final Map<Class<?>, Map<String, String>> s_attributeOverrides = new HashMap<Class<?>, Map<String, String>>();

   private static final long serialVersionUID = 1L;

   private T m_tag;

   public BaseResourceTagHandler() {
      Class<?> clazz = getClass();
      Map<String, String> map = s_attributeOverrides.get(clazz);

      if (map == null) {
         s_attributeOverrides.put(clazz, buildAttributeOverrideMap(clazz));
      }
   }

   protected Map<String, String> buildAttributeOverrideMap(Class<?> clazz) {
      Map<String, String> map = new HashMap<String, String>();

      for (Method method : clazz.getMethods()) {
         TagAttributeMeta meta = method.getAnnotation(TagAttributeMeta.class);
         String methodName = method.getName();

         if (meta != null && methodName.length() > 3 && methodName.startsWith("set")) {
            String name = meta.name();

            if (name.length() == 0) {
               name = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
            }

            map.put("_" + name, name);
         }
      }

      return map;
   }

   protected abstract T createTag();

   @SuppressWarnings("unchecked")
   protected JspTagEnv createTagEnv() {
      IResourceContainer container = ResourceRuntimeContext.ctx().getContainer();
      ITagEnvFactory<PageContext> factory = container.getAttribute(ITagEnvFactory.class, JspTagEnv.NAME);

      if (factory == null) {
         throw new RuntimeException("No ITagEnvFactory for JSP was registered!");
      }

      return (JspTagEnv) factory.createTagEnv(pageContext);
   }

   @Override
   public int doEndTag() throws JspException {
      int result = super.doEndTag();
      ITagEnv env = m_tag.getEnv();
      ResourceTagState state = ResourceTagState.ENDED;

      env.onBegin(m_tag, state);

      try {
         m_tag.end();
         m_tag.setState(ResourceTagState.ENDED);
      } catch (RuntimeException cause) {
         if (env.onError(m_tag, state, cause)) {
            throw cause;
         }
      } finally {
         env.onEnd(m_tag, state);
      }

      m_tag = null;
      return result;
   }

   @Override
   public int doStartTag() throws JspException {
      JspTagEnv env = createTagEnv();
      ResourceTagState state = ResourceTagState.STARTED;

      ensureTag();

      m_tag.setEnv(env);

      env.onBegin(m_tag, state);

      try {
         m_tag.start();
         m_tag.setState(state);
      } catch (RuntimeException cause) {
         if (env.onError(m_tag, state, cause)) {
            throw cause;
         }
      } finally {
         env.onEnd(m_tag, state);
      }

      return super.doStartTag();
   }

   protected void ensureTag() {
      if (m_tag == null) {
         m_tag = createTag();
      }
   }

   protected void flushBuffer() {
      ITagEnv env = m_tag.getEnv();

      try {
         write(env.getError());
         write(env.getOutput());
      } catch (IOException e) {
         env.handleError("Error when flushing buffer!", e);
      }
   }

   protected M getModel() {
      ensureTag();
      return m_tag.getModel();
   }

   protected T getTag() {
      return m_tag;
   }

   @Override
   protected void handleBody() throws JspTagException {
      if (bodyContent != null) {
         String content = bodyContent.getString();

         if (content != null) {
            m_tag.getModel().setBodyContent(content);
         }
      }

      ITagEnv env = m_tag.getEnv();
      ResourceTagState state = ResourceTagState.BUILT;
      C component = null;

      env.onBegin(m_tag, state);

      try {
         component = m_tag.build();
         m_tag.setState(state);
      } catch (RuntimeException cause) {
         env.onError(m_tag, state, cause);
         throw cause;
      } finally {
         env.onEnd(m_tag, state);
      }

      renderComponent(component);
   }

   protected void renderComponent(C component) {
      ITagEnv env = m_tag.getEnv();
      ResourceTagState state = ResourceTagState.RENDERED;

      env.onBegin(m_tag, state);

      try {
         String content = m_tag.render(component);

         if (content != null) {
            env.out(content);
         }

         m_tag.setState(state);
      } catch (RuntimeException cause) {
         if (env.onError(m_tag, state, cause))
            throw cause;
      } finally {
         flushBuffer();
         env.onEnd(m_tag, state);
      }
   }

   @Override
   public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
      ensureTag();

      Map<String, String> map = s_attributeOverrides.get(getClass());
      String name = map == null ? null : map.get(localName);

      if (name != null) {
         m_tag.getModel().setDynamicAttribute(name, value);
      } else {
         m_tag.getModel().setDynamicAttribute(localName, value);
      }
   }
}