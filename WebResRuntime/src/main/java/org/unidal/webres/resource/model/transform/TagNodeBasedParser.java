package org.unidal.webres.resource.model.transform;

import static org.unidal.webres.resource.model.Constants.ENTITY_COMMON_SLOTS;
import static org.unidal.webres.resource.model.Constants.ENTITY_COMMON_SLOT_REF;
import static org.unidal.webres.resource.model.Constants.ENTITY_PAGES;
import static org.unidal.webres.resource.model.Constants.ENTITY_RESOURCE;
import static org.unidal.webres.resource.model.Constants.ENTITY_ROOT;
import static org.unidal.webres.resource.model.Constants.ENTITY_SLOT;
import static org.unidal.webres.resource.model.Constants.ENTITY_SLOT_GROUP;
import static org.unidal.webres.resource.model.Constants.ENTITY_SLOT_REF;

import java.io.IOException;

import org.unidal.webres.dom.ITagNode;
import org.unidal.webres.resource.model.entity.CommonSlotRef;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.model.entity.SlotGroup;
import org.unidal.webres.resource.model.entity.SlotRef;
import org.unidal.webres.tag.core.TagXmlParser;
import org.xml.sax.SAXException;

public class TagNodeBasedParser implements IParser<ITagNode> {
   public Root parse(ITagNode node) {
      return parse(new TagNodeBasedMaker(), new DefaultLinker(), node);
   }

   public Root parse(String xml) throws SAXException, IOException {
      ITagNode doc = new TagXmlParser().parse(xml);
      ITagNode rootNode = doc.getChildTagNode(ENTITY_ROOT);

      if (rootNode == null) {
         throw new RuntimeException(String.format("root element(%s) is expected!", ENTITY_ROOT));
      }

      return new TagNodeBasedParser().parse(new TagNodeBasedMaker(), new DefaultLinker(), rootNode);
   }

   public Root parse(IMaker<ITagNode> maker, ILinker linker, ITagNode node) {
      Root root = maker.buildRoot(node);

      if (node != null) {
         Root parent = root;

         for (ITagNode child : node.getGrandchildTagNodes(ENTITY_COMMON_SLOTS)) {
            Slot commonSlot = maker.buildSlot(child);

            if (linker.onCommonSlot(parent, commonSlot)) {
               parseForSlot(maker, linker, commonSlot, child);
            }
         }

         for (ITagNode child : node.getGrandchildTagNodes(ENTITY_PAGES)) {
            Page page = maker.buildPage(child);

            if (linker.onPage(parent, page)) {
               parseForPage(maker, linker, page, child);
            }
         }
      }

      return root;
   }

   public void parseForCommonSlotRef(IMaker<ITagNode> maker, ILinker linker, CommonSlotRef parent, ITagNode node) {
   }

   public void parseForPage(IMaker<ITagNode> maker, ILinker linker, Page parent, ITagNode node) {
      for (ITagNode child : node.getChildTagNodes(ENTITY_SLOT)) {
         Slot slot = maker.buildSlot(child);

         if (linker.onSlot(parent, slot)) {
            parseForSlot(maker, linker, slot, child);
         }
      }

      for (ITagNode child : node.getChildTagNodes(ENTITY_COMMON_SLOT_REF)) {
         CommonSlotRef commonSlotRef = maker.buildCommonSlotRef(child);

         if (linker.onCommonSlotRef(parent, commonSlotRef)) {
            parseForCommonSlotRef(maker, linker, commonSlotRef, child);
         }
      }

      for (ITagNode child : node.getChildTagNodes(ENTITY_SLOT_GROUP)) {
         SlotGroup slotGroup = maker.buildSlotGroup(child);

         if (linker.onSlotGroup(parent, slotGroup)) {
            parseForSlotGroup(maker, linker, slotGroup, child);
         }
      }
   }

   public void parseForResource(IMaker<ITagNode> maker, ILinker linker, Resource parent, ITagNode node) {
   }

   public void parseForSlot(IMaker<ITagNode> maker, ILinker linker, Slot parent, ITagNode node) {
      for (ITagNode child : node.getChildTagNodes(ENTITY_RESOURCE)) {
         Resource resource = maker.buildResource(child);

         if (linker.onResource(parent, resource)) {
            parseForResource(maker, linker, resource, child);
         }
      }
   }

   public void parseForSlotGroup(IMaker<ITagNode> maker, ILinker linker, SlotGroup parent, ITagNode node) {
      for (ITagNode child : node.getChildTagNodes(ENTITY_SLOT_REF)) {
         SlotRef slotRef = maker.buildSlotRef(child);

         if (linker.onSlotRef(parent, slotRef)) {
            parseForSlotRef(maker, linker, slotRef, child);
         }
      }
   }

   public void parseForSlotRef(IMaker<ITagNode> maker, ILinker linker, SlotRef parent, ITagNode node) {
   }
}
