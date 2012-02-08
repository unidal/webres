package org.unidal.webres.resource.template;

import org.unidal.webres.resource.api.INamespace;

public enum TemplateNamespace implements INamespace {
   LOCAL("local", "Resources at local folder under WebContent"),

   SHARED("shared", "Resource defined resource sharing library"),

   WAR("war", "Resource referenced to another war"),

   INLINE("inline", "Resource defined within template"),

   JAVA("java", "Resource provided with java class");

   private String m_name;

   private String m_description;

   private boolean m_virtual;

   private TemplateNamespace(String name, String description, boolean virtual) {
      m_name = name;
      m_description = description;
      m_virtual = virtual;
   }

   private TemplateNamespace(String id, String description) {
      this(id, description, false);
   }

   public static TemplateNamespace getByName(String name) {
      for (TemplateNamespace namespace : values()) {
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