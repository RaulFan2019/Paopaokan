package com.app.pao.activity.settings;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.config.WXConfig;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.SimpleTipMsgDialog;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Raul on 2015/11/18.
 * 用户帐号绑定页面
 */
@ContentView(R.layout.activity_safe_settings)
public class SafeSettingsActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "SafeSettingsActivity";
    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;
    private static final int MSG_PSW_ERROR = 2;//密码错误
    private static final int MSG_WX_BOUND_OK = 3;//微信绑定成功

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏

    @ViewInject(R.id.tv_phone)
    private TextView mPhoneNumTv;//手机号码
    @ViewInject(R.id.tv_bound_state)
    private TextView mBoundStateTv;//微信是否绑定
    @ViewInject(R.id.btn_bound_wx)
    private Button mBoundWxBtn;//绑定微信按钮
    @ViewInject(R.id.ll_reset_psw)
    private LinearLayout mResetPswLl;//重置密码

    private DBUserEntity mLoginUser;//登录用户
    private boolean mPostAble = true;

    final UMSocialService mLoginController = UMServiceFactory.getUMSocialService("com.umeng.login");//初始化友盟


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
                    startActivity(ResetPswActivity.class);
                    break;
                case MSG_POST_ERROR:
                    mPostAble = true;
                    mDialogBuilder.progressDialog.dismiss();
                    T.showShort(mContext,msg.obj.toString());
                    break;

                case MSG_PSW_ERROR:
                    mDialogBuilder.progressDialog.dismiss();
                    T.showShort(mContext, "密码错误，请输入正确的密码");
                    break;
                case MSG_WX_BOUND_OK:
                    mPostAble = true;
                    mDialogBuilder.progressDialog.dismiss();
                    mBoundWxBtn.setVisibility(View.GONE);
                    mBoundStateTv.setVisibility(View.VISIBLE);
                    mBoundStateTv.setText("已绑定" + "\n [" + mLoginUser.getWeixinnickname() + "]");
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

    @Override
    @OnClick({R.id.btn_bound_wx, R.id.ll_reset_psw})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bound_wx:
                initWxBound();
                break;
            case R.id.ll_reset_psw:
                launchResetPsw();
                break;
        }
    }

    @Override
    protected void initData() {
        mLoginUser = LocalApplication.getInstance().getLoginUser(mContext);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);

        mPhoneNumTv.setText(mLoginUser.getMobile());

        if(mLoginUser.getThirdpartyaccount() == AppEnum.ThirdPartyAccount.UNBOUND ){
            mBoundWxBtn.setVisibility(View.VISIBLE);
            mBoundStateTv.setVisibility(View.GONE);
        }else{
            mBoundWxBtn.setVisibility(View.GONE);
            mBoundStateTv.setVisibility(View.VISIBLE);
            mBoundStateTv.setText("已绑定"+"\n ["+mLoginUser.getWeixinnickname()+"]");
        }
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

    @Override
    protected void onPause() {
        if (mDialogBuilder.progressDialog != null) {
            if (mDialogBuilder.progressDialog.isShowing()) {
                mDialogBuilder.progressDialog.dismiss();
            }
        }
        super.onPause();
    }

    /**
     * 重置密码
     */
    private void launchResetPsw(){
        //判断密码是否为空
        if(mLoginUser.getPasswordisblank() == AppEnum.Passwordisblank.BLANK){
            startActivity(ResetPswActivity.class);
        }else{
            showInputDialog();
        }
    }

    /**
     * 显示简单输入框dialog
     */
    private void showInputDialog() {
        mDialogBuilder.setResetPasswordDialog(mContext,"取消","确定");
        mDialogBuilder.setListener(new MyDialogBuilderV1.ResetPasswordDialogListener() {
            @Override
            public void onLeftBtnClick() {

            }

            @Override
            public void onRightBtnClick(String password) {
                if(password != null && !password.isEmpty()){
                    checkOldPsw(password);
                }else {
                    T.showShort(mContext,"密码不能为空，请输入密码");
                }
            }

            @Override
            public void onCancel() {

            }
        });
//        Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
//            @Override
//            protected void onBuildDone(Dialog dialog) {
//                dialog.layoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            }
//
//            @Override
//            public void onPositiveActionClicked(DialogFragment fragment) {
//                EditText mPswEt = (EditText) fragment.getDialog().findViewById(R.id.custom_et_input);
//                String password = mPswEt.getText().toString();
//                if(password != null && !password.isEmpty()){
//                    checkOldPsw(password);
//                }else {
//                    T.showShort(mContext,"密码不能为空，请输入密码");
//                }
//                super.onPositiveActionClicked(fragment);
//            }
//
//            @Override
//            public void onNegativeActionClicked(DialogFragment fragment) {
//
//                super.onNegativeActionClicked(fragment);
//            }
//        };
//        builder.positiveAction("确定").negativeAction("取消").contentView(R.layout.dialog_input);
//        DialogFragment fragment = DialogFragment.newInstance(builder);
//        fragment.show(getSupportFragmentManager(), null);
    }


    /**
     * 初始化微信Bound
     */
    private void initWxBound() {
        UMWXHandler wxHandler = new UMWXHandler(mContext, WXConfig.APP_ID, WXConfig.APP_SECRET);
        wxHandler.addToSocialSDK();
        if (wxHandler.isClientInstalled()) {
            showLoadingWxDialog();
            startWxBound();
        } else {
            T.showShort(mContext, "请安装微信");
        }
    }

    /**
     * 开始微信授权Bound
     */
    private void startWxBound() {
        mLoginController.doOauthVerify(mContext, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                T.showShort(mContext, "授权开始");

            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                T.showShort(mContext, "授权错误");
                onFinish();
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                // 获取相关授权信息
                String uid = value.getString("uid");
                if (!TextUtils.isEmpty(uid)) {
                    getWxUserInfor();
                } else {

                }
                onFinish();
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                T.showShort(mContext, "授权取消");
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
    }

    /**
     * 获取微信返回用户值
     */
    private void getWxUserInfor() {
        mLoginController.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, new SocializeListeners.UMDataListener() {
            @Override
            public void onStart() {
                //       Toast.makeText(MainActivityV2.this, "获取平台数据开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(int status, Map<String, Object> info) {
                if (status == 200 && info != null) {
                    // 开始与服务器通信进行Bound
                    showWxBoundRequestDialog(info);
                } else {

                }
            }
        });
    }

    /**
     * 显示等待调起微信Dialog
     */
    private void showLoadingWxDialog() {
        mDialogBuilder.showProgressDialog(mContext, "正在跳转微信", false);
    }

    /**
     * 显示微信授权成功，开始进行APP Bound DIALOG
     */
    private void showWxBoundRequestDialog(final Map<String, Object> wxInfor) {
        //若请求未结束
        if (!mPostAble) {
            return;
        }
        mDialogBuilder.showProgressDialog(mContext, "开始绑定···", true);
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

        mPostAble = false;
        //开始请求
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_BIND_WX_ACCOUNT;
        RequestParams params = RequestParamsBuild.buildBindWxAccountRequest(mContext,wxInfor.get("unionid")
                .toString(), wxInfor.get("nickname").toString());
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
                    mPostMsg.what = MSG_WX_BOUND_OK;
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
     * 检查用户输入的旧密码是否正确
     */
    private void checkOldPsw(final String Psw) {

        mDialogBuilder.showProgressDialog(mContext, "请稍后···", true);
        mDialogBuilder.setListener(new MyDialogBuilderV1.ProgressDilaogListener() {
            @Override
            public void onCancelBtnClick() {
                cancelPostHandler();
            }

            @Override
            public void onCancel() {
                cancelPostHandler();
            }
        });

        //开始请求
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_CHECK_PASSWORD;
        RequestParams params = RequestParamsBuild.buildCheckOldPswRequest(mContext,Psw);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.arg1 = errorCode;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                int checkPass = 0;
                try {
                    JSONObject checkObj = new JSONObject(Response);
                    checkPass = checkObj.getInt("checkpass");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(checkPass == 1){
                    if (mPostHandler != null) {
                        Message mPostMsg = new Message();
                        mPostMsg.what = MSG_POST_OK;
                        mPostMsg.obj = Response;
                        mPostHandler.sendMessage(mPostMsg);
                    }
                }else{
                    if (mPostHandler != null) {
                        Message mPostMsg = new Message();
                        mPostMsg.what = MSG_PSW_ERROR;
                        mPostMsg.obj = Response;
                        mPostHandler.sendMessage(mPostMsg);
                    }
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.arg1 = 0;
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
            mPostHandler.removeMessages(MSG_POST_OK);
            mPostHandler.removeMessages(MSG_POST_ERROR);
        }
    }
}
