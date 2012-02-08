package org.unidal.webres.resource.variation.transform;

import static org.unidal.webres.resource.variation.Constants.ATTR_ID;
import static org.unidal.webres.resource.variation.Constants.ATTR_TYPE;
import static org.unidal.webres.resource.variation.Constants.ATTR_URN;

import java.util.LinkedHashMap;
import java.util.Map;

import org.unidal.webres.dom.INode;
import org.unidal.webres.dom.ITagNode;
import org.unidal.webres.dom.NodeType;
import org.unidal.webres.dom.TextNode;
import org.unidal.webres.resource.variation.entity.Permutation;
import org.unidal.webres.resource.variation.entity.ResourceMapping;
import org.unidal.webres.resource.variation.entity.ResourceVariation;
import org.unidal.webres.resource.variation.entity.Rule;
import org.unidal.webres.resource.variation.entity.Variation;
import org.unidal.webres.resource.variation.entity.VariationDefinition;
import org.unidal.webres.resource.variation.entity.VariationRef;

public class TagNodeBasedMaker implements IMaker<ITagNode> {

   @Override
   public Permutation buildPermutation(ITagNode node) {
      Permutation permutation = new Permutation();

      return permutation;
   }

   @Override
   public ResourceMapping buildResourceMapping(ITagNode node) {
      String urn = node.getAttribute(ATTR_URN);

      ResourceMapping resourceMapping = new ResourceMapping(urn);

      return resourceMapping;
   }

   @Override
   public ResourceVariation buildResourceVariation(ITagNode node) {
      ResourceVariation resourceVariation = new ResourceVariation();

      return resourceVariation;
   }

   @Override
   public Rule buildRule(ITagNode node) {
      Rule rule = new Rule();

      rule.setText(getText(node));

      Map<String, String> dynamicAttributes = new LinkedHashMap<String, String>(node.getAttributes());

      for (Map.Entry<String, String> e : dynamicAttributes.entrySet()) {
         rule.setDynamicAttribute(e.getKey(), e.getValue());
      }

      return rule;
   }

   @Override
   public Variation buildVariation(ITagNode node) {
      String id = node.getAttribute(ATTR_ID);

      Variation variation = new Variation(id);

      Map<String, String> dynamicAttributes = new LinkedHashMap<String, String>(node.getAttributes());

      dynamicAttributes.remove(ATTR_ID);

      for (Map.Entry<String, String> e : dynamicAttributes.entrySet()) {
         variation.setDynamicAttribute(e.getKey(), e.getValue());
      }

      return variation;
   }

   @Override
   public VariationDefinition buildVariationDefinition(ITagNode node) {
      String type = node.getAttribute(ATTR_TYPE);

      VariationDefinition variationDefinition = new VariationDefinition(type);

      return variationDefinition;
   }

   @Override
   public VariationRef buildVariationRef(ITagNode node) {
      String type = node.getAttribute(ATTR_TYPE);
      String id = node.getAttribute(ATTR_ID);

      VariationRef variationRef = new VariationRef(type);

      if (id != null) {
         variationRef.setId(id);
      }

      return variationRef;
   }

   private String getText(ITagNode node) {
      if (node != null) {
         StringBuilder sb = new StringBuilder();

         for (INode child : node.getChildNodes()) {
            if (child.getNodeType() == NodeType.TEXT) {
               sb.append(((TextNode) child).getNodeValue());
            }
         }

         if (sb.length() != 0) {
            return sb.toString();
         }
      }
      
      return null;
   }
}
