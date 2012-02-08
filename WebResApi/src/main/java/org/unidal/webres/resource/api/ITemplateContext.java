package org.unidal.webres.resource.api;

import java.util.Map;

public interface ITemplateContext {
   public Object getAttribute(String name);

   public Map<String, Object> getAttributes();

   public void setAttribute(String name, Object value);
}