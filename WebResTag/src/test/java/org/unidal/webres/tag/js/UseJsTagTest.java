package org.unidal.webres.tag.js;

import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.expression.JsExpression;
import org.unidal.webres.resource.expression.ResourceExpressionParser;
import org.unidal.webres.resource.js.JsFactory;
import org.unidal.webres.tag.support.ResourceTagTestSupport;

public class UseJsTagTest extends ResourceTagTestSupport {
   @BeforeClass
   public static void beforeClass() throws Exception {
      setup(new UseJsTagTest().copyResourceFrom("/UseJsTagTest"));
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
   public void testAttributesOverrideExternalized() {
      UseJsTag tag = new UseJsTag();
      UseJsTagModel model = tag.getModel();

      model.setId("myjs");
      model.setValue(JsFactory.forRef().createLocalRef("/ebaytime.js"));
      model.setDynamicAttribute("src", "..."); // this will be ignored
      model.setDynamicAttribute("alt", "My Js");

      checkTag(tag, "<script src=\"/test/js/ebaytime.js\" type=\"text/javascript\" alt=\"My Js\"></script>");
   }

   @Test
   public void testAttributesOverrideInline() {
      UseJsTag tag = new UseJsTag();
      UseJsTagModel model = tag.getModel();

      model.setId("myjs");
      model.setBodyContent(".here {is: css;}");
      model.setDynamicAttribute("src", "..."); // this will be ignored
      model.setDynamicAttribute("alt", "My Js");

      checkTag(tag, "<script type=\"text/javascript\" alt=\"My Js\">.here {is: css;}</script>");
   }

   @Test
   public void testExpression() {
      UseJsTag tag = new UseJsTag();
      JsExpression eBayLogo = new ResourceExpressionParser(getExpressionEnv()).parse("${res.js.local.ebaytime_js}");
      UseJsTagModel model = tag.getModel();

      model.setValue(eBayLogo);

      checkTag(tag, "<script src=\"/test/js/ebaytime.js\" type=\"text/javascript\"></script>");
   }

   @Test
   public void testHtmlId() {
      UseJsTag tag = new UseJsTag();
      UseJsTagModel model = tag.getModel();

      model.setId("myjs");
      model.setBodyContent(".here {is: css;}");
      model.setDynamicAttribute("src", "..."); // this will be ignored
      model.setDynamicAttribute("alt", "My Js");
      model.setDynamicAttribute("id", "myid");

      checkTag(tag, "<script type=\"text/javascript\" alt=\"My Js\" id=\"myid\">.here {is: css;}</script>");
   }

   @Test
   public void testInline() {
      UseJsTag tag = new UseJsTag();
      UseJsTagModel model = tag.getModel();

      model.setBodyContent(".here {is: css;}");

      checkTag(tag, "<script type=\"text/javascript\">.here {is: css;}</script>");
   }

   @Test
   public void testLocalJsSecureUrl() {
      UseJsTag tag = new UseJsTag();
      UseJsTagModel model = tag.getModel();

      model.setValue(JsFactory.forRef().createLocalRef("/ebaytime.js"));
      model.setSecure(true);

      checkTag(tag, "<script src=\"/test/js/ebaytime.js\" type=\"text/javascript\"></script>");
   }

   @Test
   public void testLocalJsUrl() {
      UseJsTag tag = new UseJsTag();
      UseJsTagModel model = tag.getModel();

      model.setValue(JsFactory.forRef().createLocalRef("/ebaytime.js"));

      checkTag(tag, "<script src=\"/test/js/ebaytime.js\" type=\"text/javascript\"></script>");
   }

   @Test
   public void testSharedJsSecureUrl() {
      UseJsTag tag = new UseJsTag();
      UseJsTagModel model = tag.getModel();

      model.setValue(JsFactory.forRef().createSharedRef("/ebaytime.js"));
      model.setSecure(true);

      checkTag(tag, "<script src=\"https://res.unidal.org/js/ebaytime.js\" type=\"text/javascript\"></script>");
   }

   @Test
   public void testSharedJsUrl() {
      UseJsTag tag = new UseJsTag();
      UseJsTagModel model = tag.getModel();

      model.setValue(JsFactory.forRef().createSharedRef("/ebaytime.js"));

      checkTag(tag, "<script src=\"http://res.unidal.org/js/ebaytime.js\" type=\"text/javascript\"></script>");
   }
}
