package org.unidal.webres.resource.profile.transform;

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

public abstract class EmptyVisitor implements IVisitor {
   @Override
   public void visitCommonCssSlotRef(CommonCssSlotRef commonCssSlotRef) {
   }

   @Override
   public void visitCommonJsSlotRef(CommonJsSlotRef commonJsSlotRef) {
   }

   @Override
   public void visitCss(Css css) {
   }

   @Override
   public void visitCssSlot(CssSlot cssSlot) {
   }

   @Override
   public void visitCssSlotGroup(CssSlotGroup cssSlotGroup) {
   }

   @Override
   public void visitCssSlotRef(CssSlotRef cssSlotRef) {
   }

   @Override
   public void visitImgDataUri(ImgDataUri imgDataUri) {
   }

   @Override
   public void visitJs(Js js) {
   }

   @Override
   public void visitJsSlot(JsSlot jsSlot) {
   }

   @Override
   public void visitJsSlotGroup(JsSlotGroup jsSlotGroup) {
   }

   @Override
   public void visitJsSlotRef(JsSlotRef jsSlotRef) {
   }

   @Override
   public void visitPage(Page page) {
   }

   @Override
   public void visitProfile(Profile profile) {
   }
}
