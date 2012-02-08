package org.unidal.webres.tag.common;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.tag.resource.ResourceTagModelSupport;
import org.unidal.webres.tag.resource.ResourceTagRenderType;

public class TokenTagModel extends ResourceTagModelSupport<ResourceTagRenderType> {
   private IResourceType m_type;

   public TokenTagModel() {
      super(ResourceTagRenderType.DEFAULT);
   }

   public IResourceType getType() {
      return m_type;
   }

   public void setType(String type) {
      m_type = SystemResourceType.getByName(type);

      if (m_type != SystemResourceType.Js && m_type != SystemResourceType.Css) {
         throw new RuntimeException("Current only resource type(js, css) is supported in res:token tag!");
      }
   }
}
