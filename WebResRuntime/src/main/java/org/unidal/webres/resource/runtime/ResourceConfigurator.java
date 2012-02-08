package org.unidal.webres.resource.runtime;

import org.unidal.webres.resource.aggregation.ResourceAggregatorFactory;
import org.unidal.webres.resource.css.AggregatedCssResolver;
import org.unidal.webres.resource.css.AggregatedCssUrlBuilder;
import org.unidal.webres.resource.css.InlineCssResolver;
import org.unidal.webres.resource.css.InlineCssUrlBuilder;
import org.unidal.webres.resource.css.LocalCssResolver;
import org.unidal.webres.resource.css.LocalCssUrlBuilder;
import org.unidal.webres.resource.css.SharedCssResolver;
import org.unidal.webres.resource.css.SharedCssUrlBuilder;
import org.unidal.webres.resource.css.WarCssResolver;
import org.unidal.webres.resource.css.WarCssUrlBuilder;
import org.unidal.webres.resource.expression.CssExpressionFactory;
import org.unidal.webres.resource.expression.CssPropertyEvaluator;
import org.unidal.webres.resource.expression.ImageExpressionFactory;
import org.unidal.webres.resource.expression.ImagePropertyEvaluator;
import org.unidal.webres.resource.expression.JsExpressionFactory;
import org.unidal.webres.resource.expression.JsPropertyEvaluator;
import org.unidal.webres.resource.expression.LinkExpressionFactory;
import org.unidal.webres.resource.expression.LinkPropertyEvaluator;
import org.unidal.webres.resource.img.ImageDataUriBuilder;
import org.unidal.webres.resource.img.LocalImageResolver;
import org.unidal.webres.resource.img.LocalImageUrlBuilder;
import org.unidal.webres.resource.img.PicsImageResolver;
import org.unidal.webres.resource.img.PicsImageUrlBuilder;
import org.unidal.webres.resource.img.SharedImageResolver;
import org.unidal.webres.resource.img.SharedImageUrlBuilder;
import org.unidal.webres.resource.img.WarImageResolver;
import org.unidal.webres.resource.img.WarImageUrlBuilder;
import org.unidal.webres.resource.js.AggregatedJsResolver;
import org.unidal.webres.resource.js.AggregatedJsUrlBuilder;
import org.unidal.webres.resource.js.InlineJsResolver;
import org.unidal.webres.resource.js.InlineJsUrlBuilder;
import org.unidal.webres.resource.js.LocalJsResolver;
import org.unidal.webres.resource.js.LocalJsUrlBuilder;
import org.unidal.webres.resource.js.SharedJsResolver;
import org.unidal.webres.resource.js.SharedJsUrlBuilder;
import org.unidal.webres.resource.js.WarJsResolver;
import org.unidal.webres.resource.js.WarJsUrlBuilder;
import org.unidal.webres.resource.link.PagesLinkResolver;
import org.unidal.webres.resource.link.PagesLinkUrlBuilder;
import org.unidal.webres.resource.spi.IResourceConfigurator;
import org.unidal.webres.resource.spi.IResourceRegistry;
import org.unidal.webres.resource.spi.IResourceTokenStorage;
import org.unidal.webres.resource.template.InlineTemplateResolver;
import org.unidal.webres.resource.template.JavaTemplateResolver;
import org.unidal.webres.resource.template.LocalTemplateResolver;
import org.unidal.webres.resource.template.evaluator.SimpleTemplateEvaluator;
import org.unidal.webres.resource.token.ResourceTokenStorage;
import org.unidal.webres.resource.variation.ResourceMappingApplier;

public class ResourceConfigurator implements IResourceConfigurator {
   @Override
   public void configure(IResourceRegistry registry) {
      registry.register(IResourceTokenStorage.class, ResourceTokenStorage.INSTANCE);
      registry.register(new ResourceAggregatorFactory());
      registry.register(new ResourceMappingApplier());

      /* below for image resource */
      registry.register(new ImageExpressionFactory());

      for (ImagePropertyEvaluator evaluator : ImagePropertyEvaluator.values()) {
         registry.register(evaluator);
      }

      registry.register(new ImageDataUriBuilder());
      registry.register(new LocalImageResolver());
      registry.register(new LocalImageUrlBuilder());
      registry.register(new PicsImageResolver());
      registry.register(new PicsImageUrlBuilder());
      registry.register(new SharedImageResolver());
      registry.register(new SharedImageUrlBuilder());
      registry.register(new WarImageResolver());
      registry.register(new WarImageUrlBuilder());

      /* below for link resource */
      registry.register(new LinkExpressionFactory());

      for (LinkPropertyEvaluator evaluator : LinkPropertyEvaluator.values()) {
         registry.register(evaluator);
      }

      registry.register(new PagesLinkResolver());
      registry.register(new PagesLinkUrlBuilder());

      /* below for js resource */
      registry.register(new JsExpressionFactory());

      for (JsPropertyEvaluator evaluator : JsPropertyEvaluator.values()) {
         registry.register(evaluator);
      }

      registry.register(new LocalJsResolver());
      registry.register(new LocalJsUrlBuilder());
      registry.register(new InlineJsResolver());
      registry.register(new InlineJsUrlBuilder());
      registry.register(new SharedJsResolver());
      registry.register(new SharedJsUrlBuilder());
      registry.register(new WarJsResolver());
      registry.register(new WarJsUrlBuilder());
      registry.register(new AggregatedJsResolver());
      registry.register(new AggregatedJsUrlBuilder());

      /* below for css resource */
      registry.register(new CssExpressionFactory());

      for (CssPropertyEvaluator evaluator : CssPropertyEvaluator.values()) {
         registry.register(evaluator);
      }

      registry.register(new LocalCssResolver());
      registry.register(new LocalCssUrlBuilder());
      registry.register(new InlineCssResolver());
      registry.register(new InlineCssUrlBuilder());
      registry.register(new SharedCssResolver());
      registry.register(new SharedCssUrlBuilder());
      registry.register(new WarCssResolver());
      registry.register(new WarCssUrlBuilder());
      registry.register(new AggregatedCssResolver());
      registry.register(new AggregatedCssUrlBuilder());

      /* below for template resource */
      registry.register(new LocalTemplateResolver());
      registry.register(new InlineTemplateResolver());
      registry.register(new JavaTemplateResolver());
      
      registry.register(new SimpleTemplateEvaluator());
   }
}
