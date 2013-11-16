package org.unidal.webres.resource.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.unidal.helper.Splitters;
import org.unidal.webres.resource.api.IResourceMeta;
import org.unidal.webres.resource.loader.IClassLoader;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;

public class RemoteMetaLoader<T extends IResourceMeta> {
   private String m_base;

   private IRemoteMetaBuilder<T> m_builder;

   public RemoteMetaLoader(IRemoteMetaBuilder<T> builder, String base) {
      m_builder = builder;
      m_base = base;
   }

   public String getResourceBase() {
      return m_base;
   }

   public Map<String, T> load(InputStream in, String name) throws IOException {
      Map<String, T> metas = new HashMap<String, T>();
      BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
      List<String> items = new ArrayList<String>(3);

      try {
         while (true) {
            String line = reader.readLine();

            if (line == null) {
               break;
            }

            Splitters.by('|').split(line, items);

            if (!m_builder.build(metas, name, items)) {
               System.err.println(String.format("Unable to parse line(%s) in meta(%s)!", line, name));
            }

            items.clear();
         }
      } finally {
         try {
            reader.close();
         } catch (IOException e) {
            // ignore it
         }
      }

      return metas;
   }

   public Map<String, T> load(String name) throws IOException {
      IClassLoader appClassLoader = ResourceRuntimeContext.ctx().getConfig().getAppClassLoader();
      URL url = appClassLoader.getResource(m_base + name);

      if (url == null) {
         throw new RuntimeException(String.format("Unable to get resource(%s)!", m_base + name));
      }

      return load(url.openStream(), name);
   }
}
