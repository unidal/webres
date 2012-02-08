package org.unidal.webres.resource.js;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.api.INamespace;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.injection.ResourceAttribute;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceResolver;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class AggregatedJsResolver implements IResourceResolver<AggregatedJsRef, IJs>,
      IResourceRegisterable<AggregatedJsResolver> {
   private IResourceUrlBuilder<IJs> m_urlBuilder;

   private boolean m_validate;

   private boolean m_verbose;

   @Override
   public INamespace getNamespace() {
      return JsNamespace.AGGREGATED;
   }

   @Override
   public AggregatedJsResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Js.Aggregated;
   }

   @Override
   public Class<? super AggregatedJsResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Js;
   }

   @Override
   public AggregatedJs resolve(AggregatedJsRef ref, IResourceContext ctx) throws ResourceException {
      AggregatedJs js = new AggregatedJs(ctx, ref, m_verbose);

      if (m_validate) {
         js.validate();
      }

      js.setUrlBuilder(m_urlBuilder);
      return js;
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Js.Aggregated)
   public void setUrlBuilder(IResourceUrlBuilder<? extends IJs> urlBuilder) {
      m_urlBuilder = (IResourceUrlBuilder<IJs>) urlBuilder;
   }

   @ResourceAttribute(value = ResourceConstant.Js.AggregatedValidation, optional = true)
   public void setValidate(boolean validate) {
      m_validate = validate;
   }

   @ResourceAttribute(value = ResourceConstant.Js.AggregatedVerbose, optional = true)
   public void setVerbose(boolean verbose) {
      m_verbose = verbose;
   }
}