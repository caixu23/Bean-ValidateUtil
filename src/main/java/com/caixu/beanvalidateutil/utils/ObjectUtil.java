package com.caixu.beanvalidateutil.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;


public class ObjectUtil {
    /**
     * 判断对象是否为空，包括数组、集合、map等是否为空
     *
     * @param o java.lang.Object 输入对象
     * @return boolean 为空则返回true
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmptyAll(Object o) {
        if (null == o)
            return true;
        //字符串
        if(o instanceof CharSequence)
            return !org.springframework.util.StringUtils.hasText((CharSequence) o);

        if (o instanceof Collection) {
            return ((Collection) o).isEmpty();
        }
        if (o instanceof Map) {
            return ((Map) o).isEmpty();
        }
        if (o.getClass().isArray()) {
            if (Array.getLength(o) == 0) {
                return true;
            }
        }


        return false;
    }



    /**
     * 判断对象不为null 并且不为""
     * @param obj
     * @return
     */
    public static boolean isNotNullEmpty(Object obj) {
        return !isEmptyAll(obj);
    }
}
