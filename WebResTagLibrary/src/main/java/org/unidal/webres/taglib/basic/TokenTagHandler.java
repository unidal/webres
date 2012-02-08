package org.unidal.webres.taglib.basic;

import org.unidal.webres.resource.SystemResourceType;
import org.unidal.webres.resource.dummy.IDummyResourceRef;
import org.unidal.webres.tag.common.TokenTag;
import org.unidal.webres.tag.common.TokenTagModel;
import org.unidal.webres.tag.meta.TagAttributeMeta;
import org.unidal.webres.tag.meta.TagMeta;

/**
 * <ul>Following use cases are supported:
 * <li>&lt;res:token type="js" /&gt;</li>
 * <li>&lt;res:token type="css" /&gt;</li>
 * </ul>
 */
@TagMeta(name = ResourceTagLibConstants.RESOURCE_TOKEN, info = "Resource token tag for page resources", dynamicAttributes = false)
public class TokenTagHandler extends BaseResourceTagHandler<IDummyResourceRef, TokenTagModel, TokenTag> {
   private static final long serialVersionUID = 1L;

   @Override
   protected TokenTag createTag() {
      return new TokenTag();
   }

   @TagAttributeMeta(description = "Customized tag render type")
   public void setRenderType(String type) {
      getModel().setRenderType(type);
   }

   @TagAttributeMeta(required = true, description = "Token type of resource: js, css")
   public void setType(String type) {
      String cssType = SystemResourceType.Css.getName();
      String jsType = SystemResourceType.Js.getName();

      if (!cssType.equalsIgnoreCase(type) && !jsType.equalsIgnoreCase(type)) {
         throw new RuntimeException(String.format(
               "Resource type(%s) is not supported in token tag, please use '%s' or '%s'!", type, cssType, jsType));
      }

      getModel().setType(type);
   }
}