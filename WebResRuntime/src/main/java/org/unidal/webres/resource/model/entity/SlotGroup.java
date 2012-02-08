package org.unidal.webres.resource.model.entity;

import static org.unidal.webres.resource.model.Constants.ATTR_ID;
import static org.unidal.webres.resource.model.Constants.ENTITY_SLOT_GROUP;

import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.resource.model.BaseEntity;
import org.unidal.webres.resource.model.IVisitor;

public class SlotGroup extends BaseEntity<SlotGroup> {
   private String m_id;

   private String m_mainSlot;

   private List<SlotRef> m_slotRefs = new ArrayList<SlotRef>();

   public SlotGroup(String id) {
      m_id = id;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitSlotGroup(this);
   }

   public SlotGroup addSlotRef(SlotRef slotRef) {
      m_slotRefs.add(slotRef);
      return this;
   }

   public SlotRef findSlotRef(String id) {
      for (SlotRef slotRef : m_slotRefs) {
         if (!slotRef.getId().equals(id)) {
            continue;
         }

         return slotRef;
      }

      return null;
   }

   public String getId() {
      return m_id;
   }

   public String getMainSlot() {
      return m_mainSlot;
   }

   public List<SlotRef> getSlotRefs() {
      return m_slotRefs;
   }

   @Override
   public void mergeAttributes(SlotGroup other) {
      assertAttributeEquals(other, ENTITY_SLOT_GROUP, ATTR_ID, m_id, other.getId());

      if (other.getMainSlot() != null) {
         m_mainSlot = other.getMainSlot();
      }
   }

   public boolean removeSlotRef(String id) {
      int len = m_slotRefs.size();

      for (int i = 0; i < len; i++) {
         SlotRef slotRef = m_slotRefs.get(i);

         if (!slotRef.getId().equals(id)) {
            continue;
         }

         m_slotRefs.remove(i);
         return true;
      }

      return false;
   }

   public SlotGroup setMainSlot(String mainSlot) {
      m_mainSlot=mainSlot;
      return this;
   }

}
