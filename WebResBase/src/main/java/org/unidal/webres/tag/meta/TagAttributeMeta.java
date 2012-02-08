package org.unidal.webres.tag.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TagAttributeMeta {
   String name() default "";

   boolean required() default false;

   boolean rtexprvalue() default true;

   boolean fragment() default false;

   String description() default "";
}