package com.app.pao.network.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.app.pao.config.AppConfig;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.RecordData;
import com.app.pao.network.api.MyRequestParams;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.http.RequestParams;
import com.tencent.mm.sdk.modelbiz.AddCardToWXCardPackage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Raul on 2015/11/2.
 */
public class RequestParamsBuild {


    /**
     * 使用 用户,密码登录的接口
     *
     * @param phoneNum     电话号码
     * @param password     密码
     * @param devicepushid 推送设备号
     * @param Deviceos     设备类型
     * @return
     */
    public static RequestParams buildCommonLoginRequest(Context context, String phoneNum, String password, String devicepushid, String
            Deviceos) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("name", phoneNum);
        requestParams.addBodyParameter("password", password);
        requestParams.addBodyParameter("devicepushid", devicepushid);
        requestParams.addBodyParameter("deviceos", Deviceos);
        requestParams.addBodyParameter("appversion", URLConfig.USER_ANGENT);
        return requestParams;
    }

    /**
     * 上传埋点数据
     *
     * @param context
     * @param operations
     * @return
     */
    public static RequestParams buildUploadMaidianRequest(final Context context, final String operations) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("operations", operations);
        return requestParams;
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
    public static RequestParams buildLoginByVcCodeRequest(Context context, String phoneNum, String code, String devicepushid, String
            Deviceos) {
        MyRequestParams requestParams = new MyRequestParams(context);
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
     * @param context  上下文
     * @param unionid  微信ID
     * @param nickname 昵称
     * @param gender   用户性别
     * @param avatar   头像地址
     * @param province 省
     * @param city     市
     * @return
     */
    public static RequestParams buildWeixinLoginRequest(Context context, String unionid, String nickname, String gender,
                                                        String avatar, String province, String city) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("unionid", unionid);
        if (nickname != null && !nickname.isEmpty()) {
            requestParams.addBodyParameter("nickname", nickname);
        }
        if (gender != null && !gender.isEmpty()) {
            requestParams.addBodyParameter("gender", gender);
        }
        requestParams.addBodyParameter("devicepushid", JPushInterface.getRegistrationID(context));

        requestParams.addBodyParameter("deviceos", "2");
        requestParams.addBodyParameter("avatar", avatar);
        requestParams.addBodyParameter("locationprovince", province);
        requestParams.addBodyParameter("locationcity", city);
        requestParams.addBodyParameter("appversion", URLConfig.USER_ANGENT);

        return requestParams;
    }

    /**
     * 注册接口
     *
     * @param phoneNum 手机号码
     * @param nickname 昵称
     * @param pwd      密码
     * @param avatar   头像图片文件
     * @return
     */
    public static RequestParams buildRegistRequest(Context context, String phoneNum, String nickname, String pwd, String avatar,
                                                   String jpushId, int gender, int weight, int height,
                                                   String birthdate, String locationprovince, String locationcity) {
        MyRequestParams requestParams = new MyRequestParams(context);
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
     * 登出
     *
     * @return
     */
    public static RequestParams buildLogoutRequest(Context context) {
        MyRequestParams requestParams = new MyRequestParams(context);
        return requestParams;
    }

    /**
     * 上传头像文件
     *
     * @param avatarF 头像文件
     * @return
     */
    public static RequestParams buildUploadAvatarRequest(Context context, File avatarF) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("upload", avatarF);
        return requestParams;
    }

    /**
     * 获取短信验证码
     *
     * @param Mobile 手机号
     * @return
     */
    public static RequestParams buildGetVCCodeRequest(Context context, String Mobile) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("mobile", Mobile);
        return requestParams;
    }

    /**
     * 验证手机验证码是否正确
     *
     * @param Mobile 手机号
     * @param Code   验证码
     * @return
     */
    public static RequestParams buildCheckVCCodeRequest(Context context, final String Mobile, final String Code) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("mobile", Mobile);
        requestParams.addBodyParameter("code", Code);
        return requestParams;
    }

    /**
     * 更新用户的基本信息
     *
     * @param id        用户ID
     * @param name      用户名
     * @param weight    用户体重
     * @param height    用户身高
     * @param age       用户年龄
     * @param nickname  用户昵称
     * @param birthdate 生日
     * @param gender    用户性别
     * @param province  省
     * @param city      城市
     * @return
     */
    public static RequestParams buildUpdateUserBasicInfoRequest(Context context, int id, String name, String weight, String height,
                                                                String age,
                                                                String nickname, String birthdate, String gender,
                                                                String province, String city) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(id));
        requestParams.addBodyParameter("name", name);
        requestParams.addBodyParameter("weight", weight);
        requestParams.addBodyParameter("height", height);
        requestParams.addBodyParameter("age", age);
        requestParams.addBodyParameter("nickname", nickname);
        requestParams.addBodyParameter("birthdate", birthdate);
        requestParams.addBodyParameter("gender", gender);
        requestParams.addBodyParameter("locationprovince", province);
        requestParams.addBodyParameter("locationcity", city);
        return requestParams;
    }

    /**
     * 更新用户极光设备信息
     *
     * @param devicepushid
     * @return
     */
    public static RequestParams buildUpdateUserJpushIdRequest(Context context, String devicepushid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("devicepushid", devicepushid);
        return requestParams;
    }

    /**
     * 发送修改密码请求
     *
     * @param password 用户密码
     * @return
     */
    public static RequestParams buildResetUserPasswordRequest(Context context, String password) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("password", password);
        return requestParams;
    }

    /**
     * 发送修改头像
     *
     * @param avatar 头像地址
     * @return
     */
    public static RequestParams buildResetUserAvatarRequest(Context context, String avatar) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("avatar", avatar);
        return requestParams;
    }

    /**
     * 发送修改移动电话
     *
     * @param mobile 移动电话
     * @return
     */
    public static RequestParams buildResetUserMobileRequest(Context context, String mobile) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("name", mobile);
        return requestParams;
    }

    /**
     * 发送修改移动电话
     *
     * @param mobile 移动电话
     * @return
     */
    public static RequestParams buildResetUserMobileRequestByWeixin(final String mobile, final int userid) {
        RequestParams requestParams = new RequestParams();
        requestParams.addHeader("user-agent", URLConfig.USER_ANGENT);
        requestParams.addBodyParameter("name", mobile);
        requestParams.addBodyParameter("sessionid", "");
        requestParams.addBodyParameter("userid", userid + "");
        return requestParams;
    }

    /**
     * 发送修改昵称
     *
     * @param nickname 昵称
     * @return
     */
    public static RequestParams buildResetUserNicknameRequest(Context context, String nickname) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("nickname", nickname);
        return requestParams;
    }

    /**
     * \
     * 更新用户地址
     *
     * @param locationprovince
     * @param locationcity
     * @return
     */
    public static RequestParams buildResetUserLocationRequest(Context context, String locationprovince, String locationcity) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("locationprovince", locationprovince);
        requestParams.addBodyParameter("locationcity", locationcity);
        return requestParams;
    }

    /**
     * \
     * 更新用户地址
     *
     * @param locationprovince
     * @param locationcity
     * @return
     */
    public static RequestParams buildResetUserLocationAndNickNameRequest(Context context, String locationprovince, String locationcity, String nickname) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("locationprovince", locationprovince);
        requestParams.addBodyParameter("locationcity", locationcity);
        requestParams.addBodyParameter("nickname", nickname);
        return requestParams;
    }

    /**
     * 更新用户性别
     *
     * @param gender
     * @return
     */
    public static RequestParams buildResetUserGenderRequest(Context context, int gender) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("gender", gender + "");
        return requestParams;
    }

    /**
     * 更新用户生日
     *
     * @param birthdate
     * @return
     */
    public static RequestParams buildResetUserBirthdayRequest(Context context, String birthdate) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("birthdate", birthdate);
        return requestParams;
    }

    /**
     * 更新用户身高
     *
     * @param height
     * @return
     */
    public static RequestParams buildResetUserHeightRequest(Context context, int height) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("height", height + "");
        return requestParams;
    }

    /**
     * 更新用户体重
     *
     * @param weight
     * @return
     */
    public static RequestParams buildResetUserWeightRequest(Context context, int weight) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("weight", weight + "");
        return requestParams;
    }


    /**
     * 验证手机号码是否已经注册
     *
     * @param phoneNum
     * @return
     */
    public static RequestParams buildCheckPhoneNumIsRegist(Context context, String phoneNum) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("name", phoneNum);
        return requestParams;
    }

    /**
     * 验证手机号码是否已经注册
     *
     * @param phoneNum
     * @return
     */
    public static RequestParams buildCheckPhoneNumIsRegistByWeixin(final String phoneNum, final int userId) {
        RequestParams requestParams = new RequestParams();
        requestParams.addHeader("user-agent", URLConfig.USER_ANGENT);
        requestParams.addBodyParameter("name", phoneNum);
        requestParams.addBodyParameter("userid", userId + "");
        requestParams.addBodyParameter("sessionid", "");
        return requestParams;
    }


    /**
     * 上传跑步信息
     *
     * @param uploadinfo
     * @return
     */
    public static RequestParams saveWorkoutSegment(Context context, String uploadinfo) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("workout", uploadinfo);
        return requestParams;
    }


    /**
     * 删除跑步历史
     *
     * @param starttime
     * @return
     */
    public static RequestParams removeWorkout(Context context, String starttime) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("starttime", starttime);
        return requestParams;
    }


    /**
     * 通过用户名搜索用户
     *
     * @param name
     * @return
     */
    public static RequestParams BuildSearchUserByNameParams(Context context, String name) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("name", name);
        return requestParams;
    }


    /**
     * 申请添加好友
     *
     * @param friendid
     * @return
     */
    public static RequestParams BuildAddFriendParams(Context context, int friendid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("friendid", String.valueOf(friendid));
        return requestParams;
    }


    /**
     * 根据用户列表查询用户
     *
     * @param namelist
     * @return
     */
    public static RequestParams buildSearchUsersByNameRequest(Context context, final String namelist) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("namelist", namelist);
        return requestParams;
    }


    /**
     * 获取指定用户的好友列表
     *
     * @param fromId
     * @return
     */
    public static RequestParams buildGetFriendListRequest(Context context, int fromId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(fromId));
        return requestParams;
    }


    /**
     * 增加一条点赞
     *
     * @param context
     * @param targetid
     * @param type
     * @return
     */
    public static RequestParams buildAddThumbRequest(final Context context, final int targetid, final int type) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("targetid", String.valueOf(targetid));
        requestParams.addBodyParameter("type", String.valueOf(type));
        return requestParams;
    }

    /***
     * 解析二维码
     *
     * @param result
     * @return
     */
    public static RequestParams buildParseQrcodeRequest(Context context, final String result) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("qrcode", result);
        return requestParams;
    }


    /**
     * 获取用户个人信息
     *
     * @param toId 用户id
     * @return
     */
    public static RequestParams buildGetUserInfoRequest(Context context, int toId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(toId));
        return requestParams;
    }

    /**
     * 获取跑步历史列表
     *
     * @param toId  获取对象
     * @param limit 限制条件
     * @return
     */
    public static RequestParams buildGetWorkoutListRequest(Context context, int toId, String limit) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("limit", limit);
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
    public static RequestParams buildGetDynamicRequest(Context context, String date, String limit) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("date", date);
        requestParams.addBodyParameter("limit", limit);
        return requestParams;
    }


    /**
     * 获取跑步个人纪录信息
     *
     * @param toId 获取对象
     * @return
     */
    public static RequestParams buildGetPersonRecordRequest(Context context, int toId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(toId));
        return requestParams;
    }

    /**
     * 获取跑步历史详情
     *
     * @param workoutId
     * @return
     */
    public static RequestParams buildGetWorkoutInforRequest(Context context, int workoutId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(workoutId));
        return requestParams;
    }


    /**
     * 获取点赞列表
     *
     * @param id
     * @param starttime
     * @return
     */
    public static RequestParams buildGetThumbUpListRequest(Context context, int id, String starttime) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("runnerid", String.valueOf(id));
        requestParams.addBodyParameter("starttime", starttime);
        return requestParams;
    }

    /**
     * 发送点赞
     *
     * @param workoutid
     * @return
     */
    public static RequestParams thumbUpToWorkoutRequest(Context context, String workoutid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("workoutid", String.valueOf(workoutid));
        return requestParams;
    }

    /**
     * 获取跑团列表
     *
     * @param id
     * @return
     */
    public static RequestParams buildGetGroupListRequest(Context context, int id) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(id));
        return requestParams;
    }

    /**
     * 获取摄像头位置列表
     *
     * @return
     */
    public static RequestParams buildGetCamareListRequest(Context context) {
        MyRequestParams requestParams = new MyRequestParams(context);
        return requestParams;
    }

    /**
     * (接口 11) 获取最后一个跑步历史
     *
     * @param starttime
     * @return
     */
    public static RequestParams buildGetLatestWorkoutDataRequest(Context context, String starttime,
                                                                 int startsplit, int id, int tuid, int commentid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("starttime", starttime);
        requestParams.addBodyParameter("id", String.valueOf(id));
        requestParams.addBodyParameter("startsplit", String.valueOf(startsplit));
        requestParams.addBodyParameter("tuid", tuid + "");
        requestParams.addBodyParameter("commentid", commentid + "");
        return requestParams;
    }

    /**
     * 获取申请好友列表
     *
     * @return
     */
    public static RequestParams buildGetApplyFriendList(Context context) {
        MyRequestParams requestParams = new MyRequestParams(context);
        return requestParams;
    }

    /**
     * 向服务器发送用户意见
     *
     * @param
     * @return
     */
    public static RequestParams buildSubmitFeedbackRequest(Context context, String comment) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("comment", comment);
        return requestParams;
    }

    /**
     * 获取共同的好友
     *
     * @param id
     * @return
     */
    public static RequestParams buildGetSameFriendRequest(Context context, int id) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(id));
        return requestParams;
    }

    /**
     * 发送消息已存给服务器
     *
     * @param messageid
     * @return
     */
    public static RequestParams readMessageRequest(Context context, int messageid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("messageid", String.valueOf(messageid));
        return requestParams;
    }

    /**
     * 获取好友短信邀请码文本
     *
     * @return
     */
    public static RequestParams BuildGetSMSInviteRequest(Context context) {
        MyRequestParams requestParams = new MyRequestParams(context);
        return requestParams;
    }

    /**
     * 获取跑团短信邀请码文本
     *
     * @return
     */
    public static RequestParams BuildGetSMSGroupInviteRequest(Context context, int groupid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", groupid + "");
        return requestParams;
    }

    /**
     * 处理邀请码
     *
     * @param code
     * @return
     */
    public static RequestParams buildProcessInviteCodeRequest(Context context, String code) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("code", code);
        return requestParams;
    }

    /**
     * 获取微信分享邀请码
     *
     * @return
     */
    public static RequestParams BuildGetWxInviteRequest(Context context) {
        MyRequestParams requestParams = new MyRequestParams(context);
        return requestParams;
    }

    /**
     * 创建跑团
     *
     * @param mGroupName
     * @param mGroupDescription
     * @param mGroupAvatar
     * @param mGroupProvince
     * @param mGroupCity
     * @param mName
     * @param mEmail
     * @param mPhone
     * @param applyersocialid
     * @param mUserAvatar
     * @param mPartyAvatar
     * @return
     */
    public static RequestParams buildCreateGroupRequest(Context context, String mGroupName, String mGroupDescription,
                                                        String mGroupAvatar, String mGroupProvince, String
                                                                mGroupCity, String mName, String mEmail, String
                                                                mPhone, String applyersocialid, String mUserAvatar,
                                                        String mPartyAvatar) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("name", mGroupName);
        requestParams.addBodyParameter("description", mGroupDescription);
        requestParams.addBodyParameter("avatar", mGroupAvatar);
        requestParams.addBodyParameter("locationprovince", mGroupProvince);
        requestParams.addBodyParameter("locationcity", mGroupCity);
        requestParams.addBodyParameter("applyerrealname", mName);
        requestParams.addBodyParameter("applyemail", mEmail);
        requestParams.addBodyParameter("applyerphone", mPhone);
        requestParams.addBodyParameter("applyersocialid", applyersocialid);
        requestParams.addBodyParameter("picture1", mUserAvatar);
        requestParams.addBodyParameter("picture2", mPartyAvatar);
        return requestParams;
    }


    /**
     * 搜索跑团
     *
     * @return
     */
    public static RequestParams BuildSearchGroupByNameParams(Context context, String name) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("name", name);
        return requestParams;
    }

    /**
     * 获取跑团信息
     *
     * @param groupId
     * @return
     */
    public static RequestParams buildGetGroupInfoRequest(Context context, int groupId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(groupId));
        return requestParams;
    }

    /**
     * 获取跑团信息
     *
     * @param groupId
     * @return
     */
    public static RequestParams buildGetGroupDetailInfoRequest(Context context, int groupId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(groupId));
        return requestParams;
    }


    /**
     * 获取跑团成员列表
     *
     * @param groupid
     * @return
     */
    public static RequestParams buildGetGroupMemberListRequest(Context context, int groupid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(groupid));
        return requestParams;
    }


    /**
     * 获取赛事列表
     *
     * @return
     */
    public static RequestParams buildGetRaceListRequest(Context context) {
        MyRequestParams requestParams = new MyRequestParams(context);
        return requestParams;
    }

    /**
     * 获取赛事选手列表
     *
     * @param raceId
     * @return
     */
    public static RequestParams buildGetRacePersonListRequest(Context context, int raceId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("raceid", String.valueOf(raceId));
        return requestParams;
    }


    /**
     * 申请入团
     *
     * @param groupId
     * @return
     */
    public static RequestParams buildApplyJoinGroupRequest(Context context, int groupId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", String.valueOf(groupId));
        return requestParams;
    }

    /**
     * 同意入团
     *
     * @param groupId
     * @return
     */
    public static RequestParams buildAcceptJoinGroupRequest(Context context, int groupId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", String.valueOf(groupId));
        return requestParams;
    }

    /**
     * 修改跑团简介
     *
     * @param groupId
     * @param Description
     * @return
     */
    public static RequestParams buildupdateGroupDescriptionRequest(Context context, int groupId
            , String Description) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", String.valueOf(groupId));
        requestParams.addBodyParameter("description", Description);
        return requestParams;
    }

    /**
     * 更新跑团头像
     *
     * @param groupId
     * @param avatar
     * @return
     */
    public static RequestParams buildResetGroupAvatarRequest(Context context, int groupId
            , String avatar) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", String.valueOf(groupId));
        requestParams.addBodyParameter("avatar", avatar);
        return requestParams;
    }

    /**
     * 更新跑团主页图片
     *
     * @param groupId
     * @param wallpaper
     * @return
     */
    public static RequestParams buildSetGroupPictureRequest(Context context, int groupId
            , String wallpaper) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", String.valueOf(groupId));
        requestParams.addBodyParameter("wallpaper", wallpaper);
        return requestParams;
    }

    /**
     * 获取跑团成员排行
     *
     * @param groupid
     * @param type
     * @return
     */
    public static RequestParams buildGetGroupRankRequest(Context context, int groupid, int type) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", String.valueOf(groupid));
        requestParams.addBodyParameter("type", String.valueOf(type));
        return requestParams;
    }

    /**
     * 获取好友成员排行
     *
     * @param userid 目标用户ID
     * @param type   获取排行类型
     * @return
     */
    public static RequestParams buildGetFriendRankRequest(Context context, int userid, int type) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(userid));
        requestParams.addBodyParameter("type", String.valueOf(type));
        return requestParams;
    }

    /**
     * 获取跑团活动列表
     *
     * @param groupid
     * @return
     */
    public static RequestParams buildGetGroupPartyListRequest(Context context, int groupid, int userid, String date) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", String.valueOf(groupid));
        requestParams.addBodyParameter("date", date);
        requestParams.addBodyParameter("id", String.valueOf(userid));
        return requestParams;
    }


    /**
     * 创建活动
     *
     * @param groupid
     * @param mPartyName
     * @param mDescription
     * @param mRegistTIme
     * @param mStartTime
     * @param mLocation
     * @param province
     * @param city
     * @return
     */
    public static RequestParams buildCreatePartyRequest(Context context, int groupid, String mPartyName, String mDescription,
                                                        String mRegistTIme, String mStartTime, String endTime, String
                                                                mLocation,
                                                        String province, String city, String labellist, String
                                                                pictureUrl) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", String.valueOf(groupid));
        requestParams.addBodyParameter("name", mPartyName);
        requestParams.addBodyParameter("description", mDescription);
        requestParams.addBodyParameter("signupduetime", mRegistTIme);
        requestParams.addBodyParameter("starttime", mStartTime);
        requestParams.addBodyParameter("location", mLocation);
        requestParams.addBodyParameter("locationprovince", province);
        requestParams.addBodyParameter("locationcity", city);
        requestParams.addBodyParameter("labellist", labellist);
        requestParams.addBodyParameter("picture", pictureUrl);
        requestParams.addBodyParameter("endtime", endTime);
        return requestParams;
    }

    /**
     * 获取活动人员列表
     *
     * @param partyid
     * @return
     */
    public static RequestParams buildGetPartyMemberListRequest(Context context, int partyid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", String.valueOf(partyid));
        return requestParams;
    }

    /**
     * 获取二维码字符串
     *
     * @param id
     * @param type
     */
    public static RequestParams buildQrcodeRequest(Context context, int id, int type) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(id));
        requestParams.addBodyParameter("type", String.valueOf(type));
        return requestParams;
    }

    /**
     * 取消活动
     *
     * @param partyid
     * @return
     */
    public static RequestParams BuildCancelPartyParams(Context context, int partyid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(partyid));
        return requestParams;
    }


    /**
     * 开始活动
     *
     * @param partyid
     * @return
     */
    public static RequestParams BuildStartPartyParams(Context context, int partyid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(partyid));
        return requestParams;
    }


    /**
     * 开始活动
     *
     * @param partyid
     * @return
     */
    public static RequestParams BuildFinishPartyParams(Context context, int partyid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(partyid));
        return requestParams;
    }

    /**
     * 报名活动
     *
     * @param partyid
     * @return
     */
    public static RequestParams BuildSignupPartyParams(Context context, int partyid, String pace, int isleader) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", String.valueOf(partyid));
        requestParams.addBodyParameter("pace", pace);
        requestParams.addBodyParameter("isleader", isleader + "");
        return requestParams;
    }

    /**
     * 签到活动
     *
     * @return
     */
    public static RequestParams BuildCheckInParams(Context context, int partyid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", String.valueOf(partyid));
        return requestParams;
    }

    /**
     * 获取申请加入跑团列表
     *
     * @param groupid
     * @param fromid
     * @return
     */
    public static RequestParams buildGetGroupApplyMemberListRequest(Context context, int groupid, int fromid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", String.valueOf(groupid));
        requestParams.addBodyParameter("fromid", String.valueOf(fromid));
        return requestParams;
    }

    /**
     * 同意加入跑团
     *
     * @param groupid
     * @param id
     * @return
     */
    public static RequestParams buildGetAgreeGroupApplyRequest(Context context, int groupid, int id) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", String.valueOf(groupid));
        requestParams.addBodyParameter("id", String.valueOf(id));
        requestParams.addBodyParameter("result", "1");
        return requestParams;
    }

    /**
     * 获取用户参与的活动列表
     *
     * @return
     */
    public static RequestParams buildGetUserGroupPartyListRequest(Context context, int id, int groupid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(id));
        requestParams.addBodyParameter("groupid", String.valueOf(groupid));
        return requestParams;
    }

    /**
     * 获取用户所有的活动列表
     *
     * @param id
     * @return
     */
    public static RequestParams buildGetUserPartyListRequest(Context context, int id) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(id));
        return requestParams;
    }

    /**
     * 移除管理员
     *
     * @param groupid
     * @param memberid
     * @return
     */
    public static RequestParams buildRemoveManagerRequest(Context context, int groupid, int memberid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", String.valueOf(groupid));
        requestParams.addBodyParameter("id", String.valueOf(memberid));
        return requestParams;
    }

    /**
     * 增加管理员
     *
     * @param groupid
     * @param memberid
     * @return
     */
    public static RequestParams buildAndManagerRequest(Context context, int groupid, int memberid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", String.valueOf(groupid));
        requestParams.addBodyParameter("id", String.valueOf(memberid));
        return requestParams;
    }

    /**
     * 删除跑团成员
     *
     * @param groupid
     * @param userid
     * @return
     */
    public static RequestParams buildRemoveGroupMemberRequest(Context context, int groupid, int userid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", String.valueOf(groupid));
        requestParams.addBodyParameter("id", String.valueOf(userid));
        return requestParams;
    }


    /**
     * 请求解散跑团
     *
     * @param groupid
     * @param mName
     * @param mPhone
     * @param mGroupDescription
     * @return
     */
    public static RequestParams buildDismisGroupRequest(Context context, int groupid, String mName, String mPhone, String
            mGroupDescription) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", String.valueOf(groupid));
        requestParams.addBodyParameter("applyerrealname", mName);
        requestParams.addBodyParameter("applyerphone", mPhone);
        requestParams.addBodyParameter("comments", mGroupDescription);
        return requestParams;
    }

    /**
     * 判断当前密码是否正确
     *
     * @param password 密码
     * @return
     */
    public static RequestParams buildCheckOldPswRequest(Context context, String password) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("password", password);
        return requestParams;
    }

    /**
     * 绑定微信
     *
     * @param Unionid
     * @param WxNickName
     * @return
     */
    public static RequestParams buildBindWxAccountRequest(Context context, String Unionid, String WxNickName) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("unionid", Unionid);
        requestParams.addBodyParameter("nickname", WxNickName);
        return requestParams;
    }

    /**
     * 获取好友在跑团中的列表
     *
     * @param groupid
     * @return
     */
    public static RequestParams buildGetFriendListInGroup(Context context, int groupid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", String.valueOf(groupid));
        return requestParams;
    }

    /**
     * 邀请好友入团
     *
     * @param id
     * @param groupId
     * @return
     */
    public static RequestParams buildInviteFriend(Context context, int id, int groupId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(id));
        requestParams.addBodyParameter("groupid", String.valueOf(groupId));
        return requestParams;
    }


    /**
     * 推荐好友入团
     *
     * @param id
     * @param groupId
     * @return
     */
    public static RequestParams buildReCommendFriend(Context context, int id, int groupId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(id));
        requestParams.addBodyParameter("groupid", String.valueOf(groupId));
        return requestParams;
    }

    /**
     * 获取微信分享跑团请求
     *
     * @param groupid
     * @return
     */
    public static RequestParams BuildGetGroupWxInviteRequest(Context context, int groupid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", String.valueOf(groupid));
        return requestParams;
    }

    /**
     * 审核加好友
     * result 1.通过, 2.拒绝
     *
     * @return
     */
    public static RequestParams buildApproveAddFriend(Context context, int friendid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("friendid", String.valueOf(friendid));
        requestParams.addBodyParameter("result", "1");
        return requestParams;
    }

    /**
     * 删除好友
     *
     * @param friendid
     * @return
     */
    public static RequestParams BuildRemoveFriendParams(Context context, int friendid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("friends", String.valueOf(friendid));
        return requestParams;
    }

    /**
     * 分享历史
     *
     * @param workoutid
     * @return
     */
    public static RequestParams BuildGetWorkoutShareRequest(Context context, int workoutid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(workoutid));
        return requestParams;
    }


    /**
     * 获取广告图片链接
     *
     * @return
     */
    public static RequestParams buildDownloadAdpicRequest(Context context) {
        MyRequestParams requestParams = new MyRequestParams(context);
        return requestParams;
    }

    /**
     * 创建一个团标签
     *
     * @param groupId
     * @param lableName
     * @return
     */
    public static RequestParams buildCreateGroupLabel(Context context, int groupId, String lableName) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", groupId + "");
        requestParams.addBodyParameter("name", lableName + "");
        return requestParams;
    }

    /**
     * 获取团标签
     *
     * @param groupId
     * @return
     */
    public static RequestParams buildGetRunGroupLabelList(Context context, int groupId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", groupId + "");
        return requestParams;
    }

    /**
     * 删除一个团标签
     *
     * @param labelid
     * @return
     */
    public static RequestParams buildRemoveGroupLabel(Context context, int labelid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("labelid", labelid + "");
        return requestParams;
    }

    /**
     * 获取团员的标签列表
     *
     * @return
     */
    public static RequestParams buildGetPersonLabelList(Context context, int groupid, int userId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", groupid + "");
        requestParams.addBodyParameter("id", userId + "");
        return requestParams;
    }

    /**
     * 更新团员所属标签
     *
     * @return
     */
    public static RequestParams buildUpdatePersonLabelList(Context context, int groupId, int userId, String labelIdList) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", groupId + "");
        requestParams.addBodyParameter("id", userId + "");
        requestParams.addBodyParameter("labelidlist", labelIdList + "");
        return requestParams;
    }

    /**
     * 获取标签的人员列表
     *
     * @param labelid
     * @return
     */
    public static RequestParams buildGetLabelMemberList(Context context, int labelid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("labelid", labelid + "");
        return requestParams;
    }

    /**
     * 编辑标签的成员列表
     *
     * @param labelid
     * @param memberIdList
     * @return
     */
    public static RequestParams buildUpdateLabelMemberList(Context context, int labelid, String memberIdList) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("labelid", labelid + "");
        requestParams.addBodyParameter("memberlist", memberIdList);
        return requestParams;
    }

    /**
     * 设置团成员所属团内的团备注
     *
     * @param userId  被修改者ID
     * @param groupid 被修改者所属跑团
     * @param alias   修改备注内容
     * @return RequestParams
     */
    public static RequestParams buildSetMemberAlias(Context context, int userId, int groupid, String alias) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("groupid", groupid + "");
        requestParams.addBodyParameter("id", userId + "");
        requestParams.addBodyParameter("alias", alias);
        return requestParams;
    }

    /**
     * 设置个人的一个标签
     *
     * @param mUserId
     * @param mLabelId
     * @return
     */
    public static RequestParams buildAddMemberOneLabel(Context context, int mUserId, int mLabelId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", mUserId + "");
        requestParams.addBodyParameter("labelid", mLabelId + "");
        return requestParams;
    }

    /**
     * 删除个人的一个标签
     *
     * @param mUserId
     * @param mLabelId
     * @return
     */
    public static RequestParams buildRemoveMemberOneLabel(Context context, int mUserId, int mLabelId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", mUserId + "");
        requestParams.addBodyParameter("labelid", mLabelId + "");
        return requestParams;
    }

    /**
     * 点赞
     *
     * @param targetid
     * @param type
     * @return
     */
    public static RequestParams thumbUpYesRequest(Context context, String targetid, String type) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("targetid", targetid);
        requestParams.addBodyParameter("type", type);
        return requestParams;
    }

    /**
     * 点赞
     *
     * @param targetid
     * @param type
     * @return
     */
    public static RequestParams thumbUpNoRequest(Context context, String targetid, String type) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("targetid", targetid);
        requestParams.addBodyParameter("type", type);
        return requestParams;
    }

    /**
     * 增加一条评论
     *
     * @param targetid
     * @param type
     * @param replyuserid
     * @param comment
     * @return
     */
    public static RequestParams addCommentsRequest(Context context, String targetid, String type,
                                                   String replyuserid, String comment) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("targetid", targetid);
        requestParams.addBodyParameter("type", type);
        requestParams.addBodyParameter("replyuserid", replyuserid);
        requestParams.addBodyParameter("comment", comment);
        return requestParams;
    }

    /**
     * 获取活动内容
     *
     * @param partyid
     * @return
     */
    public static RequestParams buildGetPartyInfoRequest(Context context, int partyid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(partyid));
        return requestParams;
    }

    /**
     * 获取活动详细内容
     *
     * @param partyid
     * @return
     */
    public static RequestParams buildGetPartyDetailInfoRequest(Context context, int partyid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", String.valueOf(partyid));
        return requestParams;
    }


    /**
     * 设置定位签到的地点和距离
     *
     * @param latitude
     * @param longitude
     * @return
     * @Param partyId
     */
    public static RequestParams buildSetCheckInPosition(Context context, int partyId, double latitude, double longitude) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", partyId + "");
        requestParams.addBodyParameter("latitude", latitude + "");
        requestParams.addBodyParameter("longitude", longitude + "");
        return requestParams;
    }

    /**
     * 获取跑团成员的主页信息
     *
     * @param userId
     * @param groupId
     * @return
     */
    public static RequestParams buildGetRunGroupMemberInfor(Context context, int userId, int groupId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", userId + "");
        requestParams.addBodyParameter("groupid", groupId + "");
        return requestParams;
    }

    /**
     * 交换活动拥有者
     *
     * @param partyid
     * @param lastUserId
     * @param newUserId
     * @return
     */
    public static RequestParams buildChangePartyOwner(Context context, int partyid, int lastUserId, int newUserId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", partyid + "");
        requestParams.addBodyParameter("userid", lastUserId + "");
        requestParams.addBodyParameter("id", newUserId + "");
        return requestParams;
    }

    /**
     * 添加活动管理员
     *
     * @param partyid
     * @param userId
     * @return
     */
    public static RequestParams buildAddPartyManager(Context context, int partyid, int userId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", partyid + "");
        requestParams.addBodyParameter("id", userId + "");
        return requestParams;
    }

    /**
     * 添加活动管理员
     *
     * @param partyid
     * @param userId
     * @return
     */
    public static RequestParams buildRemovePartyManager(Context context, int partyid, int userId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", partyid + "");
        requestParams.addBodyParameter("id", userId + "");
        return requestParams;
    }

    /**
     * 获取用户可以远程签到的workout列表
     *
     * @param partyid
     * @return
     */
    public static RequestParams buildGetWorkoutListForParty(Context context, int partyid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", partyid + "");
        return requestParams;
    }

    /**
     * 设置用于签到的workout id的列表
     *
     * @param partyId
     * @param workoutlist
     * @return
     */
    public static RequestParams buildSetCheckInWorkOutList(Context context, int partyId, String workoutlist) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", partyId + "");
        requestParams.addBodyParameter("workoutlist", workoutlist);
        return requestParams;
    }

    /**
     * 获取某个用户活动签到的workouts
     *
     * @param partyId
     * @param id
     * @return
     */
    public static RequestParams buildGetCheckInWorkoutList(Context context, int partyId, int id) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", partyId + "");
        requestParams.addBodyParameter("id", id + "");
        return requestParams;
    }

    /**
     * 新增活动总结类容
     *
     * @param partyid
     * @param photoIdList
     * @param title
     * @param description
     * @return
     */
    public static RequestParams buildAddSummarySection(Context context, int partyid, String photoIdList,
                                                       String title, String description) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", partyid + "");
        requestParams.addBodyParameter("photolist", photoIdList);
        requestParams.addBodyParameter("title", title);
        requestParams.addBodyParameter("description", description);
        return requestParams;
    }

    /**
     * 提交活动总结
     *
     * @param partyid
     * @param showcheckinlist
     * @param showrankinglist
     * @return
     */
    public static RequestParams buildSubmitSummary(Context context, int partyid, int showcheckinlist, int showrankinglist) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", partyid + "");
        requestParams.addBodyParameter("showcheckinlist", showcheckinlist + "");
        requestParams.addBodyParameter("showrankinglist", showrankinglist + "");
        return requestParams;
    }

    /**
     * 上传活动照片URL
     *
     * @param partyid
     * @param pictureUrl
     * @return
     */
    public static RequestParams buildUploadPicture(Context context, int partyid, String pictureUrl) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", partyid + "");
        requestParams.addBodyParameter("url", pictureUrl);
        return requestParams;
    }

    /**
     * 获取简明的图片列表
     *
     * @param partyId
     * @return
     */
    public static RequestParams buildGetBriefPictureList(Context context, int partyId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", partyId + "");
        return requestParams;
    }

    /**
     * 获取活动的总结列表
     *
     * @param partyid
     * @return
     */
    public static RequestParams buildListSummarySection(Context context, int partyid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", partyid + "");
        return requestParams;
    }

    /**
     * 获取GroupPartyPhotoWallActivity picture 数据
     *
     * @param partyid
     * @return
     */
    public static RequestParams buildGetPictureWallList(Context context, int partyid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", partyid + "");
        return requestParams;
    }

    /**
     * 获取活动中单个成员上传的照片列表
     *
     * @param partyid
     * @param userId
     * @return
     */
    public static RequestParams buildGetOnePersonPictureList(Context context, int partyid, int userId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", partyid + "");
        requestParams.addBodyParameter("id", userId + "");
        return requestParams;
    }

    /**
     * 删除单张图片
     *
     * @param partyid
     * @param photoid
     * @return
     */
    public static RequestParams buildRemoveOnPicture(Context context, int partyid, int photoid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", partyid + "");
        requestParams.addBodyParameter("photoid", photoid + "");
        return requestParams;
    }

    /**
     * 获取跑团活动中的配速分档
     *
     * @return
     */
    public static RequestParams buildGetPartyPaceLevel(Context context) {
        MyRequestParams requestParams = new MyRequestParams(context);
        return requestParams;
    }

    /**
     * 通过跑步历史开始时间分享获取分享内容
     *
     * @param runnerid
     * @param startTime
     * @return
     */
    public static RequestParams BuildGetWorkoutShareByWorkoutNameRequest(Context context,
                                                                         int runnerid, String startTime) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("runnerid", runnerid + "");
        requestParams.addBodyParameter("starttime", startTime);
        return requestParams;
    }

    /**
     * 获取跑步历史返回
     *
     * @param workoutid
     * @param videoid
     * @return
     */
    public static RequestParams BuildGetHsitoryVideoShareRequest(Context context, int workoutid, int videoid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", videoid + "");
        requestParams.addBodyParameter("workoutid", workoutid + "");
        return requestParams;
    }


    /**
     * 获取点赞列表
     *
     * @param id
     * @param type
     * @return
     */
    public static RequestParams buildGetThumbListRequest(Context context, int id, int type) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("targetid", id + "");
        requestParams.addBodyParameter("type", type + "");
        return requestParams;
    }


    /**
     * 获取评论列表
     *
     * @param id
     * @param type
     * @return
     */
    public static RequestParams buildGetCommentsListRequest(Context context, int id, int type) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("targetid", id + "");
        requestParams.addBodyParameter("type", type + "");
        return requestParams;
    }

    /**
     * 获取回放视频列表
     *
     * @return
     */
    public static RequestParams buildGetPlaybackCameraListRequest(Context context, int id) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", id + "");
        return requestParams;
    }


    /**
     * 获取正在跑步的人员列表
     *
     * @return
     */
    public static RequestParams buildGetRunningUsersListRequest(Context context) {
        MyRequestParams requestParams = new MyRequestParams(context);
        return requestParams;
    }

    /**
     * 获取活动列表
     *
     * @param date
     * @return
     */
    public static RequestParams buildGetAllPartyListRequest(Context context, int id, String date) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("date", date);
        requestParams.addBodyParameter("id", id + "");
        return requestParams;
    }

    /**
     * 设置手动输入的锻炼记录
     *
     * @param length    长度，单位米
     * @param duration  时长，单位秒
     * @param starttime 开始时间
     * @param avgpace   平均配速，单位秒
     * @return
     */
    public static RequestParams buildCreateManualWorkOut(Context context, float length, long duration, String starttime, long avgpace) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("length", length + "");
        requestParams.addBodyParameter("duration", duration + "");
        requestParams.addBodyParameter("starttime", starttime + "");
        requestParams.addBodyParameter("avgpace", avgpace + "");
        return requestParams;
    }


    /**
     * 获取本人直播间的URL
     *
     * @return
     */
    public static RequestParams buildGetLiveRoomUrlRequest(Context context) {
        MyRequestParams requestParams = new MyRequestParams(context);
        return requestParams;
    }


    /**
     * 上传错误日志
     *
     * @return
     */
    public static RequestParams buildUploadCrashRequest(Context context, String info, String time) {
        MyRequestParams requestParams = new MyRequestParams(context);
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
     * 获取手机型号
     *
     * @return
     */
    public static RequestParams buildStartUpRequest(Context context) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("deviceos", 2+"");
        requestParams.addBodyParameter("appversion",URLConfig.USER_ANGENT);
        return requestParams;
    }

    /**
     * 上传内存泄露日志
     *
     * @return
     */
    public static RequestParams buildUploadLeakCanaryRequest(Context context, String info) {
        MyRequestParams requestParams = new MyRequestParams(context);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        String timeStr = df.format(System.currentTimeMillis());

        requestParams.addBodyParameter("leakcanaryinfo", info);
        requestParams.addBodyParameter("deviceos", "android " + android.os.Build.VERSION.RELEASE);
        requestParams.addBodyParameter("deviceinfo", android.os.Build.MODEL);
        requestParams.addBodyParameter("appversion", AppConfig.Version);
        requestParams.addBodyParameter("time", timeStr);
        return requestParams;
    }

    /**
     * 获取活动分享内容
     *
     * @param partyid
     * @return
     */
    public static RequestParams BuildGetPartySumShareRequest(Context context, int partyid) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("partyid", partyid + "");
        return requestParams;
    }

    /**
     * 获取日日报分享内容
     *
     * @return
     */
    public static RequestParams BuildGetWxShareDailyText(Context context) {
        MyRequestParams requestParams = new MyRequestParams(context);
        return requestParams;
    }


    /**
     * 获取个人直播间分享内容
     *
     * @return
     */
    public static RequestParams BuildGetLiveRoomShareRequest(Context context) {
        MyRequestParams requestParams = new MyRequestParams(context);
        return requestParams;
    }

    /**
     * 检查复制的
     *
     * @param invitetext
     * @return
     */
    public static RequestParams BuildGetParseInviteTextRequest(Context context, String invitetext) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("invitetext", invitetext);
        return requestParams;
    }

    /**
     * 上传语音
     *
     * @param targetid
     * @param type
     * @param replyuserid
     * @param mediatype
     * @param length
     * @param mediaurl
     * @return
     */
    public static RequestParams buildUploadLiveVoiceRequest(Context context, int targetid,
                                                            int type, int replyuserid, int mediatype, int length, String mediaurl) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("targetid", targetid + "");
        requestParams.addBodyParameter("type", type + "");
        requestParams.addBodyParameter("replyuserid", replyuserid + "");
        requestParams.addBodyParameter("mediatype", mediatype + "");
        requestParams.addBodyParameter("length", length + "");
        requestParams.addBodyParameter("mediaurl", mediaurl);
        return requestParams;
    }


    /**
     * 上传原始位置信息
     *
     * @param originaldata
     * @return
     */
    public static RequestParams uploadOgiginalPosition(Context context, String originaldata) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("originaldata", originaldata);
        return requestParams;
    }

    /**
     * 获取意见列表
     *
     * @return
     */
    public static RequestParams getCommentListRequest(Context context) {
        MyRequestParams requestParams = new MyRequestParams(context);
        return requestParams;
    }


    /**
     * 获取
     *
     * @return
     */
    public static RequestParams buildGetIsRunningRequest(final Context context, final int id) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("id", id + "");
        return requestParams;
    }

    /**
     * 获取评论点赞数量
     *
     * @param targetid targetid: 对于type为1 ，锻炼记录的id。 对于type为2，跑团活动id，对于type为3，视频片段id
     * @param type     1，锻炼记录，2，跑团活动，3，视频片段
     * @return
     */
    public static RequestParams buildGetSocialListRequest(final Context context, final int targetid, final int type) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("targetid", targetid + "");
        requestParams.addBodyParameter("type", type + "");
        return requestParams;
    }

    /**
     * 获取没有心率的链接
     *
     * @param context
     * @return
     */
    public static RequestParams buildGetNoneHeartbeatLinkRequest(final Context context) {
        MyRequestParams requestParams = new MyRequestParams(context);
        return requestParams;
    }


    /**
     * 获取准备跑的数据
     *
     * @param context
     * @param longitude
     * @param latitude
     * @return
     */
    public static RequestParams buildGetBeginRunInfoRequest(final Context context, final double latitude, double longitude) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("longitude", longitude + "");
        requestParams.addBodyParameter("latitude", latitude + "");
        return requestParams;
    }

    /**
     * 给闹钟
     *
     * @param context
     * @param personId
     * @return
     */
    public static RequestParams buildIncreaseClock(final Context context, final int personId) {
        MyRequestParams requestParams = new MyRequestParams(context);
        requestParams.addBodyParameter("personid", personId + "");
        return requestParams;
    }
}