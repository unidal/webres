package org.unidal.webres.tag.img;

import org.unidal.webres.tag.resource.IResourceTagRenderType;

public enum ImageTagRenderType implements IResourceTagRenderType {
   HTML("html"),

   DATA_URI("dataUri");

   private String m_name;

   private ImageTagRenderType(String name) {
      m_name = name;
   }

   @Override
   public String getName() {
      return m_name;
   }
}