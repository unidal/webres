package org.unidal.webres.taglib.basic;

import org.unidal.webres.resource.api.IImageRef;
import org.unidal.webres.tag.img.ImageTag;
import org.unidal.webres.tag.img.ImageTagModel;
import org.unidal.webres.tag.meta.TagAttributeMeta;
import org.unidal.webres.tag.meta.TagMeta;

/**
 * <ul>Following use cases are supported:
 * <li>&lt;res:img value="${res.img.local.half.buttons.btn_Buy37x19_gif}" /&gt;</li>
 * </ul>
 */
@TagMeta(name = ResourceTagLibConstants.RESOURCE_IMG_TAG_NAME, info = "Image Resource Tag", dynamicAttributes = true, parseBody = false)
public class ImageTagHandler extends BaseResourceTagHandler<IImageRef, ImageTagModel, ImageTag> {
   private static final long serialVersionUID = 1L;

   @Override
   protected ImageTag createTag() {
      return new ImageTag();
   }

   @Override
   @TagAttributeMeta(description = "The reference id for img.")
   public void setId(String id) {
      getModel().setId(id);
   }

   @TagAttributeMeta(description = "Customized tag render type")
   public void setRenderType(String type) {
      getModel().setRenderType(type);
   }

   @TagAttributeMeta(description = "Identify whether the image URL is secure or not.")
   public void setSecure(boolean secure) {
      getModel().setSecure(secure);
   }

   @TagAttributeMeta(required = true, description = "The value for image, could be a expression or a image path.")
   public void setValue(Object value) {
      getModel().setValue(value);
   }
}
