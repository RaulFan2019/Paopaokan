package com.app.pao.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.activity.main.ClipGroupDialogActivity;
import com.app.pao.activity.main.ClipUserDialogActivity;
import com.app.pao.activity.main.InputInviteActivity;
import com.app.pao.config.AppConfig;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.network.GetParseInviteTextResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Administrator on 2016/3/2.
 * <p/>
 * 读取剪贴板内容Service
 */
public class ClipboardService extends Service {
    private ClipboardManager clipboardManager;//剪贴板管理器


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initClipBoard();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化剪贴板
     */
    private void initClipBoard() {
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        //第一次启动Server时尝试读取剪贴板内容，看是否有信息
        if (clipboardManager.hasPrimaryClip()) {
            getClipDataText();
        }
        //添加剪贴板内容改变监听
        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                getClipDataText();
            }
        });
    }

    /**
     * 读取剪贴板内容
     */
    private void getClipDataText() {
        //若复制的是文本
        if (clipboardManager.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData clipData = clipboardManager.getPrimaryClip();
            ClipData.Item item = clipData.getItemAt(0);

            if (clipData.getDescription().getLabel() != null && clipData.getDescription().getLabel().equals("123Go")) {
//                T.showShort(ClipboardService.this, "label 符合 done");
                return;
            }

            //若复制的内容不为空
            if (item.getText() != null && !(item.getText().toString().isEmpty())) {
                //向服务器发送请求
                checkReceiveCode(item.getText().toString());
            }
        }
    }

    /**
     * 检查文本中的复制的邀请码
     */
    private void checkReceiveCode(String mInviteText) {
        //跑步中或游客无反应
        if (WorkoutData.getUnFinishWorkout(ClipboardService.this,LocalApplication.getInstance().getLoginUser(ClipboardService.this).userId) != null
                || LocalApplication.getInstance().getLoginUser(ClipboardService.this).getUserId() == AppEnum.DEFAULT_USER_ID) {
            return;
        }
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_PARSE_INVITE_TEXT;
        RequestParams params = RequestParamsBuild.BuildGetParseInviteTextRequest(ClipboardService.this,mInviteText);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(ClipboardService.this) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {

            }

            @Override
            protected void onRightResponse(String Response) {
//                T.showShort(ClipboardService.this, Response + "");
                GetParseInviteTextResult result = JSON.parseObject(Response,GetParseInviteTextResult.class);
                //复写内容清空上次复制
                ClipboardManager cbManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData textCd = ClipData.newPlainText("123Go", "");
                cbManager.setPrimaryClip(textCd);
                //若是用户自己发出的
                if(result.getOwnerid() == LocalApplication.getInstance().getLoginUser(ClipboardService.this).getUserId()){
                    return;
                }
                if(isAppOnForeground()){
                    launchActivity(result);
                }else {
                    LocalApplication.getInstance().setClipresult(result);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 跳转页面
     */
    private void launchActivity(GetParseInviteTextResult result) {
        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putSerializable("data", result);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(b);
        //若类型是好友
        if (result.getType() == AppEnum.ClipType.USER) {
            intent.setClass(ClipboardService.this, ClipUserDialogActivity.class);
            ClipboardService.this.startActivity(intent);
            //若类型是团
        } else if (result.getType() == AppEnum.ClipType.GROUP) {
            intent.setClass(ClipboardService.this, ClipGroupDialogActivity.class);
            ClipboardService.this.startActivity(intent);
        }
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
