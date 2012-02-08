package org.unidal.webres.resource.api;

public interface IImage extends IResource<IImageMeta, byte[]> {
   public String getDataUri();

   public String getSecureUrl();

   public String getUrl();
}
