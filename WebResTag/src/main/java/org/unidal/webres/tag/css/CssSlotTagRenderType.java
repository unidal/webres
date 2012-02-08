package org.unidal.webres.tag.css;

import org.unidal.webres.tag.resource.IResourceTagRenderType;

public enum CssSlotTagRenderType implements IResourceTagRenderType {
   INLINE("inline"),

   EXTERNALIZED("externalized");

   private String m_name;

   private CssSlotTagRenderType(String name) {
      m_name = name;
   }

   @Override
   public String getName() {
      return m_name;
   }
}