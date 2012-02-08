package org.unidal.webres.resource.css;

import java.io.File;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.resource.api.INamespace;
import org.unidal.webres.resource.api.IResourcePermutation;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.injection.ResourceAttribute;
import org.unidal.webres.resource.runtime.ResourceRuntime;
import org.unidal.webres.resource.runtime.ResourceRuntimeConfig;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceResolver;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class WarCssResolver implements IResourceResolver<ICssRef, ICss>, IResourceRegisterable<WarCssResolver> {
   private IResourceUrlBuilder<ICss> m_urlBuilder;

   private File m_warRoot;

   private String m_cssBase;

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
      IResourcePermutation permutation = ctx.getPermutation();
      String path = urn.getPathInfo();
      File cssFile;

      if (permutation != null) {
         String external = permutation.toExternal();

         cssFile = new File(new File(base, external), path);

         if (!cssFile.exists()) { // fail back to no permutation
            cssFile = new File(base, path);
            ctx.setFallbackPermutation(true);
         }
      } else {
         cssFile = new File(base, path);
      }

      if (!cssFile.isFile()) {
         String pathInfo = getFallbackPath(path);

         if (pathInfo != null) {
            File file = new File(base, pathInfo);

            if (file.isFile()) {
               cssFile = file;
               urn.setPathInfo(pathInfo);
            }
         }
      } else {
         urn.setPathInfo(path);
      }

      if (!cssFile.isFile()) {
         throw new ResourceException(String.format("Css(%s) not found!", cssFile));
      }

      return cssFile;
   }

   @Override
   public INamespace getNamespace() {
      return CssNamespace.WAR;
   }

   @Override
   public WarCssResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Css.War;
   }

   @Override
   public Class<? super WarCssResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Css;
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
            urn.setPathInfo(path.substring(pos));

            m_warRoot = config.getWarRoot();
            m_cssBase = config.getContainer().getAttribute(String.class, ResourceConstant.Css.Base);
         } else {
            throw new ResourceException(String.format("Css(%s) not found, please make sure war(%s) configured!", urn, warName));
         }
      } else {
         throw new RuntimeException(String.format("Invalid resource urn(%s) found!", urn));
      }
   }

   @Override
   public WarCss resolve(ICssRef ref, IResourceContext ctx) throws ResourceException {
      prepare(ref.getUrn());

      File base = m_cssBase != null ? new File(m_warRoot, m_cssBase) : m_warRoot;
      File cssFile = getCssFile(ctx, ref.getUrn(), base);
      WarCss css = new WarCss(ctx, ref, cssFile);

      css.validate();
      css.setUrlBuilder(m_urlBuilder);
      return css;
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Css.War)
   public void setUrlBuilder(IResourceUrlBuilder<? extends ICss> urlBuilder) {
      m_urlBuilder = (IResourceUrlBuilder<ICss>) urlBuilder;
   }
}