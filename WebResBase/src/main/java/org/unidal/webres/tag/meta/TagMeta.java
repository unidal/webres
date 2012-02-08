package org.unidal.webres.tag.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.servlet.jsp.tagext.TagExtraInfo;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TagMeta {
	static final String JSP_BODY_CONTENT = "JSP";
	static final String EMPTY_BODY_CONTENT = "empty";
	static final String TAG_DEPENDENT = "tagdependent";
	static final String SCRIPTLESS = "scriptless";
	
	String name() default ""; // use getClass().getSimpleName() by default
	String bodycontent() default JSP_BODY_CONTENT;
	String info();
	boolean dynamicAttributes() default false;
	boolean dynamicElements() default false;
	Class<? extends TagExtraInfo> tagextrainfo() default TagExtraInfo.class;
	
	//support tag cache in taghandlerpool
	boolean isCached() default true;
	//support to override EL reference value by tag content 
	boolean override() default false;

	//support parse body for tag element injection
	boolean parseBody() default true;
}
