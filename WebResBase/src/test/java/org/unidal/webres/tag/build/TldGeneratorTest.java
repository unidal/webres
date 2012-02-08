package org.unidal.webres.tag.build;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.unidal.webres.tag.build.TldGenerator;
import org.unidal.webres.tag.core.BaseTagLibDefinition;
import org.unidal.webres.tag.meta.FunctionMeta;
import org.unidal.webres.tag.meta.TagAttributeMeta;
import org.unidal.webres.tag.meta.TagLibMeta;
import org.unidal.webres.tag.meta.TagMeta;

public class TldGeneratorTest {

   private static Document document;

   @BeforeClass
   public static void beforeClass() {
      TldGenerator tldGen = new TldGenerator();
      BaseTagLibDefinition tldef;
      try {
         tldef = tldGen.getInstanceOfDefinition(MockTagLibDefinition.class);
         document = tldGen.generateDocument(tldef);

      } catch (ClassNotFoundException e) {

         e.printStackTrace();
         fail("ClassNotFoundException Occurs!");
      } catch (DOMException e) {

         e.printStackTrace();
         fail("DOMException Occurs!");
      } catch (ParserConfigurationException e) {

         e.printStackTrace();
         fail("ParserConfigurationException Occurs!");
      } catch (Exception e) {

         e.printStackTrace();
         fail("Exception Occurs!");
      }
   }

   @Test
   public final void testTaglib() {
      Element taglib = document.getDocumentElement();
      assertHasNodeName(taglib, "taglib");
      assertHasNodeAttribute(taglib, "version", "2.0");
      assertHasNodeAttribute(taglib, "xmlns", "http://java.sun.com/xml/ns/j2ee");
      assertHasNodeAttribute(taglib, "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
      assertHasNodeAttribute(taglib, "xsi:schemaLocation",
               "http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd");
   }

   @Test
   public final void testTaglibDescription() {
      Element taglib = document.getDocumentElement();
      assertHasChildNodeOnceWithContent(taglib, "description", TldInfo.DESC1);
   }

   @Test
   public final void testTaglibTlibVersion() {
      Element taglib = document.getDocumentElement();
      assertHasChildNodeOnceWithContent(taglib, "tlib-version", "1.0");
   }

   @Test
   public final void testTaglibShortName() {
      Element taglib = document.getDocumentElement();
      assertHasChildNodeOnceWithContent(taglib, "short-name", TldInfo.SHORTNAME);
   }

   @Test
   public final void testTaglibUri() {
      Element taglib = document.getDocumentElement();
      assertHasChildNodeOnceWithContent(taglib, "uri", TldInfo.URI);
   }

   @Test
   public final void testTaglibTagAndFunctionCounts() {
      Element taglib = document.getDocumentElement();
      assertHasNodeCounts(taglib, "tag", 2);
      assertHasNodeCounts(taglib, "function", 14);
   }

   @Test
   public final void testTaglibTags() {
      Element taglib = document.getDocumentElement();

      assertTrue(hasTextContent(taglib, "tag.name", TldInfo.NAME1));
      assertTrue(hasTextContent(taglib, "tag.name", TldInfo.NAME2));
      assertTrue(hasTextContent(taglib, "tag.tag-class", MockTagHandler1.class.getName()));
      assertTrue(hasTextContent(taglib, "tag.tag-class", MockTagHandler2.class.getName()));
      assertTrue(hasTextContent(taglib, "tag.body-content", "JSP"));
      assertTrue(hasTextContent(taglib, "tag.description", TldInfo.INFO1));
      assertTrue(hasTextContent(taglib, "tag.description", TldInfo.INFO2));
      assertTrue(hasTextContent(taglib, "function.name", "array0"));
      assertTrue(hasTextContent(taglib, "function.name", "array1"));
      assertTrue(hasTextContent(taglib, "function.name", "array2"));
      assertTrue(hasTextContent(taglib, "function.name", "array3"));
      assertTrue(hasTextContent(taglib, "function.name", "arrays"));
      assertTrue(hasTextContent(taglib, "function.name", "map1"));
      assertTrue(hasTextContent(taglib, "function.name", "map2"));
      assertTrue(hasTextContent(taglib, "function.name", "map3"));
      assertTrue(hasTextContent(taglib, "function.name", "maps"));
      assertTrue(hasTextContent(taglib, "function.name", "content"));
      assertTrue(hasTextContent(taglib, "function.name", "content2"));
      assertTrue(hasTextContent(taglib, "function.name", "content3"));
      assertTrue(hasTextContent(taglib, "function.name", "htmlId"));
      assertTrue(hasTextContent(taglib, "function.name", "jsBind"));
      // assertTrue(hasTextContent(taglib, "function.name", "jsFunc"));
      assertTrue(hasTextContent(taglib, "tag.attribute.name", "attribute1"));
      assertTrue(hasTextContent(taglib, "tag.attribute.name", "attribute2"));
   }

   private boolean hasTextContent(Node node, String path, String content) {

      boolean result = false;
      // System.out.println("Calling " + path);
      if (path.contains(".")) {
         String name = path.substring(0, path.indexOf("."));

         NodeList nl = node.getChildNodes();

         for (int i = 0; i < nl.getLength(); i++) {
            if (name.equals(nl.item(i).getNodeName()))
               if (hasTextContent(nl.item(i), path.substring(path.indexOf(".") + 1), content)) {
                  result = true;
                  break;
               }
         }
      } else {
         NodeList nl = node.getChildNodes();
         for (int i = 0; i < nl.getLength(); i++) {
            // System.out.println(nl.item(i).getNodeName());
            if (path.equals(nl.item(i).getNodeName()) && content.equals(nl.item(i).getTextContent())) {
               result = true;
               break;
            }
         }
      }
      return result;
   }

   private void assertHasNodeCounts(Node node, String name, int counts) {
      NodeList nl = node.getChildNodes();

      int occurs = 0;
      for (int i = 0; i < nl.getLength(); i++) {
         if (nl.item(i).getNodeName().equals(name)) {
            occurs++;
         }
      }

      assertEquals(counts, occurs);
   }

   private void assertHasNodeAttribute(Node node, String name, Object obj) {
      assertEquals(obj.toString(), node.getAttributes().getNamedItem(name).getNodeValue());
   }

   private void assertHasChildNodeOnceWithContent(Node node, String name, Object obj) {
      NodeList nl = node.getChildNodes();

      Node theNode = null;
      int occurs = 0;
      for (int i = 0; i < nl.getLength(); i++) {
         if (nl.item(i).getNodeName().equals(name)) {
            theNode = nl.item(i);
            occurs++;
         }
      }

      assertTrue(occurs > 0);
      assertHasNodeCounts(node, name, 1);
      assertEquals(obj.toString(), theNode.getTextContent());

   }

   private void assertHasNodeName(Node node, String name) {
      assertEquals(name, node.getNodeName());
   }

}

@TagLibMeta(uri = TldInfo.URI, shortName = TldInfo.SHORTNAME, description = TldInfo.DESC1, version = TldInfo.VERSION, jspVersion = TldInfo.JSPVERSION)
class MockTagLibDefinition extends BaseTagLibDefinition {

   public MockTagLibDefinition(String anUriIn, String aShortName, String aDescription, String aVersion,
            String aJspVersion) {
      super(anUriIn, aShortName, aDescription, aVersion, aJspVersion);
   }

   @Override
   protected void init() {
      addTag(MockTagHandler1.class);
      addTag(MockTagHandler2.class);
      addFunctionsFromClass(MockTagFunction.class);
   }

}

@TagMeta(name = TldInfo.NAME1, info = TldInfo.INFO1, dynamicAttributes = false)
class MockTagHandler1 extends BodyTagSupport {

   /**
	 * 
	 */
   private static final long serialVersionUID = 1L;

   @TagAttributeMeta(description = TldInfo.DESC2)
   public void setAttribute1(String attr1) {
   }

   @TagAttributeMeta(description = TldInfo.DESC3, required = true)
   public void setAttribute2(String attr2) {

   }

   @TagAttributeMeta(description = TldInfo.DESC4, required = true)
   public void setAttribute3(Object attr3) {

   }

}

@TagMeta(name = TldInfo.NAME2, info = TldInfo.INFO2, dynamicAttributes = true)
class MockTagHandler2 extends BodyTagSupport implements DynamicAttributes {

   private static final long serialVersionUID = 1L;

   @TagAttributeMeta(description = TldInfo.DESC5)
   public void setAttribute1(String attr1) {
   }

   @TagAttributeMeta(description = TldInfo.DESC6, required = true)
   public void setAttribute2(String attr2) {

   }

   @Override
   public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {

   }
}

class MockTagFunction {
   @FunctionMeta(description = TldInfo.DESC7)
   public static Object[] array0() {
      return null;
   }

   @FunctionMeta(description = TldInfo.DESC7)
   public static Object[] array1(Object obj1) {
      return null;
   }

   @FunctionMeta(description = TldInfo.DESC7)
   public static Object[] array2(Object obj1, Object obj2) {
      return null;
   }

   @FunctionMeta(description = TldInfo.DESC7)
   public static Object[] array3(Object obj1, Object obj2, Object obj3) {
      return null;
   }

   @FunctionMeta(description = TldInfo.DESC7)
   public static Object[] arrays(Object[] array1, Object[] array2) {
      return null;
   }

   @FunctionMeta(description = TldInfo.DESC8)
   public static Object map1(String key, Object value) {
      return null;
   }

   @FunctionMeta(description = TldInfo.DESC8)
   public static Object map2(String key1, Object value1, String key2, Object value2) {
      return null;
   }

   @FunctionMeta(description = TldInfo.DESC8)
   public static Object map3(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
      return null;
   }

   @FunctionMeta(description = TldInfo.DESC8)
   public static Object maps(Map<String, Object> map1, Map<String, Object> map2) {
      return null;
   }

   @FunctionMeta(description = "Get DContent with expression")
   public static Object content(Object expression) {

      return null;
   }

   @FunctionMeta(description = "Get content value with the input data model")
   public static Object content2(Object expression, Object model) {
      return null;
   }

   @FunctionMeta(description = "Get content value with the input model and mapping data")
   public static Object content3(Object expression, Object model, String mappingData) {

      return null;
   }

   @FunctionMeta(description = "Generate a html id with the input key; it will return the same html id when input key is the same")
   public static String htmlId(String key) {
      return null;
   }

   /**
    * @param jsFunc
    *           ResourceExpression or JsFunc
    * @param args
    *           parameters for ResourceExpression
    */
   @FunctionMeta(description = "Bind a js event with the element id and jsFunc expression")
   public static void jsBind(String elementId, String eventType, Object jsFunc, Object[] args) {
   }
   //
   // @EsfFunctionMeta(description =
   // "Return a jsFunc resource with the input arguments")
   // public static IJsFuncResource jsFunc(ResourceExpression expression,
   // Object[] args) {
   // return null;
   // }

}

interface TldInfo {
   public static final String URI = "Some URI";
   public static final String SHORTNAME = "Some shortName";
   public static final String VERSION = "X.X";
   public static final String JSPVERSION = "Y.Y.Y";

   public static final String NAME1 = "Some Name1";
   public static final String NAME2 = "Some Name2";
   public static final String NAME3 = "Some Name3";
   public static final String NAME4 = "Some Name4";
   public static final String NAME5 = "Some Name5";
   public static final String NAME6 = "Some Name6";

   public static final String INFO1 = "Some Info1";
   public static final String INFO2 = "Some Info2";
   public static final String INFO3 = "Some Info3";
   public static final String INFO4 = "Some Info4";
   public static final String INFO5 = "Some Info5";
   public static final String INFO6 = "Some Info6";

   public static final String DESC1 = "Some Description1";
   public static final String DESC2 = "Some Description2";
   public static final String DESC3 = "Some Description3";
   public static final String DESC4 = "Some Description4";
   public static final String DESC5 = "Some Description5";
   public static final String DESC6 = "Some Description6";
   public static final String DESC7 = "Some Description7";
   public static final String DESC8 = "Some Description8";
   public static final String DESC9 = "Some Description9";

}
