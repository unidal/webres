package org.unidal.webres.tag.js;

import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.js.Scripts;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.tag.ITagLookupManager;
import org.unidal.webres.tag.resource.IResourceTagRenderType;
import org.unidal.webres.tag.resource.IResourceTagRenderer;

public enum UseJsTagRenderer implements IResourceTagRenderer<UseJsTag, IJs>, IResourceRegisterable<UseJsTagRenderer> {
   INLINE(UseJsTagRenderType.INLINE) {
      @Override
      public String render(UseJsTag tag, IJs js) {
         UseJsTagModel model = tag.getModel();

         return Scripts.forHtml().buildInlineScript(js.getContent(), model.getDynamicAttributes());
      }
   },

   EXTERNALIZED(UseJsTagRenderType.EXTERNALIZED) {
      @Override
      public String render(UseJsTag tag, IJs js) {
         ITagLookupManager manager = tag.getEnv().getLookupManager();
         IResourceContext ctx = manager.lookupComponent(IResourceContext.class);
         UseJsTagModel model = tag.getModel();
         boolean secure = model.getSecure() != null ? model.getSecure().booleanValue() : ctx.isSecure();
         String url = secure ? js.getSecureUrl() : js.getUrl();

         if (url != null) {
            return Scripts.forHtml().buildScript(url, model.getDynamicAttributes());
         } else {
            // fall back to inline text
            return Scripts.forHtml().buildInlineScript(js.getContent(), model.getDynamicAttributes());
         }
      }
   };

   private UseJsTagRenderType m_renderType;

   private UseJsTagRenderer(UseJsTagRenderType renderType) {
      m_renderType = renderType;
   }

   public UseJsTagRenderer getRegisterInstance() {
      return this;
   }

   public String getRegisterKey() {
      return UseJsTag.class.getName() + ":" + m_renderType.getName();
   }

   @Override
   public Class<? super UseJsTagRenderer> getRegisterType() {
      return IResourceTagRenderer.class;
   }

   @Override
   public IResourceTagRenderType getType() {
      return m_renderType;
   }
}