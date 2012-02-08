package org.unidal.webres.resource.spi;

import org.unidal.webres.resource.api.IResourcePermutation;

public interface IResourceContext {
   public IResourcePermutation getPermutation();

   public <T> T getVariation(String name);

   public <T> T getVariation(String name, T defaultValue);

   public boolean hasVariation(String name);

   public boolean isFallbackPermutation();

   public boolean isSecure();

   public <T> T lookup(Class<T> type);

   public <T> T lookup(Class<T> type, String key);

   public void setFallbackPermutation(boolean fallback);

   public void setPermutation(IResourcePermutation permutation);

   public void setSecure(boolean secure);

   public void setVariation(String name, Object value);
}
