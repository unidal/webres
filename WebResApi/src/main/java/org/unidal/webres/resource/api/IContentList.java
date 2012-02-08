package org.unidal.webres.resource.api;

import java.util.List;

public interface IContentList<C> extends IContent<List<C>> {

   public int getLength();

   public List<C> getList();

   public List<String> getResource();
}
