package org.unidal.webres.tag.common;

import org.unidal.webres.tag.resource.ResourceTagModelSupport;
import org.unidal.webres.tag.resource.ResourceTagRenderType;

public class BeanTagModel extends ResourceTagModelSupport<ResourceTagRenderType> {
   private String m_id = "res";

   public BeanTagModel() {
      super(ResourceTagRenderType.DEFAULT);
   }

   public String getId() {
      return m_id;
   }

   public void setId(String id) {
      m_id = id;
   }
}
