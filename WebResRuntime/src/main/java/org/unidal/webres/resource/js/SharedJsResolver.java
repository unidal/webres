package org.unidal.webres.resource.js;

import java.net.URL;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.SystemResourceType;
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

public class SharedJsResolver implements IResourceResolver<IJsRef, IJs>, IResourceRegisterable<SharedJsResolver> {
   private static final String SHARED_RESOURCE_BASE = "META-INF/resources";

   private IResourceUrlBuilder<IJs> m_urlBuilder;

   protected URL getJsURL(IResourceContext ctx, IResourceUrn urn) {
      URL jsUrl = ResourceResolvings.fromJar().resolve(ctx, urn, SHARED_RESOURCE_BASE);

      if (jsUrl == null) {
         throw new ResourceException(String.format("Js(%s) not found in any jars or classpath!", SHARED_RESOURCE_BASE
               + jsUrl));
      }

      return jsUrl;
   }

   @Override
   public INamespace getNamespace() {
      return JsNamespace.SHARED;
   }

   @Override
   public SharedJsResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Js.Shared;
   }

   @Override
   public Class<? super SharedJsResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Js;
   }

   @Override
   public SharedJs resolve(IJsRef ref, IResourceContext ctx) throws ResourceException {
      URL jsUrl = getJsURL(ctx, ref.getUrn());
      SharedJs js = new SharedJs(ctx, ref, jsUrl);

      js.validate();

      js.setUrlBuilder(m_urlBuilder);
      return js;
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Js.Shared)
   public void setUrlBuilder(IResourceUrlBuilder<? extends IJs> urlBuilder) {
      m_urlBuilder = (IResourceUrlBuilder<IJs>) urlBuilder;
   }
}