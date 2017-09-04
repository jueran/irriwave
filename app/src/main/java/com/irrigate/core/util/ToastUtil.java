package com.irrigate.core.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.irrigate.R;


/**
 * 弹出Toast时,能立即改变内容,而不需要等待上个提示内容的消失
 */
@SuppressWarnings("PMD.NonThreadSafeSingleton")
public final class ToastUtil {
    public static boolean isWordClient = false;
    private static Toast toast;

    private ToastUtil() {
    }

    public static void toastShort(Context context, String text) {
        if (TextUtils.isEmpty(text) || context == null) {
            return;
        }
        if (toast == null) {
            // 单词端要求是居中，为了最小改动代码，用静态变量控制显示样式
            if (isWordClient) {
                toast = new Toast(context.getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                View view = LayoutInflater.from(context).inflate(R.layout.transient_notification, null);
                if (view != null) {
                    toast.setView(view);
                }
            } else {
                toast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
            }
        }
        toast.setText(text);
        toast.show();
    }

    public static void toastOnCenter(Context context, String text) {
        if (toast == null)
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }
}
