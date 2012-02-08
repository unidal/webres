package org.unidal.webres.logging;

/**
 * Logger Factory Interface
 * 
 * @author XShao
 */
public interface ILoggerFactory {
	public ILogger getLogger(Class<?> source);
}
