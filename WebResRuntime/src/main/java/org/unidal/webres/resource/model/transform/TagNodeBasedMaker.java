package org.unidal.webres.resource.model.transform;

import static org.unidal.webres.resource.model.Constants.ATTR_ACTIVE;
import static org.unidal.webres.resource.model.Constants.ATTR_AFTER_SLOT;
import static org.unidal.webres.resource.model.Constants.ATTR_BEFORE_SLOT;
import static org.unidal.webres.resource.model.Constants.ATTR_ENABLED;
import static org.unidal.webres.resource.model.Constants.ATTR_FLUSHED;
import static org.unidal.webres.resource.model.Constants.ATTR_ID;
import static org.unidal.webres.resource.model.Constants.ATTR_MAIN_SLOT;
import static org.unidal.webres.resource.model.Constants.ATTR_OLD_URN;
import static org.unidal.webres.resource.model.Constants.ATTR_OVERRIDE;
import static org.unidal.webres.resource.model.Constants.ATTR_SKIP_FRAGMENTS;
import static org.unidal.webres.resource.model.Constants.ATTR_URN;

import org.unidal.webres.dom.ITagNode;
import org.unidal.webres.resource.model.entity.CommonSlotRef;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.model.entity.SlotGroup;
import org.unidal.webres.resource.model.entity.SlotRef;

public class TagNodeBasedMaker implements IMaker<ITagNode> {

   @Override
   public CommonSlotRef buildCommonSlotRef(ITagNode node) {
      String id = node.getAttribute(ATTR_ID);
      String beforeSlot = node.getAttribute(ATTR_BEFORE_SLOT);
      String afterSlot = node.getAttribute(ATTR_AFTER_SLOT);

      CommonSlotRef commonSlotRef = new CommonSlotRef(id);

      if (beforeSlot != null) {
         commonSlotRef.setBeforeSlot(beforeSlot);
      }

      if (afterSlot != null) {
         commonSlotRef.setAfterSlot(afterSlot);
      }

      return commonSlotRef;
   }

   @Override
   public Page buildPage(ITagNode node) {
      String id = node.getAttribute(ATTR_ID);

      Page page = new Page(id);

      return page;
   }

   @Override
   public Resource buildResource(ITagNode node) {
      String urn = node.getAttribute(ATTR_URN);
      String oldUrn = node.getAttribute(ATTR_OLD_URN);
      String enabled = node.getAttribute(ATTR_ENABLED);

      Resource resource = new Resource(urn);

      if (oldUrn != null) {
         resource.setOldUrn(oldUrn);
      }

      if (enabled != null) {
         resource.setEnabled(Boolean.valueOf(enabled));
      }

      return resource;
   }

   @Override
   public Root buildRoot(ITagNode node) {
      Root root = new Root();

      return root;
   }

   @Override
   public Slot buildSlot(ITagNode node) {
      String id = node.getAttribute(ATTR_ID);
      String override = node.getAttribute(ATTR_OVERRIDE);
      String active = node.getAttribute(ATTR_ACTIVE);
      String flushed = node.getAttribute(ATTR_FLUSHED);
      String skipFragments = node.getAttribute(ATTR_SKIP_FRAGMENTS);

      Slot slot = new Slot(id);

      if (override != null) {
         slot.setOverride(Boolean.valueOf(override));
      }

      if (active != null) {
         slot.setActive(Boolean.valueOf(active));
      }

      if (flushed != null) {
         slot.setFlushed(Boolean.valueOf(flushed));
      }

      if (skipFragments != null) {
         slot.setSkipFragments(Boolean.valueOf(skipFragments));
      }

      return slot;
   }

   @Override
   public SlotGroup buildSlotGroup(ITagNode node) {
      String id = node.getAttribute(ATTR_ID);
      String mainSlot = node.getAttribute(ATTR_MAIN_SLOT);

      SlotGroup slotGroup = new SlotGroup(id);

      if (mainSlot != null) {
         slotGroup.setMainSlot(mainSlot);
      }

      return slotGroup;
   }

   @Override
   public SlotRef buildSlotRef(ITagNode node) {
      String id = node.getAttribute(ATTR_ID);

      SlotRef slotRef = new SlotRef(id);

      return slotRef;
   }
}
