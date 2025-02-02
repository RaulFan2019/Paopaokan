package com.app.pao.network.utils;

import android.content.Context;

import com.app.pao.config.AppConfig;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.network.api.MyRequestParams;
import com.app.pao.network.api.MyRequestParamsV2;
import com.app.pao.utils.Log;

import org.xutils.http.RequestParams;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Raul.Fan on 2016/9/5.
 */
public class RequestParamsBuilder {

    /*== about login  ==*/

    /**
     * 获取手机号验证参数
     *
     * @param context
     * @param url
     * @param phoneNum
     * @return
     */
    public static RequestParams buildCheckPhoneRP(final Context context, final String url, final String phoneNum) {
        MyRequestParamsV2 params = new MyRequestParamsV2(context, url);
        params.addBodyParameter("name", phoneNum);
        return params;
    }


    /**
     * 获取手机验证码
     *
     * @param context
     * @param url
     * @param phoneNum
     * @return
     */
    public static RequestParams buildGetVCodeRP(final Context context, final String url, final String phoneNum) {
        MyRequestParamsV2 params = new MyRequestParamsV2(context, url);
        params.addBodyParameter("mobile", phoneNum);
        return params;
    }

    /**
     * 验证手机验证码是否正确
     *
     * @param Mobile 手机号
     * @param Code   验证码
     * @return
     */
    public static RequestParams buildCheckVCodeRequest(final Context context, final String url,
                                                       final String Mobile, final String Code) {
        MyRequestParamsV2 requestParams = new MyRequestParamsV2(context, url);
        requestParams.addBodyParameter("mobile", Mobile);
        requestParams.addBodyParameter("code", Code);
        return requestParams;
    }

    /**
     * 获取普通登录请求
     *
     * @param context
     * @param url
     * @param phoneNum
     * @param password
     * @param devicepushid
     * @param Deviceos
     * @return
     */
    public static RequestParams buildCommonLoginRP(final Context context, final String url, final String phoneNum,
                                                   final String password, final String devicepushid,
                                                   final String Deviceos) {
        MyRequestParamsV2 params = new MyRequestParamsV2(context, url);
        params.addBodyParameter("name", phoneNum);
        params.addBodyParameter("password", password);
        params.addBodyParameter("devicepushid", devicepushid);
        params.addBodyParameter("deviceos", Deviceos);
        params.addBodyParameter("appversion", URLConfig.USER_ANGENT);
        return params;
    }

    /**
     * 使用短信验证码登录
     *
     * @param phoneNum     电话号码
     * @param code         验证码
     * @param devicepushid 极光号
     * @param Deviceos     设备类型
     * @return
     */
    public static RequestParams buildLoginByVcCodeRequest(final Context context, final String url,
                                                          final String phoneNum, final String code,
                                                          final String devicepushid, final String Deviceos) {
        MyRequestParamsV2 requestParams = new MyRequestParamsV2(context, url);
        requestParams.addBodyParameter("mobile", phoneNum);
        requestParams.addBodyParameter("code", code);
        requestParams.addBodyParameter("devicepushid", devicepushid);
        requestParams.addBodyParameter("deviceos", Deviceos);
        requestParams.addBodyParameter("appversion", URLConfig.USER_ANGENT);
        return requestParams;
    }

    /**
     * 微信登录/注册接口
     *
     * @param context
     * @param url
     * @param unionid
     * @param nickname
     * @param gender
     * @param avatar
     * @param province
     * @param city
     * @return
     */
    public static RequestParams buildWeiXinLoginRequest(final Context context, final String url,
                                                        final String unionid, final String nickname,
                                                        final String gender, final String avatar,
                                                        final String province, String city) {
        MyRequestParamsV2 requestParams = new MyRequestParamsV2(context, url);
        requestParams.addBodyParameter("unionid", unionid);
        if (nickname != null && !nickname.isEmpty()) {
            requestParams.addBodyParameter("nickname", nickname);
        }
        if (gender != null && !gender.isEmpty()) {
            requestParams.addBodyParameter("gender", gender);
        }
        requestParams.addBodyParameter("devicepushid", JPushInterface.getRegistrationID(context));
        requestParams.addBodyParameter("deviceos", AppEnum.DEVICEOS);
        requestParams.addBodyParameter("avatar", avatar);
        requestParams.addBodyParameter("locationprovince", province);
        requestParams.addBodyParameter("locationcity", city);
        requestParams.addBodyParameter("appversion", URLConfig.USER_ANGENT);

        return requestParams;
    }

    /**
     * 注册接口
     *
     * @param context
     * @param url
     * @param phoneNum
     * @param nickname
     * @param pwd
     * @param avatar
     * @param jpushId
     * @param gender
     * @param weight
     * @param height
     * @param birthdate
     * @param locationprovince
     * @param locationcity
     * @return
     */
    public static RequestParams buildRegisterRP(final Context context, final String url, final String phoneNum, final String nickname,
                                                final String pwd, final String avatar, final String jpushId, final int gender,
                                                final int weight, final int height, final String birthdate,
                                                final String locationprovince, final String locationcity) {
        MyRequestParamsV2 requestParams = new MyRequestParamsV2(context, url);
        requestParams.addBodyParameter("name", phoneNum);
        requestParams.addBodyParameter("nickname", nickname);
        requestParams.addBodyParameter("password", pwd);
        requestParams.addBodyParameter("avatar", avatar);
        requestParams.addBodyParameter("devicepushid", jpushId);
        requestParams.addBodyParameter("devicepushid", AppEnum.DEVICEOS);
        requestParams.addBodyParameter("appversion", URLConfig.USER_ANGENT);
        requestParams.addBodyParameter("weight", weight + "");
        requestParams.addBodyParameter("height", height + "");
        requestParams.addBodyParameter("age", "35");
        requestParams.addBodyParameter("gender", gender + "");
        requestParams.addBodyParameter("birthdate", birthdate);
        requestParams.addBodyParameter("locationprovince", locationprovince);
        requestParams.addBodyParameter("locationcity", locationcity);
        return requestParams;
    }


    /**
     * 更新用户信息
     *
     * @param context
     * @param url
     * @param nickname
     * @param birthdate
     * @param weight
     * @param height
     * @param avatar
     * @param gender
     * @param locationprovince
     * @param locationcity
     * @param jpushId
     * @return
     */
    public static RequestParams buildUpdateUserInfoRP(final Context context, final String url, final String nickname,
                                                      final String birthdate, final int weight, final int height, final String avatar,
                                                      final int gender, final String locationprovince, final String locationcity,
                                                      final String jpushId) {
        MyRequestParamsV2 requestParams = new MyRequestParamsV2(context, url);
        requestParams.addBodyParameter("nickname", nickname);
        requestParams.addBodyParameter("birthdate", birthdate);
        requestParams.addBodyParameter("weight", weight + "");
        requestParams.addBodyParameter("height", height + "");
        requestParams.addBodyParameter("avatar", avatar);
        requestParams.addBodyParameter("gender", gender + "");
        requestParams.addBodyParameter("locationprovince", locationprovince);
        requestParams.addBodyParameter("locationcity", locationcity);
        requestParams.addBodyParameter("devicepushid", jpushId);
        requestParams.addBodyParameter("devicepushid", AppEnum.DEVICEOS);
        return requestParams;
    }

    /**
     * 发送修改密码请求
     *
     * @param password 用户密码
     * @return
     */
    public static RequestParams buildResetPwdRP(final Context context, final String url,
                                                final String password) {
        MyRequestParamsV2 requestParams = new MyRequestParamsV2(context, url);
        requestParams.addBodyParameter("password", password);
        return requestParams;
    }

    /**
     * 发送修改移动电话
     *
     * @param mobile 移动电话
     * @return
     */
    public static RequestParams buildWeiXinBingMobileRP(final String url, final String mobile, final int userid) {
        RequestParams requestParams = new RequestParams(url);
        requestParams.addHeader("user-agent", URLConfig.USER_ANGENT);
        requestParams.addBodyParameter("name", mobile);
        requestParams.addBodyParameter("sessionid", "");
        requestParams.addBodyParameter("userid", userid + "");
        return requestParams;
    }

    /*== 修改用户信息 ==*/

    /**
     * 上传头像文件
     *
     * @param avatarF 头像文件
     * @return
     */
    public static RequestParams buildUploadAvatarFileRequest(final Context context, final String url,
                                                             final File avatarF) {
        MyRequestParamsV2 requestParams = new MyRequestParamsV2(context, url);
        requestParams.addBodyParameter("upload", avatarF);
        return requestParams;
    }


    /*== 跑步相关 ==*/

    /**
     * 获取准备跑的数据
     *
     * @param context
     * @param longitude
     * @param latitude
     * @return
     */
    public static RequestParams buildGetBeginRunInfoRequest(final Context context, final String url,
                                                            final double latitude, double longitude) {
        MyRequestParamsV2 requestParams = new MyRequestParamsV2(context, url);
        requestParams.addBodyParameter("longitude", longitude + "");
        requestParams.addBodyParameter("latitude", latitude + "");
        return requestParams;
    }


    /**
     * 获取跑步个人纪录信息
     *
     * @param toId 获取对象
     * @return
     */
    public static RequestParams buildGetPersonRecordRequest(final Context context, final String url, final int toId) {
        MyRequestParamsV2 requestParams = new MyRequestParamsV2(context, url);
        requestParams.addBodyParameter("id", String.valueOf(toId));
        return requestParams;
    }

    /**
     * 获取动态列表
     *
     * @param date  获取对象
     * @param limit 限制条件
     * @return
     */
    public static RequestParams buildGetDynamicRP(final Context context,final String url, final String date,final String limit) {
        MyRequestParamsV2 requestParams = new MyRequestParamsV2(context,url);
        requestParams.addBodyParameter("date", date);
        requestParams.addBodyParameter("limit", limit);
        return requestParams;
    }


    /*== other ==*/

    /**
     * 获取手机型号
     *
     * @return
     */
    public static RequestParams buildStartUpRequest(final Context context, final String url) {
        MyRequestParamsV2 requestParams = new MyRequestParamsV2(context, url);
        requestParams.addBodyParameter("deviceos", AppEnum.DEVICEOS);
        requestParams.addBodyParameter("appversion", URLConfig.USER_ANGENT);
        return requestParams;
    }

    /**
     * 获取下载广告图片的链接
     *
     * @param context
     * @param url
     * @return
     */
    public static RequestParams buildGetAdPicUrlRP(final Context context, final String url) {
        MyRequestParamsV2 params = new MyRequestParamsV2(context, url);
        return params;
    }

    /**
     * 上传错误日志
     *
     * @param context
     * @param url
     * @param info
     * @param time
     * @return
     */
    public static RequestParams buildUploadCrashRP(final Context context, final String url,
                                                   final String info, final String time) {
        MyRequestParamsV2 requestParams = new MyRequestParamsV2(context, url);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        String timeStr = "";
        try {
            timeStr = df.format(new Date(Long.parseLong(time)));
        } catch (NumberFormatException ex) {
            timeStr = time + "";
        }
        requestParams.addBodyParameter("debuginfo", info);
        requestParams.addBodyParameter("deviceos", "android " + android.os.Build.VERSION.RELEASE);
        requestParams.addBodyParameter("deviceinfo", android.os.Build.MODEL);
        requestParams.addBodyParameter("appversion", AppConfig.Version);
        requestParams.addBodyParameter("time", timeStr);
        return requestParams;
    }


    /**
     * 上传埋点数据
     *
     * @param context
     * @param operations
     * @return
     */
    public static RequestParams buildUploadMaiDianRequest(final Context context, final String url,
                                                          final String operations) {
        MyRequestParamsV2 requestParams = new MyRequestParamsV2(context, url);
        requestParams.addBodyParameter("operations", operations);
        return requestParams;
    }


}
