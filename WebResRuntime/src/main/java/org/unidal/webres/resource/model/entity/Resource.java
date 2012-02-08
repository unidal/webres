package org.unidal.webres.resource.model.entity;

import static org.unidal.webres.resource.model.Constants.ATTR_URN;
import static org.unidal.webres.resource.model.Constants.ENTITY_RESOURCE;

import org.unidal.webres.resource.model.BaseEntity;
import org.unidal.webres.resource.model.IVisitor;

public class Resource extends BaseEntity<Resource> {
   private String m_urn;

   private String m_oldUrn;

   private Boolean m_enabled;

   private Object m_reference;

   public Resource(String urn) {
      m_urn = urn;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitResource(this);
   }

   public Boolean getEnabled() {
      return m_enabled;
   }

   public String getOldUrn() {
      return m_oldUrn;
   }

   public Object getReference() {
      return m_reference;
   }

   public String getUrn() {
      return m_urn;
   }

   public boolean isEnabled() {
      return m_enabled != null && m_enabled.booleanValue();
   }

   @Override
   public void mergeAttributes(Resource other) {
      assertAttributeEquals(other, ENTITY_RESOURCE, ATTR_URN, m_urn, other.getUrn());

      if (other.getOldUrn() != null) {
         m_oldUrn = other.getOldUrn();
      }

      if (other.getEnabled() != null) {
         m_enabled = other.getEnabled();
      }

      if (other.getReference() != null) {
         m_reference = other.getReference();
      }
   }

   public Resource setEnabled(Boolean enabled) {
      m_enabled=enabled;
      return this;
   }

   public Resource setOldUrn(String oldUrn) {
      m_oldUrn=oldUrn;
      return this;
   }

   public Resource setReference(Object reference) {
      m_reference=reference;
      return this;
   }

}
