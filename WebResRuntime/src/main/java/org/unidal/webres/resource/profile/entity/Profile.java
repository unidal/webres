package org.unidal.webres.resource.profile.entity;

import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.resource.profile.BaseEntity;
import org.unidal.webres.resource.profile.IVisitor;

public class Profile extends BaseEntity<Profile> {
   private List<JsSlot> m_commonJsSlots = new ArrayList<JsSlot>();

   private List<CssSlot> m_commonCssSlots = new ArrayList<CssSlot>();

   private List<Page> m_pages = new ArrayList<Page>();

   public Profile() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitProfile(this);
   }

   public Profile addCommonCssSlot(CssSlot commonCssSlot) {
      m_commonCssSlots.add(commonCssSlot);
      return this;
   }

   public Profile addCommonJsSlot(JsSlot commonJsSlot) {
      m_commonJsSlots.add(commonJsSlot);
      return this;
   }

   public Profile addPage(Page page) {
      m_pages.add(page);
      return this;
   }

   public CssSlot findCommonCssSlot(String id) {
      for (CssSlot commonCssSlot : m_commonCssSlots) {
         if (!commonCssSlot.getId().equals(id)) {
            continue;
         }

         return commonCssSlot;
      }

      return null;
   }

   public JsSlot findCommonJsSlot(String id) {
      for (JsSlot commonJsSlot : m_commonJsSlots) {
         if (!commonJsSlot.getId().equals(id)) {
            continue;
         }

         return commonJsSlot;
      }

      return null;
   }

   public Page findPage(String id) {
      for (Page page : m_pages) {
         if (page.getId() == null) {
            if (id != null) {
               continue;
            }
         } else if (!page.getId().equals(id)) {
            continue;
         }

         return page;
      }

      return null;
   }

   public List<CssSlot> getCommonCssSlots() {
      return m_commonCssSlots;
   }

   public List<JsSlot> getCommonJsSlots() {
      return m_commonJsSlots;
   }

   public List<Page> getPages() {
      return m_pages;
   }

   @Override
   public void mergeAttributes(Profile other) {
   }

   public boolean removeCommonCssSlot(String id) {
      int len = m_commonCssSlots.size();

      for (int i = 0; i < len; i++) {
         CssSlot commonCssSlot = m_commonCssSlots.get(i);

         if (!commonCssSlot.getId().equals(id)) {
            continue;
         }

         m_commonCssSlots.remove(i);
         return true;
      }

      return false;
   }

   public boolean removeCommonJsSlot(String id) {
      int len = m_commonJsSlots.size();

      for (int i = 0; i < len; i++) {
         JsSlot commonJsSlot = m_commonJsSlots.get(i);

         if (!commonJsSlot.getId().equals(id)) {
            continue;
         }

         m_commonJsSlots.remove(i);
         return true;
      }

      return false;
   }

   public boolean removePage(String id) {
      int len = m_pages.size();

      for (int i = 0; i < len; i++) {
         Page page = m_pages.get(i);

         if (!page.getId().equals(id)) {
            continue;
         }

         m_pages.remove(i);
         return true;
      }

      return false;
   }

}
