package org.unidal.webres.resource.img;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.ResourceUrn;
import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.api.IImage;
import org.unidal.webres.resource.api.IImageMeta;
import org.unidal.webres.resource.api.IImageRef;
import org.unidal.webres.resource.api.INamespace;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.injection.ResourceAttribute;
import org.unidal.webres.resource.remote.IRemoteMetaBuilder;
import org.unidal.webres.resource.remote.IRemoteMetaBuilder.IRemoteMetaKeyBuilder;
import org.unidal.webres.resource.remote.IRemoteMetaProvider;
import org.unidal.webres.resource.remote.RemoteMetaProvider;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.IResourceResolver;
import org.unidal.webres.resource.spi.IResourceUrlBuilder;

public class PicsImageResolver implements IResourceResolver<IImageRef, IImage>,
      IResourceRegisterable<PicsImageResolver> {
   private IResourceUrlBuilder<IImage> m_urlBuilder;

   private IResourceUrlBuilder<IImage> m_dataUriBuilder;

   private IRemoteMetaProvider<PicsImageMeta> m_provider = new RemoteMetaProvider<PicsImageMeta>(
         PicsImageMetaBuilder.INSTANCE, "pics");

   protected String getFallbackPath(String path) {
      int pos1 = path.lastIndexOf('/');
      int pos2 = path.lastIndexOf('_');

      if (pos2 > pos1) {
         return path.substring(0, pos2) + '.' + path.substring(pos2 + 1);
      } else {
         return null;
      }
   }

   protected PicsImageMeta getImageMeta(IResourceContext ctx, IResourceUrn urn) {
      String path = urn.getResourceId();

      try {
         PicsImageMeta meta = m_provider.getMeta(path);

         if (meta == null) {
            String pathInfo = getFallbackPath(path);

            if (pathInfo != null) {
               meta = m_provider.getMeta(pathInfo);

               if (meta != null) {
                  urn.setPathInfo(pathInfo);
               }
            }
         }

         return meta;
      } catch (IOException e) {
         throw new RuntimeException(String.format("Unable to find image(%s)!", path), e);
      }
   }

   @Override
   public INamespace getNamespace() {
      return ImageNamespace.PICS;
   }

   @Override
   public PicsImageResolver getRegisterInstance() {
      return this;
   }

   @Override
   public String getRegisterKey() {
      return ResourceConstant.Image.Pics;
   }

   @Override
   public Class<? super PicsImageResolver> getRegisterType() {
      return IResourceResolver.class;
   }

   @Override
   public IResourceType getResourceType() {
      return SystemResourceType.Image;
   }

   @Override
   public PicsImage resolve(IImageRef ref, IResourceContext ctx) throws ResourceException {
      IResourceUrn urn = ref.getUrn();
      PicsImageMeta meta = getImageMeta(ctx, urn);

      if (meta == null) {
         throw new RuntimeException(String.format("No pics image(%s) was found!", urn));
      }

      PicsImage image = new PicsImage(ctx, meta);

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
   @ResourceAttribute(ResourceConstant.Image.Pics)
   public void setUrlBuilder(IResourceUrlBuilder<? extends IImage> urlBuilder) {
      m_urlBuilder = (IResourceUrlBuilder<IImage>) urlBuilder;
   }

   public static class PicsImageMeta implements IImageMeta, IRemoteMetaKeyBuilder {
      private int m_width;

      private int m_height;

      private IResourceUrn m_urn;

      public PicsImageMeta(String path) {
         m_urn = new ResourceUrn(SystemResourceType.Image.getName(), ImageNamespace.PICS.getName(), path);
      }

      public int getHeight() {
         return m_height;
      }

      @Override
      public String getKey(String name) {
         int start = name.lastIndexOf('/');

         return m_urn.getPathInfo().substring(start + 1);
      }

      @Override
      public long getLastModified() {
         return -1;
      }

      @Override
      public long getLength() {
         return -1;
      }

      @Override
      public String getMimeType() {
         return guessMimeType(m_urn.getPathInfo());
      }

      public String getPath() {
         return m_urn.getPathInfo();
      }

      @Override
      public IResourceType getResourceType() {
         return SystemResourceType.Image;
      }

      @Override
      public IResourceUrn getUrn() {
         return m_urn;
      }

      public int getWidth() {
         return m_width;
      }

      protected String guessMimeType(String pathInfo) {
         int pos = pathInfo.lastIndexOf('.');
         String type = pathInfo.substring(pos + 1);

         if ("jpg".equals(type)) {
            return "image/jpeg";
         } else {
            return "image/" + type;
         }
      }

      public void setHeight(int height) {
         m_height = height;
      }

      public void setWidth(int width) {
         m_width = width;
      }

      @Override
      public String toString() {
         return String.format("%s[path=%s, width=%s, height=%s]", getClass().getSimpleName(), m_urn.getPathInfo(),
               m_width, m_height);
      }
   }

   public static enum PicsImageMetaBuilder implements IRemoteMetaBuilder<PicsImageMeta> {
      INSTANCE;

      @Override
      public boolean build(Map<String, PicsImageMeta> metas, String path, List<String> items) {
         if (items.size() >= 3) {
            int index = 0;
            String id = items.get(index++);
            int width = Integer.parseInt(items.get(index++));
            int height = Integer.parseInt(items.get(index++));
            PicsImageMeta meta = new PicsImageMeta(toPath(id));

            meta.setWidth(width);
            meta.setHeight(height);

            metas.put(meta.getKey(path), meta);
            return true;
         }

         return false;
      }

      public String toPath(String id) {
         int len = id.length();
         StringBuilder sb = new StringBuilder(len + 1);
         int pos = -1;

         sb.append('/');

         for (int i = 0; i < len; i++) {
            char ch = id.charAt(i);

            if (ch == '.') {
               sb.append('/');
            } else if (ch == '_') {
               if (pos == i - 1) {
                  sb.setLength(sb.length() - 1);
                  sb.append('-');
                  pos = -1;
               } else {
                  pos = i;
                  sb.append(ch);
               }
            } else {
               sb.append(ch);
            }
         }

         if (pos > 0) {
            sb.setCharAt(sb.length() + pos - len, '.');
         }

         return sb.toString();
      }
   }
}