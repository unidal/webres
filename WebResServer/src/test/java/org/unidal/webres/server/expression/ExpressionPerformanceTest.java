package org.unidal.webres.server.expression;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.server.support.SimpleServerSupport;
import org.unidal.webres.taglib.support.JettyTestSupport;

public class ExpressionPerformanceTest extends SimpleServerSupport {
   @AfterClass
   public static void afterClass() throws Exception {
      JettyTestSupport.shutdownServer();
   }

   @BeforeClass
   public static void beforeClass() throws Exception {
      JettyTestSupport.startServer(new ExpressionPerformanceTest().copyResourceFrom("/ExpressionPerformanceTest"));
   }

   @Override
   protected boolean isKeepGenerated() {
      return true;
   }

   @Test
//   @Ignore
   public void testEL() throws IOException {
      int num = 100000;
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
            "<res:bean id=\"res\"/>" + //
            "<% for (int i=0;i<" + num + ";i++) { %>" + //
            "${res.css.local.ebaytime_css}" + //
            "<% } %>";

      // ResourceExpressionOptimizer.initialize(null);

      long start = System.nanoTime();
      checkJspWithSource("testEL.jsp", template, null);
      System.out.println("Time used: " + (System.nanoTime() - start) / 1e6 + " ms");
   }
}
