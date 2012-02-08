package org.unidal.webres.resource.api;

public interface ICss extends IResource<ICssMeta, String> {
   public String getSecureUrl();

   public String getUrl();
}
