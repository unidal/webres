package org.unidal.webres.resource.helper;

import java.io.File;
import java.net.URL;

import org.unidal.webres.resource.api.IResourcePermutation;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.loader.IClassLoader;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceContext;

public class ResourceResolvings {
   public static DirResolving fromDir() {
      return DirResolving.INSTANCE;
   }

   public static JarResolving fromJar() {
      return JarResolving.INSTANCE;
   }

   public static enum DirResolving {
      INSTANCE;

      protected String getFallbackPath(String path) {
         int pos1 = path.lastIndexOf('/');
         int pos2 = path.lastIndexOf('_');

         if (pos2 > pos1) {
            return path.substring(0, pos2) + '.' + path.substring(pos2 + 1);
         } else {
            return null;
         }
      }

      public File resolve(IResourceContext ctx, IResourceUrn urn, File base) {
         IResourcePermutation permutation = ctx.getPermutation();
         String path = urn.getResourceId();
         File file;

         if (permutation != null) {
            String external = permutation.toExternal();

            file = new File(new File(base, external), path);

            if (!file.exists()) { // replace last '_' with '.'
               String pathInfo = getFallbackPath(path);

               if (pathInfo != null) {
                  file = new File(new File(base, external), pathInfo);

                  if (file.isFile()) {
                     urn.setPathInfo(pathInfo);
                  }
               }
            }

            if (!file.exists()) { // fail back to no permutation
               file = new File(base, path);
               ctx.setFallbackPermutation(true);
            }
         } else {
            file = new File(base, path);
         }

         if (!file.isFile()) {
            String pathInfo = getFallbackPath(path);

            if (pathInfo != null) {
               file = new File(base, pathInfo);

               if (file.isFile()) {
                  urn.setPathInfo(pathInfo);
               }
            }
         }

         return file;
      }
   }

   public static enum JarResolving {
      INSTANCE;

      protected String getFallbackPath(String path) {
         int pos1 = path.lastIndexOf('/');
         int pos2 = path.lastIndexOf('_');

         if (pos2 > pos1) {
            return path.substring(0, pos2) + '.' + path.substring(pos2 + 1);
         } else {
            return null;
         }
      }

      public URL resolve(IResourceContext ctx, IResourceUrn urn, String base) {
         IClassLoader classloader = ResourceRuntimeContext.ctx().getConfig().getAppClassLoader();
         IResourcePermutation permutation = ctx.getPermutation();
         String path = urn.getResourceId();
         URL url = null;

         if (permutation != null) {
            String resName = base + '/' + permutation.toExternal() + path;

            url = classloader.getResource(resName);

            if (url == null) {
               String pathInfo = getFallbackPath(path);

               if (pathInfo != null) {
                  url = classloader.getResource(base + '/' + permutation.toExternal() + pathInfo);

                  if (url != null) {
                     urn.setPathInfo(pathInfo);
                  }
               }
            }

            if (url == null) {
               ctx.setFallbackPermutation(true);
            }
         }

         if (url == null) {
            String resName = base + path;

            url = classloader.getResource(resName);

            if (url == null) {
               String pathInfo = getFallbackPath(path);

               if (pathInfo != null) {
                  url = classloader.getResource(base + pathInfo);

                  if (url != null) {
                     urn.setPathInfo(pathInfo);
                  }
               }
            }
         }
         
         return url;
      }
   }
}
