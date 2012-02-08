package org.unidal.webres.resource.api;

public class ResourceException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public ResourceException(String message) {
      super(message);
   }

   public ResourceException(String message, Throwable cause) {
      super(message, cause);
   }
}
