package org.unidal.webres.resource.aggregation;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IResourceRef;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.spi.IResourceRegisterable;

public class ResourceAggregatorFactory implements IResourceAggregatorFactory, IResourceRegisterable<ResourceAggregatorFactory> {
   @Override
   public IResourceAggregator<? extends IResourceRef<?>> createAggregator(String pageId, IResourceType resourceType) {
      if (resourceType == SystemResourceType.Js) {
         return new JsAggregator(pageId);
      } else if (resourceType == SystemResourceType.Css) {
         return new CssAggregator(pageId);
      } else if (resourceType == SystemResourceType.Image) {
         return new ImageAggregator(pageId);
      } else {
         throw new RuntimeException(String.format("Resource aggregation is not supported for resource type(%s)!",
               resourceType.getName()));
      }
   }

   @Override
   public ResourceAggregatorFactory getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return null;
   }

   @Override
   public Class<? super ResourceAggregatorFactory> getRegisterType() {
      return IResourceAggregatorFactory.class;
   }
}
