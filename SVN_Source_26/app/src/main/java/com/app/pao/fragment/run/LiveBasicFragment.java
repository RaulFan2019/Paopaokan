package com.app.pao.fragment.run;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.run.LiveActivityV3;
import com.app.pao.config.AppEnum;
import com.app.pao.config.MapEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.UploadData;
import com.app.pao.entity.db.DBEntityLocation;
import com.app.pao.entity.db.DBEntitySplite;
import com.app.pao.entity.db.DBUploadEntity;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.event.EventSendSocial;
import com.app.pao.entity.event.EventSwitchFragment;
import com.app.pao.entity.event.EventUpdateSocialPos;
import com.app.pao.entity.event.EventUpdateVideoPos;
import com.app.pao.entity.model.LiveHeartrateEntity;
import com.app.pao.entity.model.LivePlayBackLocEntityV2;
import com.app.pao.entity.model.LiveSplite;
import com.app.pao.entity.network.GetPlaybackCameraListResult;
import com.app.pao.entity.network.GetSocialListResult;
import com.app.pao.entity.network.LiveSocial;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.fragment.history.HistorySocialsFragment;
import com.app.pao.fragment.history.HistoryVideoFragment;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.ui.widget.LinearLayoutNoScroll;
import com.app.pao.ui.widget.chart.HistoryHeartrateChartView;
import com.app.pao.ui.widget.chart.HistoryPaceChartView;
import com.app.pao.ui.widget.chart.LiveHeartrateChartView;
import com.app.pao.ui.widget.chart.LivePaceChartView;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.LiveUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/12.
 */
public class LiveBasicFragment extends BaseFragment implements View.OnClickListener,
        AMap.OnMarkerClickListener, AMap.OnCameraChangeListener,AMap.OnMapClickListener {

    /* contains */
    private static final String TAG = "LiveBasicFragment";

    private static final int SHOW_NONE = 0;//没有显示Fragment
    private static final int SHOW_SCOIAL = 1;//显示评论点赞
    private static final int SHOW_VIDEO = 2;//显示视频

    /* local view */
    @ViewInject(R.id.view_map)
    private MapView mapView;// 高德地图
    @ViewInject(R.id.btn_show_km)
    private ImageButton mKmBtn;//显示分段公里的控制按钮

    @ViewInject(R.id.btn_down_fragment)
    private ImageButton mDownFragmentBtn;//隐藏Fragment
    @ViewInject(R.id.btn_map_prise)
    private LinearLayout mShowSocialBtn;//显示点赞评论Fragment
    @ViewInject(R.id.tv_prise_count)
    private TextView mSocialCountTv;
    @ViewInject(R.id.btn_map_video)
    private LinearLayout mShowVideoBtn;//显示视频Fragment
    @ViewInject(R.id.tv_video_count)
    private TextView mVideoCountTv;//视频数量文本

    /* local view about data */
    @ViewInject(R.id.ll_big_data)
    private LinearLayout mBigDataLl;//大数据布局
    @ViewInject(R.id.iv_user)
    private CircularImage mAvatarIv;//用户头像
    @ViewInject(R.id.tv_user_name)
    private TextView mUserNameTv;//用户名称文本
    @ViewInject(R.id.tv_length)
    private TextView mLengthTv;//长度文本
    @ViewInject(R.id.tv_duration)
    private TextView mDurationTv;//时长文本
    @ViewInject(R.id.tv_pace)
    private TextView mPaceTv;//配速文本
    @ViewInject(R.id.tv_calorie)
    private TextView mCalorieTv;//卡路里文本
    @ViewInject(R.id.tv_heartrate)
    private TextView mHeartrateTv;//心率文本
    @ViewInject(R.id.ll_pace_chart)
    private LinearLayout mPaceChartLl;//配速Chart承载布局
    @ViewInject(R.id.ll_heartrate_chart)
    private LinearLayout mHeartrateChartLl;//心率Chart承载布局
    private LivePaceChartView mPaceChart;
    private LiveHeartrateChartView mHeartrateChart;

    /* local view about small data*/
    @ViewInject(R.id.ll_small_data)
    private LinearLayout mSmallDataLl;//小数据布局
    @ViewInject(R.id.iv_small_data_user)
    private CircularImage mSmallIv;//用户头像
    @ViewInject(R.id.tv_small_data_user_name)
    private TextView mSmallNickNameTv;//用户昵称
    @ViewInject(R.id.tv_small_data_pace)
    private TextView mSmallDataPaceTv;
    @ViewInject(R.id.tv_small_data_length)
    private TextView mSmallDataLengthTv;//小数据布局中的距离
    @ViewInject(R.id.ll_fragment_social)
    public LinearLayoutNoScroll mFragmentLl;

    /* local data */
    private Typeface typeFace;//字体
    private double mLastLa = 0;//最后一个纬度
    private double mLastLo = 0;//最后一个经度
    private float mLength = 0;//目前距离


    // about amap
    private AMap mAMap;
    private List<Polyline> mLineList = new ArrayList<>();//线集合
    private Marker StartMk;// 起点
    private Marker EndMk;// 终点
    private BitmapDescriptor mUserBitmapDes;// 用户头像
    private LatLngBounds mBounds;//视频范围

    private boolean isShowKm = true;
    private int mSplitMarkerZoomSize;
    private float mCurrZoom;//当前放大比例

    private List<LiveSplite> splitList = new ArrayList<LiveSplite>();
    private List<LiveSocial> mSocialList = new ArrayList<>();

    private List<Marker> mSplitMarkList = new ArrayList<Marker>();
    private List<Marker> mSocialMarkList = new ArrayList<>();//评论点赞的Mark列表
    private List<Marker> mVideoMarkList = new ArrayList<>();//视频的Mark列表

    private Marker mCurrMarker;
    private Marker mCurrVideoMarker;
    private Marker mLastVideoMarker;

    private List<GetPlaybackCameraListResult.VideoEntity> mVideoList = new ArrayList<>();//视频列表
    private boolean mIsFirstIn = true;

    /* local data about fragment*/
    private int mShowFragment = SHOW_NONE;
    private FragmentManager mFragmentManager;
    private LiveSocialFragment mSocialFragment;//评论Fragment
    private LiveVideoFragmentV2 mVideoFragment;//视频Fragment

    /* utils */
    private BitmapUtils mBitmapUtils;

    /* local data about user */
    private String mAvatar;//用户头像地址
    private String mUserNickName;//用户昵称
    private int mWorkoutId;//跑步历史ID

    public static LiveBasicFragment newInstance() {
        LiveBasicFragment fragment = new LiveBasicFragment();
        return fragment;
    }

    /**
     * 视频焦点发生变化
     *
     * @param eventComment
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventUpdateVideoPos eventComment) {
        mCurrVideoMarker.setPosition(mVideoMarkList.get(eventComment.pos).getPosition());
    }

    /**
     * 评论焦点发生变化
     *
     * @param eventComment
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventUpdateSocialPos eventComment) {
        mCurrMarker.setPosition(new LatLng(eventComment.latitude, eventComment.longitude));
    }

    /**
     * 接收到发送评论信息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventSendSocial event) {
        if (event.socialType == EventSendSocial.COMMENTS) {
            addNewComments(event.replayId, event.comments, event.replayUserNickName,
                    event.replayUserName, event.replayUserGender, event.replayUserAvatar);
        } else if (event.socialType == EventSendSocial.THUMB) {
            //若已经点赞过
            String info = mWorkoutId + ":" + AppEnum.dynamicType.WORKOUT;
            DBUploadEntity uploadEntity;
            if (event.mHasGiveThumbup == AppEnum.hasGiveThumbup.YES) {
                T.showShort(mContext, "您已经赞过啦");
//                uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_THUMB_UP_NO, URLConfig
//                        .URL_THUMB_UP_NO, info, "", LocalApplication.getInstance().getLoginUser(mContext).getUserId());
//                int cancelThumbPosition = 0;
//                for (; cancelThumbPosition < mSocialList.size(); cancelThumbPosition++) {
//                    if (mSocialList.get(cancelThumbPosition).getUserid() == LocalApplication.getInstance().getLoginUser(mContext).getUserId()
//                            && mSocialList.get(cancelThumbPosition).type == 1) {
//                        mSocialList.remove(cancelThumbPosition);
//                        break;
//                    }
//                }
//                mSocialFragment.cancelThumb(cancelThumbPosition);
//                T.showShort(mContext, "取消点赞");
            } else {
                uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_THUMB_UP_YES, URLConfig
                        .URL_THUMB_UP_YES, info
                        , "", LocalApplication.getInstance().getLoginUser(mContext).getUserId());

                DBUserEntity user = LocalApplication.getInstance().getLoginUser(mContext);
                LiveSocial result = new LiveSocial();
                result.time = TimeUtils.NowTime();
                result.type = 1;
                result.userid = user.userId;
                result.longitude = mLastLo;
                result.latitude = mLastLa;
                result.comment = "";
                result.mediatype = 1;
                result.mediaurl = "";
                result.length = 0;
                result.usernickname = user.nickname;
                result.useravatar = user.avatar;
                result.replyuserid = 0;
                result.replyusername = "";
                result.replynickname = "";
                result.replygender = 1;
                result.replyavatar = "";
                result.position = mLength;
                result.isSelect = false;
                result.readnow = false;
                mSocialList.add(0, result);
                mSocialFragment.andThumb(result);
                mCurrMarker.setPosition(new LatLng(mLastLa, mLastLo));
                //发送点赞请求
                UploadData.createNewUploadData(mContext, uploadEntity);
                T.showShort(mContext, "点赞成功");
            }
            mSocialCountTv.setText(mSocialList.size() + "");
            if (mSocialList.size() > 0) {
                mSocialCountTv.setTextColor(Color.parseColor("#ffc445"));
            } else {
                mSocialCountTv.setTextColor(Color.parseColor("#ffffff"));
            }
        } else {
            addNewVoice(event.replayId, event.comments, event.replayUserNickName,
                    event.replayUserName, event.replayUserGender, event.replayUserAvatar);
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        //目前状态是评论
        if (mShowFragment == SHOW_SCOIAL) {
            clickSocialBtn();
            //目前状态是录像
        } else if (mShowFragment == SHOW_VIDEO) {
            clickVideoBtn();
        }
    }

    public void updateWorkoutInfo(int workoutId) {
        mWorkoutId = workoutId;
        if (mSocialFragment != null) {
            mSocialFragment.mWorkoutId = mWorkoutId;
        }
        if (mVideoFragment != null) {
            mVideoFragment.mWorkoutId = mWorkoutId;
        }
    }

    /**
     * 更新配速Chart
     */
    public void updatePaceChart(final List<LivePlayBackLocEntityV2> mPlayerLoc) {
//        Log.v(TAG, "updatePaceChart size :" + mPlayerLoc.size());
        mPaceChart = new LivePaceChartView(mContext);
        mPaceChart.initViews(0, mPlayerLoc.size());
        mPaceChart.setEnabled(false);
        mPaceChart.addLine(mPlayerLoc, Color.parseColor("#0068ffa1"), true);
        mPaceChartLl.removeAllViews();
        mPaceChartLl.addView(mPaceChart, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 更新心率Chart
     */
    public void updateHeartChart(final List<LiveHeartrateEntity> mHeartList) {
        mHeartrateChart = new LiveHeartrateChartView(mContext);
        mHeartrateChart.initViews(0, mHeartList.size());
        mHeartrateChart.setEnabled(false);
        mHeartrateChart.addLine(mHeartList, Color.parseColor("#0068ffa1"), true);
        mHeartrateChartLl.removeAllViews();
        mHeartrateChartLl.addView(mHeartrateChart, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    @OnClick({R.id.btn_show_km, R.id.btn_back_to_center, R.id.btn_pace,
            R.id.btn_map_prise, R.id.btn_down_fragment, R.id.btn_map_video})
    public void onClick(View v) {
        //是否显示KM标记
        if (v.getId() == R.id.btn_show_km) {
            if (isShowKm) {
                destroySpliteMarker();
            } else {
                drawSpliteMarker();
            }
            isShowKm = !isShowKm;
            //回到地图中心
        } else if (v.getId() == R.id.btn_back_to_center) {
            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mBounds, DeviceUtils.dp2px(mContext, 85)));
            //点击配速
        } else if (v.getId() == R.id.btn_pace) {
            EventBus.getDefault().post(new EventSwitchFragment(EventSwitchFragment.TYPE_HISTORY, EventSwitchFragment.EVENT_HISTORY_SPLIT));
            //点击评论展开按钮
        } else if (v.getId() == R.id.btn_map_prise) {
            clickSocialBtn();
            //点击向下按钮
        } else if (v.getId() == R.id.btn_down_fragment) {
            clickDownBtn();
            //点击视频展开按钮
        } else if (v.getId() == R.id.btn_map_video) {
            clickVideoBtn();
        }
    }

    /**
     * 地图上的Marker点击事件
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        //找到点击的maker
        if (mShowFragment == SHOW_VIDEO) {
            int pos = 0;
            for (; pos < mVideoMarkList.size(); pos++) {
                if (marker.getPosition().latitude == mVideoMarkList.get(pos).getPosition().latitude
                        && marker.getPosition().longitude == mVideoMarkList.get(pos).getPosition().longitude) {
                    mCurrVideoMarker.setPosition(marker.getPosition());
                    mVideoFragment.updateHighlightPos(pos + 2);
                    mLastVideoMarker.setVisible(true);
                    mLastVideoMarker = mVideoMarkList.get(pos);
                    mLastVideoMarker.setVisible(false);
                    break;
                }
            }
        }
        return false;
    }


    /**
     * 更新位置信息
     */
    public void updateLocationData(final LivePlayBackLocEntityV2 lastPlayLoc, final int HeartRate) {
        mLength = lastPlayLoc.length;
        // 距离
        String length = NumUtils.retainTheDecimal(lastPlayLoc.length);
        mLengthTv.setText(length);
        mSmallDataLengthTv.setText(length + "公里");
        //耗时
        String duration = TimeUtils.formatSecondsToLongHourTime(lastPlayLoc.Duration);
        mDurationTv.setText(duration);
        //平均配速
        mPaceTv.setText(TimeUtils.formatSecondsToSpeedTime((long) (lastPlayLoc.Duration * 1000 / lastPlayLoc.length)));
        mSmallDataPaceTv.setText(TimeUtils.formatSecondsToSpeedTime((long) (lastPlayLoc.Duration * 1000 / lastPlayLoc.length)) + "/公里");
        // 卡路里(千卡)
        float Caloriestemp = (float) (1.036 * 60 * lastPlayLoc.length / 1000);
        BigDecimal Caloriesb = new BigDecimal(Caloriestemp);
        mCalorieTv.setText(Caloriesb.setScale(0, BigDecimal.ROUND_HALF_UP).intValue() + "千卡"); // floatValue
        //平均心率
        if (HeartRate != 0) {
            mHeartrateTv.setText(HeartRate + "");
        } else {
            mHeartrateTv.setText("- -");
        }
    }

    /**
     * 更新分段数据
     */
    public void updateNewSplitOnView(List<LiveSplite> SplitList) {
        Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
        splitList = SplitList;
        // 若存在新的splite
        for (int i = mSplitMarkList.size(); i < splitList.size(); i++) {
            LatLng latLng = new LatLng(splitList.get(i).getLatitude(),
                    splitList.get(i).getLongitude());
            mSplitMarkList.add(mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(latLng)
                    .draggable(false).zIndex(MapEnum.ZINDEX_SPLITE_IMAGE)
                    .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.GetSpliteBitmap(mContext, (i + 1), typeFace)))));
        }
    }

    /**
     * 销毁 splite 标记
     */
    private void destroySpliteMarker() {
        mKmBtn.setBackgroundResource(R.drawable.icon_dismiss_per_km_tip);
        for (Marker marker : mSplitMarkList) {
            marker.remove();
            marker.destroy();
        }
    }

    /**
     * 画splite 的标记
     */
    private void drawSpliteMarker() {
        mKmBtn.setBackgroundResource(R.drawable.btn_check_per_km);
        // splite 终点的位置
        mSplitMarkList = new ArrayList<Marker>();
        mSplitMarkList.clear();
        if (splitList == null) {
            return;
        }
        for (int i = 0, splitSize = splitList.size(); i < splitSize; i += mSplitMarkerZoomSize) {
            LiveSplite split = splitList.get(i);
            if (split.getLatitude() == 0 && split.getLongitude() == 0) {
                break;
            }
            LatLng latLng = new LatLng(split.getLatitude(), split.getLongitude());
            mSplitMarkList.add(mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(latLng)
                    .draggable(false).zIndex(MapEnum.ZINDEX_SPLITE_IMAGE)
                    .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.GetSpliteBitmap(mContext, (i + 1), typeFace)))));
        }
    }

    /**
     * 更新视频信息
     */
    public void updateVideoList(List<GetPlaybackCameraListResult.VideoEntity> VideoList) {
        mVideoList = VideoList;
        mVideoCountTv.setText(mVideoList.size() + "");
        if (mVideoList.size() > 0) {
            mVideoCountTv.setTextColor(Color.parseColor("#ffc445"));
        } else {
            mVideoCountTv.setTextColor(Color.parseColor("#ffffff"));
        }
        if (mVideoFragment != null) {
            mVideoFragment.updateVideoList(mVideoList);
        }

    }

    /**
     * 更新评论界面
     */
    public void updateSocialView(List<LiveSocial> SocialList) {
        int AddThumbCount = 0;
        int AddCommentCount = 0;
        for (LiveSocial social : SocialList) {
            if (social.type == 1) {
                AddThumbCount++;
            } else {
                AddCommentCount++;
            }
            mSocialList.add(0, social);
        }
        //点赞评论数量
        mSocialCountTv.setText(mSocialList.size() + "");
        if (mSocialList.size() > 0) {
            mSocialCountTv.setTextColor(Color.parseColor("#ffc445"));
        } else {
            mSocialCountTv.setTextColor(Color.parseColor("#ffffff"));
        }
        if (mSocialFragment != null) {
            mSocialFragment.updateSocialList(mSocialList, AddThumbCount, AddCommentCount);
        }
    }

    /**
     * 画线
     *
     * @param polylineOption
     */
    public void drawNewLine(final PolylineOptions polylineOption, final LatLngBounds Bounds) {
        mBounds = Bounds;
        mLineList.add(mAMap.addPolyline(polylineOption));
        // 若是第一条线段,做起点标记
        if (StartMk == null) {
            StartMk = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(polylineOption.getPoints()
                    .get(0)).title("起点").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_run_start))
                    .draggable(true).zIndex(MapEnum.ZINDEX_START_END));
        }
        LatLng lastLaLng = polylineOption.getPoints().get(polylineOption.getPoints().size() - 1);
        mLastLa = lastLaLng.latitude;
        mLastLo = lastLaLng.longitude;
        // 若已绘画过终点,更新终点
        if (EndMk != null) {
            EndMk.setPosition(lastLaLng);
        } else {
            if (mUserBitmapDes != null) {
                EndMk = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                        .position(lastLaLng)
                        .icon(mUserBitmapDes)
                        .draggable(true).zIndex(MapEnum.ZINDEX_START_END));
            } else {
                EndMk = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position
                        (lastLaLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_end)).draggable(true)
                        .zIndex(MapEnum.ZINDEX_START_END));
            }
        }
        // 检查点的位置是否超过屏幕
        Point point = mAMap.getProjection().toScreenLocation(lastLaLng);
        float des = DeviceUtils.dpToPixel(20);
        if (mIsFirstIn) {
            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mBounds, DeviceUtils.dp2px(mContext, 85)));
            mIsFirstIn = false;
        } else {
            if ((mapView.getTop() + des * 4) > point.y || (mapView.getBottom() - des) < point.y
                    || (mapView.getLeft() + des) > point.x || (mapView.getRight() - des) < point.x) {
                mAMap.moveCamera(CameraUpdateFactory.changeLatLng(lastLaLng));
            }
        }
    }

    public void resetMapLineData() {
        for (int i = 0; i < mLineList.size(); i++) {
            mLineList.get(i).remove();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_basic;
    }

    @Override
    protected void initParams() {
        mapView.onCreate(mSavedInstanceState);
        EventBus.getDefault().register(this);
        mFragmentManager = getFragmentManager();
        initData();
        initViews();
        setUpMap();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mapView.onDestroy();
        causeGC();
    }

    /**
     * 内存释放
     */
    private void causeGC() {

    }

    /**
     * 初始化数据
     */
    private void initData() {
        LiveActivityV3 activity = (LiveActivityV3) getActivity();
        mAvatar = activity.getAvatar();
        mUserNickName = activity.getNickName();
        mWorkoutId = activity.getWorkoutId();
        //获取头像图片
        mBitmapUtils = new BitmapUtils(mContext);
        mUserBitmapDes = LiveUtils.getUserBitmapDescriptor(mContext, mBitmapUtils, mAvatar);
        mSplitMarkerZoomSize = 1;
    }

    /**
     * 初始化界面
     */
    private void initViews() {
        //初始化文本字体
        typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
        mLengthTv.setTypeface(typeFace);
        mDurationTv.setTypeface(typeFace);
        mPaceTv.setTypeface(typeFace);
        mCalorieTv.setTypeface(typeFace);
        mHeartrateTv.setTypeface(typeFace);
        mSmallDataPaceTv.setTypeface(typeFace);
        mSmallDataLengthTv.setTypeface(typeFace);
        //初始化用户信息
        mUserNameTv.setText(mUserNickName);
        mSmallNickNameTv.setText(mUserNickName);
        ImageUtils.loadUserImage(mAvatar, mAvatarIv);
        ImageUtils.loadUserImage(mAvatar, mSmallIv);
    }

    /**
     * 初始化地图
     */
    private void setUpMap() {
        if (mAMap == null) {
            mAMap = mapView.getMap();
            mAMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
            mAMap.getUiSettings().setZoomControlsEnabled(false);
            mAMap.setOnMarkerClickListener(this);
            mAMap.setOnMapClickListener(this);

        }
    }

    /**
     * 点击评论点赞按钮
     */
    private void clickSocialBtn() {
        //若当前状态是不显示任何Fragment
        if (mShowFragment == SHOW_NONE) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            if (mSocialFragment == null) {
                mSocialFragment = LiveSocialFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putSerializable("socialList", (Serializable) mSocialList);
                bundle.putDouble("latitude", mLastLa);
                bundle.putDouble("longitude", mLastLo);
                bundle.putDouble("length", mLength);
                bundle.putInt("workoutid", mWorkoutId);
                mSocialFragment.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                        .add(R.id.ll_fragment_social, mSocialFragment)
                        .commitAllowingStateLoss();
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                        .show(mSocialFragment)
                        .commitAllowingStateLoss();
            }
            drawSocialMarker();
            mDownFragmentBtn.setVisibility(View.VISIBLE);
            mShowSocialBtn.setBackgroundResource(R.drawable.btn_map_big_on);
            mSocialCountTv.setTextColor(Color.parseColor("#ffffff"));
            mBigDataLl.setVisibility(View.INVISIBLE);
            mSmallDataLl.setVisibility(View.VISIBLE);
            mShowFragment = SHOW_SCOIAL;
            //若当前状态是显示点赞评论
        } else if (mShowFragment == SHOW_SCOIAL) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                    .hide(mSocialFragment)
                    .commitAllowingStateLoss();
            mDownFragmentBtn.setVisibility(View.GONE);
            mBigDataLl.setVisibility(View.VISIBLE);
            mSmallDataLl.setVisibility(View.INVISIBLE);
            mShowSocialBtn.setBackgroundResource(R.drawable.btn_live_map_big_off);
            if (mSocialList.size() > 0) {
                mSocialCountTv.setTextColor(Color.parseColor("#ffc445"));
            } else {
                mSocialCountTv.setTextColor(Color.parseColor("#ffffff"));
            }
            mShowFragment = SHOW_NONE;
            destroySocialMarker();
            //若当前状态显示的是视频
        } else {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            if (mSocialFragment == null) {
                mSocialFragment = LiveSocialFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putSerializable("socialList", (Serializable) mSocialList);
                bundle.putDouble("latitude", mLastLa);
                bundle.putDouble("longitude", mLastLo);
                bundle.putDouble("length", mLength);
                bundle.putInt("workoutid", mWorkoutId);
                mSocialFragment.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                        .add(R.id.ll_fragment_social, mSocialFragment).hide(mVideoFragment)
                        .commitAllowingStateLoss();
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                        .hide(mVideoFragment).show(mSocialFragment)
                        .commitAllowingStateLoss();
            }
            drawSocialMarker();
            destroyVideoMarker();
            mShowSocialBtn.setBackgroundResource(R.drawable.btn_map_big_on);
            mSocialCountTv.setTextColor(Color.parseColor("#ffffff"));
            mShowVideoBtn.setBackgroundResource(R.drawable.btn_live_map_big_off);
            if (mVideoList.size() > 0) {
                mVideoCountTv.setTextColor(Color.parseColor("#ffc445"));
            } else {
                mVideoCountTv.setTextColor(Color.parseColor("#ffffff"));
            }
            mShowFragment = SHOW_SCOIAL;
        }
    }

    /**
     * 点击视频展示按钮
     */
    private void clickVideoBtn() {
        //若当前状态是不显示任何Fragment
        if (mShowFragment == SHOW_NONE) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            if (mVideoFragment == null) {
                mVideoFragment = LiveVideoFragmentV2.newInstance();
                Bundle bundle = new Bundle();
                bundle.putSerializable("videoList", (Serializable) mVideoList);
                bundle.putInt("workoutid", mWorkoutId);
                mVideoFragment.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                        .add(R.id.ll_fragment_social, mVideoFragment)
                        .commitAllowingStateLoss();
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                        .show(mVideoFragment)
                        .commitAllowingStateLoss();
            }
            drawVideoMarker();
            mDownFragmentBtn.setVisibility(View.VISIBLE);
            mShowVideoBtn.setBackgroundResource(R.drawable.btn_map_big_on);
            mVideoCountTv.setTextColor(Color.parseColor("#ffffff"));
            mBigDataLl.setVisibility(View.INVISIBLE);
            mSmallDataLl.setVisibility(View.VISIBLE);
            mShowFragment = SHOW_VIDEO;
            //若当前状态是显示视频
        } else if (mShowFragment == SHOW_VIDEO) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                    .hide(mVideoFragment)
                    .commitAllowingStateLoss();
            destroyVideoMarker();
            mDownFragmentBtn.setVisibility(View.GONE);
            mBigDataLl.setVisibility(View.VISIBLE);
            mSmallDataLl.setVisibility(View.INVISIBLE);
            if (mVideoList.size() > 0) {
                mVideoCountTv.setTextColor(Color.parseColor("#ffc445"));
            } else {
                mVideoCountTv.setTextColor(Color.parseColor("#ffffff"));
            }
            mShowVideoBtn.setBackgroundResource(R.drawable.btn_live_map_big_off);
            mShowFragment = SHOW_NONE;
            //当前状态显示评论点赞
        } else {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            if (mVideoFragment == null) {
                mVideoFragment = LiveVideoFragmentV2.newInstance();
                Bundle bundle = new Bundle();
                bundle.putSerializable("videoList", (Serializable) mVideoList);
                bundle.putInt("workoutid", mWorkoutId);
                mVideoFragment.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                        .add(R.id.ll_fragment_social, mVideoFragment).hide(mSocialFragment)
                        .commitAllowingStateLoss();
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                        .hide(mSocialFragment).show(mVideoFragment)
                        .commitAllowingStateLoss();
            }
            destroySocialMarker();
            drawVideoMarker();
            mShowVideoBtn.setBackgroundResource(R.drawable.btn_map_big_on);
            mVideoCountTv.setTextColor(Color.parseColor("#ffffff"));
            mShowSocialBtn.setBackgroundResource(R.drawable.btn_live_map_big_off);
            if (mSocialList.size() > 0) {
                mSocialCountTv.setTextColor(Color.parseColor("#ffc445"));
            } else {
                mSocialCountTv.setTextColor(Color.parseColor("#ffffff"));
            }
            mShowFragment = SHOW_VIDEO;
        }
    }

    /**
     * 点击向下按钮
     */
    private void clickDownBtn() {
        //目前状态是评论
        if (mShowFragment == SHOW_SCOIAL) {
            clickSocialBtn();
            //目前状态是录像
        } else {
            clickVideoBtn();
        }
    }

    /**
     * 画评论标记
     */
    private void drawSocialMarker() {
        if (mSocialList.size() == 0) {
            mCurrMarker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(new LatLng(0, 0))
                    .draggable(false).zIndex(MapEnum.ZINDEX_SOCIAL_BIG)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_social_mark_big)));
            return;
        }
        for (int i = 0, socialSize = mSocialList.size(); i < socialSize; i++) {
            LiveSocial entity = mSocialList.get(i);
            LatLng latLng = new LatLng(entity.latitude, entity.longitude);
            mSocialMarkList.add(mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(latLng).draggable(false).zIndex(MapEnum.ZINDEX_SOCIAL)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_social_mark))));
        }
        LiveSocial entity = mSocialList.get(0);
        mCurrMarker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .position(new LatLng(entity.latitude, entity.longitude))
                .draggable(false).zIndex(MapEnum.ZINDEX_SOCIAL_BIG)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_social_mark_big)));
    }


    /**
     * 销毁评论标记
     */
    private void destroySocialMarker() {
        if (mSocialList.size() == 0) {
            return;
        }
        for (Marker marker : mSocialMarkList) {
            marker.remove();
            marker.destroy();
        }
        mCurrMarker.remove();
        mCurrMarker.destroy();
    }

    /**
     * 画视频标记
     */
    private void drawVideoMarker() {
        if (mVideoList.size() == 0) {
            mCurrVideoMarker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(new LatLng(0, 0))
                    .draggable(false).zIndex(MapEnum.ZINDEX_SOCIAL_BIG)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_mark_video_big)));
            return;
        }
        for (int i = 0, videoSize = mVideoList.size(); i < videoSize; i++) {
            GetPlaybackCameraListResult.VideoEntity entity = mVideoList.get(i);
            LatLng latLng = new LatLng(entity.cameralatitude, entity.cameralongitude);
            mVideoMarkList.add(mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(latLng).draggable(false).zIndex(MapEnum.ZINDEX_SOCIAL)
                    .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.GetVideoBitmap(mContext, (i + 1), typeFace)))));
        }
        mLastVideoMarker = mVideoMarkList.get(mVideoList.size() / 2);
        mLastVideoMarker.setVisible(false);
        GetPlaybackCameraListResult.VideoEntity entity = mVideoList.get(mVideoList.size() / 2);
        mCurrVideoMarker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .position(new LatLng(entity.cameralatitude, entity.cameralongitude))
                .draggable(false).zIndex(MapEnum.ZINDEX_SOCIAL_BIG)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_mark_video_big)));
    }


    /**
     * 销毁评论标记
     */
    private void destroyVideoMarker() {
        if (mVideoList.size() == 0) {
            return;
        }
        for (Marker marker : mVideoMarkList) {
            marker.remove();
            marker.destroy();
        }
        mCurrVideoMarker.remove();
        mCurrVideoMarker.destroy();
    }

    /**
     * 发送评论
     */
    public void addNewComments(final int replayUserId, final String comment,
                               final String replayUserNickName, final String replayUserName,
                               final int replayUserGender, final String replayUserAvatar) {
        //发送评论
        String info = mWorkoutId + ":" + AppEnum.dynamicType.WORKOUT + ":" + replayUserId + ":" + comment;
//        Log.v(TAG,"addNewComments info:" + info);

        DBUploadEntity uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_COMMENTS, URLConfig
                .URL_ADD_COMMENT, info, "", LocalApplication.getInstance().getLoginUser(mContext).getUserId());
        UploadData.createNewUploadData(mContext, uploadEntity);

        //改变列表
        DBUserEntity user = LocalApplication.getInstance().getLoginUser(mContext);

        LiveSocial result = new LiveSocial();
        result.time = TimeUtils.NowTime();
        result.type = 2;
        result.userid = user.userId;
        result.longitude = mLastLo;
        result.latitude = mLastLa;
        result.comment = comment;
        result.mediatype = 1;
        result.mediaurl = "";
        result.length = 0;
        result.usernickname = user.nickname;
        result.useravatar = user.avatar;
        result.replyuserid = replayUserId;
        result.replyusername = replayUserName;
        result.replynickname = replayUserNickName;
        result.replygender = replayUserGender;
        result.replyavatar = replayUserAvatar;
        result.position = mLength;
        result.isSelect = false;
        result.readnow = false;
        ;

        mSocialList.add(0, result);
        mSocialFragment.updateSocialList(result);
        mSocialCountTv.setText(mSocialList.size() + "");
        if (mSocialList.size() > 0) {
            mSocialCountTv.setTextColor(Color.parseColor("#ffc445"));
        } else {
            mSocialCountTv.setTextColor(Color.parseColor("#ffffff"));
        }
        mCurrMarker.setPosition(new LatLng(mLastLa, mLastLo));
    }

    /**
     * 发送评论
     */
    public void addNewVoice(final int replayUserId, final String comment,
                            final String replayUserNickName, final String replayUserName,
                            final int replayUserGender, final String replayUserAvatar) {

        //改变列表
        DBUserEntity user = LocalApplication.getInstance().getLoginUser(mContext);

        LiveSocial result = new LiveSocial();
        result.time = TimeUtils.NowTime();
        result.type = 2;
        result.userid = user.userId;
        result.longitude = mLastLo;
        result.latitude = mLastLa;
        result.comment = comment;
        result.mediatype = 2;
        result.mediaurl = comment.split("&:&")[1];
        result.length = Integer.parseInt(comment.split("&:&")[0]);
        result.usernickname = user.nickname;
        result.useravatar = user.avatar;
        result.replyuserid = replayUserId;
        result.replyusername = replayUserName;
        result.replynickname = replayUserNickName;
        result.replygender = replayUserGender;
        result.replyavatar = replayUserAvatar;
        result.position = mLength;
        result.isSelect = true;
        result.readnow = false;
        ;
//        Log.v(TAG, "mediaurl:" + result.mediaurl);
        mSocialList.add(0, result);
        mSocialFragment.updateSocialList(result);
        mSocialCountTv.setText(mSocialList.size() + "");
        if (mSocialList.size() > 0) {
            mSocialCountTv.setTextColor(Color.parseColor("#ffc445"));
        } else {
            mSocialCountTv.setTextColor(Color.parseColor("#ffffff"));
        }
        mCurrMarker.setPosition(new LatLng(mLastLa, mLastLo));
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (!isShowKm || (int) mCurrZoom == (int) cameraPosition.zoom) {
            return;
        }

        if (cameraPosition.zoom > 15) {
            mSplitMarkerZoomSize = 1;
        } else if (cameraPosition.zoom > 13) {
            mSplitMarkerZoomSize = 2;
        } else if (cameraPosition.zoom > 12) {
            mSplitMarkerZoomSize = 4;
        } else if (cameraPosition.zoom > 11) {
            mSplitMarkerZoomSize = 7;
        } else if (cameraPosition.zoom > 10) {
            mSplitMarkerZoomSize = 11;
        } else {
            mSplitMarkerZoomSize = 16;
        }
        destroySpliteMarker();
        drawSpliteMarker();
        mCurrZoom = cameraPosition.zoom;
    }


}
