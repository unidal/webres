package org.unidal.webres.converter;

public interface IConverterHandler {
   public Object convert(ConverterContext ctx) throws ConverterException;
}
