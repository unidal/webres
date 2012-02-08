package org.unidal.webres.tag.js;

import java.util.List;

import org.unidal.webres.helper.Markers;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.aggregation.JsAggregator;
import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceDeferRenderer;
import org.unidal.webres.tag.resource.ResourceTagState;
import org.unidal.webres.tag.resource.ResourceTagSupport;

public class JsSlotTag extends ResourceTagSupport<JsSlotTagModel, IJsRef, IJs> {
   public JsSlotTag() {
      super(new JsSlotTagModel());
   }

   @Override
   public IJsRef build() {
      return null;
   }

   @Override
   public String render(final IJsRef ref) {
      final ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();
      final boolean isDeferRendering = ctx.isDeferRendering();
      final JsAggregator aggregator = ctx.getResourceAggregator(SystemResourceType.Js);

      aggregator.registerSlot(getModel().getId());

      if (isDeferRendering) {
         final String key = getClass().getSimpleName() + ":" + getModel().getId();
         final String marker = Markers.forDefer().build(key);

         ctx.getContainer().setAttribute(IResourceDeferRenderer.class, key, new IResourceDeferRenderer() {
            @Override
            public String deferRender() {
               List<IJsRef> refs = aggregator.getAggregatedResourceWithCommonSlots(getModel().getId(), false);
               JsSlotTag tag = JsSlotTag.this;

               if (!refs.isEmpty()) {
                  try {
                     tag.getEnv().onBegin(tag, ResourceTagState.RENDERED);

                     if (refs.size() == 1) {
                        String content = JsSlotTag.super.render(refs.get(0));

                        return content;
                     } else {
                        StringBuilder sb = new StringBuilder(2048);

                        for (IJsRef ref : refs) {
                           String content = JsSlotTag.super.render(ref);

                           if (content != null) {
                              sb.append(content);
                           }
                        }

                        return sb.toString();
                     }
                  } catch (RuntimeException cause) {
                     tag.getEnv().onError(tag, ResourceTagState.RENDERED, cause);
                     throw cause;
                  } finally {
                     tag.getEnv().onEnd(tag, ResourceTagState.RENDERED);
                  }
               } else {
                  return "";
               }
            }
         });

         return marker;
      } else {
         return renderCommonSlot(aggregator);
      }
   }

   protected String renderCommonSlot(JsAggregator aggregator) {
      List<IJsRef> refs = aggregator.getAggregatedResourceWithCommonSlots(getModel().getId(), true);

      if (!refs.isEmpty()) {
         StringBuilder sb = new StringBuilder(2048);

         for (IJsRef ref : refs) {
            String content = super.render(ref);

            if (content != null) {
               sb.append(content);
            }
         }
         return sb.toString();
      } else {
         return null;
      }
   }
}
