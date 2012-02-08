package org.unidal.webres.resource.template;

public enum TemplateLanguage {
   Simple,

   Jsp(".jsp", ".jspf") {
      @Override
      public boolean matches(String content) {
         // with taglib directive
         if (content.contains("<%@") && content.contains("taglib")) {
            return true;
         } else {
            return false;
         }
      }
   };

   private String[] m_extensions;

   private TemplateLanguage(String... extensions) {
      m_extensions = extensions;
   }

   public static TemplateLanguage getByContent(String content, TemplateLanguage defaultValue) {
      for (TemplateLanguage language : values()) {
         if (language.matches(content)) {
            return language;
         }
      }

      return defaultValue;
   }

   public static TemplateLanguage getByExtension(String extension, TemplateLanguage defaultValue) {
      for (TemplateLanguage language : values()) {
         for (String e : language.m_extensions) {
            if (e.equals(extension)) {
               return language;
            }
         }
      }

      return defaultValue;
   }

   public static TemplateLanguage getByName(String name, TemplateLanguage defaultValue) {
      for (TemplateLanguage language : values()) {
         if (language.getName().equals(name)) {
            return language;
         }
      }

      return defaultValue;
   }

   public String getName() {
      return name();
   }

   public boolean matches(String content) {
      return false;
   }
}
