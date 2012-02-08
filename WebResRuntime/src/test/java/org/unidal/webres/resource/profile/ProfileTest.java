package org.unidal.webres.resource.profile;

import java.io.IOException;

import org.junit.Assert;

import org.junit.Test;
import org.xml.sax.SAXException;

import org.unidal.webres.resource.profile.entity.Js;
import org.unidal.webres.resource.profile.entity.JsSlot;
import org.unidal.webres.resource.profile.entity.Page;
import org.unidal.webres.resource.profile.entity.Profile;

public class ProfileTest {
   @Test
   public void testWithJava() {
      Profile profile = new Profile();

      profile.addCommonJsSlot(new JsSlot("sys") //
            .addJs(new Js("firstJs")) //
            .addJs(new Js("thirdJs")));
      profile.addCommonJsSlot(new JsSlot("search"));
      profile.addPage(new Page("header"));
      profile.addPage(new Page("footer"));
      profile.addPage(new Page("home") //
            .addJsSlot(new JsSlot("HEADER") //
                  .addJs(new Js("trackingJs")) //
                  .addJs(new Js("firstJs"))));

      // validate it, or throw RuntimeException
      Profiles.forObject().validate(profile);
   }

   @Test
   public void testForXml() throws IOException, SAXException {
      Profile src = Profiles.forXml().parse(getClass().getResourceAsStream("profile_final.xml"), "utf-8");

      // validate it, or throw RuntimeException
      Profiles.forObject().validate(src);

      Profile src2 = Profiles.forXml().parse(src.toString());

      // parse, build, parse and build again should work well
      // this proves parsing and building are consistent
      Assert.assertEquals(src.toString(), src2.toString());
   }
}
