package com.app.pao.service;

import android.app.IntentService;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.app.pao.config.URLConfig;
import com.app.pao.data.PreferencesData;
import com.app.pao.entity.network.UpdateEntity;
import com.app.pao.utils.DeviceUtils;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Raul.Fan on 2016/9/5.
 */
public class CheckUpdateService extends IntentService {

    /* contains */
    private static final String TAG = "CheckUpdateService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public CheckUpdateService(String name) {
        super(name);
    }

    public CheckUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        checkUpdate();
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        RequestParams params = new RequestParams(URLConfig.URL_CHECK_UPDATE);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String string) {
//                Log.v(TAG,"onSuccess:" + string);
                    try {
                        UpdateEntity mUpdateEntity = JSON.parseObject(string, UpdateEntity.class);
                        //需要升级
                        if (mUpdateEntity != null
                                && DeviceUtils.getVersionCode(getApplication().getPackageName())
                                < Integer.valueOf(mUpdateEntity.versionCode).intValue()) {
                            PreferencesData.setUpdateInfo(getApplicationContext(), string);
                        } else {
                            PreferencesData.setUpdateInfo(getApplicationContext(), "");
                        }
                    } catch (JSONException exception) {

                    }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
//                Log.v(TAG,"onError:" + HttpExceptionHelper.getErrorMsg(throwable));
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
