package org.unidal.webres.resource.profile.entity;

import static org.unidal.webres.resource.profile.Constants.ATTR_ID;
import static org.unidal.webres.resource.profile.Constants.ENTITY_JS_SLOT_GROUP;

import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.resource.profile.BaseEntity;
import org.unidal.webres.resource.profile.IVisitor;

public class JsSlotGroup extends BaseEntity<JsSlotGroup> {
   private String m_id;

   private String m_mainSlot;

   private List<JsSlotRef> m_jsSlotRefs = new ArrayList<JsSlotRef>();

   public JsSlotGroup(String id) {
      m_id = id;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitJsSlotGroup(this);
   }

   public JsSlotGroup addJsSlotRef(JsSlotRef jsSlotRef) {
      m_jsSlotRefs.add(jsSlotRef);
      return this;
   }

   public JsSlotRef findJsSlotRef(String id) {
      for (JsSlotRef jsSlotRef : m_jsSlotRefs) {
         if (!jsSlotRef.getId().equals(id)) {
            continue;
         }

         return jsSlotRef;
      }

      return null;
   }

   public String getId() {
      return m_id;
   }

   public List<JsSlotRef> getJsSlotRefs() {
      return m_jsSlotRefs;
   }

   public String getMainSlot() {
      return m_mainSlot;
   }

   @Override
   public void mergeAttributes(JsSlotGroup other) {
      assertAttributeEquals(other, ENTITY_JS_SLOT_GROUP, ATTR_ID, m_id, other.getId());

      if (other.getMainSlot() != null) {
         m_mainSlot = other.getMainSlot();
      }
   }

   public boolean removeJsSlotRef(String id) {
      int len = m_jsSlotRefs.size();

      for (int i = 0; i < len; i++) {
         JsSlotRef jsSlotRef = m_jsSlotRefs.get(i);

         if (!jsSlotRef.getId().equals(id)) {
            continue;
         }

         m_jsSlotRefs.remove(i);
         return true;
      }

      return false;
   }

   public JsSlotGroup setMainSlot(String mainSlot) {
      m_mainSlot=mainSlot;
      return this;
   }

}
