package com.irrigate.core.util;

/**
 * 判断双击事件,双击退出
 */
public final class ClickUtil {
    private static final long EXIT_INTERVAL = 2000;
    private static final long DOUBLE_CLICK_INTERVAL = 400;
    private static final long CONTINUE_CLICK_INTERVAL = 2200;

    private static long lastTryTime;

    private ClickUtil() {
    }

    public static boolean clickAndReturnIsDoubleClick() {
        long duration = System.currentTimeMillis() - lastTryTime;
        lastTryTime = System.currentTimeMillis();
        return duration < EXIT_INTERVAL;
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        //双次点击间隔产品定义全局统一间隔为600ms
        boolean b = 0 < timeD && timeD < DOUBLE_CLICK_INTERVAL;
        if (!b) {
            // 刷新最后一次可点击的时间
            lastClickTime = time;
        }
        return b;
    }

    public static boolean isFastDoubleClick(long doubleClickInterval) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        boolean b = 0 < timeD && timeD < doubleClickInterval;
        if (!b) {
            // 刷新最后一次可点击的时间
            lastClickTime = time;
        }
        return b;
    }

    public static boolean isContinueClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < CONTINUE_CLICK_INTERVAL) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
