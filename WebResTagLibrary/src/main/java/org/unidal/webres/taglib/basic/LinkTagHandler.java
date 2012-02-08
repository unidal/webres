package org.unidal.webres.taglib.basic;

import org.unidal.webres.resource.api.ILinkRef;
import org.unidal.webres.tag.link.LinkTag;
import org.unidal.webres.tag.link.LinkTagModel;
import org.unidal.webres.tag.meta.TagAttributeMeta;
import org.unidal.webres.tag.meta.TagMeta;

/**
 * <ul>Following use cases are supported:
 * <li>&lt;res:link value="${res.link.pages.half.faq_html}" /&gt;</li>
 * </ul>
 */
@TagMeta(name = ResourceTagLibConstants.RESOURCE_LINK_TAG_NAME, info = "Link Resource Tag", dynamicAttributes = true, parseBody = false)
public class LinkTagHandler extends BaseResourceTagHandler<ILinkRef, LinkTagModel, LinkTag> {
   private static final long serialVersionUID = 1L;

   @Override
   protected LinkTag createTag() {
      return new LinkTag();
   }

   @TagAttributeMeta(description = "Identify whether the link URL is secure or not.")
   public void setSecure(boolean secure) {
      getModel().setSecure(secure);
   }

   @TagAttributeMeta(required = true, description = "The value for link, could be a expression or a link ref.")
   public void setValue(Object value) {
      getModel().setValue(value);
   }

   @TagAttributeMeta(description = "Customized tag render type")
   public void setRenderType(String type) {
      getModel().setRenderType(type);
   }
}
