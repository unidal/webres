package org.unidal.webres.resource.img;

import java.io.File;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.annotation.ImageBase;
import org.unidal.webres.resource.annotation.WarRoot;
import org.unidal.webres.resource.api.IImage;
import org.unidal.webres.resource.api.IImageRef;
import org.unidal.webres.resource.api.INamespace;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.helper.ResourceResolvings;
import org.unidal.webres.resource.injection.ResourceAttribute;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceResolver;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class LocalImageResolver implements IResourceResolver<IImageRef, IImage>,
      IResourceRegisterable<LocalImageResolver> {
   private File m_warRoot;

   private String m_imageBase;

   private boolean m_validate;

   private IResourceUrlBuilder<IImage> m_urlBuilder;

   private IResourceUrlBuilder<IImage> m_dataUriBuilder;

   protected File getImageFile(IResourceContext ctx, IResourceUrn urn, File base) {
      File imgFile = ResourceResolvings.fromDir().resolve(ctx, urn, base);

      if (!imgFile.isFile()) {
         throw new ResourceException(String.format("Image(%s) not found!", imgFile));
      }

      return imgFile;
   }

   @Override
   public INamespace getNamespace() {
      return ImageNamespace.LOCAL;
   }

   @Override
   public LocalImageResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Image.Local;
   }

   @Override
   public Class<? super LocalImageResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Image;
   }

   @Override
   public LocalImage resolve(IImageRef ref, IResourceContext ctx) throws ResourceException {
      File base = m_imageBase != null ? new File(m_warRoot, m_imageBase) : m_warRoot;
      File imgFile = getImageFile(ctx, ref.getUrn(), base);
      LocalImage image = new LocalImage(ctx, ref, imgFile);

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

   @ImageBase
   public void setImageBase(String imageBase) {
      m_imageBase = imageBase;
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Image.Local)
   public void setUrlBuilder(IResourceUrlBuilder<? extends IImage> urlBuilder) {
      m_urlBuilder = (IResourceUrlBuilder<IImage>) urlBuilder;
   }

   @ResourceAttribute(value = ResourceConstant.Image.Validation, optional = true)
   public void setValidate(boolean validate) {
      m_validate = validate;
   }

   @WarRoot
   public void setWarRoot(File warRoot) {
      m_warRoot = warRoot;
   }
}