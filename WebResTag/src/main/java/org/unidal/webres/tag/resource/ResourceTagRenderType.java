package org.unidal.webres.tag.resource;

public enum ResourceTagRenderType implements IResourceTagRenderType {
   DEFAULT;

   @Override
   public String getName() {
      return name();
   }
}