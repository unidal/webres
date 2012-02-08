package org.unidal.webres.tag.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TagLibMeta {
   static final String JSP_VERSION = "2.0";

   String uri() default ""; // TODO remove default

   String shortName();

   String description();

   String version();

   String jspVersion() default JSP_VERSION;

}
