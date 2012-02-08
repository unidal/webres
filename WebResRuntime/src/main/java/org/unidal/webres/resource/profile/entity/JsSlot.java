package org.unidal.webres.resource.profile.entity;

import static org.unidal.webres.resource.profile.Constants.ATTR_ID;
import static org.unidal.webres.resource.profile.Constants.ENTITY_JS_SLOT;

import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.resource.profile.BaseEntity;
import org.unidal.webres.resource.profile.IVisitor;

public class JsSlot extends BaseEntity<JsSlot> {
   private String m_id;

   private Boolean m_override;

   private List<Js> m_jsList = new ArrayList<Js>();

   public JsSlot(String id) {
      m_id = id;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitJsSlot(this);
   }

   public JsSlot addJs(Js js) {
      m_jsList.add(js);
      return this;
   }

   public Js findJs(String urn) {
      for (Js js : m_jsList) {
         if (!js.getUrn().equals(urn)) {
            continue;
         }

         return js;
      }

      return null;
   }

   public String getId() {
      return m_id;
   }

   public List<Js> getJsList() {
      return m_jsList;
   }

   public Boolean getOverride() {
      return m_override;
   }

   public boolean isOverride() {
      return m_override != null && m_override.booleanValue();
   }

   @Override
   public void mergeAttributes(JsSlot other) {
      assertAttributeEquals(other, ENTITY_JS_SLOT, ATTR_ID, m_id, other.getId());

      if (other.getOverride() != null) {
         m_override = other.getOverride();
      }
   }

   public boolean removeJs(String urn) {
      int len = m_jsList.size();

      for (int i = 0; i < len; i++) {
         Js js = m_jsList.get(i);

         if (!js.getUrn().equals(urn)) {
            continue;
         }

         m_jsList.remove(i);
         return true;
      }

      return false;
   }

   public JsSlot setOverride(Boolean override) {
      m_override=override;
      return this;
   }

}
