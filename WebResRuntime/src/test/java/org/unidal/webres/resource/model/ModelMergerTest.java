package org.unidal.webres.resource.model;

import java.io.IOException;

import org.junit.Assert;

import org.junit.Test;
import org.xml.sax.SAXException;

import org.unidal.webres.resource.model.entity.Root;

public class ModelMergerTest {
   @Test
   public void testClone() throws IOException, SAXException {
      Root src = Models.forXml().parse(getClass().getResourceAsStream("model_final.xml"), "utf-8");
      Root dst = Models.forObject().clone(src);

      Assert.assertEquals(src.toString(), dst.toString());
   }

   @Test
   public void testMergeOne() throws IOException, SAXException {
      Root src = Models.forXml().parse(getClass().getResourceAsStream("model_final.xml"), "utf-8");

      // only merge once, the result should be same as input
      Root dst1 = new Root();

      Models.forObject().merge(dst1, src);

      Assert.assertEquals(src.toString(), dst1.toString());

      // merge same model twice, result keeps same
      Root dst2 = new Root();

      Models.forObject().merge(dst2, src, src);

      Assert.assertEquals(src.toString(), dst2.toString());
      Assert.assertEquals(dst1.toString(), dst2.toString());
   }

   @Test
   public void testMergeMultiple() throws IOException, SAXException {
      Root src1 = Models.forXml().parse(getClass().getResourceAsStream("model_base.xml"), "utf-8");
      Root src2 = Models.forXml().parse(getClass().getResourceAsStream("model_override.xml"), "utf-8");
      Root expected = Models.forXml().parse(getClass().getResourceAsStream("model_final.xml"), "utf-8");

      Root dst = new Root();

      Models.forObject().merge(dst, src1, src2);

      Assert.assertEquals(expected.toString(), dst.toString());
   }
}
