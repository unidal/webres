package org.unidal.webres.resource.profile.entity;

import static org.unidal.webres.resource.profile.Constants.ATTR_ID;
import static org.unidal.webres.resource.profile.Constants.ENTITY_JS_SLOT_REF;

import org.unidal.webres.resource.profile.BaseEntity;
import org.unidal.webres.resource.profile.IVisitor;

public class JsSlotRef extends BaseEntity<JsSlotRef> {
   private String m_id;

   public JsSlotRef(String id) {
      m_id = id;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitJsSlotRef(this);
   }

   public String getId() {
      return m_id;
   }

   @Override
   public void mergeAttributes(JsSlotRef other) {
      assertAttributeEquals(other, ENTITY_JS_SLOT_REF, ATTR_ID, m_id, other.getId());

   }

}
