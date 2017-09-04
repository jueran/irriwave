package com.irrigate.core.util.formate;

import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * Created by xinyuanzhong on 2017/7/13.
 */
@SuppressWarnings({"checkstyle:MagicNumber", "PMD:AvoidDuplicateLiterals",
        "PMD.AvoidLiteralsInIfCondition"})
public final class PriceUtil {

    public static final String ZERO_POINT_DOUBLE_ZERO = "0.00";
    public static final String ZERO_POINT = "0.";
    public static final String ZERO_POINT_ZERO = "0.0";
    public static final String ZERO = "0";
    public static final String POINT = ".";

    private PriceUtil() {
    }

    /**
     * 保留float后面两位小数:888.88
     *
     * @param num 分的价格
     * @return
     */
    public static String getPriceFormat(int num) {
        String s = String.valueOf(num);

        if (TextUtils.isEmpty(s) || s.length() < 2) {
            return ZERO_POINT_DOUBLE_ZERO;
        }
        if (s.length() < 3) {
            return ZERO_POINT + s;
        }
        return s.substring(0, s.length() - 2)
                + POINT
                + s.substring(s.length() - 2, s.length());
    }

    /**
     * 保留整数:888
     *
     * @param cent 分的价格，可以有正负
     * @return
     */
    public static String getYuanFromCent(int cent) {
        return cent / 100 + "";
    }

    /**
     * 转化为0.00的钱币格式
     *
     * @param money 单位是分
     */
    public static String getWalletMoneyFormat(String money) {
        if (money == null || TextUtils.isEmpty(money) || ZERO.equals(money)) {
            return ZERO_POINT_DOUBLE_ZERO;
        } else if (money.length() == 1) {
            return ZERO_POINT_ZERO + money;
        } else if (money.length() == 2) {
            return ZERO_POINT + money;
        } else {
            String formatMoney = new StringBuilder().append(money.substring(0, money.length() - 2)).
                    append(POINT).append(money.substring(money.length() - 2,
                    money.length())).toString();
            return formatMoney;
        }
    }

    /**
     * 转化为0的钱币格式
     *
     * @param money 单位是分
     */
    public static String getWalletMoneyForIntegerFormat(String money) {
        if (money == null || TextUtils.isEmpty(money) || ZERO.equals(money)) {
            return ZERO;
        } else if (money.length() == 1) {
            return ZERO;
        } else if (money.length() == 2) {
            return ZERO;
        } else {
            return new StringBuilder().append(
                    money.substring(0, money.length() - 2)).toString();
        }
    }

    /**
     * ¥19      整数价格只展示整数，无小数点，0显示0.
     * ¥19.9    小数点后一位，展示一位
     * ¥19.99   小数点后一位，展示两位。
     *
     * @param cent 单位是分
     */
    public static String formatCentPrice(String cent) {
        String result = getWalletMoneyFormat(cent);
        if (result.indexOf(".") > 0) {
            result = result.replaceAll("0+?$", "");//去掉多余的0
            result = result.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return result;
    }

    /**
     * 把100转化成￥1.00
     *
     * @param cent 分
     * @return
     */
    public static String convertPriceFromApi(String cent) {
        String result = ZERO_POINT_DOUBLE_ZERO;
        if (TextUtils.isEmpty(cent)) {
            result = ZERO_POINT_DOUBLE_ZERO;
        }
        if (cent.length() > 2) {
            result = cent.substring(0, cent.length() - 2)
                    + POINT
                    + cent.substring(cent.length() - 2, cent.length());
        } else if (cent.length() > 1) {
            result = ZERO_POINT + cent;
        } else if (cent.length() > 0) {
            result = ZERO_POINT_ZERO + cent;
        }
        return "￥" + result;
    }

    /**
     * 把分格式化为保留两位小数的元
     * <p>
     * 来源格式:单位为分
     * 目标格式:显示两位小数, 例如1800.00, 单位是元
     */
    public static String formatNormalPrice(long price) {
        long l = price;
        if (l < 0) {
            l = 0;
        }
        Double result = l / 100d;
        return new DecimalFormat(ZERO_POINT_DOUBLE_ZERO).format(result);
    }
}
