package org.unidal.webres.resource.model.transform;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.unidal.webres.resource.model.IVisitor;
import org.unidal.webres.resource.model.entity.CommonSlotRef;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.model.entity.SlotGroup;
import org.unidal.webres.resource.model.entity.SlotRef;

public class ModelModifier implements IVisitor {
   private Root m_root;

   @Override
   public void visitCommonSlotRef(CommonSlotRef commonSlotRef) {
   }

   @Override
   public void visitPage(Page page) {
      List<CommonSlotRef> commonSlotRefs = page.getCommonSlotRefs();
      Set<String> commonResources = new HashSet<String>();

      for (CommonSlotRef ref : commonSlotRefs) {
         Slot commonSlot = m_root.findCommonSlot(ref.getId());

         if (commonSlot != null) {
            String slotId = null;
            boolean flag = false;

            if (ref.getBeforeSlot() != null) {
               flag = true;
               slotId = ref.getBeforeSlot();
            } else if (ref.getAfterSlot() != null) {
               flag = false;
               slotId = ref.getAfterSlot();
            }

            if (slotId != null) {
               Slot slot = page.findSlot(slotId);

               if (slot != null) {
                  // link common slot and page slot
                  if (flag) {
                     slot.setBeforeCommonSlot(commonSlot);
                  } else {
                     slot.setAfterCommonSlot(commonSlot);
                  }
               }
            }

            // collect all common slot resources
            for (Resource resource : commonSlot.getResources()) {
               commonResources.add(resource.getUrn());
            }
         }
      }

      // filter out resource abstracted in common slots
      if (!commonResources.isEmpty()) {
         for (Slot slot : page.getSlots()) {
            List<Resource> resources = slot.getResources();
            int size = resources.size();

            for (int i = size - 1; i >= 0; i--) {
               Resource resource = resources.get(i);

               if (commonResources.contains(resource.getUrn())) {
                  resources.remove(i);
               }
            }
         }
      }

      // merge logical slots into physical slot
      for (SlotGroup group : page.getSlotGroups()) {
         String mainSlotId = group.getMainSlot();
         Slot mainSlot = page.findSlot(mainSlotId);

         if (mainSlot != null) {
            List<Resource> resources = new ArrayList<Resource>();

            for (SlotRef ref : group.getSlotRefs()) {
               Slot slot = page.findSlot(ref.getId());

               if (slot != null) {
                  resources.addAll(slot.getResources());
                  slot.getResources().clear();
               }
            }

            mainSlot.getResources().addAll(resources);
         }
      }
   }

   @Override
   public void visitResource(Resource resource) {
   }

   @Override
   public void visitRoot(Root root) {
      m_root = root;

      for (Page page : root.getPages()) {
         visitPage(page);
      }
   }

   @Override
   public void visitSlot(Slot slot) {
   }

   @Override
   public void visitSlotGroup(SlotGroup slotGroup) {
   }

   @Override
   public void visitSlotRef(SlotRef slotRef) {
   }
}
