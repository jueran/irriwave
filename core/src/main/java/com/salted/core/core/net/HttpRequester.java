package com.salted.core.core.net;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.salted.core.BuildConfig;
import com.salted.core.core.util.LogUtil;
import com.salted.core.core.util.SandBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * 说明：
 * 作者：杨健
 * 时间：2017/5/17.
 */
@SuppressWarnings("PMD.GodClass")
public abstract class HttpRequester<T extends HttpResult> implements HQAsyncTask<T> {
    /**
     * The default socket timeout in milliseconds
     */
    public static final int MY_DEFAULT_TIMEOUT_MS = 10 * 1000;
    private static final int LOGOUT_CODE = 40002;

    protected TaskListener<T> mTaskListener;
    /**
     * TaskStatus
     */
    protected TaskStatus status;
    protected final Class<T> mResultClassType;
    Context context;

    public HttpRequester(@Nullable Context ctx, @Nullable TaskListener<T> taskListener,
                         Class<T> resultClassType) {
        this.mTaskListener = taskListener;
        this.status = TaskStatus.INIT;
        this.mResultClassType = resultClassType;
        this.context = ctx;
    }

    @Override
    public void execute() {
        onStarted();

        // 准备数据
        Map<String, String> header = new HashMap<>();
        setHeader(header);
        List<AbNameValuePair> params = new ArrayList<>();
        addParam(params);

        Request.Builder reqBuild = makeRequestBuildWithHeader(header);

        // 构造请求体
        Request request;
        if (isGetMethod()) {
            request = makeGetRequest(params, reqBuild);
        } else {
            request = makePostRequest(params, reqBuild);
        }

        LogUtil.d("http_work", "url: " + request.url().toString());
        // 发起网络请求
        (new OkHttpClient.Builder()
                .connectTimeout(getTimeOutMS(), TimeUnit.MILLISECONDS))
                .readTimeout(getTimeOutMS(), TimeUnit.MILLISECONDS)
                .writeTimeout(getTimeOutMS(), TimeUnit.MILLISECONDS)
                .build().newCall(request).enqueue(getResponseCallback());
    }

    @NonNull
    private Request.Builder makeRequestBuildWithHeader(Map<String, String> header) {
        Set<String> keys = header.keySet();
        Request.Builder reqBuild = new Request.Builder();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (TextUtils.isEmpty(key)) continue;
            if (TextUtils.isEmpty(header.get(key))) continue;
            reqBuild.addHeader(key, header.get(key));
        }
        return reqBuild;
    }

    private Request makeGetRequest(List<AbNameValuePair> params, Request.Builder reqBuild) {
        Request request;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(getHost() + getPath()).newBuilder();
        for (AbNameValuePair pair : params) {
            if (pair.isStringValue()) {
                urlBuilder.addQueryParameter(pair.getName(), pair.getStringValue());
            }
        }
        request = reqBuild.url(urlBuilder.build()).build();
        return request;
    }

    private Request makePostRequest(List<AbNameValuePair> params, Request.Builder reqBuild) {
        Request request;
        int count = 0;
        MultipartBody.Builder multipartBody =
                new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (AbNameValuePair pair : params) {
            if (pair.isStringValue()) {
                multipartBody.addFormDataPart(pair.getName(), pair.getStringValue());
                count++;
            }
            if (pair.isFileValue()) {
                MediaType type = MediaType.parse("application/octet-stream");
                RequestBody fileBody = RequestBody.create(type, pair.getFileValue());
                multipartBody.addFormDataPart(pair.getName(),
                        pair.getFileValue().getName(), fileBody);
                count++;
            }
        }
        if (count > 0) {
            request = reqBuild.url(getHost() + getPath()).post(multipartBody.build()).build();
        } else {
            request = reqBuild.url(getHost() + getPath())
                    .post(okhttp3.internal.Util.EMPTY_REQUEST).build();
        }
        return request;
    }


    @NonNull
    private Callback getResponseCallback() {
        return new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, final IOException e) {
                if (BuildConfig.DEBUG) Log.e("http_work", e.toString());
                onCompleted(null, e);
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response)
                    throws IOException {
                T result = null;
                Exception exception = null;
                try {
                    String body = response.body().string();
                    result = new Gson().fromJson(body, mResultClassType);
                    if (BuildConfig.DEBUG) Log.e("http_work", new Gson().toJson(result));
                    // 当前业务类的异常
                    if (result == null) {
                        exception = new IOException(body);
                    } else if (!result.isSuccess(result)) {
                        exception = new HQException(result.getState(), result.getMessage());
                    }
                } catch (Exception e) {
                    exception = e;
                } finally {
                    onCompleted(result, exception);
                }
            }

        };
    }

    public TaskStatus getStatus() {
        return status;
    }


    protected int getTimeOutMS() {
        return MY_DEFAULT_TIMEOUT_MS;
    }

    @Override
    public void cancel() {
        onCanceled();
    }

    public TaskListener<T> getTaskListener() {
        return mTaskListener;
    }

    @Override
    public void setTaskListener(TaskListener<T> listener) {
        this.mTaskListener = listener;
    }

    protected abstract void setHeader(Map<String, String> header);

    /**
     * POST or GET 请求
     *
     * @return
     */
    protected boolean isGetMethod() {
        return true;
    }

    protected abstract void addParam(List<AbNameValuePair> list);

    /**
     * 获取url路径，带最前面的/
     *
     * @return
     */
    protected abstract String getPath();

    /**
     * 获取主机地址，结尾不带/
     *
     * @return
     */
    protected abstract String getHost();

    @CallSuper
    protected boolean onStarted() {
        boolean startAble = true;
        switch (status) {
            case INIT:
                startAble = true;
                break;
            default:
                startAble = false;
                break;
        }
        if (!startAble) {
            if (BuildConfig.DEBUG) {
                throw new IllegalStateException("不要重复启动同一个任务");
            } else {
                startAble = true;
            }
        }
        status = TaskStatus.STARTED;
        if (mTaskListener != null) {
            SandBox.start(new Runnable() {
                @Override
                public void run() {
                    mTaskListener.onTaskStart(mTaskListener);
                }
            });
        }
        return startAble;
    }

    @CallSuper
    protected boolean onCanceled() {
        boolean cancelAble;
        switch (status) {
            case INIT:
            case STARTED:
                cancelAble = true;
                break;
            default:
                cancelAble = false;
                break;
        }
        if (!cancelAble) return false;
        status = TaskStatus.CANCELED;
        if (mTaskListener != null) {
            SandBox.start(new Runnable() {
                @Override
                public void run() {
                    mTaskListener.onCancel();
                }
            });
        }
        mTaskListener = null;
        return cancelAble;
    }

    @CallSuper
    protected boolean onCompleted(final T result, final Exception e) {
        boolean completeAble = true;
        switch (status) {
            case INIT:
            case STARTED:
                completeAble = true;
                break;
            default:
                completeAble = false;
                break;
        }
        if (!completeAble) return false;
        status = TaskStatus.COMPLETED;
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (result != null) {
                    if (result.getState() == LOGOUT_CODE) {
                        if (hasSession()) {
                            // 不交给mTaskListener，直接处理掉
                            onSessionOutTime(getHost() + getPath());
                            return;
                        } else {
                            onIllegalRequestWithoutSession(getHost() + getPath());
                        }
                    } else if (isInterceptionCode(result.getState())) {
                        doSomeThingWithCodeInterception(result.getState(), context);
                        return;
                    }
                }
                // 如果Activity在destroy前没有调用cancel,这里很可能发生奔溃
                // 加入try，这样即使忘记cancel也不会奔溃
                if (mTaskListener != null) {
                    SandBox.start(new Runnable() {
                        @Override
                        public void run() {
                            mTaskListener.onTaskComplete(mTaskListener, result, e);
                        }
                    });
                }
            }
        });
        return completeAble;
    }

    protected abstract boolean isInterceptionCode(int code);

    protected abstract void doSomeThingWithCodeInterception(int code, Context context);

    protected abstract boolean hasSession();

    protected abstract void onSessionOutTime(String url);

    protected abstract void onIllegalRequestWithoutSession(String url);
}
