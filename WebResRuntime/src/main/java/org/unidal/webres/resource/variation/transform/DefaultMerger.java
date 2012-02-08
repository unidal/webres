package org.unidal.webres.resource.variation.transform;

import java.util.Stack;

import org.unidal.webres.resource.variation.IVisitor;
import org.unidal.webres.resource.variation.entity.Permutation;
import org.unidal.webres.resource.variation.entity.ResourceMapping;
import org.unidal.webres.resource.variation.entity.ResourceVariation;
import org.unidal.webres.resource.variation.entity.Rule;
import org.unidal.webres.resource.variation.entity.Variation;
import org.unidal.webres.resource.variation.entity.VariationDefinition;
import org.unidal.webres.resource.variation.entity.VariationRef;

public class DefaultMerger implements IVisitor {

   private Stack<Object> m_stack = new Stack<Object>();

   private ResourceVariation m_resourceVariation;

   public DefaultMerger(ResourceVariation resourceVariation) {
      m_resourceVariation = resourceVariation;
   }

   protected ResourceVariation getResourceVariation() {
      return m_resourceVariation;
   }

   protected Stack<Object> getStack() {
      return m_stack;
   }

   @Override
   public void visitPermutation(Permutation permutation) {
      Object parent = m_stack.peek();
      Permutation old = null;

      if (parent instanceof ResourceVariation) {
         ResourceVariation resourceVariation = (ResourceVariation) parent;

         old = resourceVariation.getPermutation();

         if (old == null) {
            resourceVariation.setPermutation(permutation);
         } else {
            old.mergeAttributes(permutation);
         }
      }

      visitPermutationChildren(old, permutation);
   }

   protected void visitPermutationChildren(Permutation old, Permutation permutation) {
      if (old != null) {
         m_stack.push(old);

         for (VariationRef variationRef : permutation.getVariationRefs()) {
            visitVariationRef(variationRef);
         }

         m_stack.pop();
      }
   }

   @Override
   public void visitResourceMapping(ResourceMapping resourceMapping) {
      Object parent = m_stack.peek();
      ResourceMapping old = null;

      if (parent instanceof ResourceVariation) {
         ResourceVariation resourceVariation = (ResourceVariation) parent;

         old = resourceVariation.findResourceMapping(resourceMapping.getUrn());

         if (old == null) {
            resourceVariation.addResourceMapping(resourceMapping);
         } else {
            old.mergeAttributes(resourceMapping);
         }
      }

      visitResourceMappingChildren(old, resourceMapping);
   }

   protected void visitResourceMappingChildren(ResourceMapping old, ResourceMapping resourceMapping) {
      if (old != null) {
         m_stack.push(old);

         for (Rule rule : resourceMapping.getRules()) {
            visitRule(rule);
         }

         m_stack.pop();
      }
   }

   @Override
   public void visitResourceVariation(ResourceVariation resourceVariation) {
      visitResourceVariationChildren(m_resourceVariation, resourceVariation);
   }

   protected void visitResourceVariationChildren(ResourceVariation old, ResourceVariation resourceVariation) {
      if (old != null) {
         m_stack.push(old);

         for (VariationDefinition variationDefinition : resourceVariation.getVariationDefinitions().values()) {
            visitVariationDefinition(variationDefinition);
         }

         if (resourceVariation.getPermutation() != null) {
            visitPermutation(resourceVariation.getPermutation());
         }

         for (ResourceMapping resourceMapping : resourceVariation.getResourceMappings().values()) {
            visitResourceMapping(resourceMapping);
         }

         m_stack.pop();
      }
   }

   @Override
   public void visitRule(Rule rule) {
      Object parent = m_stack.peek();
      Rule old = null;

      if (parent instanceof ResourceMapping) {
         ResourceMapping resourceMapping = (ResourceMapping) parent;

         resourceMapping.addRule(rule);
      }

      visitRuleChildren(old, rule);
   }

   protected void visitRuleChildren(Rule old, Rule rule) {
   }

   @Override
   public void visitVariation(Variation variation) {
      Object parent = m_stack.peek();
      Variation old = null;

      if (parent instanceof VariationDefinition) {
         VariationDefinition variationDefinition = (VariationDefinition) parent;

         old = variationDefinition.findVariation(variation.getId());

         if (old == null) {
            variationDefinition.addVariation(variation);
         } else {
            old.mergeAttributes(variation);
         }
      }

      visitVariationChildren(old, variation);
   }

   protected void visitVariationChildren(Variation old, Variation variation) {
   }

   @Override
   public void visitVariationDefinition(VariationDefinition variationDefinition) {
      Object parent = m_stack.peek();
      VariationDefinition old = null;

      if (parent instanceof ResourceVariation) {
         ResourceVariation resourceVariation = (ResourceVariation) parent;

         old = resourceVariation.findVariationDefinition(variationDefinition.getType());

         if (old == null) {
            resourceVariation.addVariationDefinition(variationDefinition);
         } else {
            old.mergeAttributes(variationDefinition);
         }
      }

      visitVariationDefinitionChildren(old, variationDefinition);
   }

   protected void visitVariationDefinitionChildren(VariationDefinition old, VariationDefinition variationDefinition) {
      if (old != null) {
         m_stack.push(old);

         for (Variation variation : variationDefinition.getVariations()) {
            visitVariation(variation);
         }

         m_stack.pop();
      }
   }

   @Override
   public void visitVariationRef(VariationRef variationRef) {
      Object parent = m_stack.peek();
      VariationRef old = null;

      if (parent instanceof Permutation) {
         Permutation permutation = (Permutation) parent;

         old = permutation.findVariationRef(variationRef.getType());

         if (old == null) {
            permutation.addVariationRef(variationRef);
         } else {
            old.mergeAttributes(variationRef);
         }
      }

      visitVariationRefChildren(old, variationRef);
   }

   protected void visitVariationRefChildren(VariationRef old, VariationRef variationRef) {
   }
}
