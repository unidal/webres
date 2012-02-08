package org.unidal.webres.logging;

/*
 * "[esf_logger] WARN : 2011-06-01 06:36:24 Test Get Logger Warning"
 */
public class ConsoleLogger extends BaseLogger {

	public ConsoleLogger(String loggerName) {
		super(loggerName);
	}

	protected void doLog(LogLevel level, String message, Throwable cause) {
		StringBuilder sb = new StringBuilder(256);
//		String threadName = Thread.currentThread().getName();
		long time = System.currentTimeMillis();
//		String loggerName = getName();
		LogFormatter.format(sb, null, null, level, time, message, cause);
		System.out.println(sb.toString());
	}
}
