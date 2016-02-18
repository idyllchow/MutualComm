package com.sponia.foundationmoudle.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @packageName com.sponia.foundationmoudle.utils
 * @description 时间处理类
 * @date 15/9/10
 * @auther shibo
 */
public class TimeUtil {

    private static final String TAG = "TimeUtil";
    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_ALL = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_MS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT_HOUR_MINUTE = "HH:mm";

    public static String[] WEEK_NAME = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public static String getLocalDate(String utcTime) {
        if (TextUtils.isEmpty(utcTime)) {
            return "";
        }
        return utc2Local(utcTime, DATE_FORMAT_ALL, DATE_FORMAT_DEFAULT);
    }

    public static String utc2Local(String utcTime, String utcTimePattern,
                                   String localTimePattern) {
        SimpleDateFormat utcFormat = new SimpleDateFormat(utcTimePattern, Locale.getDefault());
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormat.parse(utcTime);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
        if (gpsUTCDate != null) {
            SimpleDateFormat localFormat = new SimpleDateFormat(localTimePattern, Locale.getDefault());
            localFormat.setTimeZone(TimeZone.getDefault());
            return localFormat.format(gpsUTCDate.getTime());
        } else {
            return "";
        }
    }

    public static long getLocalTime(String utcTime, String utcTimePattern) {
        SimpleDateFormat utcFormat = new SimpleDateFormat(utcTimePattern, Locale.getDefault());
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormat.parse(utcTime);
            return gpsUTCDate.getTime();
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
        return 0;
    }

    public static String getLocalTime(long time, String timePattern) {
        SimpleDateFormat localFormat = new SimpleDateFormat(timePattern, Locale.getDefault());
        localFormat.setTimeZone(TimeZone.getDefault());
        return localFormat.format(time);
    }

    public static String getWeekName(String utcTime) {
        SimpleDateFormat utcFormat = new SimpleDateFormat(DATE_FORMAT_ALL, Locale.getDefault());
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormat.parse(utcTime);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
        if (null != gpsUTCDate) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(gpsUTCDate);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayOfWeek < 0) {
                dayOfWeek = 0;
            }
            return WEEK_NAME[dayOfWeek];
        }
        return WEEK_NAME[0];
    }

    /**
     * 获取小时分钟
     * @param utcTime
     * @return
     */
    public static String getHourMinute(String utcTime) {
        if (TextUtils.isEmpty(utcTime)) {
            return "";
        }
        return utc2Local(utcTime, DATE_FORMAT_ALL, DATE_FORMAT_HOUR_MINUTE);
    }

//    /**
//     * 将当前时间格式化输出至微秒
//     *
//     * @return String 当前时间至毫秒2015-02-28-09-24-49
//     */
//    public static final String getTimeFormatMSS() {
//        SimpleDateFormat simpleDataFormat = new SimpleDateFormat(
//                "yyyy-MM-dd hh:mm:ss.SSS");
//        return simpleDataFormat.format(new Date(System.currentTimeMillis()));
//    }

    /**
     * 提供时间转化至毫秒的方法
     *
     * @param date 时间
     * @return String 至毫秒的时间2015-02-28-09-23-07
     */
//    public static final String getTimeFormatMS(Date date) {
//        if (null == date) {
//            return null;
//        }
//        ;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
//                "yyyy-MM-dd hh-mm-ss");
//        return simpleDateFormat.format(date);
//    }

    /**
     * 将当前时间格式化输出至毫秒
     *
     * @return String 当前时间至微秒2015-02-28 09:21:52
     */
    public static final String getTimeFormatMicrosecond(long time) {
        SimpleDateFormat simpleDataFormat = new SimpleDateFormat(DATE_FORMAT_MS);
        simpleDataFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDataFormat.format(new Date(time));
    }

//    /**
//     * 将当前时间格式化输出至微秒
//     *
//     * @return String 当前时间至微秒2015-02-28 09:21:52.585
//     */
//    public static final String getTimeFormatMicrosecond() {
//        SimpleDateFormat simpleDataFormat = new SimpleDateFormat(
//                "yyyy-MM-dd hh:mm:ss.SSS");
//        return simpleDataFormat.format(new Date(System.currentTimeMillis()));
//    }
//
//    /**
//     * 提供时间格式化输出至分钟
//     *
//     * @param date 时间
//     * @return String 至分钟的时间 2015-02-28 09:18
//     */
//    public static final String getTimeFormatYMDHM(Date date) {
//        if (null == date) {
//            return null;
//        }
//        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
//    }

    /**
     * 提供时间格式化输出为UTC年月日
     *
     * @param date 时间
     * @return String 年月日2015-02-28
     */
    public static final String getTimeformatYMDUTC(Date date) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat simpleDataFormat = new SimpleDateFormat(DATE_FORMAT_DEFAULT);
        simpleDataFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDataFormat.format(date);
    }

    public static final String getTimeformatYMD(Date date) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat simpleDataFormat = new SimpleDateFormat(DATE_FORMAT_DEFAULT);
        simpleDataFormat.setTimeZone(TimeZone.getDefault());
        return simpleDataFormat.format(date);
    }

    /**
     * 提供得到当前年份的方法
     *
     * @return 当前年份:2015
     */
    public static final int getYear() {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyymmdd");
        String nowStr = format.format(now);
        return Integer.parseInt(nowStr.substring(0, 4));
    }

    /**
     * 提供得到当前月份的方法
     *
     * @return int 当前月份:2
     */
    public static final int getMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int month = calendar.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 提供得到当前天份的方法
     *
     * @return int 当前天份:27
     */
    public static final int getDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    /**
     * 提供得到当前年月的方法
     *
     * @return String 当前年月:201502
     */
    public static final String getYearMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String year = (calendar.get(Calendar.YEAR)) + "";
        int month = calendar.get(Calendar.MONTH) + 1;
        String monthstr = month + "";
        if (month < 10) {
            monthstr = "0" + month;
        }
        String nowStr = year + monthstr;
        return nowStr;
    }

    /**
     * 将当前时间格式化输出日
     *
     * @return String 当前时间至日:2015-02-27
     */
    public static final String getDateFormatMD() {
        SimpleDateFormat simpleDataFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDataFormat.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取时分秒
     * @param time
     * @return
     */
    public static final String getDateFormatHMS(long time) {
        SimpleDateFormat simpleDataFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        return simpleDataFormat.format(new Date(time));
    }

    /**
     * 转换时间为分秒
     * @param time
     * @return
     */
    public static final String getMSTimeFormat(long time) {
        if (time == 0) {
            return "";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "mm:ss");
        return simpleDateFormat.format(time);
    }

    /**
     * 把时间值转换时间为分秒
     * @param time
     * @return
     */
    public static final String getMSTimeFormat(float time) {
        if (time == 0) {
            return "";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "mm:ss");
        return simpleDateFormat.format(time);
    }

    /**
     * 获取date
     * @param str
     * @return
     */
    public static final Date getDate(String str) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date();
        try {
            d = df.parse(str);
        } catch (ParseException e) {
            LogUtil.defaultLog(e);
        }
        return d;
    }

    public static long getUTCTimeStamp(String utcTime) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_ALL);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = format.parse(utcTime);
            return date.getTime();
        } catch (ParseException e) {
            LogUtil.defaultLog(e);
        }
        return 0;
    }
}
