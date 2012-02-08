package org.unidal.webres.resource.link;

import org.unidal.webres.resource.ResourceUrn;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.ILinkRef;
import org.unidal.webres.resource.api.IResourceUrn;

public class LinkFactory {
   public static Ref forRef() {
      return Ref.INSTANCE;
   }

   public static enum Ref {
      INSTANCE;

      public ILinkRef createPagesRef(String path) {
         return createRef(LinkNamespace.PAGES.getName(), path);
      }

      public ILinkRef createRef(String namespace, String path) {
         IResourceUrn urn = new ResourceUrn(SystemResourceType.Link.getName(), namespace, path);

         return new LinkRef(urn);
      }
   }
}
