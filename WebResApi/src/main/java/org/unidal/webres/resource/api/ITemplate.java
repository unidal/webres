package org.unidal.webres.resource.api;

public interface ITemplate extends IResource<ITemplateMeta, String> {
   public String evaluate(ITemplateContext ctx) throws Exception;
}
