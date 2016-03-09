package com.caixu.beanvalidateutil.imlclass;

import com.caixu.beanvalidateutil.annotations.DataValidate;
import com.caixu.beanvalidateutil.utils.ObjectUtil;
import com.google.common.base.Predicate;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import sun.reflect.misc.FieldUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xu on 2015/12/4.
 */
public class BeanValidateUtil {

    /**验证List*/
    public static <T> VResult vali(List<T> ts)throws Exception{
        List<VVResult> vvResults = new ArrayList<>();
        for (int i=0;i<ts.size();i++){
            VResult vResult=vali(ts.get(i),String.valueOf(i));
            if (!vResult.isRelult()){
                vvResults.addAll(vResult.getVvResult());
            }
        }
        //Collections.reverse(vvResults);
        VResult vResult=new VResult();
        if (vvResults.isEmpty()){
            vResult.setRelult(true);
        }else {
            vResult.setRelult(false);
            vResult.setVvResult(vvResults);
            vResult.setMessgae("总共"+vvResults.size()+"个异常");
        }
        return vResult;
    }

    /**验证单个实体类(多注解形式)*/
    public static <T> VResult vali(T t,String... rowN) throws Exception {
        List<VVResult> vvResults = new ArrayList<>();
        Field[] fieldArr = FieldUtil.getDeclaredFields(t.getClass());
        for (int i = 0; i < fieldArr.length; i++) {
            fieldArr[i].setAccessible(true);
            Object oo = fieldArr[i].get(t);
            Annotation as[] = AnnotationUtils.getAnnotations(fieldArr[i]);
            for (Annotation annotation : as) {
                Predicate p = ValidateRule.map.get(annotation.annotationType());
                Map map=new HashMap();
                Method[] ms = annotation.getClass().getDeclaredMethods();
                map.put("methods",ms);
                map.put("object",oo);
                map.put("annotation",annotation);

                if (p.apply(map)) {
                    String message = "nomessage";
                    Method gg = null;
                    try {
                        gg = annotation.getClass().getDeclaredMethod("message");
                    } catch (NoSuchMethodException e) {
                        //没有这个方法也不需要做任何操作，返回默认值即可
                    }
                    if (gg != null) {
                        String v= BeanUtils.getProperty(t,fieldArr[i].getName());
                        message = fieldArr[i].getName()+ ">>>" + (String) gg.invoke(annotation);
                    }
                    VVResult vvResult = new VVResult();
                    if (ObjectUtil.isEmptyAll(rowN)){
                        vvResult.setRowIndex(1);
                    }else {
                        vvResult.setRowIndex(Integer.valueOf(rowN[0]));
                    }
                    vvResult.setColumnIndex(i);
                    vvResult.setMessage(message);
                    vvResults.add(vvResult);
                }
            }
        }

        VResult vResult=new VResult();
        if (vvResults.isEmpty()){
            vResult.setRelult(true);
        }else {
            vResult.setRelult(false);
            vResult.setVvResult(vvResults);
            vResult.setMessgae("总共"+vvResults.size()+"个异常");
        }

        return vResult;
    }



    public static <T> VResult vali2(List<T> ts,String... startIndex)throws Exception{
        List<VVResult> vvResults = new ArrayList<>();

        if (ObjectUtil.isEmptyAll(ts)){
            VResult vResult=new VResult();
            VVResult vvResult12=new VVResult();
            vvResult12.setMessage("没有需要导入的记录");
            vvResults.add(vvResult12);

            vResult.setVvResult(vvResults);
            vResult.setRelult(false);
            return vResult;
        }

        for (int i=0;i<ts.size();i++){
            VResult vResult=new VResult();
            if (ObjectUtil.isNotNullEmpty(startIndex)){
                vResult=vali2(ts.get(i),String.valueOf(i),startIndex[0]);
            }else {
                vResult=vali2(ts.get(i),String.valueOf(i));
            }
            if (!vResult.isRelult()){
                vvResults.addAll(vResult.getVvResult());
            }
        }
        //Collections.reverse(vvResults);
        VResult vResult=new VResult();
        if (vvResults.isEmpty()){
            vResult.setRelult(true);
        }else {
            vResult.setRelult(false);
            vResult.setVvResult(vvResults);
            vResult.setMessgae("总共"+vvResults.size()+"个异常");
        }
        return vResult;
    }
    public static<T> VResult vali2(T t, String... rowN) throws Exception{
        List<VVResult> vvResults = new ArrayList<>();
        VResult vResult=new VResult();
        Field[] fieldArr = FieldUtil.getDeclaredFields(t.getClass());
        for (int i = 0; i < fieldArr.length; i++) {
            fieldArr[i].setAccessible(true);
            Object oo = fieldArr[i].get(t);//值
            Annotation annotation= AnnotationUtils.getAnnotation(fieldArr[i],DataValidate.class);
            if (annotation==null){continue;}
            Map<String,Object> attrs= AnnotationUtils.getAnnotationAttributes(annotation);

            String message="";
            for (Map.Entry entry:attrs.entrySet()){
                Object k=ValidateRule.annMap.get(entry.getKey());
                Predicate p = ValidateRule.map.get(k);
                if (p==null){continue;}
                Map map=new HashMap();
                map.put("methods",annotation.getClass().getDeclaredMethods());
                map.put("object",oo);//值
                map.put("objectField",fieldArr[i]);//字段
                map.put("objectClass",t);//整个类
                map.put("annotation",annotation);
                map.put("source","vali2");
                if (p.apply(map)) {
                    Method gg = null;
                    try {
                        gg = annotation.getClass().getDeclaredMethod(entry.getKey()+"Message");
                    } catch (NoSuchMethodException e) {
                        //没有这个方法也不需要做任何操作，返回默认值即可
                    }
                    if (gg != null) {
                        String v= BeanUtils.getProperty(t,fieldArr[i].getName());
                        if (StringUtils.isNotBlank(v)&&v.length()>21){
                            v=StringUtils.substring(v,0,20)+"...";
                        }
                        message +=  "[{title}]值为["+v+ "]>>>[" + (String) gg.invoke(annotation)+"]; ";
                    }
                }
            }


            if (StringUtils.isBlank(message)){

            }else {
                vResult.setRelult(false);
                VVResult vvResult=new VVResult();
                if (rowN!=null&&rowN.length>0){
                    vvResult.setRowIndex(Integer.valueOf(rowN[0]) );
                    vvResult.setColumnIndex(Integer.valueOf(i));
                    Integer ii=Integer.valueOf(rowN[0]);
                    if (rowN.length>1){
                        ii=ii+Integer.valueOf(rowN[1])+1;
                    }
                    vvResult.setMessage("索引号为:第"+ii+"行,第"+(i+1)+"列>>"+message+"; ");
                }else {
                    vvResult.setRowIndex(Integer.valueOf(0) );
                    vvResult.setColumnIndex(Integer.valueOf(i));
                    vvResult.setMessage(message+"; ");
                }
                vvResults.add(vvResult);
            }
        }
        if (vvResults.isEmpty()){
            vResult.setRelult(true);
        }
        vResult.setVvResult(vvResults);
        return vResult;
    }



}