package com.app.pao.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/12/9.
 */
public class AppStoreUtils {
    private static final String APP_STORE_PACKAGE_NAME ="com.tencent.android.qqdownloader";

    public static boolean isAppStoreExist(Context context){
        boolean isExist = false;
        final PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfoList = pm.getInstalledPackages(0);

        String tempName = null;
        for(PackageInfo info:packageInfoList){
            tempName = info.packageName;
            if(tempName!= null && tempName.equals(APP_STORE_PACKAGE_NAME)){
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    /**
     * 启动到app详情界面
     * @param appPkg
     *            App的包名
     *    如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public static void launchAppDetail(Context context,String appPkg) {
        try {
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            if (!TextUtils.isEmpty(APP_STORE_PACKAGE_NAME)) {
                intent.setPackage(APP_STORE_PACKAGE_NAME);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * q
     * @param context
     * @param appPkg
     */
    public static void launchWebAppDetail(Context context,String appPkg){
        String url = "http://a.app.qq.com/o/simple.jsp?pkgname="+appPkg;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
}
