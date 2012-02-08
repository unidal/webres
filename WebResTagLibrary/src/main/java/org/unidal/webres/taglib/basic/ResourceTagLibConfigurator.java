package org.unidal.webres.taglib.basic;

import org.unidal.webres.resource.spi.IResourceConfigurator;
import org.unidal.webres.resource.spi.IResourceRegistry;
import org.unidal.webres.taglib.JspTagEnvFactory;

public class ResourceTagLibConfigurator implements IResourceConfigurator {
   @Override
   public void configure(IResourceRegistry registry) {
      registry.register(new JspTagEnvFactory());
   }
}
