package org.unidal.webres.taglib;

import javax.servlet.jsp.PageContext;

import org.unidal.webres.resource.runtime.ResourceRuntimeContext;
import org.unidal.webres.resource.spi.IResourceContainer;
import org.unidal.webres.resource.spi.IResourceRegisterable;
import org.unidal.webres.tag.ITagEnv;
import org.unidal.webres.tag.ITagEnvFactory;

public class JspTagEnvFactory implements ITagEnvFactory<PageContext>, IResourceRegisterable<JspTagEnvFactory> {
   @Override
   public ITagEnv createTagEnv(PageContext pageContext) {
      IResourceContainer container = ResourceRuntimeContext.ctx().getContainer();
      JspTagEnv env = new JspTagEnv(container);

      env.setPageContext(pageContext);
      return env;
   }

   @Override
   public Class<? super JspTagEnvFactory> getRegisterType() {
      return ITagEnvFactory.class;
   }

   @Override
   public String getRegisterKey() {
      return JspTagEnv.NAME;
   }

   @Override
   public JspTagEnvFactory getRegisterInstance() {
      return this;
   }
}