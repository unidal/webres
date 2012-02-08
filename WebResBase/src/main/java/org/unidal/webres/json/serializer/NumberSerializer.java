package org.unidal.webres.json.serializer;

import org.unidal.webres.json.SerializationException;


public class NumberSerializer extends AbstractSerializer {
	private static Class<?>[] s_serializableClasses =
		new Class[] {
			Integer.class,
			Byte.class,
			Short.class,
			Long.class,
			Float.class,
			Double.class };

	private static Class<?>[] s_JSONClasses =
		new Class[] {
			Integer.class,
			Byte.class,
			Short.class,
			Long.class,
			Float.class,
			Double.class,
			String.class };

	public Class<?>[] getSerializableClasses() {
		return s_serializableClasses;
	}
	public Class<?>[] getJSONClasses() {
		return s_JSONClasses;
	}

	public ObjectMatch tryUnmarshall(
		SerializerState state,
		Class<?> clazz,
		Object jso)
		throws SerializationException {
		try {
			toNumber(clazz, jso);
		} catch (NumberFormatException e) {
			throw new SerializationException("not a number");
		}
		return ObjectMatch.OKAY;
	}

	public Object toNumber(Class<?> clazz, Object jso)
		throws NumberFormatException {
		if (clazz == Integer.class) {
			if (jso instanceof String) {
				return new Integer((String) jso);
			} 
			return new Integer(((Number) jso).intValue());
			
		} else if (clazz == Long.class) {
			if (jso instanceof String) {
				return new Long((String) jso);
			}
			return new Long(((Number) jso).longValue());
			
		} else if (clazz == Short.class) {
			if (jso instanceof String) {
				return new Short((String) jso);
			} 
			return new Short(((Number) jso).shortValue());
			
		} else if (clazz == Byte.class) {
			if (jso instanceof String) {
				return new Byte((String) jso);
			}
			return new Byte(((Number) jso).byteValue());
			
		} else if (clazz == Float.class) {
			if (jso instanceof String) {
				return new Float((String) jso);
			}
			return new Float(((Number) jso).floatValue());
			
		} else if (clazz == Double.class) {
			if (jso instanceof String) {
				return new Double((String) jso);
			} 
			return new Double(((Number) jso).doubleValue());
			
		}
		return null;
	}

	public Object unmarshall(SerializerState state, Class<?> clazz, Object jso)
		throws SerializationException {
		try {
			if (jso == null || "".equals(jso)){
				return null;
			}
			return toNumber(clazz, jso);
		} catch (NumberFormatException nfe) {
			throw new SerializationException(
				"cannot convert object " + jso + " to type " + clazz.getName());
		}
	}

	public Object marshall(SerializerState state, Object o)
		throws SerializationException {
		return o;
	}

}
