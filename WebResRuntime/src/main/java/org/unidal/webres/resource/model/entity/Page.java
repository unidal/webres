package org.unidal.webres.resource.model.entity;

import static org.unidal.webres.resource.model.Constants.ATTR_ID;
import static org.unidal.webres.resource.model.Constants.ENTITY_PAGE;

import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.resource.model.BaseEntity;
import org.unidal.webres.resource.model.IVisitor;

public class Page extends BaseEntity<Page> {
   private String m_id;

   private List<Slot> m_slots = new ArrayList<Slot>();

   private List<CommonSlotRef> m_commonSlotRefs = new ArrayList<CommonSlotRef>();

   private List<SlotGroup> m_slotGroups = new ArrayList<SlotGroup>();

   public Page(String id) {
      m_id = id;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitPage(this);
   }

   public Page addCommonSlotRef(CommonSlotRef commonSlotRef) {
      m_commonSlotRefs.add(commonSlotRef);
      return this;
   }

   public Page addSlot(Slot slot) {
      m_slots.add(slot);
      return this;
   }

   public Page addSlotGroup(SlotGroup slotGroup) {
      m_slotGroups.add(slotGroup);
      return this;
   }

   public CommonSlotRef findCommonSlotRef(String id) {
      for (CommonSlotRef commonSlotRef : m_commonSlotRefs) {
         if (!commonSlotRef.getId().equals(id)) {
            continue;
         }

         return commonSlotRef;
      }

      return null;
   }

   public Slot findSlot(String id) {
      for (Slot slot : m_slots) {
         if (!slot.getId().equals(id)) {
            continue;
         }

         return slot;
      }

      return null;
   }

   public SlotGroup findSlotGroup(String id) {
      for (SlotGroup slotGroup : m_slotGroups) {
         if (!slotGroup.getId().equals(id)) {
            continue;
         }

         return slotGroup;
      }

      return null;
   }

   public List<CommonSlotRef> getCommonSlotRefs() {
      return m_commonSlotRefs;
   }

   public String getId() {
      return m_id;
   }

   public List<SlotGroup> getSlotGroups() {
      return m_slotGroups;
   }

   public List<Slot> getSlots() {
      return m_slots;
   }

   @Override
   public void mergeAttributes(Page other) {
      assertAttributeEquals(other, ENTITY_PAGE, ATTR_ID, m_id, other.getId());

   }

   public boolean removeCommonSlotRef(String id) {
      int len = m_commonSlotRefs.size();

      for (int i = 0; i < len; i++) {
         CommonSlotRef commonSlotRef = m_commonSlotRefs.get(i);

         if (!commonSlotRef.getId().equals(id)) {
            continue;
         }

         m_commonSlotRefs.remove(i);
         return true;
      }

      return false;
   }

   public boolean removeSlot(String id) {
      int len = m_slots.size();

      for (int i = 0; i < len; i++) {
         Slot slot = m_slots.get(i);

         if (!slot.getId().equals(id)) {
            continue;
         }

         m_slots.remove(i);
         return true;
      }

      return false;
   }

   public boolean removeSlotGroup(String id) {
      int len = m_slotGroups.size();

      for (int i = 0; i < len; i++) {
         SlotGroup slotGroup = m_slotGroups.get(i);

         if (!slotGroup.getId().equals(id)) {
            continue;
         }

         m_slotGroups.remove(i);
         return true;
      }

      return false;
   }

}
