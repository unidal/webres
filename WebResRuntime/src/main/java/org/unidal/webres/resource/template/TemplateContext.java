package org.unidal.webres.resource.template;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.unidal.webres.resource.api.ITemplateContext;

public class TemplateContext implements ITemplateContext {
   private Map<String, Object> m_map = new HashMap<String, Object>();

   public TemplateContext(Object... pairs) {
      int len = pairs.length;

      if (len % 2 != 0) {
         throw new IllegalArgumentException(String.format("Parameters(%s) should be paired!", Arrays.asList(pairs)));
      }

      for (int i = 0; i < len; i += 2) {
         String name = (String) pairs[i];
         Object value = pairs[i + 1];

         m_map.put(name, value);
      }
   }

   @Override
   public Object getAttribute(String name) {
      return m_map.get(name);
   }

   @Override
   public Map<String, Object> getAttributes() {
      return m_map;
   }

   @Override
   public void setAttribute(String name, Object value) {
      m_map.put(name, value);
   }
}