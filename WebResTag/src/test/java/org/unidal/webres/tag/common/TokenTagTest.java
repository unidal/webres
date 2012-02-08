package org.unidal.webres.tag.common;

import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.js.JsFactory;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.tag.js.JsSlotTag;
import org.unidal.webres.tag.js.UseJsTag;
import org.unidal.webres.tag.support.ResourceTagTestSupport;

public class TokenTagTest extends ResourceTagTestSupport {
   @BeforeClass
   public static void beforeClass() throws Exception {
      setup(new TokenTagTest().copyResourceFrom("TokenTagTest"));
   }

   @Override
   protected String getContextPath() {
      return "/test";
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

      JsSlotTag tag = new JsSlotTag();
      tag.getModel().setId("bottom");
      checkTag(tag, "${MARKER,JsSlotTag:bottom}");

      checkDeferTag(js1, js1.getModel().getId(), "");
      checkDeferTag(js2, js2.getModel().getId(), "");
      checkDeferTag(tag, tag.getModel().getId(), "<script type=\"text/javascript\">// ebaytime js here\r\n" + //
            "// bitbybit ebaytime js here\r\n" + //
            "</script>");

      TokenTag token = new TokenTag();
      token.getModel().setType(SystemResourceType.Js.getName());

      checkTag(token, "${MARKER,TokenTag:js}");
      checkDeferTag(token, null, "d83e5231");
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

      JsSlotTag slot = new JsSlotTag();
      slot.getModel().setId("bottom");
      checkTag(slot, null);

      TokenTag token = new TokenTag();
      token.getModel().setType(SystemResourceType.Js.getName());

      checkTag(token, null);
   }
}
