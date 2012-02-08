package org.unidal.webres.json;

import java.io.UnsupportedEncodingException;

import org.unidal.webres.json.serializer.ArraySerializer;
import org.unidal.webres.json.serializer.BeanSerializer;
import org.unidal.webres.json.serializer.BooleanSerializer;
import org.unidal.webres.json.serializer.DateSerializer;
import org.unidal.webres.json.serializer.DictionarySerializer;
import org.unidal.webres.json.serializer.EnumSerializer;
import org.unidal.webres.json.serializer.JsonRpcSerializer;
import org.unidal.webres.json.serializer.ListSerializer;
import org.unidal.webres.json.serializer.MapSerializer;
import org.unidal.webres.json.serializer.NumberSerializer;
import org.unidal.webres.json.serializer.PrimitiveSerializer;
import org.unidal.webres.json.serializer.SetSerializer;
import org.unidal.webres.json.serializer.StringSerializer;


/**
 * @author tneel
 */
public class JsonSerializer {

	private static JsonSerializer s_instance;
	private JsonRpcSerializer m_rpcSerializer;

	public static JsonSerializer getInstance() {
		if (s_instance == null) {
			s_instance = new JsonSerializer();
		}
		return s_instance;
	}

	/**
	 * 
	 */
	private JsonSerializer() {
		super();
		try {
			m_rpcSerializer = new JsonRpcSerializer();
			/* registering default serializers */ 		
			m_rpcSerializer.registerSerializer(new ArraySerializer());
			m_rpcSerializer.registerSerializer(new DictionarySerializer());
			m_rpcSerializer.registerSerializer(new MapSerializer());
			m_rpcSerializer.registerSerializer(new SetSerializer());
			ListSerializer listSer = new ListSerializer();
			listSer.setMarshallClassHints(false);
			m_rpcSerializer.registerSerializer(listSer);
			m_rpcSerializer.registerSerializer(new DateSerializer());
			m_rpcSerializer.registerSerializer(new StringSerializer());
			m_rpcSerializer.registerSerializer(new NumberSerializer());
			m_rpcSerializer.registerSerializer(new BooleanSerializer());
			m_rpcSerializer.registerSerializer(new PrimitiveSerializer());
			m_rpcSerializer.registerSerializer(new EnumSerializer());
			/* Do not add the class name in the JSON string */
			BeanSerializer bSer = new BeanSerializer();
			bSer.setMarshallClassHints(false);
			m_rpcSerializer.registerSerializer(bSer);
			//m_rpcSerializer.registerSerializer(new VjoObjSerializer());
		} catch (SerializationException e) {
			//TODO
		}
	}

	/* (non-Javadoc)
	 * @see com.ebay.webres.services.Serializer#deserialize(String, Class)
	 */
	public Object deserialize(final String aContent, final Class<?> aClass)
			throws SerializationException
	{
		return m_rpcSerializer.fromJSON(aContent, aClass);
	}

	/* (non-Javadoc)
	 * @see com.ebay.webres.services.Serializer#serialize(java.lang.Object)
	 */
	public String serialize(final Object aSource)
		throws SerializationException, UnsupportedEncodingException
	{
		String str = m_rpcSerializer.toJSON(aSource);
		return str;
		//return (str == null) ? null : str.getBytes("utf-8");
	}
	
	/* (non-Javadoc)
	 * @see com.ebay.webres.services.Serializer#deserialize(String, Class)
	 */
	public Object deserialize(final String aContent, final Class<?> aClass, boolean forceClassHintsInBeanMashalling)
			throws SerializationException {
		return m_rpcSerializer.fromJSON(aContent, aClass, forceClassHintsInBeanMashalling);
	}

	/* (non-Javadoc)
	 * @see com.ebay.webres.services.Serializer#serialize(java.lang.Object)
	 */
	public String serialize(final Object aSource, boolean forceClassHintsInBeanMashalling)
		throws SerializationException, UnsupportedEncodingException {
		return m_rpcSerializer.toJSON(aSource, forceClassHintsInBeanMashalling);
		//return (str == null) ? null : str.getBytes("utf-8");
	}
	
	
	public org.unidal.webres.json.serializer.ISerializer getSerializer(final Object obj) {
		if (obj==null) {
			return null;
		}
		return m_rpcSerializer.getSerializer(obj.getClass(), null);
	}
}