package org.unidal.webres.tag;

import org.unidal.webres.logging.ILogger;
import org.unidal.webres.logging.LoggerFactory;

public abstract class TagSupport<T, M extends ITagModel, S extends ITagState<S>> implements ITag<T, M, S> {
   private S m_state;

   private M m_model;

   private ITagEnv m_env;

   private int m_errors;

   public TagSupport(M model, S initState) {
      m_model = model;
      m_state = initState;
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

   protected String asString(Object... attributes) {
      return asString(false, attributes);
   }

   @Override
   public void end() {
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

      m_errors++;
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
   public final S getState() {
      return m_state;
   }

   public boolean hasErrors() {
      return m_errors > 0;
   }

   protected void out(Object obj) {
      ITagEnv env = getEnv();

      env.out(obj);
   }

   @Override
   public void setEnv(ITagEnv env) {
      m_env = env;
   }

   @Override
   public final void setState(S newState) {
      if (m_state.canTransit(newState)) {
         m_state = newState;
      } else {
         throw new RuntimeException(String.format("Can't transit state from(%s) to (%s) in tag(%s)!", m_state,
               newState, this));
      }
   }

   @Override
   public void start() {
   }
}
