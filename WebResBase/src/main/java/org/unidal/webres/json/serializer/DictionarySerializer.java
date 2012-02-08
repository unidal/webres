package org.unidal.webres.json.serializer;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.unidal.webres.json.JsonObject;
import org.unidal.webres.json.SerializationException;


public class DictionarySerializer extends AbstractSerializer {
	private static Class<?>[] s_serializableClasses =
		new Class[] { Hashtable.class };

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
					&& Dictionary.class.isAssignableFrom(clazz)));
	}

	@SuppressWarnings("rawtypes")
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
		if (!(java_class.equals("java.util.Dictionary")
			|| java_class.equals("java.util.Hashtable"))) {
			throw new SerializationException("not a Dictionary");
		}
		JsonObject jsonmap = jso.getJSONObject("map");
		if (jsonmap == null) {
			throw new SerializationException("map missing");
		}
		ObjectMatch m = new ObjectMatch(-1);
		Iterator i = jsonmap.keys();
		String key = null;
		try {
			while (i.hasNext()) {
				key = (String) i.next();
				m =
					getOwner().tryUnmarshall(
						state,
						null,
						jsonmap.get(key)).max(
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
		if (java_class == null){
			throw new SerializationException("no type hint");
		}
		Hashtable<String, Object> ht = null;
		if (java_class.equals("java.util.Dictionary")
			|| java_class.equals("java.util.Hashtable")) {
			ht = new Hashtable<String, Object>();
		} else {
			throw new SerializationException("not a Dictionary");
		}
		JsonObject jsonmap = jso.getJSONObject("map");
		if (jsonmap == null){
			throw new SerializationException("map missing");
		}
		Iterator<String> i = jsonmap.keys();
		String key = null;
		try {
			while (i.hasNext()) {
				key = i.next();
				ht.put(
					key,
					getOwner().unmarshall(state, null, jsonmap.get(key)));
			}
		} catch (SerializationException e) {
			throw new SerializationException(
				"key " + key + " " + e.getMessage());
		}
		return ht;
	}

	@SuppressWarnings("rawtypes")
   public Object marshall(SerializerState state, Object o)
		throws SerializationException {
		Dictionary ht = (Dictionary) o;
		//JsonObject obj = new JsonObject();
		JsonObject mapdata = new JsonObject();
		//if (getOwner().getMarshallClassHints()) {
		/*if(isMarshallClassHints()) {
			obj.put("javaClass", o.getClass().getName());
		}*/
		//obj.put("map", mapdata);
		Object key = null;
		Object val = null;
		try {
			Enumeration en = ht.keys();
			while (en.hasMoreElements()) {
				key = en.nextElement();
				val = ht.get(key);
				// only support String keys
				mapdata.put(key.toString(), getOwner().marshall(state, val));
			}
		} catch (SerializationException e) {
			throw new SerializationException(
				"map key " + key + " " + e.getMessage());
		}
		return mapdata;
		//return obj;
	}

}
