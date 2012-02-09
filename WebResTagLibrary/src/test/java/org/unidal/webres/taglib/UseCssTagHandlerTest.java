package org.unidal.webres.taglib;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.css.CssFactory;
import org.unidal.webres.taglib.support.JettyTestSupport;
import org.unidal.webres.taglib.support.ResourceTemplateTestSupport;

public class UseCssTagHandlerTest extends ResourceTemplateTestSupport {
   @AfterClass
   public static void afterClass() throws Exception {
      JettyTestSupport.shutdownServer();
   }

   @BeforeClass
   public static void beforeClass() throws Exception {
      JettyTestSupport.startServer(new UseCssTagHandlerTest().copyResourceFrom("/UseCssTagHandlerTest"));
   }

   @Override
   protected void configure() throws Exception {
      super.configure();

      getRegistry().register(String.class, ResourceConstant.Css.SharedUrlPrefix, "http://res.unidal.org/css");
      getRegistry().register(String.class, ResourceConstant.Css.SharedSecureUrlPrefix, "https://res.unidal.org/css");
   }

   @Override
   protected String getContextPath() {
      return "/test";
   }

   @Test
   public void testUseCssWithEL() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
            "<res:bean id=\"res\"/>" + //
            "<res:useCss value='${res.css.local.ebaytime_css}'/>" + //
            "<res:useCss value='${res.css.local.ebaytime_css}' secure='true'/>" + //
            "<res:useCss value='${res.css.shared.ebaytime_css}'/>" + //
            "<res:useCss value='${res.css.shared.ebaytime_css}' secure='true'/>" + //
            "<res:useCss>.here {one: css;}</res:useCss>" + //
            "<res:useCss>.here {another: css;}</res:useCss>";
      String expected = "<link href=\"/test/css/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">" + //
            "<link href=\"/test/css/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">" + //
            "<link href=\"http://res.unidal.org/css/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">" + //
            "<link href=\"https://res.unidal.org/css/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">" + //
            "<style type=\"text/css\">.here {one: css;}</style>" + //
            "<style type=\"text/css\">.here {another: css;}</style>";

      checkJspWithSource("cssWithEL.jsp", template, expected);
   }

   @Test
   public void testUseCssWithRef() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.unidal.org/webres\" %>" + //
            "<res:useCss value='${ref}'/>";
      String expected = "<link href=\"/test/css/bitbybit/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">";

      setAttribute("ref", CssFactory.forRef().createLocalRef("/bitbybit/ebaytime.css"));
      checkJspWithSource("cssWithRef.jsp", template, expected);
   }
}
