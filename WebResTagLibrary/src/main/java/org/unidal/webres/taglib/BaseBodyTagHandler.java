package org.unidal.webres.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Class <code>BaseBodyTagHandler</code> is for defining tag handlers extends 
 * <code>BodyTagSupport</code>. 
 * 
 * @see BodyTagSupport
 */
public abstract class BaseBodyTagHandler extends BodyTagSupport {
   private static final long serialVersionUID = 1L;

   // Indicate whether there is a body content found
   private boolean m_hasBody;

   @Override
   public int doAfterBody() throws JspException {
      m_hasBody = true;
      handleBody();

      return SKIP_BODY;
   }

   @Override
   public int doEndTag() throws JspException {
      // In case of no body actually provided, we should allow it.
      if (!m_hasBody) {
         handleBody();
      }

      m_hasBody = false;
      return EVAL_PAGE;
   }

   protected abstract void handleBody() throws JspTagException;

   protected void write(String data) throws IOException {
      if (data != null && data.length() > 0) {
         final JspWriter out = pageContext.getOut();

         if (bodyContent != null && out instanceof BodyContent) {
            ((BodyContent) out).getEnclosingWriter().write(data);
         } else {
            out.write(data);
         }
      }
   }
}
