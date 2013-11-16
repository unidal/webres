package org.unidal.webres.resource.runtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.unidal.helper.Splitters;
import org.unidal.webres.resource.helper.Permutations;

public class ResourcePropertyLoader {
   private static final String JS_PROPERTY_KEY = "js.base";

   private static final String CSS_PROPERTY_KEY = "css.base";

   private static final String IMAGE_PROPERTY_KEY = "image.base";

   private static final String APP_ID = "app.id";

   private static final String APP_VERSION = "app.version";

   private static final String PERMUTATION_PROPERTY_PREFIX = "permutation.";

   private static final String LANGUAGE = "v1";

   private static final String COUNTRY = "v2";

   private static final String VARIANT = "v3";

   private static final String TARGET = "v4";

   private String m_imgBase = "/img";

   private String m_cssBase = "/css";

   private String m_jsBase = "/js";

   private String m_appId;

   private String m_appVersion;

   private static final String WAR_REFERENCE_PREFIX = "war";

   private static final String WAR_REFERENCE_NAME = "name";

   private List<ResourcePermutation> m_permutations = new ArrayList<ResourcePermutation>();

   private List<ResourceWarReference> m_references = new ArrayList<ResourceWarReference>();

   private String m_warId = null;

   public String getAppId() {
      return m_appId;
   }

   public String getAppVersion() {
      return m_appVersion;
   }

   public String getCssBase() {
      return m_cssBase;
   }

   public String getImageBase() {
      return m_imgBase;
   }

   public String getJsBase() {
      return m_jsBase;
   }

   public List<ResourcePermutation> getPermutations() {
      return m_permutations;
   }

   public List<ResourceWarReference> getReferences() {
      return m_references;
   }

   public String getWarId() {
      return m_warId;
   }

   public void load(File file) throws IOException {
      InputStream in = new FileInputStream(file);
      try {
         load(in);
      }
      finally {
         in.close();
      }
   }

   public void load(InputStream in) throws IOException {
      Properties props = new Properties();
      props.load(in);

      Map<String, PermutationProperty> permProps = new HashMap<String, PermutationProperty>();
      Map<String, ResourceWarReference> references = new HashMap<String, ResourceWarReference>();

      for (Entry<Object, Object> entry : props.entrySet()) {
         String key = (String) entry.getKey();
         String value = (String) entry.getValue();

         if (value != null) {
            value = value.length() == 0 ? null : value.trim();
         }

         if (IMAGE_PROPERTY_KEY.equals(key)) {
            m_imgBase = value;
         } else if (CSS_PROPERTY_KEY.equals(key)) {
            m_cssBase = value;
         } else if (JS_PROPERTY_KEY.equals(key)) {
            m_jsBase = value;
         } else if (APP_ID.equals(key)) {
            m_appId = value;
         } else if (APP_VERSION.equals(key)) {
            m_appVersion = value;
         } else if (key.startsWith(PERMUTATION_PROPERTY_PREFIX)) {
            List<String> parts = Splitters.by('.').split(key);

            if (parts.size() == 3) {
               String perName = parts.get(1);
               String property = parts.get(2);

               PermutationProperty permutation = permProps.get(perName);
               if (permutation == null) {
                  permutation = new PermutationProperty();
                  permProps.put(perName, permutation);
               }

               if (LANGUAGE.equals(property)) {
                  permutation.setLanguage(value);
               } else if (COUNTRY.equals(property)) {
                  permutation.setCountry(value);
               } else if (VARIANT.equals(property)) {
                  permutation.setVariant(value);
               } else if (TARGET.equals(property)) {
                  permutation.setTarget(value);
               }
            }
         } else if (key.startsWith(WAR_REFERENCE_PREFIX)) {
            List<String> parts = Splitters.by('.').split(key);

            if (parts.size() == 3) {
               String dependencyName = parts.get(1);
               String suffix = parts.get(2);

               if (suffix.equals(WAR_REFERENCE_NAME)) {
                  ResourceWarReference reference = references.get(dependencyName);
                  if (reference == null) {
                     reference = new ResourceWarReference();
                     references.put(dependencyName, reference);
                  }
                  reference.setLogicalName(dependencyName);
                  reference.setAppId(value);
               }
            } else if (parts.size() == 2) {
               String suffix = parts.get(1);
               if (suffix.equals(WAR_REFERENCE_NAME)) {
                  m_warId = value;
               }
            }
         }
      }

      for (PermutationProperty prop : permProps.values()) {
         // only set those valid permutations
         ResourcePermutation perm = prop.toPermutation();
         if (perm != null && !m_permutations.contains(perm)) {
            m_permutations.add(perm);
         }
      }

      for (ResourceWarReference prop : references.values()) {
         m_references.add(prop);
      }
   }

   static class PermutationProperty {
      private String m_target;

      private String m_variant;

      private String m_country;

      private String m_lang;

      public void setCountry(String value) {
         m_country = value;
      }

      public void setLanguage(String value) {
         m_lang = value;
      }

      public void setTarget(String value) {
         m_target = value;
      }

      public void setVariant(String value) {
         m_variant = value;
      }

      public ResourcePermutation toPermutation() {
         if (!Permutations.forLanguage().isValidLanguage(m_lang)
               || !Permutations.forCountry().isValidCountry(m_country)) {
            return null;
         }

         Locale locale = new Locale(m_lang, m_country != null ? m_country : "", m_variant == null ? "" : m_variant);
         return ResourcePermutation.create(locale, m_target);
      }

   }
}