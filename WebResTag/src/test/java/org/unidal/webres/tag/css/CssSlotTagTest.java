package org.unidal.webres.tag.css;

import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.aggregation.CssAggregator;
import org.unidal.webres.resource.css.CssFactory;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.model.entity.Root;
import org.unidal.webres.resource.model.entity.Slot;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.tag.support.ResourceTagTestSupport;

public class CssSlotTagTest extends ResourceTagTestSupport {
   @BeforeClass
   public static void beforeClass() throws Exception {
      setup(new CssSlotTagTest().copyResourceFrom("CssSlotTagTest"));
   }

   @Override
   protected String getContextPath() {
      return "/test";
   }

   protected Root prepareProfile(ResourceRuntimeContext ctx) {
      Root profile = new Root();

      ctx.getConfig().setProfile(SystemResourceType.Css, profile);

      profile.addCommonSlot(new Slot("sys") //
            .addResource(new Resource("css.local:/ebaytime.css")) //
            .addResource(new Resource("css.local:/bitbybit/ebaytime.css")) //
            );

      return profile;
   }

   @Test
   public void testCommonSlotFromProfile() {
      CssSlotTag head = new CssSlotTag();
      ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();

      ctx.getResourceAggregator(SystemResourceType.Css);
      ctx.applyProfile();

      head.getModel().setId("HEAD");
      checkTag(head, "<style type=\"text/css\">.error {font-weight:bold; color:red}\r\n" + 
      		".footer {color:#00c; font-size:12px}\r\n" + 
      		"</style>");
   }

   @Test
   public void testCommonSlotWithDeferRenderering() {
      CssSlotTag head = new CssSlotTag();

      ResourceRuntimeContext.ctx().setDeferRendering(true);

      head.getModel().setId("head");
      checkTag(head, "${MARKER,CssSlotTag:head}");

      CssSlotTag body = new CssSlotTag();

      body.getModel().setId("body");

      UseCssTag css1 = new UseCssTag();
      css1.getModel().setValue(CssFactory.forRef().createInlineRef("// css1"));
      css1.getModel().setId("css1");
      css1.getModel().setTarget("body");
      checkTag(css1, "${MARKER,UseCssTag:css1}");

      UseCssTag css2 = new UseCssTag();
      css2.getModel().setValue(CssFactory.forRef().createInlineRef("// css2"));
      css2.getModel().setId("css2");
      css2.getModel().setTarget("body");
      checkTag(css2, "${MARKER,UseCssTag:css2}");

      ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();
      CssAggregator aggregator = ctx.getResourceAggregator(SystemResourceType.Css);

      Root profile = prepareProfile(ctx);
      aggregator.registerSlot("body", "sys", true);

      ctx.applyProfile();

      try {
         checkTag(body, "${MARKER,CssSlotTag:body}");
         checkDeferTag(body, body.getModel().getId(),
               "<style type=\"text/css\">.footer {color:#00c; font-size:12px}\r\n" + //
                     ".error {font-weight:bold; color:red}\r\n" + //
                     "</style>" + //
                     "<style type=\"text/css\">// css1\r\n" + // 
                     "// css2\r\n" + //
                     "</style>");
      } finally {
         profile.removeCommonSlot("sys");
      }
   }

   @Test
   public void testCommonSlotWithoutDeferRenderering() {
      CssSlotTag head = new CssSlotTag();

      head.getModel().setId("head");
      checkTag(head, null);

      CssSlotTag body = new CssSlotTag();

      body.getModel().setId("body");
      ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();
      CssAggregator aggregator = ctx.getResourceAggregator(SystemResourceType.Css);

      Root profile = prepareProfile(ctx);
      aggregator.registerSlot("body", "sys", true);

      ctx.applyProfile();

      try {
         checkTag(body, "<style type=\"text/css\">.footer {color:#00c; font-size:12px}\r\n" + // 
               ".error {font-weight:bold; color:red}\r\n" + //
               "</style>");
      } finally {
         profile.removeCommonSlot("sys");
      }
   }

   @Test
   public void testWithDeferRendering() {
      ResourceRuntimeContext.ctx().setDeferRendering(true);

      UseCssTag css1 = new UseCssTag();
      css1.getModel().setValue(CssFactory.forRef().createLocalRef("/ebaytime.css"));
      css1.getModel().setId("css1");
      css1.getModel().setTarget("bottom");
      checkTag(css1, "${MARKER,UseCssTag:css1}");

      UseCssTag css2 = new UseCssTag();
      css2.getModel().setValue(CssFactory.forRef().createLocalRef("/bitbybit/ebaytime.css"));
      css2.getModel().setId("css2");
      css2.getModel().setTarget("bottom");
      checkTag(css2, "${MARKER,UseCssTag:css2}");

      CssSlotTag tag = new CssSlotTag();
      tag.getModel().setId("bottom");
      checkTag(tag, "${MARKER,CssSlotTag:bottom}");

      checkDeferTag(css1, css1.getModel().getId(), "");
      checkDeferTag(css2, css2.getModel().getId(), "");
      checkDeferTag(tag, tag.getModel().getId(), "<style type=\"text/css\">.footer {color:#00c; font-size:12px}\r\n" + // 
            ".error {font-weight:bold; color:red}\r\n" + //
            "</style>");
   }

   @Test
   public void testWithoutDeferRendering() {
      UseCssTag css1 = new UseCssTag();
      css1.getModel().setValue(CssFactory.forRef().createLocalRef("/ebaytime.css"));
      css1.getModel().setTarget("bottom");
      checkTag(css1, "<link href=\"/test/css/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">");

      UseCssTag css2 = new UseCssTag();
      css2.getModel().setValue(CssFactory.forRef().createLocalRef("/bitbybit/ebaytime.css"));
      css2.getModel().setTarget("bottom");
      checkTag(css2, "<link href=\"/test/css/bitbybit/ebaytime.css\" type=\"text/css\" rel=\"stylesheet\">");

      CssSlotTag tag = new CssSlotTag();
      tag.getModel().setId("bottom");
      checkTag(tag, null);
   }
}
