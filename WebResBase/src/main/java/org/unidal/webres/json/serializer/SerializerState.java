package org.unidal.webres.json.serializer;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used by Serializers to hold state during marshalling and
 * unmarshalling.
 */

public class SerializerState {
	
	private Map<Class<?>, Object> m_stateMap = new HashMap<Class<?>, Object>();
//	private IRequestValidator m_validator;
	
	public Object get(Class<?> clazz)
		throws InstantiationException, IllegalAccessException {
		
		Object obj = m_stateMap.get(clazz);
		if (obj != null){
			return obj;
		}
		obj = clazz.newInstance();
		m_stateMap.put(clazz, obj);
		return obj;
	}
	
	private boolean m_forceClassHintsInBeanMashalling = false;;

	public boolean isForceClassHintsInBeanMashalling() {
		return m_forceClassHintsInBeanMashalling;
	}
	
	public void setForceClassHintsInBeanMashalling(boolean set) {
		m_forceClassHintsInBeanMashalling = set;
	}

//	public IRequestValidator getValidator() {
//		return m_validator;
//	}
//
//	public void setValidator(IRequestValidator validator) {
//		this.m_validator = validator;
//	}

}
