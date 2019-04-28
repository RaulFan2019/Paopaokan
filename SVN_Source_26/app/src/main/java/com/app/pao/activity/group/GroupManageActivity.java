package com.app.pao.activity.group;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.ActivityStackManager;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupInfoResult;
import com.app.pao.entity.network.GetWxInviteResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.utils.T;
import com.app.pao.utils.wx.WxShareManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.TextView;

/**
 * Created by Raul on 2015/12/8.
 * 跑团管理界面
 */
@ContentView(R.layout.activity_group_manage)
public class GroupManageActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "GroupManageActivity";

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.ll_apply)
    private FrameLayout mApplyLl;//邀请好友布局
    @ViewInject(R.id.ll_qrcode)
    private FrameLayout mQrcodeLl;//跑团二维码布局
    @ViewInject(R.id.ll_add_manager)
    private FrameLayout mAddManagerLl;//增加管理员布局
    @ViewInject(R.id.ll_exit)
    private FrameLayout mExitLl;//退出跑团布局
    @ViewInject(R.id.ll_tag_manager)
    private FrameLayout mTagLl;//团标签管理布局
    @ViewInject(R.id.tv_exit)
    private TextView mExitTv;//退出跑团文本


    /* local data */
    private GetGroupInfoResult mGroupInfo;//跑团详情

    private WxShareManager share;

    /**
     * Toolbar 点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * on Click
     *
     * @param v
     */
    @Override
    @OnClick({R.id.frame_apply, R.id.frame_qrcode, R.id.frame_add_manager,
            R.id.frame_share, R.id.ll_exit, R.id.frame_tag_manager})
    public void onClick(View v) {
        switch (v.getId()) {
            //邀请好友
            case R.id.frame_apply:
                launchApplyFriend();
                break;
            //添加管理员
            case R.id.frame_add_manager:
                launchAddManager();
                break;
            //跑团二维码
            case R.id.frame_qrcode:
                launchQrcode();
                break;
            //跑团分享
            case R.id.frame_share:
                shareGroup();
                break;
            //退出,解散跑团
            case R.id.ll_exit:
                exitGroup();
                break;
            case R.id.frame_tag_manager:
                launchGroupTag();
                break;
        }
    }


    @Override
    protected void initData() {
        mGroupInfo = (GetGroupInfoResult) getIntent().getExtras().getSerializable("group");

        share = new WxShareManager(GroupManageActivity.this);
    }

    @Override
    protected void initViews() {
        ActivityStackManager.getAppManager().addTempActivity(this);
        setSupportActionBar(mToolbar);
        //团长
        if (mGroupInfo.getRungroup().getRole() != AppEnum.groupRole.OWNER) {
            mAddManagerLl.setVisibility(View.GONE);
            mExitTv.setText("退出跑团");
        } else {
            mExitTv.setText("解散跑团");
        }
        //团管理或团长
        if (mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.OWNER
                || mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.MANAGER) {
            mTagLl.setVisibility(View.VISIBLE);
        } else {
            mTagLl.setVisibility(View.GONE);
        }
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
        ActivityStackManager.getAppManager().finishTempActivity(this);
    }


    /**
     * 邀请好友入团列表
     */
    private void launchApplyFriend() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("group", mGroupInfo);
        startActivity(GroupApplyFriendListActivity.class, bundle);
    }

    /**
     * 启动添加管理员
     */
    private void launchAddManager() {
        Bundle bundle = new Bundle();
        bundle.putInt("groupid", mGroupInfo.getRungroup().getId());
        startActivity(GroupAddMangerActivity.class, bundle);
    }

    /**
     * 启动跑团二维码页面
     */
    private void launchQrcode() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("group", mGroupInfo);
        startActivity(GroupQrCodeActivity.class, bundle);
    }

    /**
     * 解散. 退出跑团
     */
    private void exitGroup() {
        //团长解散跑团
        if (mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.OWNER) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("group", mGroupInfo);
            startActivity(DismissGroupActivity.class, bundle);
            //非团长离开跑团
        } else {
            showExitGroupDialog();
        }
    }


    /**
     * 显示退出跑团的对话框
     */
    private void showExitGroupDialog() {
        mDialogBuilder.showChoiceTwoBtnDialog(mContext, "提醒", "是否退出跑团", "取消", "退出");
        mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
            @Override
            public void onLeftBtnClick() {

            }

            @Override
            public void onRightBtnClick() {
                postExitRequest();
            }

            @Override
            public void onCancel() {}
        });
    }

    /**
     * 发送退出跑团的请求
     */
    private void postExitRequest() {
        mDialogBuilder.showProgressDialog(mContext, "正在请求退出跑团..", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GROUP_REMOVE_MEMBER;
        RequestParams params = RequestParamsBuild.buildRemoveGroupMemberRequest(mContext,mGroupInfo.getRungroup().getId(),
                LocalApplication.getInstance().getLoginUser(mContext).getUserId());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);

            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "您已退出跑团");
                ActivityStackManager.getAppManager().finishAllTempActivity();
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
            }

            @Override
            protected void onFinish() {
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }

    /**
     * 分享跑团
     */
    private void shareGroup() {
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }
        mDialogBuilder.showProgressDialog(mContext, "正在获取分享信息..", false);
        // 发送好友请求
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_WEIXIN_SHARE_TEXT;
        final RequestParams params = RequestParamsBuild.BuildGetGroupWxInviteRequest(mContext,mGroupInfo.getRungroup().getId());
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                GetWxInviteResult result = JSON.parseObject(Response, GetWxInviteResult.class);
                if (result.getTitle() != null && !result.getTitle().isEmpty()) {

                    share.startWxShareUrl(result.getText(), result.getTitle(), result.getLink());
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }


    /**
     * 启动团标签管理页面
     */
    private void launchGroupTag() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("group", mGroupInfo);
        startActivity(GroupTagManagerActivity.class,bundle);
//        bundle.putInt("groupId", mGroupInfo.getRungroup().getId());
//        startActivity(GroupMemberTagListActivity.class,bundle);
//        startActivity(GroupCreateTagActivity.class,bundle);
    }

}
