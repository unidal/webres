package org.unidal.webres.tag.common;

import org.unidal.webres.helper.Markers;
import org.unidal.webres.resource.dummy.IDummyResource;
import org.unidal.webres.resource.dummy.IDummyResourceRef;
import org.unidal.webres.resource.dummy.DummyResourceRef;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceDeferRenderer;
import org.unidal.webres.tag.resource.ResourceTagSupport;

/**
 * <ul>Following use cases are supported:
 * <li>&lt;res:bean id="res" /&gt;</li>
 * </ul>
 */
public class TokenTag extends ResourceTagSupport<TokenTagModel, IDummyResourceRef, IDummyResource> {
   public TokenTag() {
      super(new TokenTagModel());
   }

   @Override
   public IDummyResourceRef build() {
      return DummyResourceRef.INSTANCE;
   }

   @Override
   public String render(final IDummyResourceRef ref) {
      final ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();
      boolean deferRenderingSupported = ctx.isDeferRendering();

      if (deferRenderingSupported) {
         final String key = getClass().getSimpleName() + ":" + getModel().getType().getName();
         final String marker = Markers.forDefer().build(key);

         ctx.getContainer().setAttribute(IResourceDeferRenderer.class, key, new IResourceDeferRenderer() {
            @Override
            public String deferRender() {
               return TokenTag.super.render(ref);
            }
         });

         return marker;
      } else {
         // res:token is not supported in non-defer rendering mode
         return null;
      }
   }
}
