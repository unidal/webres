package org.unidal.webres.resource.css;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.runtime.ResourceRuntime;
import org.unidal.webres.resource.runtime.ResourceRuntimeConfig;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class WarCssUrlBuilder implements IResourceUrlBuilder<ICss>, IResourceRegisterable<WarCssUrlBuilder> {
   private String m_contextPath;

   private String m_cssBase;

   @Override
   public String build(IResourceContext ctx, ICss css) {
      prepare(css.getMeta().getUrn());

      StringBuilder sb = new StringBuilder(128);

      if (m_contextPath != null) {
         sb.append(m_contextPath);
      }

      if (m_cssBase != null) {
         sb.append(m_cssBase);
      }

      if (ctx.getPermutation() != null && !ctx.isFallbackPermutation()) {
         sb.append('/').append(ctx.getPermutation().toExternal());
      }

      sb.append(css.getMeta().getUrn().getPathInfo());

      return sb.toString();
   }

   @Override
   public WarCssUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Css.War;
   }

   @Override
   public Class<? super WarCssUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }

   protected void prepare(IResourceUrn urn) {
      String path = urn.getResourceId();
      int pos = path.indexOf('/', 1);

      if (pos > 0) {
         String warName = path.substring(1, pos);
         ResourceRuntimeConfig config = ResourceRuntime.INSTANCE.findConfigByWarName(warName);

         if (config == null) {
            String contextPath = path.substring(0, pos);

            config = ResourceRuntime.INSTANCE.getConfig(contextPath);
         }

         if (config != null) {
            m_contextPath = config.getContextPath();
            m_cssBase = config.getContainer().getAttribute(String.class, ResourceConstant.Css.Base);
         } else {
            throw new ResourceException(String.format("Css(%s) not found, please make sure war(%s) configured!", urn,
                  warName));
         }
      } else {
         throw new RuntimeException(String.format("Invalid resource urn(%s) found!", urn));
      }
   }
}
