package org.unidal.webres.resource.css;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.resource.api.INamespace;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.injection.ResourceAttribute;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceResolver;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class InlineCssResolver implements IResourceResolver<ICssRef, ICss>, IResourceRegisterable<InlineCssResolver> {
   private IResourceUrlBuilder<ICss> m_urlBuilder;

   @Override
   public INamespace getNamespace() {
      return CssNamespace.INLINE;
   }

   @Override
   public InlineCssResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Css.Inline;
   }

   @Override
   public Class<? super InlineCssResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Css;
   }

   @Override
   public ICss resolve(ICssRef ref, IResourceContext ctx) throws ResourceException {
      InlineCss css = new InlineCss(ctx, (InlineCssRef) ref);

      css.validate();
      css.setUrlBuilder(m_urlBuilder);
      return css;
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Css.Inline)
   public void setUrlBuilder(IResourceUrlBuilder<? extends ICss> urlBuilder) {
      m_urlBuilder = (IResourceUrlBuilder<ICss>) urlBuilder;
   }
}