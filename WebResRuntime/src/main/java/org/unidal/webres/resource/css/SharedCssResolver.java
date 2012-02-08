package org.unidal.webres.resource.css;

import java.net.URL;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.SystemResourceType;
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

public class SharedCssResolver implements IResourceResolver<ICssRef, ICss>, IResourceRegisterable<SharedCssResolver> {
   private static final String SHARED_RESOURCE_BASE = "META-INF/resources";

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

   protected URL getCssURL(IResourceContext ctx, IResourceUrn urn) {
      URL cssUrl = ResourceResolvings.fromJar().resolve(ctx, urn, SHARED_RESOURCE_BASE);

      if (cssUrl == null) {
         throw new ResourceException(String.format("Css(%s) not found in any jars or classpath!", SHARED_RESOURCE_BASE + cssUrl));
      }

      return cssUrl;
   }

   @Override
   public INamespace getNamespace() {
      return CssNamespace.SHARED;
   }

   @Override
   public SharedCssResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Css.Shared;
   }

   @Override
   public Class<? super SharedCssResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Css;
   }

   @Override
   public SharedCss resolve(ICssRef ref, IResourceContext ctx) throws ResourceException {
      URL cssUrl = getCssURL(ctx, ref.getUrn());
      SharedCss css = new SharedCss(ctx, ref, cssUrl);

      css.validate();

      css.setUrlBuilder(m_urlBuilder);
      return css;
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Css.Shared)
   public void setUrlBuilder(IResourceUrlBuilder<? extends ICss> urlBuilder) {
      m_urlBuilder = (IResourceUrlBuilder<ICss>) urlBuilder;
   }
}