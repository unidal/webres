package org.unidal.webres.resource.css;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class AggregatedCssUrlBuilder implements IResourceUrlBuilder<AggregatedCss>,
      IResourceRegisterable<AggregatedCssUrlBuilder> {
   @Override
   public String build(IResourceContext ctx, AggregatedCss css) {
      return null;
   }

   @Override
   public AggregatedCssUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Css.Aggregated;
   }

   @Override
   public Class<? super AggregatedCssUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }
}
