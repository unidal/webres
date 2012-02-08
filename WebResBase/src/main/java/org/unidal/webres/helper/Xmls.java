package org.unidal.webres.helper;

public class Xmls {
   public static Data forData() {
      return new Data();
   }

   public static class Data {
      private static final String PREFIX = "<![CDATA[";

      private static final String SUFFIX = "]]>";

      private boolean m_trim;

      public boolean isEnclosedByCData(String xmlStr) {
         if (xmlStr != null) {
            if (m_trim) {
               xmlStr = xmlStr.trim();
            }
            return xmlStr.startsWith(PREFIX) && xmlStr.endsWith(SUFFIX);
         } else {
            return false;
         }
      }

      public String trimCData(String xmlStr) {
         if (isEnclosedByCData(xmlStr)) {
            if (m_trim) {
               xmlStr = xmlStr.trim();
            }

            return xmlStr.substring(PREFIX.length(), xmlStr.length() - SUFFIX.length());
         } else {
            return xmlStr;
         }
      }

      public Data trim() {
         m_trim = true;
         return this;
      }
   }
}
