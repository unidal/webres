package org.unidal.webres.resource.js;

import java.util.Collections;
import java.util.Map;

public class Scripts {
   public static Html forHtml() {
      return Html.INSTANCE;
   }

   public static enum Html {
      INSTANCE;

      public String buildInlineScript(String text, Map<String, Object> attributes) {
         int len = (text == null ? 0 : text.length());
         StringBuilder sb = new StringBuilder(len + 64);

         if (len > 0) {
            sb.append("<script");

            if (attributes == null) {
               attributes = Collections.emptyMap();
            }

            Object type = attributes.get("type");

            if (type == null) {
               sb.append(" type=\"text/javascript\"");
            }

            for (Map.Entry<String, Object> e : attributes.entrySet()) {
               String key = e.getKey();

               if (!"src".equalsIgnoreCase(key)) {
                  sb.append(' ').append(e.getKey()).append("=\"").append(e.getValue()).append('"');
               }
            }

            sb.append('>');
            sb.append(text);
            sb.append("</script>");
         }

         return sb.toString();
      }

      public String buildScript(String url, Map<String, Object> attributes) {
         StringBuilder sb = new StringBuilder(url == null ? 0 : url.length() + 64);

         if (url != null) {
            sb.append("<script");

            if (attributes == null) {
               attributes = Collections.emptyMap();
            }

            Object type = attributes.get("type");

            sb.append(" src=\"").append(url).append("\"");

            if (type == null) {
               sb.append(" type=\"text/javascript\"");
            }

            for (Map.Entry<String, Object> e : attributes.entrySet()) {
               String key = e.getKey();

               if (!"src".equalsIgnoreCase(key)) {
                  sb.append(' ').append(key).append("=\"").append(e.getValue()).append('"');
               }
            }

            sb.append("></script>");
         }

         return sb.toString();
      }
   }
}
