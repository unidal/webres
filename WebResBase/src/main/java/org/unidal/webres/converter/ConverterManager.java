package org.unidal.webres.converter;

import java.lang.reflect.Type;

public class ConverterManager {
   private IConverterHandler m_handler = new ConverterHandler();

   private ConverterRegistry m_registry = new ConverterRegistry();

   public Object convert(Object from, Type targetType) throws ConverterException {
      return convert(from, targetType, false);
   }

   public Object convert(final Object from, final Type targetType, boolean customized) throws ConverterException {
      ConverterContext ctx = new ConverterContext(from, targetType, this);
      ctx.setCustomized(customized);

      return m_handler.convert(ctx);
   }

   public IConverterHandler getHandler() {
      return m_handler;
   }

   public ConverterRegistry getRegistry() {
      return m_registry;
   }

   public void setHandler(IConverterHandler handler) {
      m_handler = handler;
   }
}
