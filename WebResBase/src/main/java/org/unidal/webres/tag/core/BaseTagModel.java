package org.unidal.webres.tag.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.unidal.webres.dom.ITagNode;
import org.unidal.webres.helper.Xmls;

public abstract class BaseTagModel implements ITagModel {
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

   public void setDynamicAttributes(Map<String, Object> attrs) {
      m_dynamicAttributes = attrs;
   }

   @Override
   public void setDynamicAttribute(String name, Object value) {
      if (m_dynamicAttributes == null) {
         m_dynamicAttributes = new HashMap<String, Object>();
      }

      m_dynamicAttributes.put(name, value);
   }

   @Override
   public void setDynamicElements(ITagNode dynamicElements) {
      m_dynamicElements = dynamicElements;
   }
}
