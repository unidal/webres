package org.unidal.webres.converter;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConverterContext {
   public static final String OVERRIDE = "override";

   private static final InheritableThreadLocal<Map<String, Object>> s_threadLocalData = new InheritableThreadLocal<Map<String, Object>>() {
      @Override
      protected Map<String, Object> initialValue() {
         return new HashMap<String, Object>();
      }
   };

   public static final ConverterSkippedException SKIP = new ConverterSkippedException();

   @SuppressWarnings("unchecked")
   public static <T> T getThreadLocal(String name) {
      return (T) s_threadLocalData.get().get(name);
   }

   public static boolean isOverride() {
      Object override = getThreadLocal(OVERRIDE);
      return override != null && Boolean.valueOf(override.toString());
   }

   public static void setThreadLocal(String name, Object value) {
      s_threadLocalData.get().put(name, value);
   }

   private Object m_source;

   private Class<?> m_sourceClass;

   private Type m_targetType;

   private Class<?> m_targetClass;

   private Map<Converter<?>, Exception> m_converters;

   private ConverterManager m_manager;

   private boolean m_customized;

   public ConverterContext(Object source, Type targetType, ConverterManager manager) {
      m_source = source;
      m_sourceClass = source.getClass();
      m_targetType = targetType;
      m_targetClass = TypeUtil.getRawType(targetType);
      m_manager = manager;
   }

   public void addConverterException(Converter<?> converter, Exception e) {
      if (m_converters == null) {
         m_converters = new LinkedHashMap<Converter<?>, Exception>();
      }

      m_converters.put(converter, e);
   }

   public Map<Converter<?>, Exception> getConverters() {
      return m_converters;
   }

   public ConverterManager getManager() {
      return m_manager;
   }

   public Object getSource() {
      return m_source;
   }

   public Class<?> getSourceClass() {
      return m_sourceClass;
   }

   public Class<?> getTargetClass() {
      return m_targetClass;
   }

   public Type getTargetType() {
      return m_targetType;
   }

   public boolean isCustomized() {
      return m_customized;
   }

   public void setCustomized(boolean customized) {
      m_customized = customized;
   }

}
