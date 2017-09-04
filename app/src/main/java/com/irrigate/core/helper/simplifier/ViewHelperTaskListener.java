package com.irrigate.core.helper.simplifier;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.irrigate.core.helper.activityhelper.ViewHelper;
import com.irrigate.core.net.HttpResult;

/**
 * Description:
 * 通过ViewHelper进一步简化使用 {@link BaseTaskListener}，但这样可能让MVP职责不清晰，使用需谨慎
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-05-17.
 */

public abstract class ViewHelperTaskListener<R extends HttpResult> extends BaseTaskListener<R> {
    private ViewHelper viewHelper;

    public ViewHelperTaskListener(ViewHelper viewHelper) {
        this.viewHelper = viewHelper;
    }

    /*--------------------------------------------------------------------------------------------*/

    public void onTaskStart() {
        viewHelper.showLoadingView();
    }

    public void onTaskCancel() {
        viewHelper.hideLoadingView();
    }

    public void onTaskComplete(@Nullable R result, @Nullable Exception e) {
        viewHelper.hideLoadingView();
    }

    public void onReturnToastMsgErrorCode(@NonNull String errorMsg) {
        viewHelper.toast(errorMsg);
    }
}