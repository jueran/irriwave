package com.irrigate.core.helper.systemadvance;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.irrigate.core.helper.systemadvance.StatusBarManager.StatusBarSetResult.FAILED;
import static com.irrigate.core.helper.systemadvance.StatusBarManager.StatusBarSetResult.SUCCESS_ANDROID_M;
import static com.irrigate.core.helper.systemadvance.StatusBarManager.StatusBarSetResult.SUCCESS_SPECIFIC;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-05-17.
 */

@SuppressLint("all")
public final class StatusBarManager {
    private StatusBarManager() {
    }

    /**
     * 同时设置状态栏文字颜色为light模式、状态栏背景为指定颜色
     * -- 状态栏文字颜色为light模式：文字为深色，此颜色不可指定，只能使用系统默认
     * -- 推荐同时把状态栏背景颜色指定为浅色
     * <p>
     * 有可能设置失败，失败的话状态栏文字颜色和背景颜色都维持原状态
     *
     * @param lightColorResId 状态栏背景颜色资源id（推荐使用浅色）
     */
    public static void setStatusBarLightMode(Activity activity, int lightColorResId) {
        switch (setStatusBarDarkWords(activity)) {
            case SUCCESS_SPECIFIC:
                // fall through
            case SUCCESS_ANDROID_M:
                setStatusBarBackgroundColorId(lightColorResId, activity);
                break;
            default:
                // 如果无法设置状态栏文字颜色，则状态栏背景颜色也不被会设置，保持原状态
                break;
        }
    }

    /*--------------------------------------------------------------------------------------------*/

    /**
     * 设置状态栏背景颜色
     * <p>
     * Android版本大于21可以设置
     *
     * @param colorResId 状态栏背景颜色资源id
     */
    public static void setStatusBarBackgroundColorId(int colorResId, Activity activity) {
        if (activity == null) return;
        if (colorResId <= 0) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(colorResId));
        }
    }

    /**
     * 设置状态栏背景颜色
     * <p>
     * Android版本大于21可以设置
     *
     * @param color 状态栏背景颜色资源id
     */
    public static void setStatusBarBackgroundColor(int color, Activity activity) {
        if (activity == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        }
    }

    /*-------------------------------------------------*/

    /**
     * 设置状态栏文字颜色为light模式（即文字深色）
     * <p>
     * 小米和魅族手机的Android版本大于21可以设置，其他手机的Android版本大于23可以设置
     * Recommend call this method at onCreate()
     *
     * @return StatusBarSetResult 返回设置结果，成功或失败
     */
    public static StatusBarSetResult setStatusBarDarkWords(Activity activity) {
        if (activity == null) return FAILED;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return FAILED;

        if (setMIUIStatusBarLightMode(activity.getWindow(), true)) {
            return SUCCESS_SPECIFIC;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setAndroidMStatusBarLightMode(activity);
            return SUCCESS_ANDROID_M;
        } else {
            return FAILED;
        }
    }

    public enum StatusBarSetResult {
        FAILED,
        SUCCESS_SPECIFIC, // 特殊机型设置成功，这个结果优先于6.0以上系统的设置，即如果是6.0以上的特殊机型，会使用特殊机型的设置方式
        SUCCESS_ANDROID_M // 6.0以上系统设置成功
    }

    /*--------------------------------------------------------------------------------------------*/

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * <p>
     * 这个方法有效，但是在特殊情况下会有异常表现，所以决定废弃，不对魅族系统做特殊适配
     * 一个特殊情况是：当Activity设置为透明状态栏模式时
     * *   （getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);）
     * *   状态栏文字颜色会设置不上，但等一段时间，又能设置上去，或者弹一个dialog之后也能设置上去，但是在设置上之
     * *   后切到后台又切出来，也会设置回去
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    @Deprecated
    private static boolean setFlymeStatusBarLightMode(Window window, boolean dark) {
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean setMIUIStatusBarLightMode(Window window, boolean dark) {
        if (window != null) {
            Class clazz = window.getClass();
            try {
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag); //状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag); //清除黑色字体
                }
                return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void setAndroidMStatusBarLightMode(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = activity.getWindow().getDecorView();
            view.setSystemUiVisibility(view.getSystemUiVisibility()
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /*--------------------------------------------------------------------------------------------*/

    public static int getStatusBarHeight(Activity activity) {
        //获取status_bar_height资源的ID
        int resourceId =
                activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return activity.getResources().getDimensionPixelSize(resourceId);
        }
        return -1;
    }
}