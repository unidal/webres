package org.unidal.webres.json.serializer;

/**
 * This class is returned from the Serializer tryUnmarshall method to
 * indicate number of mismatched fields. This is used to handle ambiguities
 * with JavaScript's typeless objects combined with and Java's operator
 * overloading.
 */

public class ObjectMatch {
	public final static ObjectMatch OKAY = new ObjectMatch(-1);
	public final static ObjectMatch NULL = new ObjectMatch(0);

	private int m_mismatch;

	public ObjectMatch(int mismatch) {
		this.m_mismatch = mismatch;
	}

	public ObjectMatch max(ObjectMatch m) {
		if (this.m_mismatch > m.m_mismatch) {
			return this;
		}
		return m;
	}
}
