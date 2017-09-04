package com.salted.core.core.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xinyuanzhong on 16/10/13.
 */
@SuppressWarnings({"PMD"})
public final class StringUtil {
    private static final String[] grades = {
            "一年级",
            "二年级",
            "三年级",
            "四年级",
            "五年级",
            "六年级",
            "七年级",
            "八年级",
            "九年级",
            "高一",
            "高二",
            "高三",
    };

    private StringUtil() {
    }

    /**
     * 把字符串转化成int
     *
     * @param string
     * @return
     */
    public static int getIntFromString(String string) {
        int i = 0;
        if (!TextUtils.isEmpty(string)) {
            try {
                i = Integer.parseInt(string);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    /**
     * 返回非null字符串
     *
     * @param s
     * @return
     */
    public static String getString(Object s) {
        if (s == null) {
            return "";
        }
        return s.toString();
    }

    /**
     * null和空字符串返回0
     *
     * @param s
     * @return
     */
    public static String getEmptyZeroString(String s) {
        if (TextUtils.isEmpty(s)) {
            return "0";
        }
        return s;
    }

    /**
     * 把1-12转化成年级
     *
     * @param grade
     * @return
     */
    public static String getGradeFromNumber(String grade) {
        String gradeName = "";
        try {
            int index = Integer.parseInt(grade);
            if (index < 1 || index > 12) {
                return "";
            } else {
                gradeName = grades[index - 1] + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gradeName;
    }


    public static double getDoubleFromString(String s) {

        double i = 0;
        if (!TextUtils.isEmpty(s)) {
            try {
                i = Double.valueOf(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    /**
     * 用英文逗号拼接String
     *
     * @param stringList
     * @return
     */
    public static String combineEnglishStrings(List<String> stringList) {
        if (stringList == null || stringList.isEmpty()) {
            return "";
        }
        if (stringList.size() == 1) {
            return stringList.get(0);
        }
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < stringList.size() - 1; i++) {
            s.append(stringList.get(i)).append(",");
        }
        s.append(stringList.get(stringList.size() - 1));
        return s.toString();
    }

    /**
     * 用英文逗号拼接String
     *
     * @param stringList
     * @return
     */
    public static String combineStringsWithSymbol(List<String> stringList, String splitSymbol) {
        if (stringList == null || stringList.isEmpty()) {
            return "";
        }
        if (stringList.size() == 1) {
            return stringList.get(0);
        }
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < stringList.size() - 1; i++) {
            s.append(stringList.get(i)).append(splitSymbol);
        }
        s.append(stringList.get(stringList.size() - 1));
        return s.toString();
    }

    public static float getFloatFromString(String s) {
        float f = 0;
        try {
            f = Float.valueOf(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public static long getLongFromString(String s) {

        long l = 0;
        try {
            l = Long.parseLong(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param origin
     * @return
     */

    public static String getFineMoney(String origin) {
        String result = getTwoDigitFormatData(origin);
        if (result.indexOf('.') > 0) {
            result = result.replaceAll("0+?$", "");//去掉多余的0
            result = result.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return result;
    }

    /**
     * 0.00
     * 0.10
     * 1.00
     *
     * @param money
     * @return
     */

    public static String getTwoDigitFormatData(String money) {
        if (money == null || TextUtils.isEmpty(money) || money.equals("0")) {
            return "0.00";
        } else if (money.length() == 1) {
            return "0.0" + money;
        } else if (money.length() == 2) {
            return "0." + money;
        } else {
            String formatMoney = new StringBuilder().append(money.substring(0, money.length() - 2)).
                    append(".").append(money.substring(money.length() - 2, money.length())).toString();
            return formatMoney;
        }
    }

    /**
     * 0
     * 0.10
     * 1.00
     *
     * @param money
     * @return
     */
    public static String getOrderMoney(String money) {
        if (money == null || TextUtils.isEmpty(money) || money.equals("0")) {
            return "0";
        } else if (money.length() == 1) {
            return "0.0" + money;
        } else if (money.length() == 2) {
            return "0." + money;
        } else {
            String formatMoney = new StringBuilder().append(money.substring(0, money.length() - 2)).
                    append(".").append(money.substring(money.length() - 2, money.length())).toString();
            return formatMoney;
        }
    }

    /**
     * 四舍五入方法
     *
     * @param num   要四舍五入的数字
     * @param digit 要保留的位数
     */
    public static String roundingOffNum(double num, int digit) {
        String s = "0";
        for (int i = 0; i < digit; i++) {
            s = getFormat(s, i);
        }
        DecimalFormat df = new DecimalFormat(s);
        return df.format(num);
    }

    @NonNull
    private static String getFormat(String string, int i) {
        String s;
        if (i == 0) {
            s = string + ".0";
        } else {
            s = string + "0";
        }
        return s;
    }

    /**
     * 小数格式化
     *
     * @param num 小数
     * @param reservedDecimalSize 保留小数位数
     */
    /**
     */
    @Deprecated
    public static String formatDecimal(float num, int reservedDecimalSize) {
        BigDecimal bigDecimal = new BigDecimal(Float.toString(num));
        return bigDecimal.setScale(reservedDecimalSize, RoundingMode.DOWN).toString();
    }

    /**
     * 分离符号链接的字符串
     *
     * @param s      需要被拆分的字符串
     * @param symbol 链接符号
     * @return
     */
    public static List<String> getListFromSymbolText(String s, String symbol) {
        List<String> stringList = new ArrayList<>();
        if (TextUtils.isEmpty(s)) {
            return stringList;
        }

        if (TextUtils.isEmpty(symbol)) {
            stringList.add(s);
            return stringList;
        }

        String[] array = s.split(symbol);
        stringList.addAll(Arrays.asList(array));

        return stringList;
    }

    /**
     * 判断两个字符串列表包含的值是否相等（不判断顺序）
     *
     * @param strings1
     * @param strings2
     * @return
     */
    public static boolean isTwoStringListEquals(List<String> strings1, List<String> strings2) {

        return ListUtil.isTwoListEquals(strings1, strings2);
    }

    /**
     * 距离转换
     * 1000m以下转换为m
     * 1000m以上转换为km单位
     */
    public static String getDistance(String distanceString) {
        double distanceDouble;
        try {
            distanceDouble = Double.valueOf(distanceString);
        } catch (Exception e) {
            return "";
        }
        String distance;
        if (distanceDouble / 1000 >= 1) {
            Double doubleBMI = distanceDouble / 1000;
            DecimalFormat df = new DecimalFormat("#.0");
            String bmi = df.format(doubleBMI);
            distance = bmi + "km";
        } else {
            int trans = (int) distanceDouble;
            distance = String.format("%d", trans) + "m";
        }
        return distance;
    }

    public static String subStringIfOverLength(String s, int max) {
        if (s == null) {
            return "";
        }
        if (s.length() > max) {
            return s.substring(0, max) + "...";
        }
        return s;
    }

    /**
     * 手机号格式是否合法
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        return !TextUtils.isEmpty(phoneNumber)
                && phoneNumber.length() == 11
                && phoneNumber.substring(0, 2).matches("1[3-9]");
    }

    /**
     * <<<<<<< HEAD
     * 把字符串中所有的指定字符换成其他字符
     *
     * @param s       目标String
     * @param oldChar 需要替换的char
     * @param newChar 替换后的char
     * @return 替换后的String
     */
    public static String replaceCharsWith(String s, String oldChar, String newChar) {
        if (TextUtils.isEmpty(s)) {
            return "";
        }
        String result = s.replaceAll(oldChar, newChar);
        return result;
    }

    /**
     * 判断字符串是否只包含数字和字母
     *
     * @param s String
     * @return 是否只包含数字和字母
     */
    public static boolean isStringOnlyHaveCharAndNum(String s) {
        if (s == null || s.length() < 1) {
            return false;
        }
        return !s.matches(".*[^a-zA-Z0-9]+.*");
    }

    /**
     * 加密手机号
     *
     * @param mobile number
     * @return number
     */
    public static String encryptionMobile(String mobile) {
        if (isPhoneNumberValid(mobile)) {
            return mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        }
        return mobile;
    }
}
