package com.irrigate.core.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-03-31.
 */

public class BaseModel implements Parcelable {

    private boolean haveValue;

    /*--------------------------------------------------------------------------------------------*/

    public boolean isEmpty() {
        return !haveValue;
    }

    public boolean isValued() {
        return haveValue;
    }

    public void charge() {
        haveValue = true;
    }

    /**
     * This method just update flag, but do not clear data, be careful.
     */
    public void clear() {
        haveValue = false;
    }

    /*--------------------------------------------------------------------------------------------*/

    public BaseModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.haveValue ? (byte) 1 : (byte) 0);
    }

    protected BaseModel(Parcel in) {
        this.haveValue = in.readByte() != 0;
    }

    public static final Creator<BaseModel> CREATOR = new Creator<BaseModel>() {
        @Override
        public BaseModel createFromParcel(Parcel source) {
            return new BaseModel(source);
        }

        @Override
        public BaseModel[] newArray(int size) {
            return new BaseModel[size];
        }
    };
}