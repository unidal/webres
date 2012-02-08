package org.unidal.webres.tag;

import java.io.IOException;

public interface ITagEnv {
   public ITagEnv err(Object obj);

   public Object findAttribute(String name);

   public void flush() throws IOException;

   public String getError();

   public ITagLookupManager getLookupManager();

   public String getOutput();

   public Object getPageAttribute(String name);

   public Object getProperty(String name);

   public void handleError(String message, Throwable cause);

   public String getRequestUri();

   public <S extends ITagState<S>> void onBegin(ITag<?, ?, S> tag, S state);

   public <S extends ITagState<S>> void onEnd(ITag<?, ?, S> tag, S state);

   public <S extends ITagState<S>> boolean onError(ITag<?, ?, S> tag, S state, Throwable cause);

   public ITagEnv out(Object obj);

   public void setPageAttribute(String name, Object value);

   public void setProperty(String name, Object value);
}
