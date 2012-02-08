package org.unidal.webres.taglib.basic;

import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.tag.js.UseJsTag;
import org.unidal.webres.tag.js.UseJsTagModel;
import org.unidal.webres.tag.meta.TagAttributeMeta;
import org.unidal.webres.tag.meta.TagMeta;

/**
 * <ul>Following use cases are supported:
 * <li>&lt;res:useJs value="${res.js.local.ebaytime_js}" /&gt;</li>
 * </ul>
 */
@TagMeta(name = ResourceTagLibConstants.RESOURCE_JS_TAG_NAME, info = "useJs Resource Tag", dynamicAttributes = true, parseBody = false)
public class UseJsTagHandler extends BaseResourceTagHandler<IJsRef, UseJsTagModel, UseJsTag> {
   private static final long serialVersionUID = 1L;

   @Override
   protected UseJsTag createTag() {
      return new UseJsTag();
   }

   @Override
   @TagAttributeMeta(description = "The reference id for Js.")
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

   @TagAttributeMeta(description = "Target placement for this js resource to render")
   public void setTarget(String target) {
      getModel().setTarget(target);
   }

   @TagAttributeMeta(description = "Set the js value with EL or a js ref.")
   public void setValue(Object value) {
      getModel().setValue(value);
   }
}
