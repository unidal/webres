package org.unidal.webres.resource.aggregation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.unidal.webres.logging.ILogger;
import org.unidal.webres.logging.LoggerFactory;
import org.unidal.webres.resource.api.IResourceRef;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.model.Models;
import org.unidal.webres.resource.model.entity.CommonSlotRef;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.model.transform.BaseVisitor;
import org.unidal.webres.resource.model.transform.ModelModifier;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceTokenStorage;

public abstract class ResourceAggregator<T extends IResourceRef<?>> implements IResourceAggregator<T> {
   private static final String DEFAULT_SLOT = "default";

   private static ILogger s_logger = LoggerFactory.getLogger(ResourceAggregator.class);

   private IResourceType m_resourceType;

   private Root m_model;

   private Page m_page;

   private Map<String, String> m_urnToSlotIdMap = new HashMap<String, String>();

   public ResourceAggregator(String pageId, IResourceType resourceType) {
      m_page = new Page(pageId);
      m_model = new Root().addPage(m_page);
      m_resourceType = resourceType;
   }

   protected void addAggregatedResource(List<T> list, String slotId, List<Resource> resources) {
      int size = resources.size();
      List<Resource> aggregatedResources = new ArrayList<Resource>(size);
      boolean lastExternalized = true;

      for (int i = 0; i < size; i++) {
         Resource resource = resources.get(i);
         String urn = resource.getUrn();
         boolean externalized = urn.indexOf(".inline:") < 0;

         if (i == 0) {
            lastExternalized = externalized;
         } else if (lastExternalized != externalized) {
            list.add(aggregateResource(slotId, aggregatedResources));
            lastExternalized = externalized;
            aggregatedResources.clear();

            if (s_logger.isWarnEnabled()) {
               if (lastExternalized) {
                  s_logger.warn(String.format("Inline %s(%s) found in slot(%s).", getResourceType().getName(), urn, slotId));
               }
            }
         }

         aggregatedResources.add(resource);
      }

      if (aggregatedResources.size() > 0) {
         list.add(aggregateResource(slotId, aggregatedResources));
      }
   }

   protected abstract T aggregateResource(String slotId, List<Resource> resources);

   public void applyProfile() {
      ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();
      Root profile = ctx.getConfig().getProfile(m_resourceType);

      if (profile != null) {
         Models.forObject().merge(m_model, profile);
      }

      String token = ctx.getContainer().getAttribute(String.class, "parent.token." + m_resourceType.getName());

      if (token != null) {
         applyToken(token);
      }

      m_model.accept(new ModelModifier());
   }

   protected void applyToken(String token) {
      ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();
      IResourceTokenStorage storage = ctx.getContainer().getAttribute(IResourceTokenStorage.class);
      List<String> urns = storage.loadResourceUrns(token);

      if (urns != null) {
         //create common slot
         Slot commonSlot = new Slot(token);

         for (String urn : urns) {
            Resource resource = new Resource(urn);

            commonSlot.addResource(resource);
         }

         m_model.addCommonSlot(commonSlot);

         //add common slot to page
         CommonSlotRef slotRef = new CommonSlotRef(commonSlot.getId());

         m_page.addCommonSlotRef(slotRef);
      } else {
         s_logger.warn(String.format("No urns found for token(%s)!", token));
      }
   }

   @Override
   public T getAggregatedResource(String slotId) {
      Slot slot = getOrCreateSlot(slotId);

      if (slot.isActive()) {
         T ref = aggregateResource(slotId, slot.getResources());

         return ref;
      } else {
         return null;
      }
   }

   @Override
   public List<T> getAggregatedResourceWithCommonSlots(String slotId, boolean ignoreCurrentSlot) {
      List<T> list = new ArrayList<T>();
      Slot slot = getOrCreateSlot(slotId);

      if (slot.isActive()) {
         Slot beforeCommonSlot = slot.getBeforeCommonSlot();
         Slot afterCommonSlot = slot.getAfterCommonSlot();

         if (beforeCommonSlot != null) {
            list.add(aggregateResource(beforeCommonSlot.getId(), beforeCommonSlot.getResources()));
         }

         if (!ignoreCurrentSlot) {
            addAggregatedResource(list, slotId, slot.getResources());
         }

         if (afterCommonSlot != null) {
            list.add(aggregateResource(beforeCommonSlot.getId(), afterCommonSlot.getResources()));
         }
      }

      return list;
   }

   protected Slot getOrCreateSlot(String slotId) {
      Slot slot = m_page.findSlot(slotId);

      if (slot == null) {
         slot = new Slot(slotId);
         m_page.addSlot(slot);
      }

      return slot;
   }

   public Page getPage() {
      return m_page;
   }

   @Override
   @SuppressWarnings("unchecked")
   public T getResource(String slotId, String urn) {
      if (slotId == null) {
         slotId = DEFAULT_SLOT;
      }

      Slot slot = m_page.findSlot(slotId);
      Resource resource = (slot == null ? null : slot.findResource(urn));

      if (resource != null && !slot.isActive()) {
         return (T) resource.getReference();
      }

      // no rendering
      return null;
   }

   @Override
   public IResourceType getResourceType() {
      return m_resourceType;
   }

   @Override
   public List<String> getResourceUrns() {
      List<String> urns = new ArrayList<String>();
      ResourceUrnCollector collector = new ResourceUrnCollector(urns);

      m_model.accept(collector);
      return urns;
   }

   @Override
   public List<Resource> getSlotResources(String slotId) {
      Slot slot = getOrCreateSlot(slotId);

      return slot.getResources();
   }

   @Override
   public boolean registerResource(String slotId, T ref) {
      if (slotId == null) {
         slotId = DEFAULT_SLOT;
      }

      String urn = ref.getUrn().toString();

      if (m_urnToSlotIdMap.containsKey(urn)) { // dedup
         return false;
      }

      Slot slot = getOrCreateSlot(slotId);
      Resource resource = slot.findResource(urn);

      if (resource == null) {
         resource = new Resource(urn);

         m_urnToSlotIdMap.put(urn, slotId);
         resource.setReference(ref);
         slot.addResource(resource);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void registerSlot(String slotId) {
      Slot slot = getOrCreateSlot(slotId);

      slot.setActive(true);
   }

   @Override
   public void registerSlot(String slotId, String commonSlotId, boolean before) {
      registerSlot(slotId);

      CommonSlotRef commonSlotRef = new CommonSlotRef(commonSlotId);

      if (before) {
         commonSlotRef.setBeforeSlot(slotId);
      } else {
         commonSlotRef.setAfterSlot(slotId);
      }

      m_page.addCommonSlotRef(commonSlotRef);
   }

   static class ResourceUrnCollector extends BaseVisitor {
      List<String> m_urns;

      public ResourceUrnCollector(List<String> urns) {
         m_urns = urns;
      }

      @Override
      public void visitResource(Resource resource) {
         m_urns.add(resource.getUrn());
      }
   }
}
