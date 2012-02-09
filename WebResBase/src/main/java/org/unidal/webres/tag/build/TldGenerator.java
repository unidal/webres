package org.unidal.webres.tag.build;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.tagext.FunctionInfo;
import javax.servlet.jsp.tagext.TagAttributeInfo;
import javax.servlet.jsp.tagext.TagFileInfo;
import javax.servlet.jsp.tagext.TagInfo;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.unidal.webres.tag.core.BaseTagLibDefinition;
import org.unidal.webres.tag.core.BaseTagLibDefinition.EsfFunctionInfo;
import org.unidal.webres.tag.core.BaseTagLibDefinition.EsfTagAttributeInfo;
import org.unidal.webres.tag.meta.TagLibMeta;

public class TldGenerator {
   private static String OLD_TAGLIB_URI_RESOURCE = "http://www.unidal.org/esf/resource";

   /**
    * This class is for build3. Build3 plugin will invoke "main" method with
    * arguments - taglib name/classname pairs and output directory.
    * 
    * e.g. [taglibOne org.unidal.esf.TagLibDefinitionOne taglibTwo
    * org.unidal.esf.TagLibDefinationTwo C:\project\META-INF]
    */

   public static void main(String[] args) {
      int len = args.length;
      if (len < 2) {
         System.err.println(String.format("Usage: java %s <key> <class> [<key> <class> ...] [<outputdir>]",
               TldGenerator.class.getName()));
         System.exit(-1);
      }

      File tldDir = len % 2 == 0 ? new File(".") : new File(args[len - 1]);

      if (!tldDir.exists()) {
         tldDir.mkdirs();
      }

      if (!tldDir.isDirectory()) {
         System.err.println(tldDir.toString() + " is not a directory.");
         System.exit(-2);
      }

      if (!tldDir.canWrite()) {
         System.err.println(tldDir.toString() + " can not be written.");
         System.exit(-3);
      }

      List<Exception> tldExceptions = new ArrayList<Exception>();

      TldGenerator tldGenerator = new TldGenerator();

      for (int i = 0; i < len - 1; i += 2) {
         tldGenerator.generateTldFile(tldDir, args[i], args[i + 1], tldExceptions, null);

         tldGenerator.generateTldFile(tldDir, args[i] + "_deprecated", args[i + 1], tldExceptions, OLD_TAGLIB_URI_RESOURCE);
      }

      if (!tldExceptions.isEmpty()) {
         for (Exception e : tldExceptions) {
            e.printStackTrace();
         }

         System.err.println("Something error occured during the tld generating phase.");
         System.exit(-4);
      }
   }

   @SuppressWarnings("unchecked")
   public void generateTldFile(File tldDir, String fileName, String className, List<Exception> tldExceptions, String newUri) {

      File tldFile = new File(tldDir, fileName + ".tld");

      try {
         BaseTagLibDefinition tldef = getInstanceOfDefinition((Class<? extends BaseTagLibDefinition>) Thread.currentThread()
               .getContextClassLoader().loadClass(className));

         if (newUri != null) {
            tldef.setUri(newUri);
         }

         Document document = generateDocument(tldef);

         transformDom(new DOMSource(document), new StreamResult(tldFile));

         if (tldExceptions.isEmpty()) {
            System.out.println("TLD file " + tldFile.getCanonicalPath() + " generated");
         } else {
            for (Exception e : tldExceptions) {
               e.printStackTrace();
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
         tldExceptions.add(e);
      }

   }

   protected void transformDom(Source source, Result result) throws TransformerException {

      TransformerFactory tf = TransformerFactory.newInstance();
      //		tf.setAttribute("indent-number", new Integer(2));

      Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.transform(source, result);
   }

   protected Document generateDocument(BaseTagLibDefinition tldef) throws DOMException, ParserConfigurationException {
      Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      tldefToDom(tldef, document);
      return document;
   }

   protected void tldefToDom(BaseTagLibDefinition tldef, Document document) throws DOMException, ParserConfigurationException {

      Element taglib = newElement(document, "taglib");

      taglib.setAttribute("xmlns", "http://java.sun.com/xml/ns/j2ee");
      taglib.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
      taglib.setAttribute("xsi:schemaLocation",
            "http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd");
      taglib.setAttribute("version", tldef.getJspVersion());

      newElement(taglib, "description", tldef.getInfo());
      newElement(taglib, "tlib-version", "1.0");
      newElement(taglib, "short-name", tldef.getShortName());
      newElement(taglib, "uri", tldef.getUri());

      for (TagInfo tagInfo : tldef.getTags().values()) {
         generateTag(taglib, tagInfo);
      }

      for (TagFileInfo tagFileInfo : tldef.getTagFiles().values()) {
         generateTagFile(taglib, tagFileInfo);
      }

      for (FunctionInfo functionInfo : tldef.getFunctions().values()) {
         generateFunction(taglib, functionInfo);
      }

   }

   protected final Element newElement(Node node, String name) {
      return newElement(node, name, null);
   }

   protected final Element newElement(Node node, String name, Object value) {
      Element e = node.getOwnerDocument() == null ? ((Document) node).createElement(name) : node.getOwnerDocument().createElement(
            name);

      if (value != null)
         e.setTextContent(value.toString());

      return (Element) node.appendChild(e);
   }

   protected final CDATASection newCDATASection(Node node, String name) {
      CDATASection cds = node.getOwnerDocument() == null ? ((Document) node).createCDATASection(name) : node.getOwnerDocument()
            .createCDATASection(name);

      return (CDATASection) node.appendChild(cds);
   }

   protected void generateTagFile(Node node, TagFileInfo info) {

      Element tag = newElement(node, "tag-file");

      newElement(tag, "name", info.getName());
      newElement(tag, "path", info.getPath());

   }

   protected void generateFunction(Node node, FunctionInfo funInfo) {

      Element f = newElement(node, "function");

      // add Function description.
      if (funInfo instanceof EsfFunctionInfo) {

         EsfFunctionInfo info = (EsfFunctionInfo) funInfo;
         String description = info.getDescription();

         if (description != null && !description.isEmpty())
            newCDATASection(newElement(f, "description"), description);
      }

      newElement(f, "name", funInfo.getName());
      newElement(f, "function-class", funInfo.getFunctionClass());
      newElement(f, "function-signature", funInfo.getFunctionSignature());

   }

   protected void generateTag(Node node, TagInfo tagInfo) {

      Element tag = newElement(node, "tag");

      // append a description Element with DCDATASection.
      String infoString = tagInfo.getInfoString();
      if (infoString != null && !infoString.isEmpty())
         newCDATASection(newElement(tag, "description"), infoString);

      newElement(tag, "name", tagInfo.getTagName());
      newElement(tag, "tag-class", tagInfo.getTagClassName());
      newElement(tag, "body-content", tagInfo.getBodyContent());

      for (TagAttributeInfo tagAttr : tagInfo.getAttributes()) {
         Element attr = newElement(tag, "attribute");

         // add tag attribute description.
         if (tagAttr instanceof EsfTagAttributeInfo) {
            EsfTagAttributeInfo info = (EsfTagAttributeInfo) tagAttr;
            String description = info.getDescription();
            if (description != null && !description.isEmpty())
               newCDATASection(newElement(attr, "description"), description);
         }

         newElement(attr, "name", tagAttr.getName());
         newElement(attr, "required", tagAttr.isRequired());
         newElement(attr, "rtexprvalue", tagAttr.canBeRequestTime());
         newElement(attr, "type", tagAttr.getTypeName());

      }

      newElement(tag, "dynamic-attributes", tagInfo.hasDynamicAttributes());
   }

   /**
    * Convert the given BaseTagLibDefinition class to an Instance.
    * 
    * @param clz
    *            the BaseTagLibDefinition to convert
    */
   protected BaseTagLibDefinition getInstanceOfDefinition(Class<? extends BaseTagLibDefinition> clz) throws Exception {

      String tagLibUri = null;

      Class<?>[] CONSTRUCTOR = new Class[] { String.class, String.class, String.class, String.class, String.class };

      final TagLibMeta tagMeta = clz.getAnnotation(TagLibMeta.class);

      if (tagMeta == null) {
         throw new Exception(clz.getName() + " is missing tag meta annotation.");
      }

      // auto detect it from annotation or class name
      if (tagMeta != null && tagMeta.uri().length() > 0) {
         tagLibUri = tagMeta.uri();
      } else {
         tagLibUri = clz.getName().replace('.', '/');
      }

      try {
         final Constructor<?> constructor = clz.getDeclaredConstructor(CONSTRUCTOR);

         final Object[] params = new Object[] { tagLibUri, tagMeta.shortName(), tagMeta.description(), tagMeta.version(),
               tagMeta.jspVersion() };

         final BaseTagLibDefinition lib = (BaseTagLibDefinition) constructor.newInstance(params);
         return lib;
      } catch (Exception e) {
         throw new Exception("Unable to construct tag lib definition: " + tagLibUri, e);
      }
   }
}