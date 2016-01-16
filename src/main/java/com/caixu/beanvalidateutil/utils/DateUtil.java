package com.caixu.beanvalidateutil.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by xu on 2016/1/16.
 */
public class DateUtil {
    /**包含了大部分的日期格式*/
    public static final Map<String, String> dateRegFormat = new HashMap<>();
    static {
        dateRegFormat.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D*$", "yyyy-MM-dd-HH-mm-ss");//2014年3月12日 13时5分34秒，2014-03-12 12:05:34，2014/3/12 12:5:34
        dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd-HH-mm");//2014-03-12 12:05
        dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd-HH");//2014-03-12 12
        dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd");//2014-03-12
        dateRegFormat.put("^\\d{4}\\D+\\d{2}$", "yyyy-MM");//2014-03
        dateRegFormat.put("^\\d{4}$", "yyyy");//2014
        dateRegFormat.put("^\\d{14}$", "yyyyMMddHHmmss");//20140312120534
        dateRegFormat.put("^\\d{12}$", "yyyyMMddHHmm");//201403121205
        dateRegFormat.put("^\\d{10}$", "yyyyMMddHH");//2014031212
        dateRegFormat.put("^\\d{8}$", "yyyyMMdd");//20140312
        dateRegFormat.put("^\\d{6}$", "yyyyMM");//201403
        dateRegFormat.put("^\\d{2}\\s*:\\s*\\d{2}\\s*:\\s*\\d{2}$", "yyyy-MM-dd-HH-mm-ss");//13:05:34  拼接当前日期
        dateRegFormat.put("^\\d{2}\\s*:\\s*\\d{2}$", "yyyy-MM-dd-HH-mm");//13:05  拼接当前日期
        dateRegFormat.put("^\\d{2}\\D+\\d{1,2}\\D+\\d{1,2}$", "yy-MM-dd");//14.10.18(年.月.日)
        dateRegFormat.put("^\\d{1,2}\\D+\\d{1,2}$", "yyyy-dd-MM");//30.12(日.月) 拼接当前年份
        dateRegFormat.put("^\\d{1,2}\\D+\\d{1,2}\\D+\\d{4}$", "dd-MM-yyyy");//12.21.2013(日.月.年)
    }

    /**各种字符串日期格式的格式化*/
    public static Date allStr2Date(String dateStr) throws Exception{
        String curDate =new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //DateFormat formatter1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat formatter2;
        String dateReplace;
        // String strSuccess="";

        try {
            for (Map.Entry<String,String> entry : dateRegFormat.entrySet()){
                String key= entry.getKey();
                String value=entry.getValue();
                if (Pattern.compile(key).matcher(dateStr).matches()){
                    formatter2 = new SimpleDateFormat(value);
                    if (key.equals("^\\d{2}\\s*:\\s*\\d{2}\\s*:\\s*\\d{2}$")
                            || key.equals("^\\d{2}\\s*:\\s*\\d{2}$")) {//13:05:34 或 13:05 拼接当前日期
                        dateStr = curDate + "-" + dateStr;
                    } else if (key.equals("^\\d{1,2}\\D+\\d{1,2}$")) {//21.1 (日.月) 拼接当前年份
                        dateStr = curDate.substring(0, 4) + "-" + dateStr;
                    }
                    dateReplace = dateStr.replaceAll("\\D+", "-");
                    // strSuccess = formatter1.format(formatter2.parse(dateReplace));
                    //System.out.println(strSuccess);
                    return formatter2.parse(dateReplace);
                    //break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParseException("日期格式无效>>"+dateStr,0);
        }
        throw new ParseException("日期格式无效>>"+dateStr,0);
    }

    /**字符串转为日期时间，只接受形如 1985-12-26 23:23:31格式*/
    public static Date str2DateTime(String dateStr){
        DateTime dt2 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(dateStr);
        return dt2.toDate();
    }
    public static Date str2DateTime(String dateStr,String pattern){
        DateTime dt2 = DateTimeFormat.forPattern(pattern).parseDateTime(dateStr);
        return dt2.toDate();
    }
}
