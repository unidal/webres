package org.unidal.webres.resource.variation.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import org.unidal.webres.resource.variation.BaseEntity;
import org.unidal.webres.resource.variation.IVisitor;

public class ResourceVariation extends BaseEntity<ResourceVariation> {
   private Map<String, VariationDefinition> m_variationDefinitions = new LinkedHashMap<String, VariationDefinition>();

   private Permutation m_permutation;

   private Map<String, ResourceMapping> m_resourceMappings = new LinkedHashMap<String, ResourceMapping>();

   public ResourceVariation() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitResourceVariation(this);
   }

   public ResourceVariation addResourceMapping(ResourceMapping resourceMapping) {
      m_resourceMappings.put(resourceMapping.getUrn(), resourceMapping);
      return this;
   }

   public ResourceVariation addVariationDefinition(VariationDefinition variationDefinition) {
      m_variationDefinitions.put(variationDefinition.getType(), variationDefinition);
      return this;
   }

   public ResourceMapping findResourceMapping(String urn) {
      return m_resourceMappings.get(urn);
   }

   public VariationDefinition findVariationDefinition(String type) {
      return m_variationDefinitions.get(type);
   }

   public Permutation getPermutation() {
      return m_permutation;
   }

   public Map<String, ResourceMapping> getResourceMappings() {
      return m_resourceMappings;
   }

   public Map<String, VariationDefinition> getVariationDefinitions() {
      return m_variationDefinitions;
   }

   @Override
   public void mergeAttributes(ResourceVariation other) {
   }

   public boolean removeResourceMapping(String urn) {
      if (m_resourceMappings.containsKey(urn)) {
         m_resourceMappings.remove(urn);
         return true;
      }

      return false;
   }

   public boolean removeVariationDefinition(String type) {
      if (m_variationDefinitions.containsKey(type)) {
         m_variationDefinitions.remove(type);
         return true;
      }

      return false;
   }

   public ResourceVariation setPermutation(Permutation permutation) {
      m_permutation=permutation;
      return this;
   }

}
