package com.app.pao.activity.run;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseActivityV3;
import com.app.pao.activity.workout.HistoryInfoActivityV2;
import com.app.pao.config.AppEnum;
import com.app.pao.config.MapEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.LocationData;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.db.DBEntityLocation;
import com.app.pao.entity.db.DBEntityWorkout;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.network.GetRunningLiveRoomResult;
import com.app.pao.entity.network.REBase;
import com.app.pao.fragment.run.RunPauseFragment;
import com.app.pao.fragment.run.RunRunningFragment;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.api.MyRequestParamsV2;
import com.app.pao.network.utils.HttpExceptionHelper;
import com.app.pao.service.RunningServiceV2;
import com.app.pao.ui.dialog.StopWalkPopupWindow;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.TTsUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Raul.Fan on 2016/9/11.
 */
public class RunningActivityV2 extends BaseActivityV3 {

    /* contains */
    private static final String TAG = "RunningActivityV2";

    private static final int MSG_ANIM_BIG = 0x01;
    private static final int MSG_ANIM_FINISH = 0x02;
    private static final int MSG_ZOOM_MAP = 0x04;

    private static final long FreshInterval = 1000 * 2;//刷新抓取的时间间隔

    /* local view */
    @BindView(R.id.map_running)
    MapView mMapView;
    @BindView(R.id.btn_run_live_room)
    Button mLiveRoomBtn;
    @BindView(R.id.btn_running_control)
    Button mControlBtn;
    @BindView(R.id.btn_running_map)
    Button mMapBtn;
    @BindView(R.id.tv_control)
    TextView mControlTv;
    @BindView(R.id.ll_basic)
    LinearLayout mBasicLl;

    @BindView(R.id.tv_count_num)
    TextView mCountTv;//倒计时文本
    @BindView(R.id.ll_count_num)
    LinearLayout mCountLl;//倒计时页面

    @BindView(R.id.ll_dialog_admin)
    LinearLayout mDialogLl;
    @BindView(R.id.ll_base)
    FrameLayout mBaseLl;

    private StopWalkPopupWindow menuWindow;

    private AMap mAMap;//地图
    private boolean mStartFromPreper = false;
    private int mRunCount;//已跑步次数

    /* local data */
    private DBUserEntity mUser;//用户信息
    private DBEntityWorkout mWorkout;//跑步信息

    //倒计时
    private Animation mBigAnimation;// 变大动画
    private int count = 4;// 计数数字

    //绘图相关
    private List<DBEntityLocation> mPointList = new ArrayList<DBEntityLocation>();
    private Marker mStartMk;// 起点
    private Marker mEndMk;// 终点
    private BitmapDescriptor mUserBitmapDes;// 用户头像
    private float mPolylineWidth;

    //当前跑步状态
    private boolean mIsShowData = true;//当前是否显示数据页面
    private boolean mIsRunning = true;//是否正在跑状态
    private long mLastLocationId = 0;// 最后一次位置序号
    private boolean mIsFirstIn = true;//是否第一次进来

    private Handler mHandler;// 刷新地图的Handler
    private Runnable mTicker;// 刷新地图的Runnable


    private RunRunningFragment mRunFragment;
    private RunPauseFragment mPauseFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_running;
    }


    /**
     * 地图放大Handler
     */
    Handler zoomHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_ZOOM_MAP) {
                if (mAMap.getCameraPosition() != null && mAMap.getCameraPosition().zoom > 16) {
                    mIsFirstIn = false;
                    if (mEndMk != null) {
                        mAMap.animateCamera(CameraUpdateFactory.changeLatLng(
                                new LatLng(mEndMk.getPosition().latitude, mEndMk.getPosition().longitude)));
                    }
                    return;
                }
                if (mIsFirstIn) {
                    mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    if (zoomHandler != null) {
                        zoomHandler.sendEmptyMessageDelayed(MSG_ZOOM_MAP, 500);
                    }
                }
            }
        }
    };

    /**
     * 动画Handler
     */
    Handler animHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ANIM_BIG:
                    count--;
                    mCountTv.setText("" + count);
                    mBigAnimation.reset();
                    mCountTv.startAnimation(mBigAnimation);
                    if (count == 0) {
                        animHandler.sendEmptyMessageDelayed(MSG_ANIM_FINISH, 1000);
                    } else {
                        animHandler.sendEmptyMessageDelayed(MSG_ANIM_BIG, 1000);
                    }
                    break;
                case MSG_ANIM_FINISH:
                    mCountTv.clearAnimation();
                    mCountLl.setVisibility(View.GONE);
                    mBasicLl.setVisibility(View.VISIBLE);
                    animHandler.removeMessages(MSG_ANIM_FINISH);
                    TTsUtils.playStartRun(RunningActivityV2.this, mRunCount);
                    setDataFragment();
                    break;
            }
        }
    };

    @OnClick({R.id.btn_run_live_room, R.id.btn_running_finish, R.id.btn_running_control, R.id.btn_running_map})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击直播按钮
            case R.id.btn_run_live_room:
                postGetLiveRoomUrl();
                break;
            //点击结束按钮
            case R.id.btn_running_finish:
                onClickFinish();
                break;
            //点击控制按钮
            case R.id.btn_running_control:
                onControlBtnClick();
                break;
            //点击地图按钮
            case R.id.btn_running_map:
                onClickMapBtn();
                break;
        }
    }

    @Override
    protected void initData() {
        mCheckNewData = false;
        //是否通过用户手动启动的
        if (getIntent().hasExtra("StartFromPreper")) {
            mStartFromPreper = true;
            mRunCount = getIntent().getIntExtra("runCount", 1);
        } else {
            mStartFromPreper = false;
        }
        mUser = LocalApplication.getInstance().getLoginUser(RunningActivityV2.this);
        mUserBitmapDes = ImageUtils.loadDefaultUserDes(RunningActivityV2.this);
        mPolylineWidth = MapEnum.WIDTH_POLYLINE;
    }

    @Override
    protected void initViews() {
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        //初始化地图
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void doMyCreate() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        //判断是否显示倒数动画
        if (!mStartFromPreper) {
            mCountLl.setVisibility(View.GONE);
            mBasicLl.setVisibility(View.VISIBLE);
            setDataFragment();
        } else {
            mBigAnimation = AnimationUtils.loadAnimation(this, R.anim.animation_tv_big);
            animHandler.sendEmptyMessageDelayed(1, 1000);
            animHandler.sendEmptyMessageDelayed(2, 10000);
        }
        //加载用户头像
        doLoadUserAvatar();
        // 启动服务
        Intent intent = new Intent(this, RunningServiceV2.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        initFreshHandler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        mHandler.removeCallbacks(mTicker);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

    @Override
    protected void causeGC() {
        mMapView.onDestroy();
    }


    /**
     * 显示Fragment
     */
    public void setDataFragment() {
        try {
            // 开启一个Fragment事务
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
            if (mIsRunning) {
                if (mRunFragment == null) {
                    mRunFragment = RunRunningFragment.newInstance();
                }
                transaction.replace(R.id.ll_running_fragment_root, mRunFragment);
                mIsShowData = true;
                mMapBtn.setBackgroundResource(R.drawable.btn_run_map_off);
                //若跑步暂停
            } else {
                if (mPauseFragment == null) {
                    mPauseFragment = RunPauseFragment.newInstance();
                }
                transaction.replace(R.id.ll_running_fragment_root, mPauseFragment);
                transaction.addToBackStack(null);
                mIsShowData = true;
                mMapBtn.setBackgroundResource(R.drawable.btn_run_map_off);
            }
            transaction.commitAllowingStateLoss();
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 加载用户头像图片
     */
    private void doLoadUserAvatar() {
        if (mUser.avatar != null) {
            String loadAvatar = "";
            if (!mUser.avatar.startsWith("http://wx.qlogo.cn/")
                    && !mUser.avatar.equals("")) {
                loadAvatar = mUser.avatar + "?imageView2/1/w/200/h/200";
            } else {
                loadAvatar = mUser.avatar;
            }
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setLoadingDrawableId(R.drawable.icon_user_photo)
                    .setFailureDrawableId(R.drawable.icon_user_photo)
                    .build();
            x.image().loadFile(loadAvatar, imageOptions, new Callback.CacheCallback<File>() {
                @Override
                public boolean onCache(File file) {
                    mUserBitmapDes = ImageUtils.loadUserDesFromFile(RunningActivityV2.this, file);
                    mEndMk = null;
                    return false;
                }

                @Override
                public void onSuccess(File file) {
                    mUserBitmapDes = ImageUtils.loadUserDesFromFile(RunningActivityV2.this, file);
                    mEndMk = null;
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
    }


    /**
     * 开始更新地图
     */
    private void initFreshHandler() {
        mHandler = new Handler();
        mTicker = new Runnable() {
            public void run() {
                if (mPointList.isEmpty()) {
                    // 过滤可以一次发送的
                    getUnDrawLocation();
                    if (mPointList.size() > 0) {
                        refreshMap();
                    }
                    mPointList.clear();
                }
                mHandler.postDelayed(mTicker, FreshInterval);
            }
        };
        mHandler.post(mTicker);
    }

    /**
     * 刷新map
     */
    private void refreshMap() {
        if (mPointList.size() > 0) {
            DBEntityLocation location = mPointList.get(mPointList.size() - 1);
            Point point = mAMap.getProjection().toScreenLocation(
                    new LatLng(location.latitude, location.longitude));
            if (mMapView.getTop() > point.y || mMapView.getBottom() < point.y
                    || mMapView.getLeft() > point.x || mMapView.getRight() < point.x) {
                mAMap.animateCamera(CameraUpdateFactory.changeLatLng(
                        new LatLng(location.latitude, location.longitude)));
            }
            drawPolyLine(mPointList);
            if (mStartMk == null) {
                DBEntityLocation startLoc = mPointList.get(0);
                mStartMk = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(new LatLng(startLoc.latitude, startLoc.longitude))
                        .title("起点")
                        .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource
                                (getResources(), R.drawable.icon_run_start)))
                        .draggable(true));
            }
            DBEntityLocation endLoc = mPointList.get(mPointList.size() - 1);
            if (mEndMk == null) {
                mEndMk = mAMap
                        .addMarker(new MarkerOptions().icon(mUserBitmapDes)
                                .anchor(0.5f, 0.5f).position(
                                        new LatLng(endLoc.latitude, endLoc.longitude))
                                .setFlat(true));
            } else {
                mEndMk.setPosition(new LatLng(endLoc.latitude, endLoc.longitude));
            }
            if (mIsFirstIn) {
                if (zoomHandler != null) {
                    zoomHandler.sendEmptyMessage(MSG_ZOOM_MAP);
                }
            }
        }
    }

    /**
     * 画线段
     */
    private void drawPolyLine(final List<DBEntityLocation> drawList) {
        PolylineOptions polylineOption = null;
        String lapStartTime = "";
        for (int i = 0; i < drawList.size(); i++) {
            DBEntityLocation location = drawList.get(i);
            //有分段
            if (!lapStartTime.equals(location.lapStartTime)) {
                //画虚线
                if (!lapStartTime.equals("")) {
                    PolylineOptions pausePolylineOption = new PolylineOptions();
                    LatLng startLoc = new LatLng(drawList.get(i - 1).latitude, drawList.get(i - 1).longitude);
                    LatLng endLoc = new LatLng(drawList.get(i).latitude, drawList.get(i).longitude);
                    pausePolylineOption.add(startLoc, endLoc)
                            .width(mPolylineWidth).setDottedLine(true).geodesic(true)
                            .color(MapEnum.COLOR_PAUSE_LINE)
                            .zIndex(MapEnum.ZINDEX_POLYLINE);
                    mAMap.addPolyline(pausePolylineOption);
                }
                //画实线
                if (polylineOption != null) {
                    polylineOption.width(mPolylineWidth)
                            .color(MapEnum.COLOR_RUNNING_POLYLINE)
                            .zIndex(MapEnum.ZINDEX_POLYLINE);
                    mAMap.addPolyline(polylineOption);
                }
                lapStartTime = location.lapStartTime;
                polylineOption = new PolylineOptions();
            }
            polylineOption.add(new LatLng(location.latitude, location.longitude));
        }

        if (polylineOption != null) {
            polylineOption.width(mPolylineWidth)
                    .color(MapEnum.COLOR_RUNNING_POLYLINE)
                    .zIndex(MapEnum.ZINDEX_POLYLINE);
            mAMap.addPolyline(polylineOption);
        }
    }

    /**
     * 获取没有画的点
     */
    private void getUnDrawLocation() {
//        Log.v(TAG,"getUnDrawLocation");
        if (mWorkout == null) {
            mWorkout = WorkoutData.getUnFinishWorkout(RunningActivityV2.this, mUser.userId);
            return;
        }
        List<DBEntityLocation> result = LocationData.getAfterLocFromWorkout(RunningActivityV2.this, mWorkout.starttime, mLastLocationId);
        if (result != null && result.size() > 0) {
            mPointList.addAll(result);
            mLastLocationId = result.get(result.size() - 1).id;
        }
    }

    /**
     * 点击地图按钮
     */
    private void onClickMapBtn() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (mIsShowData) {
            if (mIsRunning) {
                if (mRunFragment != null) {
                    transaction.remove(mRunFragment);
                }
            } else {
                if (mPauseFragment != null) {
                    transaction.remove(mPauseFragment);
                }
            }
        } else {
            if (mIsRunning) {
                if (mRunFragment == null) {
                    mRunFragment = RunRunningFragment.newInstance();
                }
                transaction.replace(R.id.ll_running_fragment_root, mRunFragment);
            } else {
                if (mPauseFragment == null) {
                    mPauseFragment = RunPauseFragment.newInstance();
                }
                transaction.replace(R.id.ll_running_fragment_root, mPauseFragment);
            }
        }
        transaction.commitAllowingStateLoss();
        mIsShowData = !mIsShowData;
    }

    /**
     * 点击控制按钮
     */
    private void onControlBtnClick() {
        if (mIsRunning) {
            setRunningStatus(RunningServiceV2.CMD_PAUSE);
            setDataFragment();
            mControlBtn.setBackgroundResource(R.drawable.btn_run_play);
            mControlTv.setText(getResources().getText(R.string.Button_RunningActivity_Continue));
            TTsUtils.playPauseRun(RunningActivityV2.this);
        } else {
            setRunningStatus(RunningServiceV2.CMD_CONTINUE);
            setDataFragment();
            mControlBtn.setBackgroundResource(R.drawable.btn_run_pause);
            mControlTv.setText(getResources().getText(R.string.Button_RunningActivity_Pause));
            TTsUtils.playContinueRun(RunningActivityV2.this);
        }
    }


    /**
     * 获取直播间的地址
     */
    private void postGetLiveRoomUrl() {
        RequestParams params = new MyRequestParamsV2(RunningActivityV2.this, URLConfig.URL_GET_APP_LIVEING_ROOM_URL);
        x.http().post(params, new Callback.CommonCallback<REBase>() {
            @Override
            public void onSuccess(REBase reBase) {
                if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                    GetRunningLiveRoomResult result = JSON.parseObject(reBase.result, GetRunningLiveRoomResult.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", result.link);
                    bundle.putInt("type", AppEnum.WebViewType.PersonalLivingRoom);
                    startActivity(LiveRoomH5Activity.class, bundle);
                } else {
                    T.showShort(RunningActivityV2.this, reBase.errormsg);
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                T.showShort(RunningActivityV2.this, HttpExceptionHelper.getErrorMsg(throwable));
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 点击跑步结束按钮
     */
    private void onClickFinish() {
        if (mIsRunning) {
            TTsUtils.playPauseRun(RunningActivityV2.this);
        }
        showFinishWorkoutDialog();
    }

    /**
     * 显示结束跑步弹框
     */
    private void showFinishWorkoutDialog() {
        setRunningStatus(RunningServiceV2.CMD_PAUSE);
        //跑步结束，获得最后跑步位置
        mWorkout = WorkoutData.getUnFinishWorkout(RunningActivityV2.this, mUser.userId);
        menuWindow = new StopWalkPopupWindow((Activity) RunningActivityV2.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow.dismiss();
                switch (v.getId()) {
                    case R.id.tv_stop_and_save:
                        finishWorkout();
                        setAnimationOut();
                        break;
                    case R.id.tv_keep_walking:
                        menuWindow.dismiss();
                        setAnimationOut();
                        keepGoing();
                        break;
                }
            }
        }, new StopWalkPopupWindow.OnDialogDismissListener() {
            @Override
            public void dialogDismiss() {
                setAnimationOut();
                keepGoing();
            }
        });

        AlphaAnimation animationIn = new AlphaAnimation(0.0f, 1.0f);
        animationIn.setDuration(300);
        animationIn.setFillAfter(true);
        mDialogLl.setAnimation(animationIn);
        animationIn.start();
        mDialogLl.setVisibility(View.VISIBLE);
        //设置设置layout在PopupWindow中显示的位置
        menuWindow.showAtLocation(mBaseLl, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 结束跑步
     */
    private void finishWorkout() {
        mWorkout = WorkoutData.getUnFinishWorkout(RunningActivityV2.this, mUser.userId);
        setRunningStatus(RunningServiceV2.CMD_FINISH);
        if (mWorkout.length < 100) {
            T.showShort(RunningActivityV2.this, "距离太短，不保存记录");
            finish();
        } else {
            TTsUtils.playFinishRun(RunningActivityV2.this);
            Bundle bundle = new Bundle();
            bundle.putString("workoutname", mWorkout.starttime);
            startActivity(HistoryInfoActivityV2.class, bundle);
            finish();
        }
    }

    /**
     * 背景色渐隐
     */
    private void setAnimationOut() {
        AlphaAnimation animationOut = new AlphaAnimation(1.0f, 0.0f);
        animationOut.setDuration(300);
        animationOut.setFillAfter(true);
        mDialogLl.setAnimation(animationOut);
        animationOut.start();
        mDialogLl.setVisibility(View.GONE);
    }

    /**
     * 当结束跑步对话框但并没有结束跑步时，跑步继续
     */
    private void keepGoing() {
        setRunningStatus(RunningServiceV2.CMD_CONTINUE);
        setDataFragment();
        mControlBtn.setBackgroundResource(R.drawable.btn_run_pause);
        mControlTv.setText(getResources().getText(R.string.Button_RunningActivity_Pause));
        TTsUtils.playContinueRun(RunningActivityV2.this);
    }

    /**
     * 改变跑步状态
     *
     * @param msg
     */
    public void setRunningStatus(int msg) {

        switch (msg) {
            //暂停跑步
            case RunningServiceV2.CMD_PAUSE:
                mIsRunning = false;
                break;
            //跑步继续
            case RunningServiceV2.CMD_CONTINUE:
                mIsRunning = true;
                break;
            //跑步结束
            case RunningServiceV2.CMD_FINISH:
                break;
        }
        Intent intent = new Intent(this, RunningServiceV2.class);
        intent.putExtra("CMD", msg);
        startService(intent);
    }
}
