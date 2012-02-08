package org.unidal.webres.resource.profile.entity;

import static org.unidal.webres.resource.profile.Constants.ATTR_URN;
import static org.unidal.webres.resource.profile.Constants.ENTITY_IMG_DATA_URI;

import org.unidal.webres.resource.profile.BaseEntity;
import org.unidal.webres.resource.profile.IVisitor;

public class ImgDataUri extends BaseEntity<ImgDataUri> {
   private String m_urn;

   public ImgDataUri(String urn) {
      m_urn = urn;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitImgDataUri(this);
   }

   public String getUrn() {
      return m_urn;
   }

   @Override
   public void mergeAttributes(ImgDataUri other) {
      assertAttributeEquals(other, ENTITY_IMG_DATA_URI, ATTR_URN, m_urn, other.getUrn());

   }

}
