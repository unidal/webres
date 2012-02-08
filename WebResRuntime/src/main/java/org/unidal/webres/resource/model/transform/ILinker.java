package org.unidal.webres.resource.model.transform;

import org.unidal.webres.resource.model.entity.CommonSlotRef;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.model.entity.SlotGroup;
import org.unidal.webres.resource.model.entity.SlotRef;

public interface ILinker {

   public boolean onAfterCommonSlot(Slot parent, Slot slot);

   public boolean onBeforeCommonSlot(Slot parent, Slot slot);

   public boolean onCommonSlot(Root parent, Slot slot);

   public boolean onCommonSlotRef(Page parent, CommonSlotRef commonSlotRef);

   public boolean onPage(Root parent, Page page);

   public boolean onResource(Slot parent, Resource resource);

   public boolean onSlot(Page parent, Slot slot);

   public boolean onSlotGroup(Page parent, SlotGroup slotGroup);

   public boolean onSlotRef(SlotGroup parent, SlotRef slotRef);
}
