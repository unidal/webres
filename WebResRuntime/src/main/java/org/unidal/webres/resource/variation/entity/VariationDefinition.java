package org.unidal.webres.resource.variation.entity;

import static org.unidal.webres.resource.variation.Constants.ATTR_TYPE;
import static org.unidal.webres.resource.variation.Constants.ENTITY_VARIATION_DEFINITION;

import java.util.ArrayList;
import java.util.List;

import org.unidal.webres.resource.variation.BaseEntity;
import org.unidal.webres.resource.variation.IVisitor;

public class VariationDefinition extends BaseEntity<VariationDefinition> {
   private String m_type;

   private List<Variation> m_variations = new ArrayList<Variation>();

   public VariationDefinition(String type) {
      m_type = type;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitVariationDefinition(this);
   }

   public VariationDefinition addVariation(Variation variation) {
      m_variations.add(variation);
      return this;
   }

   public Variation findVariation(String id) {
      for (Variation variation : m_variations) {
         if (!variation.getId().equals(id)) {
            continue;
         }

         return variation;
      }

      return null;
   }

   public String getType() {
      return m_type;
   }

   public List<Variation> getVariations() {
      return m_variations;
   }

   @Override
   public void mergeAttributes(VariationDefinition other) {
      assertAttributeEquals(other, ENTITY_VARIATION_DEFINITION, ATTR_TYPE, m_type, other.getType());

   }

   public boolean removeVariation(String id) {
      int len = m_variations.size();

      for (int i = 0; i < len; i++) {
         Variation variation = m_variations.get(i);

         if (!variation.getId().equals(id)) {
            continue;
         }

         m_variations.remove(i);
         return true;
      }

      return false;
   }

}
