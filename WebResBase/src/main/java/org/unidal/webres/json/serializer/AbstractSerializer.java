package org.unidal.webres.json.serializer;


/**
 * Convenience class for implementing Serializers providing default
 * setOwner and canSerialize implementations.
 */

public abstract class AbstractSerializer implements ISerializer {
	
	private JsonRpcSerializer m_ser;
	private boolean m_marshallClassHints = true;

	protected JsonRpcSerializer getOwner() {
		return m_ser;
	}
	
	public void setOwner(JsonRpcSerializer aJSONSerializer) {
		m_ser = aJSONSerializer;
	}

	public boolean canSerialize(Class<?> clazz, Class<?> jsonClazz) {
		boolean canJava = false;
		boolean canJSON = false;

		Class<?> serializableClasses[] = getSerializableClasses();
		for (int i = 0; i < serializableClasses.length; i++) {
			if (clazz == serializableClasses[i]) {
				canJava = true;
			}
		}

		if (jsonClazz == null) {
			canJSON = true;
		} else {
			Class<?> jsonClasses[] = getJSONClasses();
			for (int i = 0; i < jsonClasses.length; i++) {
				if (jsonClazz == jsonClasses[i]){
					canJSON = true;
				}
			}
		}

		return (canJava && canJSON);
	}
	

	/**
	 * Should serializers defined in this object include the fully 
	 * qualified class name of objects being serialized?  This can 
	 * be helpful when unmarshalling, though if not needed can
	 * be left out in favor of increased performance and smaller 
	 * size of marshalled String.  Default is true.
	 * @return boolean
	 */
	public boolean isMarshallClassHints() {
		return m_marshallClassHints;
	}

	/**
	 * @param aB
	 */
	public void setMarshallClassHints(boolean aB) {
		m_marshallClassHints = aB;
	}

}
