package org.unidal.webres.resource.variation;

import static org.unidal.webres.resource.variation.Constants.ENTITY_RESOURCE_VARIATION;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.SAXException;

import org.unidal.webres.dom.ITagNode;
import org.unidal.webres.helper.Files;
import org.unidal.webres.resource.variation.entity.ResourceVariation;
import org.unidal.webres.resource.variation.transform.DefaultLinker;
import org.unidal.webres.resource.variation.transform.DefaultMerger;
import org.unidal.webres.resource.variation.transform.DefaultValidator;
import org.unidal.webres.resource.variation.transform.DefaultXmlBuilder;
import org.unidal.webres.resource.variation.transform.TagNodeBasedMaker;
import org.unidal.webres.resource.variation.transform.TagNodeBasedParser;
import org.unidal.webres.tag.core.TagXmlParser;

public class ResourceVariations {
   public static ObjectModel forObject() {
      return ObjectModel.INSTANCE;
   }

   public static XmlModel forXml() {
      return XmlModel.INSTANCE;
   }

   public static enum ObjectModel {
      INSTANCE;

      public ResourceVariation clone(ResourceVariation from) {
         ResourceVariation cloned = new ResourceVariation();
         DefaultMerger merger = new DefaultMerger(cloned);

         from.accept(merger);
         return cloned;
      }

      public void merge(ResourceVariation to, ResourceVariation... froms) {
         DefaultMerger merger = new DefaultMerger(to);

         for (ResourceVariation from : froms) {
            from.accept(merger);
         }
      }

      public void validate(ResourceVariation resourceVariation) {
         new DefaultValidator().visitResourceVariation(resourceVariation);
      }

   }

   public static enum XmlModel {
      INSTANCE;

      public String build(ResourceVariation ResourceVariation) {
         DefaultXmlBuilder visitor = new DefaultXmlBuilder();

         ResourceVariation.accept(visitor);
         return visitor.getString();
      }

      public ResourceVariation parse(InputStream in, String charset) throws IOException, SAXException {
         String xml = Files.forIO().readFrom(in, charset);

         return parse(xml);
      }

      public ResourceVariation parse(String xml) throws SAXException, IOException {
         ITagNode doc = new TagXmlParser().parse(xml);
         TagNodeBasedMaker maker = new TagNodeBasedMaker();
         DefaultLinker linker = new DefaultLinker();
         ITagNode ResourceVariationNode = doc.getChildTagNode(ENTITY_RESOURCE_VARIATION);

         if (ResourceVariationNode == null) {
            throw new RuntimeException(String.format("ResourceVariation element(%s) is expected!",
                  ENTITY_RESOURCE_VARIATION));
         }

         ResourceVariation ResourceVariation = new TagNodeBasedParser().parse(maker, linker, ResourceVariationNode);

         return ResourceVariation;
      }
   }
}
