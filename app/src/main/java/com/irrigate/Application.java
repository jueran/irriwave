package com.irrigate;

import android.content.Context;
import android.content.SharedPreferences;

import com.irrigate.core.util.CatchCrash;
import com.irrigate.core.util.ToastUtil;

import java.lang.ref.WeakReference;

/**
 * 说明：
 * 作者：杨健
 * 时间：2017/9/4.
 */

public class Application extends android.app.Application {
    private static WeakReference<Application> context;
    public static String Host;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.isWordClient = true;
        context = new WeakReference<>(this);

        //捕获Crash保存到本地
        CatchCrash.getInstance().init(this);


        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public static Application getContext() {
        return context.get();
    }
}
