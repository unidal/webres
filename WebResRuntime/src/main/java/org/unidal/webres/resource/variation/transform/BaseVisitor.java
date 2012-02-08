package org.unidal.webres.resource.variation.transform;

import org.unidal.webres.resource.variation.IVisitor;
import org.unidal.webres.resource.variation.entity.Permutation;
import org.unidal.webres.resource.variation.entity.ResourceMapping;
import org.unidal.webres.resource.variation.entity.ResourceVariation;
import org.unidal.webres.resource.variation.entity.Rule;
import org.unidal.webres.resource.variation.entity.Variation;
import org.unidal.webres.resource.variation.entity.VariationDefinition;
import org.unidal.webres.resource.variation.entity.VariationRef;

public abstract class BaseVisitor implements IVisitor {
   @Override
   public void visitPermutation(Permutation permutation) {
      for (VariationRef variationRef : permutation.getVariationRefs()) {
         visitVariationRef(variationRef);
      }
   }

   @Override
   public void visitResourceMapping(ResourceMapping resourceMapping) {
      for (Rule rule : resourceMapping.getRules()) {
         visitRule(rule);
      }
   }

   @Override
   public void visitResourceVariation(ResourceVariation resourceVariation) {
      for (VariationDefinition variationDefinition : resourceVariation.getVariationDefinitions().values()) {
         visitVariationDefinition(variationDefinition);
      }

      if (resourceVariation.getPermutation() != null) {
         visitPermutation(resourceVariation.getPermutation());
      }

      for (ResourceMapping resourceMapping : resourceVariation.getResourceMappings().values()) {
         visitResourceMapping(resourceMapping);
      }
   }

   @Override
   public void visitRule(Rule rule) {
   }

   @Override
   public void visitVariation(Variation variation) {
   }

   @Override
   public void visitVariationDefinition(VariationDefinition variationDefinition) {
      for (Variation variation : variationDefinition.getVariations()) {
         visitVariation(variation);
      }
   }

   @Override
   public void visitVariationRef(VariationRef variationRef) {
   }
}
