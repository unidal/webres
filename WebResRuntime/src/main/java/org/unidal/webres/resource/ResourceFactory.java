package org.unidal.webres.resource;

import org.unidal.webres.resource.api.IResourceRef;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.css.CssFactory;
import org.unidal.webres.resource.dummy.DummyResourceRef;
import org.unidal.webres.resource.dummy.IDummyResourceRef;
import org.unidal.webres.resource.img.ImageFactory;
import org.unidal.webres.resource.js.JsFactory;
import org.unidal.webres.resource.link.LinkFactory;

public class ResourceFactory {
   public static Ref forRef() {
      return Ref.INSTANCE;
   }

   public static enum Ref {
      INSTANCE;

      public IResourceRef<?> createRef(String resourceType, String namespace, String path) {
         SystemResourceType type = SystemResourceType.getByName(resourceType);

         if (type == null) {
            throw new RuntimeException(String.format("Resource type(%s) is not supported!", resourceType));
         }

         switch (type) {
         case Js:
            return JsFactory.forRef().createRef(namespace, path);
         case Css:
            return CssFactory.forRef().createRef(namespace, path);
         case Image:
            return ImageFactory.forRef().createRef(namespace, path);
         case Link:
            return LinkFactory.forRef().createRef(namespace, path);
         default:
            throw new UnsupportedOperationException(String.format(
                  "Internal error: no factory created for resource type(%s)!", resourceType));
         }
      }

      public IResourceRef<?> createRefFromUrn(String urn) {
         IResourceUrn u = ResourceUrn.parse(urn);

         return createRef(u.getResourceTypeName(), u.getNamespace(), u.getPathInfo());
      }

      public IDummyResourceRef getDummyRef() {
         return DummyResourceRef.INSTANCE;

      }
   }
}
