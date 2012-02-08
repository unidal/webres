package org.unidal.webres.server.js;

import org.unidal.webres.resource.annotation.RuntimeConfig;
import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.js.InlineJsResolver;
import org.unidal.webres.resource.runtime.ResourceRuntimeConfig;
import org.unidal.webres.resource.spi.IResourceContainer;
import org.unidal.webres.resource.spi.IResourceContext;

public class SimpleInlineJsResolver extends InlineJsResolver {
   private ResourceRuntimeConfig m_config;

   @Override
   public IJs resolve(IJsRef ref, IResourceContext ctx) throws ResourceException {
      IResourceContainer container = m_config.getRegistry().getContainer();
      String urn = ref.getUrn().toString();
      IJs js = container.getAttribute(IJs.class, urn);

      if (js == null) {
         js = super.resolve(ref, ctx);

         container.setAttribute(IJs.class, urn, js);
      }

      return js;
   }

   @RuntimeConfig
   public void setRuntimeConfig(ResourceRuntimeConfig config) {
      m_config = config;
   }
}