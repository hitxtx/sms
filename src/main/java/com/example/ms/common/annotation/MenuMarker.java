package com.example.ms.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MenuMarker {

    String value() default "";

    String icon() default "fa-circle-o";

    long sort() default 0L;

}
