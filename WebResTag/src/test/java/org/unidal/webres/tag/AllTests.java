package org.unidal.webres.tag;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import org.unidal.webres.tag.common.TokenTagTest;
import org.unidal.webres.tag.css.CssSlotTagTest;
import org.unidal.webres.tag.css.UseCssTagTest;
import org.unidal.webres.tag.img.ImageTagTest;
import org.unidal.webres.tag.js.JsSlotTagTest;
import org.unidal.webres.tag.js.UseJsTagTest;
import org.unidal.webres.tag.link.LinkTagTest;
import org.unidal.webres.tag.resource.ResourceMarkerProcessorTest;

@RunWith(Suite.class)
@SuiteClasses({

/* .common */
TokenTagTest.class,

/* .css */
UseCssTagTest.class,

CssSlotTagTest.class,

/* .img */
ImageTagTest.class,

/* .js */
UseJsTagTest.class,

JsSlotTagTest.class,

/* .link */
LinkTagTest.class,

/* .resource */
ResourceMarkerProcessorTest.class

})
public class AllTests {

}
