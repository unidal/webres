package org.unidal.webres.tag.resource;

import org.unidal.webres.resource.api.IResource;

public interface IResourceTagRenderer<T extends ResourceTagSupport<?, ?, R>, R extends IResource<?, ?>> {
   public IResourceTagRenderType getType();

   public String render(T tag, R resource);
}
