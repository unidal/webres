package org.unidal.webres.taglib;

import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import org.unidal.webres.converter.ConverterContext;
import org.unidal.webres.converter.ConverterException;
import org.unidal.webres.converter.ConverterManager;
import org.unidal.webres.converter.ConverterRuntime;
import org.unidal.webres.converter.ConverterUtil;
import org.unidal.webres.dom.INode;
import org.unidal.webres.dom.ITagNode;
import org.unidal.webres.dom.NodeType;
import org.unidal.webres.tag.meta.TagElementMeta;

public class TagModelInjector {
   private static final TagModelInjector s_instance = new TagModelInjector();

   private static final Map<Object, Map<String, Method>> s_tagElementMap = new HashMap<Object, Map<String, Method>>();

   public static final TagModelInjector getInstance() {
      return s_instance;
   }

   private TagModelInjector() {
   }

   private Map<String, Method> getTagElementMap(Class<? extends Object> clazz) {
      Map<String, Method> map = null;
      synchronized (s_tagElementMap) {
         map = s_tagElementMap.get(clazz);
      }

      if (map != null) {
         return map;
      }

      map = new HashMap<String, Method>();
      Method[] methods = clazz.getMethods();

      for (Method method : methods) {
         TagElementMeta elementMeta = method.getAnnotation(TagElementMeta.class);

         if (elementMeta != null && method.getName().startsWith("set")) {
            map.put(method.getName(), method);
         }
      }

      synchronized (s_tagElementMap) {
         s_tagElementMap.put(clazz, map);
      }

      return map;
   }

   public void inject(Object tagHandler, ITagNode dom) {
      inject(tagHandler, dom, false, null, ConverterRuntime.INSTANCE.getManager());
   }

   public void inject(Object tagHandler, ITagNode dom, boolean override, Object refValue, ConverterManager manager) {
      if (dom == null) {
         return;
      }

      Class<?> clazz = tagHandler.getClass();
      Map<String, Method> map = getTagElementMap(clazz);
      for (INode node : dom.getChildNodes()) {
         if (node.getNodeType() == NodeType.TAG) {
            String methodName = ConverterUtil.getSetMethodName(((ITagNode) node).getNodeName());
            Method method = map.get(methodName);

            if (method == null) {
               throw new IllegalArgumentException("No setter method(" + methodName
                     + ") with annotation EsfTagElementMeta defined in " + clazz);
            } else if (method.getGenericParameterTypes().length != 1) {
               throw new IllegalArgumentException("Invalid setter method(" + methodName + ") defined in " + clazz
                     + ", only one parameter allowed");
            } else {
               TagElementMeta elementMeta = method.getAnnotation(TagElementMeta.class);
               Object value;

               try {
                  if (override) {
                     ConverterContext.setThreadLocal(ConverterContext.OVERRIDE, true);
                  }

                  value = manager.convert(node, method.getGenericParameterTypes()[0], elementMeta.customized());

                  if (override && refValue != null && value != null) {
                     NodeMerger.INSTANCE.merge(refValue, value, (ITagNode) node);
                     value = refValue;
                  }
               } catch (ConverterException e) {
                  // try to convert to text as possible
                  Object text = manager.convert(node, String.class);

                  try {
                     value = manager.convert(text, method.getGenericParameterTypes()[0], elementMeta.customized());
                  } catch (ConverterException ce) {
                     // throw original exception
                     throw e;
                  }
               } finally {
                  if (override) {
                     ConverterContext.setThreadLocal(ConverterContext.OVERRIDE, false);
                  }
               }

               try {
                  method.invoke(tagHandler, value);
               } catch (Exception e) {
                  throw new RuntimeException("Error when injecting method: " + method + ", data: " + value, e);
               }
            }
         }
      }
   }

   public void injectAttributes(Node component, Map<String, Object> attributes, IProvider<Method> provider,
         ConverterManager manager) {
      Class<?> clazz = component.getClass();

      for (Map.Entry<String, Object> a : attributes.entrySet()) {
         String name = a.getKey();
         Method method = provider.getAttribute(component, name);

         if (method == null) {
            String methodName = ConverterUtil.getSetMethodName(name);

            throw new IllegalArgumentException("No setter method(" + methodName + ") defined in " + clazz
                  + " or its super class.");
         } else {
            Object value = manager.convert(a.getValue(), method.getGenericParameterTypes()[0]);

            try {
               method.invoke(component, value);
            } catch (Exception e) {
               throw new RuntimeException("Error when injecting method: " + method + ", data: " + value + ", class: "
                     + clazz);
            }
         }
      }
   }

   public void injectElements(Node component, ITagNode elements, IProvider<Method> provider, ConverterManager manager) {
      final Class<?> clazz = component.getClass();
      final List<INode> children = elements.getChildNodes();

      for (INode child : children) {
         if (child.getNodeType() == NodeType.TAG) {
            Method method = provider.getElement(component, (ITagNode) child);

            if (method == null) {
               String methodName = ConverterUtil.getSetMethodName(((ITagNode) child).getNodeName());

               throw new IllegalArgumentException("No component setter method(" + methodName + ") defined in " + clazz
                     + " or its super class.");
            } else {
               Object value = manager.convert(child, method.getGenericParameterTypes()[0]);

               try {
                  method.invoke(component, value);
               } catch (Exception e) {
                  throw new RuntimeException("Error when injecting component method: " + method + ", data: " + value
                        + ", class: " + clazz);
               }
            }
         }
      }
   }

   public static interface IProvider<T extends Member> {
      public T getAttribute(Node current, String name);

      public T getElement(Node parent, ITagNode child);
   }

   public static class MethodProvider implements IProvider<Method> {
      @Override
      public Method getAttribute(Node current, String name) {
         Class<?> clazz = current.getClass();
         String methodName = ConverterUtil.getSetMethodName(name);
         Method method = ConverterUtil.getSetMethod(clazz, methodName, null);

         return method;
      }

      @Override
      public Method getElement(Node parent, ITagNode child) {
         final Class<?> clazz = parent.getClass();
         String methodName = ConverterUtil.getSetMethodName(child.getNodeName());
         Method method = ConverterUtil.getSetMethod(clazz, methodName);

         return method;
      }
   }

   static enum NodeMerger {
      INSTANCE;

      private Object getChildValue(Object source, String attrName) {
         Class<?> clazz = source.getClass();
         Method getMethod = ConverterUtil.getGetMethod(clazz, ConverterUtil.getGetMethodName(attrName));

         try {
            return getMethod.invoke(source, (Object[]) null);
         } catch (Exception e1) {
            throw new RuntimeException("Error when invoking method: " + getMethod + ", class: " + clazz);
         }
      }

      private boolean hasELAttribute(ITagNode childNode) {
         for (Map.Entry<String, String> a : childNode.getAttributes().entrySet()) {

            String attrName = a.getKey();
            String attrValue = a.getValue();
            if (isELAttribute(attrName, attrValue)) {
               return true;
            }
         }
         return false;
      }

      private boolean isELAttribute(String attrName, String attrValue) {
         if (attrName == null || attrValue == null) {
            return false;
         }

         if (attrName.equals("value") && attrValue.startsWith("${") && attrValue.endsWith("}")) {
            return true;
         }

         return false;
      }

      private boolean isLeafNode(INode node) {
         if (node.getNodeType() == NodeType.TAG) {
            ITagNode tNode = (ITagNode) node;

            if (!tNode.hasChildNodes() && tNode.getAttributes().size() == 0) {
               return true;
            }
            if (tNode.getChildNodes().size() == 1 && tNode.getChildNodes().get(0).getNodeType() == NodeType.TEXT) {
               return true;
            }
         } else {
            throw new RuntimeException("Unexpected node type: " + node.getNodeType());
         }

         return false;
      }

      public void merge(Object target, Object source, ITagNode node) {
         //don't support collection type override
         if (source instanceof Collection<?>) {
            return;
         }

         mergeAttributes(target, source, node);

         for (INode child : node.getChildNodes()) {
            ITagNode childNode = (ITagNode) child;
            String childNodeName = childNode.getNodeName();
            Object childValue = getChildValue(source, childNodeName);

            if (hasELAttribute(childNode)) {
               setChildValue(target, childNode.getNodeName(), childValue);
               continue;
            }

            Class<?> rawType = childValue.getClass();
            if (Collection.class.isAssignableFrom(rawType) || Map.class.isAssignableFrom(rawType)
                  || Array.class.isAssignableFrom(rawType)) {
               setChildValue(target, childNodeName, childValue);
               continue;
            }

            if (isLeafNode(childNode)) {
               setChildValue(target, childNodeName, childValue);
               continue;
            }

            merge(getChildValue(target, childNodeName), childValue, childNode);
         }
      }

      private void mergeAttributes(Object target, Object source, ITagNode childNode) {
         for (Map.Entry<String, String> a : childNode.getAttributes().entrySet()) {

            String attrName = a.getKey();
            String attrValue = a.getValue();

            if (!isELAttribute(attrName, attrValue)) {
               Object childValue = getChildValue(source, attrName);
               setChildValue(target, attrName, childValue);
            }
         }
      }

      private boolean setChildValue(Object target, String fieldName, Object childValue) {
         Class<?> clazz = target.getClass();
         Method setMethod = ConverterUtil.getSetMethod(clazz, ConverterUtil.getSetMethodName(fieldName));
         try {
            setMethod.invoke(target, childValue);
         } catch (Exception e) {
            throw new RuntimeException("Error when setting method: " + setMethod + ", data: " + childValue
                  + ", class: " + clazz);
         }
         return true;
      }
   }

}
