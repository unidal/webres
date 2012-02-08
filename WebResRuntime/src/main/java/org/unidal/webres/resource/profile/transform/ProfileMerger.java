package org.unidal.webres.resource.profile.transform;

import java.util.Stack;

import org.unidal.webres.resource.profile.entity.Css;
import org.unidal.webres.resource.profile.entity.CssSlot;
import org.unidal.webres.resource.profile.entity.Js;
import org.unidal.webres.resource.profile.entity.JsSlot;
import org.unidal.webres.resource.profile.entity.Profile;

public class ProfileMerger extends DefaultMerger {
   private Stack<Object> m_stack = new Stack<Object>();

   public ProfileMerger(Profile profile) {
      super(profile);
   }

   @Override
   public void visitCssSlotChildren(CssSlot old, CssSlot slot) {
      if (old != null) {
         m_stack.push(old);

         if (old.isOverride()) {
            old.getCssList().clear();

            for (Css css : slot.getCssList()) {
               old.addCss(css);
            }
         } else {
            for (Css css : slot.getCssList()) {
               visitCss(css);
            }
         }

         m_stack.pop();
      }
   }

   @Override
   public void visitJsSlotChildren(JsSlot old, JsSlot slot) {
      if (old != null) {
         m_stack.push(old);

         if (old.isOverride()) {
            old.getJsList().clear();

            for (Js js : slot.getJsList()) {
               old.addJs(js);
            }
         } else {
            for (Js js : slot.getJsList()) {
               visitJs(js);
            }
         }

         m_stack.pop();
      }
   }
}
