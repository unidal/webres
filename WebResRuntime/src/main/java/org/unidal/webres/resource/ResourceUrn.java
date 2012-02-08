package org.unidal.webres.resource;

import org.unidal.webres.resource.api.IResourceUrn;

public class ResourceUrn implements IResourceUrn {
   private String m_resourceType;

   private String m_namespace;

   private String m_resourceId;

   private String m_pathInfo;

   private String m_urn;

   public ResourceUrn(String resourceType, String namespace, String resourceId) {
      if (!resourceId.startsWith("/")) {
         throw new IllegalArgumentException(String.format("ResourceId(%s) must be starting with '/'.", resourceId));
      }

      m_resourceType = resourceType;
      m_namespace = namespace;
      m_resourceId = resourceId;
      m_pathInfo = resourceId;
   }

   public static IResourceUrn parse(String urn) {
      int pos1 = urn.indexOf('.');
      int pos2 = urn.indexOf(':', pos1 + 1);

      if (pos1 > 0 && pos2 > pos1) {
         String resourceType = urn.substring(0, pos1);
         String namespace = urn.substring(pos1 + 1, pos2);
         String resourceId = urn.substring(pos2 + 1);

         return new ResourceUrn(resourceType, namespace, resourceId);
      }

      throw new RuntimeException(String.format("Invalid resource urn(%s)!", urn));
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (!(obj instanceof ResourceUrn)) {
         return false;
      }
      ResourceUrn other = (ResourceUrn) obj;
      if (m_namespace == null) {
         if (other.m_namespace != null) {
            return false;
         }
      } else if (!m_namespace.equals(other.m_namespace)) {
         return false;
      }
      if (m_pathInfo == null) {
         if (other.m_pathInfo != null) {
            return false;
         }
      } else if (!m_pathInfo.equals(other.m_pathInfo)) {
         return false;
      }
      if (m_resourceId == null) {
         if (other.m_resourceId != null) {
            return false;
         }
      } else if (!m_resourceId.equals(other.m_resourceId)) {
         return false;
      }
      if (m_resourceType == null) {
         if (other.m_resourceType != null) {
            return false;
         }
      } else if (!m_resourceType.equals(other.m_resourceType)) {
         return false;
      }
      return true;
   }

   @Override
   public String getNamespace() {
      return m_namespace;
   }

   @Override
   public String getPathInfo() {
      return m_pathInfo;
   }

   @Override
   public String getResourceId() {
      return m_resourceId;
   }

   @Override
   public String getResourceTypeName() {
      return m_resourceType;
   }

   @Override
   public String getScheme() {
      return m_resourceType + '.' + m_namespace;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;

      result = prime * result + ((m_namespace == null) ? 0 : m_namespace.hashCode());
      result = prime * result + ((m_pathInfo == null) ? 0 : m_pathInfo.hashCode());
      result = prime * result + ((m_resourceId == null) ? 0 : m_resourceId.hashCode());
      result = prime * result + ((m_resourceType == null) ? 0 : m_resourceType.hashCode());

      return result;
   }

   @Override
   public void setPathInfo(String pathInfo) {
      m_pathInfo = pathInfo;
   }

   @Override
   public String toString() {
      if (m_urn == null) {
         m_urn = m_resourceType + '.' + m_namespace + ':' + m_resourceId;
      }

      return m_urn;
   }
}
