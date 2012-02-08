package org.unidal.webres.resource.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.resource.model.BaseEntity;
import org.unidal.webres.resource.model.IVisitor;

public class Root extends BaseEntity<Root> {
   private List<Slot> m_commonSlots = new ArrayList<Slot>();

   private List<Page> m_pages = new ArrayList<Page>();

   public Root() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitRoot(this);
   }

   public Root addCommonSlot(Slot commonSlot) {
      m_commonSlots.add(commonSlot);
      return this;
   }

   public Root addPage(Page page) {
      m_pages.add(page);
      return this;
   }

   public Slot findCommonSlot(String id) {
      for (Slot commonSlot : m_commonSlots) {
         if (!commonSlot.getId().equals(id)) {
            continue;
         }

         return commonSlot;
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

   public List<Slot> getCommonSlots() {
      return m_commonSlots;
   }

   public List<Page> getPages() {
      return m_pages;
   }

   @Override
   public void mergeAttributes(Root other) {
   }

   public boolean removeCommonSlot(String id) {
      int len = m_commonSlots.size();

      for (int i = 0; i < len; i++) {
         Slot commonSlot = m_commonSlots.get(i);

         if (!commonSlot.getId().equals(id)) {
            continue;
         }

         m_commonSlots.remove(i);
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
