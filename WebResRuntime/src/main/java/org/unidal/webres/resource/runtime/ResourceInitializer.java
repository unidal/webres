package org.unidal.webres.resource.runtime;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.unidal.webres.helper.Servlets;
import org.unidal.webres.helper.Splitters;
import org.unidal.webres.resource.loader.ClassLoaders;
import org.unidal.webres.resource.loader.IClassLoader;
import org.unidal.webres.resource.spi.IResourceConfigurator;
import org.unidal.webres.resource.spi.IResourceRegistry;

public class ResourceInitializer {
   static List<IResourceConfigurator> findAllConfigurators(List<IClassLoader> classLoaders) {
      List<IResourceConfigurator> list = new ArrayList<IResourceConfigurator>();
      Set<String> done = new HashSet<String>(); //check for duplicated configurator class names

      for (IClassLoader classloader : classLoaders) {
         List<Properties> propsList = ResourceReflector.INSTANCE.getResourcesProperties(classloader, null,
               ResourceRuntimeConfig.RUNTIME_EXT_PROPERTIES);
         int size = propsList.size();

         // load them in reverse order
         for (int i = size - 1; i >= 0; i--) {
            Properties props = propsList.get(i);

            String classNameList = props.getProperty(ResourceRuntimeConfig.PROPERTY_RESOURCE_CONFIGURATORS);

            if (classNameList != null && !classNameList.isEmpty()) {
               List<String> classNames = Splitters.by(',').noEmptyItem().trim().split(classNameList);

               for (String className : classNames) {
                  if (!done.contains(className)) {
                     Class<?> clazz = null;

                     try {
                        clazz = classloader.loadClass(className);
                     } catch (ClassNotFoundException e) {
                        // ignore it
                        // TODO must print error message since the class loading failure
                     }

                     if (clazz != null && IResourceConfigurator.class.isAssignableFrom(clazz)) {
                        try {
                           IResourceConfigurator configurator = (IResourceConfigurator) clazz.newInstance();

                           list.add(configurator);
                           done.add(className);
                        } catch (Exception e) {
                           //ignore it
                           // TODO must print error message since the class instantiate failure
                        }
                     }
                  }
               }
            }
         }
      }

      return list;
   }

   public static void initialize(String contextPath, File warRoot) {
      initialize(contextPath, warRoot, ClassLoaders.forJava().getAvailableClassLoaders(ResourceInitializer.class));
   }

   public static void initialize(String contextPath, File warRoot, List<IClassLoader> classLoaders) {
      if (classLoaders.isEmpty()) {
         throw new IllegalArgumentException("Classloaders should not be empty!");
      }

      String path = Servlets.forContext().normalizeContextPath(contextPath);
      ResourceRuntimeConfig config = ResourceRuntime.INSTANCE.loadConfig(path, warRoot);
      List<IResourceConfigurator> configurators = findAllConfigurators(classLoaders);
      IResourceRegistry registry = config.getRegistry();

      initializeWithConfigurators(configurators, registry);

      registry.register(IResourceRegistry.class, registry);
      registry.register(ResourceRuntimeConfig.class, config);

      // TODO why do we need this, since we could have all classloaders
      config.setAppClassLoader(ClassLoaders.forJava().getAppClassLoader(ResourceInitializer.class));
   }

   static void initializeWithConfigurators(List<IResourceConfigurator> configurators, IResourceRegistry registry) {
      for (IResourceConfigurator configurator : configurators) {
         try {
            configurator.configure(registry);
         } catch (Exception e) {
            throw new RuntimeException(String.format("Error when executing resource configurator(%s)!", configurator
                  .getClass().getName()), e);
         }
      }
   }
}
