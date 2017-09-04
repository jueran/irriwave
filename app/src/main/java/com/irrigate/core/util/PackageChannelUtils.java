package com.irrigate.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * 说明：
 * 作者：杨健
 * 时间：2017/3/10.
 */

public final class PackageChannelUtils {
    private static final String FIRST_PACKAGE_CHANNEL_FILE = "PACKAGE_CHANNEL_FILE";
    private static final String FIRST_PACKAGE_CHANNEL = "FIRST_PACKAGE_CHANNEL";

    private PackageChannelUtils() {
    }

    public static String getPackageChannel(Context c) {
        String packageChannel = "";
        try {
            PackageManager manager = c.getPackageManager();
            ApplicationInfo info = manager.getApplicationInfo(
                    c.getPackageName(), PackageManager.GET_META_DATA);
            packageChannel = info.metaData.getString("PACKAGE_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageChannel;
    }

    public static String getFirstPackageChannel(Context c) {
        SharedPreferences preferences = c.getSharedPreferences(FIRST_PACKAGE_CHANNEL_FILE, 0);
        String firstPackageChannel = preferences.getString(FIRST_PACKAGE_CHANNEL, "");
        if(TextUtils.isEmpty(firstPackageChannel)) {
            firstPackageChannel = getPackageChannel(c);
            setFirstPackageChannel(c, firstPackageChannel);
        }
        return firstPackageChannel;
    }

    private static void setFirstPackageChannel(Context context, String channel) {
        SharedPreferences preferences = context.getSharedPreferences(FIRST_PACKAGE_CHANNEL_FILE, 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(FIRST_PACKAGE_CHANNEL, channel);
        edit.apply();
    }
}
