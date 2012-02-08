package org.unidal.webres.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.unidal.webres.helper.Scanners.IMatcher.Direction;

public class Scanners {
  public static DirScanner forDir() {
     return DirScanner.INSTANCE;
  }

  public static JarScanner forJar() {
     return JarScanner.INSTANCE;
  }

  public static UrlScanner forUrl() {
     return UrlScanner.INSTANCE;
  }

  public static abstract class DirMatcher implements IMatcher<File> {
     @Override
     public boolean isDirEligible() {
        return true;
     }

     @Override
     public boolean isFileElegible() {
        return false;
     }
  }

  public static enum DirScanner {
     INSTANCE;

     public List<File> scan(File base, IMatcher<File> matcher) {
        List<File> files = new ArrayList<File>();
        StringBuilder relativePath = new StringBuilder();

        scanForFiles(base, relativePath, matcher, false, files);

        return files;
     }

     protected void scanForFiles(File base, StringBuilder relativePath, IMatcher<File> matcher, boolean foundFirst,
           List<File> files) {
        int len = relativePath.length();
        File dir = len == 0 ? base : new File(base, relativePath.toString());
        String[] list = dir.list();

        if (list != null) {
           for (String item : list) {
              File child = new File(dir, item);

              if (len > 0) {
                 relativePath.append('/');
              }

              relativePath.append(item);

              if (child.isDirectory()) {
                 if (matcher.isDirEligible()) {
                    Direction direction = matcher.matches(base, relativePath.toString());

                    switch (direction) {
                    case MATCHED:
                       files.add(child);
                       break;
                    case DOWN:
                       // for sub-folders
                       scanForFiles(base, relativePath, matcher, foundFirst, files);
                       break;
                    }
                 } else {
                    scanForFiles(base, relativePath, matcher, foundFirst, files);
                 }
              } else {
                 if (matcher.isFileElegible()) {
                    Direction direction = matcher.matches(base, relativePath.toString());

                    switch (direction) {
                    case MATCHED:
                       files.add(child);
                       break;
                    case DOWN:
                       break;
                    }
                 }
              }

              relativePath.setLength(len); // reset

              if (foundFirst && files.size() > 0) {
                 break;
              }
           }
        }
     }

     public File scanForOne(File base, IMatcher<File> matcher) {
        List<File> files = new ArrayList<File>(1);
        StringBuilder relativePath = new StringBuilder();

        scanForFiles(base, relativePath, matcher, true, files);

        if (files.isEmpty()) {
           return null;
        } else {
           return files.get(0);
        }
     }
  }

  public static abstract class FileMatcher implements IMatcher<File> {
     @Override
     public boolean isDirEligible() {
        return false;
     }

     @Override
     public boolean isFileElegible() {
        return true;
     }
  }

  public static interface IDirectoryProvider<T> {
     public boolean isDirectory(T base, String path);

     public List<String> list(T base, String path) throws IOException;
  }

  public static interface IMatcher<T> {
     public boolean isDirEligible();

     public boolean isFileElegible();

     public Direction matches(T base, String path);

     public static enum Direction {
        MATCHED,

        DOWN,

        NEXT;

        public boolean isDown() {
           return this == DOWN;
        }

        public boolean isMatched() {
           return this == MATCHED;
        }

        public boolean isNext() {
           return this == NEXT;
        }
     }
  }

  public static abstract class JarMatcher implements IMatcher<JarEntry> {
     @Override
     public boolean isDirEligible() {
        return false; // TODO should be true
     }

     @Override
     public boolean isFileElegible() {
        return true;
     }
  }

  public static enum JarScanner {
     INSTANCE;

     public ZipEntry getEntry(String jarFileName, String name) {
        ZipFile zipFile = null;

        try {
           zipFile = new ZipFile(jarFileName);

           ZipEntry entry = zipFile.getEntry(name);

           return entry;
        } catch (IOException e) {
           //ignore
        } finally {
           if (zipFile != null) {
              try {
                 zipFile.close();
              } catch (IOException e) {
                 // ignore it
              }
           }
        }

        return null;
     }

     public byte[] getEntryContent(String jarFileName, String entryPath) {
        byte[] bytes = null;
        ZipFile zipFile = null;

        try {
           zipFile = new ZipFile(jarFileName);
           ZipEntry entry = zipFile.getEntry(entryPath);

           if (entry != null) {
              InputStream inputStream = zipFile.getInputStream(entry);
              bytes = Files.forIO().readFrom(inputStream);
           }
        } catch (Exception e) {
           //ignore
        } finally {
           if (zipFile != null) {
              try {
                 zipFile.close();
              } catch (Exception e) {
              }
           }
        }

        return bytes;
     }

     public boolean hasEntry(String jarFileName, String name) {
        return getEntry(jarFileName, name) != null;
     }

     public List<String> scan(File jarFile, IMatcher<JarEntry> matcher) {
        List<String> files = new ArrayList<String>();

        try {
           scanForFiles(new JarFile(jarFile), matcher, false, files);
        } catch (IOException e) {
           e.printStackTrace();
        }

        return files;
     }

     public List<String> scan(JarFile jarFile, IMatcher<JarEntry> matcher) {
        List<String> files = new ArrayList<String>();
        scanForFiles(jarFile, matcher, false, files);

        return files;
     }

     protected void scanForFiles(JarFile jarFile, IMatcher<JarEntry> matcher, boolean foundFirst, List<String> names) {
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
           JarEntry entry = entries.nextElement();
           String name = entry.getName();

           if (matcher.isDirEligible() && entry.isDirectory()) {
              Direction direction = matcher.matches(entry, name);

              if (direction.isMatched()) {
                 names.add(name);
              }
           } else if (matcher.isFileElegible() && !entry.isDirectory()) {
              Direction direction = matcher.matches(entry, name);

              if (direction.isMatched()) {
                 names.add(name);
              }
           }

           if (foundFirst && names.size() > 0) {
              break;
           }
        }
     }

     public String scanForOne(File jarFile, IMatcher<JarEntry> matcher) {
        List<String> files = new ArrayList<String>(1);

        try {
           scanForFiles(new JarFile(jarFile), matcher, true, files);
        } catch (IOException e) {
           e.printStackTrace();
        }

        if (files.isEmpty()) {
           return null;
        } else {
           return files.get(0);
        }
     }
  }

  public static abstract class UrlMatcher implements IMatcher<URL> {
     @Override
     public boolean isDirEligible() {
        return false;
     }

     @Override
     public boolean isFileElegible() {
        return true;
     }
  }

  public static enum UrlScanner {
     INSTANCE;

     public List<URL> scan(URL base, IDirectoryProvider<URL> provider, IMatcher<URL> matcher) throws IOException {
        List<URL> files = new ArrayList<URL>();
        StringBuilder relativePath = new StringBuilder();

        scanForFiles(base, relativePath, matcher, provider, files);
        return files;
     }

     protected void scanForFiles(URL base, StringBuilder relativePath, IMatcher<URL> matcher, IDirectoryProvider<URL> provider,
           List<URL> files) throws IOException {
        int len = relativePath.length();
        URL dir = new URL(base, relativePath.toString());
        List<String> list = provider.list(base, relativePath.toString());

        for (String item : list) {
           URL child = new URL(dir, item);

           if (len > 0) {
              relativePath.append('/');
           }

           if (item.endsWith("/")) {
              relativePath.append(item.substring(0, item.length() - 1));
           } else {
              relativePath.append(item);
           }

           if (provider.isDirectory(dir, item)) {
              if (matcher.isDirEligible()) {
                 Direction direction = matcher.matches(base, relativePath.toString());

                 switch (direction) {
                 case MATCHED:
                    files.add(child);
                    break;
                 case DOWN:
                    // for sub-folders
                    scanForFiles(base, relativePath, matcher, provider, files);
                    break;
                 }
              } else {
                 scanForFiles(base, relativePath, matcher, provider, files);
              }
           } else {
              if (matcher.isFileElegible()) {
                 Direction direction = matcher.matches(base, relativePath.toString());

                 switch (direction) {
                 case MATCHED:
                    files.add(child);
                    break;
                 case DOWN:
                    break;
                 }
              }
           }

           relativePath.setLength(len); // reset
        }
     }
  }
}