package org.unidal.webres.tag.link;

import org.unidal.webres.tag.resource.IResourceTagRenderType;

public enum LinkTagRenderType implements IResourceTagRenderType {
   DEFAULT("default");

   private String m_name;

   private LinkTagRenderType(String name) {
      m_name = name;
   }

   @Override
   public String getName() {
      return m_name;
   }
}