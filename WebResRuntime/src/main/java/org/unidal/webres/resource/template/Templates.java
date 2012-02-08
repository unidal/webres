package org.unidal.webres.resource.template;

public class Templates {
   public static Language forLanguage() {
      return Language.INSTANCE;
   }

   public enum Language {
      INSTANCE;

      public String detectByContent(String content) {
         TemplateLanguage language = TemplateLanguage.getByContent(content, TemplateLanguage.Simple);

         return language.getName();
      }

      public String detectByFileName(String fileName, String content) {
         int pos = fileName.lastIndexOf('.');

         if (pos > 0) {
            TemplateLanguage language = TemplateLanguage.getByExtension(fileName.substring(pos), null);

            if (language != null) {
               return language.getName();
            }
         }

         return detectByContent(content);
      }
   }
}
