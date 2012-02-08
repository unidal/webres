package org.unidal.webres.resource.model;

import java.io.IOException;

import org.junit.Assert;

import org.junit.Test;
import org.xml.sax.SAXException;

import org.unidal.webres.resource.model.entity.CommonSlotRef;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.model.entity.SlotGroup;
import org.unidal.webres.resource.model.entity.SlotRef;

public class ModelTest {
   @Test
   public void testWithJava() {
      Root model = new Root();

      model.addCommonSlot(new Slot("sys1") //
            .addResource(new Resource("firstJs")) //
            .addResource(new Resource("thirdJs")) //
            );
      model.addPage(new Page("/jsp/home.jsp") //
            .addSlot(new Slot("HEAD") //
                  .addResource(new Resource("trackingJs"))) //
            .addCommonSlotRef(new CommonSlotRef("sys1").setBeforeSlot("HEAD")) //
            .addCommonSlotRef(new CommonSlotRef("sys2").setAfterSlot("BODY")) //
            .addSlotGroup(new SlotGroup("group1") //
                  .setMainSlot("BODY") //
                  .addSlotRef(new SlotRef("Header")) //
                  .addSlotRef(new SlotRef("BODY")) //
            ));

      // validate it, or throw RuntimeException
      Models.forObject().validate(model);
   }

   @Test
   public void testForXml() throws IOException, SAXException {
      Root src = Models.forXml().parse(getClass().getResourceAsStream("model_final.xml"), "utf-8");

      // validate it, or throw RuntimeException
      Models.forObject().validate(src);

      Root src2 = Models.forXml().parse(src.toString());

      // parse, build, parse and build again should work well
      // this proves parsing and building are consistent
      Assert.assertEquals(src.toString(), src2.toString());
   }
}
