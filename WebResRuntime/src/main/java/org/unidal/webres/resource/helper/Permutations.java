package org.unidal.webres.resource.helper;

import java.util.Locale;

import org.unidal.webres.resource.runtime.ResourcePermutation;

public class Permutations {
   public static final String TARGET_DELIMETER = "__";

   public static PermCountry forCountry() {
      return PermCountry.INSTANCE;
   }

   public static PermExternal forExternal() {
      return PermExternal.INSTANCE;
   }

   public static PermLang forLanguage() {
      return PermLang.INSTANCE;
   }

   private Permutations() {
   }

   public static enum PermCountry {
      INSTANCE;

      public boolean isValidCountry(String country) {
         if (country == null || country.length() == 0) {
            return true;
         }

         for (String validCn : Locale.getISOCountries()) {
            if (validCn.equals(country)) {
               return true;
            }
         }

         return false;
      }
   }

   public static enum PermExternal {
      INSTANCE;

      public ResourcePermutation fromExternal(String external) {
         if (external == null || external.length() == 0) {
            return null;
         }

         String localeStr = external;
         String target = null;
         int targetIdx = external.lastIndexOf(TARGET_DELIMETER);
         if (targetIdx != -1) {
            target = external.substring(targetIdx + TARGET_DELIMETER.length());
            localeStr = external.substring(0, targetIdx);
         }

         String lang = localeStr;
         String country = "";
         String var = "";
         int contryIdx = localeStr.indexOf("_");
         if (contryIdx != -1) {
            lang = localeStr.substring(0, contryIdx);
            country = localeStr.substring(contryIdx + 1);
            int varIdx = country.indexOf("_");
            if (varIdx != -1) {
               var = country.substring(varIdx + 1);
               country = country.substring(0, varIdx);
            }
         }

         if (!forLanguage().isValidLanguage(lang) || !forCountry().isValidCountry(country)) {
            return null;
         }

         return ResourcePermutation.create(new Locale(lang, country, var), target);
      }

      public String toExternal(ResourcePermutation perm) {
         StringBuilder buf = new StringBuilder();

         buf.append(perm.getLocale());

         String target = perm.getTarget();
         if (target != null) {
            buf.append(TARGET_DELIMETER);
            buf.append(target);
         }

         return buf.toString();
      }
   }

   public static enum PermLang {
      INSTANCE;

      public boolean isValidLanguage(String lang) {
         if (lang != null) {
            for (String validLang : Locale.getISOLanguages()) {
               if (validLang.equals(lang)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

}
