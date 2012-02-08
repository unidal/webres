package org.unidal.webres.tag.core;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.unidal.webres.helper.Markers;
import org.unidal.webres.tag.core.ITag.State;

public abstract class BaseTagHandler<C, M extends ITagModel, T extends ITag<C, M>> extends BaseBodyTagHandler implements
      DynamicAttributes {
   private static final long serialVersionUID = 1L;

   private T m_tag;

   protected abstract T createTag();

   protected JspTagEnv createTagEnv() {
      return new JspTagEnv();
   }

   protected void initTagEnv(JspTagEnv env, T tag) {
      env.setPageContext(pageContext);
   }

   @Override
   public int doEndTag() throws JspException {
      int result = super.doEndTag();

      ITagEnv env = m_tag.getEnv();
      env.onBegin(m_tag, TagAdviceTarget.END);
      try {
         m_tag.end();
         m_tag.setState(State.ENDED);
      } catch (RuntimeException cause) {
         if (env.onError(m_tag, TagAdviceTarget.END, cause)) {
            throw cause;
         }
      } finally {
         env.onEnd(m_tag, TagAdviceTarget.END);
      }

      m_tag = null;

      return result;
   }

   @Override
   public int doStartTag() throws JspException {
      JspTagEnv env = createTagEnv();

      ensureTag();

      initTagEnv(env, m_tag);

      m_tag.setEnv(env);

      env.onBegin(m_tag, TagAdviceTarget.START);
      try {
         m_tag.start();
         m_tag.setState(State.STARTED);
      } catch (RuntimeException cause) {
         if (env.onError(m_tag, TagAdviceTarget.START, cause)) {
            throw cause;
         }
      } finally {
         env.onEnd(m_tag, TagAdviceTarget.START);
      }

      return super.doStartTag();
   }

   private void ensureTag() {
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
         env.onError("Error when flushing buffer!", e);
      }
   }

   protected M getModel() {
      ensureTag();
      return m_tag.getModel();
   }

   protected T getTag() {
      return m_tag;
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   @Override
   protected void handleBody() throws JspTagException {
      if (bodyContent != null) {
         String content = bodyContent.getString();

         if (content != null) {
            m_tag.getModel().setBodyContent(content);
         }
      }

      ITagEnv env = m_tag.getEnv();
      env.onBegin(m_tag, TagAdviceTarget.BUILD);
      C component = null;
      try {
         component = m_tag.build();
         m_tag.setState(State.BUILT);
      } catch (RuntimeException cause) {
         env.onError(m_tag, TagAdviceTarget.BUILD, cause);
         throw cause;
      } finally {
         env.onEnd(m_tag, TagAdviceTarget.BUILD);
      }

      if (m_tag instanceof IDeferRenderable<?>) {
         IDeferRenderable renderable = (IDeferRenderable) m_tag;
         boolean flag = renderable.prepareForDefer(component);

         if (flag) {
            String deferType = renderable.getDeferType();
            String deferId = renderable.getDeferId();

            m_tag.getEnv().out(Markers.forDefer().build(deferType, deferId));
         } else {
            renderComponent(component);
         }
      } else {
         renderComponent(component);
      }

      flushBuffer();
   }

   protected void renderComponent(C component) {
      if (component != null) {
         ITagEnv env = m_tag.getEnv();
         env.onBegin(m_tag, TagAdviceTarget.RENDER);
         try {
            m_tag.render(component);
            m_tag.setState(State.RENDERED);
         } catch (RuntimeException cause) {
            if (env.onError(m_tag, TagAdviceTarget.RENDER, cause))
               throw cause;
         } finally {
            env.onEnd(m_tag, TagAdviceTarget.RENDER);
         }
      }
   }

   @Override
   public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
      ensureTag();
      m_tag.getModel().setDynamicAttribute(localName, value);
   }
}