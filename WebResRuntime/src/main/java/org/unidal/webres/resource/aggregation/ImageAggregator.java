package org.unidal.webres.resource.aggregation;

import java.util.List;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IImageRef;
import org.unidal.webres.resource.model.entity.Resource;

public class ImageAggregator extends ResourceAggregator<IImageRef> {
   public ImageAggregator(String pageId) {
      super(pageId, SystemResourceType.Image);
   }

   @Override
   protected IImageRef aggregateResource(String slotId, List<Resource> resources) {
      throw new UnsupportedOperationException("Not supported yet!");
   }

   @Override
   public List<String> getResourceUrns() {
      throw new UnsupportedOperationException("Not supported yet!");
   }
}
