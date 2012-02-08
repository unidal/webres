package org.unidal.webres.resource.template;

import java.io.File;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.annotation.WarRoot;
import org.unidal.webres.resource.api.INamespace;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ITemplate;
import org.unidal.webres.resource.api.ITemplateRef;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.helper.ResourceResolvings;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceResolver;
import org.unidal.webres.resource.spi.ITemplateEvaluator;

public class LocalTemplateResolver implements IResourceResolver<ITemplateRef, ITemplate>,
      IResourceRegisterable<LocalTemplateResolver> {
   private File m_warRoot;

   protected File getTemplateFile(IResourceContext ctx, IResourceUrn urn, File base) {
      File templateFile = ResourceResolvings.fromDir().resolve(ctx, urn, base);

      if (!templateFile.isFile()) {
         throw new ResourceException(String.format("Template(%s) not found!", templateFile));
      }

      return templateFile;
   }

   @Override
   public INamespace getNamespace() {
      return TemplateNamespace.LOCAL;
   }

   @Override
   public LocalTemplateResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Template.Local;
   }

   @Override
   public Class<? super LocalTemplateResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Template;
   }

   @Override
   public LocalTemplate resolve(ITemplateRef ref, IResourceContext ctx) throws ResourceException {
      File templateFile = getTemplateFile(ctx, ref.getUrn(), m_warRoot);
      LocalTemplate template = new LocalTemplate(ctx, ref, templateFile);
      String language = template.getMeta().getLanguage();
      ITemplateEvaluator evaluator = ctx.lookup(ITemplateEvaluator.class, language);

      if (evaluator == null) {
         throw new ResourceException(String.format("No template evaluator found for language(%s)!", language));
      }

      template.validate();
      template.setTemplateEvaluator(evaluator);
      return template;
   }

   @WarRoot
   public void setWarRoot(File warRoot) {
      m_warRoot = warRoot;
   }
}