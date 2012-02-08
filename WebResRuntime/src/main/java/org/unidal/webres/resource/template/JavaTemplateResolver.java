package org.unidal.webres.resource.template;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.INamespace;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ITemplate;
import org.unidal.webres.resource.api.ITemplateRef;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceResolver;
import org.unidal.webres.resource.spi.ITemplateEvaluator;
import org.unidal.webres.resource.spi.ITemplateProvider;
import org.unidal.webres.resource.spi.ITemplateProviderFactory;

public class JavaTemplateResolver implements IResourceResolver<ITemplateRef, ITemplate>,
      IResourceRegisterable<JavaTemplateResolver> {
   @Override
   public INamespace getNamespace() {
      return TemplateNamespace.JAVA;
   }

   @Override
   public JavaTemplateResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Template.Java;
   }

   @Override
   public Class<? super JavaTemplateResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Template;
   }

   @Override
   public ITemplate resolve(ITemplateRef ref, IResourceContext ctx) throws ResourceException {
      IResourceUrn urn = ref.getUrn();
      String path = urn.getResourceId();
      int pos = path.indexOf('/', 1);
      String type;

      if (pos > 0) {
         type = path.substring(1, pos);
         path = path.substring(pos);
      } else {
         type = path.substring(1);
         path = "";
      }

      ITemplateProviderFactory factory = ctx.lookup(ITemplateProviderFactory.class, type);

      if (factory == null) {
         throw new ResourceException(String.format("No ITemplateProviderFactory found for type(%s) of resource(%s)!", type, urn));
      }

      ITemplateProvider provider = factory.create(path);

      if (provider == null) {
         throw new ResourceException(String.format("No ITemplateProvider created for path(%s) of resource(%s)!", path, urn));
      }

      JavaTemplate template = new JavaTemplate(ctx, ref, provider);
      String language = template.getMeta().getLanguage();
      ITemplateEvaluator evaluator = ctx.lookup(ITemplateEvaluator.class, language);

      if (evaluator == null) {
         throw new ResourceException(String.format("No ITemplateEvaluator found for language(%s)!", language));
      }

      template.validate();
      template.setTemplateEvaluator(evaluator);
      return template;
   }
}