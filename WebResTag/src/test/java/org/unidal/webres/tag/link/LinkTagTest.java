package org.unidal.webres.tag.link;

import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.expression.LinkExpression;
import org.unidal.webres.resource.expression.ResourceExpressionParser;
import org.unidal.webres.resource.link.LinkFactory;
import org.unidal.webres.tag.support.ResourceTagTestSupport;

public class LinkTagTest extends ResourceTagTestSupport {
   @BeforeClass
   public static void beforeClass() throws Exception {
      setup(new LinkTagTest());
   }

   @Override
   protected String getContextPath() {
      return "/test";
   }

   @Test
   public void testAttributesOverride() {
      LinkTag tag = new LinkTag();
      LinkTagModel model = tag.getModel();

      model.setValue(LinkFactory.forRef().createPagesRef("/half/faq.html"));
      model.setBodyContent("here");
      model.setDynamicAttribute("href", "..."); // this will be ignored
      model.setDynamicAttribute("id", "mylink");
      model.setDynamicAttribute("alt", "My Link");

      checkTag(tag, "<a href=\"http://pages.unidal.org/half/faq.html\" id=\"mylink\" alt=\"My Link\">here</a>");
   }

   @Test
   public void testExpression() {
      LinkTag tag = new LinkTag();
      LinkExpression faq = new ResourceExpressionParser(getExpressionEnv()).parse("${res.link.pages.half.faq_html}");
      LinkTagModel model = tag.getModel();

      model.setValue(faq);
      model.setBodyContent("here");

      checkTag(tag, "<a href=\"http://pages.unidal.org/half/faq.html\">here</a>");
   }

   @Test
   public void testPagesLinkSecureUrl() {
      LinkTag tag = new LinkTag();
      LinkTagModel model = tag.getModel();

      model.setValue(LinkFactory.forRef().createPagesRef("/half/faq.html"));
      model.setSecure(true);
      model.setBodyContent("here");

      checkTag(tag, "<a href=\"https://pages.unidal.org/half/faq.html\">here</a>");
   }

   @Test
   public void testPagesLinkUrl() {
      LinkTag tag = new LinkTag();
      LinkTagModel model = tag.getModel();

      model.setValue(LinkFactory.forRef().createPagesRef("/half/faq.html"));
      model.setBodyContent("here");

      checkTag(tag, "<a href=\"http://pages.unidal.org/half/faq.html\">here</a>");
   }
}
