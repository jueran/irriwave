package com.salted.core.core.helper.systemadvance;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-05-17.
 */

public class KeyboardStatusWatcher {
    public static final String DIMEN = "dimen";
    public static final String ANDROID = "android";

    private Activity activity;
    private VisibilityListener listener;

    private boolean keyboardListenersAttached = false;
    private View rootLayout;

    public KeyboardStatusWatcher(Activity activity, VisibilityListener listener) {
        this.listener = listener;
        this.activity = activity;
    }

    private boolean isLastTimeShowKeyboard = false;
    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // navigation bar height
                    int navigationBarHeight = 0;
                    int resourceId = activity.getResources().
                            getIdentifier("navigation_bar_height", DIMEN, ANDROID);
                    if (resourceId > 0) {
                        navigationBarHeight =
                                activity.getResources().getDimensionPixelSize(resourceId);
                    }

                    // status bar height
                    int statusBarHeight = 0;
                    resourceId = activity.getResources().
                            getIdentifier("status_bar_height", DIMEN, ANDROID);
                    if (resourceId > 0) {
                        statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
                    }

                    // display window size for the app layout
                    Rect rect = new Rect();
                    activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

                    // screen height - (user app height + status + nav) .....
                    // if non-zero, then there is a soft keyboard
                    int keyboardHeight = rootLayout.getRootView().getHeight() -
                            (statusBarHeight + navigationBarHeight + rect.height());

                    if (keyboardHeight <= 0) {
                        if (isLastTimeShowKeyboard) {
                            listener.onHideKeyboard();
                        }
                        isLastTimeShowKeyboard = false;
                    } else {
                        isLastTimeShowKeyboard = true;
                        listener.onShowKeyboard(keyboardHeight);
                    }
                }
            };

    /*--------------------------------------------------------------------------------------------*/

    public void attachKeyboardWatcher() {
        if (keyboardListenersAttached) {
            return;
        }

        rootLayout = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);
        keyboardListenersAttached = true;
    }

    public void removeKeyboardWatcher() {
        if (!keyboardListenersAttached) {
            return;
        }

        rootLayout = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(keyboardLayoutListener);
        keyboardListenersAttached = false;
    }

    /*--------------------------------------------------------------------------------------------*/

    public interface VisibilityListener {
        void onShowKeyboard(int keyboardHeight);

        void onHideKeyboard();
    }
}
