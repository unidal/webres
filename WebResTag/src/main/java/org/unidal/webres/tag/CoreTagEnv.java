package org.unidal.webres.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.unidal.webres.resource.WarConstant;
import org.unidal.webres.resource.spi.IResourceRegistry;

public class CoreTagEnv extends TagEnvSupport {
   public static final String NAME = "Core";

   private String m_contextPath;

   private String m_requestUri;

   private Map<String, Object> m_map = new HashMap<String, Object>();

   public CoreTagEnv(IResourceRegistry registry, String requestUri) {
      setLookupManager(new TagLookupManager(registry.getContainer()));
      m_contextPath = registry.lookup(String.class, WarConstant.ContextPath);
      m_requestUri = requestUri;
   }

   public Object findAttribute(String name) {
      return getPageAttribute(name);
   }

   public String getContextPath() {
      return m_contextPath;
   }

   @Override
   public Object getPageAttribute(String name) {
      return m_map.get(name);
   }

   @Override
   public String getRequestUri() {
      return m_requestUri;
   }

   @Override
   public void setPageAttribute(String name, Object value) {
      m_map.put(name, value);
   }

   @Override
   public void flush() throws IOException {
   }
}