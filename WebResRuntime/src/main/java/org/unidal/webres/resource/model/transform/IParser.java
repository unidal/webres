package org.unidal.webres.resource.model.transform;

import org.unidal.webres.resource.model.entity.CommonSlotRef;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.model.entity.SlotGroup;
import org.unidal.webres.resource.model.entity.SlotRef;

public interface IParser<T> {
   public Root parse(IMaker<T> maker, ILinker linker, T node);

   public void parseForCommonSlotRef(IMaker<T> maker, ILinker linker, CommonSlotRef parent, T node);

   public void parseForPage(IMaker<T> maker, ILinker linker, Page parent, T node);

   public void parseForResource(IMaker<T> maker, ILinker linker, Resource parent, T node);

   public void parseForSlot(IMaker<T> maker, ILinker linker, Slot parent, T node);

   public void parseForSlotGroup(IMaker<T> maker, ILinker linker, SlotGroup parent, T node);

   public void parseForSlotRef(IMaker<T> maker, ILinker linker, SlotRef parent, T node);
}
