package org.unidal.webres.tag.common;

import org.unidal.webres.tag.resource.ResourceTagModelSupport;
import org.unidal.webres.tag.resource.ResourceTagRenderType;

public class SetTagModel extends ResourceTagModelSupport<ResourceTagRenderType> {
   private String m_id;
   
   private Object m_value;

   public SetTagModel() {
      super(ResourceTagRenderType.DEFAULT);
   }

   public String getId() {
      return m_id;
   }

   public Object getValue() {
      return m_value;
   }

   public void setId(String id) {
      m_id = id;
   }

   public void setValue(Object value) {
      m_value = value;
   }
}
