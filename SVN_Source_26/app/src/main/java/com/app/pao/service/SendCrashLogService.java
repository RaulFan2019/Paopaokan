package com.app.pao.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.app.pao.config.AppConfig;
import com.app.pao.config.URLConfig;
import com.app.pao.data.PreferencesData;
import com.app.pao.entity.network.GetAdPicUrlResult;
import com.app.pao.entity.network.REBase;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.network.utils.RequestParamsBuilder;
import com.app.pao.utils.FileUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import org.xutils.common.Callback;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/15.
 * 发送程序Crash的服务
 */
public class SendCrashLogService extends IntentService {


    private ArrayList<File> crashFiles;

    private static final String TAG = "SendCrashLogService";

    private static final int MSG_UPLOAD_NEXT = 1;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SendCrashLogService(String name) {
        super(name);
    }

    public SendCrashLogService() {
        super(TAG);
    }

    Handler uploadHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == MSG_UPLOAD_NEXT) {
                uploadCrash();
            }
        }
    };

    @Override
    protected void onHandleIntent(Intent intent) {
        File crashPath = new File(AppConfig.CRASH_PATH);
        if (crashPath == null) {
            return;
        }
        if(crashPath.listFiles() == null){
            return;
        }
        if (crashPath.listFiles().length == 0) {
            return;
        }
        crashFiles = new ArrayList<File>();
        for (File f : crashPath.listFiles()) {
            crashFiles.add(f);
        }
        uploadCrash();
    }


    private void uploadCrash() {
//        Log.v(TAG,"crashFiles.size:" + crashFiles.size());
        if (crashFiles.size() > 0) {
            postCrashFile(crashFiles.get(0));
        }
        return;
    }

    private void postCrashFile(final File file) {
        String info = FileUtils.ReadTxtFile(file);
        if (info.equals("")) {
            file.delete();
            crashFiles.remove(0);
            uploadCrash();
            return;
        }

        org.xutils.http.RequestParams params = RequestParamsBuilder.buildUploadCrashRP(SendCrashLogService.this,
                URLConfig.URL_UPLOAD_CRASH,info, file.getName());
        x.http().post(params,new Callback.CommonCallback<REBase>() {
            @Override
            public void onSuccess(REBase reBase) {
//                Log.v(TAG,"onSuccess");
                if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                    file.delete();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {
                if (crashFiles.size() > 0) {
                    crashFiles.remove(0);
                }
                uploadHandler.sendEmptyMessageDelayed(MSG_UPLOAD_NEXT, 1000 * 10);
            }
        });
//        HttpUtils http = new HttpUtils();
//        String POST_URL = URLConfig.URL_UPLOAD_CRASH;
//        RequestParams params = RequestParamsBuild.buildUploadCrashRequest(SendCrashLogService.this,info, file.getName());
//        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(this) {
//
//            @Override
//            protected void onErrorResponse(int errorCode, String errorMsg) {
////                Log.v(TAG, "error:" + errorMsg);
//            }
//
//            @Override
//            protected void onRightResponse(String Response) {
////                Log.v(TAG, Response);
//                file.delete();
//            }
//
//            @Override
//            protected void onFailureResponse(HttpException e, String s) {
////                Log.v(TAG, "onFailureResponse:" + s);
//            }
//
//            @Override
//            protected void onFinish() {
//                if (crashFiles.size() > 0) {
//                    crashFiles.remove(0);
//                }
//                uploadHandler.sendEmptyMessageDelayed(MSG_UPLOAD_NEXT, 1000 * 10);
//            }
//        });
    }
}
