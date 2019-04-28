package com.app.pao.activity.login;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.pao.ActivityStackManager;
import com.app.pao.R;
import com.app.pao.activity.BaseActivityV3;
import com.app.pao.activity.main.MainActivityV2;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.PreferencesData;
import com.app.pao.entity.network.REBase;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.utils.HttpExceptionHelper;
import com.app.pao.network.utils.RequestParamsBuilder;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.utils.StringUtils;
import com.app.pao.utils.T;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Raul.Fan on 2016/9/7.
 * 忘记密码流程，重置密码页面
 */
public class ForgetPwdResetActivity extends BaseActivityV3 {

    private static final int MSG_POST_OK = 1;
    private static final int MSG_POST_ERROR = 2;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_login_resetpwd_title)
    TextView mTitleTv;
    @BindView(R.id.et_login_reset_psw)
    EditText mPwdEt;
    @BindView(R.id.ibtn_reset_password_show)
    ImageButton mShowPwdBtn;

    private MyDialogBuilderV1 mDialogBuilder;
    /* local data */
    private boolean mPostAble = true;//是否可以发送请求
    private Callback.Cancelable mCancelable;

    private boolean mIsShowPsw = false;//是否显示密码
    private String mPhoneNum;//手机号码
    private String pwd;//密码

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_reset_pwd;
    }


    @OnClick({R.id.ibtn_reset_password_show, R.id.btn_login_resetpwd_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击是否显示密码按钮
            case R.id.ibtn_reset_password_show:
                onShowPwdBtnClick();
                break;
            //提交修改密码
            case R.id.btn_login_resetpwd_commit:
                onCommitBtnClick();
                break;
        }
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
            startActivity(MainActivityV2.class);
            ActivityStackManager.getAppManager().finishAllTempActivity();
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
                case MSG_POST_ERROR:
                    T.showShort(ForgetPwdResetActivity.this, (String) msg.obj);
                    mDialogBuilder.progressDialog.dismiss();
                    mPostAble = true;
                    break;
                //验证码验证成功
                case MSG_POST_OK:
                    mDialogBuilder.progressDialog.dismiss();
                    mPostAble = true;
                    T.showShort(ForgetPwdResetActivity.this, getResources().getString(R.string.T_LoginResetPwdActivity_Success));
                    startActivity(MainActivityV2.class);
                    PreferencesData.setPassword(ForgetPwdResetActivity.this, pwd);
                    ActivityStackManager.getAppManager().finishAllTempActivity();
                    break;
            }
        }
    };

    @Override
    protected void initData() {
        mCheckNewData = false;
        mPhoneNum = getIntent().getExtras().getString("phonenum");
    }

    @Override
    protected void initViews() {
        mDialogBuilder = new MyDialogBuilderV1();
        setSupportActionBar(mToolbar);
        mTitleTv.setText("请设置密码，之后你可以使用\n手机号" + mPhoneNum + " + 密码登录");
    }

    @Override
    protected void doMyCreate() {
        ActivityStackManager.getAppManager().addTempActivity(this);
    }

    @Override
    protected void causeGC() {
        if (mHandler != null) {
            mHandler.removeMessages(MSG_POST_ERROR);
            mHandler.removeMessages(MSG_POST_OK);
        }
        if (mCancelable != null) {
            mCancelable.cancel();
        }
        ActivityStackManager.getAppManager().finishTempActivity(this);
    }

    /**
     * 点击是否显示密码按钮
     */
    private void onShowPwdBtnClick() {
        if (mIsShowPsw) {
            // 隐藏密码
            mPwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mShowPwdBtn.setImageResource(R.drawable.icon_showpwd);
        } else {
            //显示密码
            mPwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mShowPwdBtn.setImageResource(R.drawable.icon_hidepwd);
        }
        mIsShowPsw = !mIsShowPsw;
        //将光标置于最后位置
        Editable ea = mPwdEt.getText();
        mPwdEt.setSelection(ea.length());
    }


    /**
     * 点击提交重置密码按钮
     */
    private void onCommitBtnClick() {
        if (!mPostAble) {
            return;
        }
        //判断密码
        pwd = mPwdEt.getText().toString();
        String error = StringUtils.checkPasswordInputError(ForgetPwdResetActivity.this, pwd);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            T.showShort(ForgetPwdResetActivity.this, error);
            mPwdEt.setError(error);
            return;
        }
        mPostAble = false;
        //发送验证码请求提示
        mDialogBuilder.showProgressDialog(this, "正在请求修改密码..", true);
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
                RequestParams params = RequestParamsBuilder.buildResetPwdRP(ForgetPwdResetActivity.this, URLConfig.URL_UPDATE_USER_INFO, pwd);
                mCancelable = x.http().post(params, new Callback.CommonCallback<REBase>() {
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

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });
    }
}
