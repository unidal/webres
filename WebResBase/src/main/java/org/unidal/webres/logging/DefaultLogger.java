package org.unidal.webres.logging;

class DefaultLogger extends BaseLogger {

   private Class<?> m_source;

   public DefaultLogger(Class<?> source) {
      super(source.getSimpleName());
      m_source = source;
   }

   ILogger m_logger;
   
   @Override
   protected void doLog(LogLevel level, String message, Throwable throwable) {
      if (m_logger == null) {
         m_logger = LoggerFactory.getLoggerFactory().getLogger(m_source);
      }
      m_logger.log(level, message, throwable);
   }
}
