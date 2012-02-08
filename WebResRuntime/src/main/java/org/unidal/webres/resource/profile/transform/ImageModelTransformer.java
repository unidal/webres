package org.unidal.webres.resource.profile.transform;

import java.util.Stack;

import org.unidal.webres.resource.helper.ResourceHandlings;
import org.unidal.webres.resource.model.Models;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.profile.entity.ImgDataUri;
import org.unidal.webres.resource.profile.entity.Page;
import org.unidal.webres.resource.profile.entity.Profile;

public class ImageModelTransformer extends EmptyVisitor {
   private Stack<Object> m_profileStack = new Stack<Object>();

   private Stack<Object> m_modelStack = new Stack<Object>();

   private Root m_root;

   public Root transform(Profile profile) {
      m_root = new Root();
      profile.accept(this);

      return m_root;
   }

   @Override
   public void visitImgDataUri(ImgDataUri imgDataUri) {
      org.unidal.webres.resource.model.entity.Page page = (org.unidal.webres.resource.model.entity.Page) m_modelStack
            .peek();
      String urn = imgDataUri.getUrn();

      // convert to EL to URN
      if (ResourceHandlings.forEL().isEL(urn)) {
         if (ResourceHandlings.forEL().isResourceEL(urn, "res.img.")) {
            urn = ResourceHandlings.forEL().toUrn(urn);
         } else {
            throw new RuntimeException(String.format("Invalid EL(%s) found in resource profile, "
                  + "it should look like ${res.img.<namespace>.<resource-id>}!", urn));
         }
      }

      Models.forImage().enableDataUri(page, urn);
   }

   @Override
   public void visitPage(Page page) {
      Root root = (Root) m_modelStack.peek();
      org.unidal.webres.resource.model.entity.Page p = new org.unidal.webres.resource.model.entity.Page(page.getId());

      root.addPage(p);

      m_profileStack.push(page);
      m_modelStack.push(p);

      for (ImgDataUri imgDataUri : page.getImgDataUris()) {
         visitImgDataUri(imgDataUri);
      }

      m_profileStack.pop();
      m_modelStack.pop();
   }

   @Override
   public void visitProfile(Profile profile) {
      m_profileStack.push(profile);
      m_modelStack.push(m_root);

      for (Page page : profile.getPages()) {
         visitPage(page);
      }

      m_profileStack.pop();
      m_modelStack.pop();
   }
}
