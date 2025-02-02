package com.app.pao.activity.regist;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.app.pao.ActivityStackManager;
import com.app.pao.R;
import com.app.pao.activity.BaseActivityV3;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.REBase;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.utils.HttpExceptionHelper;
import com.app.pao.network.utils.RequestParamsBuilder;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.common.EditTextPhone;
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
 */
public class RegisterInputVCodeActivity extends BaseActivityV3 {

    /* contains */
    private static final int MSG_ERROR = 0x01;
    private static final int MSG_COMMIT_V_CODE_OK = 0x02;
    private static final int MSG_GET_V_CODE = 0x03;

    /* local view */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_regist_vc_inputcode_phonenum)
    EditTextPhone mPhoneNumEt;
    @BindView(R.id.et_regist_code)
    EditText mVCodeEt;

    private MyDialogBuilderV1 mDialogBuilder;

    /* local data */
    private boolean mPostAble = true;//是否可以发送请求
    private String mPhoneNum;//电话号码
    private String mVCode;//验证码

    private Callback.Cancelable mCancelable;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_regist_input_sms_code;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_COMMIT_V_CODE_OK:
                    mDialogBuilder.progressDialog.dismiss();
                    mPostAble = true;
                    Bundle bundle = new Bundle();
                    bundle.putString("phonenum", mPhoneNum);
                    startActivity(RegisterSetPwdActivity.class, bundle);
                    break;
                //请求失败
                case MSG_ERROR:
                    T.showShort(RegisterInputVCodeActivity.this, (String) msg.obj);
                    mDialogBuilder.progressDialog.dismiss();
                    mPostAble = true;
                    break;
                case MSG_GET_V_CODE:
                    mDialogBuilder.progressDialog.dismiss();
                    mPostAble = true;
                    T.showShort(RegisterInputVCodeActivity.this, "发送验证请求成功");
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.btn_commit, R.id.btn_get_code_again})
    public void onClick(View view) {
        switch (view.getId()) {
            //发送验证
            case R.id.btn_commit:
                postCommitVCode();
                break;
            case R.id.btn_get_code_again:
                showPostGetCodeAgainDialog();
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
        mDialogBuilder = new MyDialogBuilderV1();
        mPhoneNumEt.setText(mPhoneNum);
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
            mHandler.removeMessages(MSG_COMMIT_V_CODE_OK);
            mHandler.removeMessages(MSG_ERROR);
            mHandler.removeMessages(MSG_GET_V_CODE);
        }
        ActivityStackManager.getAppManager().finishTempActivity(this);
    }


    /**
     * 提交验证
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
                RequestParams params = RequestParamsBuilder.buildCheckVCodeRequest(RegisterInputVCodeActivity.this,
                        URLConfig.URL_CHECK_VC_CODE, mPhoneNum, mVCode);
                mCancelable = x.http().post(params, new Callback.CommonCallback<REBase>() {
                    @Override
                    public void onSuccess(REBase reBase) {
                        if (mHandler != null) {
                            if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                                mHandler.sendEmptyMessage(MSG_COMMIT_V_CODE_OK);
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
     * 显示是否重法验证码请求对话框
     */
    private void showPostGetCodeAgainDialog() {

        mDialogBuilder.showChoiceTwoBtnDialog(RegisterInputVCodeActivity.this, "验证码", "是否重新发送验证码?", "取消", "重发");
        mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
            @Override
            public void onLeftBtnClick() {

            }

            @Override
            public void onRightBtnClick() {
                postGetVCodeAgain();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 再次发送获取验证码请求
     */
    private void postGetVCodeAgain() {
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

        RequestParams requestParams = RequestParamsBuilder.buildGetVCodeRP(RegisterInputVCodeActivity.this,
                URLConfig.URL_GET_VC_CODE, mPhoneNum);
        mCancelable = x.http().post(requestParams, new Callback.CommonCallback<REBase>() {
            @Override
            public void onSuccess(REBase reBase) {
                if (mHandler != null) {
                    if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                        mHandler.sendEmptyMessage(MSG_GET_V_CODE);
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
