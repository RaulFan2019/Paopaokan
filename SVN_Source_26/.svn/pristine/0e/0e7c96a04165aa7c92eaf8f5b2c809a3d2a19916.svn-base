package com.app.pao.service;

import android.app.IntentService;
import android.content.Intent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.MaidianData;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.network.REBase;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.utils.RequestParamsBuilder;


import org.xutils.common.Callback;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/2/15.
 * 发送埋点数据
 */
public class SendMaidianService extends IntentService {


    private static List<DBEntityMaidian> datas;

    private static final String TAG = "SendMaidianService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SendMaidianService(String name) {
        super(name);
    }

    public SendMaidianService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        datas = MaidianData.getAllMaidianList(SendMaidianService.this);
        if (datas != null && datas.size() > 0) {
//            Log.v(TAG, "datas.size:" + datas.size());
            uploadMaiDianData();
        }
    }


    private void uploadMaiDianData() {
        JSONObject uploadObj = new JSONObject();
        JSONArray uploadArray = new JSONArray();
        try {
            for (DBEntityMaidian entityMaidian : datas) {
                JSONObject obj = new JSONObject();
                obj.put("code", entityMaidian.code);
                obj.put("time", entityMaidian.time);
                uploadArray.add(obj);
            }
            uploadObj.put("operations", uploadArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        org.xutils.http.RequestParams params = RequestParamsBuilder.buildUploadMaiDianRequest(SendMaidianService.this,
                URLConfig.URL_UPLOAD_MAIDIAN, uploadObj.toJSONString());
        x.http().post(params, new Callback.CommonCallback<REBase>() {
            @Override
            public void onSuccess(REBase reBase) {
                if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                    MaidianData.deleteAllMaidianList(SendMaidianService.this, datas);
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
