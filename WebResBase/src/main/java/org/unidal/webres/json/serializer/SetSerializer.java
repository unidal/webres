package org.unidal.webres.json.serializer;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.unidal.webres.json.JsonObject;
import org.unidal.webres.json.SerializationException;



@SuppressWarnings("rawtypes")
public class SetSerializer extends AbstractSerializer {
	private static Class<?>[] s_serializableClasses =
		new Class[] { Set.class, HashSet.class, TreeSet.class };

	private static Class<?>[] s_JSONClasses = new Class[] { JsonObject.class };

	public Class<?>[] getSerializableClasses() {
		return s_serializableClasses;
	}
	public Class<?>[] getJSONClasses() {
		return s_JSONClasses;
	}

	public boolean canSerialize(Class<?> clazz, Class<?> jsonClazz) {
		return (
			super.canSerialize(clazz, jsonClazz)
				|| ((jsonClazz == null || jsonClazz == JsonObject.class)
					&& Set.class.isAssignableFrom(clazz)));
	}

	public ObjectMatch tryUnmarshall(
		SerializerState state,
		Class<?> clazz,
		Object o)
		throws SerializationException {
		JsonObject jso = (JsonObject) o;
		String java_class = jso.getString("javaClass");
		if (java_class == null) {
			throw new SerializationException("no type hint");
		}
		if (!(java_class.equals("java.util.Set")
			|| java_class.equals("java.util.AbstractSet")
			|| java_class.equals("java.util.TreeSet")
			|| java_class.equals("java.util.HashSet"))) {
			throw new SerializationException("not a Set");
		}
		JsonObject jsonset = jso.getJSONObject("set");
		if (jsonset == null) {
			throw new SerializationException("set missing");
		}
		ObjectMatch m = new ObjectMatch(-1);

		Iterator i = jsonset.keys();
		String key = null;
		try {
			while (i.hasNext()) {
				key = (String) i.next();
				m =
					getOwner().tryUnmarshall(
						state,
						null,
						jsonset.get(key)).max(
						m);
			}
		} catch (SerializationException e) {
			throw new SerializationException(
				"key " + key + " " + e.getMessage());
		}
		return m;
	}

   public Object unmarshall(SerializerState state, Class<?> clazz, Object o)
		throws SerializationException {
		JsonObject jso = (JsonObject) o;
		String java_class = jso.getString("javaClass");
		if (java_class == null) {
			throw new SerializationException("no type hint");
		}
		AbstractSet<Object> abset = null;
		if (java_class.equals("java.util.Set")
			|| java_class.equals("java.util.AbstractSet")
			|| java_class.equals("java.util.HashSet")) {
			abset = new HashSet<Object>();
		} else if (java_class.equals("java.util.TreeSet")) {
			abset = new TreeSet<Object>();
		} else {
			throw new SerializationException("not a Set");
		}
		JsonObject jsonset = jso.getJSONObject("set");

		if (jsonset == null) {
			throw new SerializationException("set missing");
		}
		Iterator i = jsonset.keys();
		String key = null;

		try {
			while (i.hasNext()) {
				key = (String) i.next();
				Object setElement = jsonset.get(key);
				abset.add(getOwner().unmarshall(state, null, setElement));
			}
		} catch (SerializationException e) {
			throw new SerializationException("key " + i + e.getMessage());
		}
		return abset;
	}

	public Object marshall(SerializerState state, Object o)
		throws SerializationException {
		Set set = (Set) o;

		//JsonObject obj = new JsonObject();
		JsonObject setdata = new JsonObject();
		//if (getOwner().getMarshallClassHints()) {
		/*if(isMarshallClassHints()) {
			obj.put("javaClass", o.getClass().getName());
		}*/
		//obj.put("set", setdata);
		Object key = null;
		Iterator i = set.iterator();

		try {
			while (i.hasNext()) {
				key = i.next();
				// only support String keys
				setdata.put(key.toString(), getOwner().marshall(state, key));
			}
		} catch (SerializationException e) {
			throw new SerializationException("set key " + key + e.getMessage());
		}
		return setdata;
		//return obj;
	}

}
