package org.unidal.webres.tag.core;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.FunctionInfo;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.TagAttributeInfo;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.TagFileInfo;
import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagVariableInfo;

import org.unidal.webres.tag.meta.FunctionMeta;
import org.unidal.webres.tag.meta.TagAttributeMeta;
import org.unidal.webres.tag.meta.TagElementMeta;
import org.unidal.webres.tag.meta.TagMeta;

/**
 * TagLibDefinition is the ESF Java version of the JSP Tag Library Descriptor
 * (.tld) file. The ESF Java version serves a similar purpose and is used to
 * store tag library information to be used by the EsfRuntime.
 * <p>
 * This is the base class for ESF Java version of the Tag Library Descriptor
 * files. Extend this class and register tag libraries, tag files, and
 * functions. Register the class with the EsfRuntime.
 * <p>
 * The ESF java version of the .tld file does not replace the JSP .tld; the
 * standard JSP .tld file is also supported by ESF.
 * <p>
 * Java annotations are used to define the metadata for the tag library
 * descriptor:
 * <ul>
 * <li>shortName</li>
 * <li>description</li>
 * <li>version</li>
 * <li>jspVersion</li>
 * </ul>
 */
public abstract class BaseTagLibDefinition {
   private final String m_info;

   private final String m_jspVersion;

   private final String m_libVersion;

   private final String m_shortName;

   private String m_uri;

   private final Map<String, TagInfo> m_tags = new HashMap<String, TagInfo>(5);

   private final Map<String, FunctionInfo> m_functions = new HashMap<String, FunctionInfo>(5);

   private final Map<String, TagFileInfo> m_tagFiles = new HashMap<String, TagFileInfo>(5);

   /**
    * Constructs a TagLibDefinition with the given uri, shortName, description,
    * version, and jspversion. The shortName, description, version, and
    * jspVersion are specified by the annotations.
    * 
    * @param anUriIn
    *           the URI for this tag library
    * @param aShortName
    *           the tag library name
    * @param aDescription
    *           the description of this tag library
    * @param aVersion
    *           the version of this tag library
    * @param aJspVersion
    *           the JSP version required by this tag library
    */
   public BaseTagLibDefinition(String anUriIn, String aShortName, String aDescription, String aVersion,
            String aJspVersion) {
      m_uri = anUriIn;
      m_shortName = aShortName;
      m_info = aDescription;
      m_libVersion = aVersion;
      m_jspVersion = "2.0";
      // hook for initialzaiton
      init();
   }

   protected abstract void init();

   protected void addTag(String tagName, String tagClassName, String bodycontent, String infoString) {

      addTag(tagName, tagClassName, bodycontent, infoString, null, null, null, null, null, null, false);
   }

   protected void addTag(String tagName, String tagClassName, String bodycontent, String infoString,
            TagExtraInfo tagExtraInfo, TagAttributeInfo[] attributeInfo) {

      addTag(tagName, tagClassName, bodycontent, infoString, tagExtraInfo, attributeInfo, null, null, null, null, false);
   }

   protected void addTag(String tagName, String tagClassName, String bodycontent, String infoString,
            TagExtraInfo tagExtraInfo, TagAttributeInfo[] attributeInfo, String displayName, String smallIcon,
            String largeIcon, TagVariableInfo[] tagVariableInfos, boolean dynamicAttributes) {

      /* Check if the Tag has already be registered */
      if (m_tags.containsKey(tagName)) {
         throw new RuntimeException("Already registered Tag: " + tagName);
      }

      if (attributeInfo == null) {
         attributeInfo = new TagAttributeInfo[] {};
      }
      if (tagVariableInfos == null) {
         tagVariableInfos = new TagVariableInfo[] {};
      }
      m_tags.put(tagName, new TagInfo(tagName, tagClassName, bodycontent, infoString, null, tagExtraInfo,
               attributeInfo, displayName, smallIcon, largeIcon, tagVariableInfos, dynamicAttributes));
   }

   protected TagInfo getTagNamed(String aTagName) {
      TagInfo result = null;
      Iterator<TagInfo> iter = m_tags.values().iterator();
      while (iter.hasNext()) {
         TagInfo element = iter.next();
         if (element.getTagName().equals(aTagName)) {
            result = element;
            break;
         }
      }
      return result;
   }

   protected void addFunction(Method function) {
      addFunction(function, false);
   }

   protected void addFunctionsFromClass(Class<?>... clazz) {
      for (Class<?> c : clazz) {
         Method[] methods = c.getMethods();

         for (Method function : methods) {
            addFunction(function, true);
         }
      }
   }

   private void addFunction(Method function, boolean ignoreException) {
      FunctionMeta meta = function.getAnnotation(FunctionMeta.class);

      if (meta != null && meta.excluded()) {
         return;
      }

      int modifier = function.getModifiers();
      if (!Modifier.isPublic(modifier) || !Modifier.isStatic(modifier)) {
         if (!ignoreException) {
            throw new RuntimeException("Only public static method can be JSP function.");
         } else {
            return;
         }
      }

      String name = meta != null && meta.name().length() > 0 ? meta.name() : function.getName();
      String description = meta != null ? meta.description() : null;
      String className = function.getDeclaringClass().getName();
      StringBuilder sb = new StringBuilder(256);
      boolean first = true;

      sb.append(toJavaSourceType(function.getReturnType().getName())).append(' ');
      sb.append(function.getName()).append('(');

      for (Class<?> parameterType : function.getParameterTypes()) {
         if (!first) {
            sb.append(',');
         } else {
            first = false;
         }

         sb.append(toJavaSourceType(parameterType.getName()));
      }

      sb.append(')');

      addFunction(name, function.getName(), className, sb.toString(), description);
   }

   protected void addFunction(String aName, String aClassName, String aSignature) {
      addFunction(aName, aName, aClassName, aSignature);
   }

   protected void addFunction(String alias, String aName, String aClassName, String aSignature) {
      addFunction(alias, aName, aClassName, aSignature, "");
   }

   protected void addFunction(String alias, String aName, String aClassName, String aSignature, String description) {
      /* Check if the Function has already be registered */
      if (m_functions.containsKey(alias)) {
         throw new RuntimeException("Already registered Function: " + alias);
      }
      m_functions.put(alias, new EsfFunctionInfo(aName, aClassName, aSignature, description));
   }

   protected FunctionInfo getFunctionNamed(String aFunctionName) {
      FunctionInfo result = null;
      Iterator<FunctionInfo> iter = m_functions.values().iterator();
      while (iter.hasNext()) {
         FunctionInfo element = iter.next();
         if (element.getName().equals(aFunctionName)) {
            result = element;
            break;
         }
      }
      return result;
   }

   protected void addTagFile(String name, String path) {
      /* Check if the Tag file has already be registered */
      if (m_tagFiles.containsKey(name)) {
         throw new RuntimeException("Already registered Tag file: " + name);
      }
      String uri = path.replace('\\', '/');
      TagFileInfo tfi = new TagFileInfo(name, uri, null);
      m_tagFiles.put(name, tfi);
   }

   protected void addTag(Class<? extends JspTag> clz) {
      addTag(clz, null);
   }

   protected void addTag(Class<? extends JspTag> clz, String aTagName) {
      TagMeta tagMeta = clz.getAnnotation(TagMeta.class);
      if (tagMeta == null) {
         chuck(clz.getName() + " is missing tag meta annotation.");
      }

      if (aTagName == null) {
         // auto detect it from annotation or class simple name
         if (tagMeta != null && tagMeta.name().length() > 0) {
            aTagName = tagMeta.name();
         } else {
            aTagName = clz.getSimpleName();
         }
      }

      List<TagAttributeInfo> attrInfoList = new ArrayList<TagAttributeInfo>(2);

      for (Method m : clz.getMethods()) {
         // TagElementMeta support validation
         TagElementMeta elementMeta = m.getAnnotation(TagElementMeta.class);
         if (!m.getName().equals("setDynamicElements") && elementMeta != null && !tagMeta.parseBody()) {
            throw new RuntimeException(String.format("%s can not be used when \"parseBody\" is disabled on %s.",
                     TagElementMeta.class.getName(), clz.getName()));
         }

         TagAttributeMeta attrMeta = m.getAnnotation(TagAttributeMeta.class);
         if (attrMeta != null) {
            String methodName = m.getName();
            String type = null;
            if (methodName.startsWith("set")) {
               Class<?>[] paramTypes = m.getParameterTypes();
               if (paramTypes == null || paramTypes.length != 1) {
                  chuck(clz.getName() + "." + methodName + " is not a simple setter.");
               }
               type = paramTypes[0].getName();

            } else {
               chuck(clz.getName() + "." + methodName + " is not a property accessor");
            }
            // support different name for tag attribute
            String name = attrMeta != null && attrMeta.name().length() > 0 ? attrMeta.name()
                     : getPropertyName(methodName);

            // validate fragment tag attribute
            if (attrMeta.fragment()) {
               if (!JspFragment.class.getName().equals(type)) {
                  throw new RuntimeException(String.format("Parameter type of %s() of %s must be %s.", methodName,
                           clz.getName(), JspFragment.class.getName()));
               }

               if (elementMeta != null) {
                  throw new RuntimeException(String.format("%s can not be used with %s(fragment = true) on %s() of %s",
                           TagElementMeta.class.getSimpleName(), TagAttributeMeta.class.getSimpleName(), methodName,
                           clz.getName()));
               }
            }

            attrInfoList.add(new EsfTagAttributeInfo(name, // name
                     attrMeta.required(), // required
                     type, // type
                     attrMeta.rtexprvalue(), // can be runtime value
                     attrMeta.fragment(), // fragment or not
                     attrMeta.description())); // add
         }
      }

      TagAttributeInfo[] attrInfoArray = new EsfTagAttributeInfo[attrInfoList.size()];
      attrInfoList.toArray(attrInfoArray);
      Class<? extends TagExtraInfo> extraClz = tagMeta.tagextrainfo();
      TagExtraInfo extraInfo = null;
      if (extraClz != TagExtraInfo.class) {
         try {
            extraInfo = extraClz.newInstance();
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      }

      if (tagMeta.dynamicAttributes()) {
         if (!DynamicAttributes.class.isAssignableFrom(clz)) {
            throw new RuntimeException(clz + " must implement DynamicAttributes"
                     + " if TagMeta.dynamicAttributes() is true");
         }
      }

      addTag(aTagName, clz.getName(), tagMeta.bodycontent(), tagMeta.info(), extraInfo, attrInfoArray, null, null,
               null, null, tagMeta.dynamicAttributes());
   }

   private static String getPropertyName(String methodName) {
      return "" + Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
   }

   private static void chuck(String message) {
      throw new RuntimeException(message);
   }

   /**
    * Returns information about the tag library descriptor specified by the
    * annotation.
    * 
    * @return Information about tag library descriptor specified by the
    *         annotation.
    */
   public String getInfo() {
      return m_info;
   }

   /**
    * Returns the tag library descriptor name specified by the annotation.
    * 
    * @return Name of the tag library descriptor
    */
   public String getShortName() {
      return m_shortName;
   }

   /**
    * Returns the JSP version required by this tag library. 2.0 is required.
    * 
    * @return the required JSP version, 2.0
    */
   public String getJspVersion() {
      return m_jspVersion;
   }

   /**
    * Returns the version of the tag library descriptor, specified by the
    * annotation.
    * 
    * @return the version of the tag library descriptor
    */
   public String getLibVersion() {
      return m_libVersion;
   }

   /**
    * Returns the URI for this tag library descriptor, specified by the
    * annotation.
    * 
    * @return the URI for this tag library descriptor
    */
   public String getUri() {
      return m_uri;
   }

   public void setUri(String uri) {
      m_uri = uri;
   }

   public Map<String, FunctionInfo> getFunctions() {
      return m_functions;
   }

   public Map<String, TagFileInfo> getTagFiles() {
      return m_tagFiles;
   }

   public Map<String, TagInfo> getTags() {
      return m_tags;
   }

   /**
    * Returns tag, function, and tag file information for this TagLibDefinition.
    * 
    * @return information for this TagLibDefinition
    */
   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder(256);

      sb.append("taglib[");
      sb.append("info: ").append(m_info).append(", ");
      sb.append("jspVersion: ").append(m_jspVersion).append(", ");
      sb.append("libVersion: ").append(m_libVersion).append(", ");
      sb.append("shortName: ").append(m_shortName).append(", ");
      sb.append("uri: ").append(m_uri).append(", ");
      sb.append("tags: ").append(m_tags).append(", ");
      sb.append("functions: ").append(m_functions).append(", ");
      sb.append("tag files: ").append(m_tagFiles).append("]");

      return sb.toString();
   }

   public static class EsfTagAttributeInfo extends TagAttributeInfo {
      private String m_description;

      public EsfTagAttributeInfo(String name, boolean required, String type, boolean reqTime, boolean fragment,
               String description) {
         super(name, required, type, reqTime, fragment);
         m_description = description;
      }

      public EsfTagAttributeInfo(String name, boolean required, String type, boolean reqTime, boolean fragment) {
         super(name, required, type, reqTime, fragment);
      }

      public EsfTagAttributeInfo(String name, boolean required, String type, boolean reqTime) {
         super(name, required, type, reqTime);
      }

      public EsfTagAttributeInfo(String name, boolean required, String type, boolean reqTime, String description) {
         super(name, required, type, reqTime);
         m_description = description;
      }

      public void setDescription(String description) {
         m_description = description;
      }

      public String getDescription() {
         return m_description;
      }
   }

   public static class EsfFunctionInfo extends FunctionInfo {
      private String m_description;

      public EsfFunctionInfo(String name, String klass, String signature, String description) {
         super(name, klass, signature);
         m_description = description;
      }

      public String getDescription() {
         return m_description;
      }

      public void setDescription(String description) {
         m_description = description;
      }
   }

   public static String toJavaSourceType(String type) {

      if (type.charAt(0) != '[') {
         return type;
      }

      int dims = 1;
      String t = null;
      for (int i = 1; i < type.length(); i++) {
         if (type.charAt(i) == '[') {
            dims++;
         } else {
            switch (type.charAt(i)) {
            case 'Z':
               t = "boolean";
               break;
            case 'B':
               t = "byte";
               break;
            case 'C':
               t = "char";
               break;
            case 'D':
               t = "double";
               break;
            case 'F':
               t = "float";
               break;
            case 'I':
               t = "int";
               break;
            case 'J':
               t = "long";
               break;
            case 'S':
               t = "short";
               break;
            case 'L':
               t = type.substring(i + 1, type.indexOf(';'));
               break;
            }
            break;
         }
      }
      StringBuffer resultType = t != null ? new StringBuffer(t) : new StringBuffer();
      for (; dims > 0; dims--) {
         resultType.append("[]");
      }
      return resultType.toString();
   }
}
