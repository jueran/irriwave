package com.irrigate.widget;

import android.content.Context;
import android.os.Handler;

import com.irrigate.R;
import com.irrigate.core.helper.baseinterface.ILoadingView;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-05-17.
 */

public class LoadingViewManager implements ILoadingView {
//    private SVProgressHUD progressWindow;
    private Context context;
    private Handler handler;

    public LoadingViewManager(Context context) {
        this.context = context;
        initLoadingView();
    }

    private void initLoadingView() {
//        progressWindow = new SVProgressHUD(context);
//        progressWindow.getProgressBar().
//                setCircleColor(context.getResources().getColor(R.color.colorPrimary));
//        handler = new Handler();
    }

    /*--------------------------------------------------------------------------------------------*/

    public void showLoadingView() {
//        if (progressWindow == null) return;
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (!progressWindow.isShowing()) {
//
//                    progressWindow.show();
//                }
//            }
//        });
    }

    public void hideLoadingView() {
//        if (progressWindow == null) return;
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (progressWindow.isShowing()) {
//                    progressWindow.dismiss();
//                }
//            }
//        });
    }

    public void hideLoadingViewWithoutAnim() {
//        if (progressWindow == null) return;
//        if (!progressWindow.isShowing()) {
//            progressWindow.dismissImmediately();
//        }
    }

    public void hideLoadingViewDelayed(final long delayMillis) {
//        if (progressWindow == null) return;
//        if (progressWindow.isShowing()) {
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    progressWindow.dismiss();
//                }
//            }, delayMillis);
//        }
    }

    /*-------------------------------------------------*/

    public boolean isShowing() {
//        if (progressWindow == null) return false;
//        return progressWindow.isShowing();
        return false;
    }
}
