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

public enum CssSlotTagRenderer implements IResourceTagRenderer<CssSlotTag, ICss>, IResourceRegisterable<CssSlotTagRenderer> {
   INLINE(CssSlotTagRenderType.INLINE) {
      @Override
      public String render(CssSlotTag tag, ICss css) {
         CssSlotTagModel model = tag.getModel();

         return Styles.forHtml().buildStyle(css.getContent(), model.getDynamicAttributes());
      }
   },

   EXTERNALIZED(CssSlotTagRenderType.EXTERNALIZED) {
      @Override
      public String render(CssSlotTag tag, ICss css) {
         ITagLookupManager manager = tag.getEnv().getLookupManager();
         IResourceContext ctx = manager.lookupComponent(IResourceContext.class);
         CssSlotTagModel model = tag.getModel();
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

   private CssSlotTagRenderType m_renderType;

   private CssSlotTagRenderer(CssSlotTagRenderType renderType) {
      m_renderType = renderType;
   }

   public CssSlotTagRenderer getRegisterInstance() {
      return this;
   }

   public String getRegisterKey() {
      return CssSlotTag.class.getName() + ":" + m_renderType.getName();
   }

   @Override
   public Class<? super CssSlotTagRenderer> getRegisterType() {
      return IResourceTagRenderer.class;
   }

   @Override
   public IResourceTagRenderType getType() {
      return m_renderType;
   }
}