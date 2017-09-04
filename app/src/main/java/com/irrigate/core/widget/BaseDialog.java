package com.irrigate.core.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.irrigate.R;
import com.irrigate.core.util.ScreenUtil;
import com.irrigate.core.util.UIUtils;

/**
 * Description:
 * 全局通用的Dialog基类，不带任何UI样式
 * 基于{@link #showBaseDialog(View, boolean, float)}可以更简单地创建更多通用Dialog
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-07-15.
 */

public class BaseDialog {
    private Dialog dialog;
    private Context context;

    public BaseDialog(Context context) {
        this.context = context;
        dialog = new AlertDialog.Builder(context).create(); // 不建议用Dialog，在4.4以下版本中会显示异常

        // 点击任何按键都不消失
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode != KeyEvent.KEYCODE_HOME;
            }
        });

//        try {
//            int dividerID = context.getResources().getIdentifier("android:id/titleDivider", null, null);
//            View divider = dialog.findViewById(dividerID);
//            divider.setBackgroundColor(Color.TRANSPARENT);
//        } catch (Exception e) {
//            // 去除4.4及以下版本的的蓝色线条titleDiver
//            e.printStackTrace();
//        }
    }

    /*--------------------------------------------------------------------------------------------*/

    /**
     * 基本的自定义dialog，建议基于此方法创建更多通用Dialog
     * <p>
     * - 基于{@link #showBaseDialog(View, boolean, float, float)}
     * - 完全自定义整个DialogView布局
     * - 对dialog的属性做了基础的设置，并调用了show
     *
     * @param dialogView              传入整个自定义DialogView，默认上下左右居中
     * @param canCancelOnTouchOutside 点击dialog外，dialog是否会消失
     * @param cornerSizeInDp          dialog圆角大小，会对整个dialog切圆角
     */
    public final void showBaseDialog(View dialogView, boolean canCancelOnTouchOutside,
                                     float cornerSizeInDp) {
        showBaseDialog(dialogView, canCancelOnTouchOutside, 1, cornerSizeInDp);
    }

    /*--------------------------------------------------------------------------------------------*/

    /**
     * 最基本的自定义dialog
     * <p>
     * - 完全自定义整个DialogView布局
     * - 此方法对dialog的属性做了基础的设置，并调用了show
     *
     * @param dialogView              传入整个自定义DialogView，可以自定义具体宽高
     *                                最大高度是屏幕高度，最大宽度由dialogWidthScale控制，
     *                                不超过 屏幕宽度*dialogWidthScale*0.8
     *                                默认上下左右居中
     * @param canCancelOnTouchOutside 点击dialog外，dialog是否会消失
     * @param dialogWidthScale        dialog的最大宽度，通过相对屏幕的比例来设置
     *                                正常情况下传1就好，默认大概是屏幕宽度的0.8
     *                                建议传值范围 0 < ? <= 1
     * @param cornerSizeInDp          dialog圆角大小，会对整个dialog切圆角
     */
    public final void showBaseDialog(View dialogView, boolean canCancelOnTouchOutside,
                                     float dialogWidthScale, float cornerSizeInDp) {
        // 需要先show再进行设置
        dialog.show();

        Window window = dialog.getWindow();
        if (window == null) return;

        window.setContentView(getLayout(dialogView, cornerSizeInDp));
        configDialogWindow(window, canCancelOnTouchOutside, dialogWidthScale);
    }

    /*-------------------------------------------------*/

    private View getLayout(View dialogView, float cornerDp) {
        View layout = getLayout(context, R.layout.dialog_base);
        RoundCornerLayout roundCornerLayout = (RoundCornerLayout) layout.findViewById(
                R.id.round_corner_container);
        roundCornerLayout.setRadius(UIUtils.dpi2px(context, cornerDp));
        roundCornerLayout.addView(dialogView);
        return layout;
    }

    private void configDialogWindow(Window window, boolean canCancelOnTouchOutside,
                                    float dialogScale) {
        dialog.setCanceledOnTouchOutside(canCancelOnTouchOutside);

        if (dialogScale >= 1f || dialogScale <= 0) {
            // 此处也可以传入具体的值，但实际宽度会在此基础上*0.8
            // 如果*0.8后还超过屏幕宽度，则会左边保持间距，右边超出屏幕
            window.setLayout(ScreenUtil.getScreenWidth(context),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            window.setLayout((int) (ScreenUtil.getScreenWidth(context) * dialogScale),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // 防止圆角背景失效
        View v = window.getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /*--------------------------------------------------------------------------------------------*/

    public Dialog getDialog() {
        return dialog;
    }

    /**
     * 工具方法，通过layoutResId获取填充好的view
     * <p>
     * 注意：
     * - 通过不传入rootView的方式inflate，你layout最外层布局的LayoutParams会失效
     */
    public static final View getLayout(Context context, int layoutResId) {
        return LayoutInflater.from(context).inflate(layoutResId, null, false);
    }
}