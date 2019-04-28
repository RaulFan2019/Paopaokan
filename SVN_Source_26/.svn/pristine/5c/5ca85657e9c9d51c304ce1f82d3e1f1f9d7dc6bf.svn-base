package com.app.pao.service;

import android.app.IntentService;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.app.pao.config.AppConfig;
import com.app.pao.config.URLConfig;
import com.app.pao.data.PreferencesData;
import com.app.pao.entity.network.GetAdPicUrlResult;
import com.app.pao.entity.network.REBase;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.utils.RequestParamsBuilder;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.x;

import java.io.File;
import java.util.concurrent.Executor;

/**
 * Created by Raul on 2015/12/28.
 * 首屏广告下载服务
 */
public class AdService extends IntentService {

    /* contains */
    private static final String TAG = "AdService";

    private final static int MAX_DOWNLOAD_THREAD = 2;
    private final Executor executor = new PriorityExecutor(MAX_DOWNLOAD_THREAD, true);

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AdService(String name) {
        super(name);
    }

    public AdService() {
        super(TAG);
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        postGetAdPicUrl();
    }


    /**
     * 获取广告图片下载链接
     */
    private void postGetAdPicUrl() {
        org.xutils.http.RequestParams params = RequestParamsBuilder.buildGetAdPicUrlRP(AdService.this, URLConfig.URL_DOWNLOAD_AD_PIC);
        x.http().post(params, new Callback.CommonCallback<REBase>() {
            @Override
            public void onSuccess(REBase reBase) {
                if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                    GetAdPicUrlResult result = JSON.parseObject(reBase.result, GetAdPicUrlResult.class);
                    File downloadFile = new File(AppConfig.DEFAULT_SAVE_FILE_PATH);
                    if (!result.url.equals(PreferencesData.getAdUrl(AdService.this))) {
                        //下载之前会将download文件夹清空
                        if (downloadFile.exists()
                                && downloadFile.isDirectory()) {
                            File[] children = downloadFile.listFiles();
                            int size = children.length;
                            for (int i = 0; i < size; i++) {
                                children[i].delete();
                            }
                        }
                        //下载新的广告页面
                        downloadPic(result);
                    }
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

            }
        });
    }

    /**
     * 下载图片
     *
     * @param result
     */
    private void downloadPic(final GetAdPicUrlResult result) {
        org.xutils.http.RequestParams params = new org.xutils.http.RequestParams(result.url);
        params.setSaveFilePath(AppConfig.DEFAULT_SAVE_FILE_PATH + AppConfig.DEFAULT_AD_FILE_NAME);
        params.setExecutor(executor);
        x.http().get(params, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File file) {
//                Log.v(TAG, responseInfo.result.getPath());
                PreferencesData.setAdPath(AdService.this, file.getPath());
                //存储广告页面图片的url
                PreferencesData.setAdUrl(AdService.this, result.url);
                //存储点击广告页面后进入该广告页面的url
                PreferencesData.setAdH5Url(AdService.this, result.h5url);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
            }

            @Override
            public void onCancelled(CancelledException e) {
            }

            @Override
            public void onFinished() {

            }
        });
    }
}
