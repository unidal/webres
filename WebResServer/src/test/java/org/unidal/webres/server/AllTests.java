package org.unidal.webres.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.unidal.webres.server.taglib.MyTaglibTest;
import org.unidal.webres.server.template.JspTemplateTest;

@RunWith(Suite.class)
@SuiteClasses({

SimpleResourceServletTest.class,

SimpleResourceFilterTest.class,

/* .taglib */
MyTaglibTest.class,

/* .template */
JspTemplateTest.class

})
public class AllTests {

}
