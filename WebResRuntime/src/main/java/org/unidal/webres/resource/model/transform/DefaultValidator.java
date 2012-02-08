package org.unidal.webres.resource.model.transform;

import static org.unidal.webres.resource.model.Constants.ATTR_ID;
import static org.unidal.webres.resource.model.Constants.ATTR_MAIN_SLOT;
import static org.unidal.webres.resource.model.Constants.ATTR_URN;
import static org.unidal.webres.resource.model.Constants.ENTITY_COMMON_SLOT_REF;
import static org.unidal.webres.resource.model.Constants.ENTITY_COMMON_SLOTS;
import static org.unidal.webres.resource.model.Constants.ENTITY_PAGE;
import static org.unidal.webres.resource.model.Constants.ENTITY_PAGES;
import static org.unidal.webres.resource.model.Constants.ENTITY_RESOURCE;
import static org.unidal.webres.resource.model.Constants.ENTITY_ROOT;
import static org.unidal.webres.resource.model.Constants.ENTITY_SLOT;
import static org.unidal.webres.resource.model.Constants.ENTITY_SLOT_GROUP;
import static org.unidal.webres.resource.model.Constants.ENTITY_SLOT_REF;

import java.util.Stack;

import org.unidal.webres.resource.model.IVisitor;
import org.unidal.webres.resource.model.entity.CommonSlotRef;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.model.entity.SlotGroup;
import org.unidal.webres.resource.model.entity.SlotRef;

public class DefaultValidator implements IVisitor {

   private Path m_path = new Path();
   
   protected void assertRequired(String name, Object value) {
      if (value == null) {
         throw new RuntimeException(String.format("%s at path(%s) is required!", name, m_path));
      }
   }

   @Override
   public void visitCommonSlotRef(CommonSlotRef commonSlotRef) {
      m_path.down(ENTITY_COMMON_SLOT_REF);

      assertRequired(ATTR_ID, commonSlotRef.getId());

      m_path.up(ENTITY_COMMON_SLOT_REF);
   }

   @Override
   public void visitPage(Page page) {
      m_path.down(ENTITY_PAGE);

      visitPageChildren(page);

      m_path.up(ENTITY_PAGE);
   }

   protected void visitPageChildren(Page page) {
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
      m_path.down(ENTITY_RESOURCE);

      assertRequired(ATTR_URN, resource.getUrn());

      m_path.up(ENTITY_RESOURCE);
   }

   @Override
   public void visitRoot(Root root) {
      m_path.down(ENTITY_ROOT);

      visitRootChildren(root);

      m_path.up(ENTITY_ROOT);
   }

   protected void visitRootChildren(Root root) {
      m_path.down(ENTITY_COMMON_SLOTS);

      for (Slot slot : root.getCommonSlots()) {
         visitSlot(slot);
      }

      m_path.up(ENTITY_COMMON_SLOTS);

      m_path.down(ENTITY_PAGES);

      for (Page page : root.getPages()) {
         visitPage(page);
      }

      m_path.up(ENTITY_PAGES);
   }

   @Override
   public void visitSlot(Slot slot) {
      m_path.down(ENTITY_SLOT);

      assertRequired(ATTR_ID, slot.getId());

      visitSlotChildren(slot);

      m_path.up(ENTITY_SLOT);
   }

   protected void visitSlotChildren(Slot slot) {
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
      m_path.down(ENTITY_SLOT_GROUP);

      assertRequired(ATTR_ID, slotGroup.getId());
      assertRequired(ATTR_MAIN_SLOT, slotGroup.getMainSlot());

      visitSlotGroupChildren(slotGroup);

      m_path.up(ENTITY_SLOT_GROUP);
   }

   protected void visitSlotGroupChildren(SlotGroup slotGroup) {
      for (SlotRef slotRef : slotGroup.getSlotRefs()) {
         visitSlotRef(slotRef);
      }
   }

   @Override
   public void visitSlotRef(SlotRef slotRef) {
      m_path.down(ENTITY_SLOT_REF);

      assertRequired(ATTR_ID, slotRef.getId());

      m_path.up(ENTITY_SLOT_REF);
   }

   static class Path {
      private Stack<String> m_sections = new Stack<String>();

      public Path down(String nextSection) {
         m_sections.push(nextSection);

         return this;
      }

      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder();

         for (String section : m_sections) {
            sb.append('/').append(section);
         }

         return sb.toString();
      }

      public Path up(String currentSection) {
         if (m_sections.isEmpty() || !m_sections.peek().equals(currentSection)) {
            throw new RuntimeException("INTERNAL ERROR: stack mismatched!");
         }

         m_sections.pop();
         return this;
      }
   }
}
