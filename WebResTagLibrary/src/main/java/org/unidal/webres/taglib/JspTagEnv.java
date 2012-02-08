package org.unidal.webres.taglib;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.unidal.webres.helper.Servlets;
import org.unidal.webres.resource.spi.IResourceContainer;
import org.unidal.webres.tag.TagEnvSupport;
import org.unidal.webres.tag.TagLookupManager;

public class JspTagEnv extends TagEnvSupport {
   public static final String NAME = "JSP";

   private PageContext m_pageContext;

   public JspTagEnv(IResourceContainer container) {
      setLookupManager(new TagLookupManager(container));
   }

   public Object findAttribute(String name) {
      return m_pageContext.findAttribute(name);
   }

   public String getContextPath() {
      return Servlets.forContext().getContextPath(m_pageContext.getServletContext());
   }

   @Override
   public Object getPageAttribute(String name) {
      return m_pageContext.getAttribute(name);
   }

   public PageContext getPageContext() {
      return m_pageContext;
   }

   @Override
   public String getRequestUri() {
      ServletRequest request = m_pageContext.getRequest();

      if (request instanceof HttpServletRequest) {
         HttpServletRequest httpRequest = (HttpServletRequest) request;
         return httpRequest.getRequestURI();
      }

      return null;
   }

   @Override
   public void setPageAttribute(String name, Object value) {
      m_pageContext.setAttribute(name, value);
   }

   public void setPageContext(PageContext pageContext) {
      m_pageContext = pageContext;
   }

   @Override
   public void flush() throws IOException {
      m_pageContext.getResponse().flushBuffer();
   }
}