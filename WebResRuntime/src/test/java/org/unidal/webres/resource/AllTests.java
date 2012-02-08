package org.unidal.webres.resource;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import org.unidal.webres.resource.css.CssTest;
import org.unidal.webres.resource.expression.ResourceExpressionTest;
import org.unidal.webres.resource.expression.ZombieExpressionTest;
import org.unidal.webres.resource.expression.parser.ExpressionParserTest;
import org.unidal.webres.resource.img.ImageTest;
import org.unidal.webres.resource.js.JsTest;
import org.unidal.webres.resource.link.LinkTest;
import org.unidal.webres.resource.model.ModelMergerTest;
import org.unidal.webres.resource.model.ModelTest;
import org.unidal.webres.resource.profile.ProfileMergerTest;
import org.unidal.webres.resource.profile.ProfileTest;
import org.unidal.webres.resource.profile.ProfileToModelTest;
import org.unidal.webres.resource.remote.RemoteMetaLoaderTest;
import org.unidal.webres.resource.template.SimpleTemplateTest;
import org.unidal.webres.resource.template.TemplateLanguageTest;
import org.unidal.webres.resource.variation.ResourceVariationTest;

@RunWith(Suite.class)
@SuiteClasses({

/* .css */
CssTest.class,

/* .expression */
ResourceExpressionTest.class,

ZombieExpressionTest.class,

/* .expression.parser */
ExpressionParserTest.class,

/* .img */
ImageTest.class,

/* .js */
JsTest.class,

/* .link */
LinkTest.class,

/* .model */
ModelMergerTest.class,

ModelTest.class,

/* .profile */
ProfileMergerTest.class,

ProfileTest.class,

ProfileToModelTest.class,

/* .remote */
RemoteMetaLoaderTest.class,

/* .template */
TemplateLanguageTest.class,

SimpleTemplateTest.class,

/* .variation */
ResourceVariationTest.class

})
public class AllTests {

}
