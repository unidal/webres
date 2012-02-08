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

public interface ILinker {

   public boolean onCommonCssSlot(Profile parent, CssSlot cssSlot);

   public boolean onCommonCssSlotRef(Page parent, CommonCssSlotRef commonCssSlotRef);

   public boolean onCommonJsSlot(Profile parent, JsSlot jsSlot);

   public boolean onCommonJsSlotRef(Page parent, CommonJsSlotRef commonJsSlotRef);

   public boolean onCss(CssSlot parent, Css css);

   public boolean onCssSlot(Page parent, CssSlot cssSlot);

   public boolean onCssSlotGroup(Page parent, CssSlotGroup cssSlotGroup);

   public boolean onCssSlotRef(CssSlotGroup parent, CssSlotRef cssSlotRef);

   public boolean onImgDataUri(Page parent, ImgDataUri imgDataUri);

   public boolean onJs(JsSlot parent, Js js);

   public boolean onJsSlot(Page parent, JsSlot jsSlot);

   public boolean onJsSlotGroup(Page parent, JsSlotGroup jsSlotGroup);

   public boolean onJsSlotRef(JsSlotGroup parent, JsSlotRef jsSlotRef);

   public boolean onPage(Profile parent, Page page);
}
