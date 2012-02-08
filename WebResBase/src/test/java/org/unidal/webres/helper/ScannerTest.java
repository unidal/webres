package org.unidal.webres.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import junit.framework.Assert;

import org.junit.Test;

import org.unidal.webres.helper.Scanners.FileMatcher;
import org.unidal.webres.helper.Scanners.JarMatcher;

public class ScannerTest {
   @Test
   public void testScanDir() throws IOException {
      String packageName = getClass().getPackage().getName();
      URL url = getClass().getResource("/" + packageName.replace('.', '/'));
      final int expectedSize = 22;
      final StringBuilder sb = new StringBuilder(expectedSize);

      Scanners.forDir().scan(new File(url.getPath()), new FileMatcher() {
         @Override
         public Direction matches(File base, String path) {
            sb.append('.');
            return Direction.DOWN;
         }
      });

      Assert.assertTrue(sb.length() >= expectedSize);
   }

   @Test
   public void testScanJar() throws IOException {
      URL url = getClass().getResource("/junit");
      JarURLConnection conn = (JarURLConnection) url.openConnection();
      final JarFile jarFile = conn.getJarFile();
      String path = url.getPath();
      int pos = path.indexOf("!/");
      final String prefix = pos < 0 ? "" : path.substring(pos + 2);
      final int expectedSize = 68692;
      final ByteArrayOutputStream baos = new ByteArrayOutputStream(expectedSize);

      Scanners.forJar().scan(jarFile, new JarMatcher() {
         @Override
         public Scanners.IMatcher.Direction matches(JarEntry entry, String path) {
            if (path.startsWith(prefix)) {
               if (entry.isDirectory()) {
                  return Scanners.IMatcher.Direction.DOWN;
               } else {

                  try {
                     Files.forIO().copy(jarFile.getInputStream(entry), baos);
                  } catch (IOException e) {
                     throw new RuntimeException(e);
                  }

                  return Scanners.IMatcher.Direction.MATCHED;
               }
            } else {
               return Scanners.IMatcher.Direction.NEXT;
            }
         }
      });

      Assert.assertTrue(baos.size() >= expectedSize);
   }
}
