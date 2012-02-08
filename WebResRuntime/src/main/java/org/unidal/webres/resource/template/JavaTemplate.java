package org.unidal.webres.resource.template;

import org.unidal.webres.resource.api.ITemplateMeta;
import org.unidal.webres.resource.api.ITemplateRef;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.ITemplateProvider;

public class JavaTemplate extends TemplateSupport {
   private ITemplateProvider m_provider;

   private TemplateMeta m_meta;

   public JavaTemplate(IResourceContext ctx, ITemplateRef ref, ITemplateProvider provider) {
      super(ctx);

      m_provider = provider;
      m_meta = new TemplateMeta(m_provider, ref.getUrn());
   }

   @Override
   public String getContent() {
      return m_provider.getContent();
   }

   @Override
   public ITemplateMeta getMeta() {
      return m_meta;
   }

   @Override
   public void validate() throws ResourceException {
      // nothing here
   }
}
