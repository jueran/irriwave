package com.salted.core.core.net;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.salted.core.R;

import junit.framework.Assert;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by htyuan on 15-6-25.
 */
public abstract class HQHttpRequest<T extends HttpResult> extends HttpRequester<T> {
    public static HQHttpRequestHelper hqHttpRequestHelper;
    protected Context ctx;
    private static final String TAG = "HQHttpRequest";

    public HQHttpRequest(@Nullable Context ctx, @Nullable TaskListener<T> taskListener,
                         Class<T> resultClassType) {
        super(ctx, taskListener, resultClassType);
        this.ctx = ctx;
    }

    @CallSuper
    protected void setHeader(Map<String, String> header) {
        hqHttpRequestHelper.setHeader(header, getPath());
    }

    protected String getHost() {
        return hqHttpRequestHelper.getHost(ctx);
    }

    @Override
    public void execute() {
        this.execute(true);
    }

    public void execute(boolean savedToStack) {
        super.execute();
        if (savedToStack) {
            saveToStack(ctx);
        }
    }

    @Override
    public void cancel() {
        synchronized (ctx) {
            super.cancel();
            removeThisFromStack();
        }
    }


    protected final void saveToStack(Context ctx) {
        synchronized (ctx) {
            List<HQHttpRequest> requestStack = getRequestsStack(ctx);
            requestStack.add(this);
        }
    }

    public static synchronized boolean isActivityHasRequesting(Context context) {
        synchronized (context) {
            List<HQHttpRequest> requestStack = getRequestsStack(context);
            if (requestStack.isEmpty()) {
                return false;
            }
            for (int i = 0; i < requestStack.size(); i++) {
                HQHttpRequest first = requestStack.get(i);
                if (first.getStatus() == TaskStatus.STARTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static synchronized void cancelActivityAllRequests(Context context) {
        synchronized (context) {
            List<HQHttpRequest> requestStack = getRequestsStack(context);
            while (!requestStack.isEmpty()) {
                HQHttpRequest first = requestStack.get(0);
                requestStack.remove(first);
                first.cancel();
            }
        }
    }

    @NonNull
    private static List<HQHttpRequest> getRequestsStack(Context context) {
        synchronized (context) {
            Assert.assertTrue("不是Activity,就不会有UI,在方法执行时，考虑用execute(ctx, false)",
                    context instanceof Activity);
            Activity activity = (Activity) context;
            View decorView = activity.getWindow().getDecorView();
            Assert.assertNotNull("没有UI的Activity就没有必要RequestStack", decorView);
            List<HQHttpRequest> requestStack;
            Object tag = decorView.getTag(R.id.tag_request_stack);
            if (tag != null) {
                Assert.assertTrue("tag_request_stack的Tag被占用了,无法操作stack",
                        tag instanceof List);
                requestStack = (List<HQHttpRequest>) tag;
            } else {
                requestStack = Collections.synchronizedList(new LinkedList<HQHttpRequest>());
                decorView.setTag(R.id.tag_request_stack, requestStack);
            }
            return requestStack;
        }
    }

    protected synchronized void removeThisFromStack() {
        if (this.ctx == null) {
            return;
        }
        synchronized (this.ctx) {
            if (this.ctx instanceof Activity) {
                List<HQHttpRequest> requestsStack = getRequestsStack(this.ctx);
                requestsStack.remove(this);
            }
        }
    }

    @Override
    protected int getTimeOutMS() {
        return hqHttpRequestHelper.getTimeOut();
    }

    @Override
    protected boolean onCanceled() {
        boolean cancelAble = super.onCanceled();
        if (cancelAble) {
            removeThisFromStack();
        }
        return cancelAble;
    }

    @Override
    protected boolean onCompleted(T result, final Exception e) {
        boolean completeAble = super.onCompleted(result, e);
        if (completeAble) {
            removeThisFromStack();
        }
        return completeAble;
    }

    @Override
    protected boolean hasSession() {
        return hqHttpRequestHelper.hasSession();
    }

    protected void onSessionOutTime(String url) {
        hqHttpRequestHelper.onSessionOutTime(context);
        Log.e(TAG, "存在过期的网络请求" + url);
    }

    @Override
    protected void onIllegalRequestWithoutSession(String url) {
        Log.e(TAG, "存在没有登录时的网络请求" + url);
    }

    @Override
    protected boolean isInterceptionCode(int code) {
        return hqHttpRequestHelper.isInterceptionCode(code);
    }

    @Override
    protected void doSomeThingWithCodeInterception(int code, Context context) {
        hqHttpRequestHelper.doSomeThingWithCodeInterception(code, context);
    }
}