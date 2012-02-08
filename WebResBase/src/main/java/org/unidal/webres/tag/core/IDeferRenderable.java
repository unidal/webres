package org.unidal.webres.tag.core;

public interface IDeferRenderable<T> {
   public String getDeferId();

   public String getDeferType();

   public boolean prepareForDefer(T component);

   public String renderForDefer();
}
