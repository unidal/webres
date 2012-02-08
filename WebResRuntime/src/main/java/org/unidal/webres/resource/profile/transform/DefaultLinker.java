package org.unidal.webres.resource.profile.transform;

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

public class DefaultLinker implements ILinker {

   @Override
   public boolean onCommonCssSlot(Profile parent, CssSlot cssSlot) {
      parent.addCommonCssSlot(cssSlot);
      return true;
   }

   @Override
   public boolean onCommonCssSlotRef(Page parent, CommonCssSlotRef commonCssSlotRef) {
      parent.addCommonCssSlotRef(commonCssSlotRef);
      return true;
   }

   @Override
   public boolean onCommonJsSlot(Profile parent, JsSlot jsSlot) {
      parent.addCommonJsSlot(jsSlot);
      return true;
   }

   @Override
   public boolean onCommonJsSlotRef(Page parent, CommonJsSlotRef commonJsSlotRef) {
      parent.addCommonJsSlotRef(commonJsSlotRef);
      return true;
   }

   @Override
   public boolean onCss(CssSlot parent, Css css) {
      parent.addCss(css);
      return true;
   }

   @Override
   public boolean onCssSlot(Page parent, CssSlot cssSlot) {
      parent.addCssSlot(cssSlot);
      return true;
   }

   @Override
   public boolean onCssSlotGroup(Page parent, CssSlotGroup cssSlotGroup) {
      parent.addCssSlotGroup(cssSlotGroup);
      return true;
   }

   @Override
   public boolean onCssSlotRef(CssSlotGroup parent, CssSlotRef cssSlotRef) {
      parent.addCssSlotRef(cssSlotRef);
      return true;
   }

   @Override
   public boolean onImgDataUri(Page parent, ImgDataUri imgDataUri) {
      parent.addImgDataUri(imgDataUri);
      return true;
   }

   @Override
   public boolean onJs(JsSlot parent, Js js) {
      parent.addJs(js);
      return true;
   }

   @Override
   public boolean onJsSlot(Page parent, JsSlot jsSlot) {
      parent.addJsSlot(jsSlot);
      return true;
   }

   @Override
   public boolean onJsSlotGroup(Page parent, JsSlotGroup jsSlotGroup) {
      parent.addJsSlotGroup(jsSlotGroup);
      return true;
   }

   @Override
   public boolean onJsSlotRef(JsSlotGroup parent, JsSlotRef jsSlotRef) {
      parent.addJsSlotRef(jsSlotRef);
      return true;
   }

   @Override
   public boolean onPage(Profile parent, Page page) {
      parent.addPage(page);
      return true;
   }
}
