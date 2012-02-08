package org.unidal.webres.tag.img;

import java.util.Collections;
import java.util.Map;

import org.unidal.webres.resource.ResourceOutputType;
import org.unidal.webres.resource.api.IImage;
import org.unidal.webres.resource.api.IImageMeta;
import org.unidal.webres.resource.api.IResourceOutputType;

public class Images {
   public static Html forHtml() {
      return Html.INSTANCE;
   }

   public static enum Html {
      INSTANCE;

      public String buildImage(IImage image, Map<String, Object> attributes, String url, IResourceOutputType outputType) {
         if (outputType != ResourceOutputType.HTML && outputType != ResourceOutputType.XHTML) {
            throw new RuntimeException(String.format("Unsupported TagOutputType(%s) for image tag.", outputType));
         }

         StringBuilder sb = new StringBuilder(url == null ? 0 : url.length() + 64);

         if (url != null) {
            sb.append("<img");

            if (attributes == null) {
               attributes = Collections.emptyMap();
            }

            sb.append(" src=\"").append(url).append('"');

            IImageMeta meta = image.getMeta();
            Object width = attributes.get("width");
            Object height = attributes.get("height");

            if (width == null && meta.getWidth() > 0) {
               sb.append(" width=\"").append(meta.getWidth()).append('"');
            }

            if (height == null && meta.getHeight() > 0) {
               sb.append(" height=\"").append(meta.getHeight()).append('"');
            }

            for (Map.Entry<String, Object> e : attributes.entrySet()) {
               String key = e.getKey();

               if (!"src".equalsIgnoreCase(key)) {
                  sb.append(' ').append(key).append("=\"").append(e.getValue()).append('"');
               }
            }

            if (outputType == ResourceOutputType.HTML) {
               sb.append(">");
            } else {
               sb.append("/>");
            }
         }

         return sb.toString();
      }
   }
}
