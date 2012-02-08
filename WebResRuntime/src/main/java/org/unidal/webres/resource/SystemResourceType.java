package org.unidal.webres.resource;

import org.unidal.webres.resource.api.IResourceType;

public enum SystemResourceType implements IResourceType {
   Image("img"),

   Link("link"),

   Content("content"),

   Css("css"),

   Js("js"),
   
   Template("template");

   private String m_name;

   private SystemResourceType(String name) {
      m_name = name;
   }

   public static SystemResourceType getByName(String name) {
      for (SystemResourceType resourceType : values()) {
         if (resourceType.getName().equals(name)) {
            return resourceType;
         }
      }

      return null;
   }

   @Override
   public String getName() {
      return m_name;
   }
}