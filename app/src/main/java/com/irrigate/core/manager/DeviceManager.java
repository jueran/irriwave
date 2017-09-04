package com.irrigate.core.manager;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

import java.util.UUID;

/**
 * 获取设备相关属性和信息;
 * todo move to com.hqyxjy.core.helper.systemadvance
 *
 * @author Administrator
 */
@SuppressWarnings(value = {"PMD.NonThreadSafeSingleton",
        "PMD.SuspiciousConstantFieldName", "PMD.DoubleCheckedLocking"})
public class DeviceManager {
    private Context ctx;
    private static String mResolution;
    private static String mIMEI = "";
    private static String mIMSI = "";

    public DeviceManager(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * 获取设备分辨率; 格式样例: "800*480"
     *
     * @return
     */
    public String getResolution() {
        if (TextUtils.isEmpty(mResolution)) {
            WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                Display display = wm.getDefaultDisplay();
                mResolution = display.getHeight() + "*" + display.getWidth();
            }
        }
        return mResolution;
    }

    /**
     * 获取设备的IMEI标识.
     *
     * @return 设备的IMEI标识.
     */
    public String getIMEI() {
        if (TextUtils.isEmpty(mIMEI)) {
            TelephonyManager tm =
                    (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                mIMEI = tm.getDeviceId();
            }
        }
        return mIMEI;
    }

    /**
     * 获取设备的IMSI标识.
     *
     * @return 设备的IMSI标识
     */
    public String getIMSI() {
        if (TextUtils.isEmpty(mIMSI)) {
            TelephonyManager tm =
                    (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                mIMSI = tm.getSubscriberId();
            }
        }
        return mIMSI;
    }

    private static String deviceId;

    public String getDeviceId() {
        // 已经获取过后，不再获取
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = computeDeviceId();
        }
        return deviceId;
    }

    private String computeDeviceId() {
        // 兼容1.6.0及之前但版本，之前但版本是用imei/imsi。如果直接替换为新的值，就把会原来的信息进行重复统计
        String imei = getIMEI();
        if (!TextUtils.isEmpty(imei)) {
            return imei;
        }
        String imsi = getIMSI();
        if (!TextUtils.isEmpty(imsi)) {
            return imsi;
        }

        return getUniquePsuedoID();
    }

    // 算法的核心在于每部手机的serial不同，但依然不能保证绝对正确
    private static String getUniquePsuedoID() {
        String serial;
        String mszDevIDShort = "35"
                + Build.BOARD.length() % 10 + Build.BRAND.length() % 10
                + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
                + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
                + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
                + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
                + Build.TAGS.length() % 10 + Build.TYPE.length() % 10
                + Build.USER.length() % 10; //13 位

        serial = Build.SERIAL;
        if (TextUtils.isEmpty(serial)) {
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息与serial拼凑出来的15位号码
        return new UUID(mszDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static String getNetStatus() {
        return "gprs";
    }
}