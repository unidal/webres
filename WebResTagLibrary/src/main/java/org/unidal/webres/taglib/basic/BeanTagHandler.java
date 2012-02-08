package org.unidal.webres.taglib.basic;

import org.unidal.webres.resource.dummy.IDummyResourceRef;
import org.unidal.webres.tag.common.BeanTag;
import org.unidal.webres.tag.common.BeanTagModel;
import org.unidal.webres.tag.meta.TagAttributeMeta;
import org.unidal.webres.tag.meta.TagMeta;

/**
 * <ul>Following use cases are supported:
 * <li>&lt;res:bean id="res" /&gt;</li>
 * </ul>
 */
@TagMeta(name = ResourceTagLibConstants.RESOURCE_BEAN_TAG_NAME, info = "Resource Bean Tag to define resource bean variable as bootstrap", dynamicAttributes = false)
public class BeanTagHandler extends BaseResourceTagHandler<IDummyResourceRef, BeanTagModel, BeanTag> {
   private static final long serialVersionUID = 1L;

   @Override
   protected BeanTag createTag() {
      return new BeanTag();
   }

   @TagAttributeMeta(required = true, description = "A valid id for Bean.")
   public void setId(String id) {
      getModel().setId(id);
   }
}
