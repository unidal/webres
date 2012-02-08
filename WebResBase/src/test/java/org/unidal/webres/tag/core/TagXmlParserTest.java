package org.unidal.webres.tag.core;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import static org.junit.Assert.*;

import org.junit.Test;
import org.xml.sax.SAXException;

import org.unidal.webres.dom.ITagNode;
import org.unidal.webres.dom.NodeType;
import org.unidal.webres.dom.TextNode;
import org.unidal.junitnexgen.category.Category;
import org.unidal.junitnexgen.category.Description;
import org.unidal.junitnexgen.category.Category.Groups;

public class TagXmlParserTest {
   private TagXmlParser m_tagXmlParser;

   public TagXmlParserTest() throws ParserConfigurationException, SAXException {
      m_tagXmlParser = new TagXmlParser();
   }

   @Test
   @Description("parsing HTML tag")
   @Category( { Groups.P2, Groups.UNIT })
   public void testTag1() throws SAXException, IOException {
      String text = "<a href=\"http://test\">hello</a>";
      ITagNode node = (ITagNode) m_tagXmlParser.parse(text).getFirstChild();

      assertEquals(1, node.getAttributes().size());
      assertEquals(1, node.getChildNodes().size());
      assertEquals("http://test", node.getAttributes().get("href"));
      assertEquals("hello", ((TextNode) node.getFirstChild()).getNodeValue());

      assertEquals(text, node.toString());
   }

   @Test
   @Description("parsing HTML tag")
   @Category( { Groups.P2, Groups.UNIT })
   public void testTag2() throws SAXException, IOException {
      String text = "<a href=\"http://test\" style=\"bgcolor:red\" attr1=\"&lt;.&gt;\">hello</a>";
      ITagNode node = (ITagNode) m_tagXmlParser.parse(text).getFirstChild();

      assertEquals(3, node.getAttributes().size());
      assertEquals(1, node.getChildNodes().size());
      assertEquals("http://test", node.getAttributes().get("href"));
      assertEquals("hello", ((TextNode) node.getFirstChild()).getNodeValue());

      assertEquals(text, node.toString());
   }

   @Test
   @Description("parsing simple customized tag")
   @Category( { Groups.P2, Groups.UNIT })
   public void testTag3() throws SAXException, IOException {
      String text = "<text></text>";
      ITagNode node = (ITagNode) m_tagXmlParser.parse(text).getFirstChild();

      assertEquals(NodeType.TAG, node.getNodeType());
      assertEquals("text", node.getNodeName());
      assertEquals(false, node.hasChildNodes());
      assertEquals(false, node.hasAttributes());

      assertEquals(text, node.toString());
   }

   @Test
   @Description("parsing text")
   @Category( { Groups.P2, Groups.UNIT })
   public void testText1() throws SAXException, IOException {
      String text = "text";
      TextNode node = (TextNode) m_tagXmlParser.parse(text).getFirstChild();

      assertEquals(NodeType.TEXT, node.getNodeType());
      assertEquals(text, node.getNodeValue());
      assertEquals(text, node.toString());
   }

   @Test
   @Description("parsing text")
   @Category( { Groups.P2, Groups.UNIT })
   public void testText2() throws SAXException, IOException {
      String text = "&quot;&lt;text&gt;&quot;";
      TextNode node = (TextNode) m_tagXmlParser.parse(text).getFirstChild();

      assertEquals(NodeType.TEXT, node.getNodeType());
      assertEquals("\"<text>\"", node.getNodeValue());
      assertEquals(text, node.toString());
   }
}
