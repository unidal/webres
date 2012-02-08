package org.unidal.webres.resource.variation.entity;

import static org.unidal.webres.resource.variation.Constants.ATTR_URN;
import static org.unidal.webres.resource.variation.Constants.ENTITY_RESOURCE_MAPPING;

import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.resource.variation.BaseEntity;
import org.unidal.webres.resource.variation.IVisitor;

public class ResourceMapping extends BaseEntity<ResourceMapping> {
   private String m_urn;

   private String m_fallbackUrn;

   private List<Rule> m_rules = new ArrayList<Rule>();

   public ResourceMapping(String urn) {
      m_urn = urn;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitResourceMapping(this);
   }

   public ResourceMapping addRule(Rule rule) {
      m_rules.add(rule);
      return this;
   }

   public String getFallbackUrn() {
      return m_fallbackUrn;
   }

   public List<Rule> getRules() {
      return m_rules;
   }

   public String getUrn() {
      return m_urn;
   }

   @Override
   public void mergeAttributes(ResourceMapping other) {
      assertAttributeEquals(other, ENTITY_RESOURCE_MAPPING, ATTR_URN, m_urn, other.getUrn());

      if (other.getFallbackUrn() != null) {
         m_fallbackUrn = other.getFallbackUrn();
      }
   }

   public ResourceMapping setFallbackUrn(String fallbackUrn) {
      m_fallbackUrn=fallbackUrn;
      return this;
   }

   public ResourceMapping setUrn(String urn) {
      m_urn=urn;
      return this;
   }

}
