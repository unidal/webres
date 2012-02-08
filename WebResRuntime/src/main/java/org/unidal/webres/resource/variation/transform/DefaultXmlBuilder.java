package org.unidal.webres.resource.variation.transform;

import static org.unidal.webres.resource.variation.Constants.ATTR_ID;
import static org.unidal.webres.resource.variation.Constants.ATTR_TYPE;
import static org.unidal.webres.resource.variation.Constants.ATTR_URN;
import static org.unidal.webres.resource.variation.Constants.ENTITY_PERMUTATION;
import static org.unidal.webres.resource.variation.Constants.ENTITY_RESOURCE_MAPPING;
import static org.unidal.webres.resource.variation.Constants.ENTITY_RESOURCE_MAPPINGS;
import static org.unidal.webres.resource.variation.Constants.ENTITY_RESOURCE_VARIATION;
import static org.unidal.webres.resource.variation.Constants.ENTITY_RULE;
import static org.unidal.webres.resource.variation.Constants.ENTITY_VARIATION;
import static org.unidal.webres.resource.variation.Constants.ENTITY_VARIATION_DEFINITION;
import static org.unidal.webres.resource.variation.Constants.ENTITY_VARIATION_DEFINITIONS;
import static org.unidal.webres.resource.variation.Constants.ENTITY_VARIATION_REF;

import org.unidal.webres.resource.variation.IVisitor;
import org.unidal.webres.resource.variation.entity.Permutation;
import org.unidal.webres.resource.variation.entity.ResourceMapping;
import org.unidal.webres.resource.variation.entity.ResourceVariation;
import org.unidal.webres.resource.variation.entity.Rule;
import org.unidal.webres.resource.variation.entity.Variation;
import org.unidal.webres.resource.variation.entity.VariationDefinition;
import org.unidal.webres.resource.variation.entity.VariationRef;

public class DefaultXmlBuilder implements IVisitor {

   private int m_level;

   private StringBuilder m_sb = new StringBuilder(2048);

   protected void endTag(String name) {
      m_level--;

      indent();
      m_sb.append("</").append(name).append(">\r\n");
   }

   public String getString() {
      return m_sb.toString();
   }

   protected void indent() {
      for (int i = m_level - 1; i >= 0; i--) {
         m_sb.append("   ");
      }
   }


   protected void startTag(String name) {
      startTag(name, null, false);
   }
   
   protected void startTag(String name, java.util.Map<String, String> dynamicAttributes, boolean closed, Object... nameValues) {
      startTag(name, dynamicAttributes, null, closed, nameValues);
   }

   protected void startTag(String name, java.util.Map<String, String> dynamicAttributes, Object... nameValues) {
      startTag(name, dynamicAttributes, false, nameValues);
   }

   protected void startTag(String name, java.util.Map<String, String> dynamicAttributes, Object text, boolean closed, Object... nameValues) {
      indent();

      m_sb.append('<').append(name);

      int len = nameValues.length;

      for (int i = 0; i + 1 < len; i += 2) {
         Object attrName = nameValues[i];
         Object attrValue = nameValues[i + 1];

         if (attrValue != null) {
            m_sb.append(' ').append(attrName).append("=\"").append(attrValue).append('"');
         }
      }

      if (dynamicAttributes != null) {
         for (java.util.Map.Entry<String, String> e : dynamicAttributes.entrySet()) {
            m_sb.append(' ').append(e.getKey()).append("=\"").append(e.getValue()).append('"');
         }
      }

      if (text != null && closed) {
         m_sb.append('>');
         m_sb.append(text == null ? "" : text);
         m_sb.append("</").append(name).append(">\r\n");
      } else {
         if (closed) {
            m_sb.append('/');
         } else {
            m_level++;
         }
   
         m_sb.append(">\r\n");
      }
   }

   @Override
   public void visitPermutation(Permutation permutation) {
      startTag(ENTITY_PERMUTATION, null);

      if (!permutation.getVariationRefs().isEmpty()) {
         for (VariationRef variationRef : permutation.getVariationRefs()) {
            visitVariationRef(variationRef);
         }
      }

      endTag(ENTITY_PERMUTATION);
   }

   @Override
   public void visitResourceMapping(ResourceMapping resourceMapping) {
      startTag(ENTITY_RESOURCE_MAPPING, null, ATTR_URN, resourceMapping.getUrn());

      if (!resourceMapping.getRules().isEmpty()) {
         for (Rule rule : resourceMapping.getRules()) {
            visitRule(rule);
         }
      }

      endTag(ENTITY_RESOURCE_MAPPING);
   }

   @Override
   public void visitResourceVariation(ResourceVariation resourceVariation) {
      startTag(ENTITY_RESOURCE_VARIATION, null);

      if (!resourceVariation.getVariationDefinitions().isEmpty()) {
         startTag(ENTITY_VARIATION_DEFINITIONS);

         for (VariationDefinition variationDefinition : resourceVariation.getVariationDefinitions().values()) {
            visitVariationDefinition(variationDefinition);
         }

         endTag(ENTITY_VARIATION_DEFINITIONS);
      }

      if (resourceVariation.getPermutation() != null) {
         visitPermutation(resourceVariation.getPermutation());
      }

      if (!resourceVariation.getResourceMappings().isEmpty()) {
         startTag(ENTITY_RESOURCE_MAPPINGS);

         for (ResourceMapping resourceMapping : resourceVariation.getResourceMappings().values()) {
            visitResourceMapping(resourceMapping);
         }

         endTag(ENTITY_RESOURCE_MAPPINGS);
      }

      endTag(ENTITY_RESOURCE_VARIATION);
   }

   @Override
   public void visitRule(Rule rule) {
      startTag(ENTITY_RULE, rule.getDynamicAttributes(), rule.getText(), true);
   }

   @Override
   public void visitVariation(Variation variation) {
      startTag(ENTITY_VARIATION, variation.getDynamicAttributes(), true, ATTR_ID, variation.getId());
   }

   @Override
   public void visitVariationDefinition(VariationDefinition variationDefinition) {
      startTag(ENTITY_VARIATION_DEFINITION, null, ATTR_TYPE, variationDefinition.getType());

      if (!variationDefinition.getVariations().isEmpty()) {
         for (Variation variation : variationDefinition.getVariations()) {
            visitVariation(variation);
         }
      }

      endTag(ENTITY_VARIATION_DEFINITION);
   }

   @Override
   public void visitVariationRef(VariationRef variationRef) {
      startTag(ENTITY_VARIATION_REF, null, true, ATTR_TYPE, variationRef.getType(), ATTR_ID, variationRef.getId());
   }
}
