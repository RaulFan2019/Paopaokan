package com.app.pao.activity.run;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.HistoryInfoPagerAdapter;
import com.app.pao.adapter.LivePagerAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.MapEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.event.EventComment;
import com.app.pao.entity.event.EventSwitchFragment;
import com.app.pao.entity.model.LiveHeartrateEntity;
import com.app.pao.entity.model.LiveLap;
import com.app.pao.entity.model.LiveLocation;
import com.app.pao.entity.model.LivePlayBackLocEntityV2;
import com.app.pao.entity.model.LiveSplite;
import com.app.pao.entity.model.LiveWorkoutEntity;
import com.app.pao.entity.network.GetPlaybackCameraListResult;
import com.app.pao.entity.network.LiveSocial;
import com.app.pao.fragment.run.LiveBasicFragment;
import com.app.pao.fragment.run.LiveSplitFragment;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.CustomViewPager;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.TabPageIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/12.
 */
@ContentView(R.layout.activity_live_v3)
public class LiveActivityV3 extends BaseAppCompActivity implements View.OnClickListener {

    private static final String TAG = "LiveActivityV3";

    public static final int BITRATE_3GPP = 20 * 1024 * 8; // bits/sec
    public static final int BITRATE_AMR = 2 * 1024 * 8;

    /* contains about map */
    private static final int MSG_DRAW_CAMERA = 0x02;
    private static final int MSG_HAS_NEW_SPLIT = 0x03;
    private static final int MSG_PLAY_VIEW = 0x04;
    private static final int MSG_UPDATE_LIVE_DATA_OVER = 0x05;//更新数据结束
    private static final int MSG_ZOOM_MAP = 0x06;//地图放大
    private static final int MSG_POST = 0x07;//请求数据
    private static final int MSG_UPDATE_SOCIAL_VIEW = 0x08;
    private static final int MSG_HAS_NEW_VIDEO = 0x09;//有新的视频
    private static final int MSG_UPDATE_WORKOUT_INFO = 0x10;//更新workout信息

    /* contains about play*/
    private static final int PLAY_TIMER_INTERVAL = 1000;//播放速度
    private static final int REVIEW_POLYLINE_TIME = 60;//合并线路时间
    private static final long PLAY_DALAY = 67000;// 播放延迟
    private static final long POST_INTERVAL = 3000;//请求间隔
    private static final int POST_TIME_OUT = 30 * 1000;

    private LivePagerAdapter.Tab[] mItems;
    private LivePagerAdapter mPagerAdapter;//Tab适配器

    /* local data about user */
    private int mUserId;// 直播id
    private String mNickName;// 被直播人名称
    private int mGender;// 用户性别
    private String mAvatar;// 用户头像

    private int mLastTId = 0;//最后一个点赞ID
    private int mLastCommentId = 0;//最后一个评论ID

    /* local data about live */
    private List<GetPlaybackCameraListResult.VideoEntity> mVideoList = new ArrayList<>();//视频列表
    private List<LivePlayBackLocEntityV2> mPlayerLoc = new ArrayList<>();//播放点
    private List<LiveSplite> mSplitList = new ArrayList<>();//分段列表
    private List<LiveHeartrateEntity> mHeartList = new ArrayList<>();//心率列表

    /* contains about running stutas*/
    private static final int RUNNING_STATUS_RUNNING = 1;
    private static final int RUNNING_STATUS_FINISH = 2;
    private static final int RUNNING_STATUS_PAUSE = 3;

    private int mHeartRate;//心率数据
    private int mHasGiveThumbUp = AppEnum.hasGiveThumbup.YES;

    private boolean mRunningPause = false;
    private boolean mFirstUpdate = true;
    private int mRunningState;//跑步状态
    private long mStartTime;//跑步开始时间
    private String mLastLiveTime = "";//上次获取的直播时间
    public int mWorkoutId = -1;//跑步的workoutId
    private int mReviewTimer;//合并线路的计时器
    private int mLastPlayerIndex = 0;//上次播放的Index

    /* local data about zoom*/
    private double mMinLaLng = 0;
    private double mMaxLaLng = 0;
    private double mMinLoLng = 0;
    private double mMaxLoLng = 0;

    /* 插值算法相关 */
    private int lastTimeofset;
    private double lastLatitude = 0;
    private double lastLongitude = 0;
    private float lastLength;


    /* task */
    private Handler mPlayerTimerHandler;//播放Handler
    private Runnable mPlayerTimerTicker;//执行播放
    private doParseLiveDataTask mParseLiveDataTask;


    @ViewInject(R.id.tbi_history)
    private TabPageIndicator mTpi;//Tab控件
    @ViewInject(R.id.vp_history)
    public CustomViewPager mViewPager;//ViewPager
    @ViewInject(R.id.ll_activity_base)
    private LinearLayout mBaseLl;

    private android.widget.TextView mLoadingTv;

    /* fragment */
    private LiveBasicFragment mBasicFragment;
    private LiveSplitFragment mSplitFragment;

    /**
     * 根据时间变化VIEW的handler
     */
    private Handler mTimerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_PLAY_VIEW) {
                updateViewByLiveData();
            }
        }
    };

    /**
     * 直播使用的Handler
     */
    private Handler mLiveHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //更新视频列表
            if (msg.what == MSG_HAS_NEW_VIDEO) {
                updateVideoList();
                //有新的分段信息
            } else if (msg.what == MSG_HAS_NEW_SPLIT) {
                updateNewSplitOnView();
                //更新直播数据完成
            } else if (msg.what == MSG_UPDATE_LIVE_DATA_OVER) {
                updateRunningStatus();
                //请求数据
            } else if (msg.what == MSG_POST) {
                postLiveData();
                //更新评论信息
            } else if (msg.what == MSG_UPDATE_SOCIAL_VIEW) {
                List<LiveSocial> mSocialList = (List<LiveSocial>) msg.obj;
                updateSocialViews(mSocialList);
                //更新workout基本信息
            } else if (msg.what == MSG_UPDATE_WORKOUT_INFO) {
                updateWorkoutInfo();
            }
        }
    };

    @Override
    @OnClick({R.id.title_bar_left_menu})
    public void onClick(View v) {
        if (v.getId() == R.id.title_bar_left_menu) {
            finish();
        }
    }

    /**
     * Fragment 切换事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventSwitchFragment event) {
        if (event.type == EventSwitchFragment.TYPE_HISTORY) {
            if (event.event == EventSwitchFragment.EVENT_HISTORY_SPLIT) {
                mViewPager.setCurrentItem(1);
            }
        }
    }

    @Override
    protected void initData() {
        CheckNewData = false;
        EventBus.getDefault().register(this);
        initUserData();
        initPlayerTimer();
        mBasicFragment = LiveBasicFragment.newInstance();
        mSplitFragment = LiveSplitFragment.newInstance();
    }

    @Override
    protected void initViews() {
        /* TAB */
        mItems = new LivePagerAdapter.Tab[]{LivePagerAdapter.Tab.BASIC, LivePagerAdapter.Tab.SPLIT};
        mPagerAdapter = new LivePagerAdapter(getSupportFragmentManager(), mItems, new Fragment[]{mBasicFragment, mSplitFragment});
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        mTpi.setViewPager(mViewPager);
        mTpi.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
//                if (position == 0) {
//                    mViewPager.setNoScroll(true);
//                } else {
//                    mViewPager.setNoScroll(false);
//                }
                imm.hideSoftInputFromWindow(mBaseLl.getWindowToken(), 0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
        mViewPager.setNoScroll(true);
        mViewPager.setCurrentItem(0);

        mBaseLl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                //比较Activity根布局与当前布局的大小
                int heightDiff = mBaseLl.getRootView().getHeight() - mBaseLl.getHeight();
                if (heightDiff > 100) {
                    //大小超过100时，一般为显示虚拟键盘事件
                } else {
                    //大小小于100时，为不显示虚拟键盘或虚拟键盘隐藏
                    DBUserEntity userEntity = LocalApplication.getInstance().getLoginUser(mContext);
                    EventBus.getDefault().post(new EventComment(0, lastLatitude, lastLongitude, 0,
                            userEntity.nickname, userEntity.gender, userEntity.avatar, userEntity.name));
                }
            }
        });
    }

    @Override
    protected void doMyOnCreate() {
        postLiveData();
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {
        EventBus.getDefault().unregister(this);
        cancelHandler();
        causeGC();
    }

    /**
     * 取消Handler事件
     */
    private void cancelHandler() {
        if (mLiveHandler != null) {
            mLiveHandler.removeMessages(MSG_HAS_NEW_SPLIT);
            mLiveHandler.removeMessages(MSG_DRAW_CAMERA);
            mLiveHandler.removeMessages(MSG_UPDATE_LIVE_DATA_OVER);
            mLiveHandler.removeMessages(MSG_POST);
            mLiveHandler.removeMessages(MSG_UPDATE_SOCIAL_VIEW);
            mLiveHandler.removeMessages(MSG_HAS_NEW_VIDEO);
            mLiveHandler = null;
        }
        if (mPlayerTimerHandler != null) {
            mPlayerTimerHandler.removeCallbacks(mPlayerTimerTicker);
            mPlayerTimerHandler = null;
        }
        if (mTimerHandler != null) {
            mTimerHandler.removeMessages(MSG_PLAY_VIEW);
            mTimerHandler = null;
        }
    }

    /**
     * 内存释放
     */
    private void causeGC() {
        mPlayerLoc.clear();
        mSplitList.clear();
        mVideoList.clear();
    }

    /**
     * 初始化用户信息
     */
    private void initUserData() {
        // 获取用户基础信息
        Intent i = getIntent();
        mUserId = i.getIntExtra("userId", 0);
        mNickName = i.getStringExtra("userNickName");
        mGender = i.getIntExtra("userGender", 1);
        mAvatar = i.getStringExtra("avatar");
    }

    /**
     * 初始化播放Timer
     */
    private void initPlayerTimer() {
        mPlayerTimerHandler = new Handler();
        mPlayerTimerTicker = new Runnable() {
            public void run() {
                long now = SystemClock.uptimeMillis();
                long next = now + (PLAY_TIMER_INTERVAL - now % PLAY_TIMER_INTERVAL);
                if (mTimerHandler != null) {
                    mTimerHandler.sendEmptyMessage(MSG_PLAY_VIEW);
                }
                if (mPlayerTimerHandler != null) {
                    mPlayerTimerHandler.postAtTime(mPlayerTimerTicker, next);
                }
            }
        };
    }


    /**
     * 播放直播数据
     */
    private void updateViewByLiveData() {
//        Log.v(TAG, "mPlayerLoc.size():" + mPlayerLoc.size());
        // 准备画线
        PolylineOptions polylineOption = new PolylineOptions();
        polylineOption.width(MapEnum.WIDTH_POLYLINE)
                .color(MapEnum.COLOR_RUNNING_POLYLINE).zIndex(MapEnum.ZINDEX_POLYLINE);
        ;
        LivePlayBackLocEntityV2 lastPlayLoc = null;
        if (mReviewTimer > REVIEW_POLYLINE_TIME) {
            mBasicFragment.resetMapLineData();
            mLastPlayerIndex = 0;
            mReviewTimer = 0;
        }
        // 若存在没有画的点
        if (mPlayerLoc.size() > 1) {
            List<LivePlayBackLocEntityV2> tempList = mPlayerLoc.subList(mLastPlayerIndex, mPlayerLoc.size() - 1);
            if (tempList != null && tempList.size() > 1) {
                long playTimeEnd = (System.currentTimeMillis() - mStartTime - PLAY_DALAY) / 1000;
//                Log.v(TAG,"playTimeEnd:" + playTimeEnd);
                int listSize = tempList.size();
                for (int i = 0; i < listSize; i++) {
                    LivePlayBackLocEntityV2 loc = tempList.get(i);
                    // 获取需要播放的点
                    if (loc.playTime < playTimeEnd) {
                        polylineOption.add(new LatLng(loc.getLatitude(), loc.getLongitude()));
                        lastPlayLoc = loc;
                    } else {
                        break;
                    }
                }
            }

            // 若点的数量大于1 , 更新页面
            if (polylineOption.getPoints().size() > 1) {
                mLastPlayerIndex += (polylineOption.getPoints().size() - 1);
                mReviewTimer++;
                mBasicFragment.drawNewLine(polylineOption, new LatLngBounds(new LatLng(mMinLaLng, mMinLoLng), new LatLng(mMaxLaLng, mMaxLoLng)));
                if (lastPlayLoc != null) {
                    mBasicFragment.updateLocationData(lastPlayLoc, mHeartRate);
                }
            }
        }
    }

    /**
     * 有新的split更新，并表现在地图上
     */
    private void updateNewSplitOnView() {
        mBasicFragment.updateNewSplitOnView(mSplitList);
        String AvgPace = "- -";
        if (mPlayerLoc.size() > 0) {
            AvgPace = TimeUtils.formatSecondsToSpeedTime((long) (mPlayerLoc.get(mLastPlayerIndex).Duration * 1000 / mPlayerLoc.get(mLastPlayerIndex).length));
        }
        mSplitFragment.updateSplitView(mSplitList, AvgPace);
    }

    /**
     * 更新视频信息
     */
    private void updateVideoList() {
        mBasicFragment.updateVideoList(mVideoList);
    }


    /**-- 处理数据的线程  --**/
    /**
     * 启用线程处理历史数据
     */
    class doParseLiveDataTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            if (params[0] != null) {
                try {
                    LiveWorkoutEntity workout = JSON.parseObject(params[0], LiveWorkoutEntity.class);
                    mPlayerLoc.addAll(updateLiveData(workout));
                }catch (JSONException exception){}

            }
            return params[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mLiveHandler != null) {
                mLiveHandler.sendEmptyMessage(MSG_UPDATE_LIVE_DATA_OVER);
            }
        }
    }

    /**
     * 更新最新的跑步状态
     */
    private void updateRunningStatus() {
        // 启动计时线程，定时更新
        if (mFirstUpdate) {
            mPlayerTimerTicker.run();
            mFirstUpdate = false;
        }
        if (mRunningState == RUNNING_STATUS_FINISH) {
            showRunFinishDialog();
            return;
        }
        if (mRunningPause && mRunningState == RUNNING_STATUS_RUNNING) {
            mRunningPause = false;
        }
        if (mRunningState == RUNNING_STATUS_PAUSE && !mRunningPause) {
            mRunningPause = true;
        }
        mBasicFragment.updatePaceChart(mPlayerLoc);
        if (mHeartList.size() > 0) {
            mBasicFragment.updateHeartChart(mHeartList);
        }
        if (mLiveHandler != null) {
            mLiveHandler.sendEmptyMessageDelayed(MSG_POST, POST_INTERVAL);
        }
    }

    /*-- 复杂的数据处理 --*/

    /**
     * 返回需要直播的数据
     *
     * @param workout
     * @return
     */
    private List<LivePlayBackLocEntityV2> updateLiveData(final LiveWorkoutEntity workout) {
        List<LivePlayBackLocEntityV2> result = new ArrayList<LivePlayBackLocEntityV2>();
        mRunningState = workout.status;
        mStartTime = TimeUtils.getAfrerTimeNum(workout.starttime, 0);
        if (mFirstUpdate) {
            firstUpdateLiveData(result, workout);
        } else {
            normalUpdateLiveData(result, workout);
        }
        return result;
    }

    /**
     * 首次更新直播数据
     */
    private void firstUpdateLiveData(final List<LivePlayBackLocEntityV2> result,
                                     final LiveWorkoutEntity workout) {
        LiveLocation tempLoc = null;
        mWorkoutId = workout.getId();
        mHasGiveThumbUp = workout.hasGiveThumbup;

        mLiveHandler.sendEmptyMessage(MSG_UPDATE_WORKOUT_INFO);
        //遍历lap
        for (LiveLap lap : workout.lap) {
            if (lap.locationdata == null) {
                continue;
            }
            //遍历lap中的点
            int locSize = lap.locationdata.size();
            for (int i = 0; i < locSize; i++) {
                tempLoc = lap.locationdata.get(i);
                LivePlayBackLocEntityV2 locEntity = new LivePlayBackLocEntityV2(tempLoc.latitude,
                        tempLoc.longitude, workout.duration, workout.length, tempLoc.timeoffset,
                        tempLoc.speed, lap.starttime);
                result.add(locEntity);
                if (tempLoc.getLatitude() < mMinLaLng || mMinLaLng == 0) {
                    mMinLaLng = tempLoc.getLatitude();
                }
                if (tempLoc.getLatitude() > mMaxLaLng || mMaxLaLng == 0) {
                    mMaxLaLng = tempLoc.getLatitude();
                }
                if (tempLoc.getLongitude() < mMinLoLng || mMinLoLng == 0) {
                    mMinLoLng = tempLoc.getLongitude();
                }
                if (tempLoc.getLongitude() > mMaxLoLng || mMaxLoLng == 0) {
                    mMaxLoLng = tempLoc.getLongitude();
                }
            }

            // 获取最后一个心率信息
            if (lap.heartratedata != null && lap.heartratedata.size() > 0) {
                mHeartList.addAll(lap.heartratedata);
                mHeartRate = lap.heartratedata.get(lap.heartratedata.size() - 1).getBpm();
            }
        }
        if (tempLoc != null) {
            lastTimeofset = tempLoc.timeoffset;
            lastLatitude = tempLoc.latitude;
            lastLongitude = tempLoc.longitude;
            lastLength = workout.length;

            mLastLiveTime = TimeUtils.getAfrerTime(workout.getStarttime(), tempLoc.timeoffset);
        }
        //更新分段信息
        if (workout.splits != null && workout.splits.size() > 0) {
            mSplitList.addAll(workout.splits);
            mLiveHandler.sendEmptyMessage(MSG_HAS_NEW_SPLIT);
        }
        //更新视频信息
        if (workout.video != null && workout.video.size() > 0) {
            mVideoList.addAll(workout.video);
            mLiveHandler.sendEmptyMessage(MSG_HAS_NEW_VIDEO);
        }
        //更新评论信息
        if (workout.social != null && workout.social.size() > 0) {
            for (int i = 0, size = workout.social.size(); i < size; i++) {
                if (workout.social.get(i).type == 1) {
                    mLastTId = workout.social.get(i).id;
                } else {
                    mLastCommentId = workout.social.get(i).id;
                }
            }
            if (mLiveHandler != null) {
                Message message = new Message();
                message.obj = workout.social;
                message.what = MSG_UPDATE_SOCIAL_VIEW;
                mLiveHandler.sendMessage(message);
            }
        }
    }

    /**
     * 普通更新直播数据
     */
    private void normalUpdateLiveData(final List<LivePlayBackLocEntityV2> result,
                                      final LiveWorkoutEntity workout) {
        LiveLocation tempLoc = null;
        //点的数量
        int locSize = 0;
        for (LiveLap lap : workout.lap) {
            if (lap.locationdata != null) {
                locSize += lap.locationdata.size();
            }
        }
        //若本次更新没有点
        if (locSize == 0) {
            return;
        }
        //每个点的距离变化参数
        float lengthDes = (workout.length - lastLength) / locSize;
        //遍历lap
        for (LiveLap lap : workout.lap) {
            if (lap.locationdata == null) {
                continue;
            }
            if (lastLatitude == 0) {
                lastLatitude = lap.locationdata.get(0).latitude;
            }
            if (lastLongitude == 0) {
                lastLongitude = lap.locationdata.get(0).longitude;
            }
            for (int i = 0; i < locSize; i++) {
                tempLoc = lap.locationdata.get(i);
                int des = tempLoc.timeoffset - lastTimeofset;
                if (des == 0) {
                    continue;
                }
                double laDes = tempLoc.latitude - lastLatitude;
                double loDes = tempLoc.longitude - lastLongitude;
                for (int j = 0; j < des; j++) {
                    lastLength += lengthDes / des;
                    lastLatitude += laDes / des;
                    lastLongitude += loDes / des;
                    lastTimeofset += 1;
                    LivePlayBackLocEntityV2 locEntity = new LivePlayBackLocEntityV2(lastLatitude,
                            lastLongitude, lastTimeofset, lastLength, lastTimeofset,
                            tempLoc.speed, lap.starttime);
                    result.add(locEntity);
                }
                if (tempLoc.getLatitude() < mMinLaLng || mMinLaLng == 0) {
                    mMinLaLng = tempLoc.getLatitude();
                }
                if (tempLoc.getLatitude() > mMaxLaLng || mMaxLaLng == 0) {
                    mMaxLaLng = tempLoc.getLatitude();
                }
                if (tempLoc.getLongitude() < mMinLoLng || mMinLoLng == 0) {
                    mMinLoLng = tempLoc.getLongitude();
                }
                if (tempLoc.getLongitude() > mMaxLoLng || mMaxLoLng == 0) {
                    mMaxLoLng = tempLoc.getLongitude();
                }
            }

            // 获取最后一个心率信息
            if (lap.heartratedata != null && lap.heartratedata.size() > 0) {
                mHeartList.addAll(lap.heartratedata);
                mHeartRate = lap.heartratedata.get(lap.heartratedata.size() - 1).getBpm();
            }
        }
        if (tempLoc != null) {
            lastTimeofset = tempLoc.timeoffset;
            lastLatitude = tempLoc.latitude;
            lastLongitude = tempLoc.longitude;
            lastLength = workout.length;
            mLastLiveTime = TimeUtils.getAfrerTime(workout.getStarttime(), tempLoc.timeoffset);
        }
        //更新分段信息
        if (workout.splits != null && workout.splits.size() > 0) {
            mSplitList.addAll(workout.splits);
            if(mLiveHandler != null){
                mLiveHandler.sendEmptyMessage(MSG_HAS_NEW_SPLIT);
            }
        }
        //更新评论信息
//        Log.v(TAG,"workout.social.size():" + workout.social.size());
        if (workout.social != null && workout.social.size() > 0) {
            for (int i = 0, size = workout.social.size(); i < size; i++) {
                if (workout.social.get(i).type == 1) {
                    mLastTId = workout.social.get(i).id;
                } else {
                    mLastCommentId = workout.social.get(i).id;
                }
            }
            if (mLiveHandler != null) {
                Message message = new Message();
                message.obj = workout.social;
                message.what = MSG_UPDATE_SOCIAL_VIEW;
                mLiveHandler.sendMessage(message);
            }
        }
        //更新视频信息
        if (workout.video != null && workout.video.size() > 0) {
            mVideoList.addAll(workout.video);
            mLiveHandler.sendEmptyMessage(MSG_HAS_NEW_VIDEO);
        }
    }


    /*--  post fun --*/

    /**
     * 获取直播数据
     */
    private void postLiveData() {
        HttpUtils http = new HttpUtils(POST_TIME_OUT);
        String POST_URL = URLConfig.URL_GET_LATEST_WORKOUT_DATA;
//        Log.v(TAG,"mLastTId:" + mLastTId);
//        Log.v(TAG,"mLastCommentId:" + mLastCommentId);
        // params
        RequestParams params = RequestParamsBuild.buildGetLatestWorkoutDataRequest(mContext, mLastLiveTime,
                mSplitList.size() - 1, mUserId, mLastTId, mLastCommentId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mLiveHandler != null && mRunningState != RUNNING_STATUS_FINISH) {
                    mLiveHandler.sendEmptyMessageDelayed(MSG_POST, POST_INTERVAL);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mParseLiveDataTask = new doParseLiveDataTask();
                mParseLiveDataTask.execute(Response);
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mLiveHandler != null && mRunningState != RUNNING_STATUS_FINISH) {
                    mLiveHandler.sendEmptyMessageDelayed(MSG_POST, POST_INTERVAL);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 更新评论信息
     */
    private synchronized void updateSocialViews(final List<LiveSocial> socials) {
//        Log.v(TAG,"updateSocialViews");
        mBasicFragment.updateSocialView(socials);
    }

    /**
     * 更新workout基本信息
     */
    private void updateWorkoutInfo() {
        mBasicFragment.updateWorkoutInfo(mWorkoutId);
    }

    /**-- Dialog  --**/
    /**
     * 若跑步结束显示跑步结束的Dialog
     */
    private void showRunFinishDialog() {
        mDialogBuilder.showChoiceTwoBtnDialog(mContext, "提示", mNickName + " 已结束跑步,是否退出直播?"
                , "取消", "退出");
        mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
            @Override
            public void onRightBtnClick() {
                finish();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onLeftBtnClick() {

            }
        });
    }

    /* Get or Set */

    public String getAvatar() {
        return mAvatar;
    }

    public String getNickName() {
        return mNickName;
    }

    public int getWorkoutId() {
        return mWorkoutId;
    }
}
