package com.app.pao.fragment.run;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
import com.app.pao.activity.run.LiveRoomH5Activity;
import com.app.pao.activity.run.PreperRunActivity;
import com.app.pao.activity.run.RunSettingsActivity;
import com.app.pao.activity.run.RunningActivityV2;
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
import com.app.pao.entity.network.REBase;
import com.app.pao.fragment.BaseFragmentV2;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.api.MyRequestParamsV2;
import com.app.pao.network.utils.HttpExceptionHelper;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.network.utils.RequestParamsBuilder;
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
import com.lidroid.xutils.http.client.HttpRequest;
import com.rey.material.widget.ProgressView;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hwh.sdk.ble.BleManagerService;
import cn.hwh.sdk.ble.EventBleConnect;

/**
 * Created by Raul.Fan on 2016/9/13.
 */
public class ReadyRunFragment extends BaseFragmentV2 implements AMapLocationListener {


    /* contains */
    private static final int MSG_LOC_ERROR = 0x01;
    private static final int IntervalError = 5000;

    private static final int MSG_POST_BEGIN_RUN = 0x02;
    private static final int MSG_POST_RUNNING_INFO = 0x03;
    private static final int MSG_POST_ERROR = 0x04;//网络请求失败
    private static final int MSG_POST_SHARE_OK = 0x05;//发送获取分享请求成功

    /* local view about gps */
    @BindView(R.id.tv_gps_search)
    TextView mSearchTv;//搜索Gps文本
    @BindView(R.id.ll_tip)
    LinearLayout mTipLl;//Gps连接提示文本
    @BindView(R.id.tv_preperrun_tip)
    TextView mTipTv;//准备跑的提示文本
    @BindView(R.id.progress_gps_search)
    ProgressView mSearchPv;//Gps跑步搜索转圈

    /* local view about heartbeat */
    @BindView(R.id.btn_heart_state)
    ImageView mHeartStateIv;//心率按钮
    @BindView(R.id.tv_heart_beat)
    TextView mHeartBeatTv;

    /* local view about location */
    @BindView(R.id.ll_loc_info)
    LinearLayout mLocInfoLl;//位置信息资料布局
    @BindView(R.id.tv_location_info)
    TextView mLocationInfoTv;//位置信息文本
    @BindView(R.id.iv_length)
    ImageView mLengthIv;//距离相关的图片背景
    @BindView(R.id.tv_length)
    RiseNumberTextView mLengthTv;//距离文本
    @BindView(R.id.tv_wan)
    TextView mLengthWanTv;//万位的文本
    @BindView(R.id.v_race_arrow)
    View mRaceArrowV;//比赛箭头

    /* local view about runner */
    @BindView(R.id.tv_running_time)
    TextView mRunningCountTv;

    /* welcome layout */
    @BindView(R.id.tv_welcome)
    TextView mWelcomeTv;//欢迎文本
    @BindView(R.id.ll_welcome)
    FrameLayout mWelcomeLl;//欢迎布局
    @BindView(R.id.v_warning)
    View mWarningV;//警告标记

    /* local data about user */
    private int mSeccessCount;//定位成功数量
    private DBUserEntity mUserEntity;//用户信息
    private GetPersonRecordResult mPersonRecordEntity;//跑步信息
    private float mTempLength;

    @BindView(R.id.btn_clock)
    ImageButton mClockBtn;
    private BadgeView badgeView;//闹钟的数字标示

    /* 定位相关 */
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private boolean mHasLocation = false;//是否已经定位成功
    private GetBeginRunInfoResult result;

    protected MyDialogBuilderV1 mDialogBuilder;

    // blue tooth
    private BluetoothAdapter mBluetoothAdapter; //蓝牙适配器
    private boolean canUseBle = true;
    private String mLastDeviceMac;//历史连接蓝牙地址

    private Typeface typeface;//用于设置字体类型


    public static ReadyRunFragment newInstance() {
        ReadyRunFragment fragment = new ReadyRunFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ready_run;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //获取开始跑步信息
                case MSG_POST_BEGIN_RUN:
                    updateLocationViews();
                    break;
                //获取跑步信息
                case MSG_POST_RUNNING_INFO:
                    updateRunningInfoViews();
                    break;
                //定位失败
                case MSG_LOC_ERROR:
                    mTipTv.setText("请确保手机在室外或走动一下加快定位");
                    break;
                //网络请求发生错误
                case MSG_POST_ERROR:
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }
                    T.showShort(getActivity(), (String) msg.obj);
                    break;
                //获取分享请求成功
                case MSG_POST_SHARE_OK:
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }
                    shareLiveRoom((String) msg.obj);
                    break;
            }
        }
    };


    @OnClick({R.id.btn_clock, R.id.btn_share, R.id.ll_running_time, R.id.btn_close_welcome,
            R.id.ll_heartbeat,  R.id.btn_preper_run_start,
            R.id.ll_race, R.id.ll_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击闹钟按钮
            case R.id.btn_clock:
                if (LocalApplication.getInstance().getLoginUser(getActivity()).userId == AppEnum.DEFAULT_USER_ID) {
                    T.showShort(getActivity(), "请先登录");
                } else {
                    MaidianData.saveMaidian(getActivity(), new DBEntityMaidian(System.currentTimeMillis() + "",
                            AppEnum.MaidianType.RunningClock, TimeUtils.NowTime()));
                    Intent intent = new Intent(getActivity(), MyClockListActivity.class);
                    intent.putExtra("userid", mUserEntity.userId);
                    startActivity(intent);
                }
                break;
            //点击分享
            case R.id.btn_share:
                MaidianData.saveMaidian(getActivity(), new DBEntityMaidian(System.currentTimeMillis() + "",
                        AppEnum.MaidianType.SharePersonalLivingRoomInRunning, TimeUtils.NowTime()));
                if (LocalApplication.getInstance().getLoginUser(getActivity()).userId == AppEnum.DEFAULT_USER_ID) {
                    T.showShort(getActivity(), "请先登录");
                } else {
                    getShareLiveRoom();
                }
                break;
            //点击跑步次数
            case R.id.ll_running_time:
                startActivity(HistoryListActivityV2.class);
                break;
            //点击关闭欢迎按钮
            case R.id.btn_close_welcome:
                mWelcomeLl.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_heart_state:
                break;
            //点击设置心率设备
            case R.id.ll_heartbeat:
                if (canUseBle) {
                    startActivity(BleSettingsActivity.class);
                } else {
                    showNotSupportBleDialog();
                }
                break;
            //点击准备跑步
            case R.id.btn_preper_run_start:
                startRunning();
                break;
            //点击赛事区域
            case R.id.ll_race:
                onClickRaceLl();
                break;
            //跑步设置
            case R.id.ll_setting:
                MaidianData.saveMaidian(getActivity(), new DBEntityMaidian(System.currentTimeMillis() + "",
                        AppEnum.MaidianType.RunningSet, TimeUtils.NowTime()));
                startActivity(RunSettingsActivity.class);
                break;
        }
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
                mHeartStateIv.setImageResource(R.drawable.icon_heart_enable);
                mHeartBeatTv.setText("已连接");
                mHeartBeatTv.setTextColor(Color.parseColor("#3c3c39"));
                break;
            case BleManagerService.MSG_DISCONNECT:
                mHeartStateIv.setImageResource(R.drawable.icon_heart_disable);
                mHeartBeatTv.setText("未连接");
                mHeartBeatTv.setTextColor(Color.parseColor("#bbbbbb"));
                break;
            case BleManagerService.MSG_NEW_HEARTBEAT:
                mHeartStateIv.setImageResource(R.drawable.icon_heart_enable);
                mHeartBeatTv.setText(event.heartbeat + "");
                mHeartBeatTv.setTextColor(Color.parseColor("#3c3c39"));
                break;
            case BleManagerService.MSG_FIND_CONNECT:
                if (event.connectEntity == null) {
                    //断开之前的连接
                    Intent clearIntent = new Intent(getActivity(), BleManagerService.class);
                    clearIntent.putExtra("CMD", BleManagerService.CMD_CLEAR_ALL);
                    getActivity().startService(clearIntent);
                    //增加一个新的连接
                    Intent connectIntent = new Intent(getActivity(), BleManagerService.class);
                    connectIntent.putExtra("CMD", BleManagerService.CMD_ADD_NEW_CONNECT);
                    connectIntent.putExtra("address", mLastDeviceMac);
                    getActivity().startService(connectIntent);
                }
                break;
        }
    }


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
                mHandler.removeMessages(MSG_LOC_ERROR);
            }
        } else {
            mSearchTv.setText(R.string.Tv_PreperRunActivity_Gps_Search);
            mTipTv.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void initParams() {
        mSeccessCount = 0;
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
        //警告标记
        int phoneType = PreferencesData.getPhoneType(getActivity());
        if (!PreferencesData.getHasShowPhoneTypeWarning(getActivity()) &&
                (phoneType == AppEnum.PhoneType.HUAWEI
                        || phoneType == AppEnum.PhoneType.XIAOMI
                        || phoneType == AppEnum.PhoneType.OPPO)) {
            mWarningV.setVisibility(View.VISIBLE);
            PreferencesData.setHasShowPhoneTypeWarning(getActivity(), true);
        }
        mSearchPv.start();
        mUserEntity = LocalApplication.getInstance().getLoginUser(getActivity());
        if (mUserEntity != null && !mUserEntity.getRunInfo().equals("")) {
            mPersonRecordEntity = JSON.parseObject(mUserEntity.getRunInfo(), GetPersonRecordResult.class);
        }
        //设置闹钟数量
        badgeView = new BadgeView(getActivity(), mClockBtn);
        badgeView.setTextSize(8);
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
        mDialogBuilder = new MyDialogBuilderV1();
        initLocationClient();
        initBLE();
    }

    @Override
    protected void causeGC() {
        mHandler.removeMessages(MSG_POST_BEGIN_RUN);
        mHandler.removeMessages(MSG_POST_RUNNING_INFO);
        mHandler.removeMessages(MSG_LOC_ERROR);
    }

    @Override
    protected void onVisible() {
        // 启动定位
        locationClient.startLocation();
        mHandler.sendEmptyMessageDelayed(MSG_LOC_ERROR, IntervalError);
        EventBus.getDefault().register(this);
        //获取个人最新信息
        if (LocalApplication.getInstance().getLoginUser(getActivity()).userId == AppEnum.DEFAULT_USER_ID) {
            String recordStr = WorkoutData.getWorkOutCountAndLength(getActivity(), AppEnum.DEFAULT_USER_ID);
            mPersonRecordEntity = new GetPersonRecordResult();
            mPersonRecordEntity.totallength = Float.parseFloat(recordStr.split(":")[1]);
            mPersonRecordEntity.totalcount = Integer.parseInt(recordStr.split(":")[0]);
            updateRunningInfoViews();
        } else {
            postGetUserRunInfo();
        }
    }

    @Override
    protected void onInVisible() {
        EventBus.getDefault().unregister(this);
        //取消定位
        locationClient.stopLocation();
    }


    /**
     * 初始化位置信息
     */
    private void initLocationClient() {
        locationClient = new AMapLocationClient(getActivity());
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
     * 初始化蓝牙
     */
    private void initBLE() {
        BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // 检查设备上是否支持蓝牙
        if (mBluetoothAdapter == null) {
            return;
        }
        // 检查当前手机是否支持ble 蓝牙,如果不支持
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            mHeartBeatTv.setText("不可用");
            mHeartBeatTv.setTextColor(Color.parseColor("#bbbbbb"));
            return;
        }
        if (!PreferencesData.getBleEnable(getActivity())) {
            return;
        }
        // 若蓝牙需要判断 , 且连接不正常
        if (!mBluetoothAdapter.isEnabled() ||
                PreferencesData.getBlueToothDeviceMac(getActivity()).equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            mDialogBuilder.showBlueToothSetDialog(getActivity(), "提醒", "是否要连接蓝牙设备?", "下次", "不再提示");
            mDialogBuilder.setBluetoothSetListener(new MyDialogBuilderV1.BluetoothSetDialogListener() {
                @Override
                public void onLeftBtnClick() {

                }

                @Override
                public void onRightBtnClick() {
                    PreferencesData.setBleEnable(getActivity(), false);
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
            mLastDeviceMac = PreferencesData.getBlueToothDeviceMac(getActivity());
            if (!mLastDeviceMac.equals(AppEnum.DEFAULT_BLE_MAC)) {
                Intent intent = new Intent(getActivity(), BleManagerService.class);
                intent.putExtra("CMD", BleManagerService.CMD_GET_CONNECT);
                intent.putExtra("address", mLastDeviceMac);
                getActivity().startService(intent);
            }
        }
    }

    /**
     * 获取位置信息
     *
     * @param latitude
     * @param longitude
     */
    private void postGetLocationInfo(final double latitude, final double longitude) {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestParamsBuilder.buildGetBeginRunInfoRequest(getActivity(), URLConfig.URL_GET_BEGIN_RUNINFO, latitude, longitude);
                x.http().post(params, new Callback.CommonCallback<REBase>() {
                    @Override
                    public void onSuccess(REBase reBase) {
                        if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                            result = JSON.parseObject(reBase.result, GetBeginRunInfoResult.class);
                            if (mHandler != null) {
                                mHandler.sendEmptyMessage(MSG_POST_BEGIN_RUN);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable, boolean b) {

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
            mWelcomeLl.setVisibility(View.VISIBLE);
            mWelcomeTv.setText("欢迎来到水星，从这里开始你的征程吧");
        }
        if (NumUtils.getTotalLengthLevel(mTempLength) != NumUtils.getTotalLengthLevel(mPersonRecordEntity.totallength)) {
            mWelcomeLl.setVisibility(View.VISIBLE);
            mWelcomeTv.setText("欢迎来到" + NumUtils.getCardTypeString(mPersonRecordEntity.totallength));
            startBgAnim();
        }
        mTempLength = mPersonRecordEntity.totallength;
    }

    /**
     * 更新和位置相关的页面
     */
    private void updateLocationViews() {
        //位置信息
        mLocInfoLl.setVisibility(View.VISIBLE);
        String locInfo = " " + result.city + " " + result.weather + " " + result.temp + "℃ 空气 " + result.airquality;
        mLocationInfoTv.setText(locInfo);
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
     * 获取用户跑步信息
     */
    private void postGetUserRunInfo() {
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = RequestParamsBuilder.buildGetPersonRecordRequest(getActivity(), URLConfig.URL_GET_PERSON_RECORDS, mUserEntity.userId);
                x.http().post(params, new Callback.CommonCallback<REBase>() {
                    @Override
                    public void onSuccess(REBase reBase) {
                        if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                            mPersonRecordEntity = JSON.parseObject(reBase.result, GetPersonRecordResult.class);
                            mUserEntity.runInfo = reBase.result;
                            LocalApplication.getInstance().setLoginUser(mUserEntity);
                            UserData.updateUser(getActivity(), mUserEntity);
                            if (mHandler != null) {
                                mHandler.sendEmptyMessage(MSG_POST_RUNNING_INFO);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable, boolean b) {

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


    /**
     * 开始星球动画
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
     * 开始跑
     */
    private void startRunning() {
        if (!mHasLocation) {
            T.showShort(getActivity(), "请等待GPS定位");
            return;
        }
        // 启动定位
        locationClient.stopLocation();
        //检查用户信息
        DBUserEntity userEntity = LocalApplication.getInstance().getLoginUser(getActivity());
        if (userEntity.height == 0) {
            userEntity.height = 174;
        }
        if (userEntity.weight == 0) {
            userEntity.weight = 60;
        }
        LocalApplication.getInstance().setLoginUser(userEntity);
        UserData.updateUser(getActivity(), userEntity);
        if (userEntity.getUserId() == AppEnum.DEFAULT_USER_ID) {
            MaidianData.saveMaidian(getActivity(), new DBEntityMaidian(System.currentTimeMillis() + "", AppEnum.MaidianType.VisitorRunning, TimeUtils.NowTime()));
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
     * 个人直播间分享
     */
    private void getShareLiveRoom() {
        mDialogBuilder.showProgressDialog(getActivity(), "正在获取分享信息..", false);
        // 发送请求
        x.task().post(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new MyRequestParamsV2(getActivity(), URLConfig.URL_LIVE_ROOM_SHARE);
                x.http().post(params, new Callback.CommonCallback<REBase>() {
                    @Override
                    public void onSuccess(REBase reBase) {
                        if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                            Message msg = new Message();
                            msg.what = MSG_POST_SHARE_OK;
                            msg.obj = reBase.result;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = MSG_POST_ERROR;
                            msg.obj = reBase.errormsg;
                            mHandler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable, boolean b) {
                        Message msg = new Message();
                        msg.what = MSG_POST_ERROR;
                        msg.obj = HttpExceptionHelper.getErrorMsg(throwable);
                        mHandler.sendMessage(msg);
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

    /**
     * 分享直播间
     */
    private void shareLiveRoom(final String Response) {
        final GetWxInviteResult result = JSON.parseObject(Response, GetWxInviteResult.class);
        final WxShareManager share = new WxShareManager(getActivity());
        mDialogBuilder.setShareWxFriendDialog(getActivity(), "分享跑步直播到");
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
    }


    /**
     * 显示不支持BLE设备的对话框
     */
    private void showNotSupportBleDialog() {
        SpannableString spanStr = new SpannableString("本手机不支持连接心率设备");
        mDialogBuilder.showSimpleMsgDialog(getActivity(), "心率设备", spanStr);
    }

}
