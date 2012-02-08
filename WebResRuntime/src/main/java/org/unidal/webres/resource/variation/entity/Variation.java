package org.unidal.webres.resource.variation.entity;

import static org.unidal.webres.resource.variation.Constants.ATTR_ID;
import static org.unidal.webres.resource.variation.Constants.ENTITY_VARIATION;

import java.util.LinkedHashMap;
import java.util.Map;

import org.unidal.webres.resource.variation.BaseEntity;
import org.unidal.webres.resource.variation.IVisitor;

public class Variation extends BaseEntity<Variation> {
   private String m_id;

   private Map<String, String> m_dynamicAttributes = new LinkedHashMap<String, String>();

   public Variation(String id) {
      m_id = id;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitVariation(this);
   }

   public String getDynamicAttribute(String name) {
      return m_dynamicAttributes.get(name);
   }

   public Map<String, String> getDynamicAttributes() {
      return m_dynamicAttributes;
   }

   public String getId() {
      return m_id;
   }

   @Override
   public void mergeAttributes(Variation other) {
      assertAttributeEquals(other, ENTITY_VARIATION, ATTR_ID, m_id, other.getId());

      for (Map.Entry<String, String> e : other.getDynamicAttributes().entrySet()) {
         m_dynamicAttributes.put(e.getKey(), e.getValue());
      }

   }

   public void setDynamicAttribute(String name, String value) {
      m_dynamicAttributes.put(name, value);
   }

}
