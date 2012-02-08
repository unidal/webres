package org.unidal.webres.resource;

import org.unidal.webres.resource.api.IResourceOutputType;

public enum ResourceOutputType implements IResourceOutputType {
   HTML, XHTML, JSON;

   public boolean isHtml() {
      return this == HTML;
   }

   public boolean isJson() {
      return this == JSON;
   }

   public boolean isXhtml() {
      return this == XHTML;
   }
}