package org.unidal.webres.resource.js;

import java.io.File;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.annotation.JsBase;
import org.unidal.webres.resource.annotation.WarRoot;
import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.api.IJsRef;
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

public class LocalJsResolver implements IResourceResolver<IJsRef, IJs>, IResourceRegisterable<LocalJsResolver> {
   private File m_warRoot;

   private String m_jsBase;

   private IResourceUrlBuilder<IJs> m_urlBuilder;

   protected File getJsFile(IResourceContext ctx, IResourceUrn urn, File base) {
      File jsFile = ResourceResolvings.fromDir().resolve(ctx, urn, base);

      if (!jsFile.isFile()) {
         throw new ResourceException(String.format("Js(%s) not found!", jsFile));
      }

      return jsFile;
   }

   @Override
   public INamespace getNamespace() {
      return JsNamespace.LOCAL;
   }

   @Override
   public LocalJsResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Js.Local;
   }

   @Override
   public Class<? super LocalJsResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Js;
   }

   @Override
   public LocalJs resolve(IJsRef ref, IResourceContext ctx) throws ResourceException {
      File base = m_jsBase != null ? new File(m_warRoot, m_jsBase) : m_warRoot;
      File jsFile = getJsFile(ctx, ref.getUrn(), base);
      LocalJs js = new LocalJs(ctx, ref, jsFile);

      js.validate();
      js.setUrlBuilder(m_urlBuilder);
      return js;
   }

   @JsBase
   public void setJsBase(String jsBase) {
      m_jsBase = jsBase;
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Js.Local)
   public void setUrlBuilder(IResourceUrlBuilder<? extends IJs> urlBuilder) {
      m_urlBuilder = (IResourceUrlBuilder<IJs>) urlBuilder;
   }

   @WarRoot
   public void setWarRoot(File warRoot) {
      m_warRoot = warRoot;
   }
}