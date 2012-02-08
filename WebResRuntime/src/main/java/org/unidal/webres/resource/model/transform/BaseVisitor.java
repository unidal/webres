package org.unidal.webres.resource.model.transform;

import org.unidal.webres.resource.model.IVisitor;
import org.unidal.webres.resource.model.entity.CommonSlotRef;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.model.entity.SlotGroup;
import org.unidal.webres.resource.model.entity.SlotRef;

public abstract class BaseVisitor implements IVisitor {
   @Override
   public void visitCommonSlotRef(CommonSlotRef commonSlotRef) {
   }

   @Override
   public void visitPage(Page page) {
      for (Slot slot : page.getSlots()) {
         visitSlot(slot);
      }

      for (CommonSlotRef commonSlotRef : page.getCommonSlotRefs()) {
         visitCommonSlotRef(commonSlotRef);
      }

      for (SlotGroup slotGroup : page.getSlotGroups()) {
         visitSlotGroup(slotGroup);
      }
   }

   @Override
   public void visitResource(Resource resource) {
   }

   @Override
   public void visitRoot(Root root) {
      for (Slot slot : root.getCommonSlots()) {
         visitSlot(slot);
      }

      for (Page page : root.getPages()) {
         visitPage(page);
      }
   }

   @Override
   public void visitSlot(Slot slot) {
      for (Resource resource : slot.getResources()) {
         visitResource(resource);
      }

      if (slot.getBeforeCommonSlot() != null) {
         visitSlot(slot.getBeforeCommonSlot());
      }

      if (slot.getAfterCommonSlot() != null) {
         visitSlot(slot.getAfterCommonSlot());
      }
   }

   @Override
   public void visitSlotGroup(SlotGroup slotGroup) {
      for (SlotRef slotRef : slotGroup.getSlotRefs()) {
         visitSlotRef(slotRef);
      }
   }

   @Override
   public void visitSlotRef(SlotRef slotRef) {
   }
}
