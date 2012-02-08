package org.unidal.webres.server.css;

import org.unidal.webres.resource.annotation.RuntimeConfig;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.css.InlineCssResolver;
import org.unidal.webres.resource.runtime.ResourceRuntimeConfig;
import org.unidal.webres.resource.spi.IResourceContainer;
import org.unidal.webres.resource.spi.IResourceContext;

public class SimpleInlineCssResolver extends InlineCssResolver {
   private ResourceRuntimeConfig m_config;

   @Override
   public ICss resolve(ICssRef ref, IResourceContext ctx) throws ResourceException {
      IResourceContainer container = m_config.getRegistry().getContainer();
      String urn = ref.getUrn().toString();
      ICss css = container.getAttribute(ICss.class, urn);

      if (css == null) {
         css = super.resolve(ref, ctx);

         container.setAttribute(ICss.class, urn, css);
      }

      return css;
   }

   @RuntimeConfig
   public void setRuntimeConfig(ResourceRuntimeConfig config) {
      m_config = config;
   }
}