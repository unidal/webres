package org.unidal.webres.tag.core;

import org.unidal.webres.logging.ILogger;
import org.unidal.webres.logging.LoggerFactory;

public abstract class BaseTag<T, M extends ITagModel> implements ITag<T, M> {
   private State m_state;

   private M m_model;

   private ITagEnv m_env;

   public BaseTag(M model) {
      m_model = model;
      m_state = State.CREATED;
   }

   @Override
   public final State getState() {
      return m_state;
   }

   @Override
   public final void setState(State newState) {
      if (m_state.canTransit(newState)) {
         m_state = newState;
      } else {
         throw new RuntimeException(String.format("Can't transit state from(%s) to (%s) in tag(%s)!", m_state,
               newState, this));
      }
   }

   protected String asString(Object... attributes) {
      return asString(false, attributes);
   }

   protected void error(String message, Object... args) {
      ITagEnv env = getEnv();
      Object showErrorContext = env.getProperty("jsp.showErrorContext");

      String error;
      if (Boolean.TRUE.equals(showErrorContext)) {
         int len = args.length;
         Object[] params = new Object[len + 1];

         System.arraycopy(args, 0, params, 0, len);
         params[len] = toString();

         error = String.format("<!-- " + message + " Tag: %s -->", params);
      } else {
         error = String.format("<!-- " + message + " -->", args);
      }

      env.err(error);

      ILogger logger = LoggerFactory.getLogger(getClass());
      if (logger.isErrorEnabled()) {
         logger.error(error);
      }
   }

   protected void out(Object obj) {
      ITagEnv env = getEnv();

      env.out(obj);
   }

   protected String asString(boolean allowNullValue, Object... attributes) {
      int len = attributes.length;

      if (len % 2 != 0) {
         throw new RuntimeException("attributes value should be paired!");
      }

      StringBuilder sb = new StringBuilder(64);
      boolean first = true;

      sb.append(getClass().getSimpleName()).append("[");

      for (int i = 0; i < len; i += 2) {
         Object name = attributes[i];
         Object value = attributes[i + 1];

         if (first) {
            first = false;
         } else {
            sb.append(", ");
         }

         if (allowNullValue || value != null) {
            sb.append(name).append('=').append(value);
         }
      }

      sb.append(']');

      return sb.toString();
   }

   @Override
   public void end() {
   }

   @Override
   public ITagEnv getEnv() {
      return m_env;
   }

   @Override
   public M getModel() {
      return m_model;
   }

   @Override
   public void setEnv(ITagEnv env) {
      m_env = env;
   }

   @Override
   public void start() {
   }
}
