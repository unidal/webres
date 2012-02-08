package org.unidal.webres.resource.spi;

import org.unidal.webres.resource.api.ITemplate;
import org.unidal.webres.resource.api.ITemplateContext;

public interface ITemplateEvaluator {
   public String evaluate(IResourceContext ctx, ITemplate template) throws Exception;

   public void setContext(ITemplateContext ctx);
}