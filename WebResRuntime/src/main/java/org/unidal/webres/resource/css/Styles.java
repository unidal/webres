package org.unidal.webres.resource.css;

import java.util.Collections;
import java.util.Map;

public class Styles {
   public static Html forHtml() {
      return Html.INSTANCE;
   }

   public static enum Html {
      INSTANCE;

      public String buildLink(String url, Map<String, Object> attributes, boolean isXml) {
         StringBuilder sb = new StringBuilder(url == null ? 0 : url.length() + 64);

         if (url != null) {
            sb.append("<link");

            if (attributes == null) {
               attributes = Collections.emptyMap();
            }

            Object type = attributes.get("type");

            sb.append(" href=\"").append(url).append("\"");

            if (type == null) {
               sb.append(" type=\"text/css\"");
            }

            Object rel = attributes.get("rel");
            if (rel == null) {
               sb.append(" rel=\"stylesheet\"");
            }

            for (Map.Entry<String, Object> e : attributes.entrySet()) {
               String key = e.getKey();

               if (!"href".equalsIgnoreCase(key)) {
                  sb.append(' ').append(key).append("=\"").append(e.getValue()).append('"');
               }
            }

            if (isXml) {
               sb.append("/>");
            } else { //xhtml case
               sb.append(">");
            }
         }

         return sb.toString();
      }

      public String buildStyle(String text, Map<String, Object> attributes) {
         StringBuilder sb = new StringBuilder(text == null ? 0 : text.length() + 64);
         if (text != null) {
            sb.append("<style");

            if (attributes == null) {
               attributes = Collections.emptyMap();
            }

            Object type = attributes.get("type");

            if (type == null) {
               sb.append(" type=\"text/css\"");
            }

            for (Map.Entry<String, Object> e : attributes.entrySet()) {
               String key = e.getKey();

               if (!"href".equalsIgnoreCase(key)) {
                  sb.append(' ').append(key).append("=\"").append(e.getValue()).append('"');
               }
            }

            sb.append('>');
            sb.append(text);
            sb.append("</style>");
         }

         return sb.toString();
      }
   }
}
