package org.unidal.webres.resource.api;

public interface ITemplateMeta extends IResourceMeta {
   public String getLanguage();

   public long getLastModified();

   public long getLength();
}
