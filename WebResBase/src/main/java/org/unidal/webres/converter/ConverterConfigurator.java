package org.unidal.webres.converter;

import org.unidal.webres.converter.advanced.ConstructorConverter;
import org.unidal.webres.converter.advanced.StaticFieldConverter;
import org.unidal.webres.converter.basic.BooleanConverter;
import org.unidal.webres.converter.basic.ByteConverter;
import org.unidal.webres.converter.basic.CharConverter;
import org.unidal.webres.converter.basic.DoubleConverter;
import org.unidal.webres.converter.basic.EnumConverter;
import org.unidal.webres.converter.basic.FloatConverter;
import org.unidal.webres.converter.basic.IntegerConverter;
import org.unidal.webres.converter.basic.LongConverter;
import org.unidal.webres.converter.basic.ObjectConverter;
import org.unidal.webres.converter.basic.ShortConverter;
import org.unidal.webres.converter.basic.StringConverter;
import org.unidal.webres.converter.collection.ArrayConverter;
import org.unidal.webres.converter.collection.CollectionConverter;
import org.unidal.webres.converter.collection.ListConverter;
import org.unidal.webres.converter.node.TagNodeArrayConverter;
import org.unidal.webres.converter.node.TagNodeCollectionConverter;
import org.unidal.webres.converter.node.TagNodeConstructorConverter;
import org.unidal.webres.converter.node.TagNodeConverter;
import org.unidal.webres.converter.node.TagNodeListConverter;
import org.unidal.webres.converter.node.TagNodeMapConverter;
import org.unidal.webres.converter.node.TagNodeRefConverter;
import org.unidal.webres.converter.node.TagNodeValueConverter;

public class ConverterConfigurator implements IConverterConfigurator {
   @SuppressWarnings({ "rawtypes" })
   @Override
   public void configure(ConverterRegistry registry) {
      registry.registerConverter(new ObjectConverter(), ConverterPriority.VERY_LOW.getValue());

      registry.registerConverter(new StringConverter(), ConverterPriority.LOW.getValue());
      registry.registerConverter(new StaticFieldConverter(), ConverterPriority.LOW.getValue());
      registry.registerConverter(new ConstructorConverter(), ConverterPriority.LOW.getValue());

      registry.registerConverter(new BooleanConverter());
      registry.registerConverter(new ByteConverter());
      registry.registerConverter(new CharConverter());
      registry.registerConverter(new DoubleConverter());
      registry.registerConverter(new EnumConverter());
      registry.registerConverter(new FloatConverter());
      registry.registerConverter(new IntegerConverter());
      registry.registerConverter(new LongConverter());
      registry.registerConverter(new ShortConverter());

      registry.registerConverter(new ArrayConverter());
      registry.registerConverter(new CollectionConverter<Object>());
      registry.registerConverter(new ListConverter<Object>());

      registry.registerConverter(new TagNodeConverter());
      registry.registerConverter(new TagNodeArrayConverter(), ConverterPriority.HIGH.getValue());
      registry.registerConverter(new TagNodeCollectionConverter(), ConverterPriority.HIGH.getValue());
      registry.registerConverter(new TagNodeListConverter(), ConverterPriority.HIGH.getValue());
      registry.registerConverter(new TagNodeMapConverter(), ConverterPriority.HIGH.getValue());
      registry.registerConverter(new TagNodeValueConverter(), ConverterPriority.HIGH.getValue());
      registry.registerConverter(new TagNodeConstructorConverter(), ConverterPriority.HIGH.getValue());
      registry.registerConverter(new TagNodeRefConverter(), ConverterPriority.VERY_HIGH.getValue());
   }
}
