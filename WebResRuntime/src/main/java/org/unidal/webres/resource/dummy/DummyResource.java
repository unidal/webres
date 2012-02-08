package org.unidal.webres.resource.dummy;

import org.unidal.webres.resource.api.IResourceMeta;
import org.unidal.webres.resource.api.ResourceException;

public enum DummyResource implements IDummyResource {
   INSTANCE;

   @Override
   public Object getContent() {
      throw new UnsupportedOperationException();
   }

   @Override
   public IResourceMeta getMeta() {
      throw new UnsupportedOperationException();
   }

   @Override
   public void validate() throws ResourceException {
   }
}
