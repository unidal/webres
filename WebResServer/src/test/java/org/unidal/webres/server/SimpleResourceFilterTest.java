package org.unidal.webres.server;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.servlet.Context;
import org.unidal.webres.taglib.support.JettyTestSupport;
import org.unidal.webres.taglib.support.ResourceTemplateTestSupport;

public class SimpleResourceFilterTest extends ResourceTemplateTestSupport {
   @AfterClass
   public static void afterClass() throws Exception {
      JettyTestSupport.shutdownServer();
   }

   @BeforeClass
   public static void beforeClass() throws Exception {
      JettyTestSupport.startServer(new SimpleResourceFilterTest().copyResourceFrom("/SimpleResourceFilterTest"));
   }

   @Override
   protected void configure() throws Exception {
      super.configure();

      getContext().addServlet(SimpleResourceServlet.class, "/f/*");
      getContext().addServlet(SimpleResourceServlet.class, "/z/*");
      getContext().addFilter(SimpleResourceFilter.class, "*.jsp", Handler.ALL);
   }

   @Override
   protected String getContextPath() {
      return "/test";
   }

   @Override
   protected void postConfigure(Context ctx) {
      new SimpleResourceConfigurator().configure(getRegistry());

      super.postConfigure(ctx);
   }

   @Test
   public void testAjaxDedup() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
               "<res:bean id=\"res\"/>" + //
               "<res:img value='${res.img.shared.eBayLogo_gif}'/>" + //
               "<res:useJs>//here is js</res:useJs>" + //
               "<res:useCss>.here{is:css;}</res:useCss>" + //
               "<res:useJs id='myjs'>//here is my js</res:useJs>" + //
               "<res:useCss id='mycss'>.here{is:my css;}</res:useCss>" + //
               "<res:useJs value='${res.js.shared.ebaytime_js}'/>" + //
               "<res:useCss value='${res.css.shared.ebaytime_css}'/>" + //
               "jsToken=<res:token type='js'/>, cssToken=<res:token type='css'/>";
      String expected = "<img src=\"/test/f/img/shared/eBayLogo.gif\" width=\"110\" height=\"45\">" + //
               "<script src=\"/test/f/js/inline/635253329\" type=\"text/javascript\"></script>" + //
               "<link href=\"/test/f/css/inline/927456434\" type=\"text/css\" rel=\"stylesheet\">" + //
               "<script src=\"/test/f/js/inline/myjs\" type=\"text/javascript\"></script>" + //
               "<link href=\"/test/f/css/inline/mycss\" type=\"text/css\" rel=\"stylesheet\">" + //
               "<script src=\"/test/f/js/shared/ebaytime.js\" type=\"text/javascript\"></script>" + //
               "<link href=\"/test/f/css/shared/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">" + //
               "jsToken=194d613, cssToken=73e33c71";
      // ajax token is not supported without defer rendering

      checkJspWithSource("testAjaxDedup.jsp", template, expected);

      String template2 = template + //
               "<res:useJs>//here is another js</res:useJs>";
      String expected2 = "<img src=\"/test/f/img/shared/eBayLogo.gif\" width=\"110\" height=\"45\">" + //
               "<link href=\"/test/f/css/inline/927456434\" type=\"text/css\" rel=\"stylesheet\">" + //
               "<link href=\"/test/f/css/inline/mycss\" type=\"text/css\" rel=\"stylesheet\">" + //
               "<link href=\"/test/f/css/shared/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">" + //
               "jsToken=f57dc5, cssToken=73e33c71" + //
               "<script src=\"/test/f/js/inline/32279060\" type=\"text/javascript\"></script>";

      checkJspWithSource("testAjaxDedup-1.jsp", template2, expected2, SimpleResourceFilter.JS_TOKEN + "=194d613", null);

      String template3 = template + //
               "<res:useCss>.here{is:another css;}</res:useCss>";
      String expected3 = "<img src=\"/test/f/img/shared/eBayLogo.gif\" width=\"110\" height=\"45\">" + //
               "jsToken=194d613, cssToken=5250e37a" + //
               "<link href=\"/test/f/css/inline/1852945323\" type=\"text/css\" rel=\"stylesheet\">";

      checkJspWithSource("testAjaxDedup-2.jsp", template3, expected3, SimpleResourceFilter.JS_TOKEN + "=194d613&"
               + SimpleResourceFilter.CSS_TOKEN + "=73e33c71", null);
   }

   @Test
   public void testCssSlot() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
               "<res:bean id=\"res\"/>" + //
               "<res:cssSlot id='head'/>" + //
               "<res:useCss value='${res.css.local.ebaytime_css}' target='head'/>" + //
               "<res:useCss value='${res.css.local.bitbybit.ebaytime_css}' target='head'/>" + //
               "<res:useCss value='${res.css.shared.ebaytime_css}' target='body'/>" + //
               "<res:cssSlot id='body'/>" + //
               "<res:useCss value='${res.css.shared.bitbybit.ebaytime_css}' target='body'/>" + //
               "<res:useCss target='bottom'>.here{is:css;}</res:useCss>" + //
               "<res:useCss target='bottom'>.here{is:another css}</res:useCss>" + //
               "<res:useCss target='unknown'>.here{is:inline css}</res:useCss>" + //
               "<res:cssSlot id='bottom'/>";
      String expected = "<link href=\"/test/z/css/aggregated/head?urns=css.local:/ebaytime_css|" + //
               "css.local:/bitbybit/ebaytime_css\" type=\"text/css\" rel=\"stylesheet\">" + //
               "<link href=\"/test/z/css/aggregated/body?urns=css.shared:/ebaytime_css|" + //
               "css.shared:/bitbybit/ebaytime_css\" type=\"text/css\" rel=\"stylesheet\">" + //
               "<link href=\"/test/f/css/inline/228999666\" type=\"text/css\" rel=\"stylesheet\">" + //
               "<link href=\"/test/z/css/aggregated/bottom?urns=css.inline:/927456434|css.inline:/1879890282\" " + //
               "type=\"text/css\" rel=\"stylesheet\">";

      checkJspWithSource("testCssSlot.jsp", template, expected);
   }

   @Test
   public void testCssSlotInline() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
               "<res:bean id=\"res\"/>" + //
               "<res:cssSlot id='head' renderType='inline'/>" + //
               "<res:useCss value='${res.css.local.ebaytime_css}' target='head'/>" + //
               "<res:useCss value='${res.css.local.bitbybit.ebaytime_css}' target='head'/>" + //
               "<res:useCss value='${res.css.shared.ebaytime_css}' target='body'/>" + //
               "<res:cssSlot id='body' renderType='inline'/>" + //
               "<res:useCss value='${res.css.shared.bitbybit.ebaytime_css}' target='body'/>" + //
               "<res:useCss target='bottom'>.here{is:css;}</res:useCss>" + //
               "<res:useCss target='bottom'>.here{is:another css}</res:useCss>" + //
               "<res:useCss target='unknown'>.here{is:inline css}</res:useCss>" + //
               "<res:cssSlot id='bottom' renderType='inline'/>";
      String expected = "<style type=\"text/css\">.header {color:#00c; font-size:12px}\r\n" + //
               ".titleColor {color:red}\r\n" + //
               ".fs {width:450px}\r\n" + //
               ".error {font-weight:bold; color:red}\r\n" + //
               "</style><style type=\"text/css\">.footer {color:#00c; font-size:12px}\r\n" + //
               ".titleColor {color:red}\r\n" + //
               ".fs {width:450px}\r\n" + //
               ".error {font-weight:bold; color:red}\r\n" + //
               "</style>" + //
               "<link href=\"/test/f/css/inline/228999666\" type=\"text/css\" rel=\"stylesheet\">" + //
               "<style type=\"text/css\">.here{is:css;}\r\n" + //
               ".here{is:another css}\r\n" + //
               "</style>";

      checkJspWithSource("testCssSlotInline.jsp", template, expected);
   }

   @Test
   public void testJsSlot() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
               "<res:bean id=\"res\"/>" + //
               "<res:jsSlot id='head'/>" + //
               "<res:useJs value='${res.js.local.ebaytime_js}' target='head'/>" + //
               "<res:useJs value='${res.js.local.bitbybit.ebaytime_js}' target='head'/>" + //
               "<res:useJs value='${res.js.shared.ebaytime_js}' target='body'/>" + //
               "<res:jsSlot id='body'/>" + //
               "<res:useJs value='${res.js.shared.bitbybit.ebaytime_js}' target='body'/>" + //
               "<res:useJs target='bottom'>//here is one js</res:useJs>" + //
               "<res:useJs target='bottom'>//here is another js</res:useJs>" + //
               "<res:useJs target='unknown'>//here is inline js</res:useJs>" + //
               "<res:jsSlot id='bottom'/>";
      String expected = "<script src=\"/test/z/js/aggregated/head?urns=js.local:/ebaytime_js|" + //
               "js.local:/bitbybit/ebaytime_js\" type=\"text/javascript\"></script>" + //
               "<script src=\"/test/z/js/aggregated/body?urns=js.shared:/ebaytime_js|" + //
               "js.shared:/bitbybit/ebaytime_js\" type=\"text/javascript\"></script>" + //
               "<script src=\"/test/f/js/inline/1638248182\" type=\"text/javascript\"></script>" + //
               "<script src=\"/test/z/js/aggregated/bottom?urns=js.inline:/1409716905|js.inline:/32279060\" " + //
               "type=\"text/javascript\"></script>";

      checkJspWithSource("testJsSlot.jsp", template, expected);
   }

   @Test
   public void testJsSlotInline() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
               "<res:bean id=\"res\"/>" + //
               "<res:jsSlot id='head' renderType='inline'/>" + //
               "<res:useJs value='${res.js.local.ebaytime_js}' target='head'/>" + //
               "<res:useJs value='${res.js.local.bitbybit.ebaytime_js}' target='head'/>" + //
               "<res:useJs value='${res.js.shared.ebaytime_js}' target='body'/>" + //
               "<res:jsSlot id='body' renderType='inline'/>" + //
               "<res:useJs value='${res.js.shared.bitbybit.ebaytime_js}' target='body'/>" + //
               "<res:useJs target='bottom'>//here is one js</res:useJs>" + //
               "<res:useJs target='bottom'>//here is another js</res:useJs>" + //
               "<res:useJs target='unknown'>//here is inline js</res:useJs>" + //
               "<res:jsSlot id='bottom' renderType='inline'>(function(){${this.content}})();</res:jsSlot>";
      String expected = "<script type=\"text/javascript\">// local js here\r\n" + //
               "function showTimezoneInSharedBitByBitJs(){\r\n" + //
               "   var date = new Date();\r\n" + //
               "   var timezone = date.getTimezoneOffset()/60 * (-1);\r\n" + //
               "   if(timezone>0){\r\n" + //
               "      alert(\"Your Timezone: +\"+timezone);\r\n" + //
               "   } else {\r\n" + //
               "      alert(\"Your Timezone: \"+timezone);\r\n" + //
               "   }\r\n" + //
               "}\r\n" + //
               "\r\n" + //
               "</script><script type=\"text/javascript\">// shared js here\r\n" + //
               "function showTimezoneInSharedBitByBitJs(){\r\n" + //
               "   var date = new Date();\r\n" + //
               "   var timezone = date.getTimezoneOffset()/60 * (-1);\r\n" + //
               "   if(timezone>0){\r\n" + //
               "      alert(\"Your Timezone: +\"+timezone);\r\n" + //
               "   } else {\r\n" + //
               "      alert(\"Your Timezone: \"+timezone);\r\n" + //
               "   }\r\n" + //
               "}\r\n" + //
               "\r\n" + //
               "</script>" + //
               "<script src=\"/test/f/js/inline/1638248182\" type=\"text/javascript\"></script>" + //
               "<script type=\"text/javascript\">(function(){//here is one js\r\n" + //
               "//here is another js\r\n" + //
               "})();</script>";

      checkJspWithSource("testJsSlotInline.jsp", template, expected);
   }
}
