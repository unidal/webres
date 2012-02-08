package org.unidal.webres.resource.link;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.ResourceContext;
import org.unidal.webres.resource.api.ILink;
import org.unidal.webres.resource.api.ILinkRef;
import org.unidal.webres.resource.support.ResourceTestSupport;

public class LinkTest extends ResourceTestSupport {
   @BeforeClass
   public static void beforeClass() throws Exception {
      ResourceTestSupport.setup(new LinkTest());
   }

   @Test
   public void testPagesLink1() throws Exception {
      ILinkRef linkRef = LinkFactory.forRef().createPagesRef("/index.html");
      ILink link = linkRef.resolve(new ResourceContext(getRegistry()));

      Assert.assertEquals("http://pages.ebay.com/index.html", link.getUrl());
      Assert.assertEquals("https://pages.ebay.com/index.html", link.getSecureUrl());
   }

   @Test
   public void testPagesLink2() throws Exception {
      ILinkRef linkRef = LinkFactory.forRef().createPagesRef("/half/faq.html");
      ILink link = linkRef.resolve(new ResourceContext(getRegistry()));

      Assert.assertEquals("http://pages.ebay.com/half/faq.html", link.getUrl());
      Assert.assertEquals("https://pages.ebay.com/half/faq.html", link.getSecureUrl());
   }
}
