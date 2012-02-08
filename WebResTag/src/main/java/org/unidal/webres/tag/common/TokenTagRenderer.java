package org.unidal.webres.tag.common;

import org.unidal.webres.resource.dummy.IDummyResource;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.tag.resource.IResourceTagRenderType;
import org.unidal.webres.tag.resource.IResourceTagRenderer;
import org.unidal.webres.tag.resource.ResourceTagRenderType;

public enum TokenTagRenderer implements IResourceTagRenderer<TokenTag, IDummyResource>,
      IResourceRegisterable<TokenTagRenderer> {
   DEFAULT(ResourceTagRenderType.DEFAULT) {
      @Override
      public String render(TokenTag tag, IDummyResource resource) {
         String token = ResourceRuntimeContext.ctx().prepareAjaxToken(tag.getModel().getType());

         return token;
      }
   };

   private IResourceTagRenderType m_renderType;

   private TokenTagRenderer(IResourceTagRenderType renderType) {
      m_renderType = renderType;
   }

   public TokenTagRenderer getRegisterInstance() {
      return this;
   }

   public String getRegisterKey() {
      return TokenTag.class.getName() + ":" + m_renderType.getName();
   }

   @Override
   public Class<? super TokenTagRenderer> getRegisterType() {
      return IResourceTagRenderer.class;
   }

   @Override
   public IResourceTagRenderType getType() {
      return m_renderType;
   }
}