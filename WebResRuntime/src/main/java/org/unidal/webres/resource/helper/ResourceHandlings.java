package org.unidal.webres.resource.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.unidal.webres.helper.Splitters;

public class ResourceHandlings {
   public static ResourceELEvaluator forEL() {
      return ResourceELEvaluator.INSTANCE;
   }

   public static enum ResourceELEvaluator {
      INSTANCE;

      private static final String EL_PREFIX_RES = "${res.";

      private static final String EL_PREFIX = "${";

      private static final String EL_SUFFIX = "}";

      public boolean isEL(String el) {
         return el != null && el.startsWith(EL_PREFIX) && el.endsWith(EL_SUFFIX);
      }

      public boolean isResourceEL(String el, String prefix) {
         return el != null && el.startsWith(EL_PREFIX) && el.endsWith(EL_SUFFIX)
               && el.regionMatches(EL_PREFIX.length(), prefix, 0, prefix.length());
      }

      @SuppressWarnings("deprecation")
      private String encode(String string) {
         String result;

         try {
            result = URLEncoder.encode(string, "utf-8");
         } catch (UnsupportedEncodingException e) {
            result = URLEncoder.encode(string);
         }

         return result;
      }

      public String toUrn(String el) {
         return toUrn(el, null);
      }

      public String toUrn(String el, String queryStr) {
         int length = el.length();
         String str = el.substring(EL_PREFIX_RES.length(), length - EL_SUFFIX.length());
         List<String> keys = Splitters.by('.').split(str);
         StringBuilder sb = new StringBuilder(length);
         int len = keys.size();

         //for resource path
         for (int i = 0; i < len - 1; i++) {
            String key = encode(keys.get(i));

            if (i == 0) {
               sb.append(key).append('.');
            } else if (i == 1) {
               sb.append(key).append(':');
            } else {
               sb.append('/').append(key);
            }
         }

         //for property
         if (len - 1 >= 0) {
            String key = keys.get(len - 1);
            boolean hasProperty = key.length() > 0 && key.charAt(0) == '$'; //resource property prefix

            if (!hasProperty) {
               sb.append('/').append(encode(key));
            }

            if (queryStr != null) {
               sb.append('?').append(queryStr);
            }

            if (hasProperty) {
               sb.append("#" + key.substring(1));
            }
         }

         return sb.toString();
      }
   }
}
