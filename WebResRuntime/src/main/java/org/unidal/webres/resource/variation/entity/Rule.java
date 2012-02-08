package org.unidal.webres.resource.variation.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import org.unidal.webres.resource.variation.BaseEntity;
import org.unidal.webres.resource.variation.IVisitor;

public class Rule extends BaseEntity<Rule> {
   private org.unidal.webres.resource.api.IResourceUrn m_urn;

   private String m_text;

   private Map<String, String> m_dynamicAttributes = new LinkedHashMap<String, String>();

   public Rule() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitRule(this);
   }

   public String getDynamicAttribute(String name) {
      return m_dynamicAttributes.get(name);
   }

   public Map<String, String> getDynamicAttributes() {
      return m_dynamicAttributes;
   }

   public String getText() {
      return m_text;
   }

   public org.unidal.webres.resource.api.IResourceUrn getUrn() {
      return m_urn;
   }

   @Override
   public void mergeAttributes(Rule other) {
      for (Map.Entry<String, String> e : other.getDynamicAttributes().entrySet()) {
         m_dynamicAttributes.put(e.getKey(), e.getValue());
      }

      if (other.getUrn() != null) {
         m_urn = other.getUrn();
      }
   }

   public void setDynamicAttribute(String name, String value) {
      m_dynamicAttributes.put(name, value);
   }

   public Rule setText(String text) {
      m_text=text;
      return this;
   }

   public Rule setUrn(org.unidal.webres.resource.api.IResourceUrn urn) {
      m_urn=urn;
      return this;
   }

}
