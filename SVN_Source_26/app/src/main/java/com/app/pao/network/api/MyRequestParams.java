package com.app.pao.network.api;

import android.content.Context;

import com.app.pao.LocalApplication;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.db.DBUserEntity;
import com.lidroid.xutils.http.RequestParams;

/**
 * Created by Raul on 2015/11/12.
 * 网络传输对象
 */
public class MyRequestParams extends RequestParams {

    private static final String USER_AGENT = "user-agent";


    public MyRequestParams(Context context) {
        this.addHeader(USER_AGENT, URLConfig.USER_ANGENT);
        DBUserEntity userEntity = LocalApplication.getInstance().getLoginUser(context);
        if (userEntity != null) {
            this.addBodyParameter("sessionid", userEntity.getSessionid());
            this.addBodyParameter("userid", String.valueOf(userEntity.getUserId()));
        } else {
            this.addBodyParameter("sessionid", "14");
            this.addBodyParameter("userid", AppEnum.DEFAULT_USER_ID + "");
        }
    }
}
