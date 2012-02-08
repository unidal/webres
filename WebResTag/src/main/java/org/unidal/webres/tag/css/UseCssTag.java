package org.unidal.webres.tag.css;

import org.unidal.webres.helper.Markers;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.aggregation.CssAggregator;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.api.ICssRef;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.css.CssFactory;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceDeferRenderer;
import org.unidal.webres.tag.ITagEnv;
import org.unidal.webres.tag.resource.ResourceTagSupport;

public class UseCssTag extends ResourceTagSupport<UseCssTagModel, ICssRef, ICss> {
   public UseCssTag() {
      super(new UseCssTagModel());
   }

   @Override
   public ICssRef build() {
      // <res:useCss value='${res.css.local.bitbybit.ebaytime_css}'/>}
      UseCssTagModel model = getModel();
      Object value = model.getValue();
      String id = model.getId();
      ITagEnv env = getEnv();
      ICssRef cssRef = null;

      if (!hasErrors() && id != null && env.findAttribute(id) != null) {
         error("id(%s) is conflicting with an existing attribute in %s!", id, value.getClass());
      }

      if (!hasErrors()) {
         String bodyContent = model.getBodyContent();

         if (bodyContent != null) {
            if (value != null) {
               error("Can't have both attribute(value) and an inline script set within res:useCss tag!");
            } else {
               cssRef = (id == null ? CssFactory.forRef().createInlineRef(bodyContent) : CssFactory.forRef()
                     .createInlineRef("/" + id, bodyContent));
            }
         } else {
            if (value == null) {
               error("Attribute(value) or an inline script is expected for res:useCss tag!");
            } else {
               cssRef = getResourceRef(ICssRef.class, value);
            }
         }
      }

      if (id != null) {
         // TODO enable css expression 
         // env.setPageAttribute(id, null);
      }

      return cssRef;
   }

   @Override
   public String render(ICssRef ref) {
      if (ref != null) {
         final ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();
         boolean deferRenderingSupported = ctx.isDeferRendering();
         final CssAggregator aggregator = ctx.getResourceAggregator(SystemResourceType.Css);
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
                  ICssRef r = aggregator.getResource(target, urn.toString());

                  if (r != null) {
                     return UseCssTag.super.render(r);
                  } else {
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
