package org.unidal.webres.resource.js;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.api.INamespace;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.injection.ResourceAttribute;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceResolver;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class InlineJsResolver implements IResourceResolver<IJsRef, IJs>, IResourceRegisterable<InlineJsResolver> {
   private IResourceUrlBuilder<IJs> m_urlBuilder;

   @Override
   public INamespace getNamespace() {
      return JsNamespace.INLINE;
   }

   @Override
   public InlineJsResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Js.Inline;
   }

   @Override
   public Class<? super InlineJsResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Js;
   }

   @Override
   public IJs resolve(IJsRef ref, IResourceContext ctx) throws ResourceException {
      InlineJs js = new InlineJs(ctx, (InlineJsRef) ref);

      js.validate();
      js.setUrlBuilder(m_urlBuilder);
      return js;
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Js.Inline)
   public void setUrlBuilder(IResourceUrlBuilder<? extends IJs> urlBuilder) {
      m_urlBuilder = (IResourceUrlBuilder<IJs>) urlBuilder;
   }
}