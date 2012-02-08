package org.unidal.webres.tag.support;

import org.junit.Assert;

import org.unidal.webres.resource.api.IResource;
import org.unidal.webres.resource.api.IResourceRef;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceRegistry;
import org.unidal.webres.resource.spi.IResourceDeferRenderer;
import org.unidal.webres.resource.support.ResourceTestSupport;
import org.unidal.webres.tag.CoreTagEnv;
import org.unidal.webres.tag.ITagEnv;
import org.unidal.webres.tag.resource.IResourceTagRenderType;
import org.unidal.webres.tag.resource.ResourceTagConfigurator;
import org.unidal.webres.tag.resource.ResourceTagModelSupport;
import org.unidal.webres.tag.resource.ResourceTagSupport;

public abstract class ResourceTagTestSupport extends ResourceTestSupport {
   protected <R extends IResource<?, ?>, REF extends IResourceRef<R>> void checkDeferTag(
         ResourceTagSupport<? extends ResourceTagModelSupport<? extends IResourceTagRenderType>, REF, R> tag,
         String id, String expected) {
      ResourceRuntimeContext ctx = ResourceRuntimeContext.ctx();
      String key = tag.getClass().getSimpleName() + (id == null ? "" : ":" + id);
      IResourceDeferRenderer renderer = ctx.getContainer().getAttribute(IResourceDeferRenderer.class, key);

      if (renderer != null) {
         String actual = renderer.deferRender();

         Assert.assertEquals(expected, actual);
      }
   }

   protected <R extends IResource<?, ?>, REF extends IResourceRef<R>> void checkTag(
         ResourceTagSupport<? extends ResourceTagModelSupport<? extends IResourceTagRenderType>, REF, R> tag,
         String expectedOutput) {
      tag.setEnv(createCoreTagEnv());
      tag.start();

      REF ref = tag.build();

      String output = tag.render(ref);
      tag.end();

      Assert.assertEquals(expectedOutput, output);
   }

   @Override
   protected void configure() throws Exception {
      super.configure();

      new ResourceTagConfigurator().configure(getRegistry());
   }

   public ITagEnv createCoreTagEnv() {
      IResourceRegistry registry = ResourceRuntimeContext.ctx().getRegistry();

      return new CoreTagEnv(registry, "/" + getClass().getSimpleName() + ".jsp");
   }
}
