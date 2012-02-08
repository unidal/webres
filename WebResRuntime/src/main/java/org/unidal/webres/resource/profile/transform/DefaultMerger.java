package org.unidal.webres.resource.profile.transform;

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

public class DefaultMerger implements IVisitor {

   private Stack<Object> m_stack = new Stack<Object>();

   private Profile m_profile;

   public DefaultMerger(Profile profile) {
      m_profile = profile;
   }

   protected Profile getProfile() {
      return m_profile;
   }

   protected Stack<Object> getStack() {
      return m_stack;
   }

   @Override
   public void visitCommonCssSlotRef(CommonCssSlotRef commonCssSlotRef) {
      Object parent = m_stack.peek();
      CommonCssSlotRef old = null;

      if (parent instanceof Page) {
         Page page = (Page) parent;

         old = page.findCommonCssSlotRef(commonCssSlotRef.getId());

         if (old == null) {
            page.addCommonCssSlotRef(commonCssSlotRef);
         } else {
            old.mergeAttributes(commonCssSlotRef);
         }
      }

      visitCommonCssSlotRefChildren(old, commonCssSlotRef);
   }

   protected void visitCommonCssSlotRefChildren(CommonCssSlotRef old, CommonCssSlotRef commonCssSlotRef) {
   }

   @Override
   public void visitCommonJsSlotRef(CommonJsSlotRef commonJsSlotRef) {
      Object parent = m_stack.peek();
      CommonJsSlotRef old = null;

      if (parent instanceof Page) {
         Page page = (Page) parent;

         old = page.findCommonJsSlotRef(commonJsSlotRef.getId());

         if (old == null) {
            page.addCommonJsSlotRef(commonJsSlotRef);
         } else {
            old.mergeAttributes(commonJsSlotRef);
         }
      }

      visitCommonJsSlotRefChildren(old, commonJsSlotRef);
   }

   protected void visitCommonJsSlotRefChildren(CommonJsSlotRef old, CommonJsSlotRef commonJsSlotRef) {
   }

   @Override
   public void visitCss(Css css) {
      Object parent = m_stack.peek();
      Css old = null;

      if (parent instanceof CssSlot) {
         CssSlot cssSlot = (CssSlot) parent;

         old = cssSlot.findCss(css.getUrn());

         if (old == null) {
            cssSlot.addCss(css);
         } else {
            old.mergeAttributes(css);
         }
      }

      visitCssChildren(old, css);
   }

   protected void visitCssChildren(Css old, Css css) {
   }

   @Override
   public void visitCssSlot(CssSlot cssSlot) {
      Object parent = m_stack.peek();
      CssSlot old = null;

      if (parent instanceof Profile) {
         Profile profile = (Profile) parent;

         old = profile.findCommonCssSlot(cssSlot.getId());

         if (old == null) {
            profile.addCommonCssSlot(cssSlot);
         } else {
            old.mergeAttributes(cssSlot);
         }
      } else if (parent instanceof Page) {
         Page page = (Page) parent;

         old = page.findCssSlot(cssSlot.getId());

         if (old == null) {
            page.addCssSlot(cssSlot);
         } else {
            old.mergeAttributes(cssSlot);
         }
      }

      visitCssSlotChildren(old, cssSlot);
   }

   protected void visitCssSlotChildren(CssSlot old, CssSlot cssSlot) {
      if (old != null) {
         m_stack.push(old);

         for (Css css : cssSlot.getCssList()) {
            visitCss(css);
         }

         m_stack.pop();
      }
   }

   @Override
   public void visitCssSlotGroup(CssSlotGroup cssSlotGroup) {
      Object parent = m_stack.peek();
      CssSlotGroup old = null;

      if (parent instanceof Page) {
         Page page = (Page) parent;

         old = page.findCssSlotGroup(cssSlotGroup.getId());

         if (old == null) {
            page.addCssSlotGroup(cssSlotGroup);
         } else {
            old.mergeAttributes(cssSlotGroup);
         }
      }

      visitCssSlotGroupChildren(old, cssSlotGroup);
   }

   protected void visitCssSlotGroupChildren(CssSlotGroup old, CssSlotGroup cssSlotGroup) {
      if (old != null) {
         m_stack.push(old);

         for (CssSlotRef cssSlotRef : cssSlotGroup.getCssSlotRefs()) {
            visitCssSlotRef(cssSlotRef);
         }

         m_stack.pop();
      }
   }

   @Override
   public void visitCssSlotRef(CssSlotRef cssSlotRef) {
      Object parent = m_stack.peek();
      CssSlotRef old = null;

      if (parent instanceof CssSlotGroup) {
         CssSlotGroup cssSlotGroup = (CssSlotGroup) parent;

         old = cssSlotGroup.findCssSlotRef(cssSlotRef.getId());

         if (old == null) {
            cssSlotGroup.addCssSlotRef(cssSlotRef);
         } else {
            old.mergeAttributes(cssSlotRef);
         }
      }

      visitCssSlotRefChildren(old, cssSlotRef);
   }

   protected void visitCssSlotRefChildren(CssSlotRef old, CssSlotRef cssSlotRef) {
   }

   @Override
   public void visitImgDataUri(ImgDataUri imgDataUri) {
      Object parent = m_stack.peek();
      ImgDataUri old = null;

      if (parent instanceof Page) {
         Page page = (Page) parent;

         old = page.findImgDataUri(imgDataUri.getUrn());

         if (old == null) {
            page.addImgDataUri(imgDataUri);
         } else {
            old.mergeAttributes(imgDataUri);
         }
      }

      visitImgDataUriChildren(old, imgDataUri);
   }

   protected void visitImgDataUriChildren(ImgDataUri old, ImgDataUri imgDataUri) {
   }

   @Override
   public void visitJs(Js js) {
      Object parent = m_stack.peek();
      Js old = null;

      if (parent instanceof JsSlot) {
         JsSlot jsSlot = (JsSlot) parent;

         old = jsSlot.findJs(js.getUrn());

         if (old == null) {
            jsSlot.addJs(js);
         } else {
            old.mergeAttributes(js);
         }
      }

      visitJsChildren(old, js);
   }

   protected void visitJsChildren(Js old, Js js) {
   }

   @Override
   public void visitJsSlot(JsSlot jsSlot) {
      Object parent = m_stack.peek();
      JsSlot old = null;

      if (parent instanceof Profile) {
         Profile profile = (Profile) parent;

         old = profile.findCommonJsSlot(jsSlot.getId());

         if (old == null) {
            profile.addCommonJsSlot(jsSlot);
         } else {
            old.mergeAttributes(jsSlot);
         }
      } else if (parent instanceof Page) {
         Page page = (Page) parent;

         old = page.findJsSlot(jsSlot.getId());

         if (old == null) {
            page.addJsSlot(jsSlot);
         } else {
            old.mergeAttributes(jsSlot);
         }
      }

      visitJsSlotChildren(old, jsSlot);
   }

   protected void visitJsSlotChildren(JsSlot old, JsSlot jsSlot) {
      if (old != null) {
         m_stack.push(old);

         for (Js js : jsSlot.getJsList()) {
            visitJs(js);
         }

         m_stack.pop();
      }
   }

   @Override
   public void visitJsSlotGroup(JsSlotGroup jsSlotGroup) {
      Object parent = m_stack.peek();
      JsSlotGroup old = null;

      if (parent instanceof Page) {
         Page page = (Page) parent;

         old = page.findJsSlotGroup(jsSlotGroup.getId());

         if (old == null) {
            page.addJsSlotGroup(jsSlotGroup);
         } else {
            old.mergeAttributes(jsSlotGroup);
         }
      }

      visitJsSlotGroupChildren(old, jsSlotGroup);
   }

   protected void visitJsSlotGroupChildren(JsSlotGroup old, JsSlotGroup jsSlotGroup) {
      if (old != null) {
         m_stack.push(old);

         for (JsSlotRef jsSlotRef : jsSlotGroup.getJsSlotRefs()) {
            visitJsSlotRef(jsSlotRef);
         }

         m_stack.pop();
      }
   }

   @Override
   public void visitJsSlotRef(JsSlotRef jsSlotRef) {
      Object parent = m_stack.peek();
      JsSlotRef old = null;

      if (parent instanceof JsSlotGroup) {
         JsSlotGroup jsSlotGroup = (JsSlotGroup) parent;

         old = jsSlotGroup.findJsSlotRef(jsSlotRef.getId());

         if (old == null) {
            jsSlotGroup.addJsSlotRef(jsSlotRef);
         } else {
            old.mergeAttributes(jsSlotRef);
         }
      }

      visitJsSlotRefChildren(old, jsSlotRef);
   }

   protected void visitJsSlotRefChildren(JsSlotRef old, JsSlotRef jsSlotRef) {
   }

   @Override
   public void visitPage(Page page) {
      Object parent = m_stack.peek();
      Page old = null;

      if (parent instanceof Profile) {
         Profile profile = (Profile) parent;

         old = profile.findPage(page.getId());

         if (old == null) {
            profile.addPage(page);
         } else {
            old.mergeAttributes(page);
         }
      }

      visitPageChildren(old, page);
   }

   protected void visitPageChildren(Page old, Page page) {
      if (old != null) {
         m_stack.push(old);

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

         m_stack.pop();
      }
   }

   @Override
   public void visitProfile(Profile profile) {
      visitProfileChildren(m_profile, profile);
   }

   protected void visitProfileChildren(Profile old, Profile profile) {
      if (old != null) {
         m_stack.push(old);

         for (JsSlot jsSlot : profile.getCommonJsSlots()) {
            visitJsSlot(jsSlot);
         }

         for (CssSlot cssSlot : profile.getCommonCssSlots()) {
            visitCssSlot(cssSlot);
         }

         for (Page page : profile.getPages()) {
            visitPage(page);
         }

         m_stack.pop();
      }
   }
}
