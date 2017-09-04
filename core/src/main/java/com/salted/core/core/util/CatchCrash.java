package com.salted.core.core.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by htyuan on 15-5-25.
 */
@SuppressLint("StaticFieldLeak")
public class CatchCrash implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CatchCrash";
    private Thread.UncaughtExceptionHandler mDefUncaughtExceptionHandler;
    private static CatchCrash sInstance = new CatchCrash();
    private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private Context context;

    public void init(Context context) {
        this.context = context;
        mDefUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
//        if(!handleException(ex) && (mDefUncaughtExceptionHandler != null)){
//            mDefUncaughtExceptionHandler.uncaughtException(thread, ex);
//        }else{
//
//        }
        handleException(ex);
        mDefUncaughtExceptionHandler.uncaughtException(thread, ex);

        //exit process
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public static CatchCrash getInstance() {
        return sInstance;
    }


    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }

        String info = getStackTraceInfo(ex);

        saveCrashInfo2File(info);
        return true;
    }

    private String saveCrashInfo2File(String info) {
        long timeStamp = System.currentTimeMillis();

        String time = mFormatter.format(new Date());
        String fileName = "hq-crash-" + time + "-" + timeStamp + ".txt";
        try {
            if (Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED)) {
                String path =
                        AppFileUtil.CARSH_DIRECTORY + context.getPackageName().replace(".", "")
                                + File.separator;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = null;
                fos = new FileOutputStream(path + fileName);
                fos.write(info.getBytes());
                fos.close();
            }
            return fileName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    private String getStackTraceInfo(Throwable ex) {
        StringBuffer sb = new StringBuffer();

        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        return sb.toString();
    }

}
