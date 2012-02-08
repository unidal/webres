package org.unidal.webres.json.serializer;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.unidal.webres.json.JsonObject;
import org.unidal.webres.json.SerializationException;


public class BeanSerializer extends AbstractSerializer {

	private static Class<?>[] s_serializableClasses = new Class[] {};
	private static Class<?>[] s_JSONClasses = new Class[] {};

	private static volatile WeakHashMap<Class<?>, BeanData> s_beanDataCache = new WeakHashMap<Class<?>, BeanData>();

	public Class<?>[] getSerializableClasses() {
		return s_serializableClasses;
	}

	public Class<?>[] getJSONClasses() {
		return s_JSONClasses;
	}

	public boolean canSerialize(Class<?> clazz, Class<?> jsonClazz) {
		return (!clazz.isArray() && !clazz.isPrimitive()
				&& !clazz.isInterface() && !clazz.isEnum() // leave it to
															// EnumSerializer
		&& (jsonClazz == null || jsonClazz == JsonObject.class));
	}

	public static class BeanData {
		// in absence of getters and setters, these fields are
		// public to allow subclasses to access.
		public Map<String, Method> m_readableProps;
		public Map<String, Method> m_writableProps;
	}

	protected static class BeanSerializerState {
		// in absence of getters and setters, these fields are
		// public to allow subclasses to access.

		// Circular reference detection
		public Map<Object, Object> m_beanSet = new HashMap<Object, Object>();
	}

	private static BeanData createBeanData(Class<?> clazz)
			throws IntrospectionException {

		BeanData bd = new BeanData();
		PropertyDescriptor[] props = Introspector.getBeanInfo(clazz,
				Object.class).getPropertyDescriptors();
		bd.m_readableProps = new HashMap<String, Method>();
		bd.m_writableProps = new HashMap<String, Method>();
		for (int i = 0; i < props.length; i++) {
			if (props[i].getWriteMethod() != null) {
				bd.m_writableProps.put(props[i].getName(),
						props[i].getWriteMethod());
			}
			if (props[i].getReadMethod() != null) {
				bd.m_readableProps.put(props[i].getName(),
						props[i].getReadMethod());
			}
		}
		return bd;
	}

	public static BeanData getBeanData(Class<?> clazz)
			throws IntrospectionException {

		BeanData bd = s_beanDataCache.get(clazz);
		if (bd != null) {
			return bd;
		}
		synchronized (BeanSerializer.class) {
			bd = s_beanDataCache.get(clazz);
			if (bd != null) {
				return bd;
			}
			bd = createBeanData(clazz);
			WeakHashMap<Class<?>, BeanData> newCache = new WeakHashMap<Class<?>, BeanData>(
					s_beanDataCache.size() + 1);
			newCache.putAll(s_beanDataCache);
			newCache.put(clazz, bd);
			s_beanDataCache = newCache;
			return bd;
		}
	}

	public ObjectMatch tryUnmarshall(SerializerState state, Class<?> clazz,
			Object o) throws SerializationException {
		JsonObject jso = (JsonObject) o;
		BeanData bd = null;
		try {
			bd = getBeanData(clazz);
		} catch (IntrospectionException e) {
			throw new SerializationException(clazz.getName() + " is not a bean");
		}

		int match = 0, mismatch = 0;
		Iterator<Map.Entry<String, Method>> i = bd.m_writableProps.entrySet()
				.iterator();
		while (i.hasNext()) {
			Map.Entry<String, Method> ent = i.next();
			String prop = ent.getKey();
			if (jso.has(prop)) {
				match++;
			} else {
				mismatch++;
			}
		}
		if (match == 0) {
			throw new SerializationException("bean has no matches");
		}

		ObjectMatch m = null, tmp = null;
		Iterator<String> keyIter = jso.keys();
		while (keyIter.hasNext()) {
			String field = keyIter.next();
			Method setMethod = bd.m_writableProps.get(field);
			if (setMethod != null) {
				try {
					Class<?> param[] = setMethod.getParameterTypes();
					if (param.length != 1) {
						throw new SerializationException("bean "
								+ clazz.getName() + " method "
								+ setMethod.getName()
								+ " does not have one arg");
					}
					tmp = getOwner().tryUnmarshall(state, param[0],
							jso.get(field));
					if (m == null) {
						m = tmp;
					} else {
						m = m.max(tmp);
					}
				} catch (SerializationException e) {
					throw new SerializationException("bean " + clazz.getName()
							+ " " + e.getMessage());
				}
			} else {
				mismatch++;
			}
		}
		return m.max(new ObjectMatch(mismatch));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
   public Object unmarshall(SerializerState state, Class<?> clazz, Object o)
			throws SerializationException {
		JsonObject jso = (JsonObject) o;
		BeanData bd = null;
		// IRequestValidator validator = state.getValidator();
		// if (validator == null)
		// validator = new IRequestValidator() {
		//
		// @Override
		// public boolean isParamOk(String param) {
		// return true;
		// }
		//
		// @Override
		// public boolean isRequiredParamMissing(List<String> params) {
		// return false;
		// }
		// };
		try {
			bd = getBeanData(clazz);
		} catch (IntrospectionException e) {

			String msg = clazz.getName() + " is not a bean";
			throw new SerializationException(msg);
			// throw new SerializationException (
			// clazz.getName() + " is not a bean");
		}

		Object instance = null;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			String msg = "can't instantiate bean " + clazz.getName() + ": "
					+ e.getMessage();
			throw new SerializationException(msg);
			// throw new SerializationException (
			// "can't instantiate bean "
			// + clazz.getName()
			// + ": "
			// + e.getMessage());
		}
		Object invokeArgs[] = new Object[1];
		Object fieldVal;
		Iterator<String> i = jso.keys();
		List<String> params = new ArrayList<String>();
		while (i.hasNext()) {
			String field = i.next();
			params.add(field);
			Method setMethod = bd.m_writableProps.get(field);
			if (setMethod != null) {
				try {
					Class<?> param[] = setMethod.getParameterTypes();
					Type genericParam[] = setMethod.getGenericParameterTypes();
					fieldVal = getOwner().unmarshall(state, param[0],
							genericParam[0], jso.get(field));
				} catch (SerializationException e) {
					String msg = "bean " + clazz.getName() + " "
							+ e.getMessage();
					throw new SerializationException(msg);
				}
				invokeArgs[0] = fieldVal;
				try {
					try {
						setMethod.invoke(instance, invokeArgs);
					} catch (InvocationTargetException ite) {
						throw ite.getTargetException();
					}
				} catch (Throwable e) {
					String msg = "bean " + clazz.getName() + "can't invoke "
							+ setMethod.getName() + ": " + e.getMessage();
					throw new SerializationException(msg);
					// throw new SerializationException(
					// "bean "
					// + clazz.getName()
					// + "can't invoke "
					// + setMethod.getName()
					// + ": "
					// + e.getMessage());
				}
			} else { // throw error if method is not avail

				// check if get method is present and check to see if List
				Method getMethod = bd.m_readableProps.get(field);
				if (null != getMethod) {
					try {
						ParameterizedType returnType = (ParameterizedType) getMethod
								.getGenericReturnType();
						Type[] actualType = returnType.getActualTypeArguments();
						Class<?> returnClass = (Class<?>) returnType
								.getRawType();
						if (List.class.isAssignableFrom(returnClass)) {
							fieldVal = getOwner().unmarshall(state,
									returnClass, actualType[0], jso.get(field));
							try {
								// add all to list using get method
								List list = (List) getMethod.invoke(instance);
								List list2 = (List) fieldVal;
								list.addAll(list2);
							} catch (InvocationTargetException e) {
								throw new SerializationException(e.getMessage());
							} catch (IllegalArgumentException e) {
								throw new SerializationException(e.getMessage());
							} catch (IllegalAccessException e) {
								throw new SerializationException(e.getMessage());
							}
						}
					} catch (SerializationException e) {
						String msg = "bean " + clazz.getName() + " "
								+ e.getMessage();
						throw new SerializationException(msg);
					}

				}
			}
		}
		// if (validator.isRequiredParamMissing(params)) {
		// String msg = "bean " + clazz.getName() +
		// " is missing a required param";
		// SerializationException ex = new SerializationException(msg);
		// ServiceEngineError error = new ServiceEngineError(
		// IServiceEngineErrorConstants.MISSING_PARAMETER_ERROR,
		// msg,IServiceEngineErrorConstants.SEVERITY);
		// ex.setError(error);
		// throw ex;
		// }

		return instance;
	}

	public Object marshall(SerializerState state, Object o)
			throws SerializationException {
		BeanSerializerState beanState;
		try {
			beanState = (BeanSerializerState) state
					.get(BeanSerializerState.class);
		} catch (Exception e) {
			throw new SerializationException("bean serializer internal error");
		}
		// Integer identity = new Integer(System.identityHashCode(o));
		if (beanState.m_beanSet.get(o) == o) {
			throw new SerializationException("circular reference");
		}
		beanState.m_beanSet.put(o, o);

		Class<?> clazz = o.getClass();
		BeanData bd = null;
		try {
			bd = getBeanData(clazz);
		} catch (IntrospectionException e) {
			throw new SerializationException(o.getClass().getName()
					+ " is not a bean");
		}

		JsonObject val = new JsonObject();
		// if (getOwner().getMarshallClassHints()) {
		if (isMarshallClassHints() || state.isForceClassHintsInBeanMashalling()) {
			val.put("javaClass", clazz.getName());
		}
		Iterator<Map.Entry<String, Method>> i = bd.m_readableProps.entrySet()
				.iterator();
		Object args[] = new Object[0];
		Object result = null;
		while (i.hasNext()) {
			Map.Entry<String, Method> ent = i.next();
			String prop = ent.getKey();
			Method getMethod = ent.getValue();
			boolean isAcc = getMethod.isAccessible();
			if (!getMethod.isAccessible()
					&& Modifier.isPublic(getMethod.getModifiers())) {
				getMethod.setAccessible(true);
			}

			try {
				try {
					result = getMethod.invoke(o, args);
				} catch (InvocationTargetException ite) {
					throw ite.getTargetException();
				}
			} catch (Throwable e) {
				throw new SerializationException("bean "
						+ o.getClass().getName() + " can't invoke "
						+ getMethod.getName() + ": " + e.getMessage());
			}
			getMethod.setAccessible(isAcc);
			try {
				if (result != null || getOwner().getMarshallNullAttributes()) {
					val.put(prop, getOwner().marshall(state, result));
				}
			} catch (SerializationException e) {
				throw new SerializationException("bean "
						+ o.getClass().getName() + " " + e.getMessage());
			}
		}

		beanState.m_beanSet.remove(o);
		return val;
	}
}
