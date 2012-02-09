package org.unidal.webres.taglib.basic;

import org.unidal.webres.tag.core.BaseTagLibDefinition;
import org.unidal.webres.tag.meta.TagLibMeta;

/**
 * Class <code>ResourceTagLibDefinition</code> is a JSP standard tag library for web resources. <p>
 * 
 * In the ESF, use the following URI to reference the library: uri="http://www.unidal.org/webres".<p>
 * 
 * How to reference: 
 *    &lt%@ taglib uri="http://www.unidal.org/v4/resource" prefix="res" %&gt <br/>
 *    &lt;res:img src="${res.img.local.half.eBayLogo_gif}" alt="eBay Logo" height="110" width="45" /&gt; <br/>
 */
@TagLibMeta(uri = ResourceTagLibConstants.TAGLIB_URI_RESOURCE, shortName = "res", description = "JSP standard tag library for V4 resources", version = "1.0", jspVersion = "2.0")
public class ResourceTagLibDefinition extends BaseTagLibDefinition {
   /**
    * Public constructor with given arguments. 
    * 
    * @param uri           the URI for this tag library
    * @param shortName     the tag library name
    * @param description   the description of this tag library
    * @param version       the version of this tag library
    * @param jspVersion    the JSP version required by this tag library
    */
   public ResourceTagLibDefinition(String uri, String shortName, String description, String version, String jspVersion) {
      super(uri, shortName, description, version, jspVersion);
   }

   @Override
   protected void init() {
      addTag(BeanTagHandler.class);
      addTag(SetTagHandler.class);
      addTag(TokenTagHandler.class);
      
      addTag(ImageTagHandler.class);
      
      addTag(LinkTagHandler.class);
      
      addTag(UseCssTagHandler.class);
      addTag(CssSlotTagHandler.class);
      
      addTag(UseJsTagHandler.class);
      addTag(JsSlotTagHandler.class);
   }
}