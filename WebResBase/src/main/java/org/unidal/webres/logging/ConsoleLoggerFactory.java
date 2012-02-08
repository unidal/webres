package org.unidal.webres.logging;

public enum ConsoleLoggerFactory implements ILoggerFactory {
	INSTANCE;
	
	private static final String LOG_NAME = "esf_logger";

	private ILogger m_defaultLogger = new ConsoleLogger(LOG_NAME);

	@Override
	public ILogger getLogger(Class<?> source) {
		return m_defaultLogger;
	}

	public void setLogger(ILogger logger) {
		m_defaultLogger = logger;
	}
}
