package org.unidal.webres.converter;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Date;

import org.junit.Test;

import org.unidal.junitnexgen.category.Category;
import org.unidal.junitnexgen.category.Description;
import org.unidal.junitnexgen.category.Category.Groups;

public class ConstructorConverterTest {
   private ConverterManager m_manager = ConverterRuntime.INSTANCE.getManager();
   
   @Test
   @Description("convert java class")
   @Category( { Groups.P2, Groups.UNIT })
   public void testJDKClass() throws Exception {
      assertEquals(new StringBuffer(256).capacity(), ((StringBuffer) m_manager.convert(256, StringBuffer.class))
            .capacity());
      assertEquals(new File("."), m_manager.convert(".", File.class));
      assertEquals(new Date(1), m_manager.convert(1L, Date.class));
      assertEquals(new MessageFormat("{0}"), m_manager.convert("{0}", MessageFormat.class));
      assertEquals(new URL("http://www.example.org/"), m_manager.convert("http://www.example.org/", URL.class));
   }
   
   @Test
   @Description("convert customized class")
   @Category( { Groups.P2, Groups.UNIT })
   public void testUserClass() throws Exception {
      assertEquals(new MyClass("123"), m_manager.convert("123", MyClass.class));
      assertEquals(new MyClass(123), m_manager.convert(new StringBuilder("123"), MyClass.class));
   }

   public static class MyClass {
      private int m_value;

      public MyClass(int value) {
         m_value = value;
      }

      public MyClass(CharSequence value) {
    	  m_value = Integer.parseInt(value.toString());
      }

      @Override
      public boolean equals(Object obj) {
         if (obj instanceof MyClass) {
            return m_value == ((MyClass) obj).getValue();
         } else {
            return false;
         }
      }

      public int getValue() {
         return m_value;
      }

      @Override
      public int hashCode() {
         return m_value;
      }
   }
}
