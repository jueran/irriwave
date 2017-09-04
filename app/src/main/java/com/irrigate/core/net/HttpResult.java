package com.irrigate.core.net;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

/**
 * 网络请求的默认返回
 *
 * @author htyuan
 */
@SuppressWarnings("PMD.SuspiciousConstantFieldName")
public class HttpResult {
    public static final int RESULT_OK = 200;
    @SerializedName("errcode")
    private int mStat = RESULT_OK;
    @SerializedName("errmsg")
    private String mMessage;

    private String errtime;

    public HttpResult() {
    }

    public void setState(int stat) {
        mStat = stat;
    }

    public int getState() {
        return mStat;
    }

    public boolean isSuccess() {
        return mStat == RESULT_OK;
    }

    public static boolean isSuccess(HttpResult result) {
        if (result == null) {
            return false;
        }
        return result.isSuccess();
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage == null ? "" : mMessage;
    }

    @Override
    public String toString() {
        return "HttpResult{"
                + "mStat=" + mStat
                + ", mMessage='" + mMessage + '\''
                + ", errtime='" + errtime + '\''
                + '}';
    }

    protected HttpResult(Parcel in) {
        this.mStat = in.readInt();
        this.mMessage = in.readString();
        this.errtime = in.readString();
    }

}