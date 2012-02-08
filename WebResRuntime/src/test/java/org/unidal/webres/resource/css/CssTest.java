package org.unidal.webres.resource.css;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.ResourceContext;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.resource.support.ResourceTestSupport;

public class CssTest extends ResourceTestSupport {
   @BeforeClass
   public static void beforeClass() throws Exception {
      ResourceTestSupport.setup(new CssTest().copyResourceFrom("/CssTest"));
   }

   @Override
   protected void configure() throws Exception {
      super.configure();

      getRegistry().register(String.class, ResourceConstant.Css.SharedUrlPrefix, "http://res.ebay.com/css");
      getRegistry().register(String.class, ResourceConstant.Css.SharedSecureUrlPrefix, "https://res.ebay.com/css");
   }

   @Override
   protected String getContextPath() {
      return "/test";
   }

   @Test
   public void testLocalCss() throws Exception {
      ICssRef cssRef = CssFactory.forRef().createLocalRef("/ebaytime.css");
      ICss css = cssRef.resolve(new ResourceContext(getRegistry()));

      Assert.assertEquals("/test/css/ebaytime.css", css.getUrl());
      Assert.assertEquals("/test/css/ebaytime.css", css.getSecureUrl());
   }

   @Test
   public void testSharedCss() throws Exception {
      ICssRef cssRef = CssFactory.forRef().createSharedRef("/ebaytime.css");
      ICss css = cssRef.resolve(new ResourceContext(getRegistry()));

      Assert.assertEquals("http://res.ebay.com/css/ebaytime.css", css.getUrl());
      Assert.assertEquals("https://res.ebay.com/css/ebaytime.css", css.getSecureUrl());
   }
}
