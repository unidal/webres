package org.unidal.webres.taglib;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

/* .tag */
ImageTagHandlerTest.class,

LinkTagHandlerTest.class,

UseCssTagHandlerTest.class,

UseJsTagHandlerTest.class,

JsSlotTagHandlerTest.class,

SetTagHandlerTest.class

})
public class AllTests {

}
