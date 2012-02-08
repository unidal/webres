package org.unidal.webres.resource.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.unidal.webres.resource.WarConstant;
import org.unidal.webres.resource.injection.ResourceAttribute;

@ResourceAttribute(value = WarConstant.ServerUrlPrefix, optional = true)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ServerUrlPrefix {

}
