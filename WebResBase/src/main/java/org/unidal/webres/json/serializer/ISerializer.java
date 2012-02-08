package org.unidal.webres.json.serializer;

import org.unidal.webres.json.SerializationException;

/**
 * Interface to be implemented by custom serializer objects that convert
 * to and from Java objects and JSON objects.
 */
public interface ISerializer {
	public void setOwner(JsonRpcSerializer ser);

	public Class<?>[] getSerializableClasses();
	public Class<?>[] getJSONClasses();

	public boolean canSerialize(Class<?> clazz, Class<?> jsonClazz);

	public ObjectMatch tryUnmarshall(
		SerializerState state,
		Class<?> clazz,
		Object json)
		throws SerializationException;

	public Object unmarshall(SerializerState state, Class<?> clazz, Object json)
		throws SerializationException;
	

	public Object marshall(SerializerState state, Object o)
		throws SerializationException;
}
