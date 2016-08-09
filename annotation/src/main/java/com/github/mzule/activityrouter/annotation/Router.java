package com.github.mzule.activityrouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Router {

    String[] value();

    String[] stringParams() default "";

    String[] intParams() default "";

    String[] longParams() default "";

    String[] booleanParams() default "";

    String[] shortParams() default "";

    String[] floatParams() default "";

    String[] doubleParams() default "";

    String[] byteParams() default "";

    String[] charParams() default "";

    String[] transfer() default "";
}
