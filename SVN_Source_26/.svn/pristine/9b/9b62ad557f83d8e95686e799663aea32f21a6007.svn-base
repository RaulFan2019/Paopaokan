package com.app.pao.activity.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.activity.group.GroupInfoActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetProcessInviteCodeResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.StringUtils;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Raul on 2015/12/3.
 * 输入邀请码界面
 */
@ContentView(R.layout.activity_input_invite)
public class InputInviteActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */

    private static final int MSG_UPDATE_VIEW = 0;//更新页面
    private static final int MSG_UPDATE_ERROR = 1;//更新失败

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.et_invite)
    private EditText mEt;//邀请码输入框

    /* local data */
    private String invite;//邀请码

    private boolean mPostAble;
    private GetProcessInviteCodeResult result;//处理结果

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
     * 更新页面Handler
     */
    Handler updateViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_UPDATE_VIEW) {
                mPostAble = true;
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
                processInvite();
            } else if (msg.what == MSG_UPDATE_ERROR) {
                mPostAble = true;
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }

            }
        }
    };


    /**
     * on Click
     *
     * @param v
     */
    @Override
    @OnClick({R.id.btn_invite})
    public void onClick(View v) {
        if (v.getId() == R.id.btn_invite) {
            checkInvite();
        }
    }

    @Override
    protected void initData() {
        mPostAble = true;
        if (getIntent().hasExtra("invite")) {
            invite = getIntent().getStringExtra("invite");
            mEt.setText(invite);
        }
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
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
        cancelHandler();
    }


    /**
     * 检查邀请码
     */
    private void checkInvite() {
        invite = mEt.getText().toString();
        String error = StringUtils.checkInvite(mContext, invite);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            mEt.setError(error);
            return;
        } else {
            postInvite(invite);
        }
    }

    /**
     * 请求邀请码作用
     */
    private void postInvite(String invite) {
        mDialogBuilder.showProgressDialog(mContext, "正在验证邀请码..", true);

        mPostAble = false;
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_PROCESS_INVITE_CODE;
        RequestParams params = RequestParamsBuild.buildProcessInviteCodeRequest(mContext,invite);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
                if (updateViewHandler != null) {
                    updateViewHandler.sendEmptyMessage(MSG_UPDATE_ERROR);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                result = JSON.parseObject(Response, GetProcessInviteCodeResult.class);
                if (updateViewHandler != null) {
                    updateViewHandler.sendEmptyMessage(MSG_UPDATE_VIEW);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
                if (updateViewHandler != null) {
                    updateViewHandler.sendEmptyMessage(MSG_UPDATE_ERROR);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 处理邀请码
     */
    private void processInvite() {
        T.showLong(mContext, result.getResultmessage());
        //跳转到好友页面
        if (result.getType() == AppEnum.InviteResultType.FRIEND) {
            Bundle bundle = new Bundle();
            bundle.putInt("userid", result.getData());
            startActivity(UserInfoActivity.class,bundle);
            //跳转到团页面
        } else if (result.getType() == AppEnum.InviteResultType.GROUP) {
            Bundle bundle = new Bundle();
            bundle.putInt("groupid", result.getData());
            startActivity(GroupInfoActivity.class, bundle);
        }
    }


    /**
     * 销毁Handler
     */
    private void cancelHandler() {
        if (updateViewHandler != null) {
            updateViewHandler.removeMessages(MSG_UPDATE_ERROR);
            updateViewHandler.removeMessages(MSG_UPDATE_VIEW);
            updateViewHandler = null;
        }
    }
}
