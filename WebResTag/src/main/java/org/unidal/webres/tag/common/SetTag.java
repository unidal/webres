package org.unidal.webres.tag.common;

import org.unidal.webres.resource.dummy.IDummyResource;
import org.unidal.webres.resource.dummy.IDummyResourceRef;
import org.unidal.webres.tag.ITagEnv;
import org.unidal.webres.tag.ITagEnv.Scope;
import org.unidal.webres.tag.resource.ResourceTagSupport;

/**
 * <ul>Following use cases are supported:
 * <li>&lt;res:set id="img" value="${res.img.local}" /&gt;</li>
 * </ul>
 */
public class SetTag extends ResourceTagSupport<SetTagModel, IDummyResourceRef, IDummyResource> {
   public SetTag() {
      super(new SetTagModel());
   }

   @Override
   public IDummyResourceRef build() {
      ITagEnv env = getEnv();
      String id = getModel().getId();
      Object value = getModel().getValue();

      env.setAttribute(id, value, Scope.REQUEST);
      return null;
   }
}
