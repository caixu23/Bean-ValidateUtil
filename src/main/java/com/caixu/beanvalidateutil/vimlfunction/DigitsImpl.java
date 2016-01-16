package com.caixu.beanvalidateutil.vimlfunction;

import com.caixu.beanvalidateutil.vinterface.ValidateInterface;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * Created by xu on 2015/12/29.
 * 是否是整数
 */
public class DigitsImpl implements ValidateInterface {
    @Override
    public <T, E,C> boolean apply(T t, E e,C c) {
        String v=(String)t.toString();
        if (v.endsWith(".0")){
            v= StringUtils.replace(v,".0","");
        }
        return NumberUtils.isDigits(v);
    }

    @Override
    public String getName() {
        return null;
    }
}
