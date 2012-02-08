package org.unidal.webres.taglib;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.js.JsFactory;
import org.unidal.webres.taglib.support.JettyTestSupport;
import org.unidal.webres.taglib.support.ResourceTemplateTestSupport;

public class UseJsTagHandlerTest extends ResourceTemplateTestSupport {
   @AfterClass
   public static void afterClass() throws Exception {
      JettyTestSupport.shutdownServer();
   }

   @BeforeClass
   public static void beforeClass() throws Exception {
      JettyTestSupport.startServer(new UseJsTagHandlerTest().copyResourceFrom("/UseJsTagHandlerTest"));
   }

   @Override
   protected void configure() throws Exception {
      super.configure();

      getRegistry().register(String.class, ResourceConstant.Js.SharedUrlPrefix, "http://res.ebay.com/js");
      getRegistry().register(String.class, ResourceConstant.Js.SharedSecureUrlPrefix, "https://res.ebay.com/js");
   }

   @Override
   protected String getContextPath() {
      return "/test";
   }

   @Test
   public void testUseJsWithEL() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.ebay.com/webres\" %>" + //
            "<res:bean id=\"res\"/>" + //
            "<res:useJs value='${res.js.local.ebaytime_js}'/>" + //
            "<res:useJs value='${res.js.local.ebaytime_js}' secure='true'/>" + //
            "<res:useJs value='${res.js.shared.ebaytime_js}'/>" + //
            "<res:useJs value='${res.js.shared.ebaytime_js}' secure='true'/>" + //
            "<res:useJs>here is one js</res:useJs>" + //
            "<res:useJs>here is another js</res:useJs>";
      String expected = "<script src=\"/test/js/ebaytime.js\" type=\"text/javascript\"></script>" + //
            "<script src=\"/test/js/ebaytime.js\" type=\"text/javascript\"></script>" + //
            "<script src=\"http://res.ebay.com/js/ebaytime.js\" type=\"text/javascript\">" + //
            "</script><script src=\"https://res.ebay.com/js/ebaytime.js\" type=\"text/javascript\"></script>" + //
            "<script type=\"text/javascript\">here is one js</script>" + //
            "<script type=\"text/javascript\">here is another js</script>";

      checkJspWithSource("testUseJsWithEL.jsp", template, expected);
   }

   @Test
   public void testUseJsWithAttributesOverride() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.ebay.com/webres\" %>" + //
            "<res:bean id=\"res\"/>" + //
            "<res:useJs value='${res.js.local.ebaytime_js}' _id='myid' _target='mytarget' _secure='secure' _unknown='unknown' _other='other'/>" + //
            "<res:useJs>here is another js</res:useJs>";
      String expected = "<script src=\"/test/js/ebaytime.js\" type=\"text/javascript\" id=\"myid\" target=\"mytarget\" secure=\"secure\" _unknown=\"unknown\" _other=\"other\"></script>" + //
      		"<script type=\"text/javascript\">here is another js</script>";

      checkJspWithSource("testUseJsWithAttributesOverride.jsp", template, expected);
   }

   @Test
   public void testUseJsWithRef() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.ebay.com/webres\" %>" + //
            "<res:useJs value='${ref}'/>";
      String expected = "<script src=\"/test/js/bitbybit/ebaytime.js\" type=\"text/javascript\"></script>";

      setAttribute("ref", JsFactory.forRef().createLocalRef("/bitbybit/ebaytime.js"));
      checkJspWithSource("testUseJsWithRef.jsp", template, expected);
   }
}
