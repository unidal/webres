package org.unidal.webres.tag.img;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IImage;
import org.unidal.webres.resource.api.IImageRef;
import org.unidal.webres.resource.model.Models;
import org.unidal.webres.resource.model.entity.Page;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.tag.ITagEnv;
import org.unidal.webres.tag.ITagLookupManager;
import org.unidal.webres.tag.resource.ResourceTagSupport;

public class ImageTag extends ResourceTagSupport<ImageTagModel, IImageRef, IImage> {
   public ImageTag() {
      super(new ImageTagModel());
   }

   @Override
   public IImageRef build() {
      Object value = getModel().getValue();

      // <res:img value='${res.img.local.half.eBayLogo_gif}'/>}
      return getResourceRef(IImageRef.class, value);
   }

   @Override
   protected String getRenderType(IImage image) {
      ImageTagModel model = getModel();
      String renderType = model.getRenderType();

      // to enable profile
      if (renderType == null && image.getMeta() != null) {
         String urn = image.getMeta().getUrn().toString();

         if (isDataUriEnabled(urn)) {
            return ImageTagRenderType.DATA_URI.getName();
         }
      }

      // fall back to default one
      if (renderType == null) {
         renderType = model.getDefaultRenderType().getName();
      }

      return renderType;
   }

   private boolean isDataUriEnabled(String urn) {
      ITagEnv env = getEnv();
      ITagLookupManager manager = env.getLookupManager();
      ResourceRuntimeContext ctx = manager.lookupComponent(ResourceRuntimeContext.class);
      Page page = ctx.getPage(SystemResourceType.Image);

      return Models.forImage().isDateUriEnabled(page, urn);
   }
}
