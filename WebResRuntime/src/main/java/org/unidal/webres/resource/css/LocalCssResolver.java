package org.unidal.webres.resource.css;

import java.io.File;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.annotation.CssBase;
import org.unidal.webres.resource.annotation.WarRoot;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.resource.api.INamespace;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.helper.ResourceResolvings;
import org.unidal.webres.resource.injection.ResourceAttribute;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceResolver;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class LocalCssResolver implements IResourceResolver<ICssRef, ICss>, IResourceRegisterable<LocalCssResolver> {
   private File m_warRoot;

   private String m_cssBase;

   private IResourceUrlBuilder<ICss> m_urlBuilder;

   protected String getFallbackPath(String path) {
      int pos1 = path.lastIndexOf('/');
      int pos2 = path.lastIndexOf('_');

      if (pos2 > pos1) {
         return path.substring(0, pos2) + '.' + path.substring(pos2 + 1);
      } else {
         return null;
      }
   }

   protected File getCssFile(IResourceContext ctx, IResourceUrn urn, File base) {
      File cssFile = ResourceResolvings.fromDir().resolve(ctx, urn, base);

      if (!cssFile.isFile()) {
         throw new ResourceException(String.format("Css(%s) not found!", cssFile));
      }

      return cssFile;
   }

   @Override
   public INamespace getNamespace() {
      return CssNamespace.LOCAL;
   }

   @Override
   public LocalCssResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Css.Local;
   }

   @Override
   public Class<? super LocalCssResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Css;
   }

   @Override
   public LocalCss resolve(ICssRef ref, IResourceContext ctx) throws ResourceException {
      File base = m_cssBase != null ? new File(m_warRoot, m_cssBase) : m_warRoot;
      File cssFile = getCssFile(ctx, ref.getUrn(), base);
      LocalCss css = new LocalCss(ctx, ref, cssFile);

      css.validate();
      css.setUrlBuilder(m_urlBuilder);
      return css;
   }

   @CssBase
   public void setCssBase(String cssBase) {
      m_cssBase = cssBase;
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Css.Local)
   public void setUrlBuilder(IResourceUrlBuilder<? extends ICss> urlBuilder) {
      m_urlBuilder = (IResourceUrlBuilder<ICss>) urlBuilder;
   }

   @WarRoot
   public void setWarRoot(File warRoot) {
      m_warRoot = warRoot;
   }
}