package org.unidal.webres.resource.model.transform;

import org.unidal.webres.resource.model.entity.CommonSlotRef;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.model.entity.SlotGroup;
import org.unidal.webres.resource.model.entity.SlotRef;

public class DefaultLinker implements ILinker {

   @Override
   public boolean onAfterCommonSlot(Slot parent, Slot slot) {
      parent.setAfterCommonSlot(slot);
      return true;
   }

   @Override
   public boolean onBeforeCommonSlot(Slot parent, Slot slot) {
      parent.setBeforeCommonSlot(slot);
      return true;
   }

   @Override
   public boolean onCommonSlot(Root parent, Slot slot) {
      parent.addCommonSlot(slot);
      return true;
   }

   @Override
   public boolean onCommonSlotRef(Page parent, CommonSlotRef commonSlotRef) {
      parent.addCommonSlotRef(commonSlotRef);
      return true;
   }

   @Override
   public boolean onPage(Root parent, Page page) {
      parent.addPage(page);
      return true;
   }

   @Override
   public boolean onResource(Slot parent, Resource resource) {
      parent.addResource(resource);
      return true;
   }

   @Override
   public boolean onSlot(Page parent, Slot slot) {
      parent.addSlot(slot);
      return true;
   }

   @Override
   public boolean onSlotGroup(Page parent, SlotGroup slotGroup) {
      parent.addSlotGroup(slotGroup);
      return true;
   }

   @Override
   public boolean onSlotRef(SlotGroup parent, SlotRef slotRef) {
      parent.addSlotRef(slotRef);
      return true;
   }
}
