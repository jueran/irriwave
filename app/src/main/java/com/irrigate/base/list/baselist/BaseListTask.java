package com.irrigate.base.list.baselist;

import android.content.Context;

import com.irrigate.core.net.AbNameValuePair;
import com.irrigate.core.net.HQHttpRequest;
import com.irrigate.core.net.TaskListener;

import java.util.List;
import java.util.Map;

/**
 * Created by xinyuanzhong on 2017/6/13.
 */

public abstract class BaseListTask<T> extends HQHttpRequest {
    public static final String START = "start";
    public static final String COUNT = "count";

    protected Map<String, Object> params;

    private OnListApiPagingStateConfirmedCallback onListApiPagingStateConfirmedCallback;

    public BaseListTask(Context context, TaskListener<T> taskListener,
                        Class<T> resultClassType, Map<String, Object> params) {
        super(context, taskListener, resultClassType);
        this.params = params;
    }

    public void setOnListApiPagingStateConfirmedCallback(
            OnListApiPagingStateConfirmedCallback onListApiPagingStateConfirmedCallback) {
        this.onListApiPagingStateConfirmedCallback = onListApiPagingStateConfirmedCallback;
    }

    @Override
    protected final void addParam(List list) {
        addParam(list, params);

        if (onListApiPagingStateConfirmedCallback != null) {
            onListApiPagingStateConfirmedCallback.onConfirmed(isPagingEnabled(list));
        }
    }

    private boolean isPagingEnabled(List list) {
        int paramsCount = list.size();
        for (Object value : list) {
            boolean isKeyParam = ((AbNameValuePair) value).getName().equals(START)
                    || ((AbNameValuePair) value).getName().equals(COUNT);
            if (value instanceof AbNameValuePair && isKeyParam) {
                paramsCount--;
            }
        }
        return paramsCount == list.size() - 2;
    }

    protected abstract void addParam(List<AbNameValuePair> list, Map<String, Object> params);
}
