package com.irrigate.base.utils;


import com.irrigate.Application;
import com.irrigate.R;
import com.irrigate.core.net.HQException;
import com.irrigate.core.net.HttpResult;
import com.irrigate.core.net.TaskListener;

/**
 * 说明：简化请求逻辑
 * 只认为有两类回调，成功与失败
 * 取消也是失败
 * 当失败的msg为空，则为网络异常
 * 作者：杨健
 * 时间：2017/8/1.
 */

public abstract class SimpleTaskListener<T extends HttpResult> implements TaskListener<T> {

    public void onTaskStart() {
        // when task start
    }

    public boolean isCancelEqualFailed() {
        return true;
    }

    public abstract void onTaskSuccess(T result);

    public abstract void onTaskFailed(String msg);

    public void onTaskCanceled() {
        // when task canceled
    }

    /**********************************************************************************************/

    @Override
    public final void onTaskStart(TaskListener listener) {
        onTaskStart();
    }

    @Override
    public final void onTaskComplete(TaskListener<T> listener, T result, Exception e) {
        if (result != null && result.isSuccess()) {
            onTaskSuccess(result);
        } else {
            if (e instanceof HQException) {
                onTaskFailed(e.getMessage());
            } else {
                onTaskFailed(Application.getContext().getString(R.string.network_error_tips));
            }
        }
    }

    @Override
    public final void onCancel() {
        if (isCancelEqualFailed()) {
            onTaskFailed(Application.getContext().getString(R.string.network_error_tips));
        } else {
            onTaskCanceled();
        }
    }
}
