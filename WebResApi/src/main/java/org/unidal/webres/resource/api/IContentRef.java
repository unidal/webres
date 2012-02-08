package org.unidal.webres.resource.api;

import org.unidal.webres.resource.spi.IResourceContext;

public interface IContentRef extends IResourceRef<IContent<?>> {
   
   public Object[] getArguments();

   public String getBundleName();

   public String getMapping();

   public Object getModel();
   
   /**
    * Resolve the default content
    */
   public Object getContent(IResourceContext ctx);
}
