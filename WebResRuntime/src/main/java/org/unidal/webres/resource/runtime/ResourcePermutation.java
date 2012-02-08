package org.unidal.webres.resource.runtime;

import java.util.Locale;

import org.unidal.webres.resource.api.IResourcePermutation;
import org.unidal.webres.resource.helper.Permutations;

public class ResourcePermutation implements IResourcePermutation {
   public static ResourcePermutation create(Locale locale) {
      return create(locale, null);
   }

   public static ResourcePermutation create(Locale locale, String target) {
      if (locale == null) {
         throw new IllegalArgumentException("Permutation locale can't be null!");
      }

      ResourcePermutation perm = new ResourcePermutation(locale, target);

      return perm;
   }

   public static ResourcePermutation fromExternal(String external) {
      return Permutations.forExternal().fromExternal(external);
   }

   private String m_external;

   private Locale m_locale;

   private String m_target;

   private ResourcePermutation(Locale locale, String target) {
      m_locale = locale;
      m_target = target;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      ResourcePermutation other = (ResourcePermutation) obj;
      if (m_locale == null) {
         if (other.m_locale != null)
            return false;
      } else if (!m_locale.equals(other.m_locale))
         return false;
      if (m_target == null) {
         if (other.m_target != null)
            return false;
      } else if (!m_target.equals(other.m_target))
         return false;
      return true;
   }

   public Locale getLocale() {
      return m_locale;
   }

   public String getTarget() {
      return m_target;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((m_locale == null) ? 0 : m_locale.hashCode());
      result = prime * result + ((m_target == null) ? 0 : m_target.hashCode());
      return result;
   }

   public String toExternal() {
      if (m_external == null) {
         m_external = Permutations.forExternal().toExternal(this);
      }

      return m_external;
   }

   @Override
   public String toString() {
      return String.format("locale=%s, target=%s", m_locale, m_target);
   }
}