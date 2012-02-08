package org.unidal.webres.resource.runtime;

public class ResourceWarReference {
   private String m_logicalName;

   private String m_appId;

   public String getAppId() {
      return m_appId;
   }

   public String getLogicalName() {
      return m_logicalName;
   }

   public void setAppId(String appId) {
      m_appId = appId;
   }

   public void setLogicalName(String logicalName) {
      m_logicalName = logicalName;
   }
}
