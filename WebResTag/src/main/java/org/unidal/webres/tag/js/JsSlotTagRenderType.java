package org.unidal.webres.tag.js;

import org.unidal.webres.tag.resource.IResourceTagRenderType;

public enum JsSlotTagRenderType implements IResourceTagRenderType {
   INLINE("inline"),

   EXTERNALIZED("externalized");

   private String m_name;

   private JsSlotTagRenderType(String name) {
      m_name = name;
   }

   @Override
   public String getName() {
      return m_name;
   }
}