package com.caixu.beanvalidateutil.imlclass;


import com.caixu.beanvalidateutil.annotations.*;
import com.caixu.beanvalidateutil.utils.DateUtil;
import com.caixu.beanvalidateutil.utils.ObjectUtil;
import com.caixu.beanvalidateutil.vinterface.ValidateInterface;
import com.google.common.base.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xu on 2015/12/4.
 */
public class ValidateRule {
    //static Logger logger=Logger.getLogger(ValidateRule.class);
    public static final Map<Object, Predicate> map = new HashMap<>();
    static {
        map.put(NotNull.class, new Predicate<Map>() {
            @Override
            public boolean apply(Map mo) {
                Object o = mo.get("object");
                Object source = mo.get("source");
                Object bo = mo.get("annotation");
                if ("vali2".equals(source)) {
                    DataValidate notNull = (DataValidate) bo;
                    if (!notNull.notNull()) {
                        return false;
                    }
                }
                return ObjectUtil.isEmptyAll(o);
            }
        });

        //长度校验
        map.put(Length.class, new Predicate<Map>() {
            @Override
            public boolean apply(Map mo) {
                String o = mo.get("object") == null ? "" : (String) mo.get("object");
                Method[] ms = (Method[]) mo.get("methods");
                int minSize = 0;
                int maxSize = 0;
                Object bo = mo.get("annotation");
                Object source = mo.get("source");
                if ("vali2".equals(source)) {
                    DataValidate tarClass = (DataValidate) mo.get("annotation");
                    String limit = StringUtils.substringBetween(tarClass.lengthLimit(), "{", "}");
                    if (StringUtils.isBlank(limit)) {
                        return false;
                    }
                    String[] oc = limit.split(",");
                    if (oc.length == 1) {
                        minSize = 0;
                        maxSize = Integer.valueOf(oc[1]);
                    } else {
                        minSize = Integer.valueOf(oc[0]);
                        maxSize = Integer.valueOf(oc[1]);
                    }
                } else {
                    Length tarClass = (Length) mo.get("annotation");
                    minSize = tarClass.minSize();
                    maxSize = tarClass.maxSize();
                }

                String mg = "";

                int len = o.length();
                boolean b = true;
                if (len < minSize) {
                    b = false;
                    mg += "最小长度应该为" + minSize + ";";
                }
                if (len > maxSize) {
                    b = false;
                    mg += "最大长度应该为" + maxSize + ";";
                }
                if (!b) {
                    mg += "当前长度为" + len;
                    //Annotation an= (Annotation) mo.get("annotation");
                    return true;
                }
                return false;
            }
        });

        //数据类型验证验证字符串是否可以转为指定
        map.put(Type.class, new Predicate<Map>() {
            @Override
            public boolean apply(Map mo) {
                Object o = mo.get("object");
                if (o==null || "".equals(o)){return false;}
                Method[] ms = (Method[]) mo.get("methods");
                Class<?> t;
                String datePatt="";

                Object source = mo.get("source");
                if ("vali2".equals(source)) {
                    DataValidate tarClass = (DataValidate) mo.get("annotation");
                    Class<?>[] t1 = tarClass.type();
                    if (t1.length == 0) {
                        return false;
                    }
                    t = t1[0];
                    datePatt=tarClass.datePattern();
                } else {
                    Type tarClass = (Type) mo.get("annotation");
                    t = tarClass.type();
                }

                String mg = "";
                boolean b = true;
                if (!(o instanceof String)) {
                    b = false;
                    mg += "只能验证字符串类型...";
                }
                String s = (String) o;
                if (StringUtils.isBlank(s)){
                    return false;
                }
                if (Number.class.equals(t)){
                    b= NumberUtils.isNumber(s);
                    return !b;
                }else
                //是否是数字类型
                if (Number.class.isAssignableFrom(t)) {
                    //  if (Number.class.equals(t.getGenericSuperclass())){
                    try {
                        Method m = t.getDeclaredMethod("valueOf", String.class);
                        Object ooo = m.invoke(t, s);
                    } catch (NoSuchMethodException e) {
                        b = false;
                        mg += "你指定的类型没有valueOf方法，不能判断...";
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        b = false;
                        mg += "转换过程中出现异常";
                    } catch (Exception e) {
                        b = false;
                        mg += "转换过程中出现异常...";
                    }

                    if (!b) {
                        return true;
                    }
                }

                //是否是日期类型
                //if (Date.class.getName().equals(t.getName())){
                if (Date.class.isAssignableFrom(t)) {
                    try {
                        Date date=null;
                        if (StringUtils.isBlank(datePatt)){
                            date = DateUtil.allStr2Date(s);
                        }else {
                            date=DateUtil.str2DateTime(s,datePatt);
                        }

                        if (date != null) {
                            return false;
                        }
                    } catch (Exception e) {
                        return true;
                    }
                }


                return false;
            }
        });

        map.put("numberRang", new Predicate<Map>() {
            @Override
            public boolean apply(Map mo) {
                Object o = mo.get("object");
                if (o==null || "".equals(o)){return false;}
                DataValidate tarClass = (DataValidate) mo.get("annotation");
                String limit = StringUtils.substringBetween(tarClass.numberRang(), "{", "}");
                if (StringUtils.isBlank(limit)) {
                    return false;
                }
                String[] oc = limit.split(",");
                String minV="0";
                String maxV="0";
                if (oc.length == 1) {
                    minV = "0";
                    maxV = oc[1];
                } else {
                    minV = oc[0];
                    maxV = oc[1];
                }

                String ov="";
                if (o instanceof String){
                    ov= (String) o;
                    if (!NumberUtils.isNumber(ov)){
                        return true;
                    }
                }else if (o instanceof Number){
                    ov=o.toString();
                }else {
                    return true;
                }

                BigDecimal min=new BigDecimal(minV);
                BigDecimal max=new BigDecimal(maxV);
                BigDecimal v=new BigDecimal(ov);

                if (v.compareTo(min)==-1){
                    return true;
                }
                if (v.compareTo(max)==1){
                    return true;
                }

                return false;
            }
        });


        //正则校验
        map.put(RegularEx.class, new Predicate<Map>() {
            @Override
            public boolean apply( Map mo) {
                Object o = mo.get("object");
                if (o==null || "".equals(o)){return false;}
                Method[] ms = (Method[]) mo.get("methods");
                Object source = mo.get("source");
                String reg;
                if ("vali2".equals(source)) {
                    DataValidate tarClass = (DataValidate) mo.get("annotation");
                    reg = tarClass.regEx();
                    if (StringUtils.isBlank(reg)) {
                        return false;
                    }
                } else {
                    RegularEx tarClass = (RegularEx) mo.get("annotation");
                    reg = tarClass.regEx();
                }

                String s = (String) o;

                Pattern p = Pattern.compile(reg);
                Matcher m = p.matcher(s);

                return !m.matches();
            }
        });


        map.put("myFunction", new Predicate<Map>() {

            @Override
            public boolean apply( Map mo) {
                Object o=mo.get("object");
                Object oField=mo.get("objectField");
                Object oClass=mo.get("objectClass");
                if (o==null || "".equals(o)){return false;}
                Method[] ms= (Method[]) mo.get("methods");
                Object source=mo.get("source");

                DataValidate tarClass= (DataValidate) mo.get("annotation");
                Class<? extends ValidateInterface>[] t1=tarClass.myFunction();
                if (t1.length==0) {
                    return false;
                }

                for (Class<? extends ValidateInterface> vl:t1){
                    try {
                        ValidateInterface v= vl.newInstance();
                        boolean bt=v.apply(o,oField,oClass);
                        if (!bt){
                           // bb=false;
                            return true;
                        }
                    } catch (Exception e) {
                        return true;
                    }
                }
                return false;
            }
        });

    }

   public static final Map<String,Object> annMap=new HashMap<>();
    static {
        annMap.put("notNull",NotNull.class );//非空验证
        annMap.put("lengthLimit", Length.class);//长度验证
        annMap.put("type", Type.class);//类型验证
        annMap.put("regEx", RegularEx.class);//正则
        annMap.put("myFunction","myFunction");//自定义方法
        annMap.put("numberRang","numberRang");//数字范围
        //annMap.put("isNeedDate", IsDateFomat.class);
    }



}
