package org.unidal.webres.resource.js;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.ResourceContext;
import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.support.ResourceTestSupport;

public class JsTest extends ResourceTestSupport {
   @BeforeClass
   public static void beforeClass() throws Exception {
      ResourceTestSupport.setup(new JsTest().copyResourceFrom("/JsTest"));
   }

   @Override
   protected void configure() throws Exception {
      super.configure();

      getRegistry().register(String.class, ResourceConstant.Js.SharedUrlPrefix, "http://res.unidal.org/js");
      getRegistry().register(String.class, ResourceConstant.Js.SharedSecureUrlPrefix, "https://res.unidal.org/js");

      getRegistry().register(Boolean.class, ResourceConstant.Js.AggregatedVerbose, true);
   }

   @Override
   protected String getContextPath() {
      return "/test";
   }

   @Test
   public void testLocalJs() throws Exception {
      IJsRef jsRef = JsFactory.forRef().createLocalRef("/ebaytime.js");
      IJs js = jsRef.resolve(new ResourceContext(getRegistry()));

      Assert.assertEquals("/test/js/ebaytime.js", js.getUrl());
      Assert.assertEquals("/test/js/ebaytime.js", js.getSecureUrl());
      Assert.assertEquals(207, js.getContent().length());
   }

   @Test
   public void testAggregatedJs() throws Exception {
      IJsRef r1 = JsFactory.forRef().createLocalRef("/ebaytime.js");
      IJsRef r2 = JsFactory.forRef().createLocalRef("/bitbybit/ebaytime.js");
      IJsRef r3 = JsFactory.forRef().createSharedRef("/ebaytime.js");
      IJsRef r4 = JsFactory.forRef().createSharedRef("/bitbybit/ebaytime.js");
      IJsRef r5 = JsFactory.forRef().createInlineRef("//inline js1");
      IJsRef r6 = JsFactory.forRef().createInlineRef("//inline js2");

      IJsRef jsRef = JsFactory.forRef().createAggregatedRef("/slot1", r1, r2, r3, r4, r5, r6);
      IJs js = jsRef.resolve(new ResourceContext(getRegistry()));

      String content = js.getContent();

      Assert.assertTrue(content.contains(r1.getUrn().toString()));
      Assert.assertEquals(js.getMeta().getLength(), content.length());

      Assert.assertNull(js.getUrl());
      Assert.assertNull(js.getSecureUrl());
   }

   @Test
   public void testSharedJs() throws Exception {
      IJsRef jsRef = JsFactory.forRef().createSharedRef("/ebaytime.js");
      IJs js = jsRef.resolve(new ResourceContext(getRegistry()));

      Assert.assertEquals("http://res.unidal.org/js/ebaytime.js", js.getUrl());
      Assert.assertEquals("https://res.unidal.org/js/ebaytime.js", js.getSecureUrl());
      Assert.assertEquals(js.getMeta().getLength(), js.getContent().length());
   }
}
