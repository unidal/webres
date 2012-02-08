package org.unidal.webres.resource.variation.transform;

import org.unidal.webres.resource.variation.entity.Permutation;
import org.unidal.webres.resource.variation.entity.ResourceMapping;
import org.unidal.webres.resource.variation.entity.ResourceVariation;
import org.unidal.webres.resource.variation.entity.Rule;
import org.unidal.webres.resource.variation.entity.Variation;
import org.unidal.webres.resource.variation.entity.VariationDefinition;
import org.unidal.webres.resource.variation.entity.VariationRef;

public interface IMaker<T> {

   public Permutation buildPermutation(T node);

   public ResourceMapping buildResourceMapping(T node);

   public ResourceVariation buildResourceVariation(T node);

   public Rule buildRule(T node);

   public Variation buildVariation(T node);

   public VariationDefinition buildVariationDefinition(T node);

   public VariationRef buildVariationRef(T node);
}
