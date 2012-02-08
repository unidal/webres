package org.unidal.webres.resource.profile.transform;

import java.util.Stack;

import org.unidal.webres.resource.css.CssFactory;
import org.unidal.webres.resource.helper.ResourceHandlings;
import org.unidal.webres.resource.model.entity.CommonSlotRef;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.model.entity.SlotGroup;
import org.unidal.webres.resource.model.entity.SlotRef;
import org.unidal.webres.resource.profile.entity.CommonCssSlotRef;
import org.unidal.webres.resource.profile.entity.Css;
import org.unidal.webres.resource.profile.entity.CssSlot;
import org.unidal.webres.resource.profile.entity.CssSlotGroup;
import org.unidal.webres.resource.profile.entity.CssSlotRef;
import org.unidal.webres.resource.profile.entity.Page;
import org.unidal.webres.resource.profile.entity.Profile;

public class CssModelTransformer extends EmptyVisitor {
   private Stack<Object> m_profileStack = new Stack<Object>();

   private Stack<Object> m_modelStack = new Stack<Object>();

   private Root m_root;

   public Root transform(Profile profile) {
      m_root = new Root();
      profile.accept(this);

      return m_root;
   }

   @Override
   public void visitProfile(Profile profile) {
      m_profileStack.push(profile);
      m_modelStack.push(m_root);

      for (CssSlot slot : profile.getCommonCssSlots()) {
         visitCssSlot(slot);
      }

      for (Page page : profile.getPages()) {
         visitPage(page);
      }

      m_profileStack.pop();
      m_modelStack.pop();
   }

   @Override
   public void visitCommonCssSlotRef(CommonCssSlotRef ref) {
      org.unidal.webres.resource.model.entity.Page page = (org.unidal.webres.resource.model.entity.Page) m_modelStack.peek();
      CommonSlotRef r = new CommonSlotRef(ref.getId());

      r.setBeforeSlot(ref.getBeforeSlot());
      r.setAfterSlot(ref.getAfterSlot());

      page.addCommonSlotRef(r);
   }

   @Override
   public void visitCss(Css css) {
      Slot slot = (Slot) m_modelStack.peek();
      String urn = css.getUrn();

      // convert to EL to URN
      if (ResourceHandlings.forEL().isEL(urn)) {
         if (ResourceHandlings.forEL().isResourceEL(urn, "res.css.")) {
            urn = ResourceHandlings.forEL().toUrn(urn);
         } else {
            throw new RuntimeException(String.format("Invalid EL(%s) found in resource profile, "
                  + "it should look like ${res.css.<namespace>.<resource-id>}!", urn));
         }
      }

      Resource resource = new Resource(urn);

      if (css.hasText()) {
         resource.setReference(CssFactory.forRef().createInlineRef(css.getContent()));
         slot.addResource(resource);
      } else {
         Resource r = slot.findResource(urn);

         if (r == null) {
            slot.addResource(resource);
         }
      }
   }

   @Override
   public void visitCssSlot(CssSlot slot) {
      Object parent = m_profileStack.peek();
      Slot s = null;

      if (parent instanceof Profile) { // common-css-slot
         Root root = (Root) m_modelStack.peek();

         s = new Slot(slot.getId());
         root.addCommonSlot(s);
      } else if (parent instanceof Page) {
         org.unidal.webres.resource.model.entity.Page page = (org.unidal.webres.resource.model.entity.Page) m_modelStack.peek();

         s = new Slot(slot.getId());
         page.addSlot(s);
      }

      if (s != null) {
         m_profileStack.push(slot);
         m_modelStack.push(s);

         for (Css css : slot.getCssList()) {
            visitCss(css);
         }

         m_profileStack.pop();
         m_modelStack.pop();
      }
   }

   @Override
   public void visitCssSlotGroup(CssSlotGroup group) {
      org.unidal.webres.resource.model.entity.Page page = (org.unidal.webres.resource.model.entity.Page) m_modelStack.peek();
      SlotGroup g = new SlotGroup(group.getId()).setMainSlot(group.getMainSlot());

      page.addSlotGroup(g);

      m_profileStack.push(group);
      m_modelStack.push(g);

      for (CssSlotRef ref : group.getCssSlotRefs()) {
         visitCssSlotRef(ref);
      }

      m_profileStack.pop();
      m_modelStack.pop();
   }

   @Override
   public void visitCssSlotRef(CssSlotRef ref) {
      SlotGroup group = (SlotGroup) m_modelStack.peek();
      SlotRef r = new SlotRef(ref.getId());

      group.addSlotRef(r);
   }

   @Override
   public void visitPage(Page page) {
      Root root = (Root) m_modelStack.peek();
      org.unidal.webres.resource.model.entity.Page p = new org.unidal.webres.resource.model.entity.Page(page.getId());

      root.addPage(p);

      m_profileStack.push(page);
      m_modelStack.push(p);

      for (CssSlot slot : page.getCssSlots()) {
         visitCssSlot(slot);
      }

      for (CommonCssSlotRef ref : page.getCommonCssSlotRefs()) {
         visitCommonCssSlotRef(ref);
      }

      for (CssSlotGroup slot : page.getCssSlotGroups()) {
         visitCssSlotGroup(slot);
      }

      m_profileStack.pop();
      m_modelStack.pop();
   }
}
