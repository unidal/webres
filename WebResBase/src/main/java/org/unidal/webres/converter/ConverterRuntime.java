package org.unidal.webres.converter;

public enum ConverterRuntime {
   INSTANCE;

   private ConverterRuntime() {
      // configure system built-in converters
      configure(new ConverterConfigurator());
   }

   private ConverterManager m_manager = new ConverterManager();

   public void configure(IConverterConfigurator configurator) {
      configurator.configure(m_manager.getRegistry());
   }

   public ConverterManager getManager() {
      return m_manager;
   }
}
