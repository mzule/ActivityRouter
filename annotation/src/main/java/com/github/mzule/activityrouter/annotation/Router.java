package com.github.mzule.activityrouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Router {
    String[] value();

    String[] intExtra() default "";

    String[] longExtra() default "";

    String[] boolExtra() default "";

    String[] shortExtra() default "";

    String[] floatExtra() default "";

    String[] doubleExtra() default "";
}
