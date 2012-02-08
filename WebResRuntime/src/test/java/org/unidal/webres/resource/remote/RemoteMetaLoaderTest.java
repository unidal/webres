package org.unidal.webres.resource.remote;

import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.img.PicsImageResolver.PicsImageMeta;
import org.unidal.webres.resource.img.PicsImageResolver.PicsImageMetaBuilder;
import org.unidal.webres.resource.link.PagesLinkResolver.PagesLinkMeta;
import org.unidal.webres.resource.link.PagesLinkResolver.PagesLinkMetaBuilder;
import org.unidal.webres.resource.support.ResourceTestSupport;

public class RemoteMetaLoaderTest extends ResourceTestSupport {
   @BeforeClass
   public static void beforeClass() throws Exception {
      ResourceTestSupport.setup(new RemoteMetaLoaderTest());
   }

   @Test
   public void testLoadPages() throws IOException {
      RemoteMetaLoader<PagesLinkMeta> loader = new RemoteMetaLoader<PagesLinkMeta>(PagesLinkMetaBuilder.INSTANCE,
            "links");
      Map<String, PagesLinkMeta> metas = loader.load("/half/meta.0.txt");

      Assert.assertEquals(247, metas.size());

      for (PagesLinkMeta meta : metas.values()) {
         String pathInfo = meta.getUrn().getPathInfo();

         Assert.assertNotNull("uri of pages(" + pathInfo + ") is null.", meta.getUrn().getPathInfo());
         Assert.assertNotNull("pageid of pages(" + pathInfo + ") is null.", meta.getPageId());
      }
   }

   @Test
   public void testLoadPics() throws IOException {
      RemoteMetaLoader<PicsImageMeta> loader = new RemoteMetaLoader<PicsImageMeta>(PicsImageMetaBuilder.INSTANCE,
            "pics");
      Map<String, PicsImageMeta> metas = loader.load("/half/meta.0.txt");

      Assert.assertEquals(1446, metas.size());

      for (PicsImageMeta meta : metas.values()) {
         String pathInfo = meta.getUrn().getPathInfo();

         Assert.assertTrue("width of pics(" + pathInfo + ") is 0.", meta.getWidth() > 0);
         Assert.assertTrue("height of pics(" + pathInfo + ") is 0.", meta.getHeight() > 0);
      }
   }

   @Test
   public void testPages() throws IOException {
      RemoteMetaProvider<PagesLinkMeta> provider = new RemoteMetaProvider<PagesLinkMeta>(PagesLinkMetaBuilder.INSTANCE,
            "links");
      PagesLinkMeta meta = provider.getMeta("/half/faq.html");

      Assert.assertEquals("/half/faq.html", meta.getUrn().getPathInfo());
      Assert.assertEquals("466827", meta.getPageId());
   }

   @Test
   public void testPics() throws IOException {
      RemoteMetaProvider<PicsImageMeta> provider = new RemoteMetaProvider<PicsImageMeta>(PicsImageMetaBuilder.INSTANCE,
            "pics");
      PicsImageMeta meta = provider.getMeta("/half/btnSearch.gif");

      Assert.assertEquals(72, meta.getWidth());
      Assert.assertEquals(37, meta.getHeight());
   }

   @Test
   public void testPicsToPath() {
      PicsImageMetaBuilder builder = PicsImageMetaBuilder.INSTANCE;

      Assert.assertEquals("/a", builder.toPath("a"));
      Assert.assertEquals("/a/b", builder.toPath("a.b"));
      Assert.assertEquals("/a/b.c", builder.toPath("a.b_c"));
      Assert.assertEquals("/a/b/c.d", builder.toPath("a.b.c_d"));
      Assert.assertEquals("/a/b-c.d", builder.toPath("a.b__c_d"));
      Assert.assertEquals("/a/b-c-d", builder.toPath("a.b__c__d"));
      Assert.assertEquals("/a/b-c-d.e", builder.toPath("a.b__c__d_e"));
   }
}
