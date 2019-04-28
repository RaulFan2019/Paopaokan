package com.app.pao.activity.settings;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
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
 * Created by Administrator on 2015/12/9.
 */
@ContentView(R.layout.activity_reset_psw)
public class ResetPswActivity extends BaseAppCompActivity implements View.OnClickListener {

    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;

    @ViewInject(R.id.et_new_password)
    private EditText mNewPswEt;
    @ViewInject(R.id.et_confirm_password)
    private EditText mConfirmPswEt;

    private boolean mPostAble;//是否可以发送请求

    /**
     * 请求Handler
     */
    Handler mPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_POST_OK:
                    mDialogBuilder.progressDialog.dismiss();
                    mPostAble = true;
                    T.showShort(mContext, "修改成功！");
                    finish();
                    break;
                case MSG_POST_ERROR:
                    mDialogBuilder.progressDialog.dismiss();
                    T.showShort(mContext, "请求失败");
                    mPostAble = true;
                    break;
            }
        }
    };

    @Override
    @OnClick({R.id.tv_reset_psw, R.id.title_bar_left_menu})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reset_psw:
                postResetPwdRequest();
                break;

            case R.id.title_bar_left_menu:
                finish();
                break;
        }
    }

    @Override
    protected void initData() {
        mPostAble = true;
    }

    @Override
    protected void initViews() {

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
        cancelPostHandler();
    }

    /**
     * 发送修改密码请求
     */
    private void postResetPwdRequest() {
        if (!mPostAble) {
            return;
        }
        //判断新密码
        String newPwd = mNewPswEt.getText().toString();
        String newError = StringUtils.checkPasswordInputError(mContext, newPwd);
        if (!newError.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            T.showShort(mContext, newError);
            mNewPswEt.setError(newError);
            return;
        }
        //判断确认密码
        String confirmPwd = mConfirmPswEt.getText().toString();
        String confirmError = StringUtils.checkPasswordInputError(mContext, confirmPwd);
        if (!confirmError.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            T.showShort(mContext, confirmError);
            mConfirmPswEt.setError(confirmError);
            return;
        }
        //判断两次密码是否一致
        if (!newPwd.equals(confirmPwd)) {
            T.showShort(mContext, "两次密码不一致");
            mConfirmPswEt.setError("两次密码不一致");
            return;
        }
        //发送验证码请求提示
        mDialogBuilder.showProgressDialog(this, "正在请求修改密码..", true);
        mDialogBuilder.setListener(new MyDialogBuilderV1.ProgressDilaogListener() {
            @Override
            public void onCancelBtnClick() {
                mPostAble = true;
                cancelPostHandler();
            }

            @Override
            public void onCancel() {
                mPostAble = true;
                cancelPostHandler();
            }
        });
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPDATE_USER_INFO;
        RequestParams params = RequestParamsBuild.buildResetUserPasswordRequest(mContext,newPwd);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_OK;
                    mPostMsg.obj = Response;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = s;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFinish() {
            }
        });
    }

    /**
     * 取消请求
     */
    private void cancelPostHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_ERROR);
            mPostHandler.removeMessages(MSG_POST_OK);
        }
    }
}
