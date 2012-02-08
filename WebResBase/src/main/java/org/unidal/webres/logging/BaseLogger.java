package org.unidal.webres.logging;

public abstract class BaseLogger implements ILogger {

	private String m_name;

	private LogLevel m_level = LogLevel.INFO;
	
	
	public BaseLogger() {
	}

	public BaseLogger(String name) {
		m_name = name;
	}

	public String getName() {
		return m_name;
	}

	public LogLevel getLevel() {
		return m_level;
	}

	public void setLevel(LogLevel level) {
		m_level = level;
	}

	protected abstract void doLog(LogLevel level, String message, Throwable throwable);

	@Override
	public void log(LogLevel level, String message) {
		log(level, message, null);
	}
	
	@Override
	public void log(LogLevel level, String message, Throwable cause) {
		if (isLogEnabled(level)) {
			doLog(level, message, cause);
		}
	}

	@Override
	public void error(String message) {
		error(message, null);
	}

	@Override
	public void error(String message, Throwable cause) {
		log(LogLevel.ERROR, message, cause);
	}

	@Override
	public void info(String message) {
		info(message, null);
	}

	@Override
	public void info(String message, Throwable cause) {
		log(LogLevel.INFO, message, cause);
	}
	
	public boolean isLogEnabled(LogLevel level)
	{
		return m_level.ordinal() <= level.ordinal();
	}

	@Override
	public boolean isErrorEnabled() {
		return isLogEnabled(LogLevel.ERROR);
	}

	@Override
	public boolean isInfoEnabled() {
		return isLogEnabled(LogLevel.INFO);
	}

	@Override
	public boolean isWarnEnabled() {
		return isLogEnabled(LogLevel.WARN);
	}

	@Override
	public void warn(String message) {
		warn(message, null);
	}

	@Override
	public void warn(String message, Throwable cause) {
		log(LogLevel.WARN, message, cause);
	}
}
