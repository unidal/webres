package org.unidal.webres.taglib.basic;

import org.unidal.webres.resource.dummy.IDummyResourceRef;
import org.unidal.webres.tag.common.SetTag;
import org.unidal.webres.tag.common.SetTagModel;
import org.unidal.webres.tag.meta.TagAttributeMeta;
import org.unidal.webres.tag.meta.TagMeta;

/**
 * <ul>Following use cases are supported:
 * <li>&lt;res:set id="img" value="${res.img.local}" /&gt;</li>
 * </ul>
 */
@TagMeta(name = ResourceTagLibConstants.RESOURCE_SET_TAG_NAME, info = "Set tag to define a page attribute with id as name", dynamicAttributes = false)
public class SetTagHandler extends BaseResourceTagHandler<IDummyResourceRef, SetTagModel, SetTag> {
   private static final long serialVersionUID = 1L;

   @Override
   protected SetTag createTag() {
      return new SetTag();
   }

   @TagAttributeMeta(required = true, description = "The name.")
   public void setId(String id) {
      getModel().setId(id);
   }

   @TagAttributeMeta(required = true, description = "The value")
   public void setValue(Object value) {
      getModel().setValue(value);
   }
}
