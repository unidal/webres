package org.unidal.webres.resource.expression;

public interface IResourceExpressionFactory<T extends IResourceExpression<?, ?>> {
   public IResourceExpression<?, ?> create(T parent, String key);
}
