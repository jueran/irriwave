package com.irrigate.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.irrigate.Application;
import com.irrigate.core.util.AppSharePreferenceMgr;
/**
 * Created by ldf on 17/7/17.
 */

public final class SharedPreferenceUtil {

    private SharedPreferenceUtil() {

    }

    /**
     * 存基本类型
     *
     * @param key key
     * @param o   Object
     */
    public static void put(String key, Object o) {
        if (o != null) {
            AppSharePreferenceMgr.put(Application.getContext(), key, o);
        }
    }

    /**
     * 取boolean
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        if (!TextUtils.isEmpty(key)) {
            boolean result = (boolean) (AppSharePreferenceMgr.get(
                    Application.getContext(), key, defaultValue));
            return result;
        }
        return defaultValue;
    }

    /**
     * 取int
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return
     */
    public static int getInt(String key, int defaultValue) {
        if (!TextUtils.isEmpty(key)) {
            int result = (int) (AppSharePreferenceMgr.get(
                    Application.getContext(), key, defaultValue));
            return result;
        }
        return defaultValue;
    }

    public static String getString(String key, String defaultValue) {
        try {
            String result = (String) (AppSharePreferenceMgr.get(
                    Application.getContext(), key, defaultValue));
            return result;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static long getLong(String key, long defaultValue) {
        try {
            long result = (Long) (AppSharePreferenceMgr.get(
                    Application.getContext(), key, defaultValue));
            return result;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 取对象
     *
     * @param key   key
     * @param clazz 对象类型
     * @return
     */
    @Nullable
    public static Object getObject(String key, Class clazz) {
        if (!TextUtils.isEmpty(key)) {
            String result = (String) (AppSharePreferenceMgr.get(
                    Application.getContext(), key, ""));
            Gson gson = new Gson();
            return gson.fromJson(result, clazz);
        }
        return null;
    }

    /**
     * 取对象
     *
     * @param key   key
     * @param clazz 对象类型
     */
    @Nullable
    public static <T> T get(String key, Class<T> clazz) {
        if (!TextUtils.isEmpty(key)) {
            String result = (String) (AppSharePreferenceMgr.get(
                    Application.getContext(), key, ""));
            Gson gson = new Gson();
            return gson.fromJson(result, clazz);
        }
        return null;
    }

    /**
     * 清空
     *
     * @param key
     */
    public static void remove(String key) {
        AppSharePreferenceMgr.remove(Application.getContext(), key);
    }

}
