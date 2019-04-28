package com.app.pao.activity.workout;

import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.HistoryInfoPagerAdapter;
import com.app.pao.adapter.HistoryInfoPagerAdapter.Tab;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.UploadData;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.event.EventComment;
import com.app.pao.entity.event.EventSwitchFragment;
import com.app.pao.entity.model.HistoryHeartrateEntity;
import com.app.pao.entity.model.HistoryInfoEntity;
import com.app.pao.entity.model.HistoryLapEntity;
import com.app.pao.entity.model.HistoryLocEntity;
import com.app.pao.entity.model.HistorySpliteEntity;
import com.app.pao.entity.network.GetPlaybackCameraListResult;
import com.app.pao.entity.network.GetSocialListResult;
import com.app.pao.entity.network.GetWorkoutFullEntity;
import com.app.pao.entity.network.GetWorkoutHeartrateEntity;
import com.app.pao.entity.network.GetWorkoutLapEntity;
import com.app.pao.entity.network.GetWorkoutLocationEntity;
import com.app.pao.entity.network.GetWorkoutSplitsEntity;
import com.app.pao.fragment.history.FriendHistoryBasicInfoFragmentV2;
import com.app.pao.fragment.history.FriendHistoryHeartrateInfoFragmentV2;
import com.app.pao.fragment.history.FriendHistorySpliteListFragmentV2;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.CustomViewPager;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TabPageIndicator;
import com.rey.material.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/3.
 */
@ContentView(R.layout.activity_history_info_v2)
public class FriendHistoryInfoActivityV2 extends BaseAppCompActivity implements View.OnClickListener {

    private static final String TAG = "FriendHistoryInfoActivityV2";
    /* contains */
    private static final int MSG_POST_ERROR = 1;
    private static final int MSG_POST_OK = 2;
    private static final int MSG_GET_COMMENTS_AND_THUMB = 4;
    private static final int MSG_UPDATE_VIEW = 5;
    private static final int MSG_POST_VIDEO_OVER = 6;

    /* local data */
    private GetWorkoutFullEntity mFullWorkout;// 完整信息
    public HistoryInfoEntity mHistoryInfo;// 历史解析完成


    private String mWorkoutName;
    private int workoutId;//跑步历史id
    private String mNickname;
    private int userId;
    private int userAge;//用户年龄
    private String avatar;


    private Tab[] mItems;
    private HistoryInfoPagerAdapter mPagerAdapter;//Tab适配器
    private FragmentManager mFragMgr;
    private int mLastFragment = 1;//跳转前页面显示位置

    private doLoadHistoryTask mLoadHistoryTask;//分析跑步历史数据

    private List<GetSocialListResult.SocialsEntity> socialList = new ArrayList<>();
    private List<GetPlaybackCameraListResult.VideoEntity> mVieoList = new ArrayList<>();//视频列表


    private Fragment mContentFragment; // 当前显示Fragment
    private FriendHistoryBasicInfoFragmentV2 mBasicFragment;
    private FriendHistorySpliteListFragmentV2 mSplitFragment;
    private FriendHistoryHeartrateInfoFragmentV2 mHeartrateFragment;

    private RotateAnimation animation;//正在同步的动画

    /* local view */
    @ViewInject(R.id.ll_activity_base)
    private LinearLayout mBaseLl;
    @ViewInject(R.id.btn_sync)
    private ImageButton mSyncBtn;//同步按钮
    @ViewInject(R.id.delete)
    private ImageButton mDeleteBtn;//删除按钮
    @ViewInject(R.id.share)
    private ImageButton mShareBtn;//分享按钮
    @ViewInject(R.id.tbi_history)
    private TabPageIndicator mTpi;//Tab控件
    @ViewInject(R.id.vp_history)
    private CustomViewPager mViewPager;//ViewPager


    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载布局
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;//加载动画控件
    @ViewInject(R.id.tv_reload)
    private TextView mReloadTv;//加载文字
    @ViewInject(R.id.ll_title_right)
    private LinearLayout mTitleRll;
    @ViewInject(R.id.tv_title_loading)
    private TextView mLoadTitleTv;//标题加载文字


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
                    showPostError((String) msg.obj);
                    break;
                //请求成功
                case MSG_POST_OK:
                    mFullWorkout = JSON.parseObject((String) msg.obj, GetWorkoutFullEntity.class);
                    postGetCameraList();
                    break;
                //获取视频列表结束
                case MSG_POST_VIDEO_OVER:
                    postGetThumbsAndComments();
                    break;
                case MSG_GET_COMMENTS_AND_THUMB:
                    mLoadV.setLoadingText("正在解析数据...");
                    mLoadHistoryTask = new doLoadHistoryTask();
                    mLoadHistoryTask.execute("null");
                    break;

                case MSG_UPDATE_VIEW:
                    updateViewsAfterParse();
                    break;
            }
        }
    };

    /**
     * Fragment 切换事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventSwitchFragment event) {
        if (event.type == EventSwitchFragment.TYPE_HISTORY) {
            if (event.event == EventSwitchFragment.EVENT_HISTORY_HEARTRATE) {
                mViewPager.setCurrentItem(2);
            } else if (event.event == EventSwitchFragment.EVENT_HISTORY_SPLIT) {
                mViewPager.setCurrentItem(1);
            }
        }
    }

    @Override
    @OnClick({R.id.title_bar_left_menu, R.id.share,
            R.id.delete, R.id.tv_reload})
    public void onClick(View v) {
        //back
        if (v.getId() == R.id.title_bar_left_menu) {
            finish();
        } else if (v.getId() == R.id.tv_reload) {
            mLoadV.setLoadingText("加载中..");
            mReloadTv.setVisibility(View.INVISIBLE);
            doMyOnCreate();
        }
    }


    @Override
    protected void initData() {
        // 获取简单的跑步信息
        mWorkoutName = getIntent().getExtras().getString("workoutname");
        //TEST
//        workoutId = 3718159;
        workoutId = getIntent().getExtras().getInt("workoutid");
        userAge = getIntent().getExtras().getInt("Age", 20);
        mNickname = getIntent().getExtras().getString("nickname");
        userId = getIntent().getExtras().getInt("userid", 0);
        avatar = getIntent().getExtras().getString("avatar");

        //获取跑步历史数据
        mBasicFragment = FriendHistoryBasicInfoFragmentV2.newInstance();
        mSplitFragment = FriendHistorySpliteListFragmentV2.newInstance();
        mHeartrateFragment = FriendHistoryHeartrateInfoFragmentV2.newInstance();
        mFragMgr = getSupportFragmentManager();
    }

    @Override
    protected void initViews() {
        mShareBtn.setVisibility(View.GONE);
        mDeleteBtn.setVisibility(View.GONE);
        mSyncBtn.setVisibility(View.GONE);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);//设置此项可以防止切换Fragment时屏幕闪屏的问题
        EventBus.getDefault().register(this);
        /* 标题 */
        mLoadTitleTv.setVisibility(View.VISIBLE);
        mLoadTitleTv.setText("正在加载中....");
        mTitleRll.setVisibility(View.INVISIBLE);

        /* TAB */
        mItems = new Tab[]{Tab.BASIC, Tab.SPLIT, Tab.HEARTRATE};
        mPagerAdapter = new HistoryInfoPagerAdapter(getSupportFragmentManager(), mItems, new Fragment[]{mBasicFragment, mSplitFragment, mHeartrateFragment});
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
                imm.hideSoftInputFromWindow(mSyncBtn.getWindowToken(), 0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
//        mViewPager.setNoScroll(true);
        mViewPager.setCurrentItem(0);
        mShareBtn.setVisibility(View.GONE);

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
                    EventBus.getDefault().post(new EventComment(0, 0, 0, 0, userEntity.nickname, userEntity.gender, userEntity.avatar, userEntity.name));
                }
            }
        });
    }

    @Override
    protected void doMyOnCreate() {
        // 若数据库存在直接从本地刷新界面
        postGetWorkoutFullDataRequest();
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
        cancelTask();
        causeGC();
    }

    private void cancelTask() {
        if (mLoadHistoryTask != null) {
            mLoadHistoryTask.cancel(true);
        }
    }

    /**
     * 结束Handler事件
     */
    private void cancelHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_GET_COMMENTS_AND_THUMB);
            mPostHandler.removeMessages(MSG_POST_ERROR);
            mPostHandler.removeMessages(MSG_POST_OK);
            mPostHandler.removeMessages(MSG_POST_VIDEO_OVER);
            mPostHandler.removeMessages(MSG_UPDATE_VIEW);
            mPostHandler = null;
        }
    }

    /**
     * 通知GC
     */
    private void causeGC() {
        socialList.clear();
        mVieoList.clear();
        mLoadV.setVisibility(View.GONE);
    }


    /**
     * 获取所有workout数据
     */
    private void postGetWorkoutFullDataRequest() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_WORKOUT_INFO;
        RequestParams params = RequestParamsBuild.buildGetWorkoutInforRequest(mContext, workoutId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    mPostHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_OK;
                    msg.obj = Response;
                    mPostHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = s;
                    mPostHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 获取视频数据
     */
    private void postGetCameraList() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PLAYBACK_CAMERA_LIST;
        RequestParams params = RequestParamsBuild.buildGetPlaybackCameraListRequest(mContext, workoutId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {

            }

            @Override
            protected void onRightResponse(String Response) {
                GetPlaybackCameraListResult result = JSON.parseObject(Response, GetPlaybackCameraListResult.class);
                if (result.getVideo() != null) {
                    mVieoList.clear();
                    mVieoList.addAll(result.getVideo());
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_VIDEO_OVER);
                }
            }
        });
    }

    /**
     * 获取点赞和评论列表
     */
    private void postGetThumbsAndComments() {
//        Log.v(TAG,"postGetThumbsAndComments");
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_SOCIAL_LIST;
        RequestParams params = RequestParamsBuild.buildGetSocialListRequest(mContext, workoutId, 1);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                Log.v(TAG, "onErrorResponse errorMsg:" + errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                GetSocialListResult result = JSON.parseObject(Response, GetSocialListResult.class);
                Log.v(TAG, "result.socials != null: " + (result.socials != null));
                if (result.getSocials() != null) {
                    socialList.clear();
                    socialList.addAll(result.socials);
                    Log.v(TAG, "postGetThumbsAndComments socialList.size(): " + socialList.size());
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_GET_COMMENTS_AND_THUMB);
                }
            }
        });
    }

    /**
     * 显示加载错误
     */
    private void showPostError(final String error) {
        T.showShort(mContext, error);
        mLoadV.setLoadingText("加载失败");
        mReloadTv.setVisibility(View.VISIBLE);
    }


    /**
     * 解析后显示页面
     */
    private void updateViewsAfterParse() {
        //去除缓冲提示
        mLoadV.setVisibility(View.GONE);
        mLoadViewRl.setVisibility(View.GONE);
        mTitleRll.setVisibility(View.VISIBLE);
        mLoadTitleTv.setVisibility(View.GONE);
        mTpi.setVisibility(View.VISIBLE);
        //更新Fragment数据
        mSplitFragment.updateView(mHistoryInfo.spliteList, (mHistoryInfo.heartrateList.size() != 0),
                mHistoryInfo.avgPace,mHistoryInfo.length,mHistoryInfo.duration);
        mBasicFragment.updateWorkoutView(socialList, mVieoList, mHistoryInfo);
        mHeartrateFragment.updateViewFromActivity(mHistoryInfo);
        mViewPager.setView(mBasicFragment.mBaseLl);
    }


    /**
     * 启用线程处理历史数据
     */
    class doLoadHistoryTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            if (params[0] != null) {
                //保存数据至数据库
                PaserDataFromFullWorkout();
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mPostHandler != null) {
                mPostHandler.sendEmptyMessage(MSG_UPDATE_VIEW);
            }
        }
    }

    /**
     * 解析数据
     */
    private void PaserDataFromFullWorkout() {
        int avgPace = 0;
        float length = mFullWorkout.getLength();
        long duration = mFullWorkout.getDuration();
        float calorie = mFullWorkout.getCalorie();

        List<HistoryLocEntity> locList = new ArrayList<HistoryLocEntity>();
        List<HistoryHeartrateEntity> heartrateList = new ArrayList<HistoryHeartrateEntity>();
        List<HistorySpliteEntity> spliteList = new ArrayList<HistorySpliteEntity>();
        List<HistoryLapEntity> lapList = new ArrayList<HistoryLapEntity>();

        // 遍历分段信息
        if (mFullWorkout.splits != null) {
            // 保存分段信息
            for (int i = 0; i < mFullWorkout.splits.size(); i++) {
                GetWorkoutSplitsEntity splite = mFullWorkout.splits.get(i);
                HistorySpliteEntity spliteEntity = new HistorySpliteEntity(splite.length, splite.duration,
                        splite.avgheartrate, splite.latitude, splite.longitude);
                spliteList.add(spliteEntity);
            }
        }
        // 遍历LAP
        if (mFullWorkout.lap != null) {
            for (int i = 0; i < mFullWorkout.lap.size(); i++) {
                GetWorkoutLapEntity lap = mFullWorkout.lap.get(i);
                Log.v(TAG,"heartrate:" + lap.heartratedata.size());
                long lapTimeOfSet = TimeUtils.getTimesetFromStartTime(mFullWorkout.starttime, lap.starttime);
                // 获取location 信息
                for (GetWorkoutLocationEntity locEntity : lap.locationdata) {
                    long timeofset = lapTimeOfSet + locEntity.timeoffset;
                    HistoryLocEntity loc = new HistoryLocEntity(locEntity.latitude, locEntity.longitude,
                            locEntity.speed, timeofset, lap.starttime);
                    locList.add(loc);
                }
                // 获取心率信息
                for (GetWorkoutHeartrateEntity heartRateEntity : lap.heartratedata) {
                    Log.v(TAG,"heartRateEntity bpm:" + heartRateEntity.bpm);
                    HistoryHeartrateEntity heartrate = new HistoryHeartrateEntity(
                            (lapTimeOfSet + heartRateEntity.timeoffset), heartRateEntity.bpm);
                    heartrateList.add(heartrate);
                }
                //增加分段信息
                HistoryLapEntity lapEntity = new HistoryLapEntity(lap.starttime);
                if (lap.locationdata != null && lap.locationdata.size() > 0) {
                    GetWorkoutLocationEntity startLoc = lap.locationdata.get(0);
                    GetWorkoutLocationEntity endLoc = lap.locationdata.get(lap.locationdata.size() - 1);
                    lapEntity.setStartLatLng(startLoc.latitude + "," + startLoc.longitude);
                    lapEntity.setEndLatLng(endLoc.latitude + "," + endLoc.longitude);
                }
                lapList.add(lapEntity);
            }
        }

        if (mFullWorkout.length != 0) {
            avgPace = (int) (mFullWorkout.duration * 1000 / mFullWorkout.length);
        }

        mHistoryInfo = new HistoryInfoEntity(mFullWorkout.id, mFullWorkout.name, avgPace, length, duration,
                calorie, mFullWorkout.avatar, locList, heartrateList, spliteList, mFullWorkout.avgheartrate, lapList);
        mHistoryInfo.userId = mFullWorkout.users_id;
    }

    /**
     * get and set
     **/

    public String getWorkoutName() {
        return mWorkoutName;
    }

    public List<GetSocialListResult.SocialsEntity> getSocialList() {
        return socialList;
    }

    public List<GetPlaybackCameraListResult.VideoEntity> getVieoList() {
        return mVieoList;
    }
}
