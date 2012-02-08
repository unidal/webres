package org.unidal.webres.resource.profile.entity;

import static org.unidal.webres.resource.profile.Constants.ATTR_ID;
import static org.unidal.webres.resource.profile.Constants.ENTITY_CSS_SLOT_GROUP;

import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.resource.profile.BaseEntity;
import org.unidal.webres.resource.profile.IVisitor;

public class CssSlotGroup extends BaseEntity<CssSlotGroup> {
   private String m_id;

   private String m_mainSlot;

   private List<CssSlotRef> m_cssSlotRefs = new ArrayList<CssSlotRef>();

   public CssSlotGroup(String id) {
      m_id = id;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitCssSlotGroup(this);
   }

   public CssSlotGroup addCssSlotRef(CssSlotRef cssSlotRef) {
      m_cssSlotRefs.add(cssSlotRef);
      return this;
   }

   public CssSlotRef findCssSlotRef(String id) {
      for (CssSlotRef cssSlotRef : m_cssSlotRefs) {
         if (!cssSlotRef.getId().equals(id)) {
            continue;
         }

         return cssSlotRef;
      }

      return null;
   }

   public List<CssSlotRef> getCssSlotRefs() {
      return m_cssSlotRefs;
   }

   public String getId() {
      return m_id;
   }

   public String getMainSlot() {
      return m_mainSlot;
   }

   @Override
   public void mergeAttributes(CssSlotGroup other) {
      assertAttributeEquals(other, ENTITY_CSS_SLOT_GROUP, ATTR_ID, m_id, other.getId());

      if (other.getMainSlot() != null) {
         m_mainSlot = other.getMainSlot();
      }
   }

   public boolean removeCssSlotRef(String id) {
      int len = m_cssSlotRefs.size();

      for (int i = 0; i < len; i++) {
         CssSlotRef cssSlotRef = m_cssSlotRefs.get(i);

         if (!cssSlotRef.getId().equals(id)) {
            continue;
         }

         m_cssSlotRefs.remove(i);
         return true;
      }

      return false;
   }

   public CssSlotGroup setMainSlot(String mainSlot) {
      m_mainSlot=mainSlot;
      return this;
   }

}
