package org.unidal.webres.resource.css;

import org.unidal.webres.resource.ResourceUrn;
import org.unidal.webres.resource.SystemResourceType;

class InlineCssRef extends CssRef {
   private String m_content;

   public InlineCssRef(String content) {
      this(buildMockResourceId(content), content);
   }

   public InlineCssRef(String resourceId, String content) {
      super(new ResourceUrn(SystemResourceType.Css.getName(), CssNamespace.INLINE.getName(), resourceId));
      m_content = content;
   }

   private static String buildMockResourceId(String content) {
      return "/" + Math.abs(content.hashCode());
   }

   public String getContent() {
      return m_content;
   }
}
