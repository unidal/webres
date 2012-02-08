package org.unidal.webres.resource.profile.entity;

import static org.unidal.webres.resource.profile.Constants.ATTR_ID;
import static org.unidal.webres.resource.profile.Constants.ENTITY_PAGE;

import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.resource.profile.BaseEntity;
import org.unidal.webres.resource.profile.IVisitor;

public class Page extends BaseEntity<Page> {
   private String m_id;

   private List<JsSlot> m_jsSlots = new ArrayList<JsSlot>();

   private List<CssSlot> m_cssSlots = new ArrayList<CssSlot>();

   private List<CommonJsSlotRef> m_commonJsSlotRefs = new ArrayList<CommonJsSlotRef>();

   private List<CommonCssSlotRef> m_commonCssSlotRefs = new ArrayList<CommonCssSlotRef>();

   private List<JsSlotGroup> m_jsSlotGroups = new ArrayList<JsSlotGroup>();

   private List<CssSlotGroup> m_cssSlotGroups = new ArrayList<CssSlotGroup>();

   private List<ImgDataUri> m_imgDataUris = new ArrayList<ImgDataUri>();

   public Page(String id) {
      m_id = id;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitPage(this);
   }

   public Page addCommonCssSlotRef(CommonCssSlotRef commonCssSlotRef) {
      m_commonCssSlotRefs.add(commonCssSlotRef);
      return this;
   }

   public Page addCommonJsSlotRef(CommonJsSlotRef commonJsSlotRef) {
      m_commonJsSlotRefs.add(commonJsSlotRef);
      return this;
   }

   public Page addCssSlot(CssSlot cssSlot) {
      m_cssSlots.add(cssSlot);
      return this;
   }

   public Page addCssSlotGroup(CssSlotGroup cssSlotGroup) {
      m_cssSlotGroups.add(cssSlotGroup);
      return this;
   }

   public Page addImgDataUri(ImgDataUri imgDataUri) {
      m_imgDataUris.add(imgDataUri);
      return this;
   }

   public Page addJsSlot(JsSlot jsSlot) {
      m_jsSlots.add(jsSlot);
      return this;
   }

   public Page addJsSlotGroup(JsSlotGroup jsSlotGroup) {
      m_jsSlotGroups.add(jsSlotGroup);
      return this;
   }

   public CommonCssSlotRef findCommonCssSlotRef(String id) {
      for (CommonCssSlotRef commonCssSlotRef : m_commonCssSlotRefs) {
         if (!commonCssSlotRef.getId().equals(id)) {
            continue;
         }

         return commonCssSlotRef;
      }

      return null;
   }

   public CommonJsSlotRef findCommonJsSlotRef(String id) {
      for (CommonJsSlotRef commonJsSlotRef : m_commonJsSlotRefs) {
         if (!commonJsSlotRef.getId().equals(id)) {
            continue;
         }

         return commonJsSlotRef;
      }

      return null;
   }

   public CssSlot findCssSlot(String id) {
      for (CssSlot cssSlot : m_cssSlots) {
         if (!cssSlot.getId().equals(id)) {
            continue;
         }

         return cssSlot;
      }

      return null;
   }

   public CssSlotGroup findCssSlotGroup(String id) {
      for (CssSlotGroup cssSlotGroup : m_cssSlotGroups) {
         if (!cssSlotGroup.getId().equals(id)) {
            continue;
         }

         return cssSlotGroup;
      }

      return null;
   }

   public ImgDataUri findImgDataUri(String urn) {
      for (ImgDataUri imgDataUri : m_imgDataUris) {
         if (!imgDataUri.getUrn().equals(urn)) {
            continue;
         }

         return imgDataUri;
      }

      return null;
   }

   public JsSlot findJsSlot(String id) {
      for (JsSlot jsSlot : m_jsSlots) {
         if (!jsSlot.getId().equals(id)) {
            continue;
         }

         return jsSlot;
      }

      return null;
   }

   public JsSlotGroup findJsSlotGroup(String id) {
      for (JsSlotGroup jsSlotGroup : m_jsSlotGroups) {
         if (!jsSlotGroup.getId().equals(id)) {
            continue;
         }

         return jsSlotGroup;
      }

      return null;
   }

   public List<CommonCssSlotRef> getCommonCssSlotRefs() {
      return m_commonCssSlotRefs;
   }

   public List<CommonJsSlotRef> getCommonJsSlotRefs() {
      return m_commonJsSlotRefs;
   }

   public List<CssSlotGroup> getCssSlotGroups() {
      return m_cssSlotGroups;
   }

   public List<CssSlot> getCssSlots() {
      return m_cssSlots;
   }

   public String getId() {
      return m_id;
   }

   public List<ImgDataUri> getImgDataUris() {
      return m_imgDataUris;
   }

   public List<JsSlotGroup> getJsSlotGroups() {
      return m_jsSlotGroups;
   }

   public List<JsSlot> getJsSlots() {
      return m_jsSlots;
   }

   @Override
   public void mergeAttributes(Page other) {
      assertAttributeEquals(other, ENTITY_PAGE, ATTR_ID, m_id, other.getId());

   }

   public boolean removeCommonCssSlotRef(String id) {
      int len = m_commonCssSlotRefs.size();

      for (int i = 0; i < len; i++) {
         CommonCssSlotRef commonCssSlotRef = m_commonCssSlotRefs.get(i);

         if (!commonCssSlotRef.getId().equals(id)) {
            continue;
         }

         m_commonCssSlotRefs.remove(i);
         return true;
      }

      return false;
   }

   public boolean removeCommonJsSlotRef(String id) {
      int len = m_commonJsSlotRefs.size();

      for (int i = 0; i < len; i++) {
         CommonJsSlotRef commonJsSlotRef = m_commonJsSlotRefs.get(i);

         if (!commonJsSlotRef.getId().equals(id)) {
            continue;
         }

         m_commonJsSlotRefs.remove(i);
         return true;
      }

      return false;
   }

   public boolean removeCssSlot(String id) {
      int len = m_cssSlots.size();

      for (int i = 0; i < len; i++) {
         CssSlot cssSlot = m_cssSlots.get(i);

         if (!cssSlot.getId().equals(id)) {
            continue;
         }

         m_cssSlots.remove(i);
         return true;
      }

      return false;
   }

   public boolean removeCssSlotGroup(String id) {
      int len = m_cssSlotGroups.size();

      for (int i = 0; i < len; i++) {
         CssSlotGroup cssSlotGroup = m_cssSlotGroups.get(i);

         if (!cssSlotGroup.getId().equals(id)) {
            continue;
         }

         m_cssSlotGroups.remove(i);
         return true;
      }

      return false;
   }

   public boolean removeImgDataUri(String urn) {
      int len = m_imgDataUris.size();

      for (int i = 0; i < len; i++) {
         ImgDataUri imgDataUri = m_imgDataUris.get(i);

         if (!imgDataUri.getUrn().equals(urn)) {
            continue;
         }

         m_imgDataUris.remove(i);
         return true;
      }

      return false;
   }

   public boolean removeJsSlot(String id) {
      int len = m_jsSlots.size();

      for (int i = 0; i < len; i++) {
         JsSlot jsSlot = m_jsSlots.get(i);

         if (!jsSlot.getId().equals(id)) {
            continue;
         }

         m_jsSlots.remove(i);
         return true;
      }

      return false;
   }

   public boolean removeJsSlotGroup(String id) {
      int len = m_jsSlotGroups.size();

      for (int i = 0; i < len; i++) {
         JsSlotGroup jsSlotGroup = m_jsSlotGroups.get(i);

         if (!jsSlotGroup.getId().equals(id)) {
            continue;
         }

         m_jsSlotGroups.remove(i);
         return true;
      }

      return false;
   }

}
