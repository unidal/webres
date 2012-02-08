package org.unidal.webres.resource.css;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.annotation.ContextPath;
import org.unidal.webres.resource.annotation.CssBase;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class LocalCssUrlBuilder implements IResourceUrlBuilder<ICss>, IResourceRegisterable<LocalCssUrlBuilder> {
   private String m_contextPath;

   private String m_cssBase;

   @Override
   public String build(IResourceContext ctx, ICss css) {
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
   public LocalCssUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Css.Local;
   }

   @Override
   public Class<? super LocalCssUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }

   @ContextPath
   public LocalCssUrlBuilder setContextPath(String contextPath) {
      m_contextPath = contextPath;
      return this;
   }

   @CssBase
   public LocalCssUrlBuilder setCssBase(String cssBase) {
      m_cssBase = cssBase;
      return this;
   }
}
