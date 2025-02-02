package com.app.pao.utils.business;

import android.content.Context;

import com.app.pao.data.PreferencesData;
import com.app.pao.data.db.UserData;
import com.app.pao.entity.network.GetCommonLoginResult;

/**
 * Created by Raul.Fan on 2016/3/27.
 * 登录用工具
 */
public class LoginUtils {


    /**
     * 普通登录并保存用户数据到缓存
     */
    public static void CommonLogin(final Context context, final GetCommonLoginResult entity, final String pwd) {
        UserData.CommonLogin(context, entity);
        PreferencesData.setUserId(context, entity.getId());
        PreferencesData.setUserName(context, entity.getName());
        PreferencesData.setPassword(context, pwd);
        PreferencesData.setHasLogin(context,true);
    }
}
