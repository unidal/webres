package org.unidal.webres.tag.core;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.xml.sax.SAXException;

import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.dom.INode;
import org.unidal.webres.dom.ITagNode;
import org.unidal.webres.dom.NodeType;
import org.unidal.webres.tag.meta.TagElementMeta;
import org.unidal.webres.tag.meta.TagMeta;

public abstract class BaseJspTagHandler<T> extends BaseBodyTagHandler implements DynamicAttributes {
   private static final long serialVersionUID = 1L;

   private Map<String, Object> m_dynamicAttributes = new HashMap<String, Object>();

   private StringBuilder m_buffer = new StringBuilder(1024);

   private ITagNode m_dynamicElements;

   /**
    * <ul>build completed component
    *  <li>create component instance</li>
    *  <li> inject dynamic attributes if have</li>
    *  <li>inject dynamic elements if have</li>
    * </ul>
    * 
    * @return new created component
    */
   protected abstract T buildComponent();

   @Override
   public int doEndTag() throws JspException {
      int doEndTag = super.doEndTag();

      // reset
      m_dynamicAttributes.clear();
      m_buffer.setLength(0);

      return doEndTag;
   }

   protected void flushBuffer() {
      if (m_buffer.length() > 0) {
         try {
            write(m_buffer.toString());
         } catch (IOException e) {
            onError("Error when flushing buffer!", e);
         }

         m_buffer.setLength(0);
      }
   }

   protected StringBuilder getBuffer() {
      return m_buffer;
   }

   protected Map<String, Object> getDynamicAttributes() {
      return m_dynamicAttributes;
   }

   protected Map<String, ITagNode> getDynamicElements() {
      if (m_dynamicElements != null && m_dynamicElements.hasChildNodes()) {
         Map<String, ITagNode> elements = new LinkedHashMap<String, ITagNode>();

         for (INode node : m_dynamicElements.getChildNodes()) {
            // ignore text node
            if (node.getNodeType() != NodeType.TEXT) {
               ITagNode tagNode = (ITagNode) node;

               elements.put(tagNode.getNodeName(), tagNode);
            }
         }

         return elements;
      } else {
         return Collections.emptyMap();
      }
   }

   /**
    * Handles JSP tag body content.
    */
   protected void handleBody() throws JspTagException {
      final TagMeta tagMeta = getClass().getAnnotation(TagMeta.class);
      final TagModelInjector injector = TagModelInjector.getInstance();

      // step 0, injectAttributes will be done by JSP automatically in generated Servlet code
      if (tagMeta.parseBody() && bodyContent != null) {
         // step 1, parse XML content
         ITagNode root = null;

         try {
            root = parseBody();
         } catch (Exception e) {
            onError("Error when parsing body content!", e);
         }

         // step 2, do model injection, converting data type to meet
         // target parameters type
         try {
            injectElements(injector, root);
         } catch (Exception e) {
            onError("Error when injecting elements!", e);
         }
      }

      T component = null;

      // step 3, build component, inject dynamic attributes and/or dynamic elements if have
      try {
         component = buildComponent();
      } catch (Exception e) {
         onError("Error when building component!", e);
      }

      if (component != null) {
         // step 4, render component and output to buffer
         try {
            renderComponent(component);
         } catch (Exception e) {
            onError("Error when rendering component!", e);
         }
      }

      // step 5, flush buffer data to tag JSP writer
      flushBuffer();
   }

   protected void injectElements(final TagModelInjector injector, final ITagNode root) {
      injector.inject(this, root);
   }

   protected void onError(String message, Exception e) {
      throw new RuntimeException(message, e);
   }

   protected void out(Object value) {
      m_buffer.append(value);
   }

   protected ITagNode parseBody() throws SAXException, IOException {
      return new TagXmlParser().parse(bodyContent.getString());
   }

   /**
    * render and output
    * 
    * @param component
    */
   protected abstract void renderComponent(T component);

   public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
      m_dynamicAttributes.put(localName, value);
   }

   @TagElementMeta(customized = true)
   public void setDynamicElements(ITagNode dynamicElements) {
      m_dynamicElements = dynamicElements;
   }

   /**
    * Sets the current page context into ConverterContext as well. 
    * This method is invoked by the JSP page implementation object prior to doStartTag(). 
    * 
    * @param pageContext  A PageContext instance
    * @see ConverterContext   
    */
   public void setPageContext(PageContext pageContext) {
      super.setPageContext(pageContext);

      ConverterContext.setThreadLocal("pageContext", pageContext);
   }
}
