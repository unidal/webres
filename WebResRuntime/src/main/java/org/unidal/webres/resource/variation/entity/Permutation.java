package org.unidal.webres.resource.variation.entity;

import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.resource.variation.BaseEntity;
import org.unidal.webres.resource.variation.IVisitor;

public class Permutation extends BaseEntity<Permutation> {
   private List<VariationRef> m_variationRefs = new ArrayList<VariationRef>();

   public Permutation() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitPermutation(this);
   }

   public Permutation addVariationRef(VariationRef variationRef) {
      m_variationRefs.add(variationRef);
      return this;
   }

   public VariationRef findVariationRef(String type) {
      for (VariationRef variationRef : m_variationRefs) {
         if (!variationRef.getType().equals(type)) {
            continue;
         }

         return variationRef;
      }

      return null;
   }

   public List<VariationRef> getVariationRefs() {
      return m_variationRefs;
   }

   @Override
   public void mergeAttributes(Permutation other) {
   }

   public boolean removeVariationRef(String type) {
      int len = m_variationRefs.size();

      for (int i = 0; i < len; i++) {
         VariationRef variationRef = m_variationRefs.get(i);

         if (!variationRef.getType().equals(type)) {
            continue;
         }

         m_variationRefs.remove(i);
         return true;
      }

      return false;
   }

}
