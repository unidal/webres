package org.unidal.webres.json.serializer;

import java.sql.Timestamp;
import java.util.Date;

import org.unidal.webres.json.JsonObject;
import org.unidal.webres.json.SerializationException;


public class DateSerializer extends AbstractSerializer {
	
	private static Class<?>[] s_serializableClasses =
		new Class[] { Date.class, Timestamp.class, java.sql.Date.class };
	private static Class<?>[] s_JSONClasses = new Class[] { JsonObject.class };
	
	private String m_javaClass = "javaClass";	

	public Class<?>[] getSerializableClasses() {
		return s_serializableClasses;
	}
	public Class<?>[] getJSONClasses() {
		return s_JSONClasses;
	}

	public ObjectMatch tryUnmarshall(
		SerializerState state,
		Class<?> clazz,
		Object o)
		throws SerializationException {
		JsonObject jso = (JsonObject) o;
		String java_class = jso.getString(m_javaClass);
		if (java_class == null){
			throw new SerializationException("no type hint");
		}
		if (!(java_class.equals("java.util.Date"))){
			throw new SerializationException("not a Date");
		}
		return ObjectMatch.OKAY;
	}

	public Object unmarshall(SerializerState state, Class<?> clazz, Object o)
		throws SerializationException {
		
		Class<?> cls = clazz;	
		JsonObject jso = (JsonObject) o;
		long time = jso.getLong("time");
		if (jso.has(m_javaClass)) {
			try {
				cls = Class.forName(jso.getString(m_javaClass));
			} catch (ClassNotFoundException cnfe) {
				throw new SerializationException(cnfe.getMessage());
			}
		}
		if (Date.class.equals(cls)) {
			return new Date(time);
		} else if (Timestamp.class.equals(cls)) {
			return new Timestamp(time);
		} else if (java.sql.Date.class.equals(cls)) {
			return new java.sql.Date(time);
		}
		throw new SerializationException("invalid class " + cls);
	}

	public Object marshall(SerializerState state, Object o)
		throws SerializationException {
		long time;
		if (o instanceof Date) {
			time = ((Date) o).getTime();
		} else {
			throw new SerializationException(
				"cannot marshall date using class " + o.getClass());
		}
		JsonObject obj = new JsonObject();
		//if (getOwner().getMarshallClassHints()) {
		if(isMarshallClassHints()) {
			obj.put(m_javaClass, o.getClass().getName());
		}
		obj.put("time", time);
		return obj;
	}

}
