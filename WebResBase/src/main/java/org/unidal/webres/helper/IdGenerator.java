package org.unidal.webres.helper;

public final class IdGenerator {
	// Most pages need a very small number of generated id's so 200
	// is a "reasonable" cache.  The caching lookup is 50% faster than
	// the toHexString(...) etc... that was being used.  Look at the various
	// toXyz(...) methods on Long, Integer, etc..., they create arrays and
	// do a lot of work.
	private static final int MAX_CACHED = 200 ;
	private static final String[] s_ids = getCachedIds(MAX_CACHED) ;
	// Provide a max sequence number to prevent run off.
	public static final int MAX_SEQUENCE = 9999;
	private static final String PREFIX = "e4-";
	private String m_scope = "";
	private int m_htmlSequence = 0;

	public String nextHtmlId() {
		if (m_htmlSequence == MAX_SEQUENCE) {
			resetHtmlId();
		}
		return  PREFIX + m_scope + getId(m_htmlSequence++);
	}
	
	public void resetHtmlId() {
		m_htmlSequence = 0;
	}
	
	public void setScope(String scope) {
      if (scope==null) {
         return;
      }
      m_scope = scope.replaceAll("\\.", "_");
   }
   
   public void resetScope() {
      m_scope = "";
   }

	private static String getId(final int index) {
		if (index >= MAX_CACHED) {
			// toHexString(...) is pretty expensive...
			return Integer.toString(index) ;
		}
		return s_ids[index] ;
	}
	
	private static String[] getCachedIds(final int maxCached) {
		final String[] answer = new String[maxCached] ;
		for(int i = 0; i < maxCached; i++) answer[i] = Integer.toString(i) ;
		return answer ;
	}
}
