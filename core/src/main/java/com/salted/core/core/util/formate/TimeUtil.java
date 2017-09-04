package com.salted.core.core.util.formate;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by xinyuanzhong on 16/7/9.
 * <p>
 * 2016
 * 09月01日
 * 周六
 * 08:00-10:00
 * 08:00
 * 08:00-24:00
 * 10 (小时)
 */
@SuppressWarnings({"PMD.GodClass", "PMD.ExcessivePublicCount",
        "PMD.AvoidDuplicateLiterals", "PMD.AvoidLiteralsInIfCondition",
        "PMD.AvoidReassigningParameters"})
public final class TimeUtil {
    private TimeUtil() {

    }

    public static final int ONE_DAY_TIME_LONG = 86400000;

    /**
     * 取从某一日期开始之后的x天之后的日期
     * string begin="1970-01-06"
     * string out="1976-01-06"
     *
     * @param begin,fromDayNum
     * @return
     */
    public static String getFromTodayDate(String begin, int fromDayNum) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
        Date date = new Date();
        try {
            date = sdf.parse(begin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        calendar.add(Calendar.DATE, fromDayNum);
        Date date1 = calendar.getTime();

        return sdf.format(date1);
    }

    /**
     * 取从某一日期开始之后的X个月之后的日期
     * string begin="1970-01-06"
     * string out="1976-01-06"
     *
     * @param begin,fromDayNum
     * @return
     */
    public static String getFromMonthDate(String begin, int fromMonthNum) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
        Date date = sdf.parse(begin, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        calendar.add(Calendar.MONTH, fromMonthNum);
        Date date1 = calendar.getTime();

        return sdf.format(date1);
    }

    /**
     * 将日期转换成时间戳
     * tring time="1970-01-06 11:45"
     *
     * @param time
     * @return
     */
    public static long transDayToTimeStamp(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long timeStamp = 0;
        try {
            Date date = format.parse(time);
            timeStamp = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    /**
     * 将日期转换成时间戳
     * tring time="1970-01-06"
     *
     * @param time
     * @return
     */
    public static long transDateToTimeStamp(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long timeStamp = 0;
        try {
            Date date = format.parse(time);
            timeStamp = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    /**
     * 将时间秒值格式化为时间
     * // DateUtil.getSomeDay(timeS, "yyyy-MM-dd EEEE HH:mm")
     */
    public static String formatDate(String second, String formatType) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        long parseLong = 0;
        if (second != null && second.equals("")) {
            try {
                parseLong = Long.parseLong(second) * 1000;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("格式化时间出错");
            }

            return sdf.format(new Date(parseLong));
        } else {
            return null;
        }
    }

    /**
     * 获取当前时间
     *
     * @return 返回当前时间，格式为：2013-05-26 13:26:36
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = "";
        try {
            time = sdf.format(new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    private static boolean isYesterday(String messageDate) {
        String date = formatDate(messageDate, "yyyy-MM-dd");
        String now = formatDate((System.currentTimeMillis() - ONE_DAY_TIME_LONG) / 1000 + "", "yyyy-MM-dd");
        return TextUtils.equals(date, now);
    }

    private static boolean isToday(String mesFormatDate, String nowFormatDate) {
        return TextUtils.equals(mesFormatDate.substring(0, 11), nowFormatDate.substring(0, 11));
    }

    private static boolean isThisYear(String mesFormatDate, String nowFormatDate) {
        return TextUtils.equals(mesFormatDate.substring(0, 5), nowFormatDate.substring(0, 5));
    }

    /**
     * 将Date格式化为时间
     */
    public static String formatDate(Date date, String formatType) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        return sdf.format(date);

    }

    /*--------------------------------------------------------------------------------------------*/

    /**
     * 得时间戳中的天，补0
     *
     * @param stamps
     * @return
     */
    // 01
    public static String getDayFromTimeStamp(String stamps) {
        if (TextUtils.isEmpty(stamps)) {
            return "";
        }
        long data = getMilliSecond(stamps);
        SimpleDateFormat format = new SimpleDateFormat("dd");
        stamps = format.format(data);
        return stamps;
    }

    /**
     * 得时间戳中的月，补0
     *
     * @param stamps
     * @return
     */
    // 09
    public static String getMonthFromTimeStamp(String stamps) {
        if (TextUtils.isEmpty(stamps)) {
            return "";
        }
        long data = getMilliSecond(stamps);
        SimpleDateFormat format = new SimpleDateFormat("MM");
        stamps = format.format(data);
        return stamps;
    }

    /**
     * 得时间戳中的月，不补0，比如：8
     *
     * @param stamps
     * @return
     */
    // 9
    public static String getMonth(String stamps) {
        if (TextUtils.isEmpty(stamps)) {
            return "";
        }
        long data = getMilliSecond(stamps);
        SimpleDateFormat format = new SimpleDateFormat("M");
        stamps = format.format(data);
        return stamps;
    }

    /**
     * 时间戳转换后的格式: 8月27日 周六 08:00-10:00
     *
     * @param startTime
     * @return
     */
    // 9月1日 周六 08:00-10:00
    public static String getMonthDayWeekHourMinuteByStamps(String startTime, String endTime) {
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            return "";
        }
        String result = "";
        long stamp = getMilliSecond(startTime);
        SimpleDateFormat format = new SimpleDateFormat("M月d日 E HH:mm");
        String formateStartTime = format.format(stamp);
        stamp = getMilliSecond(endTime);
        format.applyPattern("-HH:mm");
        String formateEndTime = format.format(stamp);
        if ("-00:00".equals(formateEndTime)) {
            formateEndTime = "-24:00";
        }
        result = formateStartTime + formateEndTime;
        if (result.contains("星期")) {
            result = result.replaceFirst("星期", "周");
        }
        return result;
    }

    /**
     * 时间戳转换后的格式: 2016-09-26 08:00
     *
     * @param stamps
     * @return
     */
    // 2016-09-01 08:00
    public static String getDetailDateByStamps(String stamps) {
        if (TextUtils.isEmpty(stamps)) {
            return "";
        }
        long data = getMilliSecond(stamps);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        stamps = format.format(data);

        return stamps;
    }

    /**
     * 时间戳转换后的格式: 周六 08:00-10:00
     *
     * @param stamps
     * @return
     */
    // 周六 08:00-10:00
    public static String getWeekDateByStamps(String stamps) {
        if (TextUtils.isEmpty(stamps)) {
            return "";
        }
        long data = getMilliSecond(stamps);
        SimpleDateFormat format = new SimpleDateFormat("E HH:mm");
        stamps = format.format(data);
        data = data + 1000 * 60 * 60 * 2;
        format.applyPattern("- HH:mm");
        stamps = stamps + format.format(data);
        if (stamps.contains("星期")) {
            stamps = stamps.replaceFirst("星期", "周");
        }
        return stamps;
    }

    /**
     * 时间戳转换后的格式: 周六 08:00-10:00
     *
     * @param startTime 开始上课时间
     * @param endTime   结束上课时间
     * @return
     */
    // 周六 08:00-10:00
    public static String getStartAndEndByStamps(String startTime, String endTime) {
        if (TextUtils.isEmpty(startTime)) {
            return "";
        }
        String result = "";
        long stamp = getMilliSecond(startTime);
        SimpleDateFormat format = new SimpleDateFormat("E HH:mm");
        String formateStartTime = format.format(stamp);
        stamp = getMilliSecond(endTime);
        format.applyPattern("- HH:mm");
        String formateEndTime = format.format(stamp);
        if ("- 00:00".equals(formateEndTime)) {
            formateEndTime = "- 24:00";
        }
        result = formateStartTime + formateEndTime;
        if (result.contains("星期")) {
            result = result.replaceFirst("星期", "周");
        }
        return result;
    }

    /**
     * 时间戳转换后的格式: 08月27日 周六
     *
     * @param stamps
     * @return
     */
    // 09月01日 周六
    public static String getDateByStamps(String stamps) {

        if (TextUtils.isEmpty(stamps)) {
            return "";
        }

        long data = getMilliSecond(stamps);
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 E");
        stamps = format.format(data);
        if (stamps.contains("星期")) {
            stamps = stamps.replaceFirst("星期", "周");
        }
        return stamps;
    }

    /**
     * 时间戳转换后的格式: 08-27 周六
     *
     * @param stamps
     * @return
     */
    // 09-01 周六
    public static String getLinkDayByStamps(String stamps) {

        if (TextUtils.isEmpty(stamps)) {
            return "";
        }

        long data = getMilliSecond(stamps);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd E");
        stamps = format.format(data);
        if (stamps.contains("星期")) {
            stamps = stamps.replaceFirst("星期", "周");
        }
        return stamps;
    }

    /**
     * 时间戳转换后的格式: 2016-08-27 周六
     *
     * @param stamps
     * @return
     */
    // 2016-09-01 周六
    public static String getYearMonthDayWeek(String stamps) {

        if (TextUtils.isEmpty(stamps)) {
            return "";
        }

        long data = getMilliSecond(stamps);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd E");
        stamps = format.format(data);
        if (stamps.contains("星期")) {
            stamps = stamps.replaceFirst("星期", "周");
        }
        return stamps;
    }

    /**
     * 时间戳转换后的格式: 08-27 08:00
     *
     * @param time
     * @return
     */
    // 09-01 08:00
    public static String getDateBySecondsStamp(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Long timeLong = getMilliSecond(time);
        Date date = new Date(timeLong);
        SimpleDateFormat ft = new SimpleDateFormat("MM-dd HH:mm");
        return ft.format(date);
    }

    /**
     * 时间戳转换后的格式: 08-27
     *
     * @param time
     * @return
     */
    // 09-01
    public static String getMonthDayBySecondsStamp(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Long timeLong = getMilliSecond(time);
        Date date = new Date(timeLong);
        SimpleDateFormat ft = new SimpleDateFormat("MM-dd");
        return ft.format(date);
    }

    /**
     * 时间戳转换后的格式: 08月27日
     *
     * @param time
     * @return
     */
    // 09月01日
    public static String getMonthAndDayByStamps(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Long timeLong = getMilliSecond(time);
        Date date = new Date(timeLong);
        SimpleDateFormat ft = new SimpleDateFormat("MM月dd日");
        return ft.format(date);
    }

    /**
     * 时间戳转换后的格式: 08月27日
     *
     * @param time
     * @return
     */
    // 09月01日
    public static String getNoZeroMonthAndDayByStamps(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Long timeLong = getMilliSecond(time);
        Date date = new Date(timeLong);
        SimpleDateFormat ft = new SimpleDateFormat("M月d日");
        return ft.format(date);
    }

    /**
     * 时间戳转换后的格式: 08月27日
     *
     * @param startTime
     * @param endTime
     * @return
     */
    // 09月01日
    public static String getMonthAndDayByStamps(String startTime, String endTime) {
        StringBuilder stringBuilder = new StringBuilder();
        String formatStartTime = getNoZeroMonthAndDayByStamps(startTime);
        String formatEndTime = getNoZeroMonthAndDayByStamps(endTime);
        stringBuilder.append(formatStartTime).append("-").append(formatEndTime);
        return stringBuilder.toString();
    }

    /**
     * 时间戳转换后的格式: 8月27日 周六
     *
     * @param time
     * @return
     */

    public static String getHanyuMonthAndDayByStamps(long time) {
        Date date = new Date(time);
        SimpleDateFormat ft = new SimpleDateFormat("M月d日 E");
        String formatDate = ft.format(date);
        if (formatDate.contains("星期")) {
            formatDate = formatDate.replaceFirst("星期", "周");
        }
        return formatDate;
    }

    /**
     * 时间戳转换后的格式: 2016年8月
     *
     * @param time
     * @return
     */
    // 2016年09月
    public static String getYearAndMonthByStamps(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Long timeLong = getMilliSecond(time);
        Date date = new Date(timeLong);
        SimpleDateFormat ft = new SimpleDateFormat("yyyy年MM月");
        return ft.format(date);
    }

    /**
     * 时间戳转换后的格式: 2016-8，月份不补0
     *
     * @param time
     * @return
     */
    // 2016-9
    public static String getYearAndMonthByStampsInStyle2(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Long timeLong = getMilliSecond(time);
        Date date = new Date(timeLong);
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-M");
        return ft.format(date);
    }

    /**
     * 时间戳转换后的格式: 2016-08，月份补0
     *
     * @param time
     * @return
     */
    // 2016-09
    public static String getYearAndMonthByStamp(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Long timeLong = getMilliSecond(time);
        Date date = new Date(timeLong);
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM");
        return ft.format(date);
    }

    /**
     * 时间戳转换后的格式: 2016年
     *
     * @param time
     * @return
     */
    // 2016年
    public static String getYearByStamps(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Long timeLong = getMilliSecond(time);
        Date date = new Date(timeLong);
        SimpleDateFormat ft = new SimpleDateFormat("yyyy年");
        return ft.format(date);
    }

    /**
     * 时间戳转换后的格式: 2016年
     *
     * @param time
     * @return
     */
    // 2016年
    public static String getTimeByPattern(String time, String pattern) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Long timeLong = getMilliSecond(time);
        Date date = new Date(timeLong);
        SimpleDateFormat ft = new SimpleDateFormat(pattern);
        return ft.format(date);
    }


    /**
     * 时间戳转换后的格式: 10:00
     *
     * @param time
     * @return
     */
    // 08:00
    public static String getHourAndSecondByStamps(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Long timeLong = getMilliSecond(time);
        Date date = new Date(timeLong);
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm");
        return ft.format(date);
    }

    /**
     * 00:00会自动转换为24:00
     */
    // 24:00
    public static String getAnotherHourAndSecondByStamps(String time) {
        String result = getHourAndSecondByStamps(time);
        if ("00:00".equals(result)) {
            result = "24:00";
        }
        return result;
    }

    /**
     * 时间戳转换后的格式: 10 (小时)
     *
     * @param time
     * @return
     */
    // 10 (小时)
    public static String getHourByStamps(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Long timeLong = getMilliSecond(time);
        Date date = new Date(timeLong);
        SimpleDateFormat ft = new SimpleDateFormat("HH");
        return ft.format(date);
    }

    /**
     * 时间戳转换后的格式: 10 (分钟)
     *
     * @param time
     * @return
     */
    // 10 (分钟)
    public static String getMinsByStamps(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Long timeLong = getMilliSecond(time);
        Date date = new Date(timeLong);
        SimpleDateFormat ft = new SimpleDateFormat("mm");
        return ft.format(date);
    }

    /**
     * 把服务器给的时间（秒）转换成Date对象
     *
     * @param second 服务器给的时间（秒）
     */
    public static Date getDate(String second) {
        Long timeLong = getMilliSecond(second);
        return new Date(timeLong);
    }

    @NonNull
    public static Long getMilliSecond(String second) {
        long l = 0;
        if (TextUtils.isEmpty(second)) return l;

        try {
            l = (Long.parseLong(second)) * 1000L;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return l;
    }

    /**
     * 传入开始时间和结束时间，如果结束时间是第二天0点，会自动转换为24点，也就是00:00会自动转换为24:00
     */
    // 08:00-24:00
    public static String getTimeDurationByStamps(String startTimeStamps, String endTimeStamps) {
        return getHourAndSecondByStamps(startTimeStamps) + "- " + getAnotherHourAndSecondByStamps(endTimeStamps);
    }

    /**
     * 时间戳转换后的格式: 08:00-10:00
     *
     * @param stamps
     * @return
     */
    public static String getTimeSegmentByStamp(String stamps, float duration) {
        if (TextUtils.isEmpty(stamps)) {
            return "";
        }
        long data = getMilliSecond(stamps);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        stamps = format.format(data);
        data = data + (int) (1000 * 60 * 60 * duration);
        format.applyPattern("- HH:mm");
        stamps = stamps + format.format(data);
        return stamps;
    }

    /**
     * 时间戳转换后的格式: 周六
     *
     * @param time
     * @return
     */
    // 周六
    public static String getWeekByStamps(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Long timeLong = getMilliSecond(time);
        Date date = new Date(timeLong);
        SimpleDateFormat ft = new SimpleDateFormat("E");
        String weekName = ft.format(date);
        if (weekName.contains("星期")) {
            weekName = weekName.replaceFirst("星期", "周");
        }
        return weekName;
    }

    /**
     * 判断当前日期是星期几
     *
     * @param stamps 修要判断的时间戳
     * @return 在一周内的位置
     * @Exception 发生异常
     */
    public static int getDayInWeekIndex(long stamps) {
        long time = stamps * 1000;
        Date date = new Date(time);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 时间戳转换后的格式: 2016-09-25
     *
     * @param timestamp
     * @return
     */
    // 2016-09-01
    public static String getYearMonthDayByStamps(String timestamp) {
        long time = getMilliSecond(timestamp);
        Date date = new Date(time);
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        return ft.format(date);
    }

    /**
     * 时间戳转换后的格式: 2016.09.25
     *
     * @param timestamp
     * @return
     */
    // 2016.09.01
    public static String getYearMonthDayByStampsDot(String timestamp) {
        long time = getMilliSecond(timestamp);
        Date date = new Date(time);
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd");
        return ft.format(date);
    }

    /**
     * 时间戳转换后的格式: 2016-09-25
     *
     * @param date
     * @return
     */
    // 2016-09-01
    public static String getYearMonthDayByDate(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        return ft.format(date);
    }

    /**
     * 时间戳转换后的格式: 2016年09月25日（月份会补0）
     *
     * @param timestamp
     * @return
     */
    // 2016年09月01日
    public static String getYearMonthDayWithWordsByStamps(String timestamp) {
        long time = Long.parseLong(timestamp);
        long timeLong = time * 1000L;
        Date date = new Date(timeLong);
        SimpleDateFormat ft = new SimpleDateFormat("yyyy年MM月dd日");
        return ft.format(date);
    }

    /**
     * 时间戳转换后的格式: 2016年9月25日（月份不补0）
     *
     * @param timestamp
     * @return
     */
    // 2016年9月01日
    public static String getYearSingleMonthDayWithWordsByStamps(String timestamp) {
        long time = Long.parseLong(timestamp);
        long timeLong = time * 1000L;
        Date date = new Date(timeLong);
        SimpleDateFormat ft = new SimpleDateFormat("yyyy年M月dd日");
        return ft.format(date);
    }

    /**
     * 返回 周六 08:10-10:10的List
     *
     * @param strings List时间戳 单位:秒
     * @return
     */
    public static List<String> getWeekHourDateListFromStamps(List<String> strings) {

        List<String> stringList = new ArrayList<>();
        if (strings != null && !strings.isEmpty()) {
            for (String s : strings) {
                stringList.add(getWeekDateByStamps(s));
            }
        }
        return stringList;
    }


    /**
     * 时间当天显示 mm:ss，昨天显示 昨天 mm:ss，昨天之前显示 月-日 mm:ss，非当前年份显示 年-月-日。小时使用24小时制，
     * 例如“14:53”；月日时分均显示两位数字，形如"mm-dd  mm:ss"，年份显示四位数字。
     *
     * @param seconds
     * @return
     */
    public static String getMessageItemDate(String seconds) {
        if (TextUtils.isEmpty(seconds) || "0".equals(seconds)) {
            return "";
        }
        String mesFormatDate = "";
        try {
            mesFormatDate = formatDate(seconds, "yyyy-MM-dd HH:mm");
            String nowFormatDate = getCurrentTime();
            if (isToday(mesFormatDate, nowFormatDate)) {
                mesFormatDate = mesFormatDate.substring(mesFormatDate.length() - 5, mesFormatDate.length());
            } else if (isYesterday(seconds)) {
                mesFormatDate = "昨天 " + mesFormatDate.substring(mesFormatDate.length() - 5, mesFormatDate.length());
            } else if (isThisYear(mesFormatDate, nowFormatDate)) {
                mesFormatDate = mesFormatDate.substring(5, mesFormatDate.length());
            } else {
                mesFormatDate = mesFormatDate.substring(0, 10);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return mesFormatDate;
    }

    /**
     * 08:30  ->  8.5
     *
     * @param time
     * @return
     */
    public static double getAccurateHourByStamps(String time) {
        if (TextUtils.isEmpty(time)) {
            return 0.0;
        }
        Long timeLong = getMilliSecond(time);
        Date date = new Date(timeLong);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.HOUR_OF_DAY);
        i = i == 0 ? 24 : i;
        return i + c.get(Calendar.MINUTE) / 60.0;
    }

    /**
     * 获得某年第indexInYear周第一天第一秒时间戳
     *
     * @param year
     * @param indexInYear
     * @return
     */
    public static long getFirstDayStampOfWeekAt(int year, int indexInYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, 0, 1);
        return calendar.getTimeInMillis() / 1000 + (indexInYear - 1) * 7 * 24 * 3600L;
    }

    public static boolean isCurrentDay(long stampsOfDay) {
        return stampsOfDay * 1000 < System.currentTimeMillis()
                && stampsOfDay * 1000 + 24 * 3600 * 1000 > System.currentTimeMillis();
    }

    /**
     * 显示X天Y时Z分：
     * 不足1分钟，取1分钟。
     * 不足1小时，只显示分钟；
     * 不足1天，只显示小时和分钟。
     * <p>
     * 示例：
     * 35分钟
     * 2小时1分钟
     * 3天35分钟
     * 3天1小时25分钟
     *
     * @param time 秒
     * @return
     */
    public static String getDurationFromTimeStamps(long time) {
        String result;
        if (time < 0) {
            return "";
        } else {
            time += 59;
            result = (int) (time / (60 * 60 * 24)) + "天" +
                    (int) ((time % (60 * 60 * 24)) / 3600) + "小时" +
                    (int) (time % (60 * 60) / 60) + "分钟";
        }
        String filterHourAndMin = result.replace("时0分钟", "时").replace("天0小时", "天");
        if (filterHourAndMin.startsWith("0")) {
            filterHourAndMin = filterHourAndMin.replace("0天", "");
        }
        return filterHourAndMin;
    }

    @NonNull
    private static String getMinute(long time) {
        if (time % 60 == 0 || time / 60 == 59) {
            return (int) (time / 60) + "分钟";
        }
        return (int) (time / 60 + 1) + "分钟";
    }

    /**
     * 时长中，如存在秒，向上取整记为1分钟；时长≥1小时，如分钟为0，则不显示分钟；时长≥1天时，如小时和分钟为0，则不显示。示例如下：
     * 45秒：1分钟
     * 30分钟45秒：31分钟
     * 1小时0分钟：1小时
     * 3天0小时0分钟：3天
     * 3天1小时0分钟：3天1小时
     * 3天0小时5分钟：3小时5分钟
     *
     * @param time 秒
     * @return
     */
    public static String getLessonReplayFormatTime(long time) {
        StringBuilder stringBuilder = new StringBuilder();
        double formatMinute = Math.ceil(time / 60);
        if (formatMinute > 0) {
            if (formatMinute < 60) {
                stringBuilder.append(formatMinute).append("分钟");
            } else if (formatMinute < 60 * 24) {
                if (formatMinute % 60 == 0) {
                    stringBuilder.append((int) formatMinute / 60).append("小时");
                } else {
                    stringBuilder.append((int) formatMinute / 60).append("小时").append(formatMinute % 60).append("分钟");
                }
            } else {
                stringBuilder.append((int) formatMinute / (24 * 60)).append("天");
                if ((formatMinute % (24 * 60)) % 60 != 0) {
                    stringBuilder.append((int) (formatMinute % (24 * 60)) / 60).append("小时");
                }
                if (formatMinute % 60 != 0) {
                    stringBuilder.append(formatMinute % 60).append("分钟");
                }
            }
        } else {
            stringBuilder.append("0分钟");
        }
        return stringBuilder.toString();
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断 果
     * @Exception 发生异常
     */
    public static int dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    public static String getWeekdayName(String pTime) {
        return getWeekName(dayForWeek(pTime));
    }

    public static String getWeekName(int i) {
        switch (i) {
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
            case 7:
                return "周日";
            default:
                return "";
        }
    }

    public static String getWeekNum(int i) {
        switch (i) {
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            case 7:
                return "日";
            default:
                return "";
        }
    }


    /**
     * 判断两个时间是否是同一个月份
     */
    public static boolean isSameMonth(String time1, String time2) {
        if (TextUtils.isEmpty(time1) || TextUtils.isEmpty(time2)) return false;
        return getYearAndMonthByStampsInStyle2(time1).equals(getYearAndMonthByStampsInStyle2(time2));
    }

    /**
     * 判断两个时间是否是同一个天
     */
    public static boolean isSameDay(String time1, String time2) {
        if (TextUtils.isEmpty(time1) || TextUtils.isEmpty(time2)) return false;
        return getYearMonthDayByStamps(time1).equals(getYearMonthDayByStamps(time2));
    }

    /**
     * 得时间戳中的天 不带零
     *
     * @param stamps
     * @return
     */
    public static String getNoZeroDayFromTimeStamp(String stamps) {
        if (TextUtils.isEmpty(stamps)) {
            return "";
        }
        long data = getMilliSecond(stamps);
        SimpleDateFormat format = new SimpleDateFormat("d");
        stamps = format.format(data);
        return stamps;
    }

    /**
     * 得时间戳中的月 不带零
     *
     * @param stamps
     * @return
     */
    public static String getNoZeroMonthFromTimeStamp(String stamps) {
        if (TextUtils.isEmpty(stamps)) {
            return "";
        }
        long data = getMilliSecond(stamps);
        SimpleDateFormat format = new SimpleDateFormat("M");
        return format.format(data);
    }


    public static String formatSecForDelayTime(int delayTime) {
        int d;
        int h;
        int m;
        String day = "";
        String hour = "";
        String minute = "";

        delayTime = (int) Math.ceil((double) delayTime / 60) * 60;
        d = delayTime / (3600 * 24);
        h = delayTime % (3600 * 24) / 3600;
        m = delayTime % 3600 / 60;
        if (d != 0) {
            day = d + "天";
        }
        if (h != 0) {
            hour = h + "小时";
        }
        if (m != 0) {
            minute = m + "分钟";
        }
        return day + hour + minute;
    }

    // 获得当前时间戳（毫秒）
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    // 获得当前时间戳（秒）
    public static long getCurrentTimeSeconds() {
        return getCurrentTimeMillis() / 1000;
    }

    /**
     * 8-->08
     *
     * @param s
     * @return
     */
    public static String fillZeroIfLessThanTen(String s) {
        if (TextUtils.isEmpty(s)) {
            return "";
        }
        if (s.length() >= 2) {
            return s.substring(s.length() - 2, s.length());
        }
        return "0" + s;
    }

    public static String getYearFromBirthday(String birthday) {
        return birthday.substring(0, 4);
    }

    public static String getMonthFromBirthday(String birthday) {
        String substring = birthday.substring(5, 7);
        if (substring.startsWith("0")) {
            return substring.substring(1);
        }
        return substring;
    }

    public static String getDayFromBirthday(String birthday) {
        String substring = birthday.substring(8);
        if (substring.startsWith("0")) {
            return substring.substring(1);
        }
        return birthday.substring(8);
    }
}
