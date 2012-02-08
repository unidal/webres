package org.unidal.webres.converter;

public class ConverterFatalException extends RuntimeException {
   private static final long serialVersionUID = 2253167670836420035L;

   private Class<?> m_converterClass;

   public ConverterFatalException(String message, Class<?> converter) {
      super(message);
      m_converterClass = converter;
   }

   @Override
   public String getMessage() {
      return "Fatal error[" + m_converterClass.getName() + "], " + super.getMessage();
   }

}
