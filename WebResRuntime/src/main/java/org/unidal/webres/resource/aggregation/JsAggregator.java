package org.unidal.webres.resource.aggregation;

import java.util.List;

import org.unidal.webres.helper.Markers;
import org.unidal.webres.resource.ResourceFactory;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.api.IResourceRef;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.js.JsFactory;
import org.unidal.webres.resource.js.Scripts;
import org.unidal.webres.resource.model.entity.Resource;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceDeferRenderer;

public class JsAggregator extends ResourceAggregator<IJsRef> {
   public JsAggregator(String pageId) {
      super(pageId, SystemResourceType.Js);
   }

   /**
    * Add resource to a given slot and return an HTML snippet 
    * or a marker which will be picked and be handled during 2nd phase of processing. <p>
    * 
    * The caller (i.e. tag handler and application) is responsible of writing it out without any changes.
    * 
    * @param slotId
    * @param ref
    * @return HTML snippet or marker
    */
   public String addResource(final String slotId, final IJsRef ref) {
      // for dedup purpose
      if (registerResource(slotId, ref)) {
         final ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();

         if (ctx.isDeferRendering()) {
            final String key = getClass().getSimpleName() + ":" + ref.getUrn();
            final String marker = Markers.forDefer().build(key);
            final IResourceUrn urn = ref.getUrn();

            ctx.getContainer().setAttribute(IResourceDeferRenderer.class, key, new IResourceDeferRenderer() {
               @Override
               public String deferRender() {
                  IJsRef r = JsAggregator.this.getResource(slotId, urn.toString());

                  if (r != null) {
                     return render(r);
                  } else { // been aggregated
                     return "";
                  }
               }
            });

            return marker;
         } else {
            return render(ref);
         }
      }

      return "";
   }

   @Override
   protected IJsRef aggregateResource(String slotId, List<Resource> resources) {
      IJsRef[] refs = new IJsRef[resources.size()];
      int index = 0;

      for (Resource resource : resources) {
         IResourceRef<?> ref = (IResourceRef<?>) resource.getReference();

         if (ref == null) { // come from profile
            ref = ResourceFactory.forRef().createRefFromUrn(resource.getUrn());
         }

         refs[index++] = (IJsRef) ref;
      }

      return JsFactory.forRef().createAggregatedRef("/" + slotId, refs);
   }

   protected String render(IJsRef ref) {
      if (ref != null) {
         IResourceContext ctx = ResourceRuntimeContext.ctx().getResourceContext();

         if (ctx == null) {
            throw new RuntimeException("No IResourceContext was registered!");
         }

         IJs js = ref.resolve(ctx);
         String url = ctx.isSecure() ? js.getSecureUrl() : js.getUrl();

         if (url != null) {
            return Scripts.forHtml().buildScript(url, null);
         } else {
            // fall back to inline text
            return Scripts.forHtml().buildInlineScript(js.getContent(), null);
         }
      }

      return null;
   }
}
