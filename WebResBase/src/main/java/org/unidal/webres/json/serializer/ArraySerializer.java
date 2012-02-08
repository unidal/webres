package org.unidal.webres.json.serializer;

import java.lang.reflect.Array;

import org.unidal.webres.json.JsonArray;
import org.unidal.webres.json.SerializationException;


public class ArraySerializer extends AbstractSerializer {
	private static Class<?>[] s_serializableClasses =
		new Class[] {
			int[].class,
			short[].class,
			long[].class,
			float[].class,
			double[].class,
			boolean[].class,
			Integer[].class,
			Short[].class,
			Long[].class,
			Float[].class,
			Double[].class,
			Boolean[].class,
			String[].class };

	private static Class<?>[] s_JSONClasses = new Class[] { JsonArray.class };

	public Class<?>[] getSerializableClasses() {
		return s_serializableClasses;
	}
	public Class<?>[] getJSONClasses() {
		return s_JSONClasses;
	}

	public boolean canSerialize(Class<?> clazz, Class<?> jsonClazz) {
		Class<?> cc = clazz.getComponentType();
		return (
			super.canSerialize(clazz, jsonClazz)
				|| ((jsonClazz == null || jsonClazz == JsonArray.class)
					&& (clazz.isArray() && !cc.isPrimitive())));
	}

	public ObjectMatch tryUnmarshall(
		SerializerState state,
		Class<?> clazz,
		Object o)
		throws SerializationException {
		JsonArray jso = (JsonArray) o;
		Class<?> cc = clazz.getComponentType();
		int i = 0;
		ObjectMatch m = new ObjectMatch(-1);
		try {
			for (; i < jso.length(); i++) {
				m = getOwner().tryUnmarshall(state, cc, jso.get(i)).max(m);
			}
		} catch (SerializationException e) {
			throw new SerializationException(
				"element " + i + " " + e.getMessage());
		}
		return m;
	}

	public Object unmarshall(SerializerState state, Class<?> clazz, Object o)
		throws SerializationException {
		JsonArray jso = (JsonArray) o;
		Class<?> cc = clazz.getComponentType();
		int i = 0;
		try {
			if (clazz == int[].class) {
				int arr[] = new int[jso.length()];
				for (; i < jso.length(); i++) {
					arr[i] =
						((Number) getOwner().unmarshall(state, cc, jso.get(i)))
							.intValue();
				}
				return (Object) arr;
			} else if (clazz == byte[].class) {
				byte arr[] = new byte[jso.length()];
				for (; i < jso.length(); i++) {
					arr[i] =
						((Number) getOwner().unmarshall(state, cc, jso.get(i)))
							.byteValue();
				}
				return (Object) arr;
			} else if (clazz == short[].class) {
				short arr[] = new short[jso.length()];
				for (; i < jso.length(); i++) {
					arr[i] =
						((Number) getOwner().unmarshall(state, cc, jso.get(i)))
							.shortValue();
				}
				return (Object) arr;
			} else if (clazz == long[].class) {
				long arr[] = new long[jso.length()];
				for (; i < jso.length(); i++) {
					arr[i] =
						((Number) getOwner().unmarshall(state, cc, jso.get(i)))
							.longValue();
				}
				return (Object) arr;
			} else if (clazz == float[].class) {
				float arr[] = new float[jso.length()];
				for (; i < jso.length(); i++) {
					arr[i] =
						((Number) getOwner().unmarshall(state, cc, jso.get(i)))
							.floatValue();
				}
				return (Object) arr;
			} else if (clazz == double[].class) {
				double arr[] = new double[jso.length()];
				for (; i < jso.length(); i++) {
					arr[i] =
						((Number) getOwner().unmarshall(state, cc, jso.get(i)))
							.doubleValue();
				}
				return (Object) arr;
			} else if (clazz == char[].class) {
				char arr[] = new char[jso.length()];
				for (; i < jso.length(); i++) {
					arr[i] =
						(
							(String) getOwner().unmarshall(
								state,
								cc,
								jso.get(i))).charAt(
							0);
				}
				return (Object) arr;
			} else if (clazz == boolean[].class) {
				boolean arr[] = new boolean[jso.length()];
				for (; i < jso.length(); i++) {
					arr[i] =
						((Boolean) getOwner()
							.unmarshall(state, cc, jso.get(i)))
							.booleanValue();
				}
				return (Object) arr;
			} else {
				Object arr[] =
					(Object[]) Array.newInstance(
						clazz.getComponentType(),
						jso.length());
				for (; i < jso.length(); i++) {
					arr[i] = getOwner().unmarshall(state, cc, jso.get(i));
				}
				return (Object) arr;
			}
		} catch (SerializationException e) {
			throw new SerializationException(
				"element " + i + " " + e.getMessage());
		}
	}

	public Object marshall(SerializerState state, Object o)
		throws SerializationException {
		JsonArray arr = new JsonArray();
		if (o instanceof int[]) {
			int a[] = (int[]) o;
			for (int i = 0; i < a.length; i++) {
				arr.put(a[i]);
			}
		} else if (o instanceof long[]) {
			long a[] = (long[]) o;
			for (int i = 0; i < a.length; i++) {
				arr.put(a[i]);
			}
		} else if (o instanceof short[]) {
			short a[] = (short[]) o;
			for (int i = 0; i < a.length; i++) {
				arr.put(a[i]);
			}
		} else if (o instanceof byte[]) {
			byte a[] = (byte[]) o;
			for (int i = 0; i < a.length; i++) {
				arr.put(a[i]);
			}
		} else if (o instanceof float[]) {
			float a[] = (float[]) o;
			for (int i = 0; i < a.length; i++) {
				arr.put(a[i]);
			}
		} else if (o instanceof double[]) {
			double a[] = (double[]) o;
			for (int i = 0; i < a.length; i++) {
				arr.put(a[i]);
			}
		} else if (o instanceof char[]) {
			char a[] = (char[]) o;
			for (int i = 0; i < a.length; i++) {
				arr.put(a[i]);
			}
		} else if (o instanceof boolean[]) {
			boolean a[] = (boolean[]) o;
			for (int i = 0; i < a.length; i++) {
				arr.put(a[i]);
			}
		} else if (o instanceof Object[]) {
			Object a[] = (Object[]) o;
			for (int i = 0; i < a.length; i++) {
				arr.put(getOwner().marshall(state, a[i]));
			}
		}
		return arr;
	}

}
