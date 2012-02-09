package org.unidal.webres.server;

import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mortbay.jetty.servlet.Context;

import org.unidal.webres.taglib.support.JettyTestSupport;
import org.unidal.webres.taglib.support.ResourceTemplateTestSupport;

public class SimpleResourceServletTest extends ResourceTemplateTestSupport {
   @AfterClass
   public static void afterClass() throws Exception {
      JettyTestSupport.shutdownServer();
   }

   @BeforeClass
   public static void beforeClass() throws Exception {
      JettyTestSupport.startServer(new SimpleResourceServletTest().copyResourceFrom("SimpleResourceServletTest"));
   }

   @Override
   protected void configure() throws Exception {
      super.configure();

      getContext().addServlet(SimpleResourceServlet.class, "/z/*");
      getContext().addServlet(SimpleResourceServlet.class, "/f/*");
      getContext().addServlet(SimpleResourceServlet.class, "/res/*");
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
   public void testAjaxToken() throws IOException {
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
            "jsToken=, cssToken="; // ajax token is not supported without defer rendering

      checkJspWithSource("testAjaxToken.jsp", template, expected);
   }

   @Test
   public void testAttributesOverride() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
            "<res:bean id=\"res\"/>" + //
            "<res:img value='${res.img.shared.eBayLogo_gif}' _id=\"myimg\"/>" + //
            "<res:useJs _id=\"myjs1\">//here is js</res:useJs>" + //
            "<res:useCss _id=\"mycss1\">.here{is:css;}</res:useCss>" + //
            "<res:useJs id='myjs'>//here is my js</res:useJs>" + //
            "<res:useCss id='mycss'>.here{is:my css;}</res:useCss>" + //
            "<res:useJs value='${res.js.shared.ebaytime_js}' _value=\"js value\"/>" + //
            "<res:useCss value='${res.css.shared.ebaytime_css}' _value=\"css value\"/>";
      String expected2 = "<img src=\"/test/f/img/shared/eBayLogo.gif\" width=\"110\" height=\"45\" id=\"myimg\">"
            + "<script src=\"/test/f/js/inline/635253329\" type=\"text/javascript\" id=\"myjs1\"></script>"
            + "<link href=\"/test/f/css/inline/927456434\" type=\"text/css\" rel=\"stylesheet\" id=\"mycss1\">"
            + "<script src=\"/test/f/js/inline/myjs\" type=\"text/javascript\"></script>"
            + "<link href=\"/test/f/css/inline/mycss\" type=\"text/css\" rel=\"stylesheet\">"
            + "<script src=\"/test/f/js/shared/ebaytime.js\" type=\"text/javascript\" value=\"js value\"></script>"
            + "<link href=\"/test/f/css/shared/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\" value=\"css value\">";

      checkJspWithSource("testAttributesOverride.jsp", template, expected2);
   }

   @Test
   public void testCss() throws IOException {
      checkPath("/test/z/css?urns=css.shared:/ebaytime.css|css.local:/ebaytime.css", null,
            ".footer {color:#00c; font-size:12px}\r\n.header {color:#00c; font-size:12px}\r\n",
            map("Content-Type", "text/css; charset=utf-8"));
   }

   @Test
   public void testJs() throws IOException {
      checkPath("/test/z/js?urns=js.shared:/ebaytime.js|js.local:/ebaytime.js", null,
            "// shared js here\r\n// local js here\r\n", map("Content-Type", "application/x-javascript; charset=utf-8"));
   }

   @Test
   public void testLink() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
            "<res:bean id=\"res\"/>" + //
            "<res:link value=\"${res.link.cmd.signin['&user'][user]['&pwd'][pwd]['%rtm']}\">FAQ</res:link>";
      String expected = "<a href=\"/test/signin?user=guest&pwd=guest#rtm\">FAQ</a>";

      setAttribute("user", "guest");
      setAttribute("pwd", "guest");
      checkJspWithSource("linkWithEL.jsp", template, expected);
   }

   @Test
   @Ignore
   public void testJson() throws IOException {
      checkPath("/test/z/json?urns=js.shared:/ebaytime.js|css.local:/ebaytime.css", null, "// ebaytime js here",
            map("Content-Type", "application/x-javascript; charset=utf-8"));
   }

   @Test
   public void testJspPage() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
            "<res:bean id=\"res\"/>" + //
            "<res:img value='${res.img.shared.eBayLogo_gif}'/>" + //
            "<res:useJs value='${res.js.shared.ebaytime_js}'/>" + //
            "<res:useCss value='${res.css.shared.ebaytime_css}'/>";
      String expected = "<img src=\"/test/f/img/shared/eBayLogo.gif\" width=\"110\" height=\"45\">" + //
            "<script src=\"/test/f/js/shared/ebaytime.js\" type=\"text/javascript\"></script>" + //
            "<link href=\"/test/f/css/shared/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">";

      checkJspWithSource("testJspPage.jsp", template, expected);
   }

   @Test
   public void testPage404() throws IOException {
      try {
         checkPath("/test/res/unknown", null);

         Assert.fail("FileNotFoundException should be thrown!");
      } catch (FileNotFoundException e) {
         // expected
      }
   }

   @Test
   public void testSharedResourceService() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
            "<res:bean id=\"res\"/>" + //
            "<res:img value='${res.img.shared.eBayLogo_gif}'/>" + //
            "<res:useJs id='myjs'>//here is my js</res:useJs>" + //
            "<res:useCss id='mycss'>.here{is:my css;}</res:useCss>" + //
            "<res:useJs value='${res.js.shared.ebaytime_js}'/>" + //
            "<res:useCss value='${res.css.shared.ebaytime_css}'/>";

      checkJspWithSource("testSharedResourceService.jsp", template, null);

      checkPath("/test/f/js/shared/ebaytime.js", null, "// shared js here",
            map("Content-Type", "application/x-javascript; charset=utf-8"));
      checkPath("/test/f/css/shared/ebaytime.css", null, ".footer {color:#00c; font-size:12px}",
            map("Content-Type", "text/css; charset=utf-8"));
      checkPath("/test/f/img/shared/eBayLogo.gif", null, null, map("Content-Type", "image/gif"));
      checkPath("/test/f/js/inline/myjs", null, "//here is my js",
            map("Content-Type", "application/x-javascript; charset=utf-8"));
      checkPath("/test/f/css/inline/mycss", null, ".here{is:my css;}", map("Content-Type", "text/css; charset=utf-8"));
   }

   @Test
   public void testUrlBuilders() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
            "<res:bean id=\"res\"/>" + //
            "<res:img value='${res.img.shared.eBayLogo_gif}'/>" + //
            "<res:useJs>//here is js</res:useJs>" + //
            "<res:useCss>.here{is:css;}</res:useCss>" + //
            "<res:useJs id='myjs'>//here is my js</res:useJs>" + //
            "<res:useCss id='mycss'>.here{is:my css;}</res:useCss>" + //
            "<res:useJs value='${res.js.shared.ebaytime_js}'/>" + //
            "<res:useCss value='${res.css.shared.ebaytime_css}'/>";
      String expected = "<img src=\"/test/f/img/shared/eBayLogo.gif\" width=\"110\" height=\"45\">" + //
            "<script src=\"/test/f/js/inline/635253329\" type=\"text/javascript\"></script>" + //
            "<link href=\"/test/f/css/inline/927456434\" type=\"text/css\" rel=\"stylesheet\">" + //
            "<script src=\"/test/f/js/inline/myjs\" type=\"text/javascript\"></script>" + //
            "<link href=\"/test/f/css/inline/mycss\" type=\"text/css\" rel=\"stylesheet\">" + //
            "<script src=\"/test/f/js/shared/ebaytime.js\" type=\"text/javascript\"></script>" + //
            "<link href=\"/test/f/css/shared/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">";

      checkJspWithSource("testUrlBuilders.jsp", template, expected);
   }

   @Test
   public void testWelcomePage() throws IOException {
      String expectedContent = String.format("Welcome to Resource Servlet! <br><br>War Root: %s<br>Context Path: %s",
            getWarRoot().getCanonicalPath(), getContextPath());

      checkPath("/test/res", expectedContent);
   }
}
