package org.unidal.webres.resource.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.unidal.webres.resource.ResourceConstant;
import org.unidal.webres.resource.injection.ResourceAttribute;

@ResourceAttribute(ResourceConstant.Js.Base)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JsBase {

}
