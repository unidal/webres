package org.unidal.webres.helper;

import java.net.URI;
import java.net.URISyntaxException;

public class Uris {
   public static UriResolver resolver() {
      return UriResolver.INSTANCE;
   }

   public static enum UriResolver {
      INSTANCE;

      public URI resolve(URI base, URI uri) {
         if (base == null) {
            return uri;
         }

         String path = uri.getPath();
         boolean absPath = path != null && path.startsWith("/");

         if (absPath) {
            if (uri.getScheme() != null) { // uri is absolute
               return uri;
            } else if (base.getScheme() != null) {
               return base.resolve(uri);
            }
         } else {
            if (uri.getScheme() != null) {
               String b = base.toString();
               String u = uri.toString();
               int pos1 = b.indexOf(':');
               int pos2 = u.indexOf(':');
               URI newBase = base;

               try {
                  newBase = new URI(uri.getScheme() + ":" + b.substring(pos1 + 1));
               } catch (URISyntaxException e) {
                  // should not happen
               }

               return newBase.resolve(u.substring(pos2 + 1));
            } else if (base.getScheme() != null) {
               return base.resolve(uri);
            }
         }

         return base.resolve(uri);
      }
   }
}
