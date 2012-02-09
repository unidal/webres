package org.unidal.webres.server.template;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.servlet.Context;

import org.unidal.webres.resource.api.ITemplate;
import org.unidal.webres.resource.api.ITemplateContext;
import org.unidal.webres.resource.api.ITemplateRef;
import org.unidal.webres.resource.runtime.ResourcePermutation;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.template.TemplateFactory;
import org.unidal.webres.server.SimpleResourceConfigurator;
import org.unidal.webres.server.SimpleResourceFilter;
import org.unidal.webres.server.SimpleResourceServlet;
import org.unidal.webres.taglib.support.JettyTestSupport;
import org.unidal.webres.taglib.support.ResourceTemplateTestSupport;

public class JspTemplateTest extends ResourceTemplateTestSupport {
   @BeforeClass
   public static void beforeClass() throws Exception {
      ResourceTemplateTestSupport.startServer(new JspTemplateTest().copyResourceFrom("/JspTemplateTest"));
   }

   @AfterClass
   public static void afterClass() throws Exception {
      JettyTestSupport.shutdownServer();
   }

   protected void checkTemplate(ITemplateRef ref, String expected, Object... pairs) throws Exception {
      ITemplate template = ref.resolve(ResourceRuntimeContext.ctx().getResourceContext());
      ITemplateContext ctx = new MockTemplateContext(pairs);

      Assert.assertEquals(expected, template.evaluate(ctx));
   }

   protected void checkTemplate(String source, String expected, Object... pairs) throws Exception {
      ITemplateRef templateRef = TemplateFactory.forRef().createInlineRef(source);

      checkTemplate(templateRef, expected, pairs);
   }

   @Override
   protected void configure() throws Exception {
      super.configure();

      getContext().addServlet(SimpleResourceServlet.class, "/f/*");
      getContext().addServlet(SimpleResourceServlet.class, "/z/*");
      getContext().addFilter(SimpleResourceFilter.class, "*.jsp", Handler.ALL);
   }

   @Override
   protected void postConfigure(Context ctx) {
      new SimpleResourceConfigurator().configure(getRegistry());

      super.postConfigure(ctx);
   }

   @Override
   protected String getContextPath() {
      return "/test";
   }

   @Test
   public void testWithoutResource() throws Exception {
      String template = "<%@ taglib prefix='res' uri='http://www.ebay.com/webres' %>" + //
            "<res:bean id='res'/>Hello, ${name}! Nice to meet ${you}!";
      String expected = "Hello, world! Nice to meet robert!";
      ITemplateRef ref = TemplateFactory.forRef().createInlineRef("/testWithoutResource.jsp", template);

      checkTemplate(ref, expected, "name", "world", "you", "robert");
   }

   @Override
   protected boolean isKeepGenerated() {
      return true;
   }

   @Test
   public void testWithResource() throws Exception {
      String template = "<%@ taglib prefix='res' uri='http://www.ebay.com/webres' %>" + //
            "<res:bean id='res'/>Hello, <res:img value='${res.img.shared.simple_svg}'/>!" + //
            "${res.img.local.eBayLogo_gif} and ${res.img.shared.eBayLogo_gif}!";
      String expected = "Hello, <img src=\"/test/f/img/shared/simple.svg\" width=\"467\" height=\"462\">!" + //
            "/test/img/eBayLogo.gif and /test/f/img/shared/eBayLogo.gif!";
      ITemplateRef ref = TemplateFactory.forRef().createInlineRef("/testWithResource1.jsp", template);

      checkTemplate(ref, expected);
   }

   @Test
   public void testWithResourceSecure() throws Exception {
      IResourceContext ctx = ResourceRuntimeContext.ctx().getResourceContext();

      ctx.setSecure(true);

      String template = "Hello, ${res.img.local.eBayLogo_gif} and ${res.img.shared.eBayLogo_gif}!";
      String expected = "Hello, /test/img/eBayLogo.gif and /test/f/img/shared/eBayLogo.gif!";

      checkTemplate(template, expected);
   }

   @Test
   public void testWithResourceSecurePermutation() throws Exception {
      IResourceContext ctx = ResourceRuntimeContext.ctx().getResourceContext();

      ctx.setSecure(true);
      ctx.setPermutation(ResourcePermutation.create(Locale.CHINA));

      String template = "Hello, ${res.img.local.eBayLogo_gif} and ${res.img.shared.eBayLogo_gif}!";
      String expected = "Hello, /test/img/zh_CN/eBayLogo.gif and /test/f/img/shared/zh_CN/eBayLogo.gif!";

      checkTemplate(template, expected);
   }

   static class MockTemplateContext implements ITemplateContext {
      private Map<String, Object> m_map = new HashMap<String, Object>();

      public MockTemplateContext(Object[] pairs) {
         int len = pairs.length;

         if (len % 2 != 0) {
            throw new IllegalArgumentException(String.format("Parameters(%s) should be paired!", Arrays.asList(pairs)));
         }

         for (int i = 0; i < len; i += 2) {
            String name = (String) pairs[i];
            Object value = pairs[i + 1];

            m_map.put(name, value);
         }
      }

      @Override
      public Object getAttribute(String name) {
         return m_map.get(name);
      }

      @Override
      public Map<String, Object> getAttributes() {
         return m_map;
      }

      @Override
      public void setAttribute(String name, Object value) {
         m_map.put(name, value);
      }
   }
}
