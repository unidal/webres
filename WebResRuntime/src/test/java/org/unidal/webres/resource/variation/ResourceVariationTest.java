package org.unidal.webres.resource.variation;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import org.unidal.webres.resource.ResourceContext;
import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.js.JsFactory;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.support.ResourceTestSupport;
import org.unidal.webres.resource.variation.entity.ResourceVariation;
import org.unidal.webres.resource.variation.entity.VariationDefinition;
import org.unidal.webres.resource.variation.transform.ElToUrnTransformer;

public class ResourceVariationTest extends ResourceTestSupport {
   @BeforeClass
   public static void beforeClass() throws Exception {
      ResourceTestSupport.setup(new ResourceVariationTest().copyResourceFrom("/ResourceVariationTest"));
   }

   @Override
   protected String getContextPath() {
      return "/test";
   }

   @Test
   public void testELTransform() throws IOException, SAXException {
      ResourceVariation src = ResourceVariations.forXml().parse(getClass().getResourceAsStream("resource_variation.xml"), "utf-8");
      ResourceVariation expected = ResourceVariations.forXml().parse(
            getClass().getResourceAsStream("resource_variation_final.xml"), "utf-8");

      // validate it, or throw RuntimeException
      ResourceVariations.forObject().validate(src);
      ResourceVariations.forObject().validate(expected);

      src.accept(new ElToUrnTransformer(ResourceRuntimeContext.ctx().getResourceContext()));

      Assert.assertEquals(expected.toString(), src.toString());
   }

   @Test
   public void testForXml() throws IOException, SAXException {
      ResourceVariation src = ResourceVariations.forXml().parse(getClass().getResourceAsStream("resource_variation.xml"), "utf-8");

      // validate it, or throw RuntimeException
      ResourceVariations.forObject().validate(src);

      ResourceVariation src2 = ResourceVariations.forXml().parse(src.toString());

      // parse, build, parse and build again should work well
      // this proves parsing and building are consistent
      Assert.assertEquals(src.toString(), src2.toString());

      VariationDefinition variationDefinition = src.getVariationDefinitions().get("locale");
      Assert.assertEquals("<variation id=\"en_US\" language=\"en\" country=\"US\"/>\r\n", variationDefinition
            .findVariation("en_US").toString());
      Assert.assertEquals("en", variationDefinition.findVariation("en_US").getDynamicAttribute("language"));
      Assert.assertEquals("DE", variationDefinition.findVariation("de_DE").getDynamicAttribute("country"));
   }

   @Test
   public void testVariation() throws Exception {
      IJsRef jsRef = JsFactory.forRef().createLocalRef("/jquery.js");
      ResourceContext ctx = new ResourceContext(getRegistry());

      Assert.assertEquals("/test/js/jquery.js", jsRef.resolve(ctx).getUrl());

      ctx.setVariation("pooltype", "production");
      Assert.assertEquals("/test/js/jquery_min.js", jsRef.resolve(ctx).getUrl());
   }
}
