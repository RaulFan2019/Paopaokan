package com.app.pao.config;

/**
 * Created by Raul on 2015/10/19.
 * URL 配置
 */
public class URLConfig {

    public static final String USER_ANGENT = "Android " + android.os.Build.VERSION.RELEASE + ";"
            + android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL + ";" + AppConfig.Version;

    /**
     * 根据编译版本获取Host Ip 地址
     *
     * @return
     */
    public static String getHostIp() {
        //若编译版本为 Alpha 版本
        if (AppConfig.BUILD_TYPE == AppEnum.BuildType.BUILD_ALPHA) {
            return "http://121.43.113.78/";
        } else {
            return "http://www.123yd.cn/";
        }
    }

    /* 登录 . 注册 */
    //登录接口(手机,密码)
    public static final String URL_COMMON_LOGIN = getHostIp() + "xingjiansport/V2/User/login";
    //微信接口
    public static final String URL_WEIXIN_LOGIN = getHostIp() + "xingjiansport/V2/User/loginWithWeixin";
    //获取短信验证码
    public static final String URL_GET_VC_CODE = getHostIp() + "xingjiansport/V2/Sms/sendSms";
    //检查验证码
    public static final String URL_CHECK_VC_CODE = getHostIp() + "xingjiansport/V2/Sms/verificationCode";
    //注册
    public static final String URL_REGIST = getHostIp() + "xingjiansport/V2/User/registerUser/";
    //更新用户基本信息
    public static final String URL_UPDATE_USER_INFO = getHostIp() + "xingjiansport/V2/User/updateUserInfo/";
    //检查手机号码是否已注册
    public static final String URL_CHECK_PHONE_IS_REGIST = getHostIp() + "xingjiansport/V2/User/checkUserNameIsExist";
    //通过验证码登录
    public static final String URL_LOGIN_BY_VCCODE = getHostIp() + "xingjiansport/V2/User/loginByVerificationCode";
    //登出
    public static final String URL_LOGOUT = getHostIp() + "xingjiansport/V2/User/logout/";
    //检查当前登录的用户密码是否正确
    public static final String URL_CHECK_PASSWORD = getHostIp() + "xingjiansport/V2/User/checkPassword";
    //绑定微信用户
    public static final String URL_BIND_WX_ACCOUNT = getHostIp() + "xingjiansport/V2/User/bindWeixinAccount";

    /* 个人资料 */
    //上传头像
    public static final String URL_UPLOAD_AVATAR = getHostIp() + "xingjiansport/V2/Avatar/upload";
    //获取个人信息
    public static final String URL_GET_USER_INFO = getHostIp() + "xingjiansport/V2/User/getUserInfo/";
    //获取个人纪录信息
    public static final String URL_GET_PERSON_RECORDS = getHostIp() + "xingjiansport/V2/User/getPersonRecords/";


    /* 好友 */
    //申请加为好友
    public static final String URL_APPLY_ADD_FRIEND = getHostIp() + "xingjiansport/V2/User/applyAddFriend/";
    //审批用户加为好友
    public static final String URL_APPROVE_ADD_FRIEND = getHostIp() + "xingjiansport/V2/User/approveAddFriend/";
    //获取用户的所有申请列表
    public static final String URL_GET_APPLY_FRIEND_LIST = getHostIp() + "xingjiansport/V2/User/getApplyFriendList/";
    //获取好友列表
    public static final String URL_GET_FRIEND = getHostIp() + "xingjiansport/V2/User/getFriendList";
    //通过用户名搜索用户
    public static final String URL_SEARCH_USERS_BY_NAME = getHostIp() + "xingjiansport/V2/User/searchUserByName/";
    //通过用户名列表搜索用户
    public static final String URL_SEARCH_USERS_BY_NAME_LIST = getHostIp() + "xingjiansport/V2/User/searchUserByNameList/";
    //获取共同好友列表
    public static final String URL_GET_SAME_FRIEND = getHostIp() + "xingjiansport/V2/User/getCommonFriendList";
    //删除好友
    public static final String URL_REMOVE_FRIEND = getHostIp() + "xingjiansport/V2/User/removeFriends";
    //获取好友（排行）列表
    public static final String URL_GET_RANKING_FRIEND_LIST = getHostIp() + "xingjiansport/V2/User/getFriendRankingList";


    /* 跑步历史 */
    //上传历史URL
    public static final String URL_SAVE_DATA_SEGMENT = getHostIp() + "xingjiansport/V2/Workout/saveWorkoutSegment/";
    //删除跑步历史
    public static final String URL_REMOVE_WORKOUT_INFO = getHostIp() + "xingjiansport/V2/Workout/removeWorkout/";
    //创建一个新的手动输入的锻炼记录
    public static final String URL_CREATE_MANUAL_WORKOUT = getHostIp() + "xingjiansport/V2/Workout/createManualWorkout";
    //上传原始数据点
    public static final String URL_UPLOAD_ORIGINAL_POSITION = getHostIp() + "xingjiansport/V2/Workout/saveOriginalPosition";
    public static final String URL_GET_NONE_HEARTBEAT_LINK = getHostIp() + "xingjiansport/V2/Text/getHeartBeatUrl";

    /* App . 其他*/
    //获取二维码
    public static final String URL_GET_QRCODE = getHostIp() + "xingjiansport/V2/User/getQrCodeString/";
    //解析二维码页面
    public static final String URL_PARSE_QRCODE = getHostIp() + "xingjiansport/V2/User/parseQrCodeString/";
    //提交用户意见
    public static final String URL_GET_PUBLISH_COMMENT = getHostIp() + "xingjiansport/V2/User/publishComment";
    //标记消息已读
    public static final String URL_READ_MESSAGE = getHostIp() + "xingjiansport/V2/User/markMessageAsRead/";
    //获取短消息文本(好友)
    public static final String URL_GET_FRIEND_SMS_INVITE = getHostIp() + "xingjiansport/V2/Text/getSmsInviteText";
    //获取短消息文本(跑团)
    public static final String URL_GET_RUN_GROUP_SMS_INVITE = getHostIp() + "xingjiansport/V2/Text/getSmsRungroupInviteText";
    //处理邀请码
    public static final String URL_PROCESS_INVITE_CODE = getHostIp() + "xingjiansport/V2/Invitecode/processInviteCode";
    //APP升级检查
    public static final String URL_CHECK_UPDATE = getHostIp() + "xingjiansport/V1/Androidrelease/getLatestRelease";
    //获取welcome的图片
    public static final String URL_DOWNLOAD_AD_PIC = getHostIp() + "xingjiansport/V2/User/getWelcomePicture/";

    /* 跑步历史 */
    //获取跑步历史列表
    public static final String URL_GET_WORKOUT_LIST = getHostIp() + "xingjiansport/V2/Workout/listWorkout/";
    //获取跑步历史详情
    public static final String URL_GET_WORKOUT_INFO = getHostIp() + "xingjiansport/V2/Workout/getWorkoutInfo/";
    //获取跑步历史点赞列表
    public static final String URL_GET_THUMB_UP_LIST = getHostIp() + "xingjiansport/V2/Workout/listThumbup/";
    //给跑步历史点赞
    public static final String URL_THUMB_UP_TO_WORKOUT = getHostIp() + "xingjiansport/V2/Workout/addThumbuptoWorkout/";
    //分享跑步历史
    public static final String URL_SHARE_WORKOUT = getHostIp() + "xingjiansport/V2/Text/getWeixinShareWorkoutText";
    //分享跑步历史
    public static final String URL_SHARE_HISTORY_VIDEO = getHostIp() + "xingjiansport/V2/Text/getVideoShareText";
    //获取评论点赞列表
    public static final String URL_GET_SOCIAL_LIST = getHostIp() + "xingjiansport/V2/Social/listSocial/";
    //获取用户闹钟列表
    public static final String URL_GET_USER_CLOCK = getHostIp() + "xingjiansport/V2/User/getUserClockList/";
    //增加闹钟
    public static final String URL_INCREASE_CLOCK = getHostIp() + "xingjiansport/V2/User/increasePersonClockCount";

    /* 跑团 */
    //获取跑团列表
    public static final String URL_GET_GROUP_LIST = getHostIp() + "xingjiansport/V2/Rungroup/getPersonRungroupInfo/";
    //创建跑团
    public static final String URL_CREATE_GROUP = getHostIp() + "xingjiansport/V2/Rungroup/createRungroup";
    //搜索跑团
    public static final String URL_SEARCH_GROUP = getHostIp() + "xingjiansport/V2/Rungroup/queryRungroup";
    //获取跑团信息
    public static final String URL_GET_GROUP_INFO = getHostIp() + "xingjiansport/V2/Rungroup/getRungroupInfo";
    //获取跑团详细信息（new d：2016/1/26）
    public static final String URL_GET_GROUP_DETAIL_INFO = getHostIp() +
            "xingjiansport/V2/Rungroup/getRungroupDetailInfo";
    //获取跑团成员列表
    public static final String URL_GET_GROUP_MEMBER_LIST = getHostIp() +
            "xingjiansport/V2/Rungroup/getRungroupMemberList";
    //申请加入跑团
    public static final String URL_APPLY_JOIN_GROUP = getHostIp() + "xingjiansport/V2/Rungroup/applyJoinRungroup";
    //同意加入跑团
    public static final String URL_ACCEPT_JOIN_GROUP = getHostIp() + "xingjiansport/V2/Rungroup/acceptJoinRungroup";
    //更新跑团信息
    public static final String URL_UPDATE_GROUP_INFO = getHostIp() + "xingjiansport/V2/Rungroup/updateRungroupInfo";
    //获取跑团排行列表
    public static final String URL_GET_RUNGROUP_RANK_LIST = getHostIp() +
            "xingjiansport/V2/Rungroup/getRungroupRankingList";
    //获取申请加入跑团列表
    public static final String URL_GET_GROUP_APPLY_MEMBER_LIST = getHostIp() +
            "xingjiansport/V2/Rungroup/getRungroupApplicationList";
    //审批用户加入跑团
    public static final String URL_AGREE_GROUP_APPLY = getHostIp() + "xingjiansport/V2/Rungroup/approveJoinRungroup";
    //增加管理员
    public static final String URL_GROUP_ADD_MANAGER = getHostIp() + "xingjiansport/V2/Rungroup/addRungroupManager";
    //解除管理员
    public static final String URL_GROUP_REMOVE_MANAGER = getHostIp() +
            "xingjiansport/V2/Rungroup/removeRungroupManager";
    //删除跑团成员
    public static final String URL_GROUP_REMOVE_MEMBER = getHostIp() + "xingjiansport/V2/Rungroup/removeRungroupMember";
    //解散跑团
    public static final String URL_GROUP_DISSMIS = getHostIp() + "xingjiansport/V2/Rungroup/dismissRungroup";
    //获取好友在跑团中的角色
    public static final String URL_GET_FRIEND_LIST_IN_GROUP = getHostIp() +
            "xingjiansport/V2/User/getFriendListWithGroupRole";
    //邀请好友入团
    public static final String URL_GROUP_INVITE_FRIEND = getHostIp() + "xingjiansport/V2/Rungroup/inviteJoinRungroup";
    //推荐好友入团
    public static final String URL_GROUP_RECOMMEND_FRIEND = getHostIp() + "xingjiansport/V2/Rungroup/recommendRungroup";
    //获取跑团微信文本
    public static final String URL_GET_WEIXIN_SHARE_TEXT = getHostIp() +
            "xingjiansport/V2/Text/getWeixinRungroupInviteText";
    //创建团标签
    public static final String URL_CREATE_LABEL = getHostIp() + "xingjiansport/V2/Label/createLabel/";
    //获取跑团的所有标签
    public static final String URL_GET_RUN_GROUP_LABEl_LIST = getHostIp() +
            "xingjiansport/V2/Label/getRungroupLableList/";
    //删除标签
    public static final String URL_REMOVE_LABEL = getHostIp() + "xingjiansport/V2/Label/removeLabel/";
    //获取个人的团标签列表
    public static final String URL_GET_PERSON_LABEL_LIST = getHostIp() + "xingjiansport/V2/Label/getPersonLabelList/";
    //批量操作修改团员标签
    public static final String URL_UPDATE_PERSON_LABEL_LIST = getHostIp() +
            "xingjiansport/V2/Label/updatePersonLableList/";
    //增加团员标签
    public static final String URL_ADD_PERSON_LABEL_LIST = getHostIp() + "xingjiansport/V2/Label/addPersonLabel/";
    //删除团员标签
    public static final String URL_REMOVE_PERSON_LABEL_LIST = getHostIp() + "xingjiansport/V2/Label/removePersonLabel/";
    //获取标签的人员列表
    public static final String URL_GET_LABEL_MEMBER_LIST = getHostIp() + "xingjiansport/V2/Label/getLableMemberList/";
    //编辑标签人员列表
    public static final String URL_UPDATE_LABEL_MEMBER_LIST = getHostIp() +
            "xingjiansport/V2/Label/updateLableMemberList/";
    //设置跑团成员的团名片（备注）
    public static final String URL_SET_MEMBER_ALIAS = getHostIp() + "xingjiansport/V2/Rungroup/setMemberAlias/";
    //获取个人跑团名片信息
    public static final String URL_GET_RUNGROUP_MEMBER_INFOR = getHostIp() +
            "xingjiansport/V2/Rungroup/getRungroupMemberInfo";


    /* 直播 */
    //获取摄像头列表
    public static final String URL_GET_CAMERA_LIST = getHostIp() + "xingjiansport/V2/Camera/getCameraList";
    //获取用户跑步最后数据
    public static final String URL_GET_LATEST_WORKOUT_DATA = getHostIp() + "xingjiansport/V2/Workout/getLatestData/";
    //获取赛事列表
    public static final String URL_GET_RACE_LIST = getHostIp() + "xingjiansport/V2/Race/getCurrentRaceList/";
    //获取赛事选手列表
    public static final String URL_GET_RACE_PERSON_LIST = getHostIp() + "xingjiansport/V2/Race/getLivePersonList/";
    //获取短信分享文本
    public static final String URL_GETFRIEND_WX_INVITE = getHostIp() + "xingjiansport/V2/Text/getWeixinInviteText";
    //获取直播间URL
    public static final String URL_GET_LIVE_ROOM_URL = getHostIp() + "xingjiansport/V2/User/getUserLivingRoomUrl";
    //微信分享的跑步直播间
    public static final String URL_GET_WEIXIN_LIVEINGROOM_URL = getHostIp() + "xingjiansport/V2/Text/getWeixinLiveingRoomText";
    //个人跑步直播间URL
    public static final String URL_GET_APP_LIVEING_ROOM_URL = getHostIp() + "xingjiansport/V2/Text/getAppLiveingRoomText";
    //个人总结分享Url
    public static final String URL_GET_WEIXIN_PERSON_SHARE_URL = getHostIp() + "xingjiansport/V2/Text/getWeixinPersonShareText";

    /* 活动 */
    //创建活动
    public static final String URL_CREATE_PARTY = getHostIp() + "xingjiansport/V2/Party/createParty";
    //获取活动人员列表
    public static final String URL_GET_PARTY_MEMBER_LIST = getHostIp() + "xingjiansport/V2/Party/getPartyMemberList";
    //取消活动
    public static final String URL_PARTY_CANCEL = getHostIp() + "xingjiansport/V2/Party/cancelParty";
    //开始活动
    public static final String URL_PARTY_START = getHostIp() + "xingjiansport/V2/Party/beginParty";
    //结束活动
    public static final String URL_PARTY_FINSIH = getHostIp() + "xingjiansport/V2/Party/finishParty";
    //报名活动
    public static final String URL_PARTY_SIGNUP = getHostIp() + "xingjiansport/V2/Party/signupParty";
    //签到活动
    public static final String URL_PARTY_CHECK_IN = getHostIp() + "xingjiansport/V2/Party/checkinParty";
    //获取跑团活动列表
    public static final String URL_GET_USER_GROUP_PARTY_LIST = getHostIp() +
            "xingjiansport/V2/Party/getUserPartyList/";
    //转交活动拥有者
    public static final String URL_CHANGE_PARTY_OWNER = getHostIp() + "xingjiansport/V2/Party/changePartyOwner";
    //添加活动管理员
    public static final String URL_ADD_PARTY_MANAGER = getHostIp() + "xingjiansport/V2/Party/addPartyManager";
    //移除活动管理员
    public static final String URL_REMOVE_PARTY_MANAGER = getHostIp() + "xingjiansport/V2/Party/removePartyManager";
    //获取动态
    public static final String URL_GET_DYNAMIC = getHostIp() + "xingjiansport/V2/Dynamic/listDynamic/";
    //取消点赞
    public static final String URL_THUMB_UP_NO = getHostIp() + "xingjiansport/V2/Social/removeThumbup/";
    //点赞
    public static final String URL_THUMB_UP_YES = getHostIp() + "xingjiansport/V2/Social/addThumbup/";
    //发送评论
    public static final String URL_ADD_COMMENT = getHostIp() + "xingjiansport/V2/Social/addComment/";
    //发送点赞
    public static final String URL_ADD_THUMB = getHostIp() + "xingjiansport/V2/Social/addThumbup/";
    //获取活动详情
    public static final String URL_GET_PARTY_INFO = getHostIp() + "xingjiansport/V2/Party/getPartyInfo";
    //设置跑团活动定位签到地址
    public static final String URL_SET_CHECK_IN_POSITION = getHostIp() + "xingjiansport/V2/Party/setCheckinPosition/";
    //获取用户可以远程签到的workout列表
    public static final String URL_GET_WORKPUT_LIST_FOR_PARTY = getHostIp() +
            "xingjiansport/V2/Party/getWorkoutListForParty";
    //设置用于签到的WorkOut列表
    public static final String URL_SET_CHECK_IN_WORKOUT_LIST = getHostIp() +
            "xingjiansport/V2/Party/setCheckInWorkoutList";
    //获取某个用户活动签到的workout
    public static final String URL_GET_CHECK_IN_WORKOUT_LIST = getHostIp() +
            "xingjiansport/V2/Party/getCheckinWorkoutList";
    //增加活动总结类容
    public static final String URL_ADD_SUMMARY_SECTION = getHostIp() + "xingjiansport/V2/Party/addSummarySection";
    //提交活动总结
    public static final String URL_SUBMIT_SUMMARY = getHostIp() + "xingjiansport/V2/Party/submitSummary";
    //上传活动照片
    public static final String URL_UPLOAD_PICTURE = getHostIp() + "xingjiansport/V2/Party/uploadPicture";
    //获取简明的图片列表
    public static final String URL_GET_BRIEF_PICTURE_LIST = getHostIp() + "xingjiansport/V2/Party/getBriefPictureList";
    //获取活动的总结列表
    public static final String URL_LIST_SUMMARY_SECTION = getHostIp() + "xingjiansport/V2/Party/listSummarySection";
    //获取活动的图片列表(GroupPartyPhotoWallActivity)
    public static final String URL_GET_PICTURE_WALL_LIST = getHostIp() + "xingjiansport/V2/Party/getPictureList";
    //获取活动中单个成员上传的照片列表
    public static final String URL_GET_ONE_PERSON_PICTURE_LIST = getHostIp() +
            "xingjiansport/V2/Party/getOnePersonPictureList";
    //删除活动中的单张图片
    public static final String URL_REMOVE_ONE_PICTURE = getHostIp() + "xingjiansport/V2/Party/removePicture";
    //获取跑团活动中的配速分档
    public static final String URL_GET_PARTY_PACE_LEVEL = getHostIp() + "xingjiansport/V2/Party/getPartyPaceLevel/";
    //获得评论列表
    public static final String URL_GET_COMMENTS = getHostIp() + "xingjiansport/V2/Social/listComments/";
    //获得点赞列表
    public static final String URL_GET_THUMBUPS = getHostIp() + "xingjiansport/V2/Social/listThumbup/";
    public static final String URL_GET_PLAYBACK_CAMERA_LIST = getHostIp() +
            "xingjiansport/V2/Workout/getWorkoutReplayVideoInfo";
    public static final String URL_GET_RUNNING_UERS = getHostIp() + "xingjiansport/V2/Dynamic/listRunningUsers/";

    public static final String URL_GET_IS_RUNNING = getHostIp() + "xingjiansport/V2/User/isUserRunning";
    //获取所有活动列表
    public static final String URL_GET_ALL_PARTY = getHostIp() + "xingjiansport/V2/Party/getAllPartyList";
    //获取活动详细信息
    public static final String URL_GET_PARTY_DETAIL_INFO = getHostIp() + "xingjiansport/V2/Party/getPartyDetailInfo";
    //获取活动总结分享文本
    public static final String URL_GET_PARTY_SUM_SHARE_TEXT = getHostIp() + "xingjiansport/V2/Text/getWeixinPartySumText";
    //获取日日报的分享信息
    public static final String URL_GET_WX_SHARE_DAILY_TEXT = getHostIp() + "xingjiansport/V2/Text/getWeixinShareDailyText";
    //获取活动分享文本
    public static final String URL_GET_PARTY_SHARE_TEXT = getHostIp() + "xingjiansport/V2/Text/getWeixinPartyInviteText";

    //上传Crash信息
    public static final String URL_UPLOAD_CRASH = getHostIp() + "xingjiansport/V2/Text/saveDebugInfo";

    public static final String URL_GET_PHONE_TYPE = getHostIp() + "xingjiansport/V2/User/markStartup";

    //上传Crash信息
    public static final String URL_UPLOAD_MAIDIAN = getHostIp() + "xingjiansport/V2/UsersOperation/saveUIOperationRecord";
    //直播间分享
    public static final String URL_LIVE_ROOM_SHARE = getHostIp() + "xingjiansport/V2/Text/getWeixinLiveingRoomText";
    //检查复制的文本格式
    public static final String URL_PARSE_INVITE_TEXT = getHostIp() + "xingjiansport/V2/Invitecode/parseInviteText";

    //检查复制的文本格式
    public static final String URL_GET_COMMENT_LIST = getHostIp() + "xingjiansport/V2/User/getCommentList";

    //获取准备跑的信息
    public static final String URL_GET_BEGIN_RUNINFO = getHostIp() + "xingjiansport/V2/Run/getBeginRunInfo";
}

