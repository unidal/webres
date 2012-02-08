package org.unidal.webres.tag.js;

import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.aggregation.JsAggregator;
import org.unidal.webres.resource.js.JsFactory;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.tag.support.ResourceTagTestSupport;

public class JsSlotTagTest extends ResourceTagTestSupport {
   @BeforeClass
   public static void beforeClass() throws Exception {
      setup(new JsSlotTagTest().copyResourceFrom("JsSlotTagTest"));
   }

   @Override
   protected String getContextPath() {
      return "/test";
   }

   protected Root prepareProfile(ResourceRuntimeContext ctx) {
      Root profile = ctx.getConfig().getProfile(SystemResourceType.Js);

      if (profile == null) {
         profile = new Root();
         ctx.getConfig().setProfile(SystemResourceType.Js, profile);
      }

      profile.addCommonSlot(new Slot("sys") //
            .addResource(new Resource("js.local:/ebaytime.js")) //
            .addResource(new Resource("js.local:/bitbybit/ebaytime.js")) //
            );

      return profile;
   }

   @Test
   public void testCommonSlotFromProfile() {
      JsSlotTag head = new JsSlotTag();
      ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();
      
      ctx.getResourceAggregator(SystemResourceType.Js);
      ctx.applyProfile();

      head.getModel().setId("HEAD");
      checkTag(head, "<script type=\"text/javascript\">// bitbybit ebaytime js here\r\n" + // 
            "// ebaytime js here\r\n" + //
            "</script>");
   }

   @Test
   public void testCommonSlotWithDeferRenderering() {
      JsSlotTag head = new JsSlotTag();

      ResourceRuntimeContext.ctx().setDeferRendering(true);

      head.getModel().setId("head");
      checkTag(head, "${MARKER,JsSlotTag:head}");

      JsSlotTag body = new JsSlotTag();

      body.getModel().setId("body");

      UseJsTag js1 = new UseJsTag();
      js1.getModel().setValue(JsFactory.forRef().createInlineRef("// js1"));
      js1.getModel().setId("js1");
      js1.getModel().setTarget("body");
      checkTag(js1, "${MARKER,UseJsTag:js1}");

      UseJsTag js2 = new UseJsTag();
      js2.getModel().setValue(JsFactory.forRef().createInlineRef("// js2"));
      js2.getModel().setId("js2");
      js2.getModel().setTarget("body");
      checkTag(js2, "${MARKER,UseJsTag:js2}");

      ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();
      JsAggregator aggregator = ctx.getResourceAggregator(SystemResourceType.Js);

      Root profile = prepareProfile(ctx);
      aggregator.registerSlot("body", "sys", true);

      ctx.applyProfile();

      try {
         checkTag(body, "${MARKER,JsSlotTag:body}");
         checkDeferTag(body, body.getModel().getId(), "<script type=\"text/javascript\">// ebaytime js here\r\n" + // 
               "// bitbybit ebaytime js here\r\n" + //
               "</script>" + //
               "<script type=\"text/javascript\">// js1\r\n" + //
               "// js2\r\n" + //
               "</script>");
      } finally {
         profile.removeCommonSlot("sys");
      }
   }

   @Test
   public void testCommonSlotWithoutDeferRenderering() {
      JsSlotTag head = new JsSlotTag();

      head.getModel().setId("head");
      checkTag(head, null);

      JsSlotTag body = new JsSlotTag();

      body.getModel().setId("body");

      ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();
      JsAggregator aggregator = ctx.getResourceAggregator(SystemResourceType.Js);

      Root profile = prepareProfile(ctx);
      aggregator.registerSlot("body", "sys", true);

      ctx.applyProfile();

      try {
         checkTag(body, "<script type=\"text/javascript\">// ebaytime js here\r\n" + // 
               "// bitbybit ebaytime js here\r\n" + //
               "</script>");
      } finally {
         profile.removeCommonSlot("sys");
      }
   }

   @Test
   public void testDedup() {
      ResourceRuntimeContext.ctx().setDeferRendering(true);

      UseJsTag js1 = new UseJsTag();
      js1.getModel().setValue(JsFactory.forRef().createLocalRef("/ebaytime.js"));
      js1.getModel().setId("js1");
      checkTag(js1, "${MARKER,UseJsTag:js1}");

      UseJsTag js2 = new UseJsTag();
      js2.getModel().setValue(JsFactory.forRef().createLocalRef("/ebaytime.js"));
      js2.getModel().setId("js2");
      js2.getModel().setTarget("bottom");
      checkTag(js2, "${MARKER,UseJsTag:js2}");
      
      UseJsTag js3 = new UseJsTag();
      js3.getModel().setValue(JsFactory.forRef().createLocalRef("/bitbybit/ebaytime.js"));
      js3.getModel().setId("js3");
      js3.getModel().setTarget("top");
      checkTag(js3, "${MARKER,UseJsTag:js3}");
      
      UseJsTag js4 = new UseJsTag();
      js4.getModel().setValue(JsFactory.forRef().createLocalRef("/bitbybit/ebaytime.js"));
      js4.getModel().setId("js4");
      js4.getModel().setTarget("bottom");
      checkTag(js4, "${MARKER,UseJsTag:js4}");

      JsSlotTag tag = new JsSlotTag();
      tag.getModel().setId("bottom");
      checkTag(tag, "${MARKER,JsSlotTag:bottom}");

      checkDeferTag(js1, js1.getModel().getId(), "<script src=\"/test/js/ebaytime.js\" type=\"text/javascript\"></script>");
      checkDeferTag(js2, js2.getModel().getId(), "");
      checkDeferTag(js3, js3.getModel().getId(), "<script src=\"/test/js/bitbybit/ebaytime.js\" type=\"text/javascript\"></script>");
      checkDeferTag(js4, js4.getModel().getId(), "");
      checkDeferTag(tag, tag.getModel().getId(), "");
   }
   
   @Test
   public void testTemplateWithDeferRendering() {
      ResourceRuntimeContext.ctx().setDeferRendering(true);
      
      UseJsTag js1 = new UseJsTag();
      js1.getModel().setValue(JsFactory.forRef().createLocalRef("/ebaytime.js"));
      js1.getModel().setId("js1");
      js1.getModel().setTarget("bottom");
      checkTag(js1, "${MARKER,UseJsTag:js1}");
      
      UseJsTag js2 = new UseJsTag();
      js2.getModel().setValue(JsFactory.forRef().createLocalRef("/bitbybit/ebaytime.js"));
      js2.getModel().setId("js2");
      js2.getModel().setTarget("bottom");
      checkTag(js2, "${MARKER,UseJsTag:js2}");
      
      JsSlotTag slot = new JsSlotTag();
      slot.getModel().setId("bottom");
      slot.getModel().setRenderType("inline");
      slot.getModel().setBodyContent("(function(){${this.content}})();");
      checkTag(slot, "${MARKER,JsSlotTag:bottom}");
      
      checkDeferTag(js1, js1.getModel().getId(), "");
      checkDeferTag(js2, js2.getModel().getId(), "");
      checkDeferTag(slot, slot.getModel().getId(), "<script type=\"text/javascript\">(function(){// ebaytime js here\r\n" + //
            "// bitbybit ebaytime js here\r\n" + //
      "})();</script>");
   }

   @Test
   public void testWithDeferRendering() {
      ResourceRuntimeContext.ctx().setDeferRendering(true);

      UseJsTag js1 = new UseJsTag();
      js1.getModel().setValue(JsFactory.forRef().createLocalRef("/ebaytime.js"));
      js1.getModel().setId("js1");
      js1.getModel().setTarget("bottom");
      checkTag(js1, "${MARKER,UseJsTag:js1}");

      UseJsTag js2 = new UseJsTag();
      js2.getModel().setValue(JsFactory.forRef().createLocalRef("/bitbybit/ebaytime.js"));
      js2.getModel().setId("js2");
      js2.getModel().setTarget("bottom");
      checkTag(js2, "${MARKER,UseJsTag:js2}");

      JsSlotTag slot = new JsSlotTag();
      slot.getModel().setId("bottom");
      checkTag(slot, "${MARKER,JsSlotTag:bottom}");

      checkDeferTag(js1, js1.getModel().getId(), "");
      checkDeferTag(js2, js2.getModel().getId(), "");
      checkDeferTag(slot, slot.getModel().getId(), "<script type=\"text/javascript\">// ebaytime js here\r\n" + //
            "// bitbybit ebaytime js here\r\n" + //
            "</script>");
   }

   @Test
   public void testWithoutDeferRendering() {
      UseJsTag js1 = new UseJsTag();
      js1.getModel().setValue(JsFactory.forRef().createLocalRef("/ebaytime.js"));
      js1.getModel().setTarget("bottom");
      checkTag(js1, "<script src=\"/test/js/ebaytime.js\" type=\"text/javascript\"></script>");

      UseJsTag js2 = new UseJsTag();
      js2.getModel().setValue(JsFactory.forRef().createLocalRef("/bitbybit/ebaytime.js"));
      js2.getModel().setTarget("bottom");
      checkTag(js2, "<script src=\"/test/js/bitbybit/ebaytime.js\" type=\"text/javascript\"></script>");

      JsSlotTag tag = new JsSlotTag();
      tag.getModel().setId("bottom");
      checkTag(tag, null);
   }
}
