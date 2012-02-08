package org.unidal.webres.server;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.INamespace;

public enum SimpleResourceNamespace implements INamespace {
   CMD("cmd", "Link to page command", //
         SystemResourceType.Link);

   private String m_name;

   private String m_description;

   private SystemResourceType[] m_resources;

   private boolean m_virtual;

   private SimpleResourceNamespace(String name, String description, boolean virtual, SystemResourceType... resources) {
      m_name = name;
      m_description = description;
      m_virtual = virtual;
      m_resources = resources;
   }

   private SimpleResourceNamespace(String id, String description, SystemResourceType... resources) {
      this(id, description, false, resources);
   }

   public static SimpleResourceNamespace getByName(String name) {
      for (SimpleResourceNamespace namespace : values()) {
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

   public SystemResourceType[] getResources() {
      return m_resources;
   }

   public boolean isVirtual() {
      return m_virtual;
   }
}