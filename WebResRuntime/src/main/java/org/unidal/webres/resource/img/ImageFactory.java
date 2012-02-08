package org.unidal.webres.resource.img;

import org.unidal.webres.resource.ResourceUrn;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IImageRef;
import org.unidal.webres.resource.api.IResourceUrn;

public class ImageFactory {
   public static Ref forRef() {
      return Ref.INSTANCE;
   }

   public static enum Ref {
      INSTANCE;

      public IImageRef createLocalRef(String path) {
         return createRef(ImageNamespace.LOCAL.getName(), path);
      }
      
      public IImageRef createPicsRef(String path) {
         return createRef(ImageNamespace.PICS.getName(), path);
      }

      public IImageRef createSharedRef(String path) {
         return createRef(ImageNamespace.SHARED.getName(), path);
      }

      public IImageRef createRef(String namespace, String path) {
         IResourceUrn urn = new ResourceUrn(SystemResourceType.Image.getName(), namespace, path);

         return new ImageRef(urn);
      }
   }
}
