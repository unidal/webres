package org.unidal.webres.resource.variation.transform;

import static org.unidal.webres.resource.variation.Constants.ATTR_ID;
import static org.unidal.webres.resource.variation.Constants.ATTR_TYPE;
import static org.unidal.webres.resource.variation.Constants.ATTR_URN;
import static org.unidal.webres.resource.variation.Constants.ENTITY_PERMUTATION;
import static org.unidal.webres.resource.variation.Constants.ENTITY_RESOURCE_MAPPING;
import static org.unidal.webres.resource.variation.Constants.ENTITY_RESOURCE_MAPPINGS;
import static org.unidal.webres.resource.variation.Constants.ENTITY_RESOURCE_VARIATION;
import static org.unidal.webres.resource.variation.Constants.ENTITY_VARIATION;
import static org.unidal.webres.resource.variation.Constants.ENTITY_VARIATION_DEFINITION;
import static org.unidal.webres.resource.variation.Constants.ENTITY_VARIATION_DEFINITIONS;
import static org.unidal.webres.resource.variation.Constants.ENTITY_VARIATION_REF;

import java.util.Stack;

import org.unidal.webres.resource.variation.IVisitor;
import org.unidal.webres.resource.variation.entity.Permutation;
import org.unidal.webres.resource.variation.entity.ResourceMapping;
import org.unidal.webres.resource.variation.entity.ResourceVariation;
import org.unidal.webres.resource.variation.entity.Rule;
import org.unidal.webres.resource.variation.entity.Variation;
import org.unidal.webres.resource.variation.entity.VariationDefinition;
import org.unidal.webres.resource.variation.entity.VariationRef;

public class DefaultValidator implements IVisitor {

   private Path m_path = new Path();
   
   protected void assertRequired(String name, Object value) {
      if (value == null) {
         throw new RuntimeException(String.format("%s at path(%s) is required!", name, m_path));
      }
   }

   @Override
   public void visitPermutation(Permutation permutation) {
      m_path.down(ENTITY_PERMUTATION);

      visitPermutationChildren(permutation);

      m_path.up(ENTITY_PERMUTATION);
   }

   protected void visitPermutationChildren(Permutation permutation) {
      for (VariationRef variationRef : permutation.getVariationRefs()) {
         visitVariationRef(variationRef);
      }
   }

   @Override
   public void visitResourceMapping(ResourceMapping resourceMapping) {
      m_path.down(ENTITY_RESOURCE_MAPPING);

      assertRequired(ATTR_URN, resourceMapping.getUrn());

      visitResourceMappingChildren(resourceMapping);

      m_path.up(ENTITY_RESOURCE_MAPPING);
   }

   protected void visitResourceMappingChildren(ResourceMapping resourceMapping) {
      for (Rule rule : resourceMapping.getRules()) {
         visitRule(rule);
      }
   }

   @Override
   public void visitResourceVariation(ResourceVariation resourceVariation) {
      m_path.down(ENTITY_RESOURCE_VARIATION);

      visitResourceVariationChildren(resourceVariation);

      m_path.up(ENTITY_RESOURCE_VARIATION);
   }

   protected void visitResourceVariationChildren(ResourceVariation resourceVariation) {
      m_path.down(ENTITY_VARIATION_DEFINITIONS);

      for (VariationDefinition variationDefinition : resourceVariation.getVariationDefinitions().values()) {
         visitVariationDefinition(variationDefinition);
      }

      m_path.up(ENTITY_VARIATION_DEFINITIONS);

      if (resourceVariation.getPermutation() != null) {
         visitPermutation(resourceVariation.getPermutation());
      }

      m_path.down(ENTITY_RESOURCE_MAPPINGS);

      for (ResourceMapping resourceMapping : resourceVariation.getResourceMappings().values()) {
         visitResourceMapping(resourceMapping);
      }

      m_path.up(ENTITY_RESOURCE_MAPPINGS);
   }

   @Override
   public void visitRule(Rule rule) {
   }

   @Override
   public void visitVariation(Variation variation) {
      m_path.down(ENTITY_VARIATION);

      assertRequired(ATTR_ID, variation.getId());

      m_path.up(ENTITY_VARIATION);
   }

   @Override
   public void visitVariationDefinition(VariationDefinition variationDefinition) {
      m_path.down(ENTITY_VARIATION_DEFINITION);

      assertRequired(ATTR_TYPE, variationDefinition.getType());

      visitVariationDefinitionChildren(variationDefinition);

      m_path.up(ENTITY_VARIATION_DEFINITION);
   }

   protected void visitVariationDefinitionChildren(VariationDefinition variationDefinition) {
      for (Variation variation : variationDefinition.getVariations()) {
         visitVariation(variation);
      }
   }

   @Override
   public void visitVariationRef(VariationRef variationRef) {
      m_path.down(ENTITY_VARIATION_REF);

      assertRequired(ATTR_TYPE, variationRef.getType());

      m_path.up(ENTITY_VARIATION_REF);
   }

   static class Path {
      private Stack<String> m_sections = new Stack<String>();

      public Path down(String nextSection) {
         m_sections.push(nextSection);

         return this;
      }

      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder();

         for (String section : m_sections) {
            sb.append('/').append(section);
         }

         return sb.toString();
      }

      public Path up(String currentSection) {
         if (m_sections.isEmpty() || !m_sections.peek().equals(currentSection)) {
            throw new RuntimeException("INTERNAL ERROR: stack mismatched!");
         }

         m_sections.pop();
         return this;
      }
   }
}
