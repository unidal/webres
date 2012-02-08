package org.unidal.webres.server.taglib;

import org.unidal.webres.tag.core.BaseTagLibDefinition;
import org.unidal.webres.tag.meta.TagLibMeta;

@TagLibMeta(uri = "http://www.examples.com/mytaglib", shortName = "my", description = "My Tag library for test purpose", version = "1.0", jspVersion = "2.0")
public class MyTagLibrary extends BaseTagLibDefinition {
   /**
    * Public constructor with given arguments. 
    * 
    * @param uri           the URI for this tag library
    * @param shortName     the tag library name
    * @param description   the description of this tag library
    * @param version       the version of this tag library
    * @param jspVersion    the JSP version required by this tag library
    */
   public MyTagLibrary(String uri, String shortName, String description, String version, String jspVersion) {
      super(uri, shortName, description, version, jspVersion);
   }

   @Override
   protected void init() {
      addTag(MyResourceTagHandler.class);
   }
}