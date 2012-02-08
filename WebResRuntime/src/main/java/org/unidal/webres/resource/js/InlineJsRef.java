package org.unidal.webres.resource.js;

import org.unidal.webres.resource.ResourceUrn;
import org.unidal.webres.resource.SystemResourceType;

class InlineJsRef extends JsRef {
   private String m_content;

   public InlineJsRef(String content) {
      this(buildMockResourceId(content), content);
   }

   public InlineJsRef(String resourceId, String content) {
      super(new ResourceUrn(SystemResourceType.Js.getName(), JsNamespace.INLINE.getName(), resourceId));
      m_content = content;
   }

   private static String buildMockResourceId(String content) {
      return "/" + Math.abs(content.hashCode());
   }

   public String getContent() {
      return m_content;
   }
}
