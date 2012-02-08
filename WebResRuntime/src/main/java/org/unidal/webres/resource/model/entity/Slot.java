package org.unidal.webres.resource.model.entity;

import static org.unidal.webres.resource.model.Constants.ATTR_ID;
import static org.unidal.webres.resource.model.Constants.ENTITY_SLOT;

import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.resource.model.BaseEntity;
import org.unidal.webres.resource.model.IVisitor;

public class Slot extends BaseEntity<Slot> {
   private String m_id;

   private Boolean m_override;

   private Boolean m_active;

   private Boolean m_flushed;

   private Boolean m_skipFragments;

   private List<Resource> m_resources = new ArrayList<Resource>();

   private Slot m_beforeCommonSlot;

   private Slot m_afterCommonSlot;

   public Slot(String id) {
      m_id = id;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitSlot(this);
   }

   public Slot addResource(Resource resource) {
      m_resources.add(resource);
      return this;
   }

   public Resource findResource(String urn) {
      for (Resource resource : m_resources) {
         if (!resource.getUrn().equals(urn)) {
            continue;
         }

         return resource;
      }

      return null;
   }

   public Boolean getActive() {
      return m_active;
   }

   public Slot getAfterCommonSlot() {
      return m_afterCommonSlot;
   }

   public Slot getBeforeCommonSlot() {
      return m_beforeCommonSlot;
   }

   public Boolean getFlushed() {
      return m_flushed;
   }

   public String getId() {
      return m_id;
   }

   public Boolean getOverride() {
      return m_override;
   }

   public List<Resource> getResources() {
      return m_resources;
   }

   public Boolean getSkipFragments() {
      return m_skipFragments;
   }

   public boolean isActive() {
      return m_active != null && m_active.booleanValue();
   }

   public boolean isFlushed() {
      return m_flushed != null && m_flushed.booleanValue();
   }

   public boolean isOverride() {
      return m_override != null && m_override.booleanValue();
   }

   public boolean isSkipFragments() {
      return m_skipFragments != null && m_skipFragments.booleanValue();
   }

   @Override
   public void mergeAttributes(Slot other) {
      assertAttributeEquals(other, ENTITY_SLOT, ATTR_ID, m_id, other.getId());

      if (other.getOverride() != null) {
         m_override = other.getOverride();
      }

      if (other.getActive() != null) {
         m_active = other.getActive();
      }

      if (other.getFlushed() != null) {
         m_flushed = other.getFlushed();
      }

      if (other.getSkipFragments() != null) {
         m_skipFragments = other.getSkipFragments();
      }
   }

   public boolean removeResource(String urn) {
      int len = m_resources.size();

      for (int i = 0; i < len; i++) {
         Resource resource = m_resources.get(i);

         if (!resource.getUrn().equals(urn)) {
            continue;
         }

         m_resources.remove(i);
         return true;
      }

      return false;
   }

   public Slot setActive(Boolean active) {
      m_active=active;
      return this;
   }

   public Slot setAfterCommonSlot(Slot afterCommonSlot) {
      m_afterCommonSlot=afterCommonSlot;
      return this;
   }

   public Slot setBeforeCommonSlot(Slot beforeCommonSlot) {
      m_beforeCommonSlot=beforeCommonSlot;
      return this;
   }

   public Slot setFlushed(Boolean flushed) {
      m_flushed=flushed;
      return this;
   }

   public Slot setOverride(Boolean override) {
      m_override=override;
      return this;
   }

   public Slot setSkipFragments(Boolean skipFragments) {
      m_skipFragments=skipFragments;
      return this;
   }

}
