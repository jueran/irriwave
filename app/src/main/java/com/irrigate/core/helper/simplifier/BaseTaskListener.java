package com.irrigate.core.helper.simplifier;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.irrigate.core.net.HttpResult;
import com.irrigate.core.net.TaskListener;
import com.irrigate.core.util.DS;

/**
 * Description:
 * BaseTaskListener 用于辅助处理 onTaskComplete 时的逻辑
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-05-17.
 */

public abstract class BaseTaskListener<R extends HttpResult> implements TaskListener<R> {
    public final void onTaskStart(TaskListener<R> listener) {
        onTaskStart();
    }

    public final void onTaskComplete(TaskListener<R> listener, R result, Exception e) {
        onTaskComplete(result, e);

        // 网络错误（包括超时）
        if (result == null) {
            onNetworkBroken();
            return;
        }
        String errorMessage = DS.toString(result.getMessage());

        if (onReturnSpecificErrorCode(result.getState(), errorMessage, result)) {
            return;
        }

        if (!result.isSuccess()) {
            onReturnToastMsgErrorCode(errorMessage);
            return;
        }

        onSuccessReturn(result);
    }

    public final void onCancel() {
        onTaskCancel();
    }

    /*--------------------------------------------------------------------------------------------*/

    public abstract void onTaskStart();

    public abstract void onTaskCancel();

    // task complete 后最先调用这个方法，一般只需要在这里 hideLoadingView，其他的事情在后边的回调方法中做，当然也可以做其他的
    public abstract void onTaskComplete(@Nullable R result, @Nullable Exception e);

    // 一般是弹toast或显示网络错误页
    public abstract void onNetworkBroken();

    // Return true will interrupt the flow of deal HttpResult.
    // 需要对返回的指定的错误码进行特殊处理时，重写此方法
    public boolean onReturnSpecificErrorCode(int errorCode, @NonNull String errorMsg,
                                             @NonNull R result) {
        return false;
    }

    // 返回的非指定的错误码，一般在这弹toast
    // 注意，此时不会走网络错误回调
    public abstract void onReturnToastMsgErrorCode(@NonNull String errorMsg);

    // 真正处理成功返回结果的逻辑
    public abstract void onSuccessReturn(@NonNull R result);
}