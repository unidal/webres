package org.unidal.webres.resource.js;

import org.unidal.webres.resource.api.INamespace;

public enum JsNamespace implements INamespace {
   LOCAL("local", "Resources at local folder under WebContent"),

   SHARED("shared", "Resource defined resource sharing library"),

   WAR("war", "Resource referenced to another war"),

   INLINE("inline", "Resource defined within template", true),

   AGGREGATED("aggregated", "Resource aggregated from other resources", true);

   private String m_name;

   private String m_description;

   private boolean m_virtual;

   private JsNamespace(String name, String description, boolean virtual) {
      m_name = name;
      m_description = description;
      m_virtual = virtual;
   }

   private JsNamespace(String id, String description) {
      this(id, description, false);
   }

   public static JsNamespace getByName(String name) {
      for (JsNamespace namespace : values()) {
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