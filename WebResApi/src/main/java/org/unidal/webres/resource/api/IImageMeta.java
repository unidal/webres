package org.unidal.webres.resource.api;

public interface IImageMeta extends IResourceMeta {
   public int getHeight();

   public long getLastModified();

   public long getLength();

   public String getMimeType();

   public int getWidth();
}
