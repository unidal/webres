package org.unidal.webres.resource.template;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.INamespace;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.ITemplate;
import org.unidal.webres.resource.api.ITemplateRef;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceResolver;
import org.unidal.webres.resource.spi.ITemplateEvaluator;

public class InlineTemplateResolver implements IResourceResolver<ITemplateRef, ITemplate>,
      IResourceRegisterable<InlineTemplateResolver> {
   @Override
   public INamespace getNamespace() {
      return TemplateNamespace.INLINE;
   }

   @Override
   public InlineTemplateResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Template.Inline;
   }

   @Override
   public Class<? super InlineTemplateResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Template;
   }

   @Override
   public ITemplate resolve(ITemplateRef ref, IResourceContext ctx) throws ResourceException {
      InlineTemplate template = new InlineTemplate(ctx, (InlineTemplateRef) ref);
      String language = template.getMeta().getLanguage();
      ITemplateEvaluator evaluator = ctx.lookup(ITemplateEvaluator.class, language);

      if (evaluator == null) {
         throw new ResourceException(String.format("No template evaluator found for language(%s)!", language));
      }

      template.validate();
      template.setTemplateEvaluator(evaluator);
      return template;
   }
}