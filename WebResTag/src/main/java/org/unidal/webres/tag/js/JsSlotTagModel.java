package org.unidal.webres.tag.js;

import org.unidal.webres.tag.resource.ResourceTagModelSupport;

public class JsSlotTagModel extends ResourceTagModelSupport<JsSlotTagRenderType> {
   private String m_id;

   private Boolean m_secure;

   public JsSlotTagModel() {
      super(JsSlotTagRenderType.EXTERNALIZED);
   }

   public String getId() {
      return m_id;
   }

   public Boolean getSecure() {
      return m_secure;
   }

   public void setId(String id) {
      m_id = id;
   }

   public void setSecure(Boolean secure) {
      m_secure = secure;
   }
}
