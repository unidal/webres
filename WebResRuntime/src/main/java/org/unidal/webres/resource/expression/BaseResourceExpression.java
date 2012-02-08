package org.unidal.webres.resource.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.unidal.webres.resource.ResourceContext;
import org.unidal.webres.resource.api.IResource;
import org.unidal.webres.resource.api.IResourceRef;
import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;

public abstract class BaseResourceExpression<T, S> extends BaseExpression<T> implements IResourceExpression<T, S> {
   private IResourceExpressionEnv m_env;

   private BaseResourceExpression<?, ?> m_parent;

   private String m_key;

   public BaseResourceExpression(IResourceExpressionEnv env, BaseResourceExpression<?, ?> parent, String key) {
      m_env = env;
      m_parent = parent;
      m_key = key;
   }

   protected IResourceUrn buildUrn() {
      Urn urn = new Urn();

      buildUrn(urn);
      return urn;
   }

   protected void buildUrn(Urn urn) {
      if (m_parent != null) {
         m_parent.buildUrn(urn);
      }

      prepareUrn(urn);
   }

   @Override
   public T call(Object key, Object[] arguments) {
      throw new UnsupportedOperationException("Not support yet!");
   }

   protected abstract T createChild(String key);

   @Override
   public Set<Map.Entry<String, T>> entrySet() {
      // TODO implement entrySet()
      return null;
   }

   @Override
   public T get(Object key) {
      String k = String.valueOf(key);

      if (k != null && k.length() > 0 && k.charAt(0) == PROPERTY_PREFIX) {
         return getPropertyValue(k);
      } else {
         return getChild(k);
      }
   }

   protected ICache<T> getCache() {
      // by default, there is no cache enabled
      return null;
   }

   protected T getChild(String key) {
      ICache<T> cache = getCache();
      T value = null;

      if (cache != null) {
         value = cache.get(key);
      }

      if (value == null) {
         value = createChild(key);

         if (cache != null && value != null) {
            cache.put(key, value);
         }
      }

      return value;
   }

   protected abstract String getDefaultProperty();

   protected IResourceExpressionEnv getEnv() {
      return m_env;
   }

   protected String getKey() {
      return m_key;
   }

   protected BaseResourceExpression<?, ?> getParent() {
      return m_parent;
   }

   @SuppressWarnings("unchecked")
   protected T getPropertyValue(String property) {
      IResourceExpressionEnv env = getEnv();
      S ref = evaluate();

      if (ref instanceof IResourceRef) {
         ResourceContext ctx = new ResourceContext(env.getResourceContext());
         IResource<?, ?> resource = ((IResourceRef<?>) ref).resolve(ctx);
         IResourceType resourceType = resource.getMeta().getResourceType();
         IResourcePropertyEvaluator<?> evaluator = env.getResourcePropertyEvaluator(resourceType, property);

         if (evaluator == null) {
            env.err(String.format("Property(%s) is not supported in EL(%s)!", property, toExternalForm()));
         } else {
            return (T) ((IResourcePropertyEvaluator<IResource<?, ?>>) evaluator).evaluate(resource);
         }
      }

      env.err(String.format("Can't evaluate resource expression in EL(%s)!", toExternalForm()));
      return null;
   }

   protected int getTokenType(String str) {
      int len = str.length();
      boolean firstDigit = false;
      boolean withLetter = false;
      boolean withPunctuation = false;

      for (int i = 0; i < len; i++) {
         char ch = str.charAt(i);

         if (ch == '$' || ch == '_' || Character.isLetter(ch)) {
            withLetter = true;
         } else if (Character.isDigit(ch)) {
            if (i == 0) {
               firstDigit = true;
            }
         } else {
            withPunctuation = true;
         }
      }

      if (withPunctuation) {
         return 2; // with punctuation inside
      } else if (!withLetter) {
         return 3; // number
      } else if (firstDigit) {
         return 2; // not a number or identifier
      } else {
         return 1; // identifier
      }
   }

   protected abstract void prepareUrn(Urn urn);

   @Override
   public String toExternalForm() {
      StringBuilder sb = new StringBuilder(64);

      sb.append("${");
      toExternalForm(sb);
      sb.append('}');

      return sb.toString();
   }

   protected void toExternalForm(StringBuilder sb) {
      if (m_parent != null) {
         m_parent.toExternalForm(sb);

         switch (getTokenType(m_key)) {
         case 1:
            sb.append('.').append(m_key);
            break;
         case 2:
            sb.append("['").append(m_key).append("']");
            break;
         case 3:
            sb.append('[').append(m_key).append(']');
            break;
         }
      } else {
         sb.append(m_key);
      }
   }

   @Override
   public String toString() {
      String property = getDefaultProperty();

      if (property != null) {
         T value = getPropertyValue(PROPERTY_PREFIX + property);

         if (value != null) {
            return value.toString();
         }
      }

      return "";
   }

   protected static class Cache<T> implements ICache<T> {
      private Map<String, T> m_map;

      public Cache(int initialSize) {
         m_map = new HashMap<String, T>(initialSize * 4 / 3 + 1);
      }

      public T get(String key) {
         return m_map.get(key);
      }

      public void put(String key, T expr) {
         m_map.put(key, expr);
      }
   }

   public static interface ICache<T> {
      public T get(String key);

      public void put(String key, T expr);
   }

   protected static class Urn implements IResourceUrn {
      private String m_resourceType;

      private String m_namespace;

      private List<String> m_sections = new ArrayList<String>();

      // this could be adjusted later
      // for example, /half/eBayLogo_gif ==> /half/eBayLogo.gif
      private String m_pathInfo;

      public void addSection(String section) {
         m_sections.add(section);
      }

      @Override
      public String getNamespace() {
         return m_namespace;
      }

      @Override
      public String getPathInfo() {
         if (m_pathInfo == null) {
            return getResourceId();
         } else {
            return m_pathInfo;
         }
      }

      @Override
      public String getResourceId() {
         StringBuilder sb = new StringBuilder(64);

         for (String section : m_sections) {
            sb.append('/').append(section);
         }

         return sb.toString();
      }

      @Override
      public String getResourceTypeName() {
         return m_resourceType;
      }

      @Override
      public String getScheme() {
         return m_resourceType + '.' + m_namespace;
      }

      public List<String> getSections() {
         return m_sections;
      }

      public void setNamespace(String namespace) {
         m_namespace = namespace;
      }

      @Override
      public void setPathInfo(String pathInfo) {
         m_pathInfo = pathInfo;
      }

      public void setResourceType(String resourceType) {
         m_resourceType = resourceType;
      }

      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder(64);

         sb.append(m_resourceType).append('.').append(m_namespace).append(':').append(getResourceId());

         return sb.toString();
      }
   }
}
