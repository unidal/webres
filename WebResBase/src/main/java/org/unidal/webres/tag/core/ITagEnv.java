package org.unidal.webres.tag.core;

import java.io.IOException;

public interface ITagEnv {
   public ITagEnv err(Object obj);

   public Object findAttribute(String name);

   public void flush() throws IOException;

   public String getContextPath();

   public String getError();

   public String getOutput();

   public TagOutputType getOutputType();

   public Object getPageAttribute(String name);

   public Object getProperty(String name);

   public String getRequestUri();

   public void onError(String message, Throwable cause);

   public ITagEnv out(Object obj);

   public void setOutputType(TagOutputType type);

   public void setPageAttribute(String name, Object value);

   public void setProperty(String name, Object value);

   public enum TagOutputType {
      html, xhtml
   }
   
   public <T, M extends ITagModel> void onBegin(ITag<T, M> tag, TagAdviceTarget target);
   
   public <T, M extends ITagModel> void onEnd(ITag<T, M> tag, TagAdviceTarget target);
   
   /**
    * 
    * @return thrown the exception or not
    * @cause exception
    */
   public <T, M extends ITagModel> boolean onError(ITag<T, M> tag, TagAdviceTarget target,Throwable cause);
}
