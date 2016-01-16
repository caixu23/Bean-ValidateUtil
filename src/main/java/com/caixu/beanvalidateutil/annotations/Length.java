package com.caixu.beanvalidateutil.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xu on 2015/12/4.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Length {

    int minSize() default 0;
    int maxSize() default 1000;
    String message() default "字段长度不正确!";

}
