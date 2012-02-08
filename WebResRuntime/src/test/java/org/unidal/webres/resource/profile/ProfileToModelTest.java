package org.unidal.webres.resource.profile;

import java.io.IOException;

import org.junit.Assert;

import org.junit.Test;
import org.xml.sax.SAXException;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.model.Models;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.profile.entity.Profile;

public class ProfileToModelTest {
   @Test
   public void testForJs() throws IOException, SAXException {
      Profile profile = Profiles.forXml().parse(getClass().getResourceAsStream("profile.xml"), "utf-8");
      Root expectedModel = Models.forXml().parse(getClass().getResourceAsStream("model_js.xml"), "utf-8");

      // validate it, or throw RuntimeException
      Profiles.forObject().validate(profile);

      Root model = Profiles.forObject().buildModel(profile, SystemResourceType.Js);

      Assert.assertEquals(expectedModel.toString(), model.toString());
   }

   @Test
   public void testForCss() throws IOException, SAXException {
      Profile profile = Profiles.forXml().parse(getClass().getResourceAsStream("profile.xml"), "utf-8");
      Root expectedModel = Models.forXml().parse(getClass().getResourceAsStream("model_css.xml"), "utf-8");

      // validate it, or throw RuntimeException
      Profiles.forObject().validate(profile);

      Root model = Profiles.forObject().buildModel(profile, SystemResourceType.Css);

      Assert.assertEquals(expectedModel.toString(), model.toString());
   }
   
   @Test
   public void testForImg() throws IOException, SAXException {
      Profile profile = Profiles.forXml().parse(getClass().getResourceAsStream("profile.xml"), "utf-8");
      Root expectedModel = Models.forXml().parse(getClass().getResourceAsStream("model_img.xml"), "utf-8");
      
      // validate it, or throw RuntimeException
      Profiles.forObject().validate(profile);
      
      Root model = Profiles.forObject().buildModel(profile, SystemResourceType.Image);
      
      Assert.assertEquals(expectedModel.toString(), model.toString());
   }
}
