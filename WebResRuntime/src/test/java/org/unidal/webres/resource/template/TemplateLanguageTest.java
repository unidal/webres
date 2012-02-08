package org.unidal.webres.resource.template;

import junit.framework.Assert;

import org.junit.Test;

public class TemplateLanguageTest {
   @Test
   public void testByName() {
      Assert.assertEquals(TemplateLanguage.Simple, TemplateLanguage.getByName("Simple", null));
      Assert.assertEquals(TemplateLanguage.Jsp, TemplateLanguage.getByName("Jsp", null));

      Assert.assertNull(TemplateLanguage.getByName("Unknown", null));
   }

   @Test
   public void testByExtension() {
      Assert.assertEquals(TemplateLanguage.Jsp, TemplateLanguage.getByExtension(".jsp", null));
      Assert.assertEquals(TemplateLanguage.Jsp, TemplateLanguage.getByExtension(".jspf", null));

      Assert.assertNull(TemplateLanguage.getByExtension(".unknown", null));
      Assert.assertNull(TemplateLanguage.getByExtension("unknown", null));
   }

   @Test
   public void testByContent() {
      Assert.assertEquals(TemplateLanguage.Jsp,
            TemplateLanguage.getByContent("<%@ taglib prefix='res' uri='http://www.ebay.com/webres'%>", null));

      Assert.assertNull(TemplateLanguage.getByExtension("anything else", null));
   }
}
