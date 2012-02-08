package org.unidal.webres.tag.resource;

import org.unidal.webres.resource.ResourceOutputType;
import org.unidal.webres.resource.api.IResourceOutputType;
import org.unidal.webres.resource.spi.IResourceConfigurator;
import org.unidal.webres.resource.spi.IResourceRegistry;
import org.unidal.webres.tag.common.TokenTagRenderer;
import org.unidal.webres.tag.css.CssSlotTagRenderer;
import org.unidal.webres.tag.css.UseCssTagRenderer;
import org.unidal.webres.tag.img.ImageTagRenderer;
import org.unidal.webres.tag.js.JsSlotTagRenderer;
import org.unidal.webres.tag.js.UseJsTagRenderer;
import org.unidal.webres.tag.link.LinkTagRenderer;

public class ResourceTagConfigurator implements IResourceConfigurator {
   @Override
   public void configure(IResourceRegistry registry) {
      registry.register(IResourceOutputType.class, ResourceOutputType.HTML);
      registry.register(IResourceMarkerProcessor.class, new ResourceMarkerProcessor());

      /* for token tag */
      for (TokenTagRenderer renderer : TokenTagRenderer.values()) {
         registry.register(renderer);
      }

      /* for img tag */
      for (ImageTagRenderer renderer : ImageTagRenderer.values()) {
         registry.register(renderer);
      }

      /* for link tag */
      for (LinkTagRenderer renderer : LinkTagRenderer.values()) {
         registry.register(renderer);
      }

      /* for js tags */
      for (UseJsTagRenderer renderer : UseJsTagRenderer.values()) {
         registry.register(renderer);
      }

      for (JsSlotTagRenderer renderer : JsSlotTagRenderer.values()) {
         registry.register(renderer);
      }

      /* for css tags */
      for (UseCssTagRenderer renderer : UseCssTagRenderer.values()) {
         registry.register(renderer);
      }

      for (CssSlotTagRenderer renderer : CssSlotTagRenderer.values()) {
         registry.register(renderer);
      }
   }
}