package org.unidal.webres.json;

/**
 * Thrown by Serializer objects when they are unable to Unmarshall the JSON
 * objects into Java objects.
 */

public class SerializationException extends Exception {
	/**
    * 
    */
   private static final long serialVersionUID = -1374467696293796848L;

   public SerializationException(String msg) {
		super(msg);
	}
}
