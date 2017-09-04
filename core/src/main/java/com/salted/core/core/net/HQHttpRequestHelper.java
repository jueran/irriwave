package com.salted.core.core.net;

import android.content.Context;

import java.util.Map;

/**
 * 说明：
 * 作者：杨健
 * 时间：2017/6/20.
 */

public interface HQHttpRequestHelper {
    void setHeader(Map<String, String> header, String path);

    String getHost(Context ctx);

    int getTimeOut();

    void onSessionOutTime(Context context);

    boolean hasSession();

    boolean isInterceptionCode(int code);

    void doSomeThingWithCodeInterception(int code, Context context);
}
