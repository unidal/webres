package org.unidal.webres.resource.js;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.annotation.ContextPath;
import org.unidal.webres.resource.annotation.JsBase;
import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class LocalJsUrlBuilder implements IResourceUrlBuilder<IJs>, IResourceRegisterable<LocalJsUrlBuilder> {
   private String m_contextPath;

   private String m_jsBase;

   @Override
   public String build(IResourceContext ctx, IJs js) {
      StringBuilder sb = new StringBuilder(128);

      if (m_contextPath != null) {
         sb.append(m_contextPath);
      }

      if (m_jsBase != null) {
         sb.append(m_jsBase);
      }

      if (ctx.getPermutation() != null && !ctx.isFallbackPermutation()) {
         sb.append('/').append(ctx.getPermutation().toExternal());
      }

      sb.append(js.getMeta().getUrn().getPathInfo());

      return sb.toString();
   }

   @Override
   public LocalJsUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Js.Local;
   }

   @Override
   public Class<? super LocalJsUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }

   @ContextPath
   public LocalJsUrlBuilder setContextPath(String contextPath) {
      m_contextPath = contextPath;
      return this;
   }

   @JsBase
   public LocalJsUrlBuilder setJsBase(String jsBase) {
      m_jsBase = jsBase;
      return this;
   }
}
