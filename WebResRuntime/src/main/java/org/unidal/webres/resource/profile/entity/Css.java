package org.unidal.webres.resource.profile.entity;

import static org.unidal.webres.resource.profile.Constants.ATTR_URN;
import static org.unidal.webres.resource.profile.Constants.ENTITY_CSS;

import org.unidal.webres.resource.profile.BaseEntity;
import org.unidal.webres.resource.profile.IVisitor;

public class Css extends BaseEntity<Css> {
   private String m_urn;

   private String m_content;

   public Css(String urn) {
      m_urn = urn;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitCss(this);
   }

   public String getContent() {
      return m_content;
   }

   public String getUrn() {
      return m_urn;
   }

   public boolean hasText() {
      return m_content != null;
   }

   @Override
   public void mergeAttributes(Css other) {
      assertAttributeEquals(other, ENTITY_CSS, ATTR_URN, m_urn, other.getUrn());

      if (other.getContent() != null) {
         m_content = other.getContent();
      }
   }

   public Css setContent(String content) {
      m_content=content;
      return this;
   }

}
