package org.unidal.webres.resource.profile.entity;

import static org.unidal.webres.resource.profile.Constants.ATTR_ID;
import static org.unidal.webres.resource.profile.Constants.ENTITY_COMMON_JS_SLOT_REF;

import org.unidal.webres.resource.profile.BaseEntity;
import org.unidal.webres.resource.profile.IVisitor;

public class CommonJsSlotRef extends BaseEntity<CommonJsSlotRef> {
   private String m_id;

   private String m_beforeSlot;

   private String m_afterSlot;

   public CommonJsSlotRef(String id) {
      m_id = id;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitCommonJsSlotRef(this);
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
   public void mergeAttributes(CommonJsSlotRef other) {
      assertAttributeEquals(other, ENTITY_COMMON_JS_SLOT_REF, ATTR_ID, m_id, other.getId());

      if (other.getBeforeSlot() != null) {
         m_beforeSlot = other.getBeforeSlot();
      }

      if (other.getAfterSlot() != null) {
         m_afterSlot = other.getAfterSlot();
      }
   }

   public CommonJsSlotRef setAfterSlot(String afterSlot) {
      m_afterSlot=afterSlot;
      return this;
   }

   public CommonJsSlotRef setBeforeSlot(String beforeSlot) {
      m_beforeSlot=beforeSlot;
      return this;
   }

}
