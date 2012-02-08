package org.unidal.webres.resource.loader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class ClassLoaders {
   public static Java forJava() {
      return Java.INSTANCE;
   }

   static class CompositeClassLoader implements IClassLoader {
      private IClassLoader[] m_classloaders;

      public CompositeClassLoader(ClassLoader... classloaders) {
         int index = 0;

         m_classloaders = new IClassLoader[classloaders.length];

         for (ClassLoader classloader : classloaders) {
            m_classloaders[index++] = new JavaClassLoader(classloader);
         }
      }

      public CompositeClassLoader(IClassLoader... classloaders) {
         m_classloaders = classloaders;
      }

      @Override
      public URL getResource(String resName) {
         URL url = null;

         for (IClassLoader classloader : m_classloaders) {
            url = classloader.getResource(resName);

            if (url != null) {
               break;
            }
         }

         return url;
      }

      @Override
      public Enumeration<URL> getResources(String resName) throws IOException {
         List<URL> resources = new ArrayList<URL>();

         for (IClassLoader classloader : m_classloaders) {
            Enumeration<URL> urls = classloader.getResources(resName);

            while (urls.hasMoreElements()) {
               resources.add(urls.nextElement());
            }
         }

         return Collections.enumeration(resources);
      }

      @Override
      public Class<?> loadClass(String className) throws ClassNotFoundException {
         ClassNotFoundException cause = null;

         for (IClassLoader classloader : m_classloaders) {
            try {
               return classloader.loadClass(className);
            } catch (ClassNotFoundException e) {
               cause = e;
            }
         }

         if (cause != null) {
            throw cause;
         } else {
            throw new ClassNotFoundException(className);
         }
      }
   }

   public enum Java {
      INSTANCE;

      public IClassLoader getAppClassLoader(Class<?> clazz) {
         ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
         ClassLoader callerClassLoader = clazz.getClassLoader();

         if (threadClassLoader == null || threadClassLoader == callerClassLoader) {// default one if the context class loader is null
            return new JavaClassLoader(callerClassLoader);
         } else {
            return new CompositeClassLoader(callerClassLoader, threadClassLoader);
         }
      }

      public List<IClassLoader> getAvailableClassLoaders(Class<?> clazz) {
         List<IClassLoader> classloaders = new ArrayList<IClassLoader>();
         ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
         ClassLoader callerClassLoader = clazz.getClassLoader();

         classloaders.add(new JavaClassLoader(callerClassLoader));

         if (contextClassLoader != callerClassLoader) {
            classloaders.add(new JavaClassLoader(contextClassLoader));
         }

         return classloaders;
      }

      public JavaClassLoader wrap(ClassLoader classLoader) {
         return new JavaClassLoader(classLoader);
      }
   }

   static class JavaClassLoader implements IClassLoader {
      private ClassLoader m_classLoader;

      public JavaClassLoader(ClassLoader classLoader) {
         m_classLoader = classLoader;
      }

      @Override
      public URL getResource(String resName) {
         return m_classLoader.getResource(resName);
      }

      @Override
      public Enumeration<URL> getResources(String resName) throws IOException {
         return m_classLoader.getResources(resName);
      }

      @Override
      public Class<?> loadClass(String className) throws ClassNotFoundException {
         return m_classLoader.loadClass(className);
      }
   }
}
