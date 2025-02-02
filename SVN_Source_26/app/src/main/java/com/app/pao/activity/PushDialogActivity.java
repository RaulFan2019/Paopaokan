package com.app.pao.activity;

import android.content.Intent;

import com.app.pao.R;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.activity.group.GroupInfoActivity;
import com.app.pao.activity.party.PartyInfoActivity;
import com.app.pao.activity.run.LiveActivityV3;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetJpushMessageResult;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.lidroid.xutils.view.annotation.ContentView;

/**
 * Created by LY on 2016/4/13.
 */
@ContentView(R.layout.activity_push_dialog)
public class PushDialogActivity extends BaseAppCompActivity {

    private static final String TAG = "PushDialogActivity";

    @Override
    protected void initData() {
        final GetJpushMessageResult result = (GetJpushMessageResult) getIntent().getSerializableExtra("result");

        int resultType = result.getType();
        if (resultType == AppEnum.messageType.APPROVE_ADD_FRIEND) {//好友请求同意推送
            mDialogBuilder.setUserPushDialog(this,"你有了新好友",
                    result.fromuseravatar, result.fromusernickname,
                    result.location, "取消", "查看");
            mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceUserPushDialogListener() {
                @Override
                public void onLeftBtnClick() {
                    PushDialogActivity.this.finish();
                }

                @Override
                public void onRightBtnClick() {
                    Intent intent = new Intent();
                    intent.setClass(PushDialogActivity.this, UserInfoActivity.class);
                    intent.putExtra("userid", result.fromuserid);
                    startActivity(intent);
                    PushDialogActivity.this.finish();
                }

                @Override
                public void onCancel() {
                    PushDialogActivity.this.finish();
                }
            });
            //跑团邀请函推送
        } else if (resultType == AppEnum.messageType.INVITE_JOIN_RUN_GROUP) {
            mDialogBuilder.setGroupPushDialog(this,"发现跑团邀请函",
                    result.groupavatar, result.groupname,
                    "来自" + result.fromusernickname + "的邀请", "取消", "查看");
            mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceGroupPushDialogListener() {
                @Override
                public void onLeftBtnClick() {
                    PushDialogActivity.this.finish();
                }

                @Override
                public void onRightBtnClick() {
                    Intent intent = new Intent();
                    intent.setClass(PushDialogActivity.this, GroupInfoActivity.class);
                    intent.putExtra("groupid", result.getGroupid());
                    startActivity(intent);
                    PushDialogActivity.this.finish();
                }

                @Override
                public void onCancel() {
                    PushDialogActivity.this.finish();
                }
            });
        } else if (resultType == AppEnum.messageType.APPROVE_JOIN_RUN_GROUP) {//加入跑团推送
            mDialogBuilder.setGroupPushDialog(this, "你已入团",
                    result.groupavatar, result.groupname,
                    result.approvernickname + "同意你入团", "取消", "查看");
            mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceGroupPushDialogListener() {
                @Override
                public void onLeftBtnClick() {
                    PushDialogActivity.this.finish();
                }

                @Override
                public void onRightBtnClick() {
                    Intent intent = new Intent();
                    intent.setClass(PushDialogActivity.this, GroupInfoActivity.class);
                    intent.putExtra("groupid", result.getGroupid());
                    startActivity(intent);
                    PushDialogActivity.this.finish();
                }

                @Override
                public void onCancel() {
                    PushDialogActivity.this.finish();
                }
            });
        } else if (resultType == AppEnum.messageType.APPLY_JOIN_PARTY) {//邀请加入活动推送
            mDialogBuilder.setUserPushDialog(this, "你有活动邀请", result.getFromuseravatar(),
                    result.getFromusernickname(), "邀请你参加" + result.partyname + "活动", "取消", "查看");
            mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceUserPushDialogListener() {
                @Override
                public void onLeftBtnClick() {
                    PushDialogActivity.this.finish();
                }

                @Override
                public void onRightBtnClick() {
//                    Log.v(TAG,"partyid:" + result.partyid);
                    Intent intent = new Intent();
                    intent.setClass(PushDialogActivity.this, PartyInfoActivity.class);
                    intent.putExtra("partyid", result.partyid);
                    startActivity(intent);
                    PushDialogActivity.this.finish();
                }

                @Override
                public void onCancel() {
                    PushDialogActivity.this.finish();
                }
            });
        } else if (resultType == AppEnum.messageType.CANCLE_PARTY) {//活动取消推送
            mDialogBuilder.setGroupPushDialog(this, "活动已取消", result.groupavatar,
                    result.groupname, result.partyname + "活动已取消", null, null);
            mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceGroupPushDialogListener() {
                @Override
                public void onLeftBtnClick() {
                    PushDialogActivity.this.finish();
                }

                @Override
                public void onRightBtnClick() {
                    PushDialogActivity.this.finish();
                }

                @Override
                public void onCancel() {
                    PushDialogActivity.this.finish();
                }
            });
        } else if (resultType == AppEnum.messageType.START_RUN) {//请求围观推送
            mDialogBuilder.setUserPushDialog(this,"跑步求围观", result.fromuseravatar,
                    result.fromusernickname,
                    "正在跑步,快来围观吧!", "取消", "查看");
            mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceUserPushDialogListener() {
                @Override
                public void onLeftBtnClick() {
                    PushDialogActivity.this.finish();
                }

                @Override
                public void onRightBtnClick() {
                    Intent intent = new Intent(PushDialogActivity.this, LiveActivityV3.class);
                    intent.putExtra("userId", result.getFromuserid());
                    intent.putExtra("userNickName", result.getFromusernickname());
                    intent.putExtra("userGender", result.getFromusergender());
                    intent.putExtra("avatar", result.getFromuseravatar());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    PushDialogActivity.this.finish();
                }

                @Override
                public void onCancel() {
                    PushDialogActivity.this.finish();
                }
            });
        } else if (resultType == AppEnum.messageType.BROAD_CAST) {//系统广播
            mDialogBuilder.setOnlyNewsDialog(mContext, "系统消息", result.getMessage());
            mDialogBuilder.setListener(new MyDialogBuilderV1.OnlyNewsDialogListener() {
                @Override
                public void onCancel() {
                    PushDialogActivity.this.finish();
                }
            });
        } else if (resultType == AppEnum.messageType.GROUP_RECOMMEND) {//跑团推荐推送
            mDialogBuilder.setGroupPushDialog(this,"发现跑团推荐",
                    result.groupavatar, result.groupname,
                    "来自" + result.fromusernickname + "的推荐", "取消", "查看");
            mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceGroupPushDialogListener() {
                @Override
                public void onLeftBtnClick() {
                    PushDialogActivity.this.finish();
                }

                @Override
                public void onRightBtnClick() {
                    Intent intent = new Intent();
                    intent.setClass(PushDialogActivity.this, GroupInfoActivity.class);
                    intent.putExtra("groupid", result.getGroupid());
                    startActivity(intent);
                    PushDialogActivity.this.finish();
                }

                @Override
                public void onCancel() {
                    PushDialogActivity.this.finish();
                }
            });
        }
    }

    @Override
    protected void initViews() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {

    }
}
