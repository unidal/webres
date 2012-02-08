package org.unidal.webres.tag.link;

import org.unidal.webres.resource.api.ILink;
import org.unidal.webres.resource.api.ILinkRef;
import org.unidal.webres.tag.resource.ResourceTagSupport;

public class LinkTag extends ResourceTagSupport<LinkTagModel, ILinkRef, ILink> {
   public LinkTag() {
      super(new LinkTagModel());
   }

   @Override
   public ILinkRef build() {
      Object value = getModel().getValue();

      // <res:link value='${res.link.pages.half.faq_html}'/>}
      return getResourceRef(ILinkRef.class, value);
   }
}
