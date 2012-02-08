package org.unidal.webres.converter;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import org.unidal.junitnexgen.category.Category;
import org.unidal.junitnexgen.category.Description;
import org.unidal.junitnexgen.category.Category.Groups;

public class ListConverterTest {
   private ConverterManager m_manager = ConverterRuntime.INSTANCE.getManager();
   
   @Test
   @Description("convert list")
   @Category( { Groups.P2, Groups.UNIT })
   @SuppressWarnings("unchecked")
   public void testList() {
      List<String> stringArray = (List<String>) m_manager.convert(new String[] { "1", "2", "3" }, List.class);
      assertEquals(3, stringArray.size());
      assertEquals("1", stringArray.get(0));
      assertEquals("2", stringArray.get(1));
      assertEquals("3", stringArray.get(2));

      List<Object> fromList = (List<Object>) m_manager.convert(Arrays.asList("1", 2, "3", true), List.class);
      assertEquals(4, fromList.size());
      assertEquals("1", fromList.get(0));
      assertEquals(2, fromList.get(1));
      assertEquals("3", fromList.get(2));
      assertEquals(true, fromList.get(3));
   }
   
   @Test
   @Description("convert java collection")
   @Category( { Groups.P2, Groups.UNIT })
   @SuppressWarnings("unchecked")
   public void testCollection() {
      List<String> stringArray = (List<String>) m_manager.convert(new String[] { "1", "2", "3" }, Collection.class);
      assertEquals(3, stringArray.size());
      assertEquals("1", stringArray.get(0));
      assertEquals("2", stringArray.get(1));
      assertEquals("3", stringArray.get(2));

      List<Object> fromList = (List<Object>) m_manager.convert(Arrays.asList("1", 2, "3", true), Collection.class);
      assertEquals(4, fromList.size());
      assertEquals("1", fromList.get(0));
      assertEquals(2, fromList.get(1));
      assertEquals("3", fromList.get(2));
      assertEquals(true, fromList.get(3));
   }
   
   @Test
   @Ignore
   @Description("convert iterable")
   @Category( { Groups.P2, Groups.UNIT })
   @SuppressWarnings("unchecked")
   public void commented_testIterable() {
      List<String> stringArray = (List<String>) m_manager.convert(new String[] { "1", "2", "3" }, Iterable.class);
      assertEquals(3, stringArray.size());
      assertEquals("1", stringArray.get(0));
      assertEquals("2", stringArray.get(1));
      assertEquals("3", stringArray.get(2));

      List<Object> fromList = (List<Object>) m_manager.convert(Arrays.asList("1", 2, "3", true), Iterable.class);
      assertEquals(4, fromList.size());
      assertEquals("1", fromList.get(0));
      assertEquals(2, fromList.get(1));
      assertEquals("3", fromList.get(2));
      assertEquals(true, fromList.get(3));
   }
   
   @Test
   @Description("convert set")
   @Category( { Groups.P2, Groups.UNIT })
   @SuppressWarnings("unchecked")
   public void testSet() {
      List<String> stringArray = (List<String>) m_manager.convert(new String[] { "1", "2", "3" }, Set.class);
      assertEquals(3, stringArray.size());
      assertEquals("1", stringArray.get(0));
      assertEquals("2", stringArray.get(1));
      assertEquals("3", stringArray.get(2));

      List<Object> fromList = (List<Object>) m_manager.convert(Arrays.asList("1", 2, "3", true), LinkedList.class);
      assertEquals(4, fromList.size());
      assertEquals("1", fromList.get(0));
      assertEquals(2, fromList.get(1));
      assertEquals("3", fromList.get(2));
      assertEquals(true, fromList.get(3));
   }
}
