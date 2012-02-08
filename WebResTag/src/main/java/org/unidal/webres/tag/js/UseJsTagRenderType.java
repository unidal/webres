package org.unidal.webres.tag.js;

import org.unidal.webres.tag.resource.IResourceTagRenderType;

public enum UseJsTagRenderType implements IResourceTagRenderType {
   INLINE("inline"),

   EXTERNALIZED("externalized");

   private String m_name;

   private UseJsTagRenderType(String name) {
      m_name = name;
   }

   @Override
   public String getName() {
      return m_name;
   }
}