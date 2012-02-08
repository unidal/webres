package org.unidal.webres.resource.spi;

public interface IResourceRegisterable<T> {
   public Class<? super T> getRegisterType();

   public String getRegisterKey();

   public T getRegisterInstance();
}
