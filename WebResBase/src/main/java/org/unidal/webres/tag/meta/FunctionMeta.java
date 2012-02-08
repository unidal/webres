package org.unidal.webres.tag.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FunctionMeta {
   String name() default "";
   
   boolean excluded() default false;
   
   String description() default "";
}
