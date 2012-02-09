package org.unidal.webres.taglib.basic;

import javax.servlet.jsp.JspException;

import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.expression.ZombieExpression;
import org.unidal.webres.tag.ITagEnv.Scope;
import org.unidal.webres.tag.js.JsSlotTag;
import org.unidal.webres.tag.js.JsSlotTagModel;
import org.unidal.webres.tag.meta.TagAttributeMeta;
import org.unidal.webres.tag.meta.TagMeta;

/**
 * <ul>Following use cases are supported:
 * <li>&lt;res:jsSlot id="head" /&gt;</li>
 * </ul>
 */
@TagMeta(name = ResourceTagLibConstants.RESOURCE_JS_SLOT_TAG_NAME, info = "jsSlot Resource Tag", dynamicAttributes = true, parseBody = false)
public class JsSlotTagHandler extends BaseResourceTagHandler<IJsRef, JsSlotTagModel, JsSlotTag> {
   private static final long serialVersionUID = 1L;

   @Override
   protected JsSlotTag createTag() {
      return new JsSlotTag();
   }

   @Override
   public int doStartTag() throws JspException {
      int hint = super.doStartTag();

      getTag().getEnv().setAttribute("this", new ZombieExpression("this"), Scope.REQUEST);
      return hint;
   }

   @Override
   @TagAttributeMeta(description = "The reference id for JsSlot.")
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
