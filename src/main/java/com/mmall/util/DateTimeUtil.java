package com.mmall.util;


import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * @author : Administrator
 * @create 2018-04-13 21:13
 */
public class DateTimeUtil {
    
    // str - date
    public static final String STANDARD_FROMAT = "yyyy-MM-dd HH:mm:ss";


    public static Date strToDate(String dataTimeStr, String formateStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formateStr);

        DateTime dateTime = dateTimeFormatter.parseDateTime(dataTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date, String formateStr) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formateStr);
    }

    public static Date strToDate(String dataTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FROMAT);

        DateTime dateTime = dateTimeFormatter.parseDateTime(dataTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FROMAT);
    }

}
