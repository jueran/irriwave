package com.irrigate.core.net;


import android.util.Log;

import java.io.File;

/**
 * 包装参数用的兼职对
 *
 * @author htyuan
 */
public class AbNameValuePair {

    private final String name;
    private final Object value;
    private static final String TAG = "AbNameValuePair";
    private static final String ERROR = "参数不能为空！！！";

    /**
     * Default Constructor taking a name and a value. The value may be null.
     *
     * @param name  The name.
     * @param value The value.
     */
    public AbNameValuePair(String name, String value) {
        this.name = name;
        this.value = value;
        if (value == null || value.length() <= 0) {
            Log.w(TAG, ERROR);
        }
    }

    public AbNameValuePair(String name, File value) {
        this.name = name;
        this.value = value;
        if (value == null || !value.exists()) {
            Log.w(TAG, ERROR);
        }
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }


    public boolean isStringValue() {
        return value instanceof String;
    }

    public boolean isFileValue() {
        return value instanceof File;
    }

    public String getStringValue() {
        return (String) value;
    }

    public File getFileValue() {
        return (File) value;
    }
}
