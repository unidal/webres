package org.unidal.webres.resource.js;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class InlineJsUrlBuilder implements IResourceUrlBuilder<IJs>, IResourceRegisterable<InlineJsUrlBuilder> {
   @Override
   public String build(IResourceContext ctx, IJs js) {
      // Can't build externalized url for an inline resource
      return null;
   }

   @Override
   public InlineJsUrlBuilder getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Js.Inline;
   }

   @Override
   public Class<? super InlineJsUrlBuilder> getRegisterType() {
      return IResourceUrlBuilder.class;
   }
}
