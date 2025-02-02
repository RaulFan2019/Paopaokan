package com.app.pao.activity.main;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

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
import com.app.pao.entity.network.GetCommonLoginResult;
import com.app.pao.entity.network.REBase;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.utils.HttpExceptionHelper;
import com.app.pao.network.utils.RequestParamsBuilder;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.utils.T;
import com.lidroid.xutils.view.annotation.ContentView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by LY on 2016/4/17.
 * 显示广告界面
 */
@ContentView(R.layout.activity_ad)
public class AdActivity extends BaseActivityV3 {

    private static final String TAG = "AdActivity";

    private static final int MSG_CHANGE_NUM = 0;//跳过数字发生变化
    private static final int MSG_POST_ERROR = 1;//请求失败
    private static final int MSG_LOGIN_OK = 2;//请求成功

    @BindView(R.id.iv_ad_pic)
    ImageView mAdPicIv;
    @BindView(R.id.iv_ad_skip_num)
    ImageView mAdSkipNumIv;

    /* local data */
    private String mPicPath;//保存的广告图片的路径
    private int mUserId;//用户id
    private String mUserPassword;//用户密码

    private MyDialogBuilderV1 mDialogBuilder;

    private int i = 4;//循环标识

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ad;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_CHANGE_NUM) {
                i--;
                switch (i) {
                    case 3:
                        mAdSkipNumIv.setBackgroundResource(R.drawable.num_ad_skip_3);
                        mHandler.sendEmptyMessageDelayed(MSG_CHANGE_NUM, 1000);
                        break;
                    case 2:
                        mAdSkipNumIv.setBackgroundResource(R.drawable.num_ad_skip_2);
                        mHandler.sendEmptyMessageDelayed(MSG_CHANGE_NUM, 1000);
                        break;
                    case 1:
                        mAdSkipNumIv.setBackgroundResource(R.drawable.num_ad_skip_1);
                        mHandler.sendEmptyMessageDelayed(MSG_CHANGE_NUM, 1000);
                        break;
                    case 0:
                        mAdSkipNumIv.setBackgroundResource(R.drawable.num_ad_skip_go);
                        mHandler.sendEmptyMessageDelayed(MSG_CHANGE_NUM, 1000);
                        break;
                    case -1:
                        startNextActivity(0);
                        break;
                }
                //请求错误
            } else if (msg.what == MSG_POST_ERROR) {
                T.showShort(AdActivity.this, (String) msg.obj);
                mDialogBuilder.progressDialog.dismiss();
                startActivity(LoginChangeUserActivity.class);
            } else if (msg.what == MSG_LOGIN_OK) {
                mDialogBuilder.progressDialog.dismiss();
                startActivity(MainActivityV2.class);
            }
        }
    };


    @butterknife.OnClick({R.id.ll_ad_skip_but, R.id.iv_ad_pic})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_ad_pic:
                startNextActivity(1);
                break;
            case R.id.ll_ad_skip_but:
                startNextActivity(0);
                break;
        }
    }


    @Override
    protected void initData() {
        mCheckNewData = false;
        mUserId = PreferencesData.getUserId(AdActivity.this);
        mUserPassword = PreferencesData.getPassword(AdActivity.this);
        mPicPath = PreferencesData.getAdPath(AdActivity.this);
    }

    @Override
    protected void initViews() {
        mDialogBuilder = new MyDialogBuilderV1();

    }

    @Override
    protected void doMyCreate() {
        if (mPicPath.equals("") || mPicPath == null) {
            mHandler.removeMessages(MSG_CHANGE_NUM);
            startNextActivity(0);
            return;
        } else {
            File adFile = new File(mPicPath);
            //若文件不存在
            if (!adFile.exists()) {
                mHandler.removeMessages(MSG_CHANGE_NUM);
                startNextActivity(0);
                return;
            } else {
                // 按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
                mAdPicIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mAdPicIv.setImageURI(Uri.fromFile(new File(mPicPath)));
            }
        }

        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(MSG_CHANGE_NUM, 1000);
        }

    }

    @Override
    protected void causeGC() {
        if (mHandler != null) {
            mHandler.removeMessages(MSG_CHANGE_NUM);
            mHandler.removeMessages(MSG_LOGIN_OK);
            mHandler.removeMessages(MSG_POST_ERROR);
        }
    }

    /**
     * 启动页面
     *
     * @param type
     */
    private void startNextActivity(final int type) {
        //游客状态
        if (mUserId == AppEnum.DEFAULT_USER_ID) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isFirst", true);
            bundle.putInt("startAd", type);
            startActivity(LoginActivityV2.class, bundle);
            finish();
            return;
        }
        if (PreferencesData.getHasLogin(getApplicationContext())
                && LocalApplication.getInstance().getLoginUser(getApplicationContext()) == null) {
            postLogin(type);
            PreferencesData.setUserId(AdActivity.this, AppEnum.DEFAULT_USER_ID);
            Bundle bundle = new Bundle();
            bundle.putBoolean("isFirst", true);
            bundle.putInt("startAd", type);
            startActivity(LoginActivityV2.class, bundle);
            finish();
            return;
        }
        //若非游客，并且已登出，进入账号选择页面
        if (!PreferencesData.getHasLogin(AdActivity.this)) {
            Bundle bundle = new Bundle();
            bundle.putInt("startAd", type);
            startActivity(LoginChangeUserActivity.class, bundle);
            finish();
            return;
        }
        //若密码不为空进入主页面
        Bundle bundle = new Bundle();
        bundle.putInt("startAd", type);
        startActivity(MainActivityV2.class, bundle);
        finish();
        LocalApplication.getInstance().setLoginUser(UserData.getUserById(AdActivity.this, mUserId));
    }


    /**
     * 重写back键方法，当在此页面进行back键操作的时候，不会做任何反应
     */
    @Override
    public void onBackPressed() {
    }

    /**
     * 用户登录
     */
    private void postLogin(final int type) {
        mDialogBuilder.showProgressDialog(AdActivity.this, "正在登录..", false);
        x.task().post(new Runnable() {
            @Override
            public void run() {
                String phoneNum = PreferencesData.getUserName(AdActivity.this);
                String pwd = PreferencesData.getPassword(AdActivity.this);
                String devicePushId = JPushInterface.getRegistrationID(getApplicationContext());
                String deviceOs = AppEnum.DEVICEOS;
                RequestParams params = RequestParamsBuilder.buildCommonLoginRP(AdActivity.this,
                        URLConfig.URL_COMMON_LOGIN, phoneNum, pwd, devicePushId, deviceOs);
                x.http().post(params, new Callback.CommonCallback<REBase>() {
                    @Override
                    public void onSuccess(REBase reBase) {
                        if (mHandler != null) {
                            if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                                GetCommonLoginResult entity = JSON.parseObject(reBase.result, GetCommonLoginResult.class);
                                UserData.CommonLogin(AdActivity.this, entity);
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
