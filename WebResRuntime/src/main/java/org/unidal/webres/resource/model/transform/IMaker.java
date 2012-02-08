package org.unidal.webres.resource.model.transform;

import org.unidal.webres.resource.model.entity.CommonSlotRef;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.model.entity.SlotGroup;
import org.unidal.webres.resource.model.entity.SlotRef;

public interface IMaker<T> {

   public CommonSlotRef buildCommonSlotRef(T node);

   public Page buildPage(T node);

   public Resource buildResource(T node);

   public Root buildRoot(T node);

   public Slot buildSlot(T node);

   public SlotGroup buildSlotGroup(T node);

   public SlotRef buildSlotRef(T node);
}
