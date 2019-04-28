package com.app.pao.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseActivityV3;
import com.app.pao.activity.main.MainActivityV2;
import com.app.pao.activity.main.RaceWebViewActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.PreferencesData;
import com.app.pao.data.db.UserData;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.network.GetCommonLoginResult;
import com.app.pao.entity.network.REBase;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.utils.HttpExceptionHelper;
import com.app.pao.network.utils.RequestParamsBuilder;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.StringUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.business.LoginUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Raul.Fan on 2016/9/7.
 * 若用户登出，显示页面
 */
public class LoginChangeUserActivity extends BaseActivityV3 {


    private static final int MSG_LOGIN_OK = 1;
    private static final int MSG_ERROR = 2;

    /* local view */
    @BindView(R.id.iv_user_avatar)
    CircularImage mUserAvatarIv;
    @BindView(R.id.tv_user_name)
    TextView mUserNameTv;
    @BindView(R.id.et_login_psd)
    EditText mPwdEt;

    private MyDialogBuilderV1 mDialogBuilder;

    /* local data */
    private boolean mPostAble = true;
    private Callback.Cancelable mCancelable;

    private DBUserEntity mUser; //登录用户信息
    private String mLoginPwd;
    private int startAd;
    private String adH5Url;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_user_change;
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
                    T.showLong(LoginChangeUserActivity.this, (String) msg.obj);
                    break;
                case MSG_LOGIN_OK:
                    mDialogBuilder.progressDialog.dismiss();
                    startActivity(MainActivityV2.class);
                    finish();
                    break;
            }
        }
    };

    @OnClick({R.id.btn_user_login_common, R.id.btn_login_by_changeuser})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击登录按钮
            case R.id.btn_user_login_common:
                onLogonBtnClick();
                break;
            //点击切换账号按钮
            case R.id.btn_login_by_changeuser:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFirst", false);
                startActivity(LoginActivityV2.class, bundle);
                finish();
                break;
        }
    }

    @Override
    protected void initData() {
        mCheckNewData = false;
        mUser = LocalApplication.getInstance().getLoginUser(getApplicationContext());
        startAd = getIntent().getIntExtra("startAd", 0);
        adH5Url = PreferencesData.getAdH5Url(getApplicationContext());
    }

    @Override
    protected void initViews() {
        mDialogBuilder = new MyDialogBuilderV1();
        if (startAd == 1) {
            if (adH5Url != null && !adH5Url.equals("")) {
                Bundle bundle = new Bundle();
                bundle.putString("url", adH5Url);
                startActivity(RaceWebViewActivity.class, bundle);
            }
        }
        //加载用户头像
        ImageUtils.loadUserImage(mUser.getAvatar(), mUserAvatarIv);
        //设置用户电话号码
        mUserNameTv.setText(mUser.name);
    }

    @Override
    protected void doMyCreate() {
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

    @Override
    protected void causeGC() {
        if (mCancelable != null) {
            mCancelable.cancel();
        }
    }


    /**
     * 点击登录按钮
     */
    private void onLogonBtnClick() {
        //若请求未结束
        if (!mPostAble) {
            return;
        }
        //检查密码规范
        mLoginPwd = mPwdEt.getText().toString();
        String error = StringUtils.checkPasswordInputError(LoginChangeUserActivity.this, mLoginPwd);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            T.showShort(LoginChangeUserActivity.this, error);
            return;
        }
        //显示登录提示
        mDialogBuilder.showProgressDialog(this, "正在登录..", true);
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
                RequestParams params = RequestParamsBuilder.buildCommonLoginRP(LoginChangeUserActivity.this,
                        URLConfig.URL_COMMON_LOGIN, mUser.name, mLoginPwd,
                        JPushInterface.getRegistrationID(getApplicationContext()), AppEnum.DEVICEOS);
                mCancelable = x.http().post(params, new Callback.CommonCallback<REBase>() {
                    @Override
                    public void onSuccess(REBase reBase) {
                        if (mHandler != null) {
                            if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                                GetCommonLoginResult entity = JSON.parseObject(reBase.result, GetCommonLoginResult.class);
                                UserData.CommonLogin(LoginChangeUserActivity.this, entity);
                                LoginUtils.CommonLogin(LoginChangeUserActivity.this, entity, mLoginPwd);
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

}
