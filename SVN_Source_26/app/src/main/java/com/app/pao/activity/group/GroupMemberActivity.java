package com.app.pao.activity.group;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupDetailInfoRequest;
import com.app.pao.entity.network.GetGroupMemberInforResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.ui.widget.tagview.Tag;
import com.app.pao.ui.widget.tagview.TagView;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 2016/1/10.
 * 跑团成员资料
 */
@ContentView(R.layout.activity_group_member)
public class GroupMemberActivity extends BaseAppCompActivity implements View.OnClickListener {


    /* contains */
    private static final String TAG = "GroupMemberActivity";

    private static final int MSG_POST_OK = 0;
    private static final int MSG_POST_ERROR = 1;

    /* local view */
    @ViewInject(R.id.tv_title_group_member_list)
    private TextView mTitleStr;
    @ViewInject(R.id.iv_user_avatar)
    private CircularImage mAvatarIv;//头像
    @ViewInject(R.id.tv_user_nickname)
    private TextView mNickNameTv;//昵称文本
    @ViewInject(R.id.tv_role)
    private TextView mRoleTv;//角色文本
    @ViewInject(R.id.iv_gander)
    private ImageView mGanderIv;//性别文本
    @ViewInject(R.id.tv_user_age)
    private TextView mAgeTv;//年龄文本
    @ViewInject(R.id.tv_remark)
    private TextView mRemarkTv;//团名片文本
    @ViewInject(R.id.tagv_member_tag)
    private TagView mMemberTagv;
    @ViewInject(R.id.tv_party)
    private TextView mPartyTv;//参加活动的文本
    @ViewInject(R.id.tv_last_activity)
    private TextView mLastActivityTv;//最后一次活跃文本
    @ViewInject(R.id.btn_remove)
    private Button mRemoveBtn;
    @ViewInject(R.id.tv_tag_none)
    private TextView mNoneTagTv;


    @ViewInject(R.id.iv_remark_next)
    private ImageView mRemarkNextIv;//跳转箭头
    @ViewInject(R.id.iv_tag_next)
    private ImageView mTagNextIv;
    //    @ViewInject(R.id.iv_party_next)
//    private ImageView mPartyNextIv;
    @ViewInject(R.id.ll_remark)
    private LinearLayout mRemarkNextLl;
    @ViewInject(R.id.ll_tag)
    private LinearLayout mTagNextLl;
//    @ViewInject(R.id.ll_party)
//    private LinearLayout mPartyNextLl;

    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;

    private BitmapUtils bitmapUtils;

    /* local data */
    private GetGroupDetailInfoRequest mGroupInfor;
    private int mUserId;
    private GetGroupMemberInforResult mMemberInfor;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_OK) {
                mLoadV.setVisibility(View.GONE);
                mLoadViewRl.setVisibility(View.GONE);
                showViews();
            } else if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, msg.obj.toString());
                mLoadV.setLoadingText("加载失败");
                mReladV.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    @OnClick({R.id.ll_user, R.id.ll_remark, R.id.ll_tag, R.id.ll_party, R.id.btn_remove, R.id.title_bar_left_menu, R
            .id.tv_reload})
    public void onClick(View v) {
        switch (v.getId()) {
            //跳转到用户个人界面
            case R.id.ll_user:
                launchMemberMain();
                break;
            //跳转到团名片编辑页面
            case R.id.ll_remark:
                launchRemarkEdit();
                break;
            //跳转到个人团标签管理界面
            case R.id.ll_tag:
                launchMemberTagList();
                break;
            //跳转到活动列表页面
            case R.id.ll_party:
                launchMemberPartyList();
                break;
            //移出跑团
            case R.id.btn_remove:
                showTipDialog();
                break;
            //back
            case R.id.title_bar_left_menu:
                finish();
                break;
            case R.id.tv_reload:
                mReladV.setVisibility(View.GONE);
                mLoadV.setLoadingText("加载中...");
                getMemberInfor();
                break;
        }
    }


    @Override
    protected void initData() {
        mGroupInfor = (GetGroupDetailInfoRequest) getIntent().getExtras().getSerializable("group");
        mUserId = getIntent().getIntExtra("userid", 0);

        bitmapUtils = new BitmapUtils(mContext);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.icon_user_photo);
    }

    @Override
    protected void initViews() {
        mTitleStr.setText("团员信息");
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {
        getMemberInfor();
    }

    @Override
    protected void destroy() {

    }

    /**
     * 初始化显示View
     */
    private void showViews() {
        bitmapUtils.display(mAvatarIv, mMemberInfor.getAvatar());
        mNickNameTv.setText(mMemberInfor.getNickname());
        if (!mMemberInfor.getAlias().isEmpty()) {
            mRemarkTv.setText(mMemberInfor.getAlias());
        } else {
            mRemarkTv.setText("暂无");
        }
        if (!mMemberInfor.getBirthdate().isEmpty()) {
            mAgeTv.setText(TimeUtils.getAgeFromBirthDay(mMemberInfor.getBirthdate()) + "岁");
        } else {
            mAgeTv.setText("暂无");
        }

        //性别
        if (mMemberInfor.getGender() == AppEnum.UserGander.MAN) {
            mGanderIv.setBackgroundResource(R.drawable.icon_man);
        } else {
            mGanderIv.setBackgroundResource(R.drawable.icon_women);
        }

        //角色
        if (mMemberInfor.getRole() == AppEnum.groupRole.OWNER) {
            mRoleTv.setVisibility(View.VISIBLE);
            mRoleTv.setText("团长");
        } else if (mMemberInfor.getRole() == AppEnum.groupRole.MANAGER) {
            mRoleTv.setVisibility(View.VISIBLE);
            mRoleTv.setText("管理员");
        } else {
            mRoleTv.setVisibility(View.INVISIBLE);
        }

        //判断显示标签UI
        showMemberTag();

        String partyStr = mMemberInfor.getCheckinpartycount() + "/" + mMemberInfor.getSignuppartycount();
        String lastAcTimeStr = "暂无";
        if (mMemberInfor.getLaststarttime() != null && !mMemberInfor.getLaststarttime().isEmpty()) {
            lastAcTimeStr = TimeUtils.getTimestampDayString(TimeUtils.stringToDateAndMinS(mMemberInfor
                    .getLaststarttime()));
        } else {
            partyStr = "暂无";
        }
        mPartyTv.setText(partyStr);
        mLastActivityTv.setText(lastAcTimeStr);

        //只有相应的权限才可去进行操作
        if (mGroupInfor.getRungroup().getRole() == AppEnum.groupRole.OWNER
                || mGroupInfor.getRungroup().getRole() == AppEnum.groupRole.MANAGER
                || mUserId == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
            mRemoveBtn.setVisibility(View.VISIBLE);
            mRemarkNextIv.setVisibility(View.VISIBLE);
            mTagNextIv.setVisibility(View.VISIBLE);
            //    mPartyNextIv.setVisibility(View.VISIBLE);
            mRemarkNextLl.setEnabled(true);
            mTagNextLl.setEnabled(true);
            //    mPartyNextTv.setEnabled(true);
        } else {
            mRemoveBtn.setVisibility(View.GONE);
            mRemarkNextIv.setVisibility(View.INVISIBLE);
            mTagNextIv.setVisibility(View.INVISIBLE);
            //    mPartyNextIv.setVisibility(View.GONE);
            mRemarkNextLl.setEnabled(false);
            mTagNextLl.setEnabled(false);
            //     mPartyNextTv.setEnabled(false);
        }
        if (mMemberInfor.getId() == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
            mRemoveBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 显示成员的TAG
     */
    private void showMemberTag() {
        List<GetGroupMemberInforResult.Label> mLabelList = mMemberInfor.getLabel();
        if (mLabelList == null || mLabelList.size() == 0) {
            mNoneTagTv.setVisibility(View.VISIBLE);
            mMemberTagv.setVisibility(View.GONE);
            return;
        } else {
            mNoneTagTv.setVisibility(View.GONE);
            mMemberTagv.setVisibility(View.VISIBLE);
        }
        List<Tag> tagList = new ArrayList<>();
        for (int i = 0; i < mLabelList.size(); i++) {
            Tag tag = new Tag(mLabelList.get(i).getName());
            tag.layoutColor = Color.TRANSPARENT;
            tag.tagTextColor = Color.parseColor("#F06522");
            tag.deleteIndicatorColor = Color.parseColor("#F06522");
            tag.tagTextSize = 12;
            tag.radius = 2;
            tag.layoutBorderColor = Color.parseColor("#F06522");
            tag.layoutBorderSize = 1;
            tagList.add(tag);
        }
        mMemberTagv.addTags(tagList);
    }

    /**
     * 团备注编辑
     */
    private void launchRemarkEdit() {
        Bundle b = new Bundle();
        b.putInt("groupId", mGroupInfor.getRungroup().getId());
        b.putInt("userId", mUserId);
        b.putString("userAlias", mMemberInfor.getAlias());
        startActivity(GroupRemarkEditActivity.class, b);
    }

    /**
     * 团用户标签
     */
    private void launchMemberTagList() {
        Bundle b = new Bundle();
        b.putInt("groupId", mGroupInfor.getRungroup().getId());
        b.putInt("userId", mUserId);
        startActivity(GroupMemberTagListActivity.class, b);
    }

    /**
     * 用户主页
     */
    private void launchMemberMain() {
        if (LocalApplication.getInstance().getLoginUser(mContext).userId != mUserId) {
            Bundle bundle = new Bundle();
            bundle.putInt("userid", mUserId);
            startActivity(UserInfoActivity.class, bundle);
        }
    }

    /**
     * 用户活动列表
     */
    private void launchMemberPartyList() {
        Bundle b = new Bundle();
        b.putInt("userid", mUserId);
        b.putString("userName", mMemberInfor.getNickname());
        b.putInt("groupid", mGroupInfor.getRungroup().getId());
        startActivity(GroupUserPartyListActivity.class, b);
    }

    /**
     * 删除提示
     */
    private void showTipDialog() {
        mDialogBuilder.showChoiceTwoBtnDialog(mContext, "确定移出跑团?", "成员移出跑团后将在成员列表中消失", "取消",
                "确定");
        mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
            @Override
            public void onLeftBtnClick() {

            }

            @Override
            public void onRightBtnClick() {
                removeMember();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 移除团成员
     */
    private void removeMember() {
        mDialogBuilder.showProgressDialog(mContext, "正在删除...", false);

        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GROUP_REMOVE_MEMBER;
        RequestParams params = RequestParamsBuild.buildRemoveGroupMemberRequest(mContext, mGroupInfor.getRungroup().getId(),
                mUserId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "移除成功。");
                finish();
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
            }

            @Override
            protected void onFinish() {
                if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }

    /**
     * 获取成员的信息
     */
    private void getMemberInfor() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_RUNGROUP_MEMBER_INFOR;
        RequestParams params = RequestParamsBuild.buildGetRunGroupMemberInfor(mContext, mUserId, mGroupInfor.getRungroup()
                .getId());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mMemberInfor = JSON.parseObject(Response, GetGroupMemberInforResult.class);
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_OK;
                    msg.obj = Response;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = s;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }
}
