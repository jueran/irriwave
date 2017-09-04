package com.salted.core.core.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by htyuan on 15-5-15.
 */
@SuppressWarnings({"checkstyle:MagicNumber", "PMD.UnnecessaryLocalBeforeReturn", "PMD.GodClass"})
public final class UIUtils {
    private UIUtils() {
    }

    /**
     * 将屏幕尺寸转化成像素个数;
     *
     * @param context : 上下文;
     * @param dpi     : 屏幕尺寸;
     * @return: 像素数;
     */
    public static int dpi2px(Context context, int dpi) {
        return (int) (context.getResources().getDisplayMetrics().density * dpi);
    }

    /**
     * 将屏幕尺寸转化成像素个数;
     *
     * @param context : 上下文;
     * @param dpi     : 屏幕尺寸;
     * @return: 像素数;
     */
    public static int dpi2px(Context context, double dpi) {
        return (int) (context.getResources().getDisplayMetrics().density * dpi + 0.5f);
    }

    public static int dp2px(Context context, double dpi) {
        return (int) (context.getResources().getDisplayMetrics().density * dpi + 0.5f);
    }

    public static int sp2px(Context context, int sp) {
        int px = (int) (context.getResources().getDisplayMetrics().scaledDensity
                * sp + 0.5f);
        return px;
    }

    public static int sp2px(Context context, double sp) {
        int px = (int) (context.getResources().getDisplayMetrics().scaledDensity * sp);
        return px;
    }

    /**
     * 将px值转换为dip或dp值
     *
     * @param pxValue
     */
    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue
                / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 将px值转换为sp值
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     */
    public static int px2sp(Context context, float pxValue) {
        return (int) (pxValue
                / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 关闭输入法界面;
     */
    public static void dismissInputmethod(Activity activity) {
        if (activity.getCurrentFocus() != null
                && activity.getCurrentFocus().getWindowToken() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity
                            .getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 关闭键盘;
     */
    public static void hideKeyBoard(final Activity activity, Handler handler) {
        if (activity.getCurrentFocus() == null) return;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager methodManager = (InputMethodManager)
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                methodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(), 0);
            }
        }, 200);
    }

    /**
     * 打开输入法界面;
     *
     * @param et
     */
    public static void showInputmethod(final EditText et) {
        if (et == null) {
            return;
        }

//        InputMethodManager inputManager = (InputMethodManager) et.getContext()
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.showSoftInput(et, 0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager methodManager = (InputMethodManager)
                        et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                methodManager.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);
    }

    /**
     * 打开输入法
     *
     * @param editText
     */
    public static void popupSoftWareOfEditText(final EditText editText) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =

                                       (InputMethodManager) editText.getContext().getSystemService(
                                               Context.INPUT_METHOD_SERVICE);

                               inputManager.showSoftInput(editText, 0);

                           }
                       },

                200);
    }

    public static void moveCursorToEnd(final EditText edit) {
        edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edit.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            edit.setSelection(edit.getText().length());
                        }
                    }, 50);
                }
            }
        });
    }

    /**
     * 在屏幕中间显示Toast
     *
     * @param context : 上下文
     * @param infoRes : 消息id
     */
    public static void showShortToastInCenter(Context context, int infoRes) {
        Toast toast = Toast.makeText(context, infoRes, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 在屏幕中间显示Toast
     *
     * @param context : 上下文
     * @param info    : 消息
     */
    public static void showShortToastInCenter(Context context, String info) {
        String s;
        //"\\|\\|\\|"在这里面不用转义符？
        s = info.replace("|||", "\n");
        Toast toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 显示进度对话框;
     *
     * @param titleResId : 对话框标题资源ID;
     * @param msgResId   : 对话框内容资源ID;
     * @param listener   : 对话框按钮行为处理监听器;
     */
    public static ProgressDialog showProgressDialog(Context context,
                                                    int titleResId, int msgResId,
                                                    DialogInterface.OnCancelListener listener) {
        return showProgressDialog(context,
                context.getResources().getString(titleResId), context
                        .getResources().getString(msgResId), listener);
    }

    public static ProgressDialog showProgressDialog(Context context,
                                                    String title, String message,
                                                    DialogInterface.OnCancelListener listener) {

        return ProgressDialog.show(context, title, message, true, true,
                listener);
    }

    /**
     * 设置 textView drawable
     *
     * @param context
     * @param drawableId
     * @param targetView
     * @param ltrd       0,1,2,3
     */
    public static void setDrawableLTRD(Context context, int drawableId, TextView targetView,
                                       int ltrd) {
        Resources res = context.getResources();
        Drawable drawable = res.getDrawable(drawableId);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        switch (ltrd) {
            case 0:
                //设置左图标
                targetView.setCompoundDrawables(drawable, null, null, null);
                break;
            case 1:
                //设置左图标
                targetView.setCompoundDrawables(null, drawable, null, null);
                break;
            case 2:
                //设置左图标
                targetView.setCompoundDrawables(null, null, drawable, null);
                break;
            case 3:
                //设置左图标
                targetView.setCompoundDrawables(null, null, null, drawable);
                break;
            default:
                targetView.setCompoundDrawables(null, null, null, drawable);
                break;
        }
    }

    /**
     * 设置textView的最大值
     *
     * @param textView
     * @param maxLength
     */
    public static void setEditMaxLength(TextView textView, int maxLength) {
        if (textView != null) {
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            textView.setFilters(fArray);
        }
    }

    public static void setEditTextMaxLength(EditText editText, int maxLength) {
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        editText.setFilters(fArray);
    }

    /*--------------------------------------------------------------------------------------------*/

    public static void show(View view) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() != View.VISIBLE) view.setVisibility(View.VISIBLE);
    }

    public static void hide(View view) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() != View.GONE) view.setVisibility(View.GONE);
    }

    public static void invisible(View view) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() != View.INVISIBLE) view.setVisibility(View.INVISIBLE);
    }

    public static void setVisible(View view, boolean visible) {
        setVisible(view, visible, false);
    }

    public static void setVisible(View view, boolean visible, boolean useInvisibleInsteadGone) {
        if (visible) {
            show(view);
        } else {
            if (useInvisibleInsteadGone) {
                invisible(view);
            } else {
                hide(view);
            }
        }
    }

    public static void setTextBoldStyle(TextView textView, boolean isBold) {
        textView.getPaint().setFakeBoldText(isBold);
    }
}
