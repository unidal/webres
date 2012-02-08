package org.unidal.webres.resource.aggregation;

import java.util.List;

import org.unidal.webres.resource.api.IResourceRef;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.model.entity.Resource;

public interface IResourceAggregator<T extends IResourceRef<?>> {
   public void applyProfile();

   public T getAggregatedResource(String slotId);

   public List<T> getAggregatedResourceWithCommonSlots(String slotId, boolean ignoreCurrent);

   public T getResource(String slotId, String urn);

   public IResourceType getResourceType();

   public List<String> getResourceUrns();

   public List<Resource> getSlotResources(String slotId);

   public boolean registerResource(String slotId, T ref);

   public void registerSlot(String slotId);

   public void registerSlot(String slotId, String commonSlotId, boolean before);
}