package org.unidal.webres.converter;

import java.lang.reflect.TypeVariable;
import java.util.List;

public class ConverterHandler implements IConverterHandler {
   @Override
   public Object convert(final ConverterContext ctx) throws ConverterException {
      final Class<?> targetClass = ctx.getTargetClass();
      final Class<?> sourceClass = ctx.getSourceClass();

      if (targetClass.isAssignableFrom(sourceClass) && !(ctx.getTargetType() instanceof TypeVariable<?>)) {
         // No need to convert
         return ctx.getSource();
      }

      final List<Converter<?>> converters = ctx.getManager().getRegistry().findConverters(ctx);

      for (final Converter<?> converter : converters) {
         try {
            final Object value = converter.convert(ctx);
            
            return value;
         } catch (Exception e) {
            ctx.addConverterException(converter, e);
         }
      }

      throw new ConverterException(ctx);
   }
}
