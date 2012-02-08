package org.unidal.webres.resource.dummy;

import org.unidal.webres.resource.api.IResourceType;
import org.unidal.webres.resource.api.IResourceUrn;
import org.unidal.webres.resource.api.ResourceException;
import org.unidal.webres.resource.spi.IResourceContext;

public enum DummyResourceRef implements IDummyResourceRef {
   INSTANCE;

   @Override
   public IResourceType getResourceType() {
      throw new UnsupportedOperationException();
   }

   @Override
   public IResourceUrn getUrn() {
      throw new UnsupportedOperationException();
   }

   @Override
   public IDummyResource resolve(IResourceContext ctx) throws ResourceException {
      return DummyResource.INSTANCE;
   }
}
