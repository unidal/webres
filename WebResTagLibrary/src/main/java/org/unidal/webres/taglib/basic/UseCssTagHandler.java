package org.unidal.webres.taglib.basic;

import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.tag.css.UseCssTag;
import org.unidal.webres.tag.css.UseCssTagModel;
import org.unidal.webres.tag.meta.TagAttributeMeta;
import org.unidal.webres.tag.meta.TagMeta;

/**
 * <ul>Following use cases are supported:
 * <li>&lt;res:useCss value="${res.css.local.ebaytime_css}" /&gt;</li>
 * </ul>
 */
@TagMeta(name = ResourceTagLibConstants.RESOURCE_CSS_TAG_NAME, info = "useCss Resource Tag", dynamicAttributes = true, parseBody = false)
public class UseCssTagHandler extends BaseResourceTagHandler<ICssRef, UseCssTagModel, UseCssTag> {
   private static final long serialVersionUID = 1L;

   @Override
   protected UseCssTag createTag() {
      return new UseCssTag();
   }

   @Override
   @TagAttributeMeta(description = "The reference id for Css.")
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

   @TagAttributeMeta(description = "Target placement for this css resource to render")
   public void setTarget(String target) {
      getModel().setTarget(target);
   }

   @TagAttributeMeta(description = "Set the css value with EL or a css ref.")
   public void setValue(Object value) {
      getModel().setValue(value);
   }
}
