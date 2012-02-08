package org.unidal.webres.resource.css;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.api.INamespace;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.injection.ResourceAttribute;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceResolver;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class AggregatedCssResolver implements IResourceResolver<AggregatedCssRef, ICss>,
      IResourceRegisterable<AggregatedCssResolver> {
   private IResourceUrlBuilder<ICss> m_urlBuilder;

   private boolean m_validate;

   private boolean m_verbose;

   @Override
   public INamespace getNamespace() {
      return CssNamespace.AGGREGATED;
   }

   @Override
   public AggregatedCssResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Css.Aggregated;
   }

   @Override
   public Class<? super AggregatedCssResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Css;
   }

   @Override
   public AggregatedCss resolve(AggregatedCssRef ref, IResourceContext ctx) throws ResourceException {
      AggregatedCss css = new AggregatedCss(ctx, ref, m_verbose);

      if (m_validate) {
         css.validate();
      }

      css.setUrlBuilder(m_urlBuilder);
      return css;
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Css.Aggregated)
   public void setUrlBuilder(IResourceUrlBuilder<? extends ICss> urlBuilder) {
      m_urlBuilder = (IResourceUrlBuilder<ICss>) urlBuilder;
   }

   @ResourceAttribute(value = ResourceConstant.Css.AggregatedValidation, optional = true)
   public void setValidate(boolean validate) {
      m_validate = validate;
   }

   @ResourceAttribute(value = ResourceConstant.Css.AggregatedVerbose, optional = true)
   public void setVerbose(boolean verbose) {
      m_verbose = verbose;
   }
}