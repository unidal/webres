package org.unidal.webres.resource.css;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class InlineCssUrlBuilder implements IResourceUrlBuilder<ICss>, IResourceRegisterable<InlineCssUrlBuilder> {
   @Override
   public String build(IResourceContext ctx, ICss css) {
      // Can't build externalized url for an inline resource
      return null;
   }

   @Override
   public InlineCssUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Css.Inline;
   }

   @Override
   public Class<? super InlineCssUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }
}
