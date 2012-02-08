package org.unidal.webres.server;

import org.unidal.webres.resource.spi.IResourceConfigurator;
import org.unidal.webres.resource.spi.IResourceRegistry;
import org.unidal.webres.server.css.SimpleAggregatedCssUrlBuilder;
import org.unidal.webres.server.css.SimpleInlineCssResolver;
import org.unidal.webres.server.css.SimpleInlineCssUrlBuilder;
import org.unidal.webres.server.css.SimpleSharedCssUrlBuilder;
import org.unidal.webres.server.css.SimpleWarCssUrlBuilder;
import org.unidal.webres.server.img.SimpleSharedImageUrlBuilder;
import org.unidal.webres.server.img.SimpleWarImageUrlBuilder;
import org.unidal.webres.server.js.SimpleAggregatedJsUrlBuilder;
import org.unidal.webres.server.js.SimpleInlineJsResolver;
import org.unidal.webres.server.js.SimpleInlineJsUrlBuilder;
import org.unidal.webres.server.js.SimpleSharedJsUrlBuilder;
import org.unidal.webres.server.js.SimpleWarJsUrlBuilder;
import org.unidal.webres.server.link.CmdLinkResolver;
import org.unidal.webres.server.link.CmdLinkUrlBuilder;
import org.unidal.webres.server.template.JspTemplateEvaluator;

public class SimpleResourceConfigurator implements IResourceConfigurator {
   @Override
   public void configure(IResourceRegistry registry) {
      String aggregationPrefix = "/z";
      String fragmentPrefix = "/f";

      registry.register(new CmdLinkResolver());
      registry.register(new CmdLinkUrlBuilder());

      registry.register(new SimpleSharedImageUrlBuilder(fragmentPrefix));
      registry.register(new SimpleSharedCssUrlBuilder(fragmentPrefix));
      registry.register(new SimpleSharedJsUrlBuilder(fragmentPrefix));

      registry.register(new SimpleWarImageUrlBuilder(fragmentPrefix));
      registry.register(new SimpleWarCssUrlBuilder(fragmentPrefix));
      registry.register(new SimpleWarJsUrlBuilder(fragmentPrefix));

      registry.register(new SimpleInlineCssUrlBuilder(fragmentPrefix));
      registry.register(new SimpleInlineCssResolver());
      registry.register(new SimpleInlineJsUrlBuilder(fragmentPrefix));
      registry.register(new SimpleInlineJsResolver());

      registry.register(new SimpleAggregatedJsUrlBuilder(aggregationPrefix));
      registry.register(new SimpleAggregatedCssUrlBuilder(aggregationPrefix));

      registry.register(new JspTemplateEvaluator());
   }
}