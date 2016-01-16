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
public @interface Type {
    Class<?> type() default String.class;
    String message() default "类型不对...";
}
