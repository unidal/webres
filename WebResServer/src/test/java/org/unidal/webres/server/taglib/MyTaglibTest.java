package org.unidal.webres.server.taglib;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.servlet.Context;

import org.unidal.webres.server.SimpleResourceConfigurator;
import org.unidal.webres.server.SimpleResourceFilter;
import org.unidal.webres.server.SimpleResourceServlet;
import org.unidal.webres.taglib.support.JettyTestSupport;
import org.unidal.webres.taglib.support.ResourceTemplateTestSupport;

public class MyTaglibTest extends ResourceTemplateTestSupport {
   @AfterClass
   public static void afterClass() throws Exception {
      JettyTestSupport.shutdownServer();
   }

   @BeforeClass
   public static void beforeClass() throws Exception {
      JettyTestSupport.startServer(new MyTaglibTest().copyResourceFrom("/MyTaglibTest"));
   }

   @Override
   protected void configure() throws Exception {
      super.configure();

      generateTldFile("my", MyTagLibrary.class);

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
   public void testCss() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
            "<%@ taglib prefix=\"my\" uri=\"http://www.examples.com/mytaglib\" %>" + //
            "<res:bean id=\"res\"/>" + //
            "<res:cssSlot id='head'/>" + //
            "<res:useCss value='${res.css.local.ebaytime_css}' target='head'/>" + //
            "<my:res type='css' value='${res.css.local.bitbybit.ebaytime_css}' target='head'/>" + //
            "<res:useCss value='${res.css.shared.ebaytime_css}' target='body'/>" + //
            "<res:cssSlot id='body'/>" + //
            "<my:res type='css' value='${res.css.shared.bitbybit.ebaytime_css}' target='body'/>" + //
            "<res:useCss target='bottom'>.here{is:css;}</res:useCss>" + //
            "<res:useCss target='bottom'>.here{is:another css}</res:useCss>" + //
            "<res:useCss target='unknown'>.here{is:inline css}</res:useCss>" + //
            "<res:cssSlot id='bottom'/>";
      String expected1 = "<link href=\"/test/z/css/aggregated/head?urns=css.local:/ebaytime_css|" + //
            "css.local:/bitbybit/ebaytime_css\" type=\"text/css\" rel=\"stylesheet\">" + //
            "<link href=\"/test/z/css/aggregated/body?urns=css.shared:/ebaytime_css|" + //
            "css.shared:/bitbybit/ebaytime_css\" type=\"text/css\" rel=\"stylesheet\">" + //
            "<link href=\"/test/f/css/inline/228999666\" type=\"text/css\" rel=\"stylesheet\">" + //
            "<link href=\"/test/z/css/aggregated/bottom?urns=css.inline:/927456434|css.inline:/1879890282\" " + //
            "type=\"text/css\" rel=\"stylesheet\">";

      enableDeferRendering();
      checkJspWithSource("testCss.jsp", template, expected1);

      String expected2 = "<link href=\"/test/css/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">" + //
            "<link href=\"/test/css/bitbybit/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">" + //
            "<link href=\"/test/f/css/shared/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">" + //
            "<link href=\"/test/f/css/shared/bitbybit/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">" + //
            "<link href=\"/test/f/css/inline/927456434\" type=\"text/css\" rel=\"stylesheet\">" + //
            "<link href=\"/test/f/css/inline/1879890282\" type=\"text/css\" rel=\"stylesheet\">" + //
            "<link href=\"/test/f/css/inline/228999666\" type=\"text/css\" rel=\"stylesheet\">";

      disableDeferRendering();
      checkJspWithSource("testCss.jsp", template, expected2);
   }

   @Test
   public void testJs() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
            "<%@ taglib prefix=\"my\" uri=\"http://www.examples.com/mytaglib\" %>" + //
            "<res:bean id=\"res\"/>" + //
            "<res:jsSlot id='head'/>" + //
            "<my:res type='js' value='${res.js.local.ebaytime_js}' target='head'/>" + //
            "<res:useJs value='${res.js.local.bitbybit.ebaytime_js}' target='head'/>" + //
            "<my:res type='js' value='${res.js.shared.ebaytime_js}' target='body'/>" + //
            "<res:jsSlot id='body'/>" + //
            "<my:res type='js' value='${res.js.shared.bitbybit.ebaytime_js}' target='body'/>" + //
            "<res:useJs target='bottom'>//here is one js</res:useJs>" + //
            "<res:useJs target='bottom'>//here is another js</res:useJs>" + //
            "<res:useJs target='unknown'>//here is inline js</res:useJs>" + //
            "<res:jsSlot id='bottom'/>";
      String expected1 = "<script src=\"/test/z/js/aggregated/head?urns=js.local:/ebaytime_js|" + //
            "js.local:/bitbybit/ebaytime_js\" type=\"text/javascript\"></script>" + //
            "<script src=\"/test/z/js/aggregated/body?urns=js.shared:/ebaytime_js|" + //
            "js.shared:/bitbybit/ebaytime_js\" type=\"text/javascript\"></script>" + //
            "<script src=\"/test/f/js/inline/1638248182\" type=\"text/javascript\"></script>" + //
            "<script src=\"/test/z/js/aggregated/bottom?urns=js.inline:/1409716905|js.inline:/32279060\" " + //
            "type=\"text/javascript\"></script>";

      enableDeferRendering();
      checkJspWithSource("testJs.jsp", template, expected1);

      String expected2 = "<script src=\"/test/js/ebaytime.js\" type=\"text/javascript\"></script>" + //
            "<script src=\"/test/js/bitbybit/ebaytime.js\" type=\"text/javascript\"></script>" + //
            "<script src=\"/test/f/js/shared/ebaytime.js\" type=\"text/javascript\"></script>" + //
            "<script src=\"/test/f/js/shared/bitbybit/ebaytime.js\" type=\"text/javascript\">" + //
            "</script><script src=\"/test/f/js/inline/1409716905\" type=\"text/javascript\"></script>" + //
            "<script src=\"/test/f/js/inline/32279060\" type=\"text/javascript\"></script>" + //
            "<script src=\"/test/f/js/inline/1638248182\" type=\"text/javascript\"></script>";

      disableDeferRendering();
      checkJspWithSource("testJs.jsp", template, expected2);
   }
}
