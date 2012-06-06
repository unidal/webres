package org.unidal.webres.server;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.unidal.webres.helper.Servlets;
import org.unidal.webres.resource.runtime.ResourceInitializer;
import org.unidal.webres.resource.runtime.ResourceRuntime;

public class ResourceRuntimeListener implements ServletContextListener {
   private String m_contextPath;

   @Override
   public void contextDestroyed(ServletContextEvent event) {
      // war root can't be reset during re-deployment
      ResourceRuntime.INSTANCE.removeConfig(m_contextPath);
   }

   @Override
   public void contextInitialized(ServletContextEvent event) {
      ServletContext servletContext = event.getServletContext();
      String warRoot = servletContext.getRealPath("/");
      m_contextPath = Servlets.forContext().getContextPath(servletContext);

      ResourceInitializer.initialize(m_contextPath, new File(warRoot));
   }
}