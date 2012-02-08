package org.unidal.webres.helper;

import java.util.List;


public class Markers {
   public static DeferMarker forDefer() {
      return DeferMarker.INSTANCE;
   }

   public static enum DeferMarker {
      INSTANCE;

      public String build(String... sections) {
         StringBuilder sb = new StringBuilder(64);

         sb.append("${");
         sb.append("MARKER");

         for (String section : sections) {
            sb.append(',').append(section);
         }

         sb.append('}');

         return sb.toString();
      }

      public String[] parse(String marker) {
         List<String> items = Splitters.by(',').split(marker);
         int size = items.size();

         if (size > 0) {
            String[] sections = new String[size - 1];

            for (int i = 1; i < size; i++) {
               sections[i - 1] = items.get(i);
            }

            return sections;
         }

         return null;
      }

      public boolean validate(String marker) {
         return parse(marker) != null;
      }
   }
}
