package org.unidal.webres.resource.js;

import java.io.File;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.api.IJsRef;
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

public class WarJsResolver implements IResourceResolver<IJsRef, IJs>, IResourceRegisterable<WarJsResolver> {
   private IResourceUrlBuilder<IJs> m_urlBuilder;

   private File m_warRoot;

   private String m_jsBase;

   protected String getFallbackPath(String path) {
      int pos1 = path.lastIndexOf('/');
      int pos2 = path.lastIndexOf('_');

      if (pos2 > pos1) {
         return path.substring(0, pos2) + '.' + path.substring(pos2 + 1);
      } else {
         return null;
      }
   }

   protected File getJsFile(IResourceContext ctx, IResourceUrn urn, File base) {
      IResourcePermutation permutation = ctx.getPermutation();
      String path = urn.getPathInfo();
      File jsFile;

      if (permutation != null) {
         String external = permutation.toExternal();

         jsFile = new File(new File(base, external), path);

         if (!jsFile.exists()) { // fail back to no permutation
            jsFile = new File(base, path);
            ctx.setFallbackPermutation(true);
         }
      } else {
         jsFile = new File(base, path);
      }

      if (!jsFile.isFile()) {
         String pathInfo = getFallbackPath(path);

         if (pathInfo != null) {
            File file = new File(base, pathInfo);

            if (file.isFile()) {
               jsFile = file;
               urn.setPathInfo(pathInfo);
            }
         }
      } else {
         urn.setPathInfo(path);
      }

      if (!jsFile.isFile()) {
         throw new ResourceException(String.format("Js(%s) not found!", jsFile));
      }

      return jsFile;
   }

   @Override
   public INamespace getNamespace() {
      return JsNamespace.WAR;
   }

   @Override
   public WarJsResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Js.War;
   }

   @Override
   public Class<? super WarJsResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Js;
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
            m_jsBase = config.getContainer().getAttribute(String.class, ResourceConstant.Js.Base);
         } else {
            throw new ResourceException(String.format("Js(%s) not found, please make sure war(%s) configured!", urn,
                  warName));
         }
      } else {
         throw new RuntimeException(String.format("Invalid resource urn(%s) found!", urn));
      }
   }

   @Override
   public WarJs resolve(IJsRef ref, IResourceContext ctx) throws ResourceException {
      prepare(ref.getUrn());

      File base = m_jsBase != null ? new File(m_warRoot, m_jsBase) : m_warRoot;
      File jsFile = getJsFile(ctx, ref.getUrn(), base);
      WarJs js = new WarJs(ctx, ref, jsFile);

      js.validate();
      js.setUrlBuilder(m_urlBuilder);
      return js;
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Js.War)
   public void setUrlBuilder(IResourceUrlBuilder<? extends IJs> urlBuilder) {
      m_urlBuilder = (IResourceUrlBuilder<IJs>) urlBuilder;
   }
}