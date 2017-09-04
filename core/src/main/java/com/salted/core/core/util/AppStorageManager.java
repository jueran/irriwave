package com.salted.core.core.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by ldf on 17/7/17.
 * 主要功能： 提供App数据清理功能
 * 可以代替使用系统应用信息里面手动清除的方式
 */
@SuppressWarnings("checkstyle:MagicNumber")
public final class AppStorageManager {

    private AppStorageManager() {
    }

    /**
     * 清除本应用内部缓存数据(/data/data/com.xxx.xxx/cache)
     *
     * @param context 上下文
     * @return void
     */
    public static void cleanInternalCache(Context context) {
        AppFileUtil.deleteFilesByDirectory(context.getCacheDir());
        LogUtil.i("AppCleanMgr->>cleanInternalCache", "清除本应用内部缓存(/data/data/" +
                context.getPackageName() + "/cache)");
    }


    /**
     * 清除本应用外部缓存数据(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context 上下文
     * @return void
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            AppFileUtil.deleteFilesByDirectory(context.getExternalCacheDir());
            LogUtil.i("AppCleanMgr->>cleanExternalCache",
                    "清除本应用外部缓存数据(/mnt/sdcard/android/data/"
                            + context.getPackageName() + "/cache)");
        }
    }


    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
     *
     * @param context 上下文
     * @return void
     */
    public static void cleanDatabases(Context context) {
        AppFileUtil.deleteFilesByDirectory(new File(context.getFilesDir().getPath()
                + context.getPackageName() + "/databases"));
        LogUtil.i("AppCleanMgr->>cleanDatabases", "清除本应用所有数据库");
    }


    /**
     * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
     *
     * @param context 上下文
     * @return void
     */
    public static void cleanSharedPreference(Context context) {
        AppFileUtil.deleteFilesByDirectory(new File(context.getFilesDir().getPath()
                + context.getPackageName() + "/shared_prefs"));
        LogUtil.i("AppCleanMgr->>cleanSharedPreference", "清除本应用cleanSharedPreference数据信息");
    }


    /**
     * 根据名字清除本应用数据库
     *
     * @param context 上下文
     * @param dbName
     * @return void
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
        LogUtil.i("AppCleanMgr->>cleanDatabaseByName", "根据名字清除本应用数据库");
    }


    /**
     * 清除本应用files文件(/data/data/com.xxx.xxx/files)
     *
     * @param context 上下文
     * @return void
     */
    public static void cleanFiles(Context context) {
        AppFileUtil.deleteFilesByDirectory(context.getFilesDir());
        LogUtil.i("AppCleanMgr->>cleanFiles", "清除data/data/"
                + context.getPackageName() + "/files下的内容信息");
    }


    /**
     * 清除本应用所有的数据
     *
     * @param context 上下文
     * @return int
     */
    public static int cleanApplicationData(Context context) {
        //清除本应用内部缓存数据
        cleanInternalCache(context);
        //清除本应用外部缓存数据
        cleanExternalCache(context);
        //清除本应用SharedPreference
        cleanSharedPreference(context);
        //清除本应用files文件
        cleanFiles(context);
        LogUtil.i("AppCleanMgr->>cleanApplicationData", "清除本应用所有的数据");
        return 1;
    }


    /**
     * 获取App应用缓存的大小
     *
     * @param context 上下文
     * @return String
     */
    @SuppressWarnings("PMD")
    public static String getAppClearSize(Context context) {
        long clearSize = 0;
        String fileSizeStr = "";
        DecimalFormat df = new DecimalFormat("0.00");
        //获得应用内部缓存大小
        clearSize = AppFileUtil.getFileSize(context.getCacheDir());
        //获得应用SharedPreference缓存数据大小
        clearSize += AppFileUtil.getFileSize(new File(context.getFilesDir().getPath()
                + context.getPackageName() + "/shared_prefs"));
        //获得应用data/data/com.xxx.xxx/files下的内容文件大小
        clearSize += AppFileUtil.getFileSize(context.getFilesDir());
        //获取应用外部缓存大小
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            clearSize += AppFileUtil.getFileSize(context.getExternalCacheDir());
        }
        if (clearSize > 5000) {
            //转换缓存大小Byte为MB
            fileSizeStr = df.format((double) clearSize / 1048576) + "MB";
        }
        LogUtil.i("AppCleanMgr->>getAppClearSize", "获取App应用缓存的大小");
        return fileSizeStr;
    }
}
