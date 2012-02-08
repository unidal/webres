package org.unidal.webres.resource.js;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class AggregatedJsUrlBuilder implements IResourceUrlBuilder<AggregatedJs>,
      IResourceRegisterable<AggregatedJsUrlBuilder> {
   @Override
   public String build(IResourceContext ctx, AggregatedJs js) {
      return null;
   }

   @Override
   public AggregatedJsUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Js.Aggregated;
   }

   @Override
   public Class<? super AggregatedJsUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }
}
