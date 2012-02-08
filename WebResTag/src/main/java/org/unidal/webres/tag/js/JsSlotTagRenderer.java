package org.unidal.webres.tag.js;

import org.unidal.webres.resource.api.IJs;
import org.unidal.webres.resource.api.ITemplate;
import org.unidal.webres.resource.api.ITemplateContext;
import org.unidal.webres.resource.api.ITemplateRef;
import org.unidal.webres.resource.js.Scripts;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.template.TemplateContext;
import org.unidal.webres.resource.template.TemplateFactory;
import org.unidal.webres.tag.ITagLookupManager;
import org.unidal.webres.tag.resource.IResourceTagRenderType;
import org.unidal.webres.tag.resource.IResourceTagRenderer;

public enum JsSlotTagRenderer implements IResourceTagRenderer<JsSlotTag, IJs>, IResourceRegisterable<JsSlotTagRenderer> {
   INLINE(JsSlotTagRenderType.INLINE) {
      @Override
      public String render(JsSlotTag tag, IJs js) {
         JsSlotTagModel model = tag.getModel();
         String bodyContent = model.getBodyContent();

         if (bodyContent != null && bodyContent.trim().length() > 0) {
            ITemplateRef ref = TemplateFactory.forRef().createInlineRef(bodyContent);
            IResourceContext rc = tag.getEnv().getLookupManager().lookupComponent(IResourceContext.class);
            ITemplate template = ref.resolve(rc);
            ITemplateContext ctx = new TemplateContext("this", js);

            try {
               String result = template.evaluate(ctx);

               return Scripts.forHtml().buildInlineScript(result, model.getDynamicAttributes());
            } catch (Exception e) {
               tag.getEnv().onError(tag, tag.getState(), e);
               return "";
            }
         } else {
            return Scripts.forHtml().buildInlineScript(js.getContent(), model.getDynamicAttributes());
         }
      }
   },

   EXTERNALIZED(JsSlotTagRenderType.EXTERNALIZED) {
      @Override
      public String render(JsSlotTag tag, IJs js) {
         JsSlotTagModel model = tag.getModel();
         String bodyContent = model.getBodyContent();

         if (bodyContent != null && bodyContent.trim().length() > 0) {
            ITemplateRef ref = TemplateFactory.forRef().createInlineRef(bodyContent);
            IResourceContext rc = tag.getEnv().getLookupManager().lookupComponent(IResourceContext.class);
            ITemplate template = ref.resolve(rc);
            ITemplateContext ctx = new TemplateContext("this", js);

            try {
               String result = template.evaluate(ctx);

               return Scripts.forHtml().buildInlineScript(result, model.getDynamicAttributes());
            } catch (Exception e) {
               tag.getEnv().onError(tag, tag.getState(), e);
               return "";
            }
         } else {
            ITagLookupManager manager = tag.getEnv().getLookupManager();
            IResourceContext ctx = manager.lookupComponent(IResourceContext.class);
            boolean secure = model.getSecure() != null ? model.getSecure().booleanValue() : ctx.isSecure();
            String url = secure ? js.getSecureUrl() : js.getUrl();

            if (url != null) {
               return Scripts.forHtml().buildScript(url, model.getDynamicAttributes());
            } else {
               // fall back to inline text
               return Scripts.forHtml().buildInlineScript(js.getContent(), model.getDynamicAttributes());
            }
         }
      }
   };

   private JsSlotTagRenderType m_renderType;

   private JsSlotTagRenderer(JsSlotTagRenderType renderType) {
      m_renderType = renderType;
   }

   public JsSlotTagRenderer getRegisterInstance() {
      return this;
   }

   public String getRegisterKey() {
      return JsSlotTag.class.getName() + ":" + m_renderType.getName();
   }

   @Override
   public Class<? super JsSlotTagRenderer> getRegisterType() {
      return IResourceTagRenderer.class;
   }

   @Override
   public IResourceTagRenderType getType() {
      return m_renderType;
   }
}