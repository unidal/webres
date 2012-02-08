package org.unidal.webres.logging;

import org.unidal.webres.helper.Reflects;

public final class LoggerFactory {
	private static ILoggerFactory m_factory = ConsoleLoggerFactory.INSTANCE;

	public static ILogger getLogger(Class<?> source) {
		return new DefaultLogger(source);
	}

	public static void setLoggerFactoryClass(String loggerClass) {
		Class<?> factoryClass = Reflects.forClass().getClass(loggerClass);
		if (factoryClass == null) {
			throw new IllegalArgumentException(
					"Can't loading the ILoggerFactory:" + loggerClass);
		}
		Object factory = Reflects.forConstructor().createInstance(factoryClass);
		if (!(factory instanceof ILoggerFactory)) {
			if (factory == null)
				throw new IllegalArgumentException(
						"Can't create the ILoggerFactory:" + loggerClass);
			else
				throw new IllegalArgumentException(
						"The class is not an instance of ILoggerFactory:"
								+ factory);
		}
		setLoggerFactory((ILoggerFactory) factory);
	}

	public static void setLoggerFactory(ILoggerFactory loggerFactory) {
		m_factory = loggerFactory;
	}

	public static ILoggerFactory getLoggerFactory() {
		return m_factory;
	}
}
