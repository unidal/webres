package org.unidal.webres.resource.profile.transform;

import static org.unidal.webres.resource.profile.Constants.ATTR_AFTER_SLOT;
import static org.unidal.webres.resource.profile.Constants.ATTR_BEFORE_SLOT;
import static org.unidal.webres.resource.profile.Constants.ATTR_ID;
import static org.unidal.webres.resource.profile.Constants.ATTR_MAIN_SLOT;
import static org.unidal.webres.resource.profile.Constants.ATTR_OVERRIDE;
import static org.unidal.webres.resource.profile.Constants.ATTR_URN;

import org.unidal.webres.dom.INode;
import org.unidal.webres.dom.ITagNode;
import org.unidal.webres.dom.NodeType;
import org.unidal.webres.dom.TextNode;
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

public class TagNodeBasedMaker implements IMaker<ITagNode> {

   @Override
   public CommonCssSlotRef buildCommonCssSlotRef(ITagNode node) {
      String id = node.getAttribute(ATTR_ID);
      String beforeSlot = node.getAttribute(ATTR_BEFORE_SLOT);
      String afterSlot = node.getAttribute(ATTR_AFTER_SLOT);

      CommonCssSlotRef commonCssSlotRef = new CommonCssSlotRef(id);

      if (beforeSlot != null) {
         commonCssSlotRef.setBeforeSlot(beforeSlot);
      }

      if (afterSlot != null) {
         commonCssSlotRef.setAfterSlot(afterSlot);
      }

      return commonCssSlotRef;
   }

   @Override
   public CommonJsSlotRef buildCommonJsSlotRef(ITagNode node) {
      String id = node.getAttribute(ATTR_ID);
      String beforeSlot = node.getAttribute(ATTR_BEFORE_SLOT);
      String afterSlot = node.getAttribute(ATTR_AFTER_SLOT);

      CommonJsSlotRef commonJsSlotRef = new CommonJsSlotRef(id);

      if (beforeSlot != null) {
         commonJsSlotRef.setBeforeSlot(beforeSlot);
      }

      if (afterSlot != null) {
         commonJsSlotRef.setAfterSlot(afterSlot);
      }

      return commonJsSlotRef;
   }

   @Override
   public Css buildCss(ITagNode node) {
      String urn = node.getAttribute(ATTR_URN);

      Css css = new Css(urn);

      css.setContent(getText(node));

      return css;
   }

   @Override
   public CssSlot buildCssSlot(ITagNode node) {
      String id = node.getAttribute(ATTR_ID);
      String override = node.getAttribute(ATTR_OVERRIDE);

      CssSlot cssSlot = new CssSlot(id);

      if (override != null) {
         cssSlot.setOverride(Boolean.valueOf(override));
      }

      return cssSlot;
   }

   @Override
   public CssSlotGroup buildCssSlotGroup(ITagNode node) {
      String id = node.getAttribute(ATTR_ID);
      String mainSlot = node.getAttribute(ATTR_MAIN_SLOT);

      CssSlotGroup cssSlotGroup = new CssSlotGroup(id);

      if (mainSlot != null) {
         cssSlotGroup.setMainSlot(mainSlot);
      }

      return cssSlotGroup;
   }

   @Override
   public CssSlotRef buildCssSlotRef(ITagNode node) {
      String id = node.getAttribute(ATTR_ID);

      CssSlotRef cssSlotRef = new CssSlotRef(id);

      return cssSlotRef;
   }

   @Override
   public ImgDataUri buildImgDataUri(ITagNode node) {
      String urn = node.getAttribute(ATTR_URN);

      ImgDataUri imgDataUri = new ImgDataUri(urn);

      return imgDataUri;
   }

   @Override
   public Js buildJs(ITagNode node) {
      String urn = node.getAttribute(ATTR_URN);

      Js js = new Js(urn);

      js.setContent(getText(node));

      return js;
   }

   @Override
   public JsSlot buildJsSlot(ITagNode node) {
      String id = node.getAttribute(ATTR_ID);
      String override = node.getAttribute(ATTR_OVERRIDE);

      JsSlot jsSlot = new JsSlot(id);

      if (override != null) {
         jsSlot.setOverride(Boolean.valueOf(override));
      }

      return jsSlot;
   }

   @Override
   public JsSlotGroup buildJsSlotGroup(ITagNode node) {
      String id = node.getAttribute(ATTR_ID);
      String mainSlot = node.getAttribute(ATTR_MAIN_SLOT);

      JsSlotGroup jsSlotGroup = new JsSlotGroup(id);

      if (mainSlot != null) {
         jsSlotGroup.setMainSlot(mainSlot);
      }

      return jsSlotGroup;
   }

   @Override
   public JsSlotRef buildJsSlotRef(ITagNode node) {
      String id = node.getAttribute(ATTR_ID);

      JsSlotRef jsSlotRef = new JsSlotRef(id);

      return jsSlotRef;
   }

   @Override
   public Page buildPage(ITagNode node) {
      String id = node.getAttribute(ATTR_ID);

      Page page = new Page(id);

      return page;
   }

   @Override
   public Profile buildProfile(ITagNode node) {
      Profile profile = new Profile();

      return profile;
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
