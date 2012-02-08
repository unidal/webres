package org.unidal.webres.resource.spi;
public interface ITemplateProvider {
   public String getContent();

   public String getLanguage();

   public long getLastModified();

   public long getLength();
}
