package org.unidal.webres.tag.resource;

import org.unidal.webres.tag.TagModelSupport;

public abstract class ResourceTagModelSupport<T extends IResourceTagRenderType> extends TagModelSupport {
   private T m_defaultRenderType;

   private String m_renderType;

   public ResourceTagModelSupport(T defaultRenderType) {
      m_defaultRenderType = defaultRenderType;

      if (m_defaultRenderType == null) {
         throw new IllegalArgumentException("Default render type can't be null!");
      }
   }

   public T getDefaultRenderType() {
      return m_defaultRenderType;
   }

   public String getRenderType() {
      return m_renderType;
   }

   public void setRenderType(IResourceTagRenderType renderType) {
      m_renderType = renderType.getName();
   }

   public void setRenderType(String renderType) {
      m_renderType = renderType;
   }
}
