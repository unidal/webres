package org.unidal.webres.resource.runtime;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.unidal.webres.resource.loader.IClassLoader;

public enum ResourceReflector {
   INSTANCE;

   private String getResourceName(Class<?> clazz, String resName) {
      // Turn package name into a directory path
      if (resName.length() > 0 && resName.charAt(0) == '/')
         return resName.substring(1);

      String qualifiedClassName = clazz != null ? clazz.getName() : getClass().getName();
      int classIndex = qualifiedClassName.lastIndexOf('.');

      if (classIndex == -1)
         return resName; // from a default package

      return qualifiedClassName.substring(0, classIndex + 1).replace('.', '/') + resName;
   }

   public Properties getResourceProperties(IClassLoader classloader, Class<?> anchorClass, String resName) {
      resName = getResourceName(anchorClass, resName);

      URL url = classloader.getResource(resName);

      if (url != null) {
         try {
            Properties prop = new Properties();
            prop.load(url.openStream());
            return prop;
         } catch (IOException e) {
            //ignore it
         }
      }

      return null;
   }

   public List<Properties> getResourcesProperties(IClassLoader classloader, Class<?> anchorClass, String resName) {
      List<Properties> properties = new ArrayList<Properties>();

      try {
         String path = getResourceName(anchorClass, resName);
         Enumeration<URL> urls = classloader.getResources(path);

         while (urls.hasMoreElements()) {
            URL url = urls.nextElement();

            try {
               Properties prop = new Properties();

               prop.load(url.openStream());
               properties.add(prop);
            } catch (IOException e) {
               //ignore it
               // TODO should print some error message here
            }
         }
      } catch (IOException e) {
         //ignore it
         // TODO should print some error message here
      }

      return properties;
   }

   public List<Properties> getResourcesProperties(IClassLoader classloader, String resName) {
      return getResourcesProperties(classloader, null, resName);
   }
}
