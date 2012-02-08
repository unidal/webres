package org.unidal.webres.converter;

import java.util.Map;

import org.unidal.webres.dom.ITagNode;

public class ConverterException extends RuntimeException {
   private static final long serialVersionUID = 7967709318556423946L;

   private transient ConverterContext m_ctx;

   public ConverterException(ConverterContext ctx) {
      m_ctx = ctx;
   }

   public ConverterException(String message) {
      super(message);
   }

   public ConverterException(String message, Throwable cause) {
      super(message, cause);
   }

   public ConverterException(Throwable cause) {
      super(cause);
   }

   public ConverterContext getCtx() {
      return m_ctx;
   }

   @Override
   public String getMessage() {
      if (m_ctx != null) {
         StringBuilder sb = new StringBuilder();
         getMessage(m_ctx, "", sb);
         return sb.toString();
      } else {
         return super.getMessage();
      }
   }

   private void getMessage(ConverterContext ctx, String ctxSource, StringBuilder sb) {
      Object source = ctx.getSource();
      if (source instanceof ITagNode) {
         if (!ctxSource.equals("")) {
            ctxSource = ctxSource + "\\" + ((ITagNode) source).getNodeName();
         } else {
            ctxSource = ((ITagNode) source).getNodeName();
         }
      }

      String errorCtx = "";
      if (!ctxSource.equals("")) {
         errorCtx = "[" + ctxSource + "]";
      }

      Map<Converter<?>, Exception> map = ctx.getConverters();
      if (map != null) {
         for (Map.Entry<Converter<?>, Exception> e : map.entrySet()) {
            Exception exception = e.getValue();
            if (exception instanceof ConverterException) {
               ConverterContext exceptionCtx = ((ConverterException) exception).getCtx();
               if (exceptionCtx != null) {
                  getMessage(exceptionCtx, ctxSource, sb);
               } else {
                  sb.append("\r\n" + errorCtx + " " + exception.toString());
               }
            } else if (exception != null) {
               sb.append("\r\n" + errorCtx + " " + exception.toString());
            }
         }
      }
   }
}
