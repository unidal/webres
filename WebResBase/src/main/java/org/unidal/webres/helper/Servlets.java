package org.unidal.webres.helper;

import javax.servlet.ServletContext;

public class Servlets {
   public static Context forContext() {
      return Context.INSTANCE;
   }

   public static enum Context {
      INSTANCE;

      public String getContextPath(Object context) {
         String contextPath = null;

         if (context instanceof ServletContext) {
            ServletContext servletContext = (ServletContext) context;
            // Servlet 2.5
            contextPath = Reflects.forMethod().invokeMethod(servletContext, "getContextPath");

            if (contextPath == null) {
               // Servlet 2.4
               contextPath = servletContext.getServletContextName();
            }
         } else if (context instanceof String) {
            contextPath = (String) context;
         }

         return normalizeContextPath(contextPath);
      }

      public String normalizeContextPath(String contextPath) {
         if (contextPath != null) {
            if ("/".equals(contextPath) || "".equals(contextPath)) {
               contextPath = null;
            } else if (!contextPath.startsWith("/")) {
               contextPath = "/" + contextPath;
            }
         }

         return contextPath;
      }
   }

}
