package org.unidal.webres.tag;

public interface ITagEnvFactory<T> {
   public ITagEnv createTagEnv(T argument);
}
