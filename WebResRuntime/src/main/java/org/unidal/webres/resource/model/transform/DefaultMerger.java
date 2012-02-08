package org.unidal.webres.resource.model.transform;

import java.util.Stack;

import org.unidal.webres.resource.model.IVisitor;
import org.unidal.webres.resource.model.entity.CommonSlotRef;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.model.entity.SlotGroup;
import org.unidal.webres.resource.model.entity.SlotRef;

public class DefaultMerger implements IVisitor {

   private Stack<Object> m_stack = new Stack<Object>();

   private Root m_root;

   public DefaultMerger(Root root) {
      m_root = root;
   }

   protected Root getRoot() {
      return m_root;
   }

   protected Stack<Object> getStack() {
      return m_stack;
   }

   @Override
   public void visitCommonSlotRef(CommonSlotRef commonSlotRef) {
      Object parent = m_stack.peek();
      CommonSlotRef old = null;

      if (parent instanceof Page) {
         Page page = (Page) parent;

         old = page.findCommonSlotRef(commonSlotRef.getId());

         if (old == null) {
            page.addCommonSlotRef(commonSlotRef);
         } else {
            old.mergeAttributes(commonSlotRef);
         }
      }

      visitCommonSlotRefChildren(old, commonSlotRef);
   }

   protected void visitCommonSlotRefChildren(CommonSlotRef old, CommonSlotRef commonSlotRef) {
   }

   @Override
   public void visitPage(Page page) {
      Object parent = m_stack.peek();
      Page old = null;

      if (parent instanceof Root) {
         Root root = (Root) parent;

         old = root.findPage(page.getId());

         if (old == null) {
            root.addPage(page);
         } else {
            old.mergeAttributes(page);
         }
      }

      visitPageChildren(old, page);
   }

   protected void visitPageChildren(Page old, Page page) {
      if (old != null) {
         m_stack.push(old);

         for (Slot slot : page.getSlots()) {
            visitSlot(slot);
         }

         for (CommonSlotRef commonSlotRef : page.getCommonSlotRefs()) {
            visitCommonSlotRef(commonSlotRef);
         }

         for (SlotGroup slotGroup : page.getSlotGroups()) {
            visitSlotGroup(slotGroup);
         }

         m_stack.pop();
      }
   }

   @Override
   public void visitResource(Resource resource) {
      Object parent = m_stack.peek();
      Resource old = null;

      if (parent instanceof Slot) {
         Slot slot = (Slot) parent;

         old = slot.findResource(resource.getUrn());

         if (old == null) {
            slot.addResource(resource);
         } else {
            old.mergeAttributes(resource);
         }
      }

      visitResourceChildren(old, resource);
   }

   protected void visitResourceChildren(Resource old, Resource resource) {
   }

   @Override
   public void visitRoot(Root root) {
      visitRootChildren(m_root, root);
   }

   protected void visitRootChildren(Root old, Root root) {
      if (old != null) {
         m_stack.push(old);

         for (Slot slot : root.getCommonSlots()) {
            visitSlot(slot);
         }

         for (Page page : root.getPages()) {
            visitPage(page);
         }

         m_stack.pop();
      }
   }

   @Override
   public void visitSlot(Slot slot) {
      Object parent = m_stack.peek();
      Slot old = null;

      if (parent instanceof Root) {
         Root root = (Root) parent;

         old = root.findCommonSlot(slot.getId());

         if (old == null) {
            root.addCommonSlot(slot);
         } else {
            old.mergeAttributes(slot);
         }
      } else if (parent instanceof Slot) {
         Slot _slot = (Slot) parent;

         old = _slot.getBeforeCommonSlot();

         if (old == null) {
            _slot.setBeforeCommonSlot(slot);
         } else {
            old.mergeAttributes(slot);
         }         old = _slot.getAfterCommonSlot();

         if (old == null) {
            _slot.setAfterCommonSlot(slot);
         } else {
            old.mergeAttributes(slot);
         }
      } else if (parent instanceof Page) {
         Page page = (Page) parent;

         old = page.findSlot(slot.getId());

         if (old == null) {
            page.addSlot(slot);
         } else {
            old.mergeAttributes(slot);
         }
      }

      visitSlotChildren(old, slot);
   }

   protected void visitSlotChildren(Slot old, Slot slot) {
      if (old != null) {
         m_stack.push(old);

         for (Resource resource : slot.getResources()) {
            visitResource(resource);
         }

         if (slot.getBeforeCommonSlot() != null) {
            visitSlot(slot.getBeforeCommonSlot());
         }

         if (slot.getAfterCommonSlot() != null) {
            visitSlot(slot.getAfterCommonSlot());
         }

         m_stack.pop();
      }
   }

   @Override
   public void visitSlotGroup(SlotGroup slotGroup) {
      Object parent = m_stack.peek();
      SlotGroup old = null;

      if (parent instanceof Page) {
         Page page = (Page) parent;

         old = page.findSlotGroup(slotGroup.getId());

         if (old == null) {
            page.addSlotGroup(slotGroup);
         } else {
            old.mergeAttributes(slotGroup);
         }
      }

      visitSlotGroupChildren(old, slotGroup);
   }

   protected void visitSlotGroupChildren(SlotGroup old, SlotGroup slotGroup) {
      if (old != null) {
         m_stack.push(old);

         for (SlotRef slotRef : slotGroup.getSlotRefs()) {
            visitSlotRef(slotRef);
         }

         m_stack.pop();
      }
   }

   @Override
   public void visitSlotRef(SlotRef slotRef) {
      Object parent = m_stack.peek();
      SlotRef old = null;

      if (parent instanceof SlotGroup) {
         SlotGroup slotGroup = (SlotGroup) parent;

         old = slotGroup.findSlotRef(slotRef.getId());

         if (old == null) {
            slotGroup.addSlotRef(slotRef);
         } else {
            old.mergeAttributes(slotRef);
         }
      }

      visitSlotRefChildren(old, slotRef);
   }

   protected void visitSlotRefChildren(SlotRef old, SlotRef slotRef) {
   }
}
