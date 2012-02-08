package org.unidal.webres.tag.css;

import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.css.CssFactory;
import org.unidal.webres.resource.expression.CssExpression;
import org.unidal.webres.resource.expression.ResourceExpressionParser;
import org.unidal.webres.tag.support.ResourceTagTestSupport;

public class UseCssTagTest extends ResourceTagTestSupport {
   @BeforeClass
   public static void beforeClass() throws Exception {
      setup(new UseCssTagTest().copyResourceFrom("/UseCssTagTest"));
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
   public void testAttributesOverrideExternalized() {
      UseCssTag tag = new UseCssTag();
      UseCssTagModel model = tag.getModel();

      model.setId("mycss");
      model.setValue(CssFactory.forRef().createLocalRef("/ebaytime.css"));
      model.setDynamicAttribute("href", "..."); // this will be ignored
      model.setDynamicAttribute("alt", "My Css");

      checkTag(tag, "<link href=\"/test/css/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\" alt=\"My Css\">");
   }

   @Test
   public void testAttributesOverrideInline() {
      UseCssTag tag = new UseCssTag();
      UseCssTagModel model = tag.getModel();

      model.setId("mycss");
      model.setBodyContent(".here {is: css;}");
      model.setDynamicAttribute("href", "..."); // this will be ignored
      model.setDynamicAttribute("alt", "My Css");

      checkTag(tag, "<style type=\"text/css\" alt=\"My Css\">.here {is: css;}</style>");
   }

   @Test
   public void testExpression() {
      UseCssTag tag = new UseCssTag();
      CssExpression eBayLogo = new ResourceExpressionParser(getExpressionEnv()).parse("${res.css.local.ebaytime_css}");
      UseCssTagModel model = tag.getModel();

      model.setValue(eBayLogo);

      checkTag(tag, "<link href=\"/test/css/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">");
   }

   @Test
   public void testHtmlId() {
      UseCssTag tag = new UseCssTag();
      UseCssTagModel model = tag.getModel();

      model.setId("mycss");
      model.setBodyContent(".here {is: css;}");
      model.setDynamicAttribute("href", "..."); // this will be ignored
      model.setDynamicAttribute("alt", "My Css");
      model.setDynamicAttribute("id", "myid");

      checkTag(tag, "<style type=\"text/css\" alt=\"My Css\" id=\"myid\">.here {is: css;}</style>");
   }

   @Test
   public void testInline() {
      UseCssTag tag = new UseCssTag();
      UseCssTagModel model = tag.getModel();

      model.setBodyContent(".here {is: css;}");

      checkTag(tag, "<style type=\"text/css\">.here {is: css;}</style>");
   }

   @Test
   public void testLocalCssSecureUrl() {
      UseCssTag tag = new UseCssTag();
      UseCssTagModel model = tag.getModel();

      model.setValue(CssFactory.forRef().createLocalRef("/ebaytime.css"));
      model.setSecure(true);

      checkTag(tag, "<link href=\"/test/css/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">");
   }

   @Test
   public void testLocalCssUrl() {
      UseCssTag tag = new UseCssTag();
      UseCssTagModel model = tag.getModel();

      model.setValue(CssFactory.forRef().createLocalRef("/ebaytime.css"));

      checkTag(tag, "<link href=\"/test/css/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">");
   }

   @Test
   public void testSharedCssSecureUrl() {
      UseCssTag tag = new UseCssTag();
      UseCssTagModel model = tag.getModel();

      model.setValue(CssFactory.forRef().createSharedRef("/ebaytime.css"));
      model.setSecure(true);

      checkTag(tag, "<link href=\"https://res.ebay.com/css/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">");
   }

   @Test
   public void testSharedCssUrl() {
      UseCssTag tag = new UseCssTag();
      UseCssTagModel model = tag.getModel();

      model.setValue(CssFactory.forRef().createSharedRef("/ebaytime.css"));

      checkTag(tag, "<link href=\"http://res.ebay.com/css/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">");
   }
}
