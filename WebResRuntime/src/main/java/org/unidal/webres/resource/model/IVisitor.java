package org.unidal.webres.resource.model;

import org.unidal.webres.resource.model.entity.CommonSlotRef;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.model.entity.SlotGroup;
import org.unidal.webres.resource.model.entity.SlotRef;

public interface IVisitor {

   public void visitCommonSlotRef(CommonSlotRef commonSlotRef);

   public void visitPage(Page page);

   public void visitResource(Resource resource);

   public void visitRoot(Root root);

   public void visitSlot(Slot slot);

   public void visitSlotGroup(SlotGroup slotGroup);

   public void visitSlotRef(SlotRef slotRef);
}
