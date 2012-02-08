package org.unidal.webres.logging;

public interface ILogger {

	public boolean isInfoEnabled();

	public boolean isWarnEnabled();

	public boolean isErrorEnabled();
	
	public boolean isLogEnabled(LogLevel level);

	public void info(String message);

	public void info(String message, Throwable cause);

	public void warn(String message);

	public void warn(String message, Throwable cause);

	public void error(String message);

	public void error(String message, Throwable cause);

	public void log(LogLevel level, String message);
	
	public void log(LogLevel level, String message, Throwable cause);
}
