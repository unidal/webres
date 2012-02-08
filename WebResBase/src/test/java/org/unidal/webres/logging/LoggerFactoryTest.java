package org.unidal.webres.logging;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.unidal.webres.logging.ILogger;
import org.unidal.webres.logging.LoggerFactory;

public class LoggerFactoryTest {

	protected PrintStream setOut(ByteArrayOutputStream out) {
		PrintStream origOut = System.out;
		System.setOut(new PrintStream(out, true));
		return origOut;
	}

	@Test
	public void testGetLogger() {
		ILogger log = LoggerFactory.getLogger(getClass());
		assertTrue(log.isWarnEnabled());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream origOut = setOut(baos);
		log.info("Test Get Logger Info");
//		assertEquals("", baos.toString());

		log.warn("Test Get Logger Warning");

		String str = baos.toString();
		assertTrue(str.indexOf("Test Get Logger Warning") > 0);
		assertTrue(str.indexOf("WARN") > 0);

		System.setOut(origOut);
		
		ILogger log2 = LoggerFactory.getLogger(getClass());
		log2.warn("Test Get Logger Warning");
	}
	
	public static class SimpleLoggerFactory implements ILoggerFactory {

      @Override
      public ILogger getLogger(Class<?> source) {
         return new SimpleLogger(source.getSimpleName());
      }
	   
	}
	
	public static class SimpleLogEntry  {
	   public LogLevel m_level;
	   public String m_message;
	   public Throwable m_throwable;
	   
	   
	   public SimpleLogEntry(LogLevel level, String message, Throwable throwable) {
	      m_level = level;
	      m_message = message;
	      m_throwable = throwable;
	   }
	}
	
	public static class SimpleLogger extends BaseLogger {
	   
	   public SimpleLogger(String name) {
         super(name);
      }

      public List<SimpleLogEntry> entries = new ArrayList<SimpleLogEntry>();
	   
      @Override
      protected void doLog(LogLevel level, String message, Throwable throwable) {
         entries.add(new SimpleLogEntry(level, message, throwable));
      }
	}
	
	
	@Test
	public void testLoggerFactory() {
	   try {
	      LoggerFactory.setLoggerFactoryClass(SimpleLoggerFactory.class.getName());
	      
	      ILogger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);
	      assertTrue(logger instanceof DefaultLogger);
	      DefaultLogger dl = (DefaultLogger)logger;
	      assertEquals("LoggerFactoryTest", dl.getName());
	      
	      dl.info("Simple Info");
	      dl.warn("Simple Info 2", new Exception("Simple Exception"));
	      
	      SimpleLogger sl = (SimpleLogger)dl.m_logger;
	      assertEquals(2, sl.entries.size());
	      SimpleLogEntry entry1 = sl.entries.get(0);
	      assertEquals("Simple Info", entry1.m_message);
	      assertEquals(LogLevel.INFO, entry1.m_level);
	      SimpleLogEntry entry2 = sl.entries.get(1);
	      assertEquals(LogLevel.WARN, entry2.m_level);
	      assertEquals("Simple Exception", entry2.m_throwable.getMessage());
	   }
	   finally {
	      LoggerFactory.setLoggerFactory(ConsoleLoggerFactory.INSTANCE);
	   }
	   
	}
	
	private static class TestDefaultLoggerClass {
	   
	}
	
	@Test
   public void testDefaultLogger() {
	   DefaultLogger defaultLogger = (DefaultLogger)LoggerFactory.getLogger(TestDefaultLoggerClass.class);
	   assertNull(defaultLogger.m_logger);

	   defaultLogger.info("Logger before setting NewLoggerFactory");
	   assertNotNull(defaultLogger.m_logger);
	   try {      
         LoggerFactory.setLoggerFactoryClass(SimpleLoggerFactory.class.getName());
         
         ILogger logger = (ILogger)LoggerFactory.getLogger(TestDefaultLoggerClass.class);
         
         assertFalse(logger == defaultLogger);
         
         assertTrue(logger instanceof DefaultLogger);
         DefaultLogger dl = (DefaultLogger)logger;
         assertEquals("TestDefaultLoggerClass", dl.getName());
         
         dl.info("Simple Info");
         dl.warn("Simple Info 2", new Exception("Simple Exception"));
         
         SimpleLogger sl = (SimpleLogger)dl.m_logger;
         assertEquals(2, sl.entries.size());
         SimpleLogEntry entry1 = sl.entries.get(0);
         assertEquals("Simple Info", entry1.m_message);
         assertEquals(LogLevel.INFO, entry1.m_level);
         SimpleLogEntry entry2 = sl.entries.get(1);
         assertEquals(LogLevel.WARN, entry2.m_level);
         assertEquals("Simple Exception", entry2.m_throwable.getMessage());
      }
      finally {
         LoggerFactory.setLoggerFactory(ConsoleLoggerFactory.INSTANCE);
      }
   }
}
