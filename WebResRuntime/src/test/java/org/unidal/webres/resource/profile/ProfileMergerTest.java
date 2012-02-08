package org.unidal.webres.resource.profile;

import java.io.IOException;

import org.junit.Assert;

import org.junit.Test;
import org.xml.sax.SAXException;

import org.unidal.webres.resource.profile.entity.Profile;

public class ProfileMergerTest {
   @Test
   public void testClone() throws IOException, SAXException {
      Profile src = Profiles.forXml().parse(getClass().getResourceAsStream("profile_final.xml"), "utf-8");

      // only merge once, the result should be same as input
      Profile dst = Profiles.forObject().clone(src);

      Assert.assertEquals(src.toString(), dst.toString());
   }

   @Test
   public void testMergeOne() throws IOException, SAXException {
      Profile src = Profiles.forXml().parse(getClass().getResourceAsStream("profile_final.xml"), "utf-8");

      // only merge once, the result should be same as input
      Profile dst1 = new Profile();

      Profiles.forObject().merge(dst1, src);

      Assert.assertEquals(src.toString(), dst1.toString());

      // merge same profile twice, result keeps same
      Profile dst2 = new Profile();

      Profiles.forObject().merge(dst2, src, src);

      Assert.assertEquals(src.toString(), dst2.toString());
      Assert.assertEquals(dst1.toString(), dst2.toString());
   }

   @Test
   public void testMergeMultiple() throws IOException, SAXException {
      Profile src1 = Profiles.forXml().parse(getClass().getResourceAsStream("profile_base.xml"), "utf-8");
      Profile src2 = Profiles.forXml().parse(getClass().getResourceAsStream("profile_override.xml"), "utf-8");
      Profile expected = Profiles.forXml().parse(getClass().getResourceAsStream("profile_final.xml"), "utf-8");

      Profile dst = new Profile();

      Profiles.forObject().merge(dst, src1, src2);

      Assert.assertEquals(expected.toString(), dst.toString());
   }
}
