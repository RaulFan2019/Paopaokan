package com.app.pao.activity.regist;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.app.pao.ActivityStackManager;
import com.app.pao.R;
import com.app.pao.activity.BaseActivityV3;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetPhoneNumIsRegistResult;
import com.app.pao.entity.network.REBase;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.utils.HttpExceptionHelper;
import com.app.pao.network.utils.RequestParamsBuilder;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.common.EditTextPhone;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.StringUtils;
import com.app.pao.utils.T;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/5.
 */
public class RegisterActivity extends BaseActivityV3 {

    private static final int MSG_POST_ERROR = 1;//请求失败
    private static final int MSG_GET_VCODE_OK = 2;//请求成功

    /* local view */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_register_phone_num)
    EditTextPhone mPhoneEt;//手机输入框
    @BindView(R.id.btn_register)
    Button mRegisterBtn;

    private MyDialogBuilderV1 mDialogBuilder;

    /* local data */
    private String mCountryIso;//国际号
    private String mPhoneNum;//电话号码
    private boolean mPostAble = true;//是否可以发送请求

    private Callback.Cancelable mCancelable;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_regist;
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

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //请求失败
                case MSG_POST_ERROR:
                    T.showShort(RegisterActivity.this, (String) msg.obj);
                    mDialogBuilder.progressDialog.dismiss();
                    mPostAble = true;
                    break;
                case MSG_GET_VCODE_OK:
                    mDialogBuilder.progressDialog.dismiss();
                    mPostAble = true;
                    Bundle bundle = new Bundle();
                    bundle.putString("phonenum", mPhoneNum);
                    startActivity(RegisterInputVCodeActivity.class, bundle);
                    break;
            }
        }
    };

    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.btn_register, R.id.btn_privacy})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击注册按钮
            case R.id.btn_register:
                onRegisterClick();
                break;
            //点击隐私协议
            case R.id.btn_privacy:
                onPrivacyClick();
                break;
        }
    }


    @Override
    protected void initData() {
        mCheckNewData = false;
        mCountryIso = DeviceUtils.GetCountryIso(RegisterActivity.this);
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
        if (mHandler != null) {
            mHandler.removeMessages(MSG_GET_VCODE_OK);
            mHandler.removeMessages(MSG_POST_ERROR);
        }
        ActivityStackManager.getAppManager().finishTempActivity(this);
    }

    /**
     * 点击注册按钮
     */
    private void onRegisterClick() {
        mPhoneNum = mPhoneEt.getText().toString().replace(" ", "");
        String error = StringUtils.checkPhoneNumInputError(RegisterActivity.this, mPhoneNum, mCountryIso);
        //验证手机信息
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            T.showShort(RegisterActivity.this, error);
            return;
        }
        if (!mPostAble) {
            return;
        }
        mPostAble = false;
        //开始验证手机
        mDialogBuilder.showProgressDialog(this, "发送注册请求中..", true);
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
        //发送验证请求
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams requestParams = RequestParamsBuilder.buildCheckPhoneRP(RegisterActivity.this,
                        URLConfig.URL_CHECK_PHONE_IS_REGIST, mPhoneNum);
                mCancelable = x.http().post(requestParams, new Callback.CommonCallback<REBase>() {
                    @Override
                    public void onSuccess(REBase reBase) {
                        if (mHandler != null) {
                            if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                                GetPhoneNumIsRegistResult resultEntity = JSON.parseObject(reBase.result, GetPhoneNumIsRegistResult.class);
                                if (resultEntity.isExist == AppEnum.UserRegist.EXIST) {
                                    if (mHandler != null) {
                                        Message mPostMsg = new Message();
                                        mPostMsg.what = MSG_POST_ERROR;
                                        mPostMsg.obj = RegisterActivity.this.getResources().getString(R.string.Error_Check_Regist_Exist);
                                        mHandler.sendMessage(mPostMsg);
                                    }
                                } else {
                                    postGetVCode();
                                }
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
     * 获取验证码请求
     */
    private void postGetVCode() {
        RequestParams requestParams = RequestParamsBuilder.buildGetVCodeRP(RegisterActivity.this,
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
                mPostAble = true;
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 点击隐私协议
     */
    private void onPrivacyClick() {
        startActivity(PrivacyActivity.class);
    }

}
