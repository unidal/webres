package org.unidal.webres.tag.img;

import org.unidal.webres.resource.api.IImage;
import org.unidal.webres.resource.api.IResourceOutputType;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.tag.ITagLookupManager;
import org.unidal.webres.tag.resource.IResourceTagRenderType;
import org.unidal.webres.tag.resource.IResourceTagRenderer;

public enum ImageTagRenderer implements IResourceTagRenderer<ImageTag, IImage>, IResourceRegisterable<ImageTagRenderer> {
   //<img src="/half/eBayLogo.gif" width="50" height="40" alt="Red dot" />
   HTML(ImageTagRenderType.HTML) {
      @Override
      public String render(ImageTag tag, IImage image) {
         ITagLookupManager manager = tag.getEnv().getLookupManager();
         IResourceContext ctx = manager.lookupComponent(IResourceContext.class);
         IResourceOutputType outputType = manager.lookupComponent(IResourceOutputType.class);
         ImageTagModel model = tag.getModel();
         boolean secure = model.getSecure() != null ? model.getSecure().booleanValue() : ctx.isSecure();
         String url = secure ? image.getSecureUrl() : image.getUrl();

         // TODO for IE9+ and SVG
         // <object data="test.svg" type="image/svg+xml" width="500" height="500"></object>
         return Images.forHtml().buildImage(image, model.getDynamicAttributes(), url, outputType);
      }
   },

   //<img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUA
   //   AAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO
   //   9TXL0Y4OHwAAAABJRU5ErkJggg==" width="50" height="40" alt="Red dot" />
   DATA_URI(ImageTagRenderType.DATA_URI) {
      @Override
      public String render(ImageTag tag, IImage image) {
         ITagLookupManager manager = tag.getEnv().getLookupManager();
         IResourceOutputType outputType = manager.lookupComponent(IResourceOutputType.class);
         String dataUri = image.getDataUri();

         return Images.forHtml().buildImage(image, tag.getModel().getDynamicAttributes(), dataUri, outputType);
      }
   };

   private ImageTagRenderType m_renderType;

   private ImageTagRenderer(ImageTagRenderType renderType) {
      m_renderType = renderType;
   }

   public ImageTagRenderer getRegisterInstance() {
      return this;
   }

   public String getRegisterKey() {
      return ImageTag.class.getName() + ":" + m_renderType.getName();
   }

   @Override
   public Class<? super ImageTagRenderer> getRegisterType() {
      return IResourceTagRenderer.class;
   }

   @Override
   public IResourceTagRenderType getType() {
      return m_renderType;
   }
}