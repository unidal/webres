package org.unidal.webres.taglib;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.link.LinkFactory;
import org.unidal.webres.taglib.support.JettyTestSupport;
import org.unidal.webres.taglib.support.ResourceTemplateTestSupport;

public class LinkTagHandlerTest extends ResourceTemplateTestSupport {
   @AfterClass
   public static void afterClass() throws Exception {
      JettyTestSupport.shutdownServer();
   }

   @BeforeClass
   public static void beforeClass() throws Exception {
      JettyTestSupport.startServer(new LinkTagHandlerTest());
   }

   @Override
   protected String getContextPath() {
      return "/test";
   }

   @Test
   public void testLinkWithEL() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.ebay.com/webres\" %>" + //
            "<res:bean id=\"res\"/>" + //
            "<res:link value='${res.link.pages.half.faq_html}'>FAQ</res:link>" + //
            "<res:link value='${res.link.pages.half.faq_html}' secure='true'>FAQ</res:link>";
      String expected = "<a href=\"http://pages.ebay.com/half/faq.html\">FAQ</a>" + //
            "<a href=\"https://pages.ebay.com/half/faq.html\">FAQ</a>";

      setAttribute("user", "guest");
      setAttribute("pwd", "guest");
      checkJspWithSource("linkWithEL.jsp", template, expected);
   }

   @Test
   public void testLinkWithRef() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.ebay.com/webres\" %>" + //
            "<res:link value='${ref}'>FAQ</res:link>";
      String expected = "<a href=\"http://pages.ebay.com/half/faq.html\">FAQ</a>";

      setAttribute("ref", LinkFactory.forRef().createPagesRef("/half/faq.html"));
      checkJspWithSource("linkWithRef.jsp", template, expected);
   }
}
