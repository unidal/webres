package org.unidal.webres.resource.model;

import static org.unidal.webres.resource.model.Constants.ENTITY_ROOT;

import java.io.IOException;
import java.io.InputStream;

import org.unidal.helper.Files;
import org.unidal.webres.dom.ITagNode;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.model.transform.DefaultLinker;
import org.unidal.webres.resource.model.transform.DefaultValidator;
import org.unidal.webres.resource.model.transform.DefaultXmlBuilder;
import org.unidal.webres.resource.model.transform.ModelMerger;
import org.unidal.webres.resource.model.transform.TagNodeBasedMaker;
import org.unidal.webres.resource.model.transform.TagNodeBasedParser;
import org.unidal.webres.tag.core.TagXmlParser;
import org.xml.sax.SAXException;

public class Models {
   public static ImageTypeModel forImage() {
      return ImageTypeModel.INSTANCE;
   }

   public static ObjectModel forObject() {
      return ObjectModel.INSTANCE;
   }

   public static XmlModel forXml() {
      return XmlModel.INSTANCE;
   }

   public static enum ImageTypeModel {
      INSTANCE;

      private static final String SLOT_DATA_URI = "data-uri"; // virtual slot

      public void disableDataUri(Page page, String urn) {
         Slot slot = page.findSlot(SLOT_DATA_URI);

         if (slot != null) {
            slot.removeResource(urn);
         }
      }

      public void enableDataUri(Page page, String urn) {
         Resource resource = new Resource(urn);
         Slot slot = page.findSlot(SLOT_DATA_URI);

         if (slot == null) {
            slot = new Slot(SLOT_DATA_URI);
            page.addSlot(slot);
         }

         Resource r = slot.findResource(urn);

         if (r == null) {
            slot.addResource(resource);
         }
      }

      public boolean isDateUriEnabled(Page page, String urn) {
         if (page != null) {
            Slot slot = page.findSlot(SLOT_DATA_URI);

            if (slot != null) {
               Resource resource = slot.findResource(urn);

               return resource != null;
            }
         }

         return false;
      }
   }

   public static enum ObjectModel {
      INSTANCE;

      public Root clone(Root from) {
         Root cloned = new Root();
         ModelMerger merger = new ModelMerger(cloned);

         from.accept(merger);
         return cloned;
      }

      public Page ensurePage(Root root, String requestUri) {
         Page page = root.findPage(requestUri);

         if (page == null) {
            page = new Page(requestUri);
            root.addPage(page);
         }

         return page;
      }

      public void merge(Root to, Root... froms) {
         ModelMerger merger = new ModelMerger(to);

         for (Root from : froms) {
            from.accept(merger);
         }
      }

      public void validate(Root root) {
         new DefaultValidator().visitRoot(root);
      }

   }

   public static enum XmlModel {
      INSTANCE;

      public String build(Root root) {
         DefaultXmlBuilder visitor = new DefaultXmlBuilder();

         root.accept(visitor);
         return visitor.getString();
      }

      public Root parse(InputStream in, String charset) throws IOException, SAXException {
         String xml = Files.forIO().readFrom(in, charset);

         return parse(xml);
      }

      public Root parse(String xml) throws SAXException, IOException {
         ITagNode doc = new TagXmlParser().parse(xml);
         TagNodeBasedMaker maker = new TagNodeBasedMaker();
         DefaultLinker linker = new DefaultLinker();
         ITagNode rootNode = doc.getChildTagNode(ENTITY_ROOT);

         if (rootNode == null) {
            throw new RuntimeException(String.format("Root element(%s) is expected!", ENTITY_ROOT));
         }

         Root root = new TagNodeBasedParser().parse(maker, linker, rootNode);

         return root;
      }
   }
}
