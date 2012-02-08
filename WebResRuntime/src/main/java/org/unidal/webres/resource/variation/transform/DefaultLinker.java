package org.unidal.webres.resource.variation.transform;

import org.unidal.webres.resource.variation.entity.Permutation;
import org.unidal.webres.resource.variation.entity.ResourceMapping;
import org.unidal.webres.resource.variation.entity.ResourceVariation;
import org.unidal.webres.resource.variation.entity.Rule;
import org.unidal.webres.resource.variation.entity.Variation;
import org.unidal.webres.resource.variation.entity.VariationDefinition;
import org.unidal.webres.resource.variation.entity.VariationRef;

public class DefaultLinker implements ILinker {

   @Override
   public boolean onPermutation(ResourceVariation parent, Permutation permutation) {
      parent.setPermutation(permutation);
      return true;
   }

   @Override
   public boolean onResourceMapping(ResourceVariation parent, ResourceMapping resourceMapping) {
      parent.addResourceMapping(resourceMapping);
      return true;
   }

   @Override
   public boolean onRule(ResourceMapping parent, Rule rule) {
      parent.addRule(rule);
      return true;
   }

   @Override
   public boolean onVariation(VariationDefinition parent, Variation variation) {
      parent.addVariation(variation);
      return true;
   }

   @Override
   public boolean onVariationDefinition(ResourceVariation parent, VariationDefinition variationDefinition) {
      parent.addVariationDefinition(variationDefinition);
      return true;
   }

   @Override
   public boolean onVariationRef(Permutation parent, VariationRef variationRef) {
      parent.addVariationRef(variationRef);
      return true;
   }
}
