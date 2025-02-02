package com.app.pao.exception;

import android.os.Environment;
import android.os.Handler;
import android.text.format.DateFormat;

import com.app.pao.ActivityStackManager;
import com.app.pao.LocalApplication;
import com.app.pao.config.AppConfig;
import com.app.pao.config.AppEnum;
import com.app.pao.utils.Log;
import com.app.pao.utils.TimeUtils;

import org.xutils.x;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 捕捉没有抓住的Exception
 */
public abstract class BaseExceptionHandler implements UncaughtExceptionHandler {


    private static final String TAG =  "BaseExceptionHandler";

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);

        // Inject some info about android version and the device, since google
        // can't provide them in the developer console
        StackTraceElement[] trace = ex.getStackTrace();
        StackTraceElement[] trace2 = new StackTraceElement[trace.length + 3];
        System.arraycopy(trace, 0, trace2, 0, trace.length);
        trace2[trace.length + 0] = new StackTraceElement("Android", "MODEL",
                android.os.Build.MODEL, -1);
        trace2[trace.length + 1] = new StackTraceElement("Android", "VERSION",
                android.os.Build.VERSION.RELEASE, -1);
        trace2[trace.length + 2] = new StackTraceElement("Android",
                "FINGERPRINT", android.os.Build.FINGERPRINT, -1);
        ex.setStackTrace(trace2);

        ex.printStackTrace(printWriter);
        final String stacktrace = result.toString();
        printWriter.close();

        // Save the log on SD card if available
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
//            Log.v(TAG,"ready write");
            final String sdcardPath = AppConfig.CRASH_PATH;
            File crashPath = new File(sdcardPath);
            if (!crashPath.exists()) {
                crashPath.mkdir();
            }
//            if (AppConfig.BUILD_TYPE == AppEnum.BuildType.BUILD_ALPHA) {
            writeLog(stacktrace, sdcardPath + "/");
//            writeLogcat(sdcardPath + "/hwh_logcat");
//            }
        }
        ActivityStackManager.getAppManager().finishAllActivity();
        handleExeption(ex);
    }

    /**
     * /**
     * 自定义错误处理，收集错误信息发送错误报告等，自己根据情况处理异常逻辑
     *
     * @param throwable
     * @return
     */
    public abstract boolean handleExeption(Throwable throwable);

    private void writeLog(String log, String name) {
//        Log.v(TAG, log);
        String timestamp = System.currentTimeMillis() + "";
        String filename = name + timestamp;
        try {
            FileOutputStream stream = new FileOutputStream(filename);
            OutputStreamWriter output = new OutputStreamWriter(stream);
            BufferedWriter bw = new BufferedWriter(output);

            bw.write(log);
            bw.newLine();

            bw.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
//            Log.v(TAG,e.toString());
        }
    }

    private void writeLogcat(String name) {
        CharSequence timestamp = DateFormat.format("yyyyMMdd_kkmmss",
                System.currentTimeMillis());
        String filename = name + "_" + timestamp + ".log";
        String[] args = {"logcat", "-v", "time", "-d"};

        try {
            Process process = Runtime.getRuntime().exec(args);
            InputStreamReader input = new InputStreamReader(
                    process.getInputStream());
            OutputStreamWriter output = new OutputStreamWriter(
                    new FileOutputStream(filename));
            BufferedReader br = new BufferedReader(input);
            BufferedWriter bw = new BufferedWriter(output);
            String line;

            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
            }
            bw.close();
            output.close();
            br.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
