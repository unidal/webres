package org.unidal.webres.resource.profile.entity;

import static org.unidal.webres.resource.profile.Constants.ATTR_ID;
import static org.unidal.webres.resource.profile.Constants.ENTITY_CSS_SLOT;

import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.resource.profile.BaseEntity;
import org.unidal.webres.resource.profile.IVisitor;

public class CssSlot extends BaseEntity<CssSlot> {
   private String m_id;

   private Boolean m_override;

   private List<Css> m_cssList = new ArrayList<Css>();

   public CssSlot(String id) {
      m_id = id;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitCssSlot(this);
   }

   public CssSlot addCss(Css css) {
      m_cssList.add(css);
      return this;
   }

   public Css findCss(String urn) {
      for (Css css : m_cssList) {
         if (!css.getUrn().equals(urn)) {
            continue;
         }

         return css;
      }

      return null;
   }

   public List<Css> getCssList() {
      return m_cssList;
   }

   public String getId() {
      return m_id;
   }

   public Boolean getOverride() {
      return m_override;
   }

   public boolean isOverride() {
      return m_override != null && m_override.booleanValue();
   }

   @Override
   public void mergeAttributes(CssSlot other) {
      assertAttributeEquals(other, ENTITY_CSS_SLOT, ATTR_ID, m_id, other.getId());

      if (other.getOverride() != null) {
         m_override = other.getOverride();
      }
   }

   public boolean removeCss(String urn) {
      int len = m_cssList.size();

      for (int i = 0; i < len; i++) {
         Css css = m_cssList.get(i);

         if (!css.getUrn().equals(urn)) {
            continue;
         }

         m_cssList.remove(i);
         return true;
      }

      return false;
   }

   public CssSlot setOverride(Boolean override) {
      m_override=override;
      return this;
   }

}
