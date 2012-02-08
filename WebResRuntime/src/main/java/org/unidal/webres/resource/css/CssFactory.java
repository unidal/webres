package org.unidal.webres.resource.css;

import java.util.Arrays;
import java.util.List;

import org.unidal.webres.resource.ResourceUrn;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.resource.api.IResourceUrn;

public class CssFactory {
   public static Ref forRef() {
      return Ref.INSTANCE;
   }

   public static enum Ref {
      INSTANCE;

      public ICssRef createAggregatedRef(String path, ICssRef... refs) {
         return new AggregatedCssRef(path, Arrays.asList(refs));
      }

      public ICssRef createAggregatedRef(String path, List<ICssRef> refs) {
         return new AggregatedCssRef(path, refs);
      }

      public ICssRef createInlineRef(String content) {
         return new InlineCssRef(content);
      }

      public ICssRef createInlineRef(String path, String content) {
         return new InlineCssRef(path, content);
      }

      public ICssRef createLocalRef(String path) {
         return createRef(CssNamespace.LOCAL.getName(), path);
      }

      public ICssRef createRef(String namespace, String path) {
         IResourceUrn urn = new ResourceUrn(SystemResourceType.Css.getName(), namespace, path);

         return new CssRef(urn);
      }

      public ICssRef createSharedRef(String path) {
         return createRef(CssNamespace.SHARED.getName(), path);
      }
   }
}
