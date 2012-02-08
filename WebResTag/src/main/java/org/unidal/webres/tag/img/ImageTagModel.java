package org.unidal.webres.tag.img;

import org.unidal.webres.tag.resource.ResourceTagModelSupport;

public class ImageTagModel extends ResourceTagModelSupport<ImageTagRenderType> {
   private String m_id;

   private Boolean m_secure;

   private Object m_value;

   public ImageTagModel() {
      super(ImageTagRenderType.HTML);
   }

   public String getId() {
      return m_id;
   }

   public Boolean getSecure() {
      return m_secure;
   }

   public Object getValue() {
      return m_value;
   }

   public void setId(String id) {
      m_id = id;
   }

   public void setSecure(Boolean secure) {
      m_secure = secure;
   }

   public void setValue(Object value) {
      m_value = value;
   }
}
