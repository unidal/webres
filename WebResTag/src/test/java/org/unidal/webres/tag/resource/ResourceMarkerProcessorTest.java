package org.unidal.webres.tag.resource;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.spi.IResourceContainer;
import org.unidal.webres.resource.spi.IResourceDeferRenderer;
import org.unidal.webres.tag.support.ResourceTagTestSupport;

public class ResourceMarkerProcessorTest extends ResourceTagTestSupport {
   @BeforeClass
   public static void beforeClass() throws Exception {
      ResourceTagTestSupport.setup(new ResourceMarkerProcessorTest());
   }

   @Test
   public void test() throws IOException {
      IResourceContainer container = getRegistry().getContainer();
      IResourceMarkerProcessor processor = container.getAttribute(IResourceMarkerProcessor.class);

      Assert.assertNotNull("No marker processor registered yet!", processor);

      StringBuilder sb = new StringBuilder(256);

      sb.append("a${MARKER,first}b${MARKER,second}c");

      processor.process(sb);

      Assert.assertEquals("a${MARKER,first}b${MARKER,second}c", sb.toString());

      container.setAttribute(IResourceDeferRenderer.class, "second", new MockRenderer("SECOND"));
      processor.process(sb);

      Assert.assertEquals("a${MARKER,first}bSECONDc", sb.toString());

      container.setAttribute(IResourceDeferRenderer.class, "first", new MockRenderer("FIRST"));
      processor.process(sb);

      Assert.assertEquals("aFIRSTbSECONDc", sb.toString());
   }

   static class MockRenderer implements IResourceDeferRenderer {
      private String m_result;

      public MockRenderer(String result) {
         m_result = result;
      }

      @Override
      public String deferRender() {
         return m_result;
      }
   }
}
