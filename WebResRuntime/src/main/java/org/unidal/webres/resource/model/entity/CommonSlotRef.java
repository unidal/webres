package org.unidal.webres.resource.model.entity;

import static org.unidal.webres.resource.model.Constants.ATTR_ID;
import static org.unidal.webres.resource.model.Constants.ENTITY_COMMON_SLOT_REF;

import org.unidal.webres.resource.model.BaseEntity;
import org.unidal.webres.resource.model.IVisitor;

public class CommonSlotRef extends BaseEntity<CommonSlotRef> {
   private String m_id;

   private String m_beforeSlot;

   private String m_afterSlot;

   public CommonSlotRef(String id) {
      m_id = id;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitCommonSlotRef(this);
   }

   public String getAfterSlot() {
      return m_afterSlot;
   }

   public String getBeforeSlot() {
      return m_beforeSlot;
   }

   public String getId() {
      return m_id;
   }

   @Override
   public void mergeAttributes(CommonSlotRef other) {
      assertAttributeEquals(other, ENTITY_COMMON_SLOT_REF, ATTR_ID, m_id, other.getId());

      if (other.getBeforeSlot() != null) {
         m_beforeSlot = other.getBeforeSlot();
      }

      if (other.getAfterSlot() != null) {
         m_afterSlot = other.getAfterSlot();
      }
   }

   public CommonSlotRef setAfterSlot(String afterSlot) {
      m_afterSlot=afterSlot;
      return this;
   }

   public CommonSlotRef setBeforeSlot(String beforeSlot) {
      m_beforeSlot=beforeSlot;
      return this;
   }

}
