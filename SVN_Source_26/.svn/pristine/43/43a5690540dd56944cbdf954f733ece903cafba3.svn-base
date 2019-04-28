package com.app.pao.service;

import android.app.IntentService;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.app.pao.config.URLConfig;
import com.app.pao.data.PreferencesData;
import com.app.pao.entity.network.GetPhoneTypeResult;
import com.app.pao.entity.network.REBase;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.utils.RequestParamsBuilder;

import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by Raul.Fan on 2016/6/20.
 */
public class GetPhoneTypeService extends IntentService {

    private static final String TAG = "GetPhoneTypeService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GetPhoneTypeService(String name) {
        super(name);
    }

    public GetPhoneTypeService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GetPhoneType();
    }

    private void GetPhoneType() {
        org.xutils.http.RequestParams params = RequestParamsBuilder.buildStartUpRequest(GetPhoneTypeService.this,
                URLConfig.URL_GET_PHONE_TYPE);
        x.http().post(params, new Callback.CommonCallback<REBase>() {
            @Override
            public void onSuccess(REBase reBase) {
                if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {

                    GetPhoneTypeResult result = JSON.parseObject(reBase.result, GetPhoneTypeResult.class);
//                    Log.v(TAG,"result.venderid:" + result.venderid);
                    PreferencesData.setPhoneType(GetPhoneTypeService.this, result.venderid);
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
}
