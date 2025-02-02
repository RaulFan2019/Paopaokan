package com.app.pao.activity.run;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;


import android.text.SpannableString;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.settings.BleSettingsActivity;
import com.app.pao.activity.workout.HistoryListActivityV2;
import com.app.pao.activity.workout.MyClockListActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.PreferencesData;
import com.app.pao.data.db.MaidianData;
import com.app.pao.data.db.UserData;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.network.GetBeginRunInfoResult;
import com.app.pao.entity.network.GetPersonRecordResult;
import com.app.pao.entity.network.GetWxInviteResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.BadgeView;
import com.app.pao.ui.widget.RiseNumberTextView;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.app.pao.utils.wx.WxShareManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.ProgressView;
import com.umeng.socialize.bean.SHARE_MEDIA;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.hwh.sdk.ble.BleManagerService;
import cn.hwh.sdk.ble.EventBleConnect;

/**
 * Created by Raul on 2015/11/12.
 * 准备跑步页面
 */
@ContentView(R.layout.activity_preper_run)
public class PreperRunActivity extends BaseAppCompActivity implements View.OnClickListener, AMapLocationListener {

    /* contains */
    private static final String TAG = "PreperRunActivity";
    private static final int MSG_LOC_ERROR = 0x01;
    private static final int IntervalError = 5000;

    private static final int MSG_POST_BEGIN_RUN = 0x02;
    private static final int MSG_POST_RUNNING_INFO = 0x03;

    /* local view about gps */
    @ViewInject(R.id.tv_gps_search)
    private TextView mSearchTv;//搜索GPS状态文本
    @ViewInject(R.id.ll_tip)
    private LinearLayout mTipLl;//提示布局
    @ViewInject(R.id.tv_preperrun_tip)
    private TextView mTipTv;//GPS提示文本
    @ViewInject(R.id.progress_gps_search)
    private ProgressView mSearchPv;//gps查询Progressbar

    /* local view about heartbeat */
    @ViewInject(R.id.btn_heart_state)
    private ImageView mHeartStateBtn;//心率按钮
    @ViewInject(R.id.tv_heart_beat)
    private TextView mHeartBeatTv;//心率文本


    /* local view about location */
    @ViewInject(R.id.ll_loc_info)
    private LinearLayout mLocInfoLl;//位置信息资料布局
    @ViewInject(R.id.tv_location_info)
    private TextView mLocInfoTv;//位置信息文本
    @ViewInject(R.id.iv_length)
    private ImageView mLengthIv;//距离相关的图片背景
    @ViewInject(R.id.tv_length)
    private RiseNumberTextView mLengthTv;//距离文本
    @ViewInject(R.id.tv_wan)
    private TextView mLengthWanTv;//万位的文本
    @ViewInject(R.id.v_race_arrow)
    private View mRaceArrowV;//比赛箭头

    /* local view about runner */
    @ViewInject(R.id.tv_running_time)
    private TextView mRunningCountTv;//跑步次数文本

    @ViewInject(R.id.btn_clock)
    private android.widget.ImageButton mClockBtn;
    private BadgeView badgeView;//闹钟的数字标示


    /* welcome layout */
    @ViewInject(R.id.ll_welcome)
    private FrameLayout mWelcomeL;//欢迎星球布局
    @ViewInject(R.id.tv_welcome)
    private TextView mWelcomeTv;//欢迎文本
    @ViewInject(R.id.v_warning)
    private View mWarningV;//警告标记


    /* local data about user */
    private int mSeccessCount;//定位成功数量
    private DBUserEntity mUserEntity;//用户信息
    private GetPersonRecordResult mPersonRecordEntity;//跑步信息
    private float mTempLength;

    /* 定位相关 */
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private boolean mHasLocation = false;//是否已经定位成功
    private GetBeginRunInfoResult result;

    // blue tooth
    private BluetoothAdapter mBluetoothAdapter; //蓝牙适配器
    private boolean canUseBle = true;
    private String mLastDeviceMac;//历史连接蓝牙地址

    private Typeface typeface;//用于设置字体类型

    Handler postHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_BEGIN_RUN) {
                updateLocationViews();
            } else if (msg.what == MSG_POST_RUNNING_INFO) {
                updateRunningInfoViews();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (LocalApplication.getInstance().getLoginUser(mContext).getUserId() != AppEnum.DEFAULT_USER_ID) {
            getMenuInflater().inflate(R.menu.menu_activity_preper_run, menu);
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 接收到新的ble数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventBleConnect event) {
        // 获取新的ble信息
        switch (event.msg) {
            case BleManagerService.MSG_CONNECTED:
                mHeartStateBtn.setImageResource(R.drawable.icon_heart_enable);
                mHeartBeatTv.setText("已连接");
                mHeartBeatTv.setTextColor(Color.parseColor("#3c3c39"));
                break;
            case BleManagerService.MSG_DISCONNECT:
                mHeartStateBtn.setImageResource(R.drawable.icon_heart_disable);
                mHeartBeatTv.setText("未连接");
                mHeartBeatTv.setTextColor(Color.parseColor("#bbbbbb"));
                break;
            case BleManagerService.MSG_NEW_HEARTBEAT:
                mHeartStateBtn.setImageResource(R.drawable.icon_heart_enable);
                mHeartBeatTv.setText(event.heartbeat + "");
                mHeartBeatTv.setTextColor(Color.parseColor("#3c3c39"));
                break;
            case BleManagerService.MSG_FIND_CONNECT:
                if (event.connectEntity == null){
                    //断开之前的连接
                    Intent clearIntent = new Intent(PreperRunActivity.this, BleManagerService.class);
                    clearIntent.putExtra("CMD", BleManagerService.CMD_CLEAR_ALL);
                    startService(clearIntent);
                    //增加一个新的连接
                    Intent connectIntent = new Intent(PreperRunActivity.this, BleManagerService.class);
                    connectIntent.putExtra("CMD", BleManagerService.CMD_ADD_NEW_CONNECT);
                    connectIntent.putExtra("address", mLastDeviceMac);
                    startService(connectIntent);
                }
                break;
        }
    }


    Handler LocationHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_LOC_ERROR) {
                mTipTv.setText("请确保手机在室外或走动一下加快定位");
            }
        }
    };

    /**
     * 获取新的定位数据
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        //若定位有错误
        if (aMapLocation.getErrorCode() != AMapLocation.LOCATION_SUCCESS) {
            mTipTv.setText(aMapLocation.getErrorInfo());
            return;
        }
        //若定位
        float accuracy = aMapLocation.getAccuracy();// 精度
        if (accuracy < AppEnum.Gps.ACCURACY_POWER && aMapLocation.getLatitude() > 10 && aMapLocation.getLongitude() > 10) {
            mSeccessCount++;
            if (mSeccessCount > 1) {
                if (!mHasLocation) {
                    mSearchTv.setText(R.string.Tv_PreperRunActivity_Gps_Ok);
                    mTipLl.setVisibility(View.GONE);
                    mSearchPv.stop();
                    mSearchPv.setVisibility(View.GONE);
                    postGetLocationInfo(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                }
                mHasLocation = true;
                LocationHandler.removeMessages(MSG_LOC_ERROR);
            }
        } else {
            mSearchTv.setText(R.string.Tv_PreperRunActivity_Gps_Search);
            mTipTv.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    @OnClick({R.id.btn_preper_run_start, R.id.ll_setting,
            R.id.ll_heartbeat, R.id.view_guid, R.id.btn_clock, R.id.ll_race,
            R.id.ll_running_time, R.id.btn_share, R.id.btn_close_welcome})
    public void onClick(View v) {
        switch (v.getId()) {
            //点击开始跑步
            case R.id.btn_preper_run_start:
                startRunning();
                break;
            //跑步设置
            case R.id.ll_setting:
                MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "",
                        AppEnum.MaidianType.RunningSet, TimeUtils.NowTime()));
                startActivity(RunSettingsActivity.class);
                break;
            //用户心率状态
            case R.id.ll_heartbeat:
                if (canUseBle) {
                    startActivity(BleSettingsActivity.class);
                } else {
                    showNotSupportBleDialog();
                }
                break;
            //点击闹钟按钮
            case R.id.btn_clock:
                if (LocalApplication.getInstance().getLoginUser(mContext).userId == AppEnum.DEFAULT_USER_ID) {
                    T.showShort(mContext, "请先登录");
                } else {
                    MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "",
                            AppEnum.MaidianType.RunningClock, TimeUtils.NowTime()));
                    Intent intent = new Intent(mContext, MyClockListActivity.class);
                    intent.putExtra("userid", mUserEntity.userId);
                    startActivity(intent);
                }
                break;
            //点击分享按钮
            case R.id.btn_share:
                MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "",
                        AppEnum.MaidianType.SharePersonalLivingRoomInRunning, TimeUtils.NowTime()));
                if (LocalApplication.getInstance().getLoginUser(mContext).userId == AppEnum.DEFAULT_USER_ID) {
                    T.showShort(mContext, "请先登录");
                } else {
                    shareLiveRoom();
                }
                break;
            //点击比赛区域
            case R.id.ll_race:
                onClickRaceLl();
                break;
            //点击跑步次数
            case R.id.ll_running_time:
                startActivity(HistoryListActivityV2.class);
                break;
            //点击欢迎标语关闭按钮
            case R.id.btn_close_welcome:
                mWelcomeL.setVisibility(View.INVISIBLE);
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 启动定位
        locationClient.startLocation();
        LocationHandler.sendEmptyMessageDelayed(MSG_LOC_ERROR, IntervalError);
        EventBus.getDefault().register(this);
        //获取个人最新信息
        if (LocalApplication.getInstance().getLoginUser(mContext).userId == AppEnum.DEFAULT_USER_ID) {
            String recordStr = WorkoutData.getWorkOutCountAndLength(mContext, AppEnum.DEFAULT_USER_ID);
            mPersonRecordEntity = new GetPersonRecordResult();
            mPersonRecordEntity.totallength = Float.parseFloat(recordStr.split(":")[1]);
            mPersonRecordEntity.totalcount = Integer.parseInt(recordStr.split(":")[0]);
            updateRunningInfoViews();
        } else {
            postGetUserRunInfo();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        //取消定位
        locationClient.stopLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
        if (null != postHandler) {
            postHandler.removeMessages(MSG_POST_BEGIN_RUN);
        }
    }

    @Override
    protected void initData() {
        mSeccessCount = 0;
        typeface = Typeface.createFromAsset(getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
    }

    @Override
    protected void initViews() {
        //警告标记
        int phoneType = PreferencesData.getPhoneType(mContext);
        if (!PreferencesData.getHasShowPhoneTypeWarning(mContext) &&
                (phoneType == AppEnum.PhoneType.HUAWEI
                        || phoneType == AppEnum.PhoneType.XIAOMI
                        || phoneType == AppEnum.PhoneType.OPPO)) {
            mWarningV.setVisibility(View.VISIBLE);
            PreferencesData.setHasShowPhoneTypeWarning(mContext, true);
        }
        mSearchPv.start();
        mUserEntity = LocalApplication.getInstance().getLoginUser(mContext);
        if (mUserEntity != null && !mUserEntity.getRunInfo().equals("")) {
            mPersonRecordEntity = JSON.parseObject(mUserEntity.getRunInfo(), GetPersonRecordResult.class);
        }
        //设置闹钟数量
        badgeView = new BadgeView(mContext, mClockBtn);
        badgeView.setTextSize(8);
//        badgeView.setBackgroundColor(Color.WHITE);
        badgeView.setBackgroundResource(R.drawable.icon_red_circle);
        badgeView.setTextColor(Color.parseColor("#ffffff"));
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);

        if (mPersonRecordEntity != null) {
            mTempLength = mPersonRecordEntity.totallength;
            mRunningCountTv.setText(mPersonRecordEntity.totalcount + "");
        } else {
            mTempLength = 0;
            mRunningCountTv.setText("0");
        }
        mLengthTv.setTypeface(typeface);
        String startLengthStr = NumUtils.retainTheDecimal(mTempLength);
        if (startLengthStr.contains("万")) {
            mLengthWanTv.setVisibility(View.VISIBLE);
            mLengthTv.setText(NumUtils.retainTheDecimal(mTempLength / 10000));
        } else {
            mLengthWanTv.setVisibility(View.GONE);
            mLengthTv.setText(NumUtils.retainTheDecimal(mTempLength));
        }
        mLengthIv.setImageResource(NumUtils.parseBgFromLength(mTempLength));
    }

    @Override
    protected void doMyOnCreate() {
        initLocationClient();
        //检查蓝牙配置
        initBLE();
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {
        if (!getGpsState(mContext)) {
            mDialogBuilder.showChoiceDialog(mContext, "提示", "请打开定位服务和权限以确保定位成功", "取消", null, "设置");
            mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceDilaogListener() {
                @Override
                public void onLeftBtnClick() {

                }

                @Override
                public void onMiddleBtnClick() {

                }

                @Override
                public void onRightBtnClick() {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        mContext.startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // The Android SDK doc says that the location settings activity
                        // may not be found. In that case show the general settings.

                        // General settings activity
                        intent.setAction(Settings.ACTION_SETTINGS);
                        try {
                            mContext.startActivity(intent);
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    /**
     * 更新和位置相关的页面
     */
    private void updateLocationViews() {
        //位置信息
        mLocInfoLl.setVisibility(View.VISIBLE);
        String locInfo = " " + result.city + " " + result.weather + " " + result.temp + "℃ 空气 " + result.airquality;
        mLocInfoTv.setText(locInfo);
        //比赛信息
        if (result.race != null && result.race.id != 0) {
            mSearchTv.setText(result.race.name);
            mTipLl.setVisibility(View.VISIBLE);
            mTipTv.setText("沿途有" + result.race.cameracount + "个摄像点期待你的精彩表现");
            mRaceArrowV.setVisibility(View.VISIBLE);
        }
        //闹钟数量
        badgeView.setText(result.clockcount + "");
        if (result.clockcount == 0) {
            badgeView.hide();
        } else {
            badgeView.show();
            if (result.clockcount <= 99) {
                badgeView.setText(result.clockcount + "");
            } else {
                badgeView.setText("…");
            }
        }
    }

    /**
     * 更新跑步相关的页面
     */
    private void updateRunningInfoViews() {
        //更新次数
        mRunningCountTv.setText(mPersonRecordEntity.totalcount + "");
        //更新距离
        if (mTempLength != mPersonRecordEntity.totallength) {
            String lengthStr = NumUtils.retainTheDecimal(mPersonRecordEntity.totallength);
            if (lengthStr.contains("万")) {
                mLengthWanTv.setVisibility(View.VISIBLE);
                mLengthTv.withNumber(mPersonRecordEntity.totallength / 1000 / 10000);
            } else {
                mLengthWanTv.setVisibility(View.GONE);
                mLengthTv.withNumber(mPersonRecordEntity.totallength / 1000);
            }
            mLengthTv.setDuration(1500);
            mLengthTv.start();
        }
        if (mPersonRecordEntity.totallength == 0) {
            mWelcomeL.setVisibility(View.VISIBLE);
            mWelcomeTv.setText("欢迎来到水星，从这里开始你的征程吧");
        }
        if (NumUtils.getTotalLengthLevel(mTempLength) != NumUtils.getTotalLengthLevel(mPersonRecordEntity.totallength)) {
            mWelcomeL.setVisibility(View.VISIBLE);
            mWelcomeTv.setText("欢迎来到" + NumUtils.getCardTypeString(mPersonRecordEntity.totallength));
            startBgAnim();
        }
        mTempLength = mPersonRecordEntity.totallength;
    }

    @Override
    protected void destroy() {

    }

    private void initLocationClient() {
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(this);
        locationOption.setNeedAddress(false);//不需要返回位置信息
        locationOption.setInterval(2000);//定位间隔
        locationOption.setOnceLocation(false);//持续定位
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
    }

    /**
     * 开始跑
     */
    private void startRunning() {
//        Log.v(TAG,"click startRunning");
        if (!mHasLocation) {
            T.showShort(mContext, "请等待GPS定位");
            return;
        }
        // 启动定位
        locationClient.stopLocation();
        runningNow();
//        finish();
    }

    /**
     * 点击赛事区域
     */
    private void onClickRaceLl() {
        if (result != null && result.race != null && result.race.id != 0) {
            String url = result.race.url;
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            bundle.putInt("type", LiveRoomH5Activity.TYPE_RACE);
            startActivity(LiveRoomH5Activity.class, bundle);
        }
    }


    /**
     * 初始化蓝牙
     */
    private void initBLE() {
        BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // 检查设备上是否支持蓝牙
        if (mBluetoothAdapter == null) {
            return;
        }
        // 检查当前手机是否支持ble 蓝牙,如果不支持
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            mHeartBeatTv.setText("不可用");
            mHeartBeatTv.setTextColor(Color.parseColor("#bbbbbb"));
            return;
        }
        if (!PreferencesData.getBleEnable(mContext)) {
            return;
        }
        // 若蓝牙需要判断 , 且连接不正常
        if (!mBluetoothAdapter.isEnabled() ||
                PreferencesData.getBlueToothDeviceMac(mContext).equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            mDialogBuilder.showBlueToothSetDialog(mContext, "提醒", "是否要连接蓝牙设备?", "下次", "不再提示");
            mDialogBuilder.setBluetoothSetListener(new MyDialogBuilderV1.BluetoothSetDialogListener() {
                @Override
                public void onLeftBtnClick() {

                }

                @Override
                public void onRightBtnClick() {
                    PreferencesData.setBleEnable(mContext, false);
                }

                @Override
                public void onBuletoothSetClick() {
                    startActivity(BleSettingsActivity.class);
                }

                @Override
                public void onCancel() {

                }
            });
        } else {
            mLastDeviceMac = PreferencesData.getBlueToothDeviceMac(mContext);
            if (!mLastDeviceMac.equals(AppEnum.DEFAULT_BLE_MAC)) {
                Intent intent = new Intent(PreperRunActivity.this,BleManagerService.class);
                intent.putExtra("CMD",BleManagerService.CMD_GET_CONNECT);
                intent.putExtra("address", mLastDeviceMac);
                startService(intent);
            }
        }
    }

    /**
     * 立即开始跑步
     */
    private void runningNow() {
        //检查用户信息
        DBUserEntity userEntity = LocalApplication.getInstance().getLoginUser(mContext);
        if (userEntity.height == 0) {
            userEntity.height = 174;
        }
        if (userEntity.weight == 0) {
            userEntity.weight = 60;
        }
        LocalApplication.getInstance().setLoginUser(userEntity);
        UserData.updateUser(mContext, userEntity);
        if (userEntity.getUserId() == AppEnum.DEFAULT_USER_ID) {
            MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "", AppEnum.MaidianType.VisitorRunning, TimeUtils.NowTime()));
        }

        //跳转倒数界面
        Bundle b = new Bundle();
        b.putBoolean("StartFromPreper", true);
        if (mPersonRecordEntity != null) {
            b.putInt("runCount", mPersonRecordEntity.totalcount + 1);
        } else {
            b.putInt("runCount", 1);
        }
        startActivity(RunningActivityV2.class, b);
//        Log.v(TAG,"start run");
    }

    /**
     * 获取直播间的Url
     */
//    private void postGetLiveRoomUrl() {
//        HttpUtils http = new HttpUtils();
//        String POST_URL = URLConfig.URL_LIVE_ROOM_SHARE;
//        RequestParams params = RequestParamsBuild.buildGetLiveRoomUrlRequest(mContext);
//        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
//
//            @Override
//            protected void onErrorResponse(int errorCode, String errorMsg) {
//                T.showShort(mContext, errorMsg);
//            }
//
//            @Override
//            protected void onRightResponse(String Response) {
//                GetLiveRoomUrlResult result = JSON.parseObject(Response, GetLiveRoomUrlResult.class);
//                String url = result.getUrl();
//                shareLiveRoom(url);
//            }
//
//            @Override
//            protected void onFailureResponse(HttpException e, String s) {
//                T.showShort(mContext, s);
//            }
//
//            @Override
//            protected void onFinish() {
//
//            }
//        });
//    }

    /**
     * 个人直播间分享
     */
    private void shareLiveRoom() {
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }
        mDialogBuilder.showProgressDialog(mContext, "正在获取分享信息..", false);
        // 发送请求
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_LIVE_ROOM_SHARE;
        final RequestParams params = RequestParamsBuild.BuildGetLiveRoomShareRequest(mContext);
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                final GetWxInviteResult result = JSON.parseObject(Response, GetWxInviteResult.class);
//                if (result.getTitle() != null && !result.getTitle().isEmpty()) {
                final WxShareManager share = new WxShareManager(PreperRunActivity.this);
//                share.startWxShareUrl(result.getText(), result.getTitle(), url);
                mDialogBuilder.setShareWxFriendDialog(mContext, "分享跑步直播到");
                mDialogBuilder.setListener(new MyDialogBuilderV1.ShareWxFriendDialogListener() {
                    @Override
                    public void onWxFriendClick() {
                        share.startWxShareUrlByCustom(SHARE_MEDIA.WEIXIN, result.text, result.title, result.link, result.image);
                    }

                    @Override
                    public void onWxFriendCircleClick() {
                        share.startWxShareUrlByCustom(SHARE_MEDIA.WEIXIN_CIRCLE, result.text, result.title, result.link, result.image);
                    }

                    @Override
                    public void onCancle() {

                    }
                });
//                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
            }

            @Override
            protected void onFinish() {
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }


    /**
     * 显示不支持BLE设备的对话框
     */
    private void showNotSupportBleDialog() {
        SpannableString spanStr = new SpannableString("本手机不支持连接心率设备");
        mDialogBuilder.showSimpleMsgDialog(mContext, "心率设备", spanStr);
    }

    public static boolean getGpsState(Context context) {
        ContentResolver resolver = context.getContentResolver();
        boolean open = Settings.Secure.isLocationProviderEnabled(resolver,
                LocationManager.GPS_PROVIDER);
//        System.out.println("getGpsState:" + open);
        return open;
    }

    /**
     * 测试动画
     */
    private void startBgAnim() {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation1 = new AlphaAnimation(1, 0);
        alphaAnimation1.setDuration(500);
        TranslateAnimation translateAnimation1 = new TranslateAnimation(0, 0, 0, DeviceUtils.dpToPixel(420));
        translateAnimation1.setDuration(500);
        animationSet.addAnimation(alphaAnimation1);
        animationSet.addAnimation(translateAnimation1);
        mLengthIv.startAnimation(animationSet);
        alphaAnimation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLengthIv.setImageResource(NumUtils.parseBgFromLength(mPersonRecordEntity.totallength));
                AlphaAnimation alphaAnimation1 = new AlphaAnimation(0, 1);
                alphaAnimation1.setDuration(500);
                TranslateAnimation translateAnimation2 = new TranslateAnimation(0, 0, DeviceUtils.dpToPixel(420), 0);
                translateAnimation2.setDuration(500);
                AnimationSet animationSet2 = new AnimationSet(true);
                animationSet2.addAnimation(alphaAnimation1);
                animationSet2.addAnimation(translateAnimation2);
                mLengthIv.startAnimation(animationSet2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    /**
     * 获取所在地点的信息
     */
    private void postGetLocationInfo(final double latitude, final double longitude) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_BEGIN_RUNINFO;
        // params
        RequestParams params = RequestParamsBuild.buildGetBeginRunInfoRequest(mContext, latitude, longitude);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {

            }

            @Override
            protected void onRightResponse(String Response) {
                result = JSON.parseObject(Response, GetBeginRunInfoResult.class);
                if (postHandler != null) {
                    postHandler.sendEmptyMessage(MSG_POST_BEGIN_RUN);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 获取用户的跑步记录信息
     */
    private void postGetUserRunInfo() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PERSON_RECORDS;
        RequestParams params = RequestParamsBuild.buildGetPersonRecordRequest(mContext, mUserEntity.getUserId());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {

            }

            @Override
            protected void onRightResponse(String Response) {
                mPersonRecordEntity = JSON.parseObject(Response, GetPersonRecordResult.class);
//                Log.v("LocalUserFragment", Response);
                mUserEntity.setRunInfo(Response);
                LocalApplication.getInstance().setLoginUser(mUserEntity);
                UserData.updateUser(mContext, mUserEntity);
                if (postHandler != null) {
                    postHandler.sendEmptyMessage(MSG_POST_RUNNING_INFO);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {

            }
        });
    }
}
