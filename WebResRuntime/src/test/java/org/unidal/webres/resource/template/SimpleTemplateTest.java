package org.unidal.webres.resource.template;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.api.ITemplate;
import org.unidal.webres.resource.api.ITemplateContext;
import org.unidal.webres.resource.api.ITemplateRef;
import org.unidal.webres.resource.runtime.ResourcePermutation;
import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceContext;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.resource.spi.ITemplateProvider;
import org.unidal.webres.resource.spi.ITemplateProviderFactory;
import org.unidal.webres.resource.support.ResourceTestSupport;

public class SimpleTemplateTest extends ResourceTestSupport {
   @BeforeClass
   public static void beforeClass() throws Exception {
      ResourceTestSupport.setup(new SimpleTemplateTest().copyResourceFrom("/SimpleTemplateTest"));
   }

   protected void checkTemplate(ITemplateRef ref, String expected, Object... pairs) throws Exception {
      ITemplate template = ref.resolve(ResourceRuntimeContext.ctx().getResourceContext());
      ITemplateContext ctx = new TemplateContext(pairs);

      Assert.assertEquals(expected, template.evaluate(ctx));
   }

   protected void checkTemplate(String source, String expected, Object... pairs) throws Exception {
      ITemplateRef templateRef = TemplateFactory.forRef().createInlineRef(source);

      checkTemplate(templateRef, expected, pairs);
   }

   @Override
   protected void configure() throws Exception {
      super.configure();

      getRegistry().register(String.class, ResourceConstant.Image.SharedUrlPrefix, "http://res.unidal.org/img");
      getRegistry().register(String.class, ResourceConstant.Image.SharedSecureUrlPrefix, "https://res.unidal.org/img");

      getRegistry().register(MockTemplateProviderFactory.INSTANCE);
   }

   @Override
   protected String getContextPath() {
      return "/test";
   }

   @Test
   public void testJavaTemplate() throws Exception {
      ITemplateRef ref1 = TemplateFactory.forRef().createJavaRef("/mock/t1");
      ITemplateRef ref2 = TemplateFactory.forRef().createJavaRef("/mock/t2");

      checkTemplate(ref1, "Hello, world! Nice to meet robert!", "name", "world", "you", "robert");
      checkTemplate(ref2, "Hello, /test/img/eBayLogo.gif and http://res.unidal.org/img/eBayLogo.gif!");
   }

   @Test
   public void testWithoutResource() throws Exception {
      String template1 = "Hello, world!";
      String expected1 = "Hello, world!";

      checkTemplate(template1, expected1);

      String template2 = "Hello, ${name}!";
      String expected2 = "Hello, world!";

      checkTemplate(template2, expected2, "name", "world");

      String template3 = "Hello, ${name}! Nice to meet ${you}!";
      String expected3 = "Hello, world! Nice to meet robert!";

      checkTemplate(template3, expected3, "name", "world", "you", "robert");
   }

   @Test
   public void testWithoutResourceAdvanced() throws Exception {
      String template3 = "User info: ${profile.users[1].name}, ${profile.users[1].age}, ${profile.users[1].married}.";
      String expected3 = "User info: Bob, 20, false.";

      checkTemplate(template3, expected3, "profile",
            toMap("users", Arrays.asList(new User("Robert", 30, true), new User("Bob", 20, false))));

      String template1 = "User info: ${user.name}, ${user.age}, ${user.married}.";
      String expected1 = "User info: Robert, 30, true.";

      checkTemplate(template1, expected1, "user", toMap("name", "Robert", "age", 30, "married", true));

      String template2 = "User info: ${user.name}, ${user.age}, ${user.married}.";
      String expected2 = "User info: Robert, 30, true.";

      checkTemplate(template2, expected2, "user", new User("Robert", 30, true));
   }

   @Test
   public void testWithResource() throws Exception {
      String template = "Hello, ${res.img.local.eBayLogo_gif} and ${res.img.shared.eBayLogo_gif}!";
      String expected = "Hello, /test/img/eBayLogo.gif and http://res.unidal.org/img/eBayLogo.gif!";

      checkTemplate(template, expected);
   }

   @Test
   public void testWithResourceSecure() throws Exception {
      IResourceContext ctx = ResourceRuntimeContext.ctx().getResourceContext();

      ctx.setSecure(true);

      String template = "Hello, ${res.img.local.eBayLogo_gif} and ${res.img.shared.eBayLogo_gif}!";
      String expected = "Hello, /test/img/eBayLogo.gif and https://res.unidal.org/img/eBayLogo.gif!";

      checkTemplate(template, expected);
   }

   @Test
   public void testWithResourceSecurePermutation() throws Exception {
      IResourceContext ctx = ResourceRuntimeContext.ctx().getResourceContext();

      ctx.setSecure(true);
      ctx.setPermutation(ResourcePermutation.create(Locale.CHINA));

      String template = "Hello, ${res.img.local.eBayLogo_gif} and ${res.img.shared.eBayLogo_gif}!";
      String expected = "Hello, /test/img/zh_CN/eBayLogo.gif and https://res.unidal.org/img/zh_CN/eBayLogo.gif!";

      checkTemplate(template, expected);
   }

   protected Map<String, Object> toMap(Object... pairs) {
      int len = pairs.length;

      if (len % 2 != 0) {
         throw new IllegalArgumentException(String.format("Parameters(%s) should be paired!", Arrays.asList(pairs)));
      }

      Map<String, Object> map = new HashMap<String, Object>();

      for (int i = 0; i < len; i += 2) {
         String name = (String) pairs[i];
         Object value = pairs[i + 1];

         map.put(name, value);
      }

      return map;
   }

   static class MockTemplateProvider implements ITemplateProvider {
      private String m_content;

      public MockTemplateProvider(String content) {
         m_content = content;
      }

      @Override
      public String getContent() {
         return m_content;
      }

      @Override
      public String getLanguage() {
         return TemplateLanguage.Simple.getName();
      }

      @Override
      public long getLastModified() {
         return -1;
      }

      @Override
      public long getLength() {
         return m_content.length();
      }
   }

   static enum MockTemplateProviderFactory implements ITemplateProviderFactory, IResourceRegisterable<MockTemplateProviderFactory> {
      INSTANCE;

      @Override
      public ITemplateProvider create(String path) {
         if (path.equals("/t1")) {
            return new MockTemplateProvider("Hello, ${name}! Nice to meet ${you}!");
         } else if (path.equals("/t2")) {
            return new MockTemplateProvider("Hello, ${res.img.local.eBayLogo_gif} and ${res.img.shared.eBayLogo_gif}!");
         }

         return null;
      }

      @Override
      public MockTemplateProviderFactory getRegisterInstance() {
         return this;
      }

      @Override
      public String getRegisterKey() {
         return "mock";
      }

      @Override
      public Class<? super MockTemplateProviderFactory> getRegisterType() {
         return ITemplateProviderFactory.class;
      }
   }

   static class User {
      private String m_name;

      private int m_age;

      private boolean m_married;

      public User(String name, int age, boolean married) {
         m_name = name;
         m_age = age;
         m_married = married;
      }

      public int getAge() {
         return m_age;
      }

      public String getName() {
         return m_name;
      }

      public boolean isMarried() {
         return m_married;
      }
   }
}
