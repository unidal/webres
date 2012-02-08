package org.unidal.webres.resource.img;

import java.io.File;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IImage;
import org.unidal.webres.resource.api.IImageRef;
import org.unidal.webres.resource.api.INamespace;
import org.unidal.webres.resource.api.IResourcePermutation;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.injection.ResourceAttribute;
import org.unidal.webres.resource.runtime.ResourceRuntime;
import org.unidal.webres.resource.runtime.ResourceRuntimeConfig;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceResolver;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class WarImageResolver implements IResourceResolver<IImageRef, IImage>, IResourceRegisterable<WarImageResolver> {
   private File m_warRoot;

   private String m_imageBase;

   private boolean m_validate;

   private IResourceUrlBuilder<IImage> m_urlBuilder;

   private IResourceUrlBuilder<IImage> m_dataUriBuilder;

   protected String getFallbackPath(String path) {
      int pos1 = path.lastIndexOf('/');
      int pos2 = path.lastIndexOf('_');

      if (pos2 > pos1) {
         return path.substring(0, pos2) + '.' + path.substring(pos2 + 1);
      } else {
         return null;
      }
   }

   protected File getImageFile(IResourceContext ctx, IResourceUrn urn, File base) {
      IResourcePermutation permutation = ctx.getPermutation();
      String path = urn.getPathInfo();
      File imgFile;

      if (permutation != null) {
         String external = permutation.toExternal();

         imgFile = new File(new File(base, external), path);

         if (!imgFile.exists()) { // fail back to no permutation
            imgFile = new File(base, path);
            ctx.setFallbackPermutation(true);
         }
      } else {
         imgFile = new File(base, path);
      }

      if (!imgFile.isFile()) {
         String pathInfo = getFallbackPath(path);

         if (pathInfo != null) {
            File file = new File(base, pathInfo);

            if (file.isFile()) {
               imgFile = file;
               urn.setPathInfo(pathInfo);
            }
         }
      } else {
         urn.setPathInfo(path);
      }

      if (!imgFile.isFile()) {
         throw new ResourceException(String.format("Image(%s) not found!", imgFile));
      }

      return imgFile;
   }

   @Override
   public INamespace getNamespace() {
      return ImageNamespace.WAR;
   }

   @Override
   public WarImageResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Image.War;
   }

   @Override
   public Class<? super WarImageResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Image;
   }

   @Override
   public WarImage resolve(IImageRef ref, IResourceContext ctx) throws ResourceException {
      prepare(ref.getUrn());

      File base = m_imageBase != null ? new File(m_warRoot, m_imageBase) : m_warRoot;
      File imgFile = getImageFile(ctx, ref.getUrn(), base);
      WarImage image = new WarImage(ctx, ref, imgFile);

      if (m_validate) {
         image.validate();
      }

      image.setDataUriBuilder(m_dataUriBuilder);
      image.setUrlBuilder(m_urlBuilder);
      return image;
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Image.DataUriBuilder)
   public void setDataUriBuilder(IResourceUrlBuilder<? extends IImage> dataUriBuilder) {
      m_dataUriBuilder = (IResourceUrlBuilder<IImage>) dataUriBuilder;
   }

   protected void prepare(IResourceUrn urn) {
      String path = urn.getResourceId();
      int pos = path.indexOf('/', 1);

      if (pos > 0) {
         String warName = path.substring(1, pos);
         ResourceRuntimeConfig config = ResourceRuntime.INSTANCE.findConfigByWarName(warName);

         if (config == null) {
            String contextPath = path.substring(0, pos);

            config = ResourceRuntime.INSTANCE.getConfig(contextPath);
         }

         if (config != null) {
            urn.setPathInfo(path.substring(pos));

            m_warRoot = config.getWarRoot();
            m_imageBase = config.getContainer().getAttribute(String.class, ResourceConstant.Image.Base);
         } else {
            throw new ResourceException(String.format("Image(%s) not found, please make sure war(%s) configured!", urn,
                  warName));
         }
      } else {
         throw new RuntimeException(String.format("Invalid resource urn(%s) found!", urn));
      }
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Image.War)
   public void setUrlBuilder(IResourceUrlBuilder<? extends IImage> urlBuilder) {
      m_urlBuilder = (IResourceUrlBuilder<IImage>) urlBuilder;
   }

   @ResourceAttribute(value = ResourceConstant.Image.Validation, optional = true)
   public void setValidate(boolean validate) {
      m_validate = validate;
   }
}