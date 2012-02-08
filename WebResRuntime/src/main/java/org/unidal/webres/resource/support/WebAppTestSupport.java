package org.unidal.webres.resource.support;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.unidal.webres.helper.Files;
import org.unidal.webres.helper.Scanners;
import org.unidal.webres.helper.Scanners.FileMatcher;
import org.unidal.webres.helper.Scanners.IMatcher;
import org.unidal.webres.helper.Scanners.JarMatcher;

/**
 * Base integration test class in Jetty with Servlet and JSP support. <p>
 */
public abstract class WebAppTestSupport {
   private boolean m_tempWarRoot;

   private String[] m_resourceBases;

   private Map<String, String> m_fileToSyncs = new HashMap<String, String>();

   private Map<String, String[]> m_jarToWraps = new HashMap<String, String[]>();

   private DateFormat m_timestampFormat = new SimpleDateFormat("HH:mm:ss.SSS");

   protected void configure() throws Exception {
      File warRoot = getWarRoot().getCanonicalFile();

      if (m_tempWarRoot && warRoot.exists()) {
         Files.forDir().delete(warRoot, true);
      }

      warRoot.mkdirs();

      copyResources();
      syncFiles();
      wrapJars();
   }

   protected void copyFromDirectory(final File warRoot, final URL url) {
      File dir = new File(url.getPath());

      Scanners.forDir().scan(dir, new IMatcher<File>() {
         @Override
         public boolean isDirEligible() {
            return true;
         }

         @Override
         public boolean isFileElegible() {
            return true;
         }

         @Override
         public Scanners.IMatcher.Direction matches(File base, String path) {
            File file = new File(base, path);

            if (file.isDirectory()) {
               new File(warRoot, path).mkdirs();
               return Scanners.IMatcher.Direction.DOWN;
            } else {
               try {
                  Files.forDir().copyFile(file, new File(warRoot, path));
               } catch (IOException e) {
                  e.printStackTrace();
               }

               return Scanners.IMatcher.Direction.MATCHED;
            }
         }
      });
   }

   protected void copyFromJar(final File warRoot, final URL url) {
      try {
         JarURLConnection conn = (JarURLConnection) url.openConnection();
         final JarFile jarFile = conn.getJarFile();
         String path = url.getPath();
         int pos = path.indexOf("!/");
         final String prefix = pos < 0 ? "" : path.substring(pos + 2);

         Scanners.forJar().scan(jarFile, new JarMatcher() {
            @Override
            public Direction matches(JarEntry entry, String path) {
               if (path.startsWith(prefix)) {
                  File target = new File(warRoot, path);

                  if (entry.isDirectory()) {
                     target.mkdirs();
                     return Scanners.IMatcher.Direction.DOWN;
                  } else {
                     target.getParentFile().mkdirs();

                     try {
                        Files.forIO().copy(jarFile.getInputStream(entry), new FileOutputStream(target));
                     } catch (IOException e) {
                        e.printStackTrace();
                     }

                     return Scanners.IMatcher.Direction.MATCHED;
                  }
               } else {
                  return Scanners.IMatcher.Direction.NEXT;
               }
            }
         });
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   protected WebAppTestSupport copyResourceFrom(String... resourceBases) {
      m_resourceBases = resourceBases;
      return this;
   }

   protected void copyResources() {
      if (m_resourceBases != null) {
         File warRoot = getWarRoot();

         for (String resourceBase : m_resourceBases) {
            URL base = getClass().getClassLoader().getResource(resourceBase);

            if (base == null && resourceBase.startsWith("/")) {
               base = Thread.currentThread().getContextClassLoader().getResource(resourceBase.substring(1));
            }

            if (base == null) {
               throw new IllegalArgumentException(String.format("Resource(%s) not found!", resourceBase));
            }

            if ("file".equals(base.getProtocol())) {
               copyFromDirectory(warRoot, base);
            } else if ("jar".equals(base.getProtocol())) {
               copyFromJar(warRoot, base);
            }
         }
      }
   }

   protected String getContextPath() {
      return null;
   }

   protected String getTimestamp() {
      return m_timestampFormat.format(new Date());
   }

   protected File getWarRoot() {
      m_tempWarRoot = true;

      return new File(System.getProperty("java.io.tmpdir", "."), "WarRoot");
   }

   protected void syncFiles() throws IOException {
      File warRoot = getWarRoot();

      for (Map.Entry<String, String> e : m_fileToSyncs.entrySet()) {
         System.out.println(String.format("[%s] Synchronizing %s to %s ...", getTimestamp(), e.getKey(), e.getValue()));

         File from = new File(e.getKey());
         File to = new File(warRoot, e.getValue());

         to.mkdirs();

         Files.forDir().copyDir(from, to);
      }
   }

   protected WebAppTestSupport syncFiles(String fromDir, String toDir) {
      m_fileToSyncs.put(fromDir, toDir);
      return this;
   }

   protected WebAppTestSupport wrapIntoJar(String targetJar, String... sourceDirs) {
      m_jarToWraps.put(targetJar, sourceDirs);
      return this;
   }

   protected void wrapJars() throws IOException {
      File warRoot = getWarRoot();

      for (Map.Entry<String, String[]> e : m_jarToWraps.entrySet()) {
         final File targetJar = new File(warRoot, e.getKey());

         System.out.println(String.format("[%s] Wrapping %s into %s ...", getTimestamp(), Arrays.asList(e.getValue()),
               targetJar));
         targetJar.getParentFile().mkdirs();

         final JarOutputStream out = new JarOutputStream(new FileOutputStream(targetJar));

         for (String source : e.getValue()) {
            Scanners.forDir().scan(new File(source), new FileMatcher() {
               @Override
               public IMatcher.Direction matches(File base, String path) {
                  File file = new File(base, path);

                  try {
                     byte[] content = Files.forIO().readFrom(file);
                     JarEntry entry = new JarEntry(path);

                     entry.setSize(content.length);
                     entry.setTime(file.lastModified());
                     out.putNextEntry(entry);
                     out.write(content);
                  } catch (IOException e) {
                     e.printStackTrace();
                  }

                  return Direction.NEXT;
               }
            });
         }

         out.close();
      }
   }
}
