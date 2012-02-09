package org.unidal.webres.server.intg;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

org.unidal.webres.resource.AllTests.class,

org.unidal.webres.tag.AllTests.class,

org.unidal.webres.taglib.AllTests.class,

org.unidal.webres.server.AllTests.class,

})
public class AllAllTests {

}
