package org.unidal.webres.resource.link;

import org.unidal.webres.resource.api.INamespace;

public enum LinkNamespace implements INamespace {
   PAGES("pages", "Link Resources at EbayResources.jar or EboxResources.jar");

   private String m_name;

   private String m_description;

   private boolean m_virtual;

   private LinkNamespace(String name, String description, boolean virtual) {
      m_name = name;
      m_description = description;
      m_virtual = virtual;
   }

   private LinkNamespace(String id, String description) {
      this(id, description, false);
   }

   public static LinkNamespace getByName(String name) {
      for (LinkNamespace namespace : values()) {
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