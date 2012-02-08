package org.unidal.webres.resource.api;

import java.util.Locale;

public interface IResourcePermutation {
   public Locale getLocale();

   public String getTarget();

   public String toExternal();
}
