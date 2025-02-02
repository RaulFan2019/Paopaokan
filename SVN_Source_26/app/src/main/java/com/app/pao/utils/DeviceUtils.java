package com.app.pao.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.app.pao.LocalApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Raul on 2015/10/29.
 * 有关于设备的工具
 */
public class DeviceUtils {


    /**
     * 获取版本Code
     *
     * @param packageName
     * @return
     */
    public static int getVersionCode(String packageName) {
        int versionCode = 0;
        try {
            versionCode = LocalApplication.applicatonContext.getPackageManager().getPackageInfo(packageName,
                    0).versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            versionCode = 0;
        }
        return versionCode;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static float getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static float getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }


    /**
     * 获取屏幕分辨率
     *
     * @return
     */
    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) LocalApplication.applicatonContext.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay()
                .getMetrics(displaymetrics);
        return displaymetrics;
    }

    /**
     * dp转化为像素
     *
     * @param dp
     * @return
     */
    public static float dpToPixel(float dp) {
        return dp * (getDisplayMetrics().densityDpi / 160F);
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


    /**
     * 安装APK
     *
     * @param context
     * @param file
     */
    public static void installAPK(Context context, File file) {
        if (file == null || !file.exists())
            return;
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 获取SIM卡所在地区
     *
     * @param mContext
     * @return
     */
    public static String GetCountryIso(Context mContext) {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE);
        return tm.getSimCountryIso();
    }


    public static String getTotalMemory() {
        String str1 = "/proc/meminfo";
        String str2 = "";
        String info = "";
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            while ((str2 = localBufferedReader.readLine()) != null) {
                info += "\n---" + str2;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return info;
    }

    /**
     * 获取电量Intent
     */
    public static Intent getBatterPower(Context context) {
        Intent batteryInfoIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//        Log.i(TAG, "当前电量：" + batteryInfoIntent.getIntExtra("level", 0));
//        Log.i(TAG, "总电量：" + batteryInfoIntent.getIntExtra("scale", 100));
        return batteryInfoIntent;
    }


    /**
     * 获取屏幕旋转方向
     *
     * @param context
     * @return
     */
    public static boolean isScreenChange(Context context) {
        Configuration mConfiguration = context.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            //横屏
            return true;
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            //竖屏
            return false;
        }
        return false;
    }

    /**
     * 检查服务是否在工作
     *
     * @param context
     * @param serviceName
     * @return
     */
    public static boolean isWorked(final Context context, final String serviceName) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(1000);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
}
