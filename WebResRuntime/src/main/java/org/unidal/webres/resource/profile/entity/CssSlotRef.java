package org.unidal.webres.resource.profile.entity;

import static org.unidal.webres.resource.profile.Constants.ATTR_ID;
import static org.unidal.webres.resource.profile.Constants.ENTITY_CSS_SLOT_REF;

import org.unidal.webres.resource.profile.BaseEntity;
import org.unidal.webres.resource.profile.IVisitor;

public class CssSlotRef extends BaseEntity<CssSlotRef> {
   private String m_id;

   public CssSlotRef(String id) {
      m_id = id;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitCssSlotRef(this);
   }

   public String getId() {
      return m_id;
   }

   @Override
   public void mergeAttributes(CssSlotRef other) {
      assertAttributeEquals(other, ENTITY_CSS_SLOT_REF, ATTR_ID, m_id, other.getId());

   }

}
