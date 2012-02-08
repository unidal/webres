package org.unidal.webres.resource.template;

import static org.unidal.webres.resource.SystemResourceType.Template;

import org.unidal.webres.resource.ResourceUrn;

class InlineTemplateRef extends TemplateRef {
   private String m_content;

   public InlineTemplateRef(String content) {
      this(buildMockResourceId(content), content);
   }

   public InlineTemplateRef(String resourceId, String content) {
      super(new ResourceUrn(Template.getName(), TemplateNamespace.INLINE.getName(), resourceId));
      m_content = content;
   }

   private static String buildMockResourceId(String content) {
      return "/" + Math.abs(content.hashCode());
   }

   public String getContent() {
      return m_content;
   }
}
