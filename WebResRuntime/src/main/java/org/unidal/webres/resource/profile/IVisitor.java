package org.unidal.webres.resource.profile;

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

public interface IVisitor {

   public void visitCommonCssSlotRef(CommonCssSlotRef commonCssSlotRef);

   public void visitCommonJsSlotRef(CommonJsSlotRef commonJsSlotRef);

   public void visitCss(Css css);

   public void visitCssSlot(CssSlot cssSlot);

   public void visitCssSlotGroup(CssSlotGroup cssSlotGroup);

   public void visitCssSlotRef(CssSlotRef cssSlotRef);

   public void visitImgDataUri(ImgDataUri imgDataUri);

   public void visitJs(Js js);

   public void visitJsSlot(JsSlot jsSlot);

   public void visitJsSlotGroup(JsSlotGroup jsSlotGroup);

   public void visitJsSlotRef(JsSlotRef jsSlotRef);

   public void visitPage(Page page);

   public void visitProfile(Profile profile);
}
