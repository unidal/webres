package org.unidal.webres.tag.link;

import java.util.Collections;
import java.util.Map;

public class Links {
   public static Html forHtml() {
      return Html.INSTANCE;
   }

   public static enum Html {
      INSTANCE;

      public String buildLink(String url, Map<String, Object> attributes, String bodyContent) {
         StringBuilder sb = new StringBuilder(128);

         sb.append("<a");

         if (attributes == null) {
            attributes = Collections.emptyMap();
         }

         if (url != null) {
            sb.append(" href=\"").append(url).append('"');
         }

         for (Map.Entry<String, Object> e : attributes.entrySet()) {
            String key = e.getKey();

            if (!"href".equalsIgnoreCase(key)) {
               sb.append(' ').append(key).append("=\"").append(e.getValue()).append('"');
            }
         }

         sb.append(">").append(bodyContent).append("</a>");

         return sb.toString();
      }
   }
}
