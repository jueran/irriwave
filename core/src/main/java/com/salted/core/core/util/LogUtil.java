package com.salted.core.core.util;

import android.util.Log;

import com.salted.core.BuildConfig;


/**
 * Created by ldf on 17/7/17.
 */

public final class LogUtil {

    private LogUtil() {
    }

    //是否输出
    private static boolean isDebug = BuildConfig.DEBUG;

    /*
     * 设置debug模式(true:打印日志  false：不打印)
     */
    public static void isEnableDebug(boolean isDebug) {
        LogUtil.isDebug = isDebug;
    }

    /**
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg != null ? msg : "");
        }
    }

    public static void i(Object object, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(object.getClass().getSimpleName(), msg != null ? msg : "");
        }
    }

    public static void i(String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(" [INFO] --- ", msg != null ? msg : "");
        }
    }

    /**
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg != null ? msg : "");
        }
    }

    public static void d(Object object, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(object.getClass().getSimpleName(), msg != null ? msg : "");
        }
    }

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(" [DEBUG] --- ", msg != null ? msg : "");
        }
    }

    /**
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg != null ? msg : "");
        }
    }

    public static void w(Object object, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(object.getClass().getSimpleName(), msg != null ? msg : "");
        }
    }

    public static void w(String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(" [WARN] --- ", msg != null ? msg : "");
        }
    }

    /**
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg != null ? msg : "");
        }
    }

    public static void e(Object object, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(object.getClass().getSimpleName(), msg != null ? msg : "");
        }
    }

    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(" [ERROR] --- ", msg != null ? msg : "");
        }
    }

    /**
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, msg != null ? msg : "");
        }
    }

    public static void v(Object object, String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(object.getClass().getSimpleName(), msg != null ? msg : "");
        }
    }

    public static void v(String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(" [VERBOSE] --- ", msg != null ? msg : "");
        }
    }
}
