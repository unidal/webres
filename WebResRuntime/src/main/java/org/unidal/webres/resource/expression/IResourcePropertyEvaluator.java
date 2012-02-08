package org.unidal.webres.resource.expression;

import org.unidal.webres.resource.api.IResource;

public interface IResourcePropertyEvaluator<T extends IResource<?, ?>> {
   public Object evaluate(T resource);
}
