package org.unidal.webres.tag.core;

import java.util.Map;

import org.unidal.webres.dom.ITagNode;

public interface ITagModel {
   public String getBodyContent();

   public Map<String, Object> getDynamicAttributes();

   public ITagNode getDynamicElements();

   public void setBodyContent(String bodyContent);

   public void setDynamicAttribute(String name, Object value);

   public void setDynamicElements(ITagNode dynamicElements);
}
