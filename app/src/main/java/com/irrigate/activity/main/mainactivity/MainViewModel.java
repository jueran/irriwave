package com.irrigate.activity.main.mainactivity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Bear MVP BaseActivity Template.
 */

public class MainViewModel implements Parcelable {


    /*--------------------------------------------------------------------------------------------*/

    public static final Parcelable.Creator<MainViewModel> CREATOR = new Parcelable.Creator<MainViewModel>() {
        @Override
        public MainViewModel createFromParcel(Parcel source) {
            return new MainViewModel(source);
        }

        @Override
        public MainViewModel[] newArray(int size) {
            return new MainViewModel[size];
        }
    };

    public MainViewModel() {
    }

    protected MainViewModel(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
