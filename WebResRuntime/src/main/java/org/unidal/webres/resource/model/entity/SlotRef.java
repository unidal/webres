package org.unidal.webres.resource.model.entity;

import static org.unidal.webres.resource.model.Constants.ATTR_ID;
import static org.unidal.webres.resource.model.Constants.ENTITY_SLOT_REF;

import org.unidal.webres.resource.model.BaseEntity;
import org.unidal.webres.resource.model.IVisitor;

public class SlotRef extends BaseEntity<SlotRef> {
   private String m_id;

   public SlotRef(String id) {
      m_id = id;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitSlotRef(this);
   }

   public String getId() {
      return m_id;
   }

   @Override
   public void mergeAttributes(SlotRef other) {
      assertAttributeEquals(other, ENTITY_SLOT_REF, ATTR_ID, m_id, other.getId());

   }

}
