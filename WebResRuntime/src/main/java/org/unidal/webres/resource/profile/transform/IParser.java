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

public interface IParser<T> {
   public Profile parse(IMaker<T> maker, ILinker linker, T node);

   public void parseForCommonCssSlotRef(IMaker<T> maker, ILinker linker, CommonCssSlotRef parent, T node);

   public void parseForCommonJsSlotRef(IMaker<T> maker, ILinker linker, CommonJsSlotRef parent, T node);

   public void parseForCss(IMaker<T> maker, ILinker linker, Css parent, T node);

   public void parseForCssSlot(IMaker<T> maker, ILinker linker, CssSlot parent, T node);

   public void parseForCssSlotGroup(IMaker<T> maker, ILinker linker, CssSlotGroup parent, T node);

   public void parseForCssSlotRef(IMaker<T> maker, ILinker linker, CssSlotRef parent, T node);

   public void parseForImgDataUri(IMaker<T> maker, ILinker linker, ImgDataUri parent, T node);

   public void parseForJs(IMaker<T> maker, ILinker linker, Js parent, T node);

   public void parseForJsSlot(IMaker<T> maker, ILinker linker, JsSlot parent, T node);

   public void parseForJsSlotGroup(IMaker<T> maker, ILinker linker, JsSlotGroup parent, T node);

   public void parseForJsSlotRef(IMaker<T> maker, ILinker linker, JsSlotRef parent, T node);

   public void parseForPage(IMaker<T> maker, ILinker linker, Page parent, T node);
}
