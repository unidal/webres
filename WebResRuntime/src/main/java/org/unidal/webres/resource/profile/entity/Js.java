package org.unidal.webres.resource.profile.entity;

import static org.unidal.webres.resource.profile.Constants.ATTR_URN;
import static org.unidal.webres.resource.profile.Constants.ENTITY_JS;

import org.unidal.webres.resource.profile.BaseEntity;
import org.unidal.webres.resource.profile.IVisitor;

public class Js extends BaseEntity<Js> {
   private String m_urn;

   private String m_content;

   public Js(String urn) {
      m_urn = urn;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitJs(this);
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
   public void mergeAttributes(Js other) {
      assertAttributeEquals(other, ENTITY_JS, ATTR_URN, m_urn, other.getUrn());

      if (other.getContent() != null) {
         m_content = other.getContent();
      }
   }

   public Js setContent(String content) {
      m_content=content;
      return this;
   }

}
