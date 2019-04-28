package com.app.pao.activity.login;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.app.pao.ActivityStackManager;
import com.app.pao.R;
import com.app.pao.activity.BaseActivityV3;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetCommonLoginResult;
import com.app.pao.entity.network.REBase;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.utils.HttpExceptionHelper;
import com.app.pao.network.utils.RequestParamsBuilder;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.common.EditTextPhone;
import com.app.pao.utils.Log;
import com.app.pao.utils.StringUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.business.LoginUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Raul.Fan on 2016/9/7.
 * 忘记密码登录，填写验证码
 */
public class ForgetPwdInputVCodeActivity extends BaseActivityV3 {


    private static final int MSG_ERROR = 1;
    private static final int MSG_GET_CODE_OK = 2;//重新发送验证码请求
    private static final int MSG_COMMIT_OK = 3;//验证码验证成功


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_vc_inputcode_phone_num)
    EditTextPhone mPhoneEt;
    @BindView(R.id.et_vc_inputcode_phone_code)
    EditText mVCodeEt;

    private MyDialogBuilderV1 mDialogBuilder;

    /* local data */
    private boolean mPostAble = true;//是否可以发送请求
    private String mPhoneNum;//手机号码
    private String mVCode;//验证码

    private Callback.Cancelable mCancelable;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_psw_input_code;
    }

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
     * 请求返回处理
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //网络请求失败
                case MSG_ERROR:
                    T.showShort(ForgetPwdInputVCodeActivity.this, (String) msg.obj);
                    mDialogBuilder.progressDialog.dismiss();
                    mPostAble = true;
                    break;
                //验证码验证成功
                case MSG_COMMIT_OK:
                    mDialogBuilder.progressDialog.dismiss();
                    mPostAble = true;
                    Bundle bundle = new Bundle();
                    bundle.putString("phonenum", mPhoneNum);
                    startActivity(ForgetPwdResetActivity.class, bundle);
                    break;
                //获取验证码请求成功
                case MSG_GET_CODE_OK:
                    mPostAble = true;
                    mDialogBuilder.progressDialog.dismiss();
                    T.showShort(ForgetPwdInputVCodeActivity.this, "获取验证码请求成功,请稍等");
                    break;
            }
        }
    };

    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.btn_commit, R.id.btn_get_code_again})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击提交
            case R.id.btn_commit:
                postCommitVCode();
                break;
            //再次获取验证码
            case R.id.btn_get_code_again:
                showGetVCodeDialog();
                break;
        }
    }

    @Override
    protected void initData() {
        mCheckNewData = false;
        mPhoneNum = getIntent().getExtras().getString("phonenum");
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        mDialogBuilder = new MyDialogBuilderV1();
        mPhoneEt.setText(mPhoneNum);
    }

    @Override
    protected void doMyCreate() {
        ActivityStackManager.getAppManager().addTempActivity(this);
    }

    @Override
    protected void causeGC() {
        ActivityStackManager.getAppManager().finishTempActivity(this);
        if (mHandler != null) {
            mHandler.removeMessages(MSG_COMMIT_OK);
            mHandler.removeMessages(MSG_ERROR);
            mHandler.removeMessages(MSG_GET_CODE_OK);
        }
        if (mCancelable != null) {
            mCancelable.cancel();
        }
    }

    /**
     * 提交验证码
     */
    private void postCommitVCode() {
        //检查验证码
        mVCode = mVCodeEt.getText().toString();
        String error = StringUtils.checkVCCodeError(this, mVCode);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            T.showShort(this, error);
            return;
        }
        if (!mPostAble) {
            return;
        }
        mPostAble = false;
        //发送验证码请求提示
        mDialogBuilder.showProgressDialog(this, "验证验证码..", true);
        mDialogBuilder.setListener(new MyDialogBuilderV1.ProgressDilaogListener() {
            @Override
            public void onCancelBtnClick() {
                mPostAble = true;
                if (mCancelable != null) {
                    mCancelable.cancel();
                }
            }

            @Override
            public void onCancel() {
                mPostAble = true;
                if (mCancelable != null) {
                    mCancelable.cancel();
                }
            }
        });

        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestParamsBuilder.buildLoginByVcCodeRequest(ForgetPwdInputVCodeActivity.this,
                        URLConfig.URL_LOGIN_BY_VCCODE, mPhoneNum, mVCode,
                        JPushInterface.getRegistrationID(ForgetPwdInputVCodeActivity.this), AppEnum.DEVICEOS);
                mCancelable = x.http().post(params, new Callback.CommonCallback<REBase>() {
                    @Override
                    public void onSuccess(REBase reBase) {
                        if (mHandler != null) {
                            if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                                GetCommonLoginResult entity = JSON.parseObject(reBase.result, GetCommonLoginResult.class);
                                LoginUtils.CommonLogin(ForgetPwdInputVCodeActivity.this, entity, mVCode);
                                mHandler.sendEmptyMessage(MSG_COMMIT_OK);
                            } else {
                                Message mPostMsg = new Message();
                                mPostMsg.what = MSG_ERROR;
                                mPostMsg.obj = reBase.errormsg;
                                mHandler.sendMessage(mPostMsg);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable, boolean b) {
                        if (mHandler != null) {
                            Message mPostMsg = new Message();
                            mPostMsg.what = MSG_ERROR;
                            mPostMsg.obj = HttpExceptionHelper.getErrorMsg(throwable);
                            mHandler.sendMessage(mPostMsg);
                        }
                    }

                    @Override
                    public void onCancelled(CancelledException e) {
                        mPostAble = true;
                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });
    }

    /**
     * 显示获取验证码对话框
     */
    private void showGetVCodeDialog() {
        mDialogBuilder.showChoiceTwoBtnDialog(ForgetPwdInputVCodeActivity.this, "验证码", "是否重新发送验证码?", "取消", "重发");
        mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
            @Override
            public void onLeftBtnClick() {

            }

            @Override
            public void onRightBtnClick() {
                postGetVCode();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 发送获取验证码请求
     */
    private void postGetVCode() {
        //发送验证码请求提示
        mDialogBuilder.showProgressDialog(this, "再次获取验证码..", true);
        mDialogBuilder.setListener(new MyDialogBuilderV1.ProgressDilaogListener() {
            @Override
            public void onCancelBtnClick() {
                mPostAble = true;
                if (mCancelable != null) {
                    mCancelable.cancel();
                }
            }

            @Override
            public void onCancel() {
                mPostAble = true;
                if (mCancelable != null) {
                    mCancelable.cancel();
                }
            }
        });

        RequestParams requestParams = RequestParamsBuilder.buildGetVCodeRP(ForgetPwdInputVCodeActivity.this,
                URLConfig.URL_GET_VC_CODE, mPhoneNum);
        mCancelable = x.http().post(requestParams, new Callback.CommonCallback<REBase>() {
            @Override
            public void onSuccess(REBase reBase) {
                if (mHandler != null) {
                    if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                        mHandler.sendEmptyMessage(MSG_GET_CODE_OK);
                    } else {
                        Message mPostMsg = new Message();
                        mPostMsg.what = MSG_ERROR;
                        mPostMsg.obj = reBase.errormsg;
                        mHandler.sendMessage(mPostMsg);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (mHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_ERROR;
                    mPostMsg.obj = HttpExceptionHelper.getErrorMsg(throwable);
                    mHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            public void onCancelled(CancelledException e) {
                mPostAble = true;
            }

            @Override
            public void onFinished() {

            }
        });
    }
}
