package org.unidal.webres.resource.profile.transform;

import static org.unidal.webres.resource.profile.Constants.ENTITY_COMMON_CSS_SLOTS;
import static org.unidal.webres.resource.profile.Constants.ENTITY_COMMON_CSS_SLOT_REF;
import static org.unidal.webres.resource.profile.Constants.ENTITY_COMMON_JS_SLOTS;
import static org.unidal.webres.resource.profile.Constants.ENTITY_COMMON_JS_SLOT_REF;
import static org.unidal.webres.resource.profile.Constants.ENTITY_CSS;
import static org.unidal.webres.resource.profile.Constants.ENTITY_CSS_SLOT;
import static org.unidal.webres.resource.profile.Constants.ENTITY_CSS_SLOT_GROUP;
import static org.unidal.webres.resource.profile.Constants.ENTITY_CSS_SLOT_REF;
import static org.unidal.webres.resource.profile.Constants.ENTITY_IMG_DATA_URI;
import static org.unidal.webres.resource.profile.Constants.ENTITY_JS;
import static org.unidal.webres.resource.profile.Constants.ENTITY_JS_SLOT;
import static org.unidal.webres.resource.profile.Constants.ENTITY_JS_SLOT_GROUP;
import static org.unidal.webres.resource.profile.Constants.ENTITY_JS_SLOT_REF;
import static org.unidal.webres.resource.profile.Constants.ENTITY_PAGES;
import static org.unidal.webres.resource.profile.Constants.ENTITY_PROFILE;

import java.io.IOException;

import org.unidal.webres.dom.ITagNode;
import org.unidal.webres.resource.profile.entity.CommonCssSlotRef;
import org.unidal.webres.resource.profile.entity.CommonJsSlotRef;
import org.unidal.webres.resource.profile.entity.Css;
import org.unidal.webres.resource.profile.entity.CssSlot;
import org.unidal.webres.resource.profile.entity.CssSlotGroup;
import org.unidal.webres.resource.profile.entity.CssSlotRef;
import org.unidal.webres.resource.profile.entity.ImgDataUri;
import org.unidal.webres.resource.profile.entity.Js;
import org.unidal.webres.resource.profile.entity.JsSlot;
import org.unidal.webres.resource.profile.entity.JsSlotGroup;
import org.unidal.webres.resource.profile.entity.JsSlotRef;
import org.unidal.webres.resource.profile.entity.Page;
import org.unidal.webres.resource.profile.entity.Profile;
import org.unidal.webres.tag.core.TagXmlParser;
import org.xml.sax.SAXException;

public class TagNodeBasedParser implements IParser<ITagNode> {
   public Profile parse(ITagNode node) {
      return parse(new TagNodeBasedMaker(), new DefaultLinker(), node);
   }

   public Profile parse(String xml) throws SAXException, IOException {
      ITagNode doc = new TagXmlParser().parse(xml);
      ITagNode rootNode = doc.getChildTagNode(ENTITY_PROFILE);

      if (rootNode == null) {
         throw new RuntimeException(String.format("profile element(%s) is expected!", ENTITY_PROFILE));
      }

      return new TagNodeBasedParser().parse(new TagNodeBasedMaker(), new DefaultLinker(), rootNode);
   }

   public Profile parse(IMaker<ITagNode> maker, ILinker linker, ITagNode node) {
      Profile profile = maker.buildProfile(node);

      if (node != null) {
         Profile parent = profile;

         for (ITagNode child : node.getGrandchildTagNodes(ENTITY_COMMON_JS_SLOTS)) {
            JsSlot commonJsSlot = maker.buildJsSlot(child);

            if (linker.onCommonJsSlot(parent, commonJsSlot)) {
               parseForJsSlot(maker, linker, commonJsSlot, child);
            }
         }

         for (ITagNode child : node.getGrandchildTagNodes(ENTITY_COMMON_CSS_SLOTS)) {
            CssSlot commonCssSlot = maker.buildCssSlot(child);

            if (linker.onCommonCssSlot(parent, commonCssSlot)) {
               parseForCssSlot(maker, linker, commonCssSlot, child);
            }
         }

         for (ITagNode child : node.getGrandchildTagNodes(ENTITY_PAGES)) {
            Page page = maker.buildPage(child);

            if (linker.onPage(parent, page)) {
               parseForPage(maker, linker, page, child);
            }
         }
      }

      return profile;
   }

   public void parseForCommonCssSlotRef(IMaker<ITagNode> maker, ILinker linker, CommonCssSlotRef parent, ITagNode node) {
   }

   public void parseForCommonJsSlotRef(IMaker<ITagNode> maker, ILinker linker, CommonJsSlotRef parent, ITagNode node) {
   }

   public void parseForCss(IMaker<ITagNode> maker, ILinker linker, Css parent, ITagNode node) {
   }

   public void parseForCssSlot(IMaker<ITagNode> maker, ILinker linker, CssSlot parent, ITagNode node) {
      for (ITagNode child : node.getChildTagNodes(ENTITY_CSS)) {
         Css css = maker.buildCss(child);

         if (linker.onCss(parent, css)) {
            parseForCss(maker, linker, css, child);
         }
      }
   }

   public void parseForCssSlotGroup(IMaker<ITagNode> maker, ILinker linker, CssSlotGroup parent, ITagNode node) {
      for (ITagNode child : node.getChildTagNodes(ENTITY_CSS_SLOT_REF)) {
         CssSlotRef cssSlotRef = maker.buildCssSlotRef(child);

         if (linker.onCssSlotRef(parent, cssSlotRef)) {
            parseForCssSlotRef(maker, linker, cssSlotRef, child);
         }
      }
   }

   public void parseForCssSlotRef(IMaker<ITagNode> maker, ILinker linker, CssSlotRef parent, ITagNode node) {
   }

   public void parseForImgDataUri(IMaker<ITagNode> maker, ILinker linker, ImgDataUri parent, ITagNode node) {
   }

   public void parseForJs(IMaker<ITagNode> maker, ILinker linker, Js parent, ITagNode node) {
   }

   public void parseForJsSlot(IMaker<ITagNode> maker, ILinker linker, JsSlot parent, ITagNode node) {
      for (ITagNode child : node.getChildTagNodes(ENTITY_JS)) {
         Js js = maker.buildJs(child);

         if (linker.onJs(parent, js)) {
            parseForJs(maker, linker, js, child);
         }
      }
   }

   public void parseForJsSlotGroup(IMaker<ITagNode> maker, ILinker linker, JsSlotGroup parent, ITagNode node) {
      for (ITagNode child : node.getChildTagNodes(ENTITY_JS_SLOT_REF)) {
         JsSlotRef jsSlotRef = maker.buildJsSlotRef(child);

         if (linker.onJsSlotRef(parent, jsSlotRef)) {
            parseForJsSlotRef(maker, linker, jsSlotRef, child);
         }
      }
   }

   public void parseForJsSlotRef(IMaker<ITagNode> maker, ILinker linker, JsSlotRef parent, ITagNode node) {
   }

   public void parseForPage(IMaker<ITagNode> maker, ILinker linker, Page parent, ITagNode node) {
      for (ITagNode child : node.getChildTagNodes(ENTITY_JS_SLOT)) {
         JsSlot jsSlot = maker.buildJsSlot(child);

         if (linker.onJsSlot(parent, jsSlot)) {
            parseForJsSlot(maker, linker, jsSlot, child);
         }
      }

      for (ITagNode child : node.getChildTagNodes(ENTITY_CSS_SLOT)) {
         CssSlot cssSlot = maker.buildCssSlot(child);

         if (linker.onCssSlot(parent, cssSlot)) {
            parseForCssSlot(maker, linker, cssSlot, child);
         }
      }

      for (ITagNode child : node.getChildTagNodes(ENTITY_COMMON_JS_SLOT_REF)) {
         CommonJsSlotRef commonJsSlotRef = maker.buildCommonJsSlotRef(child);

         if (linker.onCommonJsSlotRef(parent, commonJsSlotRef)) {
            parseForCommonJsSlotRef(maker, linker, commonJsSlotRef, child);
         }
      }

      for (ITagNode child : node.getChildTagNodes(ENTITY_COMMON_CSS_SLOT_REF)) {
         CommonCssSlotRef commonCssSlotRef = maker.buildCommonCssSlotRef(child);

         if (linker.onCommonCssSlotRef(parent, commonCssSlotRef)) {
            parseForCommonCssSlotRef(maker, linker, commonCssSlotRef, child);
         }
      }

      for (ITagNode child : node.getChildTagNodes(ENTITY_JS_SLOT_GROUP)) {
         JsSlotGroup jsSlotGroup = maker.buildJsSlotGroup(child);

         if (linker.onJsSlotGroup(parent, jsSlotGroup)) {
            parseForJsSlotGroup(maker, linker, jsSlotGroup, child);
         }
      }

      for (ITagNode child : node.getChildTagNodes(ENTITY_CSS_SLOT_GROUP)) {
         CssSlotGroup cssSlotGroup = maker.buildCssSlotGroup(child);

         if (linker.onCssSlotGroup(parent, cssSlotGroup)) {
            parseForCssSlotGroup(maker, linker, cssSlotGroup, child);
         }
      }

      for (ITagNode child : node.getChildTagNodes(ENTITY_IMG_DATA_URI)) {
         ImgDataUri imgDataUri = maker.buildImgDataUri(child);

         if (linker.onImgDataUri(parent, imgDataUri)) {
            parseForImgDataUri(maker, linker, imgDataUri, child);
         }
      }
   }
}
