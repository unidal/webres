package org.unidal.webres.tag;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.unidal.webres.dom.ITagNode;
import org.unidal.webres.helper.Xmls;

public abstract class TagModelSupport implements ITagModel {
   private String m_bodyContent;

   private ITagNode m_dynamicElements;

   private Map<String, Object> m_dynamicAttributes;

   @Override
   public String getBodyContent() {
      return m_bodyContent;
   }

   @Override
   public Map<String, Object> getDynamicAttributes() {
      if (m_dynamicAttributes == null) {
         return Collections.emptyMap();
      } else {
         return m_dynamicAttributes;
      }
   }

   @Override
   public ITagNode getDynamicElements() {
      return m_dynamicElements;
   }

   @Override
   public void setBodyContent(String bodyContent) {
      m_bodyContent = Xmls.forData().trimCData(bodyContent);
   }

   @Override
   public void setDynamicAttribute(String name, Object value) {
      if (m_dynamicAttributes == null) {
         m_dynamicAttributes = new LinkedHashMap<String, Object>();
      }

      m_dynamicAttributes.put(name, value);
   }

   @Override
   public void setDynamicElements(ITagNode dynamicElements) {
      m_dynamicElements = dynamicElements;
   }
}
