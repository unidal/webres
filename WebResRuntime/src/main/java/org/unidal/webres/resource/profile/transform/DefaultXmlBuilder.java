package org.unidal.webres.resource.profile.transform;

import static org.unidal.webres.resource.profile.Constants.ATTR_AFTER_SLOT;
import static org.unidal.webres.resource.profile.Constants.ATTR_BEFORE_SLOT;
import static org.unidal.webres.resource.profile.Constants.ATTR_ID;
import static org.unidal.webres.resource.profile.Constants.ATTR_MAIN_SLOT;
import static org.unidal.webres.resource.profile.Constants.ATTR_OVERRIDE;
import static org.unidal.webres.resource.profile.Constants.ATTR_URN;
import static org.unidal.webres.resource.profile.Constants.ENTITY_COMMON_CSS_SLOTS;
import static org.unidal.webres.resource.profile.Constants.ENTITY_COMMON_CSS_SLOT_REF;
import static org.unidal.webres.resource.profile.Constants.ENTITY_COMMON_JS_SLOTS;
import static org.unidal.webres.resource.profile.Constants.ENTITY_COMMON_JS_SLOT_REF;
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
   public void visitCommonCssSlotRef(CommonCssSlotRef commonCssSlotRef) {
      startTag(ENTITY_COMMON_CSS_SLOT_REF, null, true, ATTR_ID, commonCssSlotRef.getId(), ATTR_BEFORE_SLOT, commonCssSlotRef.getBeforeSlot(), ATTR_AFTER_SLOT, commonCssSlotRef.getAfterSlot());
   }

   @Override
   public void visitCommonJsSlotRef(CommonJsSlotRef commonJsSlotRef) {
      startTag(ENTITY_COMMON_JS_SLOT_REF, null, true, ATTR_ID, commonJsSlotRef.getId(), ATTR_BEFORE_SLOT, commonJsSlotRef.getBeforeSlot(), ATTR_AFTER_SLOT, commonJsSlotRef.getAfterSlot());
   }

   @Override
   public void visitCss(Css css) {
      startTag(ENTITY_CSS, null, css.getContent(), true, ATTR_URN, css.getUrn());
   }

   @Override
   public void visitCssSlot(CssSlot cssSlot) {
      startTag(ENTITY_CSS_SLOT, null, ATTR_ID, cssSlot.getId(), ATTR_OVERRIDE, cssSlot.getOverride());

      if (!cssSlot.getCssList().isEmpty()) {
         for (Css css : cssSlot.getCssList()) {
            visitCss(css);
         }
      }

      endTag(ENTITY_CSS_SLOT);
   }

   @Override
   public void visitCssSlotGroup(CssSlotGroup cssSlotGroup) {
      startTag(ENTITY_CSS_SLOT_GROUP, null, ATTR_ID, cssSlotGroup.getId(), ATTR_MAIN_SLOT, cssSlotGroup.getMainSlot());

      if (!cssSlotGroup.getCssSlotRefs().isEmpty()) {
         for (CssSlotRef cssSlotRef : cssSlotGroup.getCssSlotRefs()) {
            visitCssSlotRef(cssSlotRef);
         }
      }

      endTag(ENTITY_CSS_SLOT_GROUP);
   }

   @Override
   public void visitCssSlotRef(CssSlotRef cssSlotRef) {
      startTag(ENTITY_CSS_SLOT_REF, null, true, ATTR_ID, cssSlotRef.getId());
   }

   @Override
   public void visitImgDataUri(ImgDataUri imgDataUri) {
      startTag(ENTITY_IMG_DATA_URI, null, true, ATTR_URN, imgDataUri.getUrn());
   }

   @Override
   public void visitJs(Js js) {
      startTag(ENTITY_JS, null, js.getContent(), true, ATTR_URN, js.getUrn());
   }

   @Override
   public void visitJsSlot(JsSlot jsSlot) {
      startTag(ENTITY_JS_SLOT, null, ATTR_ID, jsSlot.getId(), ATTR_OVERRIDE, jsSlot.getOverride());

      if (!jsSlot.getJsList().isEmpty()) {
         for (Js js : jsSlot.getJsList()) {
            visitJs(js);
         }
      }

      endTag(ENTITY_JS_SLOT);
   }

   @Override
   public void visitJsSlotGroup(JsSlotGroup jsSlotGroup) {
      startTag(ENTITY_JS_SLOT_GROUP, null, ATTR_ID, jsSlotGroup.getId(), ATTR_MAIN_SLOT, jsSlotGroup.getMainSlot());

      if (!jsSlotGroup.getJsSlotRefs().isEmpty()) {
         for (JsSlotRef jsSlotRef : jsSlotGroup.getJsSlotRefs()) {
            visitJsSlotRef(jsSlotRef);
         }
      }

      endTag(ENTITY_JS_SLOT_GROUP);
   }

   @Override
   public void visitJsSlotRef(JsSlotRef jsSlotRef) {
      startTag(ENTITY_JS_SLOT_REF, null, true, ATTR_ID, jsSlotRef.getId());
   }

   @Override
   public void visitPage(Page page) {
      startTag(ENTITY_PAGE, null, ATTR_ID, page.getId());

      if (!page.getJsSlots().isEmpty()) {
         for (JsSlot jsSlot : page.getJsSlots()) {
            visitJsSlot(jsSlot);
         }
      }

      if (!page.getCssSlots().isEmpty()) {
         for (CssSlot cssSlot : page.getCssSlots()) {
            visitCssSlot(cssSlot);
         }
      }

      if (!page.getCommonJsSlotRefs().isEmpty()) {
         for (CommonJsSlotRef commonJsSlotRef : page.getCommonJsSlotRefs()) {
            visitCommonJsSlotRef(commonJsSlotRef);
         }
      }

      if (!page.getCommonCssSlotRefs().isEmpty()) {
         for (CommonCssSlotRef commonCssSlotRef : page.getCommonCssSlotRefs()) {
            visitCommonCssSlotRef(commonCssSlotRef);
         }
      }

      if (!page.getJsSlotGroups().isEmpty()) {
         for (JsSlotGroup jsSlotGroup : page.getJsSlotGroups()) {
            visitJsSlotGroup(jsSlotGroup);
         }
      }

      if (!page.getCssSlotGroups().isEmpty()) {
         for (CssSlotGroup cssSlotGroup : page.getCssSlotGroups()) {
            visitCssSlotGroup(cssSlotGroup);
         }
      }

      if (!page.getImgDataUris().isEmpty()) {
         for (ImgDataUri imgDataUri : page.getImgDataUris()) {
            visitImgDataUri(imgDataUri);
         }
      }

      endTag(ENTITY_PAGE);
   }

   @Override
   public void visitProfile(Profile profile) {
      startTag(ENTITY_PROFILE, null);

      if (!profile.getCommonJsSlots().isEmpty()) {
         startTag(ENTITY_COMMON_JS_SLOTS);

         for (JsSlot commonJsSlot : profile.getCommonJsSlots()) {
            visitJsSlot(commonJsSlot);
         }

         endTag(ENTITY_COMMON_JS_SLOTS);
      }

      if (!profile.getCommonCssSlots().isEmpty()) {
         startTag(ENTITY_COMMON_CSS_SLOTS);

         for (CssSlot commonCssSlot : profile.getCommonCssSlots()) {
            visitCssSlot(commonCssSlot);
         }

         endTag(ENTITY_COMMON_CSS_SLOTS);
      }

      if (!profile.getPages().isEmpty()) {
         startTag(ENTITY_PAGES);

         for (Page page : profile.getPages()) {
            visitPage(page);
         }

         endTag(ENTITY_PAGES);
      }

      endTag(ENTITY_PROFILE);
   }
}
