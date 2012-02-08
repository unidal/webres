package org.unidal.webres.resource.variation.entity;

import static org.unidal.webres.resource.variation.Constants.ATTR_TYPE;
import static org.unidal.webres.resource.variation.Constants.ENTITY_VARIATION_REF;

import org.unidal.webres.resource.variation.BaseEntity;
import org.unidal.webres.resource.variation.IVisitor;

public class VariationRef extends BaseEntity<VariationRef> {
   private String m_type;

   private String m_id;

   public VariationRef(String type) {
      m_type = type;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitVariationRef(this);
   }

   public String getId() {
      return m_id;
   }

   public String getType() {
      return m_type;
   }

   @Override
   public void mergeAttributes(VariationRef other) {
      assertAttributeEquals(other, ENTITY_VARIATION_REF, ATTR_TYPE, m_type, other.getType());

      if (other.getId() != null) {
         m_id = other.getId();
      }
   }

   public VariationRef setId(String id) {
      m_id=id;
      return this;
   }

}
