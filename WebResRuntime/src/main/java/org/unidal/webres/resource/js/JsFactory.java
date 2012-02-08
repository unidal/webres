package org.unidal.webres.resource.js;

import java.util.Arrays;
import java.util.List;

import org.unidal.webres.resource.ResourceUrn;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.api.IResourceUrn;

public class JsFactory {
   public static Ref forRef() {
      return Ref.INSTANCE;
   }

   public static enum Ref {
      INSTANCE;

      public IJsRef createAggregatedRef(String path, IJsRef... refs) {
         return new AggregatedJsRef(path, Arrays.asList(refs));
      }

      public IJsRef createAggregatedRef(String path, List<IJsRef> refs) {
         return new AggregatedJsRef(path, refs);
      }

      public IJsRef createInlineRef(String content) {
         return new InlineJsRef(content);
      }

      public IJsRef createInlineRef(String path, String content) {
         return new InlineJsRef(path, content);
      }

      public IJsRef createLocalRef(String path) {
         return createRef(JsNamespace.LOCAL.getName(), path);
      }

      public IJsRef createRef(String namespace, String path) {
         IResourceUrn urn = new ResourceUrn(SystemResourceType.Js.getName(), namespace, path);

         return new JsRef(urn);
      }

      public IJsRef createSharedRef(String path) {
         return createRef(JsNamespace.SHARED.getName(), path);
      }

      public IJsRef createWarRef(String path) {
         return createRef(JsNamespace.WAR.getName(), path);
      }
   }
}
