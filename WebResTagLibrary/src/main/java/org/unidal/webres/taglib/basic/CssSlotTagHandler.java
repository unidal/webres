package org.unidal.webres.taglib.basic;

import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.tag.css.CssSlotTag;
import org.unidal.webres.tag.css.CssSlotTagModel;
import org.unidal.webres.tag.meta.TagAttributeMeta;
import org.unidal.webres.tag.meta.TagMeta;

/**
 * <ul>Following use cases are supported:
 * <li>&lt;res:cssSlot id="head" /&gt;</li>
 * </ul>
 */
@TagMeta(name = ResourceTagLibConstants.RESOURCE_CSS_SLOT_TAG_NAME, info = "cssSlot Resource Tag", dynamicAttributes = true, parseBody = false)
public class CssSlotTagHandler extends BaseResourceTagHandler<ICssRef, CssSlotTagModel, CssSlotTag> {
   private static final long serialVersionUID = 1L;

   @Override
   protected CssSlotTag createTag() {
      return new CssSlotTag();
   }

   @Override
   @TagAttributeMeta(description = "The reference id for CssSlot.")
   public void setId(String id) {
      getModel().setId(id);
   }

   @TagAttributeMeta(description = "Customized tag render type")
   public void setRenderType(String type) {
      getModel().setRenderType(type);
   }

   @TagAttributeMeta(description = "Identify whether the link URL is secure or not.")
   public void setSecure(boolean secure) {
      getModel().setSecure(secure);
   }
}
