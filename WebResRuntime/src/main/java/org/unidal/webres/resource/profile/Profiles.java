package org.unidal.webres.resource.profile;

import static org.unidal.webres.resource.profile.Constants.ENTITY_PROFILE;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.SAXException;

import org.unidal.webres.dom.ITagNode;
import org.unidal.webres.helper.Files;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.profile.entity.Profile;
import org.unidal.webres.resource.profile.transform.CssModelTransformer;
import org.unidal.webres.resource.profile.transform.DefaultLinker;
import org.unidal.webres.resource.profile.transform.DefaultXmlBuilder;
import org.unidal.webres.resource.profile.transform.ImageModelTransformer;
import org.unidal.webres.resource.profile.transform.JsModelTransformer;
import org.unidal.webres.resource.profile.transform.ProfileMerger;
import org.unidal.webres.resource.profile.transform.TagNodeBasedMaker;
import org.unidal.webres.resource.profile.transform.TagNodeBasedParser;
import org.unidal.webres.tag.core.TagXmlParser;

public class Profiles {
   public static ObjectModel forObject() {
      return ObjectModel.INSTANCE;
   }

   public static XmlModel forXml() {
      return XmlModel.INSTANCE;
   }

   public static enum ObjectModel {
      INSTANCE;

      public Root buildModel(Profile profile, SystemResourceType type) {
         switch (type) {
         case Js:
            return new JsModelTransformer().transform(profile);
         case Css:
            return new CssModelTransformer().transform(profile);
         case Image:
            return new ImageModelTransformer().transform(profile);
         default:
            throw new RuntimeException("Unsupported resource type:" + type);
         }
      }

      public Profile clone(Profile profile) {
         Profile cloned = new Profile();
         ProfileMerger merger = new ProfileMerger(cloned);

         profile.accept(merger);
         return cloned;
      }

      public void merge(Profile to, Profile... froms) {
         ProfileMerger merger = new ProfileMerger(to);

         for (Profile from : froms) {
            from.accept(merger);
         }
      }

      public void validate(Profile profile) throws RuntimeException {
         new ProfileValidator().visitProfile(profile);
      }
   }

   public static enum XmlModel {
      INSTANCE;

      public String build(Profile root) {
         DefaultXmlBuilder visitor = new DefaultXmlBuilder();

         root.accept(visitor);
         return visitor.getString();
      }

      public Profile parse(InputStream in, String charset) throws IOException, SAXException {
         String xml = Files.forIO().readFrom(in, charset);

         return parse(xml);
      }

      public Profile parse(String xml) throws SAXException, IOException {
         ITagNode doc = new TagXmlParser().parse(xml);
         TagNodeBasedMaker maker = new TagNodeBasedMaker();
         DefaultLinker linker = new DefaultLinker();
         ITagNode rootNode = doc.getChildTagNode(ENTITY_PROFILE);

         if (rootNode == null) {
            throw new RuntimeException(String.format("Root element(%s) is expected!", ENTITY_PROFILE));
         }

         Profile profile = new TagNodeBasedParser().parse(maker, linker, rootNode);

         return profile;
      }
   }
}
