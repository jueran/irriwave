package com.salted.core.core.helper.activityhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-07-24.
 */

public final class ActivityRouter {
    public static final String ACTIVITY_PARAM = "ACTIVITY_ROUTER_ACTIVITY_OBJECT_PARAM";
    public static final String ACTIVITY_STRING_PARAM = "ACTIVITY_ROUTER_ACTIVITY_STRING_PARAM";

    private ActivityRouter() {
    }

    public static void start(Context context, Class<?> targetActivityClass) {
        Intent intent = new Intent(context, targetActivityClass);
        context.startActivity(intent);
    }

    public static void start(Context context, Class<?> targetActivityClass, int launchedMode) {
        Intent intent = new Intent(context, targetActivityClass);
        intent.setFlags(launchedMode);
        context.startActivity(intent);
    }

    /**
     * 每个子类Activity都可以用这些方法跳转
     * <p>
     * - 传入的param类型 建议前往要跳转的Activity的 takeOutYourParam(Parcelable) 方法中查看
     * - 如果一个Activity有多个入口，而且不同入口传入的参数不同，建议只使用一种param类型，不同的入口往其中初始化
     * 不同的参数
     * - 被启动Activity在 takeOutYourParam(Parcelable) 中接收传入param，instance通过后强转得到需要的参数类
     *
     * @param param               实现了Parcelable接口的参数类
     * @param targetActivityClass 子类Activity的class对象
     */
    public static void start(Context context, Class<?> targetActivityClass, Parcelable param) {
        Intent intent = new Intent(context, targetActivityClass);
        if (param != null) intent.putExtra(ACTIVITY_PARAM, param);
        context.startActivity(intent);
    }

    public static void start(Context context, Class<?> targetActivityClass, Parcelable param,
                             int launchedMode) {
        Intent intent = new Intent(context, targetActivityClass);
        intent.setFlags(launchedMode);
        if (param != null) intent.putExtra(ACTIVITY_PARAM, param);
        context.startActivity(intent);
    }

    /**
     * - 如果跳转参数只需要一个String, 可以直接使用此方法跳转，并在被启动Activity的 takeOutYourParam(String)
     * 方法中直接获得参数
     *
     * @param param
     */
    public static void start(Context context, Class<?> targetActivityClass, String param) {
        Intent intent = new Intent(context, targetActivityClass);
        if (param != null) intent.putExtra(ACTIVITY_STRING_PARAM, param);
        context.startActivity(intent);
    }

    public static void start(Context context, Class<?> targetActivityClass, String param,
                             int launchedMode) {
        Intent intent = new Intent(context, targetActivityClass);
        intent.setFlags(launchedMode);
        if (param != null) intent.putExtra(ACTIVITY_STRING_PARAM, param);
        context.startActivity(intent);
    }

    /*-------------------------------------------------*/

    public static void startForResult(Activity activity, Class<?> targetActivityClass,
                                      int requestCode) {
        Intent intent = new Intent(activity, targetActivityClass);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startForResult(Activity activity, Class<?> targetActivityClass,
                                      int requestCode, int launchedMode) {
        Intent intent = new Intent(activity, targetActivityClass);
        intent.setFlags(launchedMode);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startForResult(Activity activity, Class<?> targetActivityClass,
                                      Parcelable param, int requestCode) {
        Intent intent = new Intent(activity, targetActivityClass);
        if (param != null) intent.putExtra(ACTIVITY_PARAM, param);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startForResult(Activity activity, Class<?> targetActivityClass,
                                      Parcelable param,
                                      int requestCode, int launchedMode) {
        Intent intent = new Intent(activity, targetActivityClass);
        intent.setFlags(launchedMode);
        if (param != null) intent.putExtra(ACTIVITY_PARAM, param);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startForResult(Activity activity, Class<?> targetActivityClass,
                                      String param, int requestCode) {
        Intent intent = new Intent(activity, targetActivityClass);
        if (param != null) intent.putExtra(ACTIVITY_STRING_PARAM, param);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startForResult(Activity activity, Class<?> targetActivityClass,
                                      String param, int requestCode, int launchedMode) {
        Intent intent = new Intent(activity, targetActivityClass);
        intent.setFlags(launchedMode);
        if (param != null) intent.putExtra(ACTIVITY_STRING_PARAM, param);
        activity.startActivityForResult(intent, requestCode);
    }

    /*--------------------------------------------------------------------------------------------*/

    public static void analysisDataThroughRouter(@Nullable Intent intent,
                                                 @Nullable TakeOutParamCallBack callBack) {
        if (intent == null) return;
        Bundle bundle = intent.getExtras();
        if (bundle == null) return;

        String stringParam = bundle.getString(ACTIVITY_STRING_PARAM);
        if (stringParam != null && callBack != null) {
            callBack.takeOutParam(stringParam);
        }

        Parcelable param = bundle.getParcelable(ACTIVITY_PARAM);
        if (param != null && callBack != null) {
            callBack.takeOutParam(param);
        }
    }

    public interface TakeOutParamCallBack {
        void takeOutParam(String stringParam);

        void takeOutParam(Parcelable param);
    }
}