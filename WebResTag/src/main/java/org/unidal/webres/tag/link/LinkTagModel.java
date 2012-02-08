package org.unidal.webres.tag.link;

import org.unidal.webres.tag.resource.ResourceTagModelSupport;

public class LinkTagModel extends ResourceTagModelSupport<LinkTagRenderType> {
   private Boolean m_secure;

   private Object m_value;

   public LinkTagModel() {
      super(LinkTagRenderType.DEFAULT);
   }

   public Boolean getSecure() {
      return m_secure;
   }

   public Object getValue() {
      return m_value;
   }

   public void setSecure(Boolean secure) {
      m_secure = secure;
   }

   public void setValue(Object value) {
      m_value = value;
   }
}
