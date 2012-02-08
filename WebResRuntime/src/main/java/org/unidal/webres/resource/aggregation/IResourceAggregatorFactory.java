package org.unidal.webres.resource.aggregation;

import org.unidal.webres.resource.api.IResourceRef;
import org.unidal.webres.resource.api.IResourceType;

public interface IResourceAggregatorFactory {
   public IResourceAggregator<? extends IResourceRef<?>> createAggregator(String pageId, IResourceType resourceType);
}