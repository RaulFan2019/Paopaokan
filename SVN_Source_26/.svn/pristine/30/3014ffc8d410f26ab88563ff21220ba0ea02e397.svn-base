package com.app.pao.utils;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by LY on 2016/3/30.
 */
public class PageLiaghtUtils {
    //当前屏幕亮度模式
    private static int screenMode;
    //当前屏幕亮度值0-255f
    private static int screenBrightness;

    public static void changeToLightMax(Context context){
        try {
            /*
             * 获得当前屏幕亮度的模式
             * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
             * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
             */
            screenMode = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            // 获得当前屏幕亮度值 0--255
            screenBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            // 如果当前的屏幕亮度调节调节模式为自动调节，则改为手动调节屏幕亮度
            if (screenMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                setScreenMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL,context);
            }
            setScreenBrightness(255.0f,context);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void backToLight(Context context){
        // 设置屏幕亮度值为原来的
        setScreenBrightness(screenBrightness,context);
        // 设置当前屏幕亮度的模式 为原来的
        setScreenMode(screenMode,context);
    }

    /**
     * 设置当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    private static void setScreenMode(int value,Context context) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, value);
    }

    /**
     * 设置当前屏幕亮度值 0--255f，并使之生效
     */
    private static void setScreenBrightness(float value,Context context) {
        Window mWindow = ((Activity)context).getWindow();
        WindowManager.LayoutParams mParams = mWindow.getAttributes();
        float f = value / 255.0F;
        mParams.screenBrightness = f;
        mWindow.setAttributes(mParams);

        // 保存设置的屏幕亮度值
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) value);
    }

}
