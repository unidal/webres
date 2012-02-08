package org.unidal.webres.tag.common;

import org.unidal.webres.resource.dummy.IDummyResource;
import org.unidal.webres.resource.dummy.IDummyResourceRef;
import org.unidal.webres.resource.expression.IResourceExpressionEnv;
import org.unidal.webres.resource.expression.ResourceExpression;
import org.unidal.webres.tag.ITagEnv;
import org.unidal.webres.tag.resource.ResourceTagSupport;

/**
 * <ul>Following use cases are supported:
 * <li>&lt;res:bean id="res" /&gt;</li>
 * </ul>
 */
public class BeanTag extends ResourceTagSupport<BeanTagModel, IDummyResourceRef, IDummyResource> {
   public BeanTag() {
      super(new BeanTagModel());
   }

   @Override
   public IDummyResourceRef build() {
      ITagEnv env = getEnv();
      String id = getModel().getId();
      IResourceExpressionEnv expressionEnv = env.getLookupManager().lookupComponent(IResourceExpressionEnv.class);

      if (env.getPageAttribute(id) == null) {
         env.setPageAttribute(id, new ResourceExpression(expressionEnv, id));
      }

      return null;
   }
}
