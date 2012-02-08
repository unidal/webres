package org.unidal.webres.resource.variation;

import org.unidal.webres.resource.variation.entity.Permutation;
import org.unidal.webres.resource.variation.entity.ResourceMapping;
import org.unidal.webres.resource.variation.entity.ResourceVariation;
import org.unidal.webres.resource.variation.entity.Rule;
import org.unidal.webres.resource.variation.entity.Variation;
import org.unidal.webres.resource.variation.entity.VariationDefinition;
import org.unidal.webres.resource.variation.entity.VariationRef;

public interface IVisitor {

   public void visitPermutation(Permutation permutation);

   public void visitResourceMapping(ResourceMapping resourceMapping);

   public void visitResourceVariation(ResourceVariation resourceVariation);

   public void visitRule(Rule rule);

   public void visitVariation(Variation variation);

   public void visitVariationDefinition(VariationDefinition variationDefinition);

   public void visitVariationRef(VariationRef variationRef);
}
