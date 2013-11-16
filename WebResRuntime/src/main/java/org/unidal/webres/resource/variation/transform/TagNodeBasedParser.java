package org.unidal.webres.resource.variation.transform;

import static org.unidal.webres.resource.variation.Constants.ENTITY_PERMUTATION;
import static org.unidal.webres.resource.variation.Constants.ENTITY_RESOURCE_MAPPINGS;
import static org.unidal.webres.resource.variation.Constants.ENTITY_RESOURCE_VARIATION;
import static org.unidal.webres.resource.variation.Constants.ENTITY_RULE;
import static org.unidal.webres.resource.variation.Constants.ENTITY_VARIATION;
import static org.unidal.webres.resource.variation.Constants.ENTITY_VARIATION_DEFINITIONS;
import static org.unidal.webres.resource.variation.Constants.ENTITY_VARIATION_REF;

import java.io.IOException;

import org.unidal.webres.dom.ITagNode;
import org.unidal.webres.resource.variation.entity.Permutation;
import org.unidal.webres.resource.variation.entity.ResourceMapping;
import org.unidal.webres.resource.variation.entity.ResourceVariation;
import org.unidal.webres.resource.variation.entity.Rule;
import org.unidal.webres.resource.variation.entity.Variation;
import org.unidal.webres.resource.variation.entity.VariationDefinition;
import org.unidal.webres.resource.variation.entity.VariationRef;
import org.unidal.webres.tag.core.TagXmlParser;
import org.xml.sax.SAXException;

public class TagNodeBasedParser implements IParser<ITagNode> {
   public ResourceVariation parse(ITagNode node) {
      return parse(new TagNodeBasedMaker(), new DefaultLinker(), node);
   }

   public ResourceVariation parse(String xml) throws SAXException, IOException {
      ITagNode doc = new TagXmlParser().parse(xml);
      ITagNode rootNode = doc.getChildTagNode(ENTITY_RESOURCE_VARIATION);

      if (rootNode == null) {
         throw new RuntimeException(String.format("resource-variation element(%s) is expected!", ENTITY_RESOURCE_VARIATION));
      }

      return new TagNodeBasedParser().parse(new TagNodeBasedMaker(), new DefaultLinker(), rootNode);
   }

   public ResourceVariation parse(IMaker<ITagNode> maker, ILinker linker, ITagNode node) {
      ResourceVariation resourceVariation = maker.buildResourceVariation(node);

      if (node != null) {
         ResourceVariation parent = resourceVariation;

         for (ITagNode child : node.getGrandchildTagNodes(ENTITY_VARIATION_DEFINITIONS)) {
            VariationDefinition variationDefinition = maker.buildVariationDefinition(child);

            if (linker.onVariationDefinition(parent, variationDefinition)) {
               parseForVariationDefinition(maker, linker, variationDefinition, child);
            }
         }

         ITagNode permutationNode = node.getChildTagNode(ENTITY_PERMUTATION);

         if (permutationNode != null) {
            Permutation permutation = maker.buildPermutation(permutationNode);

            if (linker.onPermutation(parent, permutation)) {
               parseForPermutation(maker, linker, permutation, permutationNode);
            }
         }

         for (ITagNode child : node.getGrandchildTagNodes(ENTITY_RESOURCE_MAPPINGS)) {
            ResourceMapping resourceMapping = maker.buildResourceMapping(child);

            if (linker.onResourceMapping(parent, resourceMapping)) {
               parseForResourceMapping(maker, linker, resourceMapping, child);
            }
         }
      }

      return resourceVariation;
   }

   public void parseForPermutation(IMaker<ITagNode> maker, ILinker linker, Permutation parent, ITagNode node) {
      for (ITagNode child : node.getChildTagNodes(ENTITY_VARIATION_REF)) {
         VariationRef variationRef = maker.buildVariationRef(child);

         if (linker.onVariationRef(parent, variationRef)) {
            parseForVariationRef(maker, linker, variationRef, child);
         }
      }
   }

   public void parseForResourceMapping(IMaker<ITagNode> maker, ILinker linker, ResourceMapping parent, ITagNode node) {
      for (ITagNode child : node.getChildTagNodes(ENTITY_RULE)) {
         Rule rule = maker.buildRule(child);

         if (linker.onRule(parent, rule)) {
            parseForRule(maker, linker, rule, child);
         }
      }
   }

   public void parseForRule(IMaker<ITagNode> maker, ILinker linker, Rule parent, ITagNode node) {
   }

   public void parseForVariation(IMaker<ITagNode> maker, ILinker linker, Variation parent, ITagNode node) {
   }

   public void parseForVariationDefinition(IMaker<ITagNode> maker, ILinker linker, VariationDefinition parent, ITagNode node) {
      for (ITagNode child : node.getChildTagNodes(ENTITY_VARIATION)) {
         Variation variation = maker.buildVariation(child);

         if (linker.onVariation(parent, variation)) {
            parseForVariation(maker, linker, variation, child);
         }
      }
   }

   public void parseForVariationRef(IMaker<ITagNode> maker, ILinker linker, VariationRef parent, ITagNode node) {
   }
}
