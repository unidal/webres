package org.unidal.webres.tag.css;

import java.util.List;

import org.unidal.webres.helper.Markers;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.aggregation.CssAggregator;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceDeferRenderer;
import org.unidal.webres.tag.resource.ResourceTagState;
import org.unidal.webres.tag.resource.ResourceTagSupport;

public class CssSlotTag extends ResourceTagSupport<CssSlotTagModel, ICssRef, ICss> {
   public CssSlotTag() {
      super(new CssSlotTagModel());
   }

   @Override
   public ICssRef build() {
      return null;
   }

   @Override
   public String render(final ICssRef ref) {
      final ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();
      final boolean isDeferRendering = ctx.isDeferRendering();
      final CssAggregator aggregator = ctx.getResourceAggregator(SystemResourceType.Css);

      aggregator.registerSlot(getModel().getId());

      if (isDeferRendering) {
         final String key = getClass().getSimpleName() + ":" + getModel().getId();
         final String marker = Markers.forDefer().build(key);

         ctx.getContainer().setAttribute(IResourceDeferRenderer.class, key, new IResourceDeferRenderer() {
            @Override
            public String deferRender() {
               List<ICssRef> refs = aggregator.getAggregatedResourceWithCommonSlots(getModel().getId(), false);
               CssSlotTag tag = CssSlotTag.this;

               if (!refs.isEmpty()) {
                  try {
                     tag.getEnv().onBegin(tag, ResourceTagState.RENDERED);

                     if (refs.size() == 1) {
                        String content = CssSlotTag.super.render(refs.get(0));

                        return content;
                     } else {
                        StringBuilder sb = new StringBuilder(2048);

                        for (ICssRef ref : refs) {
                           String content = CssSlotTag.super.render(ref);

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

   protected String renderCommonSlot(CssAggregator aggregator) {
      List<ICssRef> refs = aggregator.getAggregatedResourceWithCommonSlots(getModel().getId(), true);

      if (!refs.isEmpty()) {
         StringBuilder sb = new StringBuilder(2048);

         for (ICssRef ref : refs) {
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
