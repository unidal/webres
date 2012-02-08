package org.unidal.webres.resource.api;

public interface IResource<M extends IResourceMeta, C> {
   public C getContent();

   public M getMeta();

   public void validate() throws ResourceException;
}