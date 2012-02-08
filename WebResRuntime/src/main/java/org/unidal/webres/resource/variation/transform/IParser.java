package org.unidal.webres.resource.variation.transform;

import org.unidal.webres.resource.variation.entity.Permutation;
import org.unidal.webres.resource.variation.entity.ResourceMapping;
import org.unidal.webres.resource.variation.entity.ResourceVariation;
import org.unidal.webres.resource.variation.entity.Rule;
import org.unidal.webres.resource.variation.entity.Variation;
import org.unidal.webres.resource.variation.entity.VariationDefinition;
import org.unidal.webres.resource.variation.entity.VariationRef;

public interface IParser<T> {
   public ResourceVariation parse(IMaker<T> maker, ILinker linker, T node);

   public void parseForPermutation(IMaker<T> maker, ILinker linker, Permutation parent, T node);

   public void parseForResourceMapping(IMaker<T> maker, ILinker linker, ResourceMapping parent, T node);

   public void parseForRule(IMaker<T> maker, ILinker linker, Rule parent, T node);

   public void parseForVariation(IMaker<T> maker, ILinker linker, Variation parent, T node);

   public void parseForVariationDefinition(IMaker<T> maker, ILinker linker, VariationDefinition parent, T node);

   public void parseForVariationRef(IMaker<T> maker, ILinker linker, VariationRef parent, T node);
}
