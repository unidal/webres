package org.unidal.webres.json.serializer;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.unidal.webres.json.JsonObject;
import org.unidal.webres.json.SerializationException;


@SuppressWarnings("rawtypes")
public class MapSerializer extends AbstractSerializer {
	private static Class<?>[] s_serializableClasses =
		new Class[] { Map.class, HashMap.class, TreeMap.class };

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
					&& Map.class.isAssignableFrom(clazz)));
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
		if (!(java_class.equals("java.util.Map")
			|| java_class.equals("java.util.AbstractMap")
			|| java_class.equals("java.util.LinkedHashMap")
			|| java_class.equals("java.util.TreeMap")
			|| java_class.equals("java.util.HashMap"))) {
			throw new SerializationException("not a Map");
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
				m = getOwner().tryUnmarshall(state, null, jsonmap.get(key)).max(m);
			}
		} catch (SerializationException e) {
			throw new SerializationException(
				"key " + key + " " + e.getMessage());
		}
		return m;
	}

	public Object unmarshall(SerializerState state, Class clazz, Object o)
		throws SerializationException {
//		JsonObject jso = (JsonObject) o;
//		String java_class = jso.getString("javaClass");
//		if (java_class == null) {
//			throw new SerializationException("no type hint");
//		}
		AbstractMap<String, Object> abmap = null;
		if (clazz == Map.class
			|| clazz == AbstractMap.class
			|| clazz == HashMap.class) {
			abmap = new HashMap<String, Object>();
		} else if (clazz == TreeMap.class) {
			abmap = new TreeMap<String, Object>();
		} else {
			throw new SerializationException("not a Map");
		}
		JsonObject jsonmap = (JsonObject)o;
//		if (jsonmap == null) {
//			throw new SerializationException("map missing");
//		}
		Iterator i = jsonmap.keys();
		String key = null;
		try {
			while (i.hasNext()) {
				key = (String) i.next();
				abmap.put(key, getOwner().unmarshall(state, null, jsonmap.get(key)));
			}
		} catch (SerializationException e) {
			throw new SerializationException(
				"key " + key + " " + e.getMessage());
		}
		return abmap;
	}

	public Object marshall(SerializerState state, Object o)
		throws SerializationException {
		Map map = (Map) o;
		//JsonObject obj = new JsonObject();
		JsonObject mapdata = new JsonObject();
		//if (getOwner().getMarshallClassHints()) {
		/*if(isMarshallClassHints()) {
			obj.put("javaClass", o.getClass().getName());
		}
		obj.put("map", mapdata);*/
		Object key = null;
		Object val = null;
		try {
			Iterator i = map.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry ent = (Map.Entry) i.next();
				key = ent.getKey();
				val = ent.getValue();
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
