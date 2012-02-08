package org.unidal.webres.taglib;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.img.ImageFactory;
import org.unidal.webres.taglib.support.JettyTestSupport;
import org.unidal.webres.taglib.support.ResourceTemplateTestSupport;

public class ImageTagHandlerTest extends ResourceTemplateTestSupport {
   @AfterClass
   public static void afterClass() throws Exception {
      JettyTestSupport.shutdownServer();
   }

   @BeforeClass
   public static void beforeClass() throws Exception {
      JettyTestSupport.startServer(new ImageTagHandlerTest().copyResourceFrom("/ImageTagHandlerTest"));
   }

   @Override
   protected void configure() throws Exception {
      super.configure();

      getRegistry().register(String.class, ResourceConstant.Image.SharedUrlPrefix, "http://res.ebay.com/img");
      getRegistry().register(String.class, ResourceConstant.Image.SharedSecureUrlPrefix, "https://res.ebay.com/img");
   }

   @Override
   protected String getContextPath() {
      return "/test";
   }

   @Test
   public void testImageWithEL() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.ebay.com/webres\" %>" + //
            "<res:bean id=\"res\"/>" + //
            "<res:img value='${res.img.local.eBayLogo_gif}'/>" + //
            "<res:img value='${res.img.local.eBayLogo_gif}' secure='true'/>" + //
            "<res:img value='${res.img.shared.eBayLogo_gif}'/>" + //
            "<res:img value='${res.img.shared.eBayLogo_gif}' secure='true'/>" + //
            "<res:img value='${res.img.pics.half.btnSearch_gif}'/>" + //
            "<res:img value='${res.img.pics.half.btnSearch_gif}' secure='true'/>";
      String expected = "<img src=\"/test/img/eBayLogo.gif\" width=\"110\" height=\"45\">" + //
            "<img src=\"/test/img/eBayLogo.gif\" width=\"110\" height=\"45\">" + //
            "<img src=\"http://res.ebay.com/img/eBayLogo.gif\" width=\"110\" height=\"45\">" + //
            "<img src=\"https://res.ebay.com/img/eBayLogo.gif\" width=\"110\" height=\"45\">" + //
            "<img src=\"http://pics.ebaystatic.com/aw/pics/half/btnSearch.gif\" width=\"72\" height=\"37\">" + //
            "<img src=\"https://pics.ebaystatic.com/aw/pics/half/btnSearch.gif\" width=\"72\" height=\"37\">";

      checkJspWithSource("imageWithEL.jsp", template, expected);
   }

   @Test
   public void testImageWithRef() throws IOException {
      String template = "<%@ taglib prefix=\"res\" uri=\"http://www.ebay.com/webres\" %>" + //
            "<res:img value='${ref}'/>";
      String expected = "<img src=\"/test/img/half/eBayLogo.gif\" width=\"110\" height=\"45\">";

      setAttribute("ref", ImageFactory.forRef().createLocalRef("/half/eBayLogo.gif"));
      checkJspWithSource("imageWithRef.jsp", template, expected);
   }
}
