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
import static org.unidal.webres.resource.model.Constants.ENTITY_COMMON_SLOTS;
import static org.unidal.webres.resource.model.Constants.ENTITY_COMMON_SLOT_REF;
import static org.unidal.webres.resource.model.Constants.ENTITY_PAGE;
import static org.unidal.webres.resource.model.Constants.ENTITY_PAGES;
import static org.unidal.webres.resource.model.Constants.ENTITY_RESOURCE;
import static org.unidal.webres.resource.model.Constants.ENTITY_ROOT;
import static org.unidal.webres.resource.model.Constants.ENTITY_SLOT;
import static org.unidal.webres.resource.model.Constants.ENTITY_SLOT_GROUP;
import static org.unidal.webres.resource.model.Constants.ENTITY_SLOT_REF;

import org.unidal.webres.resource.model.IVisitor;
import org.unidal.webres.resource.model.entity.CommonSlotRef;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.model.entity.SlotGroup;
import org.unidal.webres.resource.model.entity.SlotRef;

public class DefaultXmlBuilder implements IVisitor {

   private int m_level;

   private StringBuilder m_sb = new StringBuilder(2048);

   protected void endTag(String name) {
      m_level--;

      indent();
      m_sb.append("</").append(name).append(">\r\n");
   }

   public String getString() {
      return m_sb.toString();
   }

   protected void indent() {
      for (int i = m_level - 1; i >= 0; i--) {
         m_sb.append("   ");
      }
   }


   protected void startTag(String name) {
      startTag(name, null, false);
   }
   
   protected void startTag(String name, java.util.Map<String, String> dynamicAttributes, boolean closed, Object... nameValues) {
      startTag(name, dynamicAttributes, null, closed, nameValues);
   }

   protected void startTag(String name, java.util.Map<String, String> dynamicAttributes, Object... nameValues) {
      startTag(name, dynamicAttributes, false, nameValues);
   }

   protected void startTag(String name, java.util.Map<String, String> dynamicAttributes, Object text, boolean closed, Object... nameValues) {
      indent();

      m_sb.append('<').append(name);

      int len = nameValues.length;

      for (int i = 0; i + 1 < len; i += 2) {
         Object attrName = nameValues[i];
         Object attrValue = nameValues[i + 1];

         if (attrValue != null) {
            m_sb.append(' ').append(attrName).append("=\"").append(attrValue).append('"');
         }
      }

      if (dynamicAttributes != null) {
         for (java.util.Map.Entry<String, String> e : dynamicAttributes.entrySet()) {
            m_sb.append(' ').append(e.getKey()).append("=\"").append(e.getValue()).append('"');
         }
      }

      if (text != null && closed) {
         m_sb.append('>');
         m_sb.append(text == null ? "" : text);
         m_sb.append("</").append(name).append(">\r\n");
      } else {
         if (closed) {
            m_sb.append('/');
         } else {
            m_level++;
         }
   
         m_sb.append(">\r\n");
      }
   }

   @Override
   public void visitCommonSlotRef(CommonSlotRef commonSlotRef) {
      startTag(ENTITY_COMMON_SLOT_REF, null, true, ATTR_ID, commonSlotRef.getId(), ATTR_BEFORE_SLOT, commonSlotRef.getBeforeSlot(), ATTR_AFTER_SLOT, commonSlotRef.getAfterSlot());
   }

   @Override
   public void visitPage(Page page) {
      startTag(ENTITY_PAGE, null, ATTR_ID, page.getId());

      if (!page.getSlots().isEmpty()) {
         for (Slot slot : page.getSlots()) {
            visitSlot(slot);
         }
      }

      if (!page.getCommonSlotRefs().isEmpty()) {
         for (CommonSlotRef commonSlotRef : page.getCommonSlotRefs()) {
            visitCommonSlotRef(commonSlotRef);
         }
      }

      if (!page.getSlotGroups().isEmpty()) {
         for (SlotGroup slotGroup : page.getSlotGroups()) {
            visitSlotGroup(slotGroup);
         }
      }

      endTag(ENTITY_PAGE);
   }

   @Override
   public void visitResource(Resource resource) {
      startTag(ENTITY_RESOURCE, null, true, ATTR_URN, resource.getUrn(), ATTR_OLD_URN, resource.getOldUrn(), ATTR_ENABLED, resource.getEnabled());
   }

   @Override
   public void visitRoot(Root root) {
      startTag(ENTITY_ROOT, null);

      if (!root.getCommonSlots().isEmpty()) {
         startTag(ENTITY_COMMON_SLOTS);

         for (Slot commonSlot : root.getCommonSlots()) {
            visitSlot(commonSlot);
         }

         endTag(ENTITY_COMMON_SLOTS);
      }

      if (!root.getPages().isEmpty()) {
         startTag(ENTITY_PAGES);

         for (Page page : root.getPages()) {
            visitPage(page);
         }

         endTag(ENTITY_PAGES);
      }

      endTag(ENTITY_ROOT);
   }

   @Override
   public void visitSlot(Slot slot) {
      startTag(ENTITY_SLOT, null, ATTR_ID, slot.getId(), ATTR_OVERRIDE, slot.getOverride(), ATTR_ACTIVE, slot.getActive(), ATTR_FLUSHED, slot.getFlushed(), ATTR_SKIP_FRAGMENTS, slot.getSkipFragments());

      if (!slot.getResources().isEmpty()) {
         for (Resource resource : slot.getResources()) {
            visitResource(resource);
         }
      }

      endTag(ENTITY_SLOT);
   }

   @Override
   public void visitSlotGroup(SlotGroup slotGroup) {
      startTag(ENTITY_SLOT_GROUP, null, ATTR_ID, slotGroup.getId(), ATTR_MAIN_SLOT, slotGroup.getMainSlot());

      if (!slotGroup.getSlotRefs().isEmpty()) {
         for (SlotRef slotRef : slotGroup.getSlotRefs()) {
            visitSlotRef(slotRef);
         }
      }

      endTag(ENTITY_SLOT_GROUP);
   }

   @Override
   public void visitSlotRef(SlotRef slotRef) {
      startTag(ENTITY_SLOT_REF, null, true, ATTR_ID, slotRef.getId());
   }
}
