package org.unidal.webres.resource.img;

import java.net.URL;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.SystemResourceType;
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

public class SharedImageResolver implements IResourceResolver<IImageRef, IImage>,
      IResourceRegisterable<SharedImageResolver> {
   private static final String SHARED_RESOURCE_BASE = "META-INF/resources";

   private IResourceUrlBuilder<IImage> m_urlBuilder;

   private IResourceUrlBuilder<IImage> m_dataUriBuilder;

   protected URL getImageURL(IResourceContext ctx, IResourceUrn urn) {
      URL imgUrl = ResourceResolvings.fromJar().resolve(ctx, urn, SHARED_RESOURCE_BASE);

      if (imgUrl == null) {
         throw new ResourceException(String.format("Image(%s) not found in any jars or classpath!", imgUrl));
      }

      return imgUrl;
   }

   @Override
   public INamespace getNamespace() {
      return ImageNamespace.SHARED;
   }

   @Override
   public SharedImageResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Image.Shared;
   }

   @Override
   public Class<? super SharedImageResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Image;
   }

   @Override
   public SharedImage resolve(IImageRef ref, IResourceContext ctx) throws ResourceException {
      URL imgUrl = getImageURL(ctx, ref.getUrn());
      SharedImage image = new SharedImage(ctx, ref, imgUrl);

      image.validate();

      image.setDataUriBuilder(m_dataUriBuilder);
      image.setUrlBuilder(m_urlBuilder);
      return image;
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Image.DataUriBuilder)
   public void setDataUriBuilder(IResourceUrlBuilder<? extends IImage> dataUriBuilder) {
      m_dataUriBuilder = (IResourceUrlBuilder<IImage>) dataUriBuilder;
   }

   @SuppressWarnings("unchecked")
   @ResourceAttribute(ResourceConstant.Image.Shared)
   public void setUrlBuilder(IResourceUrlBuilder<? extends IImage> urlBuilder) {
      m_urlBuilder = (IResourceUrlBuilder<IImage>) urlBuilder;
   }
}