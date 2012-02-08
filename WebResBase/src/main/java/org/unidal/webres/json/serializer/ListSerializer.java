package org.unidal.webres.json.serializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.unidal.webres.json.JsonArray;
import org.unidal.webres.json.JsonObject;
import org.unidal.webres.json.SerializationException;


public class ListSerializer extends AbstractSerializer {
	private static Class<?>[] s_serializableClasses =
		new Class[] {
			List.class,
			ArrayList.class,
			LinkedList.class,
			Vector.class };

	private static Class<?>[] s_JSONClasses = new Class[] { 
		JsonObject.class, 
		JsonArray.class };

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
					&& List.class.isAssignableFrom(clazz)));
	}

	public ObjectMatch tryUnmarshall(
		SerializerState state,
		Class<?> clazz,
		Object o)
		throws SerializationException {
		JsonObject jso = (JsonObject) o;
		String java_class = jso.getString("javaClass");
		if (java_class == null){
			throw new SerializationException("no type hint");
		}
		if (!(java_class.equals("java.util.List")
			|| java_class.equals("java.util.AbstractList")
			|| java_class.equals("java.util.LinkedList")
			|| java_class.equals("java.util.ArrayList")
			|| java_class.equals("java.util.Vector"))){
			throw new SerializationException("not a List");
		}
		JsonArray jsonlist = jso.getJSONArray("list");
		if (jsonlist == null){
			throw new SerializationException("list missing");
		}
		int i = 0;
		ObjectMatch m = new ObjectMatch(-1);
		try {
			for (; i < jsonlist.length(); i++) {
				m = getOwner().tryUnmarshall(state, null, jsonlist.get(i)).max(m);
			}
		} catch (SerializationException e) {
			throw new SerializationException(
				"element " + i + " " + e.getMessage());
		}
		return m;
	}
	public Object unmarshall(SerializerState state, Class<?> clazz, Object o)
		throws SerializationException {
		return unmarshall(state, clazz, null, o);
	}
	public Object unmarshall(SerializerState state, Class<?> clazz, Type type, Object o)
		throws SerializationException {
		
		if (o == null){
			throw new SerializationException("object is null");
		}
		
		JsonArray jsonlist = null;
		if(o.getClass() == JsonObject.class) {
			JsonObject jso = (JsonObject) o;
			String java_class = jso.getString("javaClass");
			if (java_class == null) {
				throw new SerializationException("no type hint");
			}
			jsonlist = (JsonArray)jso.get("list");
		}
		else if (o.getClass() == JsonArray.class){
			jsonlist = (JsonArray)o;
		}
		else {
			throw new SerializationException("object is not valid:" + o.getClass().getName());
		}

		AbstractList<Object> al = null;
		if (clazz == List.class
			|| clazz == AbstractList.class
			|| clazz == ArrayList.class) {
			al = new ArrayList<Object>();
		} else if (clazz == LinkedList.class) {
			al = new LinkedList<Object>();
		} else if (clazz == Vector.class) {
			al = new Vector<Object>();
		} else {
			throw new SerializationException("not a List");
		}
		
		if (jsonlist == null) {
			throw new SerializationException("list missing");
		}
		int i = 0;
		try {
			for (; i < jsonlist.length(); i++) {
				al.add(getOwner().unmarshall(state, getGenericParamType(type), jsonlist.get(i)));
			}
		} catch (SerializationException e) {
			throw new SerializationException(
				"element " + i + " " + e.getMessage());
		}
		return al;
	}

	@SuppressWarnings("rawtypes")
   public Object marshall(SerializerState state, Object o)
		throws SerializationException {
		List list = (List) o;
		JsonObject obj = new JsonObject();
		JsonArray arr = new JsonArray();
		//TODO: do we need metadata in json object?
		//if (getOwner().getMarshallClassHints()) {
		int index = 0;
		try {
			Iterator i = list.iterator();
			while (i.hasNext()) {
				arr.put(getOwner().marshall(state, i.next()));
				index++;
			}
		} catch (SerializationException e) {
			throw new SerializationException(
				"element " + index + " " + e.getMessage());
		}
		if(isMarshallClassHints() || state.isForceClassHintsInBeanMashalling()) {
			obj.put("javaClass", o.getClass().getName());
			obj.put("list", arr);
			return obj;
		}
		
		return arr;
	}

	private static final Class<?> getGenericParamType(Type genericParameterType) {
		try {
			if (genericParameterType instanceof ParameterizedType) {
				ParameterizedType aType = (ParameterizedType) genericParameterType;
				Type[] parameterArgTypes = aType.getActualTypeArguments();
				for (Type parameterArgType : parameterArgTypes) {
					if (parameterArgType instanceof Class) {
						Class<?> parameterArgClass = (Class<?>) parameterArgType;
						return parameterArgClass;
					}
				}
			}
			else if (genericParameterType instanceof Class) {
				Class<?> parameterArgClass = (Class<?>) genericParameterType;
				// not if type of t
				if (!List.class.isAssignableFrom(parameterArgClass)) {
					return parameterArgClass;
				}
			}
		} catch (SecurityException e) {
			// ignore
		}
		return null;
	}
}
