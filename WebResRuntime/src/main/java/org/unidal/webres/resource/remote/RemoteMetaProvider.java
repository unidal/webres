package org.unidal.webres.resource.remote;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.unidal.helper.Files;
import org.unidal.webres.json.JsonSerializer;
import org.unidal.webres.json.SerializationException;
import org.unidal.webres.resource.api.IResourceMeta;
import org.unidal.webres.resource.loader.IClassLoader;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;


public class RemoteMetaProvider<T extends IResourceMeta> implements IRemoteMetaProvider<T> {
	private MappingNode<T> m_root;

	private RemoteMetaLoader<T> m_loader;

	public RemoteMetaProvider(IRemoteMetaBuilder<T> builder, String base) {
		m_loader = new RemoteMetaLoader<T>(builder, base);
		m_root = new MappingNode<T>("");
	}

	@Override
	public T getMeta(String path) throws IOException {
		return m_root.getMeta(m_loader, path);
	}

	public static class Mapping {
		private Map<String, String> m_mapping;

		private Map<String, String[]> m_meta;

		public Map<String, String> getMapping() {
			if (m_mapping == null) {
				return Collections.emptyMap();
			} else {
				return m_mapping;
			}
		}

		public Map<String, String[]> getMeta() {
			if (m_meta == null) {
				return Collections.emptyMap();
			} else {
				return m_meta;
			}
		}

		public void setMapping(Map<String, String> mapping) {
			m_mapping = mapping;
		}

		public void setMeta(Map<String, String[]> meta) {
			m_meta = meta;
		}
	}

	static class MappingNode<T extends IResourceMeta> {
		private String m_path;

		private List<MetaNode<T>> m_metas;

		private Map<String, MappingNode<T>> m_children;

		private boolean m_loaded;

		public MappingNode(String path) {
			m_path = path;
		}

		public T getMeta(RemoteMetaLoader<T> loader, String path) throws IOException {
			load(loader.getResourceBase());

			String name = null;

			if (path.startsWith("/")) {
				int pos = path.indexOf('/', 1);

				if (pos > 0) {
					name = path.substring(1, pos);
					path = path.substring(pos + 1);
				} else {
					path = path.substring(1);
				}
			}

			if (name == null) {
				for (MetaNode<T> meta : m_metas) {
					if (meta.matches(path)) {
						return meta.getMeta(loader, path);
					}
				}
			} else {
				MappingNode<T> child = m_children.get(name);

				if (child != null) {
					return child.getMeta(loader, path);
				}
			}

			return null;
		}

		public boolean isLoaded() {
			return m_loaded;
		}

		public void load(String resourceBase) throws IOException {
			if (!m_loaded) {
				int length = m_path.length();
				Mapping mapping = loadMapping(resourceBase + m_path);

				m_metas = new ArrayList<MetaNode<T>>(mapping.getMeta().size());
				m_children = new HashMap<String, MappingNode<T>>(mapping.getMapping().size() * 4 / 3 + 1);

				for (Map.Entry<String, String[]> e : mapping.getMeta().entrySet()) {
					String[] values = e.getValue();

					m_metas.add(new MetaNode<T>(m_path + "/" + e.getKey() + ".txt", values[0].substring(length), values[1]
					      .substring(length)));
				}

				for (Map.Entry<String, String> e : mapping.getMapping().entrySet()) {
					m_children.put(e.getKey(), new MappingNode<T>(m_path + "/" + e.getKey()));
				}

				m_loaded = true;
			}
		}

		private Mapping loadMapping(String base) throws IOException {
			IClassLoader appClassLoader = ResourceRuntimeContext.ctx().getConfig().getAppClassLoader();
			String mappingFile = base + "/mapping.txt";
			URL url = appClassLoader.getResource(mappingFile);

			if (url == null) {
				throw new RuntimeException(String.format("Unable to find resource(%s) from class path, "
				      + "please check org.unidal.resources-xxx.jar dependency!", mappingFile));
			}

			String content = Files.forIO().readFrom(url.openStream(), "utf-8");

			try {
				return (Mapping) JsonSerializer.getInstance().deserialize(content, Mapping.class);
			} catch (SerializationException e) {
				throw new RuntimeException(String.format("Unable to parse mapping(%s)!", url), e);
			}
		}
	}

	static class MetaNode<T extends IResourceMeta> {
		private String m_path;

		private String m_startId;

		private String m_endId;

		private Map<String, T> m_metas;

		public MetaNode(String path, String startId, String endId) {
			m_path = path;
			m_startId = startId;
			m_endId = endId;
		}

		public T getMeta(RemoteMetaLoader<T> loader, String path) throws IOException {
			load(loader);

			T meta = m_metas.get(path);

			return meta;
		}

		private void load(RemoteMetaLoader<T> loader) throws IOException {
			if (m_metas == null) {
				m_metas = loader.load(m_path);
			}
		}

		public boolean matches(String path) {
			return m_startId.compareTo(path) <= 0 && path.compareTo(m_endId) <= 0;
		}
	}
}
