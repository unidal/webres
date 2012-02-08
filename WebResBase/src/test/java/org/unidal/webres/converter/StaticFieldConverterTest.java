package org.unidal.webres.converter;

import static org.junit.Assert.*;

import org.junit.Test;

import org.unidal.junitnexgen.category.Category;
import org.unidal.junitnexgen.category.Description;
import org.unidal.junitnexgen.category.Category.Groups;

public class StaticFieldConverterTest {
   private ConverterManager m_manager = ConverterRuntime.INSTANCE.getManager();

   @Test
   @Description("convert static field")
   @Category( { Groups.P3, Groups.FUNCTIONAL })
   public void testStaticField() throws Exception {
      assertEquals(MyClass.FIRST, m_manager.convert("FIRST", MyClass.class));
      assertEquals(HolderClass.SECOND, m_manager.convert(
            "org.unidal.webres.converter.StaticFieldConverterTest$HolderClass.SECOND", MyClass.class));

      try {
         m_manager.convert("PRIVATE", MyClass.class);

         fail("ConverterException expected");
      } catch (ConverterException e) {
         // expected
      }
   }

   public static class HolderClass {
      public static MyClass SECOND = new MyClass();
   }

   public static class MyClass {
      public static MyClass FIRST = new MyClass();

      static MyClass PRIVATE = new MyClass();

      private MyClass() {
      }
   }
}
