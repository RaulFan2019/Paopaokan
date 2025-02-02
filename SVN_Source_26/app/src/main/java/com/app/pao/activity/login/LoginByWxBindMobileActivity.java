package com.app.pao.activity.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.app.pao.ActivityStackManager;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseActivityV3;
import com.app.pao.activity.main.MainActivityV2;
import com.app.pao.activity.regist.RegisterEndActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetCommonLoginResult;
import com.app.pao.entity.network.GetPhoneNumIsRegistResult;
import com.app.pao.entity.network.REBase;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.HttpExceptionHelper;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.network.utils.RequestParamsBuilder;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.common.EditTextPhone;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.StringUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.business.LoginUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.client.HttpRequest;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Raul.Fan on 2016/9/7.
 * 通过微信登录，绑定手机号
 */
public class LoginByWxBindMobileActivity extends BaseActivityV3 {


    /* contains */
    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;
    private static final int MSG_GET_VCODE_OK = 2;


    @BindView(R.id.tb_bind_phone)
    Toolbar mToolbar;
    @BindView(R.id.et_bind_phone_num)
    EditTextPhone mPhoneEt;
    @BindView(R.id.et_bind_sms_code)
    EditText mVCodeEt;
    @BindView(R.id.btn_get_bind_sms_code)
    Button mBindVCodeBtn;

    private MyDialogBuilderV1 mDialogBuilder;

    /* local data */
    private GetCommonLoginResult mLoginEntity;//微信授权 登录返回值

    private Callback.Cancelable mCancelable;
    private SendTimer mSendTime;// 发送短信倒计时

    private boolean isExist = false;
    private String mPhoneNum;//电话号码
    private String mSmsCode;//短信验证码
    private String mCountryIso;//国际号

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wx_bind_mobile;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //请求失败
                case MSG_POST_ERROR:
                    mDialogBuilder.progressDialog.dismiss();
                    T.showLong(LoginByWxBindMobileActivity.this, msg.obj.toString());
                    break;
                //获取验证码成功
                case MSG_GET_VCODE_OK:
                    mDialogBuilder.progressDialog.dismiss();
                    T.showLong(LoginByWxBindMobileActivity.this, "发送验证码请求成功");
                    startTimer();
                    break;
                //绑定成功
                case MSG_POST_OK:
                    mDialogBuilder.progressDialog.dismiss();
                    //设置手机号码
                    mLoginEntity.setMobile(mPhoneNum);
                    mLoginEntity.setName(mPhoneNum);
                    //将信息存储至本地
                    LoginUtils.CommonLogin(LoginByWxBindMobileActivity.this, mLoginEntity, "weixin");
                    LocalApplication.getInstance().setLoginUser(null);
                    if (isExist) {
                        startActivity(MainActivityV2.class);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", AppEnum.RegistType.WeixinRegist);
                        bundle.putSerializable("entity", mLoginEntity);
                        startActivity(RegisterEndActivity.class, bundle);
                    }
                    break;
            }
        }
    };

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


    @OnClick({R.id.btn_get_bind_sms_code, R.id.btn_bind_phone_num})
    public void onClick(View view) {
        switch (view.getId()) {
            //发送验证码
            case R.id.btn_get_bind_sms_code:
                onGetBingVCodeBtnClick();
                break;
            //点击绑定手机号
            case R.id.btn_bind_phone_num:
                onBindPhoneNumBtnClick();
                break;
        }
    }


    @Override
    protected void initData() {
        mCheckNewData = false;
        mLoginEntity = (GetCommonLoginResult) getIntent().getSerializableExtra("WxLoginEntity");
        mCountryIso = DeviceUtils.GetCountryIso(LoginByWxBindMobileActivity.this);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        mDialogBuilder = new MyDialogBuilderV1();
    }

    @Override
    protected void doMyCreate() {
        ActivityStackManager.getAppManager().addTempActivity(this);
    }

    @Override
    protected void causeGC() {
        if (mCancelable != null) {
            mCancelable.cancel();
        }
        ActivityStackManager.getAppManager().finishTempActivity(this);
    }

    /**
     * 点击获取验证码按钮
     */
    private void onGetBingVCodeBtnClick() {
        //检查手机号码
        mPhoneNum = mPhoneEt.getText().toString().replace(" ", "");
        String error = StringUtils.checkPhoneNumInputError(LoginByWxBindMobileActivity.this, mPhoneNum, mCountryIso);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            mPhoneEt.setError(error);
            T.showShort(LoginByWxBindMobileActivity.this, error);
            return;
        }

        //开始验证手机
        mDialogBuilder.showProgressDialog(this, "发送注册请求中..", true);
        mDialogBuilder.setListener(new MyDialogBuilderV1.ProgressDilaogListener() {
            @Override
            public void onCancelBtnClick() {
                if (mCancelable != null) {
                    mCancelable.cancel();
                }
            }

            @Override
            public void onCancel() {
                if (mCancelable != null) {
                    mCancelable.cancel();
                }
            }
        });
        //发送验证请求
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams requestParams = RequestParamsBuilder.buildCheckPhoneRP(LoginByWxBindMobileActivity.this,
                        URLConfig.URL_CHECK_PHONE_IS_REGIST, mPhoneNum);
                mCancelable = x.http().post(requestParams, new Callback.CommonCallback<REBase>() {
                    @Override
                    public void onSuccess(REBase reBase) {
                        if (mHandler != null) {
                            if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                                GetPhoneNumIsRegistResult resultEntity = JSON.parseObject(reBase.result, GetPhoneNumIsRegistResult.class);
                                //若手机未注册才可以
                                if (resultEntity.isBound == AppEnum.UserBind.BIND) {
                                    if (mHandler != null) {
                                        Message msg = new Message();
                                        msg.what = MSG_POST_ERROR;
                                        msg.obj = "该手机号已被绑定";
                                        mHandler.sendMessage(msg);
                                    }
                                    return;
                                }
                                //若手机未注册才可以
                                if (resultEntity.isExist == AppEnum.UserRegist.NOT_EXIST) {
                                    isExist = false;
                                } else {
                                    isExist = true;
                                }
                                postGetVCode();
                            } else {
                                Message mPostMsg = new Message();
                                mPostMsg.what = MSG_POST_ERROR;
                                mPostMsg.obj = reBase.errormsg;
                                mHandler.sendMessage(mPostMsg);
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable throwable, boolean b) {
                        if (mHandler != null) {
                            Message mPostMsg = new Message();
                            mPostMsg.what = MSG_POST_ERROR;
                            mPostMsg.obj = HttpExceptionHelper.getErrorMsg(throwable);
                            mHandler.sendMessage(mPostMsg);
                        }
                    }

                    @Override
                    public void onCancelled(CancelledException e) {
                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });
    }

    /**
     * 获取验证码请求
     */
    private void postGetVCode() {
        RequestParams requestParams = RequestParamsBuilder.buildGetVCodeRP(LoginByWxBindMobileActivity.this,
                URLConfig.URL_GET_VC_CODE, mPhoneNum);
        mCancelable = x.http().post(requestParams, new Callback.CommonCallback<REBase>() {
            @Override
            public void onSuccess(REBase reBase) {
                if (mHandler != null) {
                    if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                        mHandler.sendEmptyMessage(MSG_GET_VCODE_OK);
                    } else {
                        Message mPostMsg = new Message();
                        mPostMsg.what = MSG_POST_ERROR;
                        mPostMsg.obj = reBase.errormsg;
                        mHandler.sendMessage(mPostMsg);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (mHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = HttpExceptionHelper.getErrorMsg(throwable);
                    mHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            public void onCancelled(CancelledException e) {
                mSendTime.cancel();
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 点击绑定按钮
     */
    private void onBindPhoneNumBtnClick() {
        //检查手机号码
        mPhoneNum = mPhoneEt.getText().toString().replace(" ", "");
        String error = StringUtils.checkPhoneNumInputError(LoginByWxBindMobileActivity.this, mPhoneNum, mCountryIso);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            T.showShort(LoginByWxBindMobileActivity.this, error);
            return;
        }
        //检查验证码
        mSmsCode = mVCodeEt.getText().toString();
        error = StringUtils.checkVCCodeError(LoginByWxBindMobileActivity.this, mSmsCode);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            T.showShort(LoginByWxBindMobileActivity.this, error);
            return;
        }
        mDialogBuilder.showProgressDialog(LoginByWxBindMobileActivity.this, "绑定中...", false);
        mDialogBuilder.setListener(new MyDialogBuilderV1.ProgressDilaogListener() {
            @Override
            public void onCancelBtnClick() {
                if (mCancelable != null) {
                    mCancelable.cancel();
                }
            }

            @Override
            public void onCancel() {
                if (mCancelable != null) {
                    mCancelable.cancel();
                }
            }
        });
        x.task().post(new Runnable() {
            @Override
            public void run() {
                final RequestParams params = RequestParamsBuilder.buildCheckVCodeRequest(LoginByWxBindMobileActivity.this, URLConfig.URL_CHECK_VC_CODE,
                        mPhoneNum, mSmsCode);
                x.task().post(new Runnable() {
                    @Override
                    public void run() {
                        mCancelable = x.http().post(params, new Callback.CommonCallback<REBase>() {
                            @Override
                            public void onSuccess(REBase reBase) {
                                if (mHandler != null) {
                                    if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                                        bindPhoneNum();
                                    } else {
                                        Message mPostMsg = new Message();
                                        mPostMsg.what = MSG_POST_ERROR;
                                        mPostMsg.obj = reBase.errormsg;
                                        mHandler.sendMessage(mPostMsg);
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable throwable, boolean b) {
                                if (mHandler != null) {
                                    Message msg = new Message();
                                    msg.what = MSG_POST_ERROR;
                                    msg.obj = HttpExceptionHelper.getErrorMsg(throwable);
                                    mHandler.sendMessage(msg);
                                }
                            }

                            @Override
                            public void onCancelled(CancelledException e) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 绑定手机号码
     */
    private void bindPhoneNum() {
        RequestParams params = RequestParamsBuilder.buildWeiXinBingMobileRP(URLConfig.URL_UPDATE_USER_INFO, mPhoneNum, mLoginEntity.id);
        x.http().post(params, new Callback.CommonCallback<REBase>() {
            @Override
            public void onSuccess(REBase reBase) {
                if (mHandler != null) {
                    if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                        mHandler.sendEmptyMessage(MSG_POST_OK);
                    } else {
                        Message mPostMsg = new Message();
                        mPostMsg.what = MSG_POST_ERROR;
                        mPostMsg.obj = reBase.errormsg;
                        mHandler.sendMessage(mPostMsg);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (mHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = HttpExceptionHelper.getErrorMsg(throwable);
                    mHandler.sendMessage(msg);
                }
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    // 发送倒计时
    class SendTimer extends CountDownTimer {

        public SendTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mBindVCodeBtn.setText(millisUntilFinished / 1000 + "");
        }

        @Override
        public void onFinish() {
            mBindVCodeBtn.setEnabled(true);
            mBindVCodeBtn.setText(getString(R.string.Button_WxBindMobileActivity_getCode));
            mBindVCodeBtn.setBackgroundResource(R.drawable.bg_round_rect);
        }

    }

    //开始倒计时
    private void startTimer() {
        mSendTime = new SendTimer(60000, 1000);
        mSendTime.start();
        mBindVCodeBtn.setEnabled(false);
        mBindVCodeBtn.setBackgroundResource(R.drawable.bg_round_rect_disable);
    }
}
