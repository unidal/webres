package org.unidal.webres.resource.img;

import org.unidal.webres.resource.api.INamespace;

public enum ImageNamespace implements INamespace {
   LOCAL("local", "Resources at local folder under WebContent"),

   SHARED("shared", "Resource defined resource sharing library"),

   WAR("war", "Resource referenced to another war"),

   PICS("pics", "Image Resources at EbayResources.jar or EboxResources.jar");

   private String m_name;

   private String m_description;

   private boolean m_virtual;

   private ImageNamespace(String name, String description, boolean virtual) {
      m_name = name;
      m_description = description;
      m_virtual = virtual;
   }

   private ImageNamespace(String id, String description) {
      this(id, description, false);
   }

   public static ImageNamespace getByName(String name) {
      for (ImageNamespace namespace : values()) {
         if (namespace.getName().equals(name)) {
            return namespace;
         }
      }

      return null;
   }

   public String getDescription() {
      return m_description;
   }

   @Override
   public String getName() {
      return m_name;
   }

   public boolean isVirtual() {
      return m_virtual;
   }
}