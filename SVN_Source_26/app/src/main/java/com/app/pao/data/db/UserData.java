package com.app.pao.data.db;

import android.content.Context;

import com.app.pao.LocalApplication;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.network.GetCommonLoginResult;
import com.app.pao.entity.network.GetRegistResult;
import com.app.pao.utils.Log;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

/**
 * Created by Raul on 2015/11/5.
 * 用户数据库操作类
 */
public class UserData {

    private static final String TAG = "UserData";


    /**
     * 普通登录
     *
     * @param context
     * @param entity
     * @return
     */
    public static boolean CommonLogin(final Context context, final GetCommonLoginResult entity) {
        DBUserEntity userEntity = new DBUserEntity(entity.getId(), entity.getName(), "",
                entity.getWeight(), entity.getHeight(), entity.getAge(), entity.getNickname(), entity.getGender(),
                entity.getBirthdate(), entity.getAvatar(), entity.getLocationprovince(),
                entity.getLocationcity(), entity.getSessionid(), entity.getMobile(),
                entity.getWeixinnickname(), entity.getQrcode(), entity.getPasswordisblank(), entity.getThirdpartyaccount());
        try {
            if (getUserById(context, entity.getId()) == null) {
                LocalApplication.getInstance().getDbUtils(context).save(userEntity);
            } else {
                LocalApplication.getInstance().getDbUtils(context).replace(userEntity);
            }
            LocalApplication.getInstance().setLoginUser(userEntity);
            return true;
        } catch (DbException e) {
            return false;
        }
    }

    /**
     * 注册用户
     * <p/>
     * int userId, String name, String email, int weight, int height,
     * int age, String nickname, int gender, String birthday,
     * String avatar, String province, String city, String sessionid, String mobile, String
     * weixinnickname,String qrcode,int passwordisblank,int thirdpartyaccount
     *
     * @return
     */
    public static DBUserEntity ResigtUser(final Context context, final GetRegistResult registUser) {
        DBUserEntity userEntity = new DBUserEntity(registUser.id, registUser.name, "",
                registUser.weight, registUser.height, registUser.age, registUser.nickname, registUser.gender, registUser.birthdate,
                registUser.avatar, registUser.locationprovince, registUser.locationcity,
                registUser.sessionid, registUser.mobile, registUser.weixinnickname,
                registUser.qrcode, registUser.passwordisblank, registUser.thirdpartyaccount);
        try {
            if (getUserById(context, registUser.getId()) == null) {
                LocalApplication.getInstance().getDbUtils(context).save(userEntity);
            } else {
                LocalApplication.getInstance().getDbUtils(context).update(userEntity);
            }
            return userEntity;
        } catch (DbException e) {
            return null;
        }
    }


    /**
     * 更新用户信息
     *
     * @return
     */
    public static boolean updateUser(final Context context, final DBUserEntity entity) {
        try {
            LocalApplication.getInstance().getDbUtils(context).update(entity);
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 通过用户Id 获取用户信息
     *
     * @param userId
     * @return
     */
    public static DBUserEntity getUserById(final Context context, final int userId) {
        DBUserEntity userEntity;
        try {
            userEntity = LocalApplication.getInstance().getDbUtils(context)
                    .findFirst(Selector.from(DBUserEntity.class)
                            .where("userId", "=", userId));
        } catch (DbException e) {
            e.printStackTrace();
            //保存用户信息到本地
            GetCommonLoginResult entity = new GetCommonLoginResult(14, "0", 50, 50, 25, "", "游客", "", 1, "", null, "", "",
                    "", "", "", "", 1, "", 0);
            userEntity = new DBUserEntity(entity.getId(), entity.getName(), "",
                    entity.getWeight(), entity.getHeight(), entity.getAge(), entity.getNickname(), entity.getGender(),
                    entity.getBirthdate(), entity.getAvatar(), entity.getLocationprovince(),
                    entity.getLocationcity(), entity.getSessionid(), entity.getMobile(),
                    entity.getWeixinnickname(), entity.getQrcode(), entity.getPasswordisblank(), entity.getThirdpartyaccount());
            return userEntity;
        }
        return userEntity;
    }
}
