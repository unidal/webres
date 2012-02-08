package org.unidal.webres.resource.template;

import org.unidal.webres.resource.ResourceUrn;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ITemplateRef;

public class TemplateFactory {
   public static Ref forRef() {
      return Ref.INSTANCE;
   }

   public static enum Ref {
      INSTANCE;

      public ITemplateRef createInlineRef(String content) {
         return new InlineTemplateRef(content);
      }

      public ITemplateRef createInlineRef(String path, String content) {
         return new InlineTemplateRef(path, content);
      }

      public ITemplateRef createLocalRef(String path) {
         return createRef(TemplateNamespace.LOCAL.getName(), path);
      }
      
      public ITemplateRef createJavaRef(String path) {
         return createRef(TemplateNamespace.JAVA.getName(), path);
      }

      public ITemplateRef createRef(String namespace, String path) {
         IResourceUrn urn = new ResourceUrn(SystemResourceType.Template.getName(), namespace, path);

         return new TemplateRef(urn);
      }

      public ITemplateRef createSharedRef(String path) {
         return createRef(TemplateNamespace.SHARED.getName(), path);
      }

      public ITemplateRef createWarRef(String path) {
         return createRef(TemplateNamespace.WAR.getName(), path);
      }
   }
}
