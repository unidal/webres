package org.unidal.webres.resource.template;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ITemplate;
import org.unidal.webres.resource.api.ITemplateContext;
import org.unidal.webres.resource.api.ITemplateMeta;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.ITemplateEvaluator;
import org.unidal.webres.resource.spi.ITemplateProvider;

public abstract class TemplateSupport implements ITemplate {
   private IResourceContext m_ctx;

   private ITemplateEvaluator m_evaluator;

   public TemplateSupport(IResourceContext ctx) {
      m_ctx = ctx;
   }

   @Override
   public String evaluate(ITemplateContext ctx) throws Exception {
      m_evaluator.setContext(ctx);

      String result = m_evaluator.evaluate(m_ctx, this);

      return result;
   }

   protected IResourceContext getResourceContext() {
      return m_ctx;
   }

   protected void setTemplateEvaluator(ITemplateEvaluator evaluator) {
      m_evaluator = evaluator;
   }

   protected static class TemplateMeta implements ITemplateMeta {
      private ITemplateProvider m_provider;

      private IResourceUrn m_urn;

      public TemplateMeta(ITemplateProvider provider, IResourceUrn urn) {
         m_provider = provider;
         m_urn = urn;
      }

      @Override
      public String getLanguage() {
         return m_provider.getLanguage();
      }

      @Override
      public long getLastModified() {
         return m_provider.getLastModified();
      }

      @Override
      public long getLength() {
         return m_provider.getLength();
      }

      @Override
      public IResourceType getResourceType() {
         return SystemResourceType.Template;
      }

      @Override
      public IResourceUrn getUrn() {
         return m_urn;
      }
   }
}
