package org.unidal.webres.resource.profile;

import org.unidal.webres.resource.profile.transform.DefaultXmlBuilder;

public abstract class BaseEntity<T> implements IEntity<T> {
   protected void assertAttributeEquals(Object instance, String entityName, String name, Object expectedValue, Object actualValue) {
      if (expectedValue == null && actualValue != null || expectedValue != null && !expectedValue.equals(actualValue)) {
         throw new IllegalArgumentException(String.format("Mismatched entity(%s) found! Same %s attribute is expected! %s: %s.", entityName, name, entityName, instance));
      }
   }

   @Override
   public String toString() {
      DefaultXmlBuilder builder = new DefaultXmlBuilder();

      accept(builder);
      return builder.getString();
   }
}
