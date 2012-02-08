package org.unidal.webres.resource.img;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.annotation.ContextPath;
import org.unidal.webres.resource.annotation.ImageBase;
import org.unidal.webres.resource.api.IImage;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class LocalImageUrlBuilder implements IResourceUrlBuilder<IImage>, IResourceRegisterable<LocalImageUrlBuilder> {
   private String m_contextPath;

   private String m_imageBase;

   @Override
   public String build(IResourceContext ctx, IImage image) {
      StringBuilder sb = new StringBuilder(128);

      if (m_contextPath != null) {
         sb.append(m_contextPath);
      }

      if (m_imageBase != null) {
         sb.append(m_imageBase);
      }

      if (ctx.getPermutation() != null && !ctx.isFallbackPermutation()) {
         sb.append('/').append(ctx.getPermutation().toExternal());
      }

      sb.append(image.getMeta().getUrn().getPathInfo());

      return sb.toString();
   }

   @Override
   public LocalImageUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Image.Local;
   }

   @Override
   public Class<? super LocalImageUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }

   @ContextPath
   public LocalImageUrlBuilder setContextPath(String contextPath) {
      m_contextPath = contextPath;
      return this;
   }

   @ImageBase
   public LocalImageUrlBuilder setImageBase(String imageBase) {
      m_imageBase = imageBase;
      return this;
   }
}
