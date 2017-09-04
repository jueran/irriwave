package com.irrigate.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.irrigate.core.net.HQException;
import com.irrigate.core.util.EncodingUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by htyuan on 15-7-2.
 */
public class Utils {
    private static final String TAG = "Utils";
    private static long SECOND_RATE = 1000;

    public static <T extends View> T bindView(View rootView, int resId) {
        if (rootView != null) {
            View view = rootView.findViewById(resId);
            return (T) view;
        }
        return null;
    }

    public static void entryActivity(Activity context, Class<?> classzz) {
        entryActivity(context, classzz, null);
    }

    public static void entryActivity(Activity context, Class<?> classzz, Bundle bundle) {
        if (context != null) {
            Intent intent = new Intent(context, classzz);
            intent.putExtra("", "");
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            context.startActivity(intent);
        }
    }

    public static int parseInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Long parseLong(String str, long defaultValue) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static double parseDouble(String str, double defaultValue) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static float parseFloat(String str, float defaultValue) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String converFoatToString(double f, int num) {
        StringBuffer sb = new StringBuffer();
        sb.append("%.");
        sb.append(num);
        sb.append("f");
        return String.format(sb.toString(), f);
    }

    public static void dial(Context context, String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data = Uri.parse("tel:" + tel);
        intent.setData(data);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("dial", "用户拨打电话异常" + tel);
        }
    }

    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            in.close();
            result = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getStringFromFile(String fileName) {
        String str="";

        File file=new File(fileName);
        try {
            FileInputStream in = new FileInputStream(file);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            str = EncodingUtils.getString(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return str;
    }

    public static String getPath(String name, String version, String action) {
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(name)) {
            sb.append("/");
        }
        sb.append(name);
        if (!TextUtils.isEmpty(version)) {
            sb.append("/");
        }
        sb.append(version);
        if (!TextUtils.isEmpty(action)) {
            sb.append("/");
        }
        sb.append(action);

        return sb.toString();
    }

    public static final String SYS_PROP_MOD_VERSION = "ro.modversion";

    public static String getModVersion() {
        String modVer = getSystemProperty(SYS_PROP_MOD_VERSION);

        return (modVer == null || modVer.length() == 0 ? "Unknown" : modVer);
    }

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }

    public static String getMD5(String data) {
        return encode(data, "MD5");
    }

    public static String getSha1(String data) {
        return encode(data, "SHA1");
    }

    /**
     * encode string data
     *
     * @param data
     * @param algorithm MD5  SHA1
     * @return
     */
    private static String encode(String data, String algorithm) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance(algorithm);
            digest.update(data.getBytes());
            byte md[] = digest.digest();
            StringBuilder sb = new StringBuilder(md.length * 2);
            for (int i = 0; i < md.length; i++) {
                sb.append(Integer.toHexString((md[i] & 0xf0) >>> 4));
                sb.append(Integer.toHexString(md[i] & 0x0f));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String generateToken(String secret, String platform, String cid, long time, String httUri) {
        String strA = platform + "-" + secret + "-" + time + "-" + httUri;
        String srtB = encode(strA.toLowerCase(), "SHA1");
        String strC = srtB + "-" + cid;
        String token = encode(strC.toLowerCase(), "MD5");
        return token;
    }

    public static String printBytes(byte[] bytes) {
        StringBuffer str = new StringBuffer();
        str.append("Ox");
        if (bytes != null) {
            for (byte value : bytes) {
                str.append(Integer.toHexString(value));
            }
        } else {
            str.append("0");
        }
        System.out.println(str);
        return str.toString();
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        if (TextUtils.isEmpty(a) && TextUtils.isEmpty(b)) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 毫秒转化为秒
     * @param l
     * @return
     */
    public static long getSecond(long l) {
        return l / SECOND_RATE;
    }

    public static boolean isNetException(Exception e) {
        return e != null && !(e instanceof HQException);
    }
}
