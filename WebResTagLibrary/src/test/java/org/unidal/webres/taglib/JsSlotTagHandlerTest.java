package org.unidal.webres.taglib;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.taglib.support.JettyTestSupport;
import org.unidal.webres.taglib.support.ResourceTemplateTestSupport;

public class JsSlotTagHandlerTest extends ResourceTemplateTestSupport {
   @AfterClass
   public static void afterClass() throws Exception {
      JettyTestSupport.shutdownServer();
   }

   @BeforeClass
   public static void beforeClass() throws Exception {
      JettyTestSupport.startServer(new JsSlotTagHandlerTest().copyResourceFrom("/JsSlotTagHandlerTest"));
   }

   @Override
   protected void configure() throws Exception {
      super.configure();

      getRegistry().register(String.class, ResourceConstant.Js.SharedUrlPrefix, "http://res.unidal.org/js");
      getRegistry().register(String.class, ResourceConstant.Js.SharedSecureUrlPrefix, "https://res.unidal.org/js");
   }

   @Override
   protected String getContextPath() {
      return "/test";
   }

   @Test
   public void testWithDeferRendering() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
            "<res:bean id=\"res\"/>" + //
            "<res:jsSlot id='head'/>" + //
            "<res:useJs id='js1' value='${res.js.local.ebaytime_js}' target='head'/>" + //
            "<res:useJs id='js2' value='${res.js.local.bitbybit.ebaytime_js}' target='head'/>" + //
            "<res:useJs id='js3' value='${res.js.shared.ebaytime_js}' target='body'/>" + //
            "<res:jsSlot id='body'/>" + //
            "<res:useJs id='js4' value='${res.js.shared.bitbybit.BitByBit_js}' target='body'/>" + //
            "<res:useJs id='js5' target='bottom'>//here is one js</res:useJs>" + //
            "<res:useJs id='js6' target='bottom'>//here is another js</res:useJs>" + //
            "<res:jsSlot id='bottom'>(function(){${this.content}})();</res:jsSlot>";
      String expected = "${MARKER,JsSlotTag:head}" + //
            "${MARKER,UseJsTag:js1}" + //
            "${MARKER,UseJsTag:js2}" + //
            "${MARKER,UseJsTag:js3}" + //
            "${MARKER,JsSlotTag:body}" + //
            "${MARKER,UseJsTag:js4}" + //
            "${MARKER,UseJsTag:js5}" + //
            "${MARKER,UseJsTag:js6}" + //
            "${MARKER,JsSlotTag:bottom}";

      try {
         super.enableDeferRendering();
         checkJspWithSource("testWithoutDeferRendering.jsp", template, expected);
      } finally {
         super.disableDeferRendering();
      }
   }

   @Test
   public void testWithoutDeferRendering() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
            "<res:bean id=\"res\"/>" + //
            "<res:jsSlot id='head'/>" + //
            "<res:useJs id='js1' value='${res.js.local.ebaytime_js}' target='head'/>" + //
            "<res:useJs id='js2' value='${res.js.local.bitbybit.ebaytime_js}' target='head'/>" + //
            "<res:useJs id='js3' value='${res.js.shared.ebaytime_js}' target='body'/>" + //
            "<res:jsSlot id='body'/>" + //
            "<res:useJs id='js4' value='${res.js.shared.bitbybit.BitByBit_js}' target='body'/>" + //
            "<res:useJs id='js5' target='bottom'>//here is one js</res:useJs>" + //
            "<res:useJs id='js6' target='bottom'>//here is another js</res:useJs>" + //
            "<res:jsSlot id='bottom'/>";
      String expected = "<script src=\"/test/js/ebaytime.js\" type=\"text/javascript\"></script>" + //
            "<script src=\"/test/js/bitbybit/ebaytime.js\" type=\"text/javascript\"></script>" + //
            "<script src=\"http://res.unidal.org/js/ebaytime.js\" type=\"text/javascript\"></script>" + //
            "<script src=\"http://res.unidal.org/js/bitbybit/BitByBit.js\" type=\"text/javascript\"></script>" + //
            "<script type=\"text/javascript\">//here is one js</script>" + //
            "<script type=\"text/javascript\">//here is another js</script>";

      checkJspWithSource("testWithoutDeferRendering.jsp", template, expected);
   }
}
