package com.caixu.beanvalidateutil.vinterface;

/**
 * Created by xu on 2015/12/11.
 */
public interface ValidateInterface {
    /**
     * 需要实现的主方法
     * 第一个是字段值，第二个是field对象,第三个值是bean对象
     * @param t
     * @param <T>
     * @return
     */
    <T,E,C> boolean apply(T t, E e, C c);

    /**
     * 给这个方法取一个名字
     * @return
     */
    String getName();
}
