package org.unidal.webres.logging;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class LoggerTest {

   @Test
   public void testThrowable() {
      ILogger logger = LoggerFactory.getLogger(LoggerTest.class);
      
      PrintStream orig = System.out;
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream newOut = new PrintStream(baos);
      try {
         System.setOut(newOut);
         logger.info("This is a test information", new Exception("A test exception"));
         String str = baos.toString();
         assertTrue(str.indexOf("This is a test information") > 0);
      }
      finally {
         System.setOut(orig);
      }
   }
   
   @Test
   public void testIsInfo() {
      ILogger logger = LoggerFactory.getLogger(LoggerTest.class);
      assertTrue(logger.isInfoEnabled());
      if (logger.isInfoEnabled()) {
         logger.info("This is a test information");
      }
   }
}
