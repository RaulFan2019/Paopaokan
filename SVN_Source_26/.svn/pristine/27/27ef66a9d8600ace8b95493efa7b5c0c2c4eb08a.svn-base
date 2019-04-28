package com.app.pao.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.activity.PushDialogActivity;
import com.app.pao.activity.friend.ApplyFriendListActivity;
import com.app.pao.activity.group.GroupApplyMemberListActivity;
import com.app.pao.activity.group.GroupInfoActivity;
import com.app.pao.activity.party.PartyInfoActivity;
import com.app.pao.activity.run.LiveActivityV3;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.MessageData;
import com.app.pao.data.db.UploadData;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.db.DBUploadEntity;
import com.app.pao.entity.event.EventJpush;
import com.app.pao.entity.network.GetInRunningResult;
import com.app.pao.entity.network.GetJpushMessageResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class PushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        //收到 Registration Id
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            SendRegistrationId(context, regId);
            //接收自定义消息
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);
            //接收通知
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            // 判断接收到的通知类型并在页面提示
            String value = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.d(TAG, "[MyReceiver] value: " + value);
            paserReceiverInfor(context, value, notifactionId);
            //用户点开通知
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            String value = bundle.getString(JPushInterface.EXTRA_EXTRA);
            paserJpushInfo(context, value);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..
            //Jpush网络链接状态发生改变
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            if (!connected) {
                JPushInterface.init(context); // 初始化 JPush
            }
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    /**
     * 接收通知并保存
     *
     * @param context
     * @param value
     */
    private void paserReceiverInfor(Context context, String value, int notifactionId) {

        if (LocalApplication.getInstance().getLoginUser(context) == null) {
            return;
        }
//        Log.v("MyRequestCallBack",value);
        GetJpushMessageResult result = JSON.parseObject(value, GetJpushMessageResult.class);
        //判断App是否在前台并且不再跑步中
        int userId = LocalApplication.getInstance().getLoginUser(context).getUserId();
        if (LocalApplication.getInstance().isActive()
                && result != null
                && WorkoutData.getUnFinishWorkout(context, userId) == null) {
            int resultType = result.getType();
            if (resultType == AppEnum.messageType.APPROVE_ADD_FRIEND || resultType == AppEnum.messageType.INVITE_JOIN_RUN_GROUP ||
                    resultType == AppEnum.messageType.APPROVE_JOIN_RUN_GROUP || resultType == AppEnum.messageType.GROUP_RECOMMEND ||
                    resultType == AppEnum.messageType.APPLY_JOIN_PARTY || resultType == AppEnum.messageType.CANCLE_PARTY ||
                    resultType == AppEnum.messageType.START_RUN ||
                    resultType == AppEnum.messageType.BROAD_CAST) {
                if(!DeviceUtils.isScreenChange(context)){
                    Intent intent = new Intent();
                    intent.setClass(context, PushDialogActivity.class);
                    intent.putExtra("result", result);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        }
        //若消息是需要保存的
        if (result.getType() != 0
                && result.getType() != AppEnum.messageType.START_RUN
                && result.getType() != AppEnum.messageType.BROAD_CAST) {
            MessageData.saveNewMessage(context, result);
        }
        DBUploadEntity uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_READ_MESSAGE, URLConfig
                .URL_READ_MESSAGE, String.valueOf(result.getId()), "",
                LocalApplication.getInstance().getLoginUser(context).userId);
        UploadData.createNewUploadData(context, uploadEntity);
        //发送消息事件
        switch (result.getType()) {
            //有新的好友消息
            case AppEnum.messageType.APPROVE_ADD_FRIEND:
            case AppEnum.messageType.APPLY_ADD_FRIEND:
                //有关团的消息
            case AppEnum.messageType.APPLY_JOIN_RUNGROUP:
            case AppEnum.messageType.APPROVE_JOIN_RUN_GROUP:
            case AppEnum.messageType.INVITE_JOIN_RUN_GROUP:
            case AppEnum.messageType.GROUP_RECOMMEND:
                //邀请进入活动
            case AppEnum.messageType.APPLY_JOIN_PARTY:
                //添加点赞
            case AppEnum.messageType.ADD_KUDOS:
                //添加评论
            case AppEnum.messageType.ADD_COMMENT:
                //取消活动
            case AppEnum.messageType.CANCLE_PARTY:
                //开始跑步
            case AppEnum.messageType.START_RUN:
                EventBus.getDefault().post(new EventJpush(result.getType(), notifactionId));
                break;
        }
    }

    /**
     * 用户点开通知
     *
     * @param value
     */
    private void paserJpushInfo(Context context, String value) {
        GetJpushMessageResult result = JSON.parseObject(value, GetJpushMessageResult.class);
        //点击事件
        switch (result.getType()) {
            //有关好友消息
            case AppEnum.messageType.APPROVE_ADD_FRIEND:
            case AppEnum.messageType.APPLY_ADD_FRIEND:
                Intent friendIntent = new Intent(context, ApplyFriendListActivity.class);
                friendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(friendIntent);
                break;
            //有关申请入团的消息
            case AppEnum.messageType.APPLY_JOIN_RUNGROUP:
                Intent applyIntent = new Intent(context, GroupApplyMemberListActivity.class);
                Bundle applybundle = new Bundle();
                applybundle.putInt("groupid", Integer.parseInt(result.getExtra()));
                applyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                applyIntent.putExtras(applybundle);
                context.startActivity(applyIntent);
                break;
            //其他有关团的消息
            case AppEnum.messageType.APPROVE_JOIN_RUN_GROUP:
            case AppEnum.messageType.INVITE_JOIN_RUN_GROUP:
            case AppEnum.messageType.GROUP_RECOMMEND:
                Intent groupIntent = new Intent(context, GroupInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("groupid", Integer.parseInt(result.getExtra()));
                groupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                groupIntent.putExtras(bundle);
                context.startActivity(groupIntent);
                break;
            //活动相关消息
            case AppEnum.messageType.APPLY_JOIN_PARTY:
                Intent partyIntent = new Intent(context, PartyInfoActivity.class);
                Bundle partyBundle = new Bundle();
                partyBundle.putInt("partyid", Integer.parseInt(result.getExtra()));
                partyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                partyIntent.putExtras(partyBundle);
                context.startActivity(partyIntent);
                break;
            //开始跑步
            case AppEnum.messageType.START_RUN:
                Intent runIntent = new Intent(context, LiveActivityV3.class);
                Bundle runBundle = new Bundle();
                runBundle.putInt("userId", result.getUserid());
                runBundle.putString("userNickName", result.getUsernickname());
                runBundle.putInt("userGender", result.getGender());
                runBundle.putString("avatar", result.getAvatar());
                runIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                runIntent.putExtras(runBundle);
                context.startActivity(runIntent);
                break;
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    /**
     * 把极光的设备号发给服务器
     */
    private void SendRegistrationId(Context context, String regId) {
        HttpUtils http = new HttpUtils();
        if (LocalApplication.getInstance().getLoginUser(context) == null) {
            return;
        }
        int userId = LocalApplication.getInstance().getLoginUser(context).getUserId();
        if (userId == AppEnum.DEFAULT_USER_ID) {
            return;
        }
        String POST_URL = URLConfig.URL_UPDATE_USER_INFO;

        RequestParams params = RequestParamsBuild.buildUpdateUserJpushIdRequest(context, regId);
        http.send(HttpMethod.POST, POST_URL, params, new MyRequestCallBack(context) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {

            }

            @Override
            protected void onRightResponse(String Response) {

            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 这个玩意应该算是透传
     *
     * @param context
     * @param bundle  msgtype userid
     */
    private synchronized void processCustomMessage(final Context context, final Bundle bundle) {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        Log.v(TAG, "extras:" + extras);
        GetJpushMessageResult result = JSON.parseObject(extras, GetJpushMessageResult.class);

        //开始跑步的消息
        if (result.type == AppEnum.messageType.START_RUN) {
            checkRunningState(context, result, bundle);
            //登出
        } else if (result.type == AppEnum.messageType.LOGOUT) {
            if (LocalApplication.getInstance().getLoginUser(context) == null) {
                return;
            }
            if (result.userid == LocalApplication.getInstance().getLoginUser(context).userId) {
                // 登出
//                if (messageType == MSG_TYPE_LOGOUT) {
//                    //TODO
//                }
            }
        }
    }


    /**
     * 检查跑步状态
     */
    private synchronized void checkRunningState(final Context context,
                                                final GetJpushMessageResult result,
                                                final Bundle bundle) {
        //若是今天发出的消息
        if (TimeUtils.checkIsToday(TimeUtils.stringToDate(result.sendtime))) {
            PostGetFriendRequst(context, result, bundle);
        }
    }

    /**
     * 获取好友列表请求
     */
    private synchronized void PostGetFriendRequst(final Context context,
                                                  final GetJpushMessageResult result, final Bundle bundle) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_IS_RUNNING;
        RequestParams params = RequestParamsBuild.buildGetIsRunningRequest(context, result.fromuserid);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(context) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
//                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                GetInRunningResult isRunning = JSON.parseObject(Response, GetInRunningResult.class);
                if (isRunning.isrunning == 1) {
                    makeRunningNotification(context, bundle);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 做一个跑步通知
     */
    private synchronized void makeRunningNotification(final Context context, final Bundle bundle) {
//        Log.v(TAG, "makeRunningNotification:");
        long id = System.currentTimeMillis() + 1000;
        JPushLocalNotification ln = new JPushLocalNotification();
        ln.setBuilderId(0);
        ln.setContent(bundle.getString(JPushInterface.EXTRA_MESSAGE));
        ln.setTitle(bundle.getString(JPushInterface.EXTRA_TITLE));
        ln.setNotificationId(id);
        ln.setExtras(bundle.getString(JPushInterface.EXTRA_EXTRA));
        ln.setBroadcastTime(id);

        JPushInterface.addLocalNotification(context, ln);
    }
}
