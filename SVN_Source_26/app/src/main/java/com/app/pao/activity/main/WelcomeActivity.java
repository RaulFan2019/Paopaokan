package com.app.pao.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseActivityV3;
import com.app.pao.activity.login.LoginActivityV2;
import com.app.pao.activity.login.LoginChangeUserActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.PreferencesData;
import com.app.pao.data.db.UserData;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.network.GetCommonLoginResult;
import com.app.pao.entity.network.REBase;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.utils.HttpExceptionHelper;
import com.app.pao.network.utils.RequestParamsBuilder;
import com.app.pao.service.AdService;
import com.app.pao.service.CheckUpdateService;
import com.app.pao.service.GetPhoneTypeService;
import com.app.pao.service.SendCrashLogService;
import com.app.pao.service.SendMaidianService;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.T;
import com.lidroid.xutils.view.annotation.ContentView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

/**
 * 欢迎页
 * 在整个APP堆栈底部,不被杀掉
 */
@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivityV3 {

    private static final int MSG_POST_ERROR = 1;//请求失败
    private static final int MSG_LOGIN_OK = 2;//请求成功

    /* local view */
    @BindView(R.id.ll_welcome)
    LinearLayout mRootLl;

    private MyDialogBuilderV1 mDialogBuilder;

    /* local data */
    private int mUserId;//用户ID
    private boolean showAd = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_LOGIN_OK:
                    mDialogBuilder.progressDialog.dismiss();
                    startActivity(MainActivityV2.class);
                    break;
                //登录错误
                case MSG_POST_ERROR:
                    T.showShort(WelcomeActivity.this, (String) msg.obj);
                    mDialogBuilder.progressDialog.dismiss();
                    startActivity(LoginChangeUserActivity.class);
                    break;
            }
        }
    };

    @Override
    protected void initData() {
        mCheckNewData = false;
    }

    @Override
    protected void initViews() {
        //全屏
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        mDialogBuilder = new MyDialogBuilderV1();
    }

    @Override
    protected void doMyCreate() {
        //启动广告图片下载Service
        Intent intent = new Intent(this, AdService.class);
        startService(intent);

        //启动上传报错CrashService
        Intent crashIntent = new Intent(this, SendCrashLogService.class);
        startService(crashIntent);

        //启动上传埋点列表SendMaidianService
        Intent maiDianIntent = new Intent(this, SendMaidianService.class);
        startService(maiDianIntent);

        //启动获取手机类型服务
        Intent getPhoneIntent = new Intent(this, GetPhoneTypeService.class);
        startService(getPhoneIntent);

        //启动检查APP版本
        Intent checkUpdateIntent = new Intent(this, CheckUpdateService.class);
        startService(checkUpdateIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserId = PreferencesData.getUserId(getApplicationContext());
        JPushInterface.onResume(this);
        AnimLaunch();
    }

    @Override
    protected void causeGC() {
        if (mHandler != null) {
            mHandler.removeMessages(MSG_LOGIN_OK);
            mHandler.removeMessages(MSG_POST_ERROR);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    /**
     * 启动APP的主要内容
     */
    private void launchMain() {
        //判断版本号是否更新, 若更新跳转到引导页
        if (PreferencesData.getOldVersion(this) < DeviceUtils.getVersionCode(this.getPackageName())) {
            startActivity(SplashActivity.class);
            PreferencesData.setOldVersion(getApplicationContext(), DeviceUtils.getVersionCode(this.getPackageName()));
            showAd = true;
            return;
        }

        //若正在跑步
        if (LocalApplication.getInstance().getLoginUser(getApplicationContext()) != null) {
            if (WorkoutData.getUnFinishWorkout(getApplicationContext(),
                    LocalApplication.getInstance().getLoginUser(getApplicationContext()).userId) != null) {
                LocalApplication.getInstance().setLoginUser(UserData.getUserById(getApplicationContext(), mUserId));
                Bundle bundle = new Bundle();
                startActivity(MainActivityV2.class, bundle);
                showAd = true;
                return;
            }
        }
        //若已经Show过广告页
        if (!showAd) {
            //跳转到广告页面
            showAd = true;
            startActivity(AdActivity.class);
            return;
        }
        startNextActivity();
    }

    private void startNextActivity() {
        showAd = true;
        //游客状态
        if (mUserId == AppEnum.DEFAULT_USER_ID) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isFirst", true);
            startActivity(LoginActivityV2.class, bundle);
            return;
        }
        //若用户已登录，但用户数据为空
        if (PreferencesData.getHasLogin(getApplicationContext())
                && LocalApplication.getInstance().getLoginUser(getApplicationContext()) == null) {
            postLogin();
            return;
        }

        //若非游客，但已登出，进入账号选择页面
        if (!PreferencesData.getHasLogin(WelcomeActivity.this)) {
            startActivity(LoginChangeUserActivity.class);
            return;
        }
        //若密码不为空进入主页面
        Bundle bundle = new Bundle();
        startActivity(MainActivityV2.class, bundle);
        LocalApplication.getInstance().setLoginUser(UserData.getUserById(getApplicationContext(), mUserId));
    }

    /**
     * 页面启动动画
     */
    private void AnimLaunch() {
        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        aa.setDuration(2000);
        if (mRootLl == null || aa == null) {
            launchMain();
            return;
        }
        mRootLl.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                launchMain();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    /**
     * 用户登录
     */
    private void postLogin() {
        mDialogBuilder.showProgressDialog(WelcomeActivity.this, "正在登录..", false);
        x.task().post(new Runnable() {
            @Override
            public void run() {
                String phoneNum = PreferencesData.getUserName(WelcomeActivity.this);
                String pwd = PreferencesData.getPassword(WelcomeActivity.this);
                String devicePushId = JPushInterface.getRegistrationID(getApplicationContext());
                String deviceOs = AppEnum.DEVICEOS;
                RequestParams params = RequestParamsBuilder.buildCommonLoginRP(WelcomeActivity.this,
                        URLConfig.URL_COMMON_LOGIN, phoneNum, pwd, devicePushId, deviceOs);
                x.http().post(params, new Callback.CommonCallback<REBase>() {
                    @Override
                    public void onSuccess(REBase reBase) {
                        if (mHandler != null) {
                            if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                                GetCommonLoginResult entity = JSON.parseObject(reBase.result, GetCommonLoginResult.class);
                                UserData.CommonLogin(WelcomeActivity.this, entity);
                                LocalApplication.getInstance().setLoginUser(null);
                                mHandler.sendEmptyMessage(MSG_LOGIN_OK);
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
