package org.unidal.webres.resource.injection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.unidal.webres.converter.ConverterRuntime;
import org.unidal.webres.resource.spi.IResourceContainer;

class ResourceInjector {
   private volatile Map<Class<?>, Ticket> m_tickets = new HashMap<Class<?>, Ticket>();

   public void injectAttributes(IResourceContainer container, Object injectable) {
      Class<?> clazz = injectable.getClass();
      Ticket ticket = m_tickets.get(clazz);

      if (ticket == null) {
         ticket = new Ticket(clazz);
         ticket.initialize();

         synchronized (m_tickets) {
            m_tickets.put(clazz, ticket);
         }
      }

      for (TicketEntry entry : ticket.getEntries().values()) {
         String key = entry.getKey();
         Method method = entry.getMethod();
         Class<?> type = method.getParameterTypes()[0];
         Object object = getAttributeValue(container, type, key);

         if (object != null) {
            try {
               method.invoke(injectable, new Object[] { object });
            } catch (InvocationTargetException e) {
               throw new RuntimeException(String.format(
                     "Error when injecting to method %s(%s) of %s with parameter(%s)!", method.getName(), key,
                     method.getDeclaringClass(), object.getClass().getName()), e.getCause());
            } catch (Throwable e) {
               throw new RuntimeException(String.format(
                     "Error when injecting to method %s(%s) of %s with parameter(%s)!", method.getName(), key,
                     method.getDeclaringClass(), object.getClass().getName()), e);
            }
         } else if (!entry.isOptional()) {
            throw new RuntimeException(String.format("No attribute value found when injecting to method %s(%s) of %s!",
                  method.getName(), key, method.getDeclaringClass()));
         }
      }
   }

   private Object getAttributeValue(IResourceContainer container, Class<?> type, String key) {
      Object value = container.getAttribute((Class<?>) type, key);

      if (value == null || type.isAssignableFrom(value.getClass())) {
         return value;
      } else {
         Object convertedValue = ConverterRuntime.INSTANCE.getManager().convert(value, type);

         return convertedValue;
      }
   }

   static class Ticket {
      private Class<?> m_clazz;

      private Map<String, TicketEntry> m_methods = new LinkedHashMap<String, TicketEntry>();

      public Ticket(Class<?> clazz) {
         m_clazz = clazz;
      }

      public Map<String, TicketEntry> getEntries() {
         return m_methods;
      }

      public void initialize() {
         Method[] methods = m_clazz.getMethods();

         for (Method method : methods) {
            // only have one parameter and not static
            if (method.getParameterTypes().length == 1 && !Modifier.isStatic(method.getModifiers())) {
               ResourceAttribute attribute = method.getAnnotation(ResourceAttribute.class);

               if (attribute == null) {
                  for (Annotation annotation : method.getAnnotations()) {
                     attribute = annotation.annotationType().getAnnotation(ResourceAttribute.class);

                     if (attribute != null) {
                        break;
                     }
                  }
               }

               if (attribute != null) {
                  TicketEntry entry = new TicketEntry(attribute.value(), method, attribute.optional());

                  m_methods.put(entry.getKey(), entry);
               }
            }
         }
      }
   }

   static class TicketEntry {
      private String m_key;

      private Method m_method;

      private boolean m_optional;

      public TicketEntry(String key, Method method, boolean optional) {
         m_key = key;
         m_method = method;
         m_optional = optional;
      }

      public String getKey() {
         return m_key;
      }

      public Method getMethod() {
         return m_method;
      }

      public boolean isOptional() {
         return m_optional;
      }
   }
}
