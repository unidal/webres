package org.unidal.webres.tag.js;

import org.unidal.webres.helper.Markers;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.aggregation.JsAggregator;
import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.api.IJsRef;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.js.JsFactory;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceDeferRenderer;
import org.unidal.webres.tag.ITagEnv;
import org.unidal.webres.tag.resource.ResourceTagSupport;

public class UseJsTag extends ResourceTagSupport<UseJsTagModel, IJsRef, IJs> {
   public UseJsTag() {
      super(new UseJsTagModel());
   }

   @Override
   public IJsRef build() {
      // <res:useJs value='${res.js.local.bitbybit.ebaytime_js}'/>}
      UseJsTagModel model = getModel();
      Object value = model.getValue();
      String id = model.getId();
      ITagEnv env = getEnv();
      IJsRef jsRef = null;

      if (!hasErrors() && id != null && env.findAttribute(id) != null) {
         error("id(%s) is conflicting with an existing attribute in %s!", id, value.getClass());
      }

      if (!hasErrors()) {
         String bodyContent = model.getBodyContent();

         if (bodyContent != null) {
            if (value != null) {
               error("Can't have both attribute(value) and an inline script set within res:useJs tag!");
            } else {
               jsRef = (id == null ? JsFactory.forRef().createInlineRef(bodyContent) : JsFactory.forRef()
                     .createInlineRef("/" + id, bodyContent));
            }
         } else {
            if (value == null) {
               error("Attribute(value) or an inline script is expected for res:useJs tag!");
            } else {
               jsRef = getResourceRef(IJsRef.class, value);
            }
         }
      }

      if (id != null) {
         // TODO enable js expression 
         // env.setPageAttribute(id, null);
      }

      return jsRef;
   }

   @Override
   public String render(final IJsRef ref) {
      if (ref != null) {
         final ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();
         boolean deferRenderingSupported = ctx.isDeferRendering();
         final JsAggregator aggregator = ctx.getResourceAggregator(SystemResourceType.Js);
         final String target = getModel().getTarget();

         aggregator.registerResource(target, ref);

         if (deferRenderingSupported) {
            String id = getModel().getId();
            final String key = getClass().getSimpleName() + ":" + (id != null ? id : System.identityHashCode(this));
            final String marker = Markers.forDefer().build(key);
            final IResourceUrn urn = ref.getUrn();

            ctx.getContainer().setAttribute(IResourceDeferRenderer.class, key, new IResourceDeferRenderer() {
               @Override
               public String deferRender() {
                  IJsRef r = aggregator.getResource(target, urn.toString());

                  if (r != null) {
                     return UseJsTag.super.render(r);
                  } else { // been aggregated
                     return "";
                  }
               }
            });

            return marker;
         }
      }

      return super.render(ref);
   }
}
