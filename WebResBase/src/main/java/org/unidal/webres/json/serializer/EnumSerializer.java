package org.unidal.webres.json.serializer;

import org.unidal.webres.json.JsonObject;
import org.unidal.webres.json.SerializationException;

public class EnumSerializer extends AbstractSerializer {
	private static final Class<?>[] s_serializableClasses = new Class[] {
		Enum.class};
	private static final Class<?>[] s_JSONClasses = new Class[] { 
		JsonObject.class};
	@Override
	public boolean canSerialize(Class<?> clazz, Class<?> jsonClazz) {
		return clazz.isEnum();
	}
	@Override
	public Class<?>[] getJSONClasses() {
		return s_JSONClasses;
	}
	@Override
	public Class<?>[] getSerializableClasses() {
		return s_serializableClasses;
	}
	@Override
	public Object marshall(SerializerState state, Object o)
			throws SerializationException {
		JsonObject obj = new JsonObject();
		Enum<?> enum1 = (Enum<?>)o;
		obj.put(enum1.name(), enum1.ordinal());
		return obj;
	}
	@Override
	public ObjectMatch tryUnmarshall(SerializerState state, Class<?> clazz,
			Object json) throws SerializationException {
		if (null != unmarshall(state, clazz, json)) {
			return ObjectMatch.OKAY;
		}
		else {
			return ObjectMatch.NULL;
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
   @Override
	public Object unmarshall(SerializerState state, Class<?> clazz, Object json)
			throws SerializationException {
		if (json == null){
			throw new SerializationException("object is null");
		}
		Object object = null;
		JsonObject jsonObject = (JsonObject)json;
		if (null != jsonObject) {
			if (Enum.class.isAssignableFrom(clazz)) {
				object = Enum.valueOf((Class<? extends Enum>)clazz, jsonObject.keys().next());
			}
		}
		return object;
	}
}
