package com.app.pao.activity.settings;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.app.pao.ActivityStackManager;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.main.ScanQRCodeActivityReplace;
import com.app.pao.activity.main.ScanQrCodeActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.PreferencesData;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.SwitchView;
import com.app.pao.utils.AppStoreUtils;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Raul on 2015/11/18.
 * 系统设置页面
 */
@ContentView(R.layout.activity_sys_settings)
public class SystemSettingsActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "SystemSettingsActivity";

    private static final int MSG_POST_ERROR = 1;
    private static final int MSG_POST_OK = 2;

    /* local data */
    private boolean mPostAble;//是否可以发送请求
    private DBUserEntity mUserEntity;//用户信息

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.sv_jpush)
    private SwitchView mJpushSv;//极光推送选择
//    @ViewInject(R.id.sv_tts)
//    private SwitchView mTtsSv;//语音选择
//    @ViewInject(R.id.sv_video)
//    private SwitchView mVideoSv;//直播视频

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
    Handler mPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //网络请求失败
                case MSG_POST_ERROR:
                    T.showShort(mContext, (String) msg.obj);
                    mDialogBuilder.progressDialog.dismiss();
                    mPostAble = true;
                    break;
                //登出请求成功
                case MSG_POST_OK:
                    mDialogBuilder.progressDialog.dismiss();
                    mPostAble = true;
                    Logout();
                    break;
            }
        }
    };


    @Override
    @OnClick({R.id.ll_logout, R.id.ll_richscan, R.id.ll_connection_device, R.id.ll_evaluate,
            R.id.ll_feedback, R.id.ll_about, R.id.ll_AccountAndSecurity})
    public void onClick(View v) {
        switch (v.getId()) {
            //扫一扫
            case R.id.ll_richscan:
                Bundle bundle = new Bundle();
                bundle.putInt("hasScanSys",1);
                startActivity(ScanQRCodeActivityReplace.class,bundle);
                break;
            //连接设备
            case R.id.ll_connection_device:
                startActivity(BleSettingsActivity.class);
                break;
            //评论
            case R.id.ll_evaluate:
                launchAppStore();
                break;
            //反馈
            case R.id.ll_feedback:
                startActivity(FeedBackActivityReplace.class);
                break;
            //关于
            case R.id.ll_about:
                startActivity(AboutActivity.class);
                break;
            //帐号与安全
            case R.id.ll_AccountAndSecurity:
                startActivity(SafeSettingsActivity.class);
                break;
            //登出
            case R.id.ll_logout:
                showLogoutRequst();
                break;
        }
    }

    @Override
    protected void initData() {
        mPostAble = true;
        mUserEntity = LocalApplication.getInstance().getLoginUser(mContext);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        mJpushSv.setOpened(PreferencesData.getJpushEnable(mContext));
//        mTtsSv.setOpened(PreferencesData.getVoiceEnable(mContext));
//        mVideoSv.setOpened(PreferencesData.getVideoEnable(mContext));

        mJpushSv.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                mJpushSv.setOpened(true);
                PreferencesData.setJpushEnable(mContext, true);
                JPushInterface.resumePush(getApplicationContext());
            }

            @Override
            public void toggleToOff(View view) {
                mJpushSv.setOpened(false);
                PreferencesData.setJpushEnable(mContext, true);
                JPushInterface.stopPush(getApplicationContext());
            }
        });

//        mTtsSv.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
//            @Override
//            public void toggleToOn(View view) {
//                PreferencesData.setVoiceEnable(mContext, true);
//            }
//
//            @Override
//            public void toggleToOff(View view) {
//                PreferencesData.setVoiceEnable(mContext, false);
//            }
//        });
//
//        mVideoSv.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
//            @Override
//            public void toggleToOn(View view) {
//                PreferencesData.setVideoEnable(mContext, true);
//            }
//
//            @Override
//            public void toggleToOff(View view) {
//                PreferencesData.setVideoEnable(mContext, false);
//            }
//        });
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

    }

    /**
     * 启动应用宝AppStore
     */
    private void launchAppStore() {
        if (AppStoreUtils.isAppStoreExist(mContext)) {
            AppStoreUtils.launchAppDetail(mContext, "com.app.pao");
        } else {
            AppStoreUtils.launchWebAppDetail(mContext, "com.app.pao");
        }
    }

    /**
     * 登出
     */
    private void Logout() {
        PreferencesData.setHasLogin(SystemSettingsActivity.this,false);
        ActivityStackManager.getAppManager().finishAllActivity();
    }

    /**
     * 显示登出请求
     */
    private void showLogoutRequst() {
        if (!mPostAble) {
            return;
        }
        mPostAble = false;
        mDialogBuilder.showChoiceTwoBtnDialog(mContext, "登出", "是否登出当前帐号", "取消", "登出");
        mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
            @Override
            public void onLeftBtnClick() {
                mPostAble = true;
            }

            @Override
            public void onRightBtnClick() {
                postLogoutRequest();
            }

            @Override
            public void onCancel() {
                mPostAble = true;
            }
        });
    }


    /**
     * 发送登出请求
     */
    private void postLogoutRequest() {
        //发送验证码请求提示
        mDialogBuilder.showProgressDialog(this, "正在登出..", true);
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
        String POST_URL = URLConfig.URL_LOGOUT;
        RequestParams params = RequestParamsBuild.buildLogoutRequest(mContext);
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
