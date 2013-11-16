package org.unidal.webres;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.unidal.webres.converter.ArrayConverterTest;
import org.unidal.webres.converter.BasicConverterTest;
import org.unidal.webres.converter.ConstructorConverterTest;
import org.unidal.webres.converter.ListConverterTest;
import org.unidal.webres.converter.TypeUtilTest;
import org.unidal.webres.logging.LoggerFactoryTest;
import org.unidal.webres.logging.LoggerTest;
import org.unidal.webres.tag.core.TagXmlParserTest;

@SuiteClasses({

/* .converter */
TypeUtilTest.class,

BasicConverterTest.class,

ArrayConverterTest.class,

ListConverterTest.class,

ConstructorConverterTest.class,

/* .tag.core */
TagXmlParserTest.class,

/* .logging */
LoggerTest.class,

LoggerFactoryTest.class

})
@RunWith(Suite.class)
public class AllTests {

}
