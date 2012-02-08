package org.unidal.webres.resource.expression;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.support.ResourceTestSupport;

public class ResourceExpressionTest extends ResourceTestSupport {
   @BeforeClass
   public static void beforeClass() throws Exception {
      ResourceTestSupport.setup(new ResourceExpressionTest().copyResourceFrom("/ResourceExpressionTest"));
   }

   @Override
   protected void configure() throws Exception {
      super.configure();

      getRegistry().register(String.class, ResourceConstant.Image.SharedUrlPrefix, "http://res.ebay.com/img");
      getRegistry().register(String.class, ResourceConstant.Image.SharedSecureUrlPrefix, "https://res.ebay.com/img");

      getRegistry().register(String.class, ResourceConstant.Js.SharedUrlPrefix, "http://res.ebay.com/js");
      getRegistry().register(String.class, ResourceConstant.Js.SharedSecureUrlPrefix, "https://res.ebay.com/js");

      getRegistry().register(String.class, ResourceConstant.Css.SharedUrlPrefix, "http://res.ebay.com/css");
      getRegistry().register(String.class, ResourceConstant.Css.SharedSecureUrlPrefix, "https://res.ebay.com/css");
   }

   protected ResourceExpressionParser getParser() {
      return new ResourceExpressionParser(getExpressionEnv());
   }

   @Test
   public void testInlineCss() {
      try {
         CssExpression any = getParser().parse("${res.css.inline.any_css}");
         
         any.get("$url");
         Assert.fail("ClassCastException should be thrown!");
      } catch (ClassCastException e) {
         // expected
      }
   }

   @Test
   public void testInlineJs() {
      try {
         JsExpression any = getParser().parse("${res.js.inline.any_js}");

         any.get("$url");
         Assert.fail("ClassCastException should be thrown!");
      } catch (ClassCastException e) {
         // expected
      }
   }

   @Test
   public void testLocalCss() {
      CssExpression ebayTime = getParser().parse("${res.css.local.ebaytime_css}");

      Assert.assertEquals("css.local:/ebaytime_css", ebayTime.evaluate().getUrn().toString());
      Assert.assertEquals("/css/ebaytime.css", ebayTime.get("$url"));
      Assert.assertEquals("/css/ebaytime.css", ebayTime.get("$secureurl"));
      Assert.assertEquals(".footer {color:#00c; font-size:12px}", ebayTime.get("$text"));
      Assert.assertEquals(36, ebayTime.get("$size"));
      Assert.assertEquals("/css/ebaytime.css", ebayTime.toString());
      Assert.assertEquals("${res.css.local.ebaytime_css}", ebayTime.toExternalForm());
   }

   @Test
   public void testLocalCss2() {
      CssExpression ebayTime = getParser().parse("${res.css.local.bitbybit.ebaytime_css}");

      Assert.assertEquals("css.local:/bitbybit/ebaytime_css", ebayTime.evaluate().getUrn().toString());
      Assert.assertEquals("/css/bitbybit/ebaytime.css", ebayTime.get("$url"));
      Assert.assertEquals("/css/bitbybit/ebaytime.css", ebayTime.get("$secureurl"));
      Assert.assertEquals("/css/bitbybit/ebaytime.css", ebayTime.toString());
      Assert.assertEquals("${res.css.local.bitbybit.ebaytime_css}", ebayTime.toExternalForm());
   }

   @Test
   public void testLocalImage() {
      ImageExpression eBayLogo = getParser().parse("${res.img.local.half.eBayLogo_gif}");

      Assert.assertEquals("img.local:/half/eBayLogo_gif", eBayLogo.evaluate().getUrn().toString());
      Assert.assertEquals(110, eBayLogo.get("$width"));
      Assert.assertEquals(45, eBayLogo.get("$height"));
      Assert.assertEquals("/img/half/eBayLogo.gif", eBayLogo.get("$url"));
      Assert.assertEquals("/img/half/eBayLogo.gif", eBayLogo.get("$secureurl"));
      Assert.assertEquals(45, eBayLogo.get("$height"));
      Assert.assertEquals(110, eBayLogo.get("$width"));
      Assert.assertEquals(2195, eBayLogo.get("$size"));
      Assert.assertEquals("image/gif", eBayLogo.get("$mimeType").toString());
      Assert.assertEquals(
            "data:image/gif;base64,R0lGODlhbgAtAO+/vQAA77+9ACbvv73vv70AAADvv73vv73vv71/77+9AABfX++/ve+/vQDvv73vv70AZmZm77+977+977+977+9MzPvv73vv73vv73vv73vv71/AAAALy/vv73vv71mAO+/ve+/vSPvv73vv73vv73vv73vv71A77+977+977+977+977+9VQBj77+977+977+977+9cwnvv73vv73vv73vv73vv73vv73vv73vv71A77+9X1/vv73vv73vv70I77+9EBDvv70HRO+/ve+/ve+/vT4pa++/ve+/vR/vv73vv73vv73vv73vv70QEO+/ve+/ve+/vQDvv73vv73fiFY877+977+977+977+977+935kAM0BA77+977+977+9b2/vv73vv73vv70A77+977+935/vv73vv73vv73vv71g77+977+9X2g6Xe+/vXBw77+9Z3vvv73vv70g77+9ABkA77+977+977+977+977+977+9dQBP77+9ICDvv71AQO+/ve+/ve+/vSHvv70EAQcAPwAsAAAAAG4ALQAABu+/ve+/ve+/vXBILBrvv73IpFLvv70LOO+/ve+/ve+/vWk577+9Rmfvv70MIu+/ve+/vVDvv73vv73vv71XY++/vWLvv73vv73vv73vv71Yfl3vv718Hm7vv73vv73vv73vv719AO+/ve+/ve+/vX10YiwC77+977+9AglhMBwcDAwtHGR3I0sjdwFCfyEhXn/vv71h77+977+977+977+9SxwyIi/vv73vv70vBgcmEGU3S3pqfH3vv73vv73vv71f77+977+977+9STLvv73vv73vv70mB8WwUe+/vUh2akNaQ3Hvv73vv73vv73vv71K77+977+977+977+977+9B++/vU8aSe+/vWUTRFpcXu+/ve+/vdO+Ri3vv73vv73vv73vv73vv71OU0bvv71W77+957q877+977+9Re+/ve+/vQxCYEhI77+977+977+977+9K0fvv71ZSe+/vU/vv70+Q++/vXDvv70m77+977+9MG0H77+9GDlx77+9XkPvv73vv70QIWEn77+9RREGBDHvv716YmvIjEkfPz3vv712BO+/vTAOJ1Xvv73bpnEIR++/vTXvv71i77+9TO+/vWQR77+977+9OHPvv70077+977+977+9HR1C77+977+977+9Ze+/vSLvv70oHu+/vQTvv70G77+9woIFG3zvv70dS++/ve+/ve+/vR0R77+977+9FCkEQwIVCTAc77+977+9wpHdge+/ve+/ve+/vXsCde+/ve+/vU8RSO+/vSBA77+977+977+9w4gJKO+/vUHvv73vv73vv71EKhzvv70qAUJIC++/ve+/vVYR77+977+977+9250zc3YoSEzvv73vv73vv70eH3Lvv70rEO+/ve+/vQ3MmVfvv73vv73vv73Er++/vXliIu+/vTbNuzTvv70VPETvv71uHe+/vQHvv73vv73vv71oXwIdJu+/ve+/ve+/vQQ877+977+9Cu+/vQfvv73vv70rVu+/ve+/vXbvv73vv73vv73vv70i77+977+9Vn4HNxjvv73vv70X77+9OEJhd2Lvv73vv73vv73vv73Qh8K777+9VQzvv70L77+977+9Su+/ve+/vXMM77+9GwlK77+977+977+9e++/vWXvv70aTT/vv71gSHfvv73vv71iEhHvv73vv71/UO+/vREBdD1QUe+/vQNp77+9ZVfvv70DRUgm77+9fd6lIu+/vREV77+977+977+9Su+/ve+/vRTGh++/ve+/vUAFBSAmKO+/vQDvv70R77+9ADnvv73vv70BL1Tvv71UMBxY77+9ISYB77+9d2Pvv70677+977+9QhEO77+9ICTvv71GUngNTjDvv70h77+9HAfvv73vv71DWWTvv73vv73vv71hIe+/vVABW0Pvv70g77+977+9KB7vv73vv70Q77+977+977+977+977+9HQkC3Z7vv73vv70m77+9QiQA77+977+9chrvv71OUl0i77+9J++/ve+/vRrvv73vv73vv70IemLvv73vv70UQe+/vU1C77+977+9zKLvv70277+9KO+/vT9ICu+/vXfHvSDvv71E77+9Je+/vWkZ77+977+9AQXvv73vv70q66y077+9UO+/ve+/ve+/ve+/vRZpIe+/ve+/vVdqKhLvv70j77+9KhPvv73vv70THe+/ve+/vVbvv71MIe+/vTrvv73vv73vv71c77+9X++/ve+/vXwiIG82Su+/vU8R77+9Cu+/ve+/vX3CnDpsKgDvv71gbBTvv70s77+977+9IRXXou+/vSfvv73vv70e77+966+q77+9W++/vQvlnpvvv70gG0LXmBJh77+977+9I++/ve+/ve+/vRLvv70FHe+/vSLvv70M77+977+9UO+/vV3vv70UW++/ve+/ve+/vSnvv70ECX0mGBzvv70x77+91KMc77+9Pu+/vQt6NTZMBAkBC++/ve+/vQAxUBnSlHPvv73vv73vv70uO++/vTnvv71XGyTvv73vv71277+9fCzvv71wU++/vTzvv70uFO+/ve+/vWnvv70bG++/vULvv70h77+9Je+/vR0NLWDvv73Rhe+/ve+/ve+/vShEYBBSCe+/ve+/ve+/vWRDO2wwNe+/vT0w77+9WGsF77+9Ze+/vXBdDx1TBDvvv70lVmljTu+/ve+/vRBrGVLvv712Le+/ve+/vXVUH++/ve+/ve+/ve+/ve+/vQlA77+9ICfvv73vv73vv70D77+977+9TCzvv71Z77+9045X77+977+9NEvvv71A77+9ZDBb77+9OR0x77+9IO+/ve+/vX1j77+9Ai8s77+977+977+96qy3TkUQADs=",
            eBayLogo.get("$datauri").toString());
      Assert.assertEquals("/img/half/eBayLogo.gif", eBayLogo.toString());
      Assert.assertEquals("${res.img.local.half.eBayLogo_gif}", eBayLogo.toExternalForm());
   }

   @Test
   public void testLocalImage2() {
      ImageExpression eBayLogo = getParser().parse("${res.img.local.eBayLogo_gif}");

      Assert.assertEquals("img.local:/eBayLogo_gif", eBayLogo.evaluate().getUrn().toString());
      Assert.assertEquals(110, eBayLogo.get("$width"));
      Assert.assertEquals(45, eBayLogo.get("$height"));
      Assert.assertEquals("/img/eBayLogo.gif", eBayLogo.get("$url"));
      Assert.assertEquals("/img/eBayLogo.gif", eBayLogo.get("$secureurl"));
      Assert.assertEquals(
            "data:image/gif;base64,R0lGODlhbgAtAO+/vQAA77+9ACbvv73vv70AAADvv73vv73vv71/77+9AABfX++/ve+/vQDvv73vv70AZmZm77+977+977+977+9MzPvv73vv73vv73vv73vv71/AAAALy/vv73vv71mAO+/ve+/vSPvv73vv73vv73vv73vv71A77+977+977+977+977+9VQBj77+977+977+977+9cwnvv73vv73vv73vv73vv73vv73vv73vv71A77+9X1/vv73vv73vv70I77+9EBDvv70HRO+/ve+/ve+/vT4pa++/ve+/vR/vv73vv73vv73vv73vv70QEO+/ve+/ve+/vQDvv73vv73fiFY877+977+977+977+977+935kAM0BA77+977+977+9b2/vv73vv73vv70A77+977+935/vv73vv73vv73vv71g77+977+9X2g6Xe+/vXBw77+9Z3vvv73vv70g77+9ABkA77+977+977+977+977+977+9dQBP77+9ICDvv71AQO+/ve+/ve+/vSHvv70EAQcAPwAsAAAAAG4ALQAABu+/ve+/ve+/vXBILBrvv73IpFLvv70LOO+/ve+/ve+/vWk577+9Rmfvv70MIu+/ve+/vVDvv73vv73vv71XY++/vWLvv73vv73vv73vv71Yfl3vv718Hm7vv73vv73vv73vv719AO+/ve+/ve+/vX10YiwC77+977+9AglhMBwcDAwtHGR3I0sjdwFCfyEhXn/vv71h77+977+977+977+9SxwyIi/vv73vv70vBgcmEGU3S3pqfH3vv73vv73vv71f77+977+977+9STLvv73vv73vv70mB8WwUe+/vUh2akNaQ3Hvv73vv73vv73vv71K77+977+977+977+977+9B++/vU8aSe+/vWUTRFpcXu+/ve+/vdO+Ri3vv73vv73vv73vv73vv71OU0bvv71W77+957q877+977+9Re+/ve+/vQxCYEhI77+977+977+977+9K0fvv71ZSe+/vU/vv70+Q++/vXDvv70m77+977+9MG0H77+9GDlx77+9XkPvv73vv70QIWEn77+9RREGBDHvv716YmvIjEkfPz3vv712BO+/vTAOJ1Xvv73bpnEIR++/vTXvv71i77+9TO+/vWQR77+977+9OHPvv70077+977+977+9HR1C77+977+977+9Ze+/vSLvv70oHu+/vQTvv70G77+9woIFG3zvv70dS++/ve+/ve+/vR0R77+977+9FCkEQwIVCTAc77+977+9wpHdge+/ve+/ve+/vXsCde+/ve+/vU8RSO+/vSBA77+977+977+9w4gJKO+/vUHvv73vv73vv71EKhzvv70qAUJIC++/ve+/vVYR77+977+977+9250zc3YoSEzvv73vv73vv70eH3Lvv70rEO+/ve+/vQ3MmVfvv73vv73vv73Er++/vXliIu+/vTbNuzTvv70VPETvv71uHe+/vQHvv73vv73vv71oXwIdJu+/ve+/ve+/vQQ877+977+9Cu+/vQfvv73vv70rVu+/ve+/vXbvv73vv73vv73vv70i77+977+9Vn4HNxjvv73vv70X77+9OEJhd2Lvv73vv73vv73vv73Qh8K777+9VQzvv70L77+977+9Su+/ve+/vXMM77+9GwlK77+977+977+9e++/vWXvv70aTT/vv71gSHfvv73vv71iEhHvv73vv71/UO+/vREBdD1QUe+/vQNp77+9ZVfvv70DRUgm77+9fd6lIu+/vREV77+977+977+9Su+/ve+/vRTGh++/ve+/vUAFBSAmKO+/vQDvv70R77+9ADnvv73vv70BL1Tvv71UMBxY77+9ISYB77+9d2Pvv70677+977+9QhEO77+9ICTvv71GUngNTjDvv70h77+9HAfvv73vv71DWWTvv73vv73vv71hIe+/vVABW0Pvv70g77+977+9KB7vv73vv70Q77+977+977+977+977+9HQkC3Z7vv73vv70m77+9QiQA77+977+9chrvv71OUl0i77+9J++/ve+/vRrvv73vv73vv70IemLvv73vv70UQe+/vU1C77+977+9zKLvv70277+9KO+/vT9ICu+/vXfHvSDvv71E77+9Je+/vWkZ77+977+9AQXvv73vv70q66y077+9UO+/ve+/ve+/ve+/vRZpIe+/ve+/vVdqKhLvv70j77+9KhPvv73vv70THe+/ve+/vVbvv71MIe+/vTrvv73vv73vv71c77+9X++/ve+/vXwiIG82Su+/vU8R77+9Cu+/ve+/vX3CnDpsKgDvv71gbBTvv70s77+977+9IRXXou+/vSfvv73vv70e77+966+q77+9W++/vQvlnpvvv70gG0LXmBJh77+977+9I++/ve+/ve+/vRLvv70FHe+/vSLvv70M77+977+9UO+/vV3vv70UW++/ve+/ve+/vSnvv70ECX0mGBzvv70x77+91KMc77+9Pu+/vQt6NTZMBAkBC++/ve+/vQAxUBnSlHPvv73vv73vv70uO++/vTnvv71XGyTvv73vv71277+9fCzvv71wU++/vTzvv70uFO+/ve+/vWnvv70bG++/vULvv70h77+9Je+/vR0NLWDvv73Rhe+/ve+/ve+/vShEYBBSCe+/ve+/ve+/vWRDO2wwNe+/vT0w77+9WGsF77+9Ze+/vXBdDx1TBDvvv70lVmljTu+/ve+/vRBrGVLvv712Le+/ve+/vXVUH++/ve+/ve+/ve+/ve+/vQlA77+9ICfvv73vv73vv70D77+977+9TCzvv71Z77+9045X77+977+9NEvvv71A77+9ZDBb77+9OR0x77+9IO+/ve+/vX1j77+9Ai8s77+977+977+96qy3TkUQADs=",
            eBayLogo.get("$datauri").toString());
      Assert.assertEquals("/img/eBayLogo.gif", eBayLogo.toString());
      Assert.assertEquals("${res.img.local.eBayLogo_gif}", eBayLogo.toExternalForm());
   }

   @Test
   public void testLocalJs() {
      JsExpression ebayTime = getParser().parse("${res.js.local.ebaytime_js}");

      Assert.assertEquals("js.local:/ebaytime_js", ebayTime.evaluate().getUrn().toString());
      Assert.assertEquals("/js/ebaytime.js", ebayTime.get("$url"));
      Assert.assertEquals("/js/ebaytime.js", ebayTime.get("$secureurl"));
      Assert.assertEquals("// local js here", ebayTime.get("$text"));
      Assert.assertEquals(16, ebayTime.get("$size"));
      Assert.assertEquals("/js/ebaytime.js", ebayTime.toString());
      Assert.assertEquals("${res.js.local.ebaytime_js}", ebayTime.toExternalForm());
   }

   @Test
   public void testLocalJs2() {
      JsExpression ebayTime = getParser().parse("${res.js.local.bitbybit.ebaytime_js}");

      Assert.assertEquals("js.local:/bitbybit/ebaytime_js", ebayTime.evaluate().getUrn().toString());
      Assert.assertEquals("/js/bitbybit/ebaytime.js", ebayTime.get("$url"));
      Assert.assertEquals("/js/bitbybit/ebaytime.js", ebayTime.get("$secureurl"));
      Assert.assertEquals("/js/bitbybit/ebaytime.js", ebayTime.toString());
      Assert.assertEquals("${res.js.local.bitbybit.ebaytime_js}", ebayTime.toExternalForm());
   }

   @Test
   public void testPagesLink() {
      LinkExpression faq = getParser().parse("${res.link.pages.index_html}");

      Assert.assertEquals("link.pages:/index_html", faq.evaluate().getUrn().toString());
      Assert.assertEquals("http://pages.ebay.com/index.html", faq.get("$url").toString());
      Assert.assertEquals("https://pages.ebay.com/index.html", faq.get("$secureurl"));
      Assert.assertEquals("http://pages.ebay.com/index.html", faq.toString());
      Assert.assertEquals("${res.link.pages.index_html}", faq.toExternalForm());
   }

   @Test
   public void testPagesLink2() {
      LinkExpression faq = getParser().parse("${res.link.pages.half.faq_html}");

      Assert.assertEquals("link.pages:/half/faq_html", faq.evaluate().getUrn().toString());
      Assert.assertEquals("http://pages.ebay.com/half/faq.html", faq.get("$url").toString());
      Assert.assertEquals("https://pages.ebay.com/half/faq.html", faq.get("$secureurl"));
      Assert.assertEquals("http://pages.ebay.com/half/faq.html", faq.toString());
      Assert.assertEquals("${res.link.pages.half.faq_html}", faq.toExternalForm());
   }

   @Test
   public void testPicsImage() {
      ImageExpression btnSearch = getParser().parse("${res.img.pics.x_gif}");

      Assert.assertEquals("img.pics:/x_gif", btnSearch.evaluate().getUrn().toString());
      Assert.assertEquals(1, btnSearch.get("$width"));
      Assert.assertEquals(1, btnSearch.get("$height"));
      Assert.assertEquals("http://pics.ebaystatic.com/aw/pics/x.gif", btnSearch.get("$url"));
      Assert.assertEquals("https://pics.ebaystatic.com/aw/pics/x.gif", btnSearch.get("$secureurl"));

      try {
         btnSearch.get("$datauri");
         Assert.fail("Data uri should not be supported by Pics Image.");
      } catch (Exception e) {
         // expected
      }
   }

   @Test
   public void testPicsImage2() {
      ImageExpression btnSearch = getParser().parse("${res.img.pics.half.btnSearch_gif}");

      Assert.assertEquals("img.pics:/half/btnSearch_gif", btnSearch.evaluate().getUrn().toString());
      Assert.assertEquals(72, btnSearch.get("$width"));
      Assert.assertEquals(37, btnSearch.get("$height"));
      Assert.assertEquals("http://pics.ebaystatic.com/aw/pics/half/btnSearch.gif", btnSearch.get("$url"));
      Assert.assertEquals("https://pics.ebaystatic.com/aw/pics/half/btnSearch.gif", btnSearch.get("$secureurl"));

      try {
         btnSearch.get("$datauri");
         Assert.fail("Data uri should not be supported by Pics Image.");
      } catch (Exception e) {
         // expected
      }
   }

   @Test
   public void testSharedCss() {
      CssExpression ebayTime = getParser().parse("${res.css.shared.ebaytime_css}");

      Assert.assertEquals("css.shared:/ebaytime_css", ebayTime.evaluate().getUrn().toString());
      Assert.assertEquals("http://res.ebay.com/css/ebaytime.css", ebayTime.get("$url").toString());
      Assert.assertEquals("https://res.ebay.com/css/ebaytime.css", ebayTime.get("$secureurl"));
      Assert.assertEquals("http://res.ebay.com/css/ebaytime.css", ebayTime.toString());
      Assert.assertEquals("${res.css.shared.ebaytime_css}", ebayTime.toExternalForm());
   }

   @Test
   public void testSharedCss2() {
      CssExpression ebayTime = getParser().parse("${res.css.shared.bitbybit.ebaytime_css}");

      Assert.assertEquals("css.shared:/bitbybit/ebaytime_css", ebayTime.evaluate().getUrn().toString());
      Assert.assertEquals("http://res.ebay.com/css/bitbybit/ebaytime.css", ebayTime.get("$url").toString());
      Assert.assertEquals("https://res.ebay.com/css/bitbybit/ebaytime.css", ebayTime.get("$secureurl"));
      Assert.assertEquals("http://res.ebay.com/css/bitbybit/ebaytime.css", ebayTime.toString());
      Assert.assertEquals("${res.css.shared.bitbybit.ebaytime_css}", ebayTime.toExternalForm());
   }

   @Test
   public void testSharedImage() {
      ImageExpression eBayLogo = getParser().parse("${res.img.shared.eBayLogo_gif}");

      Assert.assertEquals("img.shared:/eBayLogo_gif", eBayLogo.evaluate().getUrn().toString());
      Assert.assertEquals(110, eBayLogo.get("$width"));
      Assert.assertEquals(45, eBayLogo.get("$height"));
      Assert.assertEquals("http://res.ebay.com/img/eBayLogo.gif", eBayLogo.get("$url"));
      Assert.assertEquals("https://res.ebay.com/img/eBayLogo.gif", eBayLogo.get("$secureurl"));
      Assert.assertEquals(
            "data:image/gif;base64,R0lGODlhbgAtAO+/vQAA77+9ACbvv73vv70AAADvv73vv73vv71/77+9AABfX++/ve+/vQDvv73vv70AZmZm77+977+977+977+9MzPvv73vv73vv73vv73vv71/AAAALy/vv73vv71mAO+/ve+/vSPvv73vv73vv73vv73vv71A77+977+977+977+977+9VQBj77+977+977+977+9cwnvv73vv73vv73vv73vv73vv73vv73vv71A77+9X1/vv73vv73vv70I77+9EBDvv70HRO+/ve+/ve+/vT4pa++/ve+/vR/vv73vv73vv73vv73vv70QEO+/ve+/ve+/vQDvv73vv73fiFY877+977+977+977+977+935kAM0BA77+977+977+9b2/vv73vv73vv70A77+977+935/vv73vv73vv73vv71g77+977+9X2g6Xe+/vXBw77+9Z3vvv73vv70g77+9ABkA77+977+977+977+977+977+9dQBP77+9ICDvv71AQO+/ve+/ve+/vSHvv70EAQcAPwAsAAAAAG4ALQAABu+/ve+/ve+/vXBILBrvv73IpFLvv70LOO+/ve+/ve+/vWk577+9Rmfvv70MIu+/ve+/vVDvv73vv73vv71XY++/vWLvv73vv73vv73vv71Yfl3vv718Hm7vv73vv73vv73vv719AO+/ve+/ve+/vX10YiwC77+977+9AglhMBwcDAwtHGR3I0sjdwFCfyEhXn/vv71h77+977+977+977+9SxwyIi/vv73vv70vBgcmEGU3S3pqfH3vv73vv73vv71f77+977+977+9STLvv73vv73vv70mB8WwUe+/vUh2akNaQ3Hvv73vv73vv73vv71K77+977+977+977+977+9B++/vU8aSe+/vWUTRFpcXu+/ve+/vdO+Ri3vv73vv73vv73vv73vv71OU0bvv71W77+957q877+977+9Re+/ve+/vQxCYEhI77+977+977+977+9K0fvv71ZSe+/vU/vv70+Q++/vXDvv70m77+977+9MG0H77+9GDlx77+9XkPvv73vv70QIWEn77+9RREGBDHvv716YmvIjEkfPz3vv712BO+/vTAOJ1Xvv73bpnEIR++/vTXvv71i77+9TO+/vWQR77+977+9OHPvv70077+977+977+9HR1C77+977+977+9Ze+/vSLvv70oHu+/vQTvv70G77+9woIFG3zvv70dS++/ve+/ve+/vR0R77+977+9FCkEQwIVCTAc77+977+9wpHdge+/ve+/ve+/vXsCde+/ve+/vU8RSO+/vSBA77+977+977+9w4gJKO+/vUHvv73vv73vv71EKhzvv70qAUJIC++/ve+/vVYR77+977+977+9250zc3YoSEzvv73vv73vv70eH3Lvv70rEO+/ve+/vQ3MmVfvv73vv73vv73Er++/vXliIu+/vTbNuzTvv70VPETvv71uHe+/vQHvv73vv73vv71oXwIdJu+/ve+/ve+/vQQ877+977+9Cu+/vQfvv73vv70rVu+/ve+/vXbvv73vv73vv73vv70i77+977+9Vn4HNxjvv73vv70X77+9OEJhd2Lvv73vv73vv73vv73Qh8K777+9VQzvv70L77+977+9Su+/ve+/vXMM77+9GwlK77+977+977+9e++/vWXvv70aTT/vv71gSHfvv73vv71iEhHvv73vv71/UO+/vREBdD1QUe+/vQNp77+9ZVfvv70DRUgm77+9fd6lIu+/vREV77+977+977+9Su+/ve+/vRTGh++/ve+/vUAFBSAmKO+/vQDvv70R77+9ADnvv73vv70BL1Tvv71UMBxY77+9ISYB77+9d2Pvv70677+977+9QhEO77+9ICTvv71GUngNTjDvv70h77+9HAfvv73vv71DWWTvv73vv73vv71hIe+/vVABW0Pvv70g77+977+9KB7vv73vv70Q77+977+977+977+977+9HQkC3Z7vv73vv70m77+9QiQA77+977+9chrvv71OUl0i77+9J++/ve+/vRrvv73vv73vv70IemLvv73vv70UQe+/vU1C77+977+9zKLvv70277+9KO+/vT9ICu+/vXfHvSDvv71E77+9Je+/vWkZ77+977+9AQXvv73vv70q66y077+9UO+/ve+/ve+/ve+/vRZpIe+/ve+/vVdqKhLvv70j77+9KhPvv73vv70THe+/ve+/vVbvv71MIe+/vTrvv73vv73vv71c77+9X++/ve+/vXwiIG82Su+/vU8R77+9Cu+/ve+/vX3CnDpsKgDvv71gbBTvv70s77+977+9IRXXou+/vSfvv73vv70e77+966+q77+9W++/vQvlnpvvv70gG0LXmBJh77+977+9I++/ve+/ve+/vRLvv70FHe+/vSLvv70M77+977+9UO+/vV3vv70UW++/ve+/ve+/vSnvv70ECX0mGBzvv70x77+91KMc77+9Pu+/vQt6NTZMBAkBC++/ve+/vQAxUBnSlHPvv73vv73vv70uO++/vTnvv71XGyTvv73vv71277+9fCzvv71wU++/vTzvv70uFO+/ve+/vWnvv70bG++/vULvv70h77+9Je+/vR0NLWDvv73Rhe+/ve+/ve+/vShEYBBSCe+/ve+/ve+/vWRDO2wwNe+/vT0w77+9WGsF77+9Ze+/vXBdDx1TBDvvv70lVmljTu+/ve+/vRBrGVLvv712Le+/ve+/vXVUH++/ve+/ve+/ve+/ve+/vQlA77+9ICfvv73vv73vv70D77+977+9TCzvv71Z77+9045X77+977+9NEvvv71A77+9ZDBb77+9OR0x77+9IO+/ve+/vX1j77+9Ai8s77+977+977+96qy3TkUQADs=",
            eBayLogo.get("$datauri").toString());
   }

   @Test
   public void testSharedImage2() {
      ImageExpression eBayLogo = getParser().parse("${res.img.shared.half.eBayLogo_gif}");

      Assert.assertEquals("img.shared:/half/eBayLogo_gif", eBayLogo.evaluate().getUrn().toString());
      Assert.assertEquals(110, eBayLogo.get("$width"));
      Assert.assertEquals(45, eBayLogo.get("$height"));
      Assert.assertEquals("http://res.ebay.com/img/half/eBayLogo.gif", eBayLogo.get("$url"));
      Assert.assertEquals("https://res.ebay.com/img/half/eBayLogo.gif", eBayLogo.get("$secureurl"));
      Assert.assertEquals(
            "data:image/gif;base64,R0lGODlhbgAtAO+/vQAA77+9ACbvv73vv70AAADvv73vv73vv71/77+9AABfX++/ve+/vQDvv73vv70AZmZm77+977+977+977+9MzPvv73vv73vv73vv73vv71/AAAALy/vv73vv71mAO+/ve+/vSPvv73vv73vv73vv73vv71A77+977+977+977+977+9VQBj77+977+977+977+9cwnvv73vv73vv73vv73vv73vv73vv73vv71A77+9X1/vv73vv73vv70I77+9EBDvv70HRO+/ve+/ve+/vT4pa++/ve+/vR/vv73vv73vv73vv73vv70QEO+/ve+/ve+/vQDvv73vv73fiFY877+977+977+977+977+935kAM0BA77+977+977+9b2/vv73vv73vv70A77+977+935/vv73vv73vv73vv71g77+977+9X2g6Xe+/vXBw77+9Z3vvv73vv70g77+9ABkA77+977+977+977+977+977+9dQBP77+9ICDvv71AQO+/ve+/ve+/vSHvv70EAQcAPwAsAAAAAG4ALQAABu+/ve+/ve+/vXBILBrvv73IpFLvv70LOO+/ve+/ve+/vWk577+9Rmfvv70MIu+/ve+/vVDvv73vv73vv71XY++/vWLvv73vv73vv73vv71Yfl3vv718Hm7vv73vv73vv73vv719AO+/ve+/ve+/vX10YiwC77+977+9AglhMBwcDAwtHGR3I0sjdwFCfyEhXn/vv71h77+977+977+977+9SxwyIi/vv73vv70vBgcmEGU3S3pqfH3vv73vv73vv71f77+977+977+9STLvv73vv73vv70mB8WwUe+/vUh2akNaQ3Hvv73vv73vv73vv71K77+977+977+977+977+9B++/vU8aSe+/vWUTRFpcXu+/ve+/vdO+Ri3vv73vv73vv73vv73vv71OU0bvv71W77+957q877+977+9Re+/ve+/vQxCYEhI77+977+977+977+9K0fvv71ZSe+/vU/vv70+Q++/vXDvv70m77+977+9MG0H77+9GDlx77+9XkPvv73vv70QIWEn77+9RREGBDHvv716YmvIjEkfPz3vv712BO+/vTAOJ1Xvv73bpnEIR++/vTXvv71i77+9TO+/vWQR77+977+9OHPvv70077+977+977+9HR1C77+977+977+9Ze+/vSLvv70oHu+/vQTvv70G77+9woIFG3zvv70dS++/ve+/ve+/vR0R77+977+9FCkEQwIVCTAc77+977+9wpHdge+/ve+/ve+/vXsCde+/ve+/vU8RSO+/vSBA77+977+977+9w4gJKO+/vUHvv73vv73vv71EKhzvv70qAUJIC++/ve+/vVYR77+977+977+9250zc3YoSEzvv73vv73vv70eH3Lvv70rEO+/ve+/vQ3MmVfvv73vv73vv73Er++/vXliIu+/vTbNuzTvv70VPETvv71uHe+/vQHvv73vv73vv71oXwIdJu+/ve+/ve+/vQQ877+977+9Cu+/vQfvv73vv70rVu+/ve+/vXbvv73vv73vv73vv70i77+977+9Vn4HNxjvv73vv70X77+9OEJhd2Lvv73vv73vv73vv73Qh8K777+9VQzvv70L77+977+9Su+/ve+/vXMM77+9GwlK77+977+977+9e++/vWXvv70aTT/vv71gSHfvv73vv71iEhHvv73vv71/UO+/vREBdD1QUe+/vQNp77+9ZVfvv70DRUgm77+9fd6lIu+/vREV77+977+977+9Su+/ve+/vRTGh++/ve+/vUAFBSAmKO+/vQDvv70R77+9ADnvv73vv70BL1Tvv71UMBxY77+9ISYB77+9d2Pvv70677+977+9QhEO77+9ICTvv71GUngNTjDvv70h77+9HAfvv73vv71DWWTvv73vv73vv71hIe+/vVABW0Pvv70g77+977+9KB7vv73vv70Q77+977+977+977+977+9HQkC3Z7vv73vv70m77+9QiQA77+977+9chrvv71OUl0i77+9J++/ve+/vRrvv73vv73vv70IemLvv73vv70UQe+/vU1C77+977+9zKLvv70277+9KO+/vT9ICu+/vXfHvSDvv71E77+9Je+/vWkZ77+977+9AQXvv73vv70q66y077+9UO+/ve+/ve+/ve+/vRZpIe+/ve+/vVdqKhLvv70j77+9KhPvv73vv70THe+/ve+/vVbvv71MIe+/vTrvv73vv73vv71c77+9X++/ve+/vXwiIG82Su+/vU8R77+9Cu+/ve+/vX3CnDpsKgDvv71gbBTvv70s77+977+9IRXXou+/vSfvv73vv70e77+966+q77+9W++/vQvlnpvvv70gG0LXmBJh77+977+9I++/ve+/ve+/vRLvv70FHe+/vSLvv70M77+977+9UO+/vV3vv70UW++/ve+/ve+/vSnvv70ECX0mGBzvv70x77+91KMc77+9Pu+/vQt6NTZMBAkBC++/ve+/vQAxUBnSlHPvv73vv73vv70uO++/vTnvv71XGyTvv73vv71277+9fCzvv71wU++/vTzvv70uFO+/ve+/vWnvv70bG++/vULvv70h77+9Je+/vR0NLWDvv73Rhe+/ve+/ve+/vShEYBBSCe+/ve+/ve+/vWRDO2wwNe+/vT0w77+9WGsF77+9Ze+/vXBdDx1TBDvvv70lVmljTu+/ve+/vRBrGVLvv712Le+/ve+/vXVUH++/ve+/ve+/ve+/ve+/vQlA77+9ICfvv73vv73vv70D77+977+9TCzvv71Z77+9045X77+977+9NEvvv71A77+9ZDBb77+9OR0x77+9IO+/ve+/vX1j77+9Ai8s77+977+977+96qy3TkUQADs=",
            eBayLogo.get("$datauri").toString());
   }

   @Test
   public void testSharedJs() {
      JsExpression ebayTime = getParser().parse("${res.js.shared.ebaytime_js}");

      Assert.assertEquals("js.shared:/ebaytime_js", ebayTime.evaluate().getUrn().toString());
      Assert.assertEquals("http://res.ebay.com/js/ebaytime.js", ebayTime.get("$url").toString());
      Assert.assertEquals("https://res.ebay.com/js/ebaytime.js", ebayTime.get("$secureurl"));
      Assert.assertEquals("http://res.ebay.com/js/ebaytime.js", ebayTime.toString());
      Assert.assertEquals("${res.js.shared.ebaytime_js}", ebayTime.toExternalForm());
   }

   @Test
   public void testSharedJs2() {
      JsExpression ebayTime = getParser().parse("${res.js.shared.bitbybit.ebaytime_js}");

      Assert.assertEquals("js.shared:/bitbybit/ebaytime_js", ebayTime.evaluate().getUrn().toString());
      Assert.assertEquals("http://res.ebay.com/js/bitbybit/ebaytime.js", ebayTime.get("$url").toString());
      Assert.assertEquals("https://res.ebay.com/js/bitbybit/ebaytime.js", ebayTime.get("$secureurl"));
      Assert.assertEquals("http://res.ebay.com/js/bitbybit/ebaytime.js", ebayTime.toString());
      Assert.assertEquals("${res.js.shared.bitbybit.ebaytime_js}", ebayTime.toExternalForm());
   }
}
