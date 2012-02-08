package org.unidal.webres.tag.link;

import org.unidal.webres.resource.api.ILink;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.tag.ITagLookupManager;
import org.unidal.webres.tag.resource.IResourceTagRenderType;
import org.unidal.webres.tag.resource.IResourceTagRenderer;

public enum LinkTagRenderer implements IResourceTagRenderer<LinkTag, ILink>, IResourceRegisterable<LinkTagRenderer> {
   DEFAULT(LinkTagRenderType.DEFAULT) {
      @Override
      public String render(LinkTag tag, ILink link) {
         ITagLookupManager manager = tag.getEnv().getLookupManager();
         IResourceContext ctx = manager.lookupComponent(IResourceContext.class);
         LinkTagModel model = tag.getModel();
         boolean secure = model.getSecure() != null ? model.getSecure().booleanValue() : ctx.isSecure();
         String url = secure ? link.getSecureUrl() : link.getUrl();

         return Links.forHtml().buildLink(url, model.getDynamicAttributes(), model.getBodyContent());
      }
   };

   private LinkTagRenderType m_renderType;

   private LinkTagRenderer(LinkTagRenderType renderType) {
      m_renderType = renderType;
   }

   public LinkTagRenderer getRegisterInstance() {
      return this;
   }

   public String getRegisterKey() {
      return LinkTag.class.getName() + ":" + m_renderType.getName();
   }

   @Override
   public Class<? super LinkTagRenderer> getRegisterType() {
      return IResourceTagRenderer.class;
   }

   @Override
   public IResourceTagRenderType getType() {
      return m_renderType;
   }
}