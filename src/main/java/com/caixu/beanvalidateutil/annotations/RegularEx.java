package com.caixu.beanvalidateutil.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xu on 2015/12/5.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RegularEx {
    String regEx() default "";
    String message() default "不符合正则规则";
}
