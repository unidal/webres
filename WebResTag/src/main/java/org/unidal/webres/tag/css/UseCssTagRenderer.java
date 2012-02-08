package org.unidal.webres.tag.css;

import org.unidal.webres.resource.ResourceOutputType;
import org.unidal.webres.resource.api.ICss;
import org.unidal.webres.resource.api.IResourceOutputType;
import org.unidal.webres.resource.css.Styles;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.tag.ITagLookupManager;
import org.unidal.webres.tag.resource.IResourceTagRenderType;
import org.unidal.webres.tag.resource.IResourceTagRenderer;

public enum UseCssTagRenderer implements IResourceTagRenderer<UseCssTag, ICss>, IResourceRegisterable<UseCssTagRenderer> {
   INLINE(UseCssTagRenderType.INLINE) {
      @Override
      public String render(UseCssTag tag, ICss css) {
         UseCssTagModel model = tag.getModel();

         return Styles.forHtml().buildStyle(css.getContent(), model.getDynamicAttributes());
      }
   },

   EXTERNALIZED(UseCssTagRenderType.EXTERNALIZED) {
      @Override
      public String render(UseCssTag tag, ICss css) {
         ITagLookupManager manager = tag.getEnv().getLookupManager();
         IResourceContext ctx = manager.lookupComponent(IResourceContext.class);
         UseCssTagModel model = tag.getModel();
         boolean secure = model.getSecure() != null ? model.getSecure().booleanValue() : ctx.isSecure();
         String url = secure ? css.getSecureUrl() : css.getUrl();

         if (url != null) {
            IResourceOutputType outputType = manager.lookupComponent(IResourceOutputType.class);

            if (outputType != ResourceOutputType.HTML && outputType != ResourceOutputType.XHTML) {
               throw new RuntimeException(String.format("Unsupported ResourceOutputType(%s) for css link tag.", outputType));
            }

            return Styles.forHtml().buildLink(url, model.getDynamicAttributes(), outputType == ResourceOutputType.XHTML);
         } else {
            // fall back to inline text
            return Styles.forHtml().buildStyle(css.getContent(), model.getDynamicAttributes());
         }
      }
   };

   private UseCssTagRenderType m_renderType;

   private UseCssTagRenderer(UseCssTagRenderType renderType) {
      m_renderType = renderType;
   }

   public UseCssTagRenderer getRegisterInstance() {
      return this;
   }

   public String getRegisterKey() {
      return UseCssTag.class.getName() + ":" + m_renderType.getName();
   }

   @Override
   public Class<? super UseCssTagRenderer> getRegisterType() {
      return IResourceTagRenderer.class;
   }

   @Override
   public IResourceTagRenderType getType() {
      return m_renderType;
   }
}