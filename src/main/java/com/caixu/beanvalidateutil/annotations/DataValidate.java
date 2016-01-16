package com.caixu.beanvalidateutil.annotations;


import com.caixu.beanvalidateutil.vinterface.ValidateInterface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xu on 2015/12/5.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataValidate {
    boolean notNull() default false;
    String notNullMessage() default "不能为空";

    String lengthLimit() default "{}";
    String lengthLimitMessage() default "长度不符合规范";

    String numberRang() default "{}";
    String numberRangMessage() default "数字范围不符合规范";

    //Class<? extends Number>[] type() default {};
     /* 目前只支持数字和日期(Number和Date.class)
     * 如果要具体制定某一种日期格式，请用正则,并且已经在DateUtils中指定了大多数的日期
     */
    Class<? extends Object>[] type() default {};
    String typeMessage() default "类型错误,不是指定类型";
    String datePattern() default "";//指定日期格式

    //boolean isNeedDate() default false;//是否需要开启验证日期
    //String isNeedDateMessage() default "不是日期格式";

    String regEx() default "";
    String regExMessage() default "不符合正则规则";

    Class<? extends ValidateInterface>[] myFunction() default {};
    String myFunctionMessage() default "不符合自定义规则";
}
