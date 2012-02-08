package org.unidal.webres.resource.variation.transform;

import org.unidal.webres.resource.variation.entity.Permutation;
import org.unidal.webres.resource.variation.entity.ResourceMapping;
import org.unidal.webres.resource.variation.entity.ResourceVariation;
import org.unidal.webres.resource.variation.entity.Rule;
import org.unidal.webres.resource.variation.entity.Variation;
import org.unidal.webres.resource.variation.entity.VariationDefinition;
import org.unidal.webres.resource.variation.entity.VariationRef;

public interface ILinker {

   public boolean onPermutation(ResourceVariation parent, Permutation permutation);

   public boolean onResourceMapping(ResourceVariation parent, ResourceMapping resourceMapping);

   public boolean onRule(ResourceMapping parent, Rule rule);

   public boolean onVariation(VariationDefinition parent, Variation variation);

   public boolean onVariationDefinition(ResourceVariation parent, VariationDefinition variationDefinition);

   public boolean onVariationRef(Permutation parent, VariationRef variationRef);
}
