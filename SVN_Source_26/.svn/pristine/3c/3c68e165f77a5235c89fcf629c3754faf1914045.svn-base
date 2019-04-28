package com.app.pao.network.api;

import android.content.Context;
import android.os.Environment;

import com.alibaba.fastjson.JSON;
import com.app.pao.config.AppConfig;
import com.app.pao.entity.network.GetResponseEntity;
import com.app.pao.network.utils.HttpExceptionHelper;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Raul on 2015/10/30.
 * 网络回调函数
 */
public abstract class MyRequestCallBack extends RequestCallBack<String> {

    /*contains*/
    private static final String TAG = "MyRequestCallBack";
    private static final boolean DEBUG = false;

    private static final int ERROR_CODE_NONE = 0;
    private static final int ERRORY_CODE_JSON_EXCETION = 1;


    /* local data */
    private Context mContext;//执行网络交互的上下文


    /* 构造方法 */
    public MyRequestCallBack(Context context) {
        this.mContext = context;
    }

    /* 服务器返回逻辑错误 */
    protected abstract void onErrorResponse(int errorCode, String errorMsg);

    /* 服务器返回正确的结果 */
    protected abstract void onRightResponse(String Response);

    /* http 通常错误 */
    protected abstract void onFailureResponse(HttpException e, String s);

    /* 请求结束 */
    protected abstract void onFinish();

    /**
     * 网络上传成功返回
     *
     * @param responseInfo
     */
    @Override
    public void onSuccess(ResponseInfo<String> responseInfo) {
        Log.v(TAG, responseInfo.result);
        String result = responseInfo.result;
        if (result.indexOf("{") == -1) {
            onErrorResponse(ERRORY_CODE_JSON_EXCETION, "网络错误");
            onFinish();
            return;
        }
        result = result.substring(result.indexOf("{"));
        try {
            GetResponseEntity ResponseEntity = JSON.parseObject(result, GetResponseEntity.class);
            //没有错误
            if (ResponseEntity.getErrorcode() == ERROR_CODE_NONE) {
                onRightResponse(ResponseEntity.getResult());
                //有错误
            } else {
                onErrorResponse(ResponseEntity.getErrorcode(), ResponseEntity.getErrormsg());
            }
        } catch (com.alibaba.fastjson.JSONException e) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String sdcardPath = AppConfig.CRASH_PATH;
                File crashPath = new File(sdcardPath);
                if (!crashPath.exists()) {
                    crashPath.mkdir();
                }
//            if (AppConfig.BUILD_TYPE == AppEnum.BuildType.BUILD_ALPHA) {
//                Log.v(TAG,"抓到JSON格式错误");
                writeLog("123yd JSON CATCH :" + responseInfo.result, sdcardPath + "/");
//            writeLogcat(sdcardPath + "/hwh_logcat");
//            }
            }
            onErrorResponse(ERRORY_CODE_JSON_EXCETION, "网络错误");
        }
        onFinish();
    }

    /**
     * 网络上传返回失败
     *
     * @param e
     * @param s
     */
    @Override
    public void onFailure(HttpException e, String s) {
        Log.v(TAG, "onFailure errorcode :" + e.getExceptionCode());
        onFailureResponse(e, HttpExceptionHelper.getErrorMsg(e));
        onFinish();
    }

    private void writeLog(String log, String name) {
//        CharSequence timestamp = TimeUtils.NowTime();
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
}
