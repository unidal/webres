package org.unidal.webres.resource.api;

public interface ILink extends IResource<ILinkMeta, Void> {
   public String getSecureUrl();

   public String getUrl();
}
