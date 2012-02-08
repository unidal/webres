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

public interface IMaker<T> {

   public CommonCssSlotRef buildCommonCssSlotRef(T node);

   public CommonJsSlotRef buildCommonJsSlotRef(T node);

   public Css buildCss(T node);

   public CssSlot buildCssSlot(T node);

   public CssSlotGroup buildCssSlotGroup(T node);

   public CssSlotRef buildCssSlotRef(T node);

   public ImgDataUri buildImgDataUri(T node);

   public Js buildJs(T node);

   public JsSlot buildJsSlot(T node);

   public JsSlotGroup buildJsSlotGroup(T node);

   public JsSlotRef buildJsSlotRef(T node);

   public Page buildPage(T node);

   public Profile buildProfile(T node);
}
