package com.app.pao.activity.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

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
 * Created by Raul.Fan on 2016/9/6.
 * 通过忘记密码登录，输入手机号页面
 */
public class ForgetPwdInputPhoneActivity extends BaseActivityV3 {


    private static final int MSG_POST_ERROR = 1;
    private static final int MSG_POST_OK = 2;

    /* local view */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_login_byvc_phone_num)
    EditTextPhone mPhoneEt;

    private MyDialogBuilderV1 mDialogBuilder;

    /* local data */
    private boolean mPostAble = true;//是否可以发送请求
    private Callback.Cancelable mCancelable;

    private String mCountryIso;//国际号
    private String mPhoneNum;//手机号码

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_psw_input_phonenum;
    }

    /**
     * 请求返回处理
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //发送请求错误
                case MSG_POST_ERROR:
                    T.showShort(ForgetPwdInputPhoneActivity.this, (String) msg.obj);
                    mDialogBuilder.progressDialog.dismiss();
                    mPostAble = true;
                    break;
                //发送请求成功,跳转到验证码输入界面
                case MSG_POST_OK:
                    mDialogBuilder.progressDialog.dismiss();
                    mPostAble = true;
                    Bundle bundle = new Bundle();
                    bundle.putString("phonenum", mPhoneNum);
                    startActivity(ForgetPwdInputVCodeActivity.class, bundle);
                    break;
            }
        }
    };

    /**
     * 点击事件
     */
    @OnClick(R.id.btn_next)
    public void onClick() {
        onNextBtnClick();
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

    @Override
    protected void initData() {
        mCheckNewData = false;
        mCountryIso = DeviceUtils.GetCountryIso(this);
    }

    @Override
    protected void initViews() {
        mDialogBuilder = new MyDialogBuilderV1();
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void doMyCreate() {
        ActivityStackManager.getAppManager().addTempActivity(this);
    }

    @Override
    protected void causeGC() {
        ActivityStackManager.getAppManager().finishTempActivity(this);
        if (mHandler != null) {
            mHandler.removeMessages(MSG_POST_OK);
            mHandler.removeMessages(MSG_POST_ERROR);
        }
    }

    /**
     * 点击下一步
     */
    private void onNextBtnClick() {
        mPhoneNum = mPhoneEt.getText().toString().replace(" ", "");
        String error = StringUtils.checkPhoneNumInputError(ForgetPwdInputPhoneActivity.this, mPhoneNum, mCountryIso);
        //验证手机信息
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            T.showShort(ForgetPwdInputPhoneActivity.this, error);
            return;
        }
        if (!mPostAble) {
            return;
        }
        mPostAble = false;
        mDialogBuilder.showChoiceTwoBtnDialog(ForgetPwdInputPhoneActivity.this
                , "提示", "是否向手机号码:" + mPhoneNum + "发送验证码信息?", "取消",
                "发送");
        mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
            @Override
            public void onLeftBtnClick() {
                mPostAble = true;
            }

            @Override
            public void onRightBtnClick() {
                postCheckPhone();
            }

            @Override
            public void onCancel() {
                mPostAble = true;
            }
        });
    }

    /**
     * 发送验证手机号码
     */
    private void postCheckPhone() {
        //发送验证码请求提示
        mDialogBuilder.showProgressDialog(this, "正在请求验证码..", true);
        mDialogBuilder.setListener(new MyDialogBuilderV1.ProgressDilaogListener() {
            @Override
            public void onCancelBtnClick() {
                mPostAble = true;
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
                RequestParams params = RequestParamsBuilder.buildCheckPhoneRP(ForgetPwdInputPhoneActivity.this,
                        URLConfig.URL_CHECK_PHONE_IS_REGIST, mPhoneNum);
                mCancelable = x.http().post(params, new Callback.CommonCallback<REBase>() {
                    @Override
                    public void onSuccess(REBase reBase) {
                        if (mHandler != null) {
                            if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                                GetPhoneNumIsRegistResult resultEntity = JSON.parseObject(reBase.result, GetPhoneNumIsRegistResult.class);
                                if (resultEntity.isExist == AppEnum.UserRegist.EXIST) {
                                    postGetVCode();
                                } else {
                                    if (mHandler != null) {
                                        Message mPostMsg = new Message();
                                        mPostMsg.what = MSG_POST_ERROR;
                                        mPostMsg.obj = ForgetPwdInputPhoneActivity.this.getResources().getString(R.string.Error_Check_Login_NotExist);
                                        mHandler.sendMessage(mPostMsg);
                                    }
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
        RequestParams requestParams = RequestParamsBuilder.buildGetVCodeRP(ForgetPwdInputPhoneActivity.this,
                URLConfig.URL_GET_VC_CODE, mPhoneNum);
        mCancelable = x.http().post(requestParams, new Callback.CommonCallback<REBase>() {
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


}
