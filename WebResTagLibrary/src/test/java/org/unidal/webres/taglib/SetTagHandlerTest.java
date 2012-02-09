package org.unidal.webres.taglib;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.taglib.support.JettyTestSupport;
import org.unidal.webres.taglib.support.ResourceTemplateTestSupport;

public class SetTagHandlerTest extends ResourceTemplateTestSupport {
   @AfterClass
   public static void afterClass() throws Exception {
      JettyTestSupport.shutdownServer();
   }

   @BeforeClass
   public static void beforeClass() throws Exception {
      JettyTestSupport.startServer(new SetTagHandlerTest());
   }

   @Override
   protected void configure() throws Exception {
      super.configure();

      getRegistry().register(String.class, ResourceConstant.Image.SharedUrlPrefix, "http://res.unidal.org/img");
      getRegistry().register(String.class, ResourceConstant.Image.SharedSecureUrlPrefix, "https://res.unidal.org/img");
   }

   @Override
   protected String getContextPath() {
      return "/test";
   }

   @Test
   public void testShortcut() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
            "<res:bean id=\"res\"/>" + //
            "<res:set id='img' value='${res.img.shared}'/>" + //
            "<res:img value='${img.eBayLogo_gif}'/>" + //
            "<res:img value='${img.half.eBayLogo_gif}'/>";
      String expected = "<img src=\"http://res.unidal.org/img/eBayLogo.gif\" width=\"110\" height=\"45\">" + //
            "<img src=\"http://res.unidal.org/img/half/eBayLogo.gif\" width=\"110\" height=\"45\">";

      checkJspWithSource("testShortcut.jsp", template, expected);
   }
}
