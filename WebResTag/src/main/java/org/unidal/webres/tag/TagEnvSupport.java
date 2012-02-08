package org.unidal.webres.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class TagEnvSupport implements ITagEnv {
   protected StringBuilder m_err;

   protected StringBuilder m_out;

   private Map<String, Object> m_properties;

   @SuppressWarnings("rawtypes")
   private ITagAdvice m_tagAdvice;

   private ITagLookupManager m_lookupManager;

   @Override
   public TagEnvSupport err(Object value) {
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

   @Override
   public String getError() {
      if (m_err == null) {
         return "";
      }
      String value = m_err.toString();
      m_err.setLength(0);
      return value;
   }

   @Override
   public ITagLookupManager getLookupManager() {
      if (m_lookupManager == null) {
         throw new RuntimeException("No ITagLookupManager was set!");
      } else {
         return m_lookupManager;
      }
   }

   @Override
   public String getOutput() {
      if (m_out == null) {
         return "";
      }
      String value = m_out.toString();
      m_out.setLength(0);
      return value;
   }

   @Override
   public Object getProperty(String name) {
      if (m_properties != null) {
         return m_properties.get(name);
      } else {
         return null;
      }
   }

   @SuppressWarnings("unchecked")
   protected <S extends ITagState<S>> ITagAdvice<S, ITag<?, ?, S>> getTagAdvice() {
      return (ITagAdvice<S, ITag<?, ?, S>>) m_tagAdvice;
   }

   @Override
   public void handleError(String message, Throwable cause) {
      throw new RuntimeException(message, cause);
   }

   @SuppressWarnings("unchecked")
   @Override
   public <S extends ITagState<S>> void onBegin(ITag<?, ?, S> tag, S state) {
      if (m_tagAdvice != null) {
         m_tagAdvice.onBegin(tag, state);
      }
   }

   @SuppressWarnings("unchecked")
   @Override
   public <S extends ITagState<S>> void onEnd(ITag<?, ?, S> tag, S state) {
      if (m_tagAdvice != null) {
         m_tagAdvice.onEnd(tag, state);
      }
   }

   @SuppressWarnings("unchecked")
   @Override
   public <S extends ITagState<S>> boolean onError(ITag<?, ?, S> tag, S state, Throwable cause) {
      if (m_tagAdvice != null) {
         return m_tagAdvice.onError(tag, state, cause);
      } else {
         cause.printStackTrace();
         return true;
      }
   }

   @Override
   public TagEnvSupport out(Object value) {
      if (value != null) {
         if (m_out == null) {
            m_out = new StringBuilder(1024);
         }
         m_out.append(value);
      }
      return this;
   }

   protected void setLookupManager(ITagLookupManager lookupManager) {
      m_lookupManager = lookupManager;
   }

   @Override
   public void setProperty(String name, Object value) {
      if (m_properties == null) {
         m_properties = new HashMap<String, Object>();
      }

      m_properties.put(name, value);
   }

   protected <S extends ITagState<S>> void setTagAdvice(ITagAdvice<S, ITag<?, ?, S>> tagAdvice) {
      m_tagAdvice = tagAdvice;
   }
}