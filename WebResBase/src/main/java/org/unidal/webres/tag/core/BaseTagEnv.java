package org.unidal.webres.tag.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseTagEnv implements ITagEnv {
   //Fixed for performance tuning
   protected StringBuilder m_err;

   //Fixed for performance tuning
   protected StringBuilder m_out;

   private TagOutputType m_outputType = TagOutputType.html;

   private Map<String, Object> m_properties;
   
   private ITagAdvice m_tagAdvice;

   public ITagAdvice getTagAdvice() {
      return m_tagAdvice;
   }

   public void setTagAdvice(ITagAdvice tagAdvice) {
      m_tagAdvice = tagAdvice;
   }

   @Override
   public BaseTagEnv err(Object value) {
      if (m_err == null) {
         m_err = new StringBuilder(1024);
      }
      m_err.append(value);
      return this;
   }

   @Override
   public void flush() throws IOException {
      // do nothing since we hold a buffer
   }

   public String getError() {
      if (m_err == null) {
         return "";
      }
      String value = m_err.toString();
      m_err.setLength(0);
      return value;
   }

   public String getOutput() {
      if (m_out == null) {
         return "";
      }
      String value = m_out.toString();
      m_out.setLength(0);
      return value;
   }

   @Override
   public TagOutputType getOutputType() {
      return m_outputType;
   }

   @Override
   public Object getProperty(String name) {
      if (m_properties != null) {
         return m_properties.get(name);
      } else {
         return null;
      }
   }

   @Override
   public void onError(String message, Throwable cause) {
      throw new RuntimeException(message, cause);
   }

   public BaseTagEnv out(Object value) {
      if (value != null) {
         if (m_out == null) {
            m_out = new StringBuilder(1024);
         }
         m_out.append(value);
      }
      return this;
   }


   @Override
   public void setOutputType(TagOutputType type) {
      if (type == null) {
         throw new IllegalArgumentException("TagOutputType can't be null.");
      }

      m_outputType = type;
   }

   @Override
   public void setProperty(String name, Object value) {
      if (m_properties == null) {
         m_properties = new HashMap<String, Object>();
      }

      m_properties.put(name, value);
   }
   
   public <T, M extends ITagModel> void onBegin(ITag<T, M> tag, TagAdviceTarget target) {
      if (m_tagAdvice != null) {
         m_tagAdvice.onBegin(tag, target);
      }
   }
   
   public <T, M extends ITagModel> void onEnd(ITag<T, M> tag, TagAdviceTarget target) {
      if (m_tagAdvice != null) {
         m_tagAdvice.onEnd(tag, target);
      }
   }
   
   /**
    * 
    * @return thrown the exception or not
    * @cause exception
    */
   public <T, M extends ITagModel> boolean onError(ITag<T, M> tag, TagAdviceTarget target,Throwable cause) {
      if (m_tagAdvice != null) {
         return m_tagAdvice.onError(tag, target, cause);
      }
      return true;
   }
}