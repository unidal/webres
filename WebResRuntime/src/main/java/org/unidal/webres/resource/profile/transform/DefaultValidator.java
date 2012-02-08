package org.unidal.webres.resource.profile.transform;

import static org.unidal.webres.resource.profile.Constants.ATTR_ID;
import static org.unidal.webres.resource.profile.Constants.ATTR_MAIN_SLOT;
import static org.unidal.webres.resource.profile.Constants.ATTR_URN;
import static org.unidal.webres.resource.profile.Constants.ENTITY_COMMON_CSS_SLOT_REF;
import static org.unidal.webres.resource.profile.Constants.ENTITY_COMMON_CSS_SLOTS;
import static org.unidal.webres.resource.profile.Constants.ENTITY_COMMON_JS_SLOT_REF;
import static org.unidal.webres.resource.profile.Constants.ENTITY_COMMON_JS_SLOTS;
import static org.unidal.webres.resource.profile.Constants.ENTITY_CSS;
import static org.unidal.webres.resource.profile.Constants.ENTITY_CSS_SLOT;
import static org.unidal.webres.resource.profile.Constants.ENTITY_CSS_SLOT_GROUP;
import static org.unidal.webres.resource.profile.Constants.ENTITY_CSS_SLOT_REF;
import static org.unidal.webres.resource.profile.Constants.ENTITY_IMG_DATA_URI;
import static org.unidal.webres.resource.profile.Constants.ENTITY_JS;
import static org.unidal.webres.resource.profile.Constants.ENTITY_JS_SLOT;
import static org.unidal.webres.resource.profile.Constants.ENTITY_JS_SLOT_GROUP;
import static org.unidal.webres.resource.profile.Constants.ENTITY_JS_SLOT_REF;
import static org.unidal.webres.resource.profile.Constants.ENTITY_PAGE;
import static org.unidal.webres.resource.profile.Constants.ENTITY_PAGES;
import static org.unidal.webres.resource.profile.Constants.ENTITY_PROFILE;

import java.util.Stack;

import org.unidal.webres.resource.profile.IVisitor;
import org.unidal.webres.resource.profile.entity.CommonCssSlotRef;
import org.unidal.webres.resource.profile.entity.CommonJsSlotRef;
import org.unidal.webres.resource.profile.entity.Css;
import org.unidal.webres.resource.profile.entity.CssSlot;
import org.unidal.webres.resource.profile.entity.CssSlotGroup;
import org.unidal.webres.resource.profile.entity.CssSlotRef;
import org.unidal.webres.resource.profile.entity.ImgDataUri;
import org.unidal.webres.resource.profile.entity.Js;
import org.unidal.webres.resource.profile.entity.JsSlot;
import org.unidal.webres.resource.profile.entity.JsSlotGroup;
import org.unidal.webres.resource.profile.entity.JsSlotRef;
import org.unidal.webres.resource.profile.entity.Page;
import org.unidal.webres.resource.profile.entity.Profile;

public class DefaultValidator implements IVisitor {

   private Path m_path = new Path();
   
   protected void assertRequired(String name, Object value) {
      if (value == null) {
         throw new RuntimeException(String.format("%s at path(%s) is required!", name, m_path));
      }
   }

   @Override
   public void visitCommonCssSlotRef(CommonCssSlotRef commonCssSlotRef) {
      m_path.down(ENTITY_COMMON_CSS_SLOT_REF);

      assertRequired(ATTR_ID, commonCssSlotRef.getId());

      m_path.up(ENTITY_COMMON_CSS_SLOT_REF);
   }

   @Override
   public void visitCommonJsSlotRef(CommonJsSlotRef commonJsSlotRef) {
      m_path.down(ENTITY_COMMON_JS_SLOT_REF);

      assertRequired(ATTR_ID, commonJsSlotRef.getId());

      m_path.up(ENTITY_COMMON_JS_SLOT_REF);
   }

   @Override
   public void visitCss(Css css) {
      m_path.down(ENTITY_CSS);

      assertRequired(ATTR_URN, css.getUrn());

      m_path.up(ENTITY_CSS);
   }

   @Override
   public void visitCssSlot(CssSlot cssSlot) {
      m_path.down(ENTITY_CSS_SLOT);

      assertRequired(ATTR_ID, cssSlot.getId());

      visitCssSlotChildren(cssSlot);

      m_path.up(ENTITY_CSS_SLOT);
   }

   protected void visitCssSlotChildren(CssSlot cssSlot) {
      for (Css css : cssSlot.getCssList()) {
         visitCss(css);
      }
   }

   @Override
   public void visitCssSlotGroup(CssSlotGroup cssSlotGroup) {
      m_path.down(ENTITY_CSS_SLOT_GROUP);

      assertRequired(ATTR_ID, cssSlotGroup.getId());
      assertRequired(ATTR_MAIN_SLOT, cssSlotGroup.getMainSlot());

      visitCssSlotGroupChildren(cssSlotGroup);

      m_path.up(ENTITY_CSS_SLOT_GROUP);
   }

   protected void visitCssSlotGroupChildren(CssSlotGroup cssSlotGroup) {
      for (CssSlotRef cssSlotRef : cssSlotGroup.getCssSlotRefs()) {
         visitCssSlotRef(cssSlotRef);
      }
   }

   @Override
   public void visitCssSlotRef(CssSlotRef cssSlotRef) {
      m_path.down(ENTITY_CSS_SLOT_REF);

      assertRequired(ATTR_ID, cssSlotRef.getId());

      m_path.up(ENTITY_CSS_SLOT_REF);
   }

   @Override
   public void visitImgDataUri(ImgDataUri imgDataUri) {
      m_path.down(ENTITY_IMG_DATA_URI);

      assertRequired(ATTR_URN, imgDataUri.getUrn());

      m_path.up(ENTITY_IMG_DATA_URI);
   }

   @Override
   public void visitJs(Js js) {
      m_path.down(ENTITY_JS);

      assertRequired(ATTR_URN, js.getUrn());

      m_path.up(ENTITY_JS);
   }

   @Override
   public void visitJsSlot(JsSlot jsSlot) {
      m_path.down(ENTITY_JS_SLOT);

      assertRequired(ATTR_ID, jsSlot.getId());

      visitJsSlotChildren(jsSlot);

      m_path.up(ENTITY_JS_SLOT);
   }

   protected void visitJsSlotChildren(JsSlot jsSlot) {
      for (Js js : jsSlot.getJsList()) {
         visitJs(js);
      }
   }

   @Override
   public void visitJsSlotGroup(JsSlotGroup jsSlotGroup) {
      m_path.down(ENTITY_JS_SLOT_GROUP);

      assertRequired(ATTR_ID, jsSlotGroup.getId());
      assertRequired(ATTR_MAIN_SLOT, jsSlotGroup.getMainSlot());

      visitJsSlotGroupChildren(jsSlotGroup);

      m_path.up(ENTITY_JS_SLOT_GROUP);
   }

   protected void visitJsSlotGroupChildren(JsSlotGroup jsSlotGroup) {
      for (JsSlotRef jsSlotRef : jsSlotGroup.getJsSlotRefs()) {
         visitJsSlotRef(jsSlotRef);
      }
   }

   @Override
   public void visitJsSlotRef(JsSlotRef jsSlotRef) {
      m_path.down(ENTITY_JS_SLOT_REF);

      assertRequired(ATTR_ID, jsSlotRef.getId());

      m_path.up(ENTITY_JS_SLOT_REF);
   }

   @Override
   public void visitPage(Page page) {
      m_path.down(ENTITY_PAGE);

      visitPageChildren(page);

      m_path.up(ENTITY_PAGE);
   }

   protected void visitPageChildren(Page page) {
      for (JsSlot jsSlot : page.getJsSlots()) {
         visitJsSlot(jsSlot);
      }

      for (CssSlot cssSlot : page.getCssSlots()) {
         visitCssSlot(cssSlot);
      }

      for (CommonJsSlotRef commonJsSlotRef : page.getCommonJsSlotRefs()) {
         visitCommonJsSlotRef(commonJsSlotRef);
      }

      for (CommonCssSlotRef commonCssSlotRef : page.getCommonCssSlotRefs()) {
         visitCommonCssSlotRef(commonCssSlotRef);
      }

      for (JsSlotGroup jsSlotGroup : page.getJsSlotGroups()) {
         visitJsSlotGroup(jsSlotGroup);
      }

      for (CssSlotGroup cssSlotGroup : page.getCssSlotGroups()) {
         visitCssSlotGroup(cssSlotGroup);
      }

      for (ImgDataUri imgDataUri : page.getImgDataUris()) {
         visitImgDataUri(imgDataUri);
      }
   }

   @Override
   public void visitProfile(Profile profile) {
      m_path.down(ENTITY_PROFILE);

      visitProfileChildren(profile);

      m_path.up(ENTITY_PROFILE);
   }

   protected void visitProfileChildren(Profile profile) {
      m_path.down(ENTITY_COMMON_JS_SLOTS);

      for (JsSlot jsSlot : profile.getCommonJsSlots()) {
         visitJsSlot(jsSlot);
      }

      m_path.up(ENTITY_COMMON_JS_SLOTS);

      m_path.down(ENTITY_COMMON_CSS_SLOTS);

      for (CssSlot cssSlot : profile.getCommonCssSlots()) {
         visitCssSlot(cssSlot);
      }

      m_path.up(ENTITY_COMMON_CSS_SLOTS);

      m_path.down(ENTITY_PAGES);

      for (Page page : profile.getPages()) {
         visitPage(page);
      }

      m_path.up(ENTITY_PAGES);
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
