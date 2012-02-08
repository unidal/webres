package org.unidal.webres.json.serializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.unidal.webres.json.JsonArray;
import org.unidal.webres.json.JsonObject;
import org.unidal.webres.json.JsonTokener;
import org.unidal.webres.json.SerializationException;


/**
 * This class is the public entry point to the serialization code and provides
 * methods for marshalling Java objects into JSON objects and unmarshalling JSON
 * objects into Java objects.
 */
@SuppressWarnings("rawtypes")
public class JsonRpcSerializer {
	
	private volatile Registry m_registry = new Registry();

	// private boolean m_marshallClassHints = true;
	private boolean m_marshallNullAttributes = true;

	public void registerDefaultSerializers() throws SerializationException {
		registerSerializer(new BeanSerializer());
		registerSerializer(new ArraySerializer());
		registerSerializer(new DictionarySerializer());
		registerSerializer(new MapSerializer());
		registerSerializer(new SetSerializer());
		registerSerializer(new ListSerializer());
		registerSerializer(new DateSerializer());
		registerSerializer(new StringSerializer());
		registerSerializer(new NumberSerializer());
		registerSerializer(new BooleanSerializer());
		registerSerializer(new PrimitiveSerializer());
	}

	public void registerSerializer(ISerializer s) throws SerializationException {
		Registry.registerSerializer(s, this);
	}

	
   public ISerializer getSerializer(Class clazz, Class jsoClazz) {
		return m_registry.getSerializer(clazz, jsoClazz);
	}
	
	/**
	 * Registry that handles registrations and retrievals of serializers in a 
	 * thread-safe manner utilizing the copy-on-write pattern.
	 */
	private static class Registry {
		// Key Serializer
		private final Set<ISerializer> m_serializerSet;
		// key Class, val Serializer
		private final Map<Class,ISerializer> m_serializableMap;
		// List for reverse registration order search
		private final List<ISerializer> m_serializerList;

		/**
		 * Creates an empty registry.
		 */
		Registry() {
			m_serializerSet = new HashSet<ISerializer>();
			m_serializableMap = new HashMap<Class,ISerializer>();
			m_serializerList = new ArrayList<ISerializer>();
		}
		
		/**
		 * Creates a new registry with new collections that are copies of the 
		 * registry that is passed in.
		 */
		Registry(Registry prev) {
			m_serializerSet = new HashSet<ISerializer>(prev.m_serializerSet);
			m_serializableMap = new HashMap<Class,ISerializer>(prev.m_serializableMap);
			m_serializerList = new ArrayList<ISerializer>(prev.m_serializerList);
		}
		
		/**
		 * Registers a new serializer for the given JsonRpcSerializer.  This
		 * method is synchronized on the JsonRpcSerializer instance, and thus
		 * only one thread per JsonRpcSerializer can register a serializer at a
		 * given time.  On successful registration, a new registry instance is
		 * created as a copy of the previous one, and replaces the registry
		 * field of the JsonRpcSerializer instance (hence copy-on-write).  As
		 * long as registration is infrequent, it performs fine.
		 */
		static void registerSerializer(ISerializer s, JsonRpcSerializer jrs) 
				throws SerializationException {
			Class[] classes = s.getSerializableClasses();
			ISerializer exists;
			synchronized (jrs) {
				Registry registry = jrs.m_registry;
				for (int i = 0; i < classes.length; i++) {
					exists = registry.m_serializableMap.get(classes[i]);
					if (exists != null && exists.getClass() != s.getClass()) {
						throw new SerializationException(
								"different serializer already registered for "
								+ classes[i].getName());
					}
				}
				
				if (!registry.m_serializerSet.contains(s)) {
					// need to create a new copy
					Registry copy = new Registry(registry);
					for (int i = 0; i < classes.length; i++) {
						copy.m_serializableMap.put(classes[i], s);
					}
					s.setOwner(jrs);
					copy.m_serializerSet.add(s);
					copy.m_serializerList.add(0, s);
					// replace the registry
					jrs.m_registry = copy;
				}
			}
		}
		
		/**
		 * Gets the correct serializer registered for the given type.  If none
		 * is found, it returns null.  This method is thread safe, and does not 
		 * synchronize.
		 */
		ISerializer getSerializer(Class clazz, Class jsoClazz) {
			
			ISerializer s = m_serializableMap.get(clazz);
			
			//==== Fix for BUGDB00644350 start ===
			if (s == null && Map.class.isAssignableFrom(clazz)) {
				s = m_serializableMap.get(Map.class);
			}
			if (s == null && List.class.isAssignableFrom(clazz)) {
				s = m_serializableMap.get(List.class);
			}
			//==== Fix for BUGDB00644350 end ===

			if (s != null && s.canSerialize(clazz, jsoClazz)) {
				return s;
			}
			for (ISerializer is : m_serializerList) {
				if (is.canSerialize(clazz, jsoClazz)) {
					return is;
				}
			}
			return null;
		}
	}

	private Class getClassFromHint(Object o) throws SerializationException {
		if (o == null) {
			return null;
		}
		if (o instanceof JsonObject) {
			try {
				String class_name = ((JsonObject) o).getString("javaClass");
				Class clazz = Class.forName(class_name);
				return clazz;
			} catch (Exception e) {
				throw new SerializationException("class in hint not found");
			}
		}
		if (o instanceof JsonArray) {
			JsonArray arr = (JsonArray) o;
			if (arr.length() == 0) {
				throw new SerializationException("no type for empty array");
			}
			// return type of first element
			Class compClazz = getClassFromHint(arr.get(0));
			try {
				if (compClazz.isArray()) {
					return Class.forName("[" + compClazz.getName());
				}
				return Class.forName("[L" + compClazz.getName() + ";");

			} catch (ClassNotFoundException e) {
				throw new SerializationException("problem getting array type");
			}
		}
		return o.getClass();
	}

	public ObjectMatch tryUnmarshall(SerializerState state, Class clazz,
			Object json) throws SerializationException {

		Class<?> cls = clazz;
		/*
		 * If we have a JSON object class hint that is a sub class of the
		 * signature 'clazz', then override 'clazz' with the hint class.
		 */
		if (cls != null && json instanceof JsonObject
				&& ((JsonObject) json).has("javaClass")
				&& cls.isAssignableFrom(getClassFromHint(json))) {
			cls = getClassFromHint(json);
		}

		if (cls == null) {
			cls = getClassFromHint(json);
		}
		if (cls == null) {
			throw new SerializationException("no class hint");
		}
		if (json == null || json == JsonObject.NULL) {
			if (!cls.isPrimitive()) {
				return ObjectMatch.NULL;
			}
			throw new SerializationException("can't assign null primitive");

		}
		ISerializer s = getSerializer(cls, json.getClass());
		if (s != null) {
			return s.tryUnmarshall(state, cls, json);
		}

		throw new SerializationException("no match");
	}

	public Object unmarshall(SerializerState state, Class clazz, Object json) throws SerializationException {

		Class<?> cls = clazz;
		/*
		 * If we have a JSON object class hint that is a sub class of the
		 * signature 'clazz', then override 'clazz' with the hint class.
		 */
		if (cls != null && json instanceof JsonObject
				&& ((JsonObject) json).has("javaClass")
				&& cls.isAssignableFrom(getClassFromHint(json))) {
			cls = getClassFromHint(json);
		}

		if (cls == null) {
			cls = getClassFromHint(json);
		}
		if (cls == null) {
			throw new SerializationException("no class hint");
		}
		if (json == null || json == JsonObject.NULL) {
			if (!cls.isPrimitive()) {
				return null;
			}
			throw new SerializationException("can't assign null primitive");

		}
		ISerializer s = getSerializer(cls, json.getClass());
		if (s != null) {
			return s.unmarshall(state, cls, json);
		}

		throw new SerializationException("can't unmarshall");
	}

//	public Object unmarshall(SerializerState state, Class clazz, Object json)
//			throws SerializationException {
//		return unmarshall(state, clazz, json, null);
//	}

	public Object unmarshall(SerializerState state, Class<?> clazz, Type type, Object json)
		throws SerializationException {
		/*
		 * If we have a JSON object class hint that is a sub class of the
		 * signature 'clazz', then override 'clazz' with the hint class.
		 */
		if (type != null && json instanceof JsonObject
				&& ((JsonObject) json).has("javaClass")
				&& clazz.isAssignableFrom(getClassFromHint(json))) {
			type = getClassFromHint(json);
		}

		if (type == null) {
			type = getClassFromHint(json);
		}
		if (type == null) {
			throw new SerializationException("no class hint");
		}
		if (json == null || json == JsonObject.NULL) {
			if (!type.getClass().isPrimitive()) {
				return null;
			}
			throw new SerializationException("can't assign null primitive");

		}
		ISerializer s = getSerializer(clazz, json.getClass());
		if (s != null) {
			// serializer needs generic info
			// TODO add Map support
			if (s instanceof ListSerializer) {
				return ((ListSerializer)s).unmarshall(state, clazz, type, json);
			}
			else {
			return s.unmarshall(state, clazz, json);
			}
		}

		throw new SerializationException("can't unmarshall");
	}
	
//	public Object unmarshall(SerializerState state, Class clazz, Type type, Object json)
//			throws SerializationException {
//		return unmarshall(state, clazz, type, json, null);
//	}

	public Object marshall(SerializerState state, Object o)
			throws SerializationException {
		if (o == null) {
			return JsonObject.NULL;
		}

		ISerializer s = getSerializer(o.getClass(), null);
		if (s != null) {
			return s.marshall(state, o);
		}
		throw new SerializationException("can't marshall "
				+ o.getClass().getName());
	}

	public String toJSON(Object o, boolean forceClassHintsInBeanMashalling) throws SerializationException {
		SerializerState state = new SerializerState();;
		Object json = marshall(state, o);
		return json.toString();
	}
	
	public String toJSON(Object o) throws SerializationException {
		return toJSON(o, false);
	}

	public Object fromJSON(String s, Class aClass, boolean forceClassHintsInBeanMashalling)
			throws SerializationException {
		JsonTokener tok = new JsonTokener(s);
		Object json;
		try {
			json = tok.nextValue();
		} catch (ParseException e) {
			throw new SerializationException("couldn't parse JSON");
		}
		SerializerState state = new SerializerState();
		state.setForceClassHintsInBeanMashalling(forceClassHintsInBeanMashalling);
//		state.setValidator(validator);
		return unmarshall(state, aClass, json);
	}

	public Object fromJSON(String s, Class aClass)
			throws SerializationException {
		return fromJSON(s, aClass, false);
	}

	public Object fromJSON(String s) throws SerializationException {
		return fromJSON(s, null);
	}

	// /**
	// * Should serializers defined in this object include the fully
	// * qualified class name of objects being serialized? This can
	// * be helpful when unmarshalling, though if not needed can
	// * be left out in favor of increased performance and smaller
	// * size of marshalled String. Default is true.
	// * @return
	// */
	// public boolean getMarshallClassHints() {
	// return m_marshallClassHints;
	// }
	//
	// /**
	// * Should serializers defined in this object include the fully
	// * qualified class name of objects being serialized? This can
	// * be helpful when unmarshalling, though if not needed can
	// * be left out in favor of increased performance and smaller
	// * size of marshalled String. Default is true.
	// * @return
	// */
	// public void setMarshallClassHints(boolean marshallClassHints) {
	// this.m_marshallClassHints = marshallClassHints;
	// }

	/**
	 * Returns true if attributes will null values should still be included in
	 * the serialized JSON object. Defaults to true. Set to false for
	 * performance gains and small JSON serialized size. Useful because null and
	 * undefined for JSON object attributes is virtually the same thing.
	 * 
	 * @return
	 */
	public boolean getMarshallNullAttributes() {
		return m_marshallNullAttributes;
	}

	/**
	 * Returns true if attributes will null values should still be included in
	 * the serialized JSON object. Defaults to true. Set to false for
	 * performance gains and small JSON serialized size. Useful because null and
	 * undefined for JSON object attributes is virtually the same thing.
	 * 
	 * @return
	 */
	public void setMarshallNullAttributes(boolean marshallNullAttributes) {
		this.m_marshallNullAttributes = marshallNullAttributes;
	}
}
