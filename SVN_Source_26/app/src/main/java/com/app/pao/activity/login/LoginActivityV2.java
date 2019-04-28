package com.app.pao.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseActivityV3;
import com.app.pao.activity.main.MainActivityV2;
import com.app.pao.activity.main.RaceWebViewActivity;
import com.app.pao.activity.main.TouristMainActivity;
import com.app.pao.activity.regist.RegisterActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.config.WXConfig;
import com.app.pao.data.PreferencesData;
import com.app.pao.data.db.UserData;
import com.app.pao.entity.network.GetCommonLoginResult;
import com.app.pao.entity.network.REBase;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.utils.HttpExceptionHelper;
import com.app.pao.network.utils.RequestParamsBuilder;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.common.EditTextPhone;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.StringUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.business.LoginUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Raul.Fan on 2016/9/6.
 */
public class LoginActivityV2 extends BaseActivityV3 {


    /* contains */
    private static final int MSG_LOGIN_OK = 1;//登录成功
    private static final int MSG_ERROR = 2;//网络请求错误
    private static final int MSG_WX_LOGIN_OK = 3;//微信登录成功


    /* local view */
    @BindView(R.id.et_login_phone_num)
    EditTextPhone mPhoneNumEt;
    @BindView(R.id.et_login_password)
    EditText mPwdEt;
    @BindView(R.id.ll_second_login_view)
    LinearLayout mSecondLoginViewLl;
    @BindView(R.id.ll_first_login_view)
    LinearLayout mFirstLoginViewLl;

    private MyDialogBuilderV1 mDialogBuilder;

    /* local data */
    private boolean mPostAble = true;//是否可以发送请求
    private String mCountryIso;//国际号

    private boolean isFirst;
    private int startAd;//用于判断是否开启广告页面
    private String adH5Url;//广告H5页面的url；

    private String mLoginPhone;
    private String mLoginPwd;

    private Callback.Cancelable mCancelable;//网络交互请求
    private UMSocialService mLoginController;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //请求错误
                case MSG_ERROR:
                    mPostAble = true;
                    mDialogBuilder.progressDialog.dismiss();
                    T.showLong(LoginActivityV2.this, (String) msg.obj);
                    break;
                case MSG_LOGIN_OK:
                    mDialogBuilder.progressDialog.dismiss();
                    startActivity(MainActivityV2.class);
                    break;
                case MSG_WX_LOGIN_OK:
                    mPostAble = true;
                    mDialogBuilder.progressDialog.dismiss();
                    doWxLogin((String) msg.obj);
                    break;
            }
        }
    };

    @OnClick({R.id.btn_login_by_verificationcode, R.id.btn_login_common, R.id.btn_regist_next,
            R.id.btn_regist, R.id.btn_login_common_first, R.id.btn_register_common,
            R.id.btn_login_by_wxin, R.id.btn_login_by_guest})
    public void onClick(View view) {
        switch (view.getId()) {
            //通过忘记密码登录
            case R.id.btn_login_by_verificationcode:
                startActivity(ForgetPwdInputPhoneActivity.class);
                break;
            //点击普通登录按钮
            case R.id.btn_login_common:
                onLoginBtnBtnClick();
                break;
            //点击注册按钮
            case R.id.btn_regist_next:
                startActivity(RegisterActivity.class);
                break;
            //点击注册按钮
            case R.id.btn_regist:
                startActivity(RegisterActivity.class);
                break;
            //点击首次出现的登录按钮
            case R.id.btn_login_common_first:
                mFirstLoginViewLl.setVisibility(View.GONE);
                mSecondLoginViewLl.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_register_common:
                startActivity(RegisterActivity.class);
                break;
            case R.id.btn_login_by_wxin:
                loginByWeiXin();
                break;
            case R.id.btn_login_by_guest:
                loginByGuest();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

    @Override
    protected void initData() {
        mCheckNewData = false;
        mCountryIso = DeviceUtils.GetCountryIso(LoginActivityV2.this);
        isFirst = getIntent().getBooleanExtra("isFirst", true);
        startAd = getIntent().getIntExtra("startAd", 0);
        adH5Url = PreferencesData.getAdH5Url(getApplicationContext());
        mLoginController = UMServiceFactory.getUMSocialService("com.umeng.login");//初始化友盟
    }

    @Override
    protected void initViews() {
        mDialogBuilder = new MyDialogBuilderV1();

    }

    @Override
    protected void doMyCreate() {
        //查看是否需要启动广告
        if (startAd == 1) {
            if (adH5Url != null && !adH5Url.equals("")) {
                Bundle bundle = new Bundle();
                bundle.putString("url", adH5Url);
                startActivity(RaceWebViewActivity.class, bundle);
            }
        }
        //是否第一次启动
        if (isFirst) {
            mFirstLoginViewLl.setVisibility(View.VISIBLE);
            mSecondLoginViewLl.setVisibility(View.GONE);
        } else {
            mSecondLoginViewLl.setVisibility(View.VISIBLE);
            mFirstLoginViewLl.setVisibility(View.GONE);
        }
    }

    @Override
    protected void causeGC() {
        if (mCancelable != null) {
            mCancelable.cancel();
        }
        if (mHandler != null) {
            mHandler.removeMessages(MSG_ERROR);
            mHandler.removeMessages(MSG_LOGIN_OK);
            mHandler.removeMessages(MSG_WX_LOGIN_OK);
        }
    }


    /**
     * 点击登录按钮
     */
    private void onLoginBtnBtnClick() {
        //检查手机号码错误
        mLoginPhone = mPhoneNumEt.getText().toString().replace(" ", "");
        String error = StringUtils.checkPhoneNumInputError(LoginActivityV2.this, mLoginPhone, mCountryIso);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            T.showShort(LoginActivityV2.this, error);
            return;
        }
        //检查密码规范
        mLoginPwd = mPwdEt.getText().toString();
        error = StringUtils.checkPasswordInputError(LoginActivityV2.this, mLoginPwd);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            T.showShort(LoginActivityV2.this, error);
            return;
        }
        if (!mPostAble) {
            return;
        }
        mPostAble = false;
        mDialogBuilder.showProgressDialog(this, "发送登录请求中..", true);
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
                RequestParams params = RequestParamsBuilder.buildCommonLoginRP(LoginActivityV2.this,
                        URLConfig.URL_COMMON_LOGIN, mLoginPhone, mLoginPwd,
                        JPushInterface.getRegistrationID(getApplicationContext()), AppEnum.DEVICEOS);
                mCancelable = x.http().post(params, new Callback.CommonCallback<REBase>() {
                    @Override
                    public void onSuccess(REBase reBase) {
                        if (mHandler != null) {
                            if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                                GetCommonLoginResult entity = JSON.parseObject(reBase.result, GetCommonLoginResult.class);
                                UserData.CommonLogin(LoginActivityV2.this, entity);
                                LoginUtils.CommonLogin(LoginActivityV2.this, entity, mLoginPwd);
                                LocalApplication.getInstance().setLoginUser(null);
                                mHandler.sendEmptyMessage(MSG_LOGIN_OK);
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

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });
    }


    /**
     * 通过游客登录
     */
    private void loginByGuest() {
        //保存用户信息到本地
        GetCommonLoginResult entity = new GetCommonLoginResult(AppEnum.DEFAULT_USER_ID, "0", 50,
                50, 25, "", "游客", "", 1, "", null, "", "", "", "", "", "", 1, "", 0);
        LoginUtils.CommonLogin(LoginActivityV2.this, entity, "");
        LocalApplication.getInstance().setLoginUser(null);
        startActivity(TouristMainActivity.class);
    }


    /**
     * 通过微信登录
     */
    private void loginByWeiXin() {
        UMWXHandler wxHandler = new UMWXHandler(LoginActivityV2.this, WXConfig.APP_ID, WXConfig.APP_SECRET);
        wxHandler.addToSocialSDK();
        if (wxHandler.isClientInstalled()) {
            mDialogBuilder.showProgressDialog(LoginActivityV2.this, "正在跳转微信", false);
            mLoginController.doOauthVerify(LoginActivityV2.this, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMAuthListener() {
                @Override
                public void onStart(SHARE_MEDIA platform) {
                    T.showShort(LoginActivityV2.this, "授权开始");

                }

                @Override
                public void onError(SocializeException e, SHARE_MEDIA platform) {
                    T.showShort(LoginActivityV2.this, "授权错误");
                    onFinish();
                }

                @Override
                public void onComplete(Bundle value, SHARE_MEDIA platform) {
                    // 获取相关授权信息
                    String uid = value.getString("uid");
                    if (!TextUtils.isEmpty(uid)) {
                        getWxUserInfo();
                    } else {

                    }
                    onFinish();
                }

                @Override
                public void onCancel(SHARE_MEDIA platform) {
                    T.showShort(LoginActivityV2.this, "授权取消");
                    onFinish();
                }

                /**
                 * 结束后
                 */
                public void onFinish() {
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }
                }
            });
        } else {
            T.showShort(LoginActivityV2.this, "请按装微信");
        }
    }

    /**
     * 获取微信返回用户值
     */
    private void getWxUserInfo() {
        mLoginController.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMDataListener() {
            @Override
            public void onStart() {
                //       Toast.makeText(MainActivityV2.this, "获取平台数据开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(int status, Map<String, Object> info) {
                if (status == 200 && info != null) {
                    // 开始与服务器通信进行登录
                    showWxLoginRequestDialog(info);
                } else {
                    if (mHandler != null) {
                        Message msg = new Message();
                        msg.what = MSG_ERROR;
                        msg.obj = "微信授权失败";
                        mHandler.sendMessage(msg);
                    }
                }
            }
        });
    }

    /**
     * 显示微信授权成功，开始进行APP登录DIALOG
     */
    private void showWxLoginRequestDialog(final Map<String, Object> wxInfor) {
        //若请求未结束
        if (!mPostAble) {
            return;
        }
        Intent intent = new Intent();
        intent.setClassName("包名", "类名");
        try {
            mDialogBuilder.showProgressDialog(LoginActivityV2.this, "开始登录···", true);
        } catch (WindowManager.BadTokenException exception) {
            return;
        }
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

        mPostAble = false;
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestParamsBuilder.buildWeiXinLoginRequest(LoginActivityV2.this, URLConfig.URL_WEIXIN_LOGIN,
                        wxInfor.get("unionid").toString(), wxInfor.get("nickname").toString(), wxInfor.get("sex").toString(),
                        wxInfor.get("headimgurl").toString(), wxInfor.get("province").toString(), wxInfor.get("city").toString());
                mCancelable = x.http().post(params, new Callback.CommonCallback<REBase>() {
                    @Override
                    public void onSuccess(REBase reBase) {
                        if (mHandler != null) {
                            if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                                Message mPostMsg = new Message();
                                mPostMsg.what = MSG_WX_LOGIN_OK;
                                mPostMsg.obj = reBase.result;
                                mHandler.sendMessage(mPostMsg);
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

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });
    }

    /**
     * 准备微信登录
     *
     * @param result
     */
    private void doWxLogin(final String result) {
        GetCommonLoginResult entity = JSON.parseObject(result, GetCommonLoginResult.class);

        //若帐号绑定了手机号
        if (entity.mobile != null
                && !entity.mobile.isEmpty()) {
            LoginUtils.CommonLogin(LoginActivityV2.this, entity, "");
            LocalApplication.getInstance().setLoginUser(null);
            startActivity(MainActivityV2.class);
            return;
        }
        Bundle b = new Bundle();
        b.putSerializable("WxLoginEntity", entity);
        startActivity(LoginByWxBindMobileActivity.class, b);
    }
}
