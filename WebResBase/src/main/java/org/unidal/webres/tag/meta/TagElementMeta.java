package org.unidal.webres.tag.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TagElementMeta {
	boolean required() default false;
   
   // Developer has to handle the node(root element) passed in manually.
   boolean customized() default false; 
}
