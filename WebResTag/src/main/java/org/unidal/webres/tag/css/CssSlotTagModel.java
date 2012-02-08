package org.unidal.webres.tag.css;

import org.unidal.webres.tag.css.CssSlotTagRenderType;
import org.unidal.webres.tag.resource.ResourceTagModelSupport;

public class CssSlotTagModel extends ResourceTagModelSupport<CssSlotTagRenderType> {
   private String m_id;

   private Boolean m_secure;

   public CssSlotTagModel() {
      super(CssSlotTagRenderType.EXTERNALIZED);
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
