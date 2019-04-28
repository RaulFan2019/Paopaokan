package com.app.pao.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.app.pao.config.AppEnum;

/**
 * Created by Raul on 2015/9/25.
 * SharedPreferences 数据处理
 */
public class PreferencesData {

    /**
     * contains
     **/
    private static final String TAG = "PreferencesData";

    /**
     * 获取Preference设置
     */
    public static SharedPreferences getSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 保存登录用户ID
     *
     * @param context
     * @param userId
     */
    public static void setUserId(final Context context, final int userId) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt("userId", userId);
        editor.apply();
    }

    /**
     * 获取登录用户ID
     *
     * @param context
     * @return
     */
    public static int getUserId(final Context context) {
        return getSharedPreferences(context).getInt("userId", AppEnum.DEFAULT_USER_ID);
    }

    /**
     * 获取登录用名
     *
     * @param context
     * @return
     */
    public static String getUserName(final Context context) {
        return getSharedPreferences(context).getString("userName", AppEnum.DEFAULT_USER_NAME);
    }

    /**
     * 保存登录用户名
     *
     * @param context
     * @param UserName
     */
    public static void setUserName(final Context context, final String UserName) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("userName", UserName);
        editor.apply();
    }

    /**
     * 获取登录密码
     *
     * @param context
     * @return
     */
    public static String getPassword(final Context context) {
        return getSharedPreferences(context).getString("password", AppEnum.DEFAULT_USER_PASSWORD);
    }

    /**
     * 保存登录密码
     *
     * @param context
     * @param password
     */
    public static void setPassword(final Context context, final String password) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("password", password);
        editor.apply();
    }


    /**
     * 获取登录状态
     *
     * @param context
     * @return
     */
    public static boolean getHasLogin(final Context context) {
        return getSharedPreferences(context).getBoolean("hasLogin", true);
    }

    /**
     * 设置APP是否已登录
     *
     * @param context
     * @param hasLogin
     */
    public static void setHasLogin(final Context context, final boolean hasLogin) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean("hasLogin", hasLogin);
        editor.apply();
    }

    /**
     * 设置当前版本号
     *
     * @param context
     * @param version
     */
    public static void setOldVersion(final Context context, final int version) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt("version", version);
        editor.apply();
    }

    /**
     * 获取记录版本号(用于判断显示引导页)
     *
     * @param context
     * @return
     */
    public static int getOldVersion(final Context context) {
        return getSharedPreferences(context).getInt("version", AppEnum.DEFAULT_OLD_VERSION_CODE);
    }

    /**
     * 存储蓝牙BLe连接的设备
     *
     * @param context
     * @param deviceMac
     */
    public static void setBlueToothDeviceMac(Context context, String deviceMac, String deviceName) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("deviceMac", deviceMac);
        editor.putString("deviceName", deviceName);
        editor.apply();
    }

    /**
     * 获取存储的ble蓝牙设备MAC
     *
     * @param context
     * @return
     */
    public static String getBlueToothDeviceMac(Context context) {
        return getSharedPreferences(context).getString("deviceMac", AppEnum.DEFAULT_BLE_MAC);
    }

    /**
     * 获取存储的ble蓝牙设备Name
     *
     * @param context
     * @return
     */
    public static String getBlueToothDeviceName(Context context) {
        return getSharedPreferences(context).getString("deviceName", null);
    }

    /**
     * 设置声音是否可用
     *
     * @param context
     * @param VoiceEnable false-关闭声音 true-开启声音
     * @return
     */
    public static void setVoiceEnable(Context context, boolean VoiceEnable) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean("VoiceEnable", VoiceEnable);
        editor.apply();
    }

    /**
     * 获取声音是否可用
     *
     * @param context
     * @return false-关闭声音 true-开启声音
     */
    public static boolean getVoiceEnable(Context context) {
        return getSharedPreferences(context).getBoolean("VoiceEnable", true);
    }

    /**
     * 设置跑步语音是否可用
     *
     * @param context
     * @param VoiceEnable false-关闭声音 true-开启声音
     * @return
     */
    public static void setRunningVoiceEnable(Context context, boolean VoiceEnable) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean("RunningVoiceEnable", VoiceEnable);
        editor.apply();
    }

    /**
     * 获取跑步语音是否可用
     *
     * @param context
     * @return false-关闭声音 true-开启声音
     */
    public static boolean getRunningVoiceEnable(Context context) {
        return getSharedPreferences(context).getBoolean("RunningVoiceEnable", true);
    }

    /**
     * 设置是否纯GPS定位模式
     *
     * @param context
     * @param Enable  false-高经度定位 true-纯GPS定位
     * @return
     */
    public static void setOnlyIsGpsLocMode(Context context, boolean Enable) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean("gpsMode", Enable);
        editor.apply();
    }

    /**
     * 获取是否纯GPS定位模式
     *
     * @param context
     * @return false-关闭声音 true-开启声音
     */
    public static boolean getOnlyIsGpsLocMode(Context context) {
        return getSharedPreferences(context).getBoolean("gpsMode", false);
    }

    /**
     * 设置是否直播
     *
     * @param context
     * @param VoiceEnable false-关闭声音 true-开启声音
     * @return
     */
    public static void setLiveEnable(Context context, boolean VoiceEnable) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean("LiveEnable", VoiceEnable);
        editor.apply();
    }

    /**
     * 获取声音是否可用
     *
     * @param context
     * @return false-关闭声音 true-开启声音
     */
    public static boolean getLiveEnable(Context context) {
        return getSharedPreferences(context).getBoolean("LiveEnable", true);
    }

    /**
     * 设置是否判断蓝牙
     *
     * @param context
     * @param Enable  true-判断 false-不判断
     * @return
     */
    public static void setBleEnable(Context context, boolean Enable) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean("BleEnable", Enable);
        editor.apply();
    }

    /**
     * 获取是否判断蓝牙
     *
     * @param context
     * @return true-自动暂停 false-关闭自动暂停
     */
    public static boolean getBleEnable(Context context) {
        return getSharedPreferences(context).getBoolean("BleEnable", true);
    }

    /**
     * 设置是否接受通知
     *
     * @param context
     * @param VoiceEnable false-关闭 true-开启
     * @return
     */
    public static void setJpushEnable(Context context, boolean VoiceEnable) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean("JpushEnable", VoiceEnable);
        editor.apply();
    }

    /**
     * 获取是否接受通知
     *
     * @param context
     * @return false-关闭 true-开启
     */
    public static boolean getJpushEnable(Context context) {
        return getSharedPreferences(context).getBoolean("JpushEnable", true);
    }

    /**
     * 设置升级内容
     *
     * @param context
     * @return
     */
    public static void setUpdateInfo(Context context, String updateInfo) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("updateinfo", updateInfo);
        editor.apply();
    }

    /**
     * 获取升级内容
     *
     * @param context
     * @return
     */
    public static String getUpdateInfo(Context context) {
        return getSharedPreferences(context).getString("updateinfo", "");
    }

    /**
     * 设置首屏广告
     *
     * @param context
     * @return
     */
    public static void setAdPath(Context context, String path) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("AdPath", path);
        editor.apply();
    }

    /**
     * 获取首屏广告
     *
     * @param context
     * @return
     */
    public static String getAdPath(Context context) {
        return getSharedPreferences(context).getString("AdPath", "");
    }

    /**
     * 设置首屏广告
     *
     * @param context
     * @return
     */
    public static void setAdUrl(Context context, String path) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("AdUrl", path);
        editor.apply();
    }

    /**
     * 获取首屏广告
     *
     * @param context
     * @return
     */
    public static String getAdUrl(Context context) {
        return getSharedPreferences(context).getString("AdUrl", "");
    }

    /**
     * 设置广告连接
     *
     * @param context
     * @param h5Path
     * @return
     */
    public static void setAdH5Url(Context context, String h5Path) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("AdH5Url", h5Path);
        editor.apply();
    }

    /**
     * 获取广告连接
     *
     * @param context
     * @return
     */
    public static String getAdH5Url(Context context) {
        return getSharedPreferences(context).getString("AdH5Url", "");
    }

    /**
     * 设置是否显示闹钟导航
     *
     * @param context
     * @return
     */
    public static void setShowVideoGuid(Context context, boolean isShow) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean("showVideo", isShow);
        editor.apply();
    }


    /**
     * 获取是否显示闹钟导航
     *
     * @param context
     * @return
     */
    public static boolean getShowVideoGuid(Context context) {
        return getSharedPreferences(context).getBoolean("showVideo", true);
    }


    /**
     * 设置是否显示个人直播间导航页()
     *
     * @param context
     * @param isShow
     * @return
     */
    public static void setShowDaliyNewsGuid(Context context, boolean isShow) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean("showdailynewsguid", isShow);
        editor.apply();
    }

    /**
     * 获得是否显示个人直播间导航页
     *
     * @param context
     * @return
     */
    public static boolean getShowDaliyNewsGuid(Context context) {
        return getSharedPreferences(context).getBoolean("showdailynewsguid", true);
    }

    /**
     * 设置本手机型号
     *
     * @param context
     * @param type
     * @return
     */
    public static void setPhoneType(Context context, int type) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt("phoneType", type);
        editor.apply();
    }

    /**
     * 获取本手机型号
     *
     * @param context
     * @return
     */
    public static int getPhoneType(Context context) {
        return getSharedPreferences(context).getInt("phoneType", AppEnum.PhoneType.DEFAULT);
    }


    /**
     * 设置本手机型号
     *
     * @param context
     * @param has
     * @return
     */
    public static void setHasShowPhoneTypeWarning(Context context, boolean has) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean("phoneTypeWarning", has);
        editor.apply();
    }

    /**
     * 获取本手机型号
     *
     * @param context
     * @return
     */
    public static boolean getHasShowPhoneTypeWarning(Context context) {
        return getSharedPreferences(context).getBoolean("phoneTypeWarning", false);
    }
}
