package com.app.pao.fragment.history;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.workout.HistoryInfoActivityV2;
import com.app.pao.config.AppEnum;
import com.app.pao.config.MapEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.PreferencesData;
import com.app.pao.data.db.HeartrateData;
import com.app.pao.data.db.LapData;
import com.app.pao.data.db.LocationData;
import com.app.pao.data.db.SpliteData;
import com.app.pao.data.db.UploadData;
import com.app.pao.entity.db.DBEntityHeartrate;
import com.app.pao.entity.db.DBEntityLap;
import com.app.pao.entity.db.DBEntityLocation;
import com.app.pao.entity.db.DBEntitySplite;
import com.app.pao.entity.db.DBEntityWorkout;
import com.app.pao.entity.db.DBUploadEntity;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.event.EventComment;
import com.app.pao.entity.event.EventPlayVoice;
import com.app.pao.entity.event.EventSendSocial;
import com.app.pao.entity.event.EventSwitchFragment;
import com.app.pao.entity.event.EventUpdateSocialPos;
import com.app.pao.entity.event.EventUpdateVideoPos;
import com.app.pao.entity.network.GetPlaybackCameraListResult;
import com.app.pao.entity.network.GetSocialListResult;
import com.app.pao.entity.network.GetThumbsResult;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.ui.widget.PlayBackChartView;
import com.app.pao.ui.widget.chart.HistoryHeartrateChartView;
import com.app.pao.ui.widget.chart.HistoryPaceChartView;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/5.
 */
public class HistoryBasicInfoFragmentV2 extends BaseFragment implements AMap.OnCameraChangeListener,
        View.OnClickListener, AMap.OnMarkerClickListener, AMap.OnMapClickListener {

    /* contains */
    private static final int MSG_REFRESH_MAP = 0x01;

    private static final int SHOW_NONE = 0;//没有显示Fragment
    private static final int SHOW_SCOIAL = 1;//显示评论点赞
    private static final int SHOW_VIDEO = 2;//显示视频


    @ViewInject(R.id.ll_base)
    public LinearLayout mBaseLl;
    /* local view about map */
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
    @ViewInject(R.id.tv_workout_start_time)
    private TextView mStartTimeTv;//开始时间文本
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
    private HistoryPaceChartView mPaceChart;
    private HistoryHeartrateChartView mHeartrateChart;

    /* local view about small data*/
    @ViewInject(R.id.ll_small_data)
    private LinearLayout mSmallDataLl;//小数据布局
    @ViewInject(R.id.tv_small_data_time)
    private TextView mSmallDataTimeTv;//小数据布局中的时间
    @ViewInject(R.id.tv_small_data_pace)
    private TextView mSmallDataPaceTv;
    @ViewInject(R.id.tv_small_data_length)
    private TextView mSmallDataLengthTv;//小数据布局中的距离
    @ViewInject(R.id.view_guid)
    private FrameLayout mGuidV;//视频导航
    /* local data */
    private Typeface typeFace;//字体

    // about amap
    private AMap mAMap;
    private float mCurrZoom;//当前放大比例

    private String mWorkoutName;//历史名称
    private DBEntityWorkout mDbWorkout;//跑步记录
    private List<DBEntityLocation> locationList = new ArrayList<DBEntityLocation>();
    private List<DBEntitySplite> splitList = new ArrayList<DBEntitySplite>();
    private List<DBEntityLap> lapList = new ArrayList<DBEntityLap>();
    private List<DBEntityHeartrate> heartrateList = new ArrayList<>();
    private List<GetSocialListResult.SocialsEntity> socialList = new ArrayList<>();
    private List<GetPlaybackCameraListResult.VideoEntity> mVieoList = new ArrayList<>();//视频列表

    /* local data about map */
    private List<Marker> mSplitMarkList = new ArrayList<Marker>();
    private List<Marker> mSocialMarkList = new ArrayList<>();//评论点赞的Mark列表
    private List<Marker> mVideoMarkList = new ArrayList<>();//视频的Mark列表

    private Marker mCurrMarker;
    private Marker mCurrVideoMarker;
    private Marker mLastVideoMarker;

    private int mSplitMarkerZoomSize;
    private double mMinLaLng = 0;
    private double mMaxLaLng = 0;
    private double mMinLoLng = 0;
    private double mMaxLoLng = 0;
    private LatLngBounds mBounds;// 地图显示范围
    private boolean isShowKm = true;

    private int mShowFragment = SHOW_NONE;
    private FragmentManager mFragmentManager;
    private HistorySocialsFragment mHistorySocialsFragment;//评论点赞Fragment
    private HistoryVideoFragment mHistoryVideoFragment;//录像Fragment


    Handler freshMapHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == MSG_REFRESH_MAP) {
                if (mBounds != null) {
                    mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mBounds, DeviceUtils.dp2px(mContext, 85)));
                } else {
                    freshMapHandler.sendEmptyMessageDelayed(MSG_REFRESH_MAP, 200);
                }
            }
        }
    };

    @Override
    @OnClick({R.id.btn_show_km, R.id.btn_back_to_center, R.id.btn_pace, R.id.btn_heartrate,
            R.id.btn_map_prise, R.id.btn_down_fragment, R.id.btn_map_video, R.id.btn_guid_close,
            R.id.btn_not_prompt})
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
            freshMapHandler.sendEmptyMessage(MSG_REFRESH_MAP);
            //点击配速
        } else if (v.getId() == R.id.btn_pace) {
            EventBus.getDefault().post(new EventSwitchFragment(EventSwitchFragment.TYPE_HISTORY, EventSwitchFragment.EVENT_HISTORY_SPLIT));
            //点击心率
        } else if (v.getId() == R.id.btn_heartrate) {
            EventBus.getDefault().post(new EventSwitchFragment(EventSwitchFragment.TYPE_HISTORY, EventSwitchFragment.EVENT_HISTORY_HEARTRATE));
            //点击评论展开按钮
        } else if (v.getId() == R.id.btn_map_prise) {
            clickSocialBtn();
            //点击向下按钮
        } else if (v.getId() == R.id.btn_down_fragment) {
            clickDownBtn();
        } else if (v.getId() == R.id.btn_map_video) {
            clickVideoBtn();
        } else if (v.getId() == R.id.btn_guid_close) {
            mGuidV.setVisibility(View.GONE);
        } else if (v.getId() == R.id.btn_not_prompt) {
            mGuidV.setVisibility(View.GONE);
            PreferencesData.setShowVideoGuid(mContext, false);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //找到点击的maker
        if (mShowFragment == SHOW_VIDEO) {
            int pos = 0;
            for (; pos < mVideoMarkList.size(); pos++) {
                if (marker.getPosition().latitude == mVideoMarkList.get(pos).getPosition().latitude
                        && marker.getPosition().longitude == mVideoMarkList.get(pos).getPosition().longitude) {
                    mCurrVideoMarker.setPosition(marker.getPosition());
                    mHistoryVideoFragment.updateHighlightPos(pos + 2);
                    mLastVideoMarker.setVisible(true);
                    mLastVideoMarker = mVideoMarkList.get(pos);
                    mLastVideoMarker.setVisible(false);
                    break;
                }
            }

        }
        return false;
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

    public static HistoryBasicInfoFragmentV2 newInstance() {
        HistoryBasicInfoFragmentV2 fragment = new HistoryBasicInfoFragmentV2();
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
        } else {
            //若已经点赞过
            String info = mDbWorkout.workoutId + ":" + AppEnum.dynamicType.WORKOUT;
            DBUploadEntity uploadEntity;
            if (event.mHasGiveThumbup == AppEnum.hasGiveThumbup.YES) {
                T.showShort(mContext, "您已经赞过啦");
//                uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_THUMB_UP_NO, URLConfig
//                        .URL_THUMB_UP_NO, info, "", LocalApplication.getInstance().getLoginUser(mContext).getUserId());
//                int cancelThumbPosition = 0;
//                for (; cancelThumbPosition < socialList.size(); cancelThumbPosition++) {
//                    if (socialList.get(cancelThumbPosition).getUserid() == LocalApplication.getInstance().getLoginUser(mContext).getUserId()
//                            && socialList.get(cancelThumbPosition).socialtype == 1) {
//                        socialList.remove(cancelThumbPosition);
//                        break;
//                    }
//                }
//                mHistorySocialsFragment.cancelThumb(cancelThumbPosition);
//                T.showShort(mContext, "取消点赞");
                //DO NOTHING
            } else {
                uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_THUMB_UP_YES, URLConfig
                        .URL_THUMB_UP_YES, info
                        , "", LocalApplication.getInstance().getLoginUser(mContext).getUserId());

                DBEntityLocation entityLocation = locationList.get(locationList.size() - 1);
                DBUserEntity user = LocalApplication.getInstance().getLoginUser(mContext);
                GetSocialListResult.SocialsEntity result = new GetSocialListResult.SocialsEntity();
                result.intsocialtime = System.currentTimeMillis();
                result.socialtime = TimeUtils.NowTime();
                result.socialtype = 1;
                result.userid = user.userId;
                result.id = 0;
                result.longitude = entityLocation.longitude;
                result.latitude = entityLocation.latitude;
                result.comment = "";
                result.mediatype = 1;
                result.mediaurl = "";
                result.length = 0;
                result.username = user.name;
                result.nickname = user.nickname;
                result.gender = user.gender;
                result.avatar = user.avatar;
                result.replyuserid = 0;
                result.replyusername = "";
                result.replynickname = "";
                result.replygender = 1;
                result.replyavatar = "";
                result.position = mDbWorkout.length;
                result.isSelect = false;
                result.readnow = false;
                socialList.add(0, result);
                mHistorySocialsFragment.andThumb(result);
                mCurrMarker.setPosition(new LatLng(entityLocation.latitude, entityLocation.longitude));
                //发送点赞请求
                UploadData.createNewUploadData(mContext, uploadEntity);
                T.showShort(mContext, "点赞成功");
            }
            mSocialCountTv.setText(socialList.size() + "");
            if (socialList.size() > 0) {
                mSocialCountTv.setTextColor(Color.parseColor("#ffc445"));
            } else {
                mSocialCountTv.setTextColor(Color.parseColor("#ffffff"));
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history_basic_info_v2;
    }

    @Override
    protected void initParams() {
        mapView.onCreate(mSavedInstanceState);
        EventBus.getDefault().register(this);
        mFragmentManager = getFragmentManager();
        setUpMap();
        //初始化文本字体
        typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
        mStartTimeTv.setTypeface(typeFace);
        mLengthTv.setTypeface(typeFace);
        mDurationTv.setTypeface(typeFace);
        mPaceTv.setTypeface(typeFace);
        mCalorieTv.setTypeface(typeFace);
        mHeartrateTv.setTypeface(typeFace);
        mSmallDataTimeTv.setTypeface(typeFace);
        mSmallDataPaceTv.setTypeface(typeFace);
        mSmallDataLengthTv.setTypeface(typeFace);
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
     * 通知GC
     */
    private void causeGC() {
        locationList.clear();
        splitList.clear();
        lapList.clear();
        heartrateList.clear();
        socialList.clear();
        mDbWorkout = null;
    }

    /**
     * 更新workout数据
     */
    public void updateWorkoutView() {
        updateData();
        updateViews();

    }

    /**
     * 更新数据
     */
    private void updateData() {
        HistoryInfoActivityV2 activity = (HistoryInfoActivityV2) getActivity();
        mWorkoutName = activity.getWorkoutName();
        mDbWorkout = activity.getDBWorkout();
        socialList = activity.getSocialList();
        mVieoList = activity.getVieoList();

        // 获取location 信息
        locationList = LocationData.getAllDbLocFromWorkout(mContext, mWorkoutName);
        splitList = SpliteData.getSpliteFromWorkout(mContext, mWorkoutName);
        lapList = LapData.getLapFromWorkout(mContext, mWorkoutName);
        heartrateList = HeartrateData.getAllHeartrateFromWork(mContext, mWorkoutName);

        DBEntityLocation lastLoc = locationList.get(locationList.size() - 1);
        for (int i = 0, socialsize = socialList.size(); i < socialsize; i++) {
            GetSocialListResult.SocialsEntity entity = socialList.get(i);
            if (entity.longitude == 0 || entity.latitude == 0) {
                entity.setLatitude(lastLoc.latitude);
                entity.setLongitude(lastLoc.longitude);
            }
            socialList.set(i, entity);
        }

        //pace chart
        mPaceChart = new HistoryPaceChartView(mContext);
        mPaceChart.initViews(0, locationList.size());
        mPaceChart.setEnabled(false);
        mPaceChart.addLine(locationList, Color.parseColor("#0068ffa1"), true);

        //heart rate chart
        if (heartrateList.size() > 0) {
            mHeartrateChart = new HistoryHeartrateChartView(mContext);
            mHeartrateChart.initViews(0, heartrateList.size());
            mHeartrateChart.setEnabled(false);
            mHeartrateChart.addLine(heartrateList, Color.parseColor("#0028caa6"), true);
        }

    }

    /**
     * 更新页面
     */
    private void updateViews() {
        //开始时间文本
        String time = TimeUtils.getHistoryMapStartTime(mDbWorkout.starttime);
        mStartTimeTv.setText(time);
        mSmallDataTimeTv.setText(time);
        // 距离
        String length = NumUtils.retainTheDecimal(mDbWorkout.length);
        mLengthTv.setText(length);
        mSmallDataLengthTv.setText(length + "公里");
        //耗时
        String duration = TimeUtils.formatSecondsToLongHourTime(mDbWorkout.duration);
        mDurationTv.setText(duration);
        //平均配速
        mPaceTv.setText(TimeUtils.formatSecondsToSpeedTime((long) (mDbWorkout.duration * 1000 / mDbWorkout.length)));
        mSmallDataPaceTv.setText(TimeUtils.formatSecondsToSpeedTime((long) (mDbWorkout.duration * 1000 / mDbWorkout.length)) + "/公里");
        // 卡路里(千卡)
        float Caloriestemp = mDbWorkout.getCalorie();
        BigDecimal Caloriesb = new BigDecimal(Caloriestemp);
        mCalorieTv.setText(Caloriesb.setScale(0, BigDecimal.ROUND_HALF_UP).intValue() + ""); // floatValue
        //点赞评论数量
        mSocialCountTv.setText(socialList.size() + "");
        if (socialList.size() > 0) {
            mSocialCountTv.setTextColor(Color.parseColor("#ffc445"));
        } else {
            mSocialCountTv.setTextColor(Color.parseColor("#ffffff"));
        }

        int totalHeartCount = 0;
        int avgHeartrate = 0;
        if (heartrateList.size() != 0) {
            // 心率计算
            for (DBEntityHeartrate heartrate : heartrateList) {
                totalHeartCount += heartrate.getBmp();
            }
            avgHeartrate = totalHeartCount / heartrateList.size();
        }
        //平均心率
        if (avgHeartrate != 0) {
            mHeartrateTv.setText(avgHeartrate + "");
        } else {
            mHeartrateTv.setText("- -");
        }

        //设置pace chart
        mPaceChartLl.addView(mPaceChart, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        //设置heartrate chart
        if (heartrateList.size() > 0) {
            mHeartrateChartLl.addView(mHeartrateChart, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }

        //视频数量
        if (mVieoList.size() == 0) {
            mShowVideoBtn.setVisibility(View.GONE);
        } else {
            mShowVideoBtn.setVisibility(View.VISIBLE);
            mVideoCountTv.setText(mVieoList.size() + "");
            if (PreferencesData.getShowVideoGuid(mContext)) {
                mGuidV.setVisibility(View.VISIBLE);
            }

        }
        //绘制地图
        freshMap();
    }

    /**
     * 初始化地图
     */
    private void setUpMap() {
        if (mAMap == null) {
            mAMap = mapView.getMap();
            mAMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
            mAMap.getUiSettings().setZoomControlsEnabled(false);
            mAMap.setOnCameraChangeListener(this);
            mAMap.setOnMarkerClickListener(this);
            mAMap.setOnMapClickListener(this);

            mSplitMarkerZoomSize = 1;
        }
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

    /**
     * 刷新地图
     */
    private void freshMap() {
        mAMap.clear();
        if (locationList != null && locationList.size() > 0) {

            // 起始点位置
            mAMap.addMarker(
                    new MarkerOptions().anchor(0.5f, 0.5f)
                            .position(new LatLng(locationList.get(0).latitude,
                                    locationList.get(0).longitude))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_run_start))
                            .draggable(true));
            // 终点位置
            mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(new LatLng(locationList.get(locationList.size() - 1).latitude,
                            locationList.get(locationList.size() - 1).longitude))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_end)).draggable(true));

            drawMask(locationList.get(0));
            drawSpliteMarker();
            //画线
            new Thread() {
                public void run() {
                    PolylineOptions polylineOption = null;
                    String lapstartTime = "";
                    for (int i = 0; i < locationList.size(); i++) {
                        DBEntityLocation location = locationList.get(i);
                        if (!lapstartTime.equals(location.getLapStartTime())) {
                            if (polylineOption != null) {
                                polylineOption.width(MapEnum.WIDTH_POLYLINE)
                                        .color(MapEnum.COLOR_POLYLINE).zIndex(MapEnum.ZINDEX_POLYLINE);
                                mAMap.addPolyline(polylineOption);
                            }
                            polylineOption = new PolylineOptions();
                            lapstartTime = location.getLapStartTime();
                        }
                        polylineOption.add(new LatLng(location.getLatitude(), location.getLongitude()));
                        if (location.getLatitude() < mMinLaLng || mMinLaLng == 0) {
                            mMinLaLng = location.getLatitude();
                        }
                        if (location.getLatitude() > mMaxLaLng || mMaxLaLng == 0) {
                            mMaxLaLng = location.getLatitude();
                        }
                        if (location.getLongitude() < mMinLoLng || mMinLoLng == 0) {
                            mMinLoLng = location.getLongitude();
                        }
                        if (location.getLongitude() > mMaxLoLng || mMaxLoLng == 0) {
                            mMaxLoLng = location.getLongitude();
                        }
                    }
                    if (polylineOption != null) {
                        polylineOption.width(MapEnum.WIDTH_POLYLINE)
                                .color(MapEnum.COLOR_POLYLINE).zIndex(MapEnum.ZINDEX_POLYLINE);
                        mAMap.addPolyline(polylineOption);
                    }
                    if (mMinLaLng != 0 && mMaxLaLng != 0 && mMinLoLng != 0 && mMaxLoLng != 0) {
                        mBounds = new LatLngBounds(new LatLng(mMinLaLng, mMinLoLng), new LatLng(mMaxLaLng, mMaxLoLng));
                    }
                    //画分段虚线
//                    if (lapList.size() > 1) {
//                        LatLng startLoc = null;
//                        LatLng endLoc = null;
//                        for (int i = 0; i < lapList.size(); ) {
//                            //若没有起点
//                            if (startLoc == null) {
//                                DBEntityLap lap = lapList.get(i);
//                                DBEntityLocation location = LocationData.getLocationByLap(mContext, lap, false);
//                                if (location == null) {
//                                    i++;
//                                    continue;
//                                } else {
//                                    startLoc = new LatLng(location.getLatitude(), location.getLongitude());
//                                    i++;
//                                    continue;
//                                }
//                            }
//                            //若没有终点
//                            if (endLoc == null) {
//                                DBEntityLap lap = lapList.get(i);
//                                DBEntityLocation location = LocationData.getLocationByLap(mContext, lap, true);
//                                if (location == null) {
//                                    i++;
//                                    continue;
//                                    //找到终点
//                                } else {
//                                    endLoc = new LatLng(location.getLatitude(), location.getLongitude());
//                                    polylineOption = new PolylineOptions();
//                                    polylineOption.add(startLoc, endLoc)
//                                            .width(MapEnum.WIDTH_POLYLINE).setDottedLine(true).geodesic(true)
//                                            .color(MapEnum.COLOR_PAUSE_LINE).zIndex(MapEnum.ZINDEX_POLYLINE);
//                                    mAMap.addPolyline(polylineOption);
//                                    startLoc = null;
//                                    endLoc = null;
//                                    continue;
//                                }
//                            }
//                        }
//                    }
                    freshMapHandler.sendEmptyMessage(MSG_REFRESH_MAP);
                }
            }.start();
        }
    }

    /**
     * 绘制蒙版
     */
    private void drawMask(DBEntityLocation loc) {
        // 绘制一个长方形
        mAMap.addPolygon(
                new PolygonOptions().addAll(createRectangle(new LatLng(loc.latitude, loc.longitude), 10, 10))
                        .fillColor(MapEnum.COLOR_MASK).strokeColor(Color.TRANSPARENT).strokeWidth(1))
                .setZIndex(MapEnum.ZINDEX_MASK);
    }

    /**
     * 生成一个长方形的四个坐标点
     */
    private List<LatLng> createRectangle(LatLng center, double halfWidth, double halfHeight) {
        return Arrays.asList(new LatLng(center.latitude - halfHeight, center.longitude - halfWidth),
                new LatLng(center.latitude - halfHeight, center.longitude + halfWidth),
                new LatLng(center.latitude + halfHeight, center.longitude + halfWidth),
                new LatLng(center.latitude + halfHeight, center.longitude - halfWidth));
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
            DBEntitySplite split = splitList.get(i);
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
     * 画评论标记
     */
    private void drawSocialMarker() {
        if (socialList.size() == 0) {
            mCurrMarker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(new LatLng(0, 0))
                    .draggable(false).zIndex(MapEnum.ZINDEX_SOCIAL_BIG)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_social_mark_big)));
            return;
        }
        for (int i = 0, socialSize = socialList.size(); i < socialSize; i++) {
            GetSocialListResult.SocialsEntity entity = socialList.get(i);
            LatLng latLng = new LatLng(entity.latitude, entity.longitude);
            mSocialMarkList.add(mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(latLng).draggable(false).zIndex(MapEnum.ZINDEX_SOCIAL)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_social_mark))));
        }
        GetSocialListResult.SocialsEntity entity = socialList.get(0);
        mCurrMarker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .position(new LatLng(entity.latitude, entity.longitude))
                .draggable(false).zIndex(MapEnum.ZINDEX_SOCIAL_BIG)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_social_mark_big)));
    }


    /**
     * 销毁评论标记
     */
    private void destroySocialMarker() {
        if (socialList.size() == 0) {
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
        if (mVieoList.size() == 0) {
            mCurrVideoMarker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(new LatLng(0, 0))
                    .draggable(false).zIndex(MapEnum.ZINDEX_SOCIAL_BIG)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_mark_video_big)));
            return;
        }
        for (int i = 0, videoSize = mVieoList.size(); i < videoSize; i++) {
            GetPlaybackCameraListResult.VideoEntity entity = mVieoList.get(i);
            LatLng latLng = new LatLng(entity.cameralatitude, entity.cameralongitude);
            mVideoMarkList.add(mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .position(latLng).draggable(false).zIndex(MapEnum.ZINDEX_SOCIAL)
                    .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.GetVideoBitmap(mContext, (i + 1), typeFace)))));
        }
        mLastVideoMarker = mVideoMarkList.get(mVieoList.size() / 2);
        mLastVideoMarker.setVisible(false);
        GetPlaybackCameraListResult.VideoEntity entity = mVieoList.get(mVieoList.size() / 2);
        mCurrVideoMarker = mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .position(new LatLng(entity.cameralatitude, entity.cameralongitude))
                .draggable(false).zIndex(MapEnum.ZINDEX_SOCIAL_BIG)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_mark_video_big)));
    }


    /**
     * 销毁评论标记
     */
    private void destroyVideoMarker() {
        if (mVieoList.size() == 0) {
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
     * 点击评论点赞按钮
     */
    private void clickSocialBtn() {
        //若当前状态是不显示任何Fragment
        if (mShowFragment == SHOW_NONE) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            if (mHistorySocialsFragment == null) {
                mHistorySocialsFragment = HistorySocialsFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putSerializable("socialList", (Serializable) socialList);
                bundle.putDouble("latitude", locationList.get(locationList.size() - 1).latitude);
                bundle.putDouble("longitude", locationList.get(locationList.size() - 1).longitude);
                bundle.putDouble("length", mDbWorkout.length);
                mHistorySocialsFragment.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                        .add(R.id.ll_fragment_social, mHistorySocialsFragment)
                        .commitAllowingStateLoss();
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                        .show(mHistorySocialsFragment)
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
                    .hide(mHistorySocialsFragment)
                    .commitAllowingStateLoss();
            mDownFragmentBtn.setVisibility(View.GONE);
            mBigDataLl.setVisibility(View.VISIBLE);
            mSmallDataLl.setVisibility(View.INVISIBLE);
            mShowSocialBtn.setBackgroundResource(R.drawable.btn_map_big_off);
            if (socialList.size() > 0) {
                mSocialCountTv.setTextColor(Color.parseColor("#ffc445"));
            } else {
                mSocialCountTv.setTextColor(Color.parseColor("#ffffff"));
            }
            mShowFragment = SHOW_NONE;
            destroySocialMarker();
            //若当前状态显示的是视频
        } else {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            if (mHistorySocialsFragment == null) {
                mHistorySocialsFragment = HistorySocialsFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putSerializable("socialList", (Serializable) socialList);
                bundle.putDouble("latitude", locationList.get(locationList.size() - 1).latitude);
                bundle.putDouble("longitude", locationList.get(locationList.size() - 1).longitude);
                bundle.putDouble("length", mDbWorkout.length);
                mHistorySocialsFragment.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                        .add(R.id.ll_fragment_social, mHistorySocialsFragment).hide(mHistoryVideoFragment)
                        .commitAllowingStateLoss();
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                        .hide(mHistoryVideoFragment).show(mHistorySocialsFragment)
                        .commitAllowingStateLoss();
            }
            drawSocialMarker();
            destroyVideoMarker();
            mShowSocialBtn.setBackgroundResource(R.drawable.btn_map_big_on);
            mSocialCountTv.setTextColor(Color.parseColor("#ffffff"));
            mShowVideoBtn.setBackgroundResource(R.drawable.btn_map_big_off);
            if (mVieoList.size() > 0) {
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
            if (mHistoryVideoFragment == null) {
                mHistoryVideoFragment = HistoryVideoFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putSerializable("videoList", (Serializable) mVieoList);
                bundle.putInt("workoutid", mDbWorkout.workoutId);
                bundle.putInt("userid", LocalApplication.getInstance().getLoginUser(mContext).userId);
                mHistoryVideoFragment.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                        .add(R.id.ll_fragment_social, mHistoryVideoFragment)
                        .commitAllowingStateLoss();
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                        .show(mHistoryVideoFragment)
                        .commitAllowingStateLoss();
            }
            drawVideoMarker();
            mDownFragmentBtn.setVisibility(View.VISIBLE);
            mVideoCountTv.setTextColor(Color.parseColor("#ffffff"));
            mShowVideoBtn.setBackgroundResource(R.drawable.btn_map_big_on);
            mBigDataLl.setVisibility(View.INVISIBLE);
            mSmallDataLl.setVisibility(View.VISIBLE);
            mShowFragment = SHOW_VIDEO;
            //若当前状态是显示视频
        } else if (mShowFragment == SHOW_VIDEO) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                    .hide(mHistoryVideoFragment)
                    .commitAllowingStateLoss();
            destroyVideoMarker();
            mDownFragmentBtn.setVisibility(View.GONE);
            mBigDataLl.setVisibility(View.VISIBLE);
            mSmallDataLl.setVisibility(View.INVISIBLE);
            mShowVideoBtn.setBackgroundResource(R.drawable.btn_map_big_off);
            if (mVieoList.size() > 0) {
                mVideoCountTv.setTextColor(Color.parseColor("#ffc445"));
            } else {
                mVideoCountTv.setTextColor(Color.parseColor("#ffffff"));
            }
            mShowFragment = SHOW_NONE;
            //当前状态显示评论点赞
        } else {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            if (mHistoryVideoFragment == null) {
                mHistoryVideoFragment = HistoryVideoFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putSerializable("videoList", (Serializable) mVieoList);
                bundle.putInt("userid", LocalApplication.getInstance().getLoginUser(mContext).userId);
                mHistoryVideoFragment.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                        .add(R.id.ll_fragment_social, mHistoryVideoFragment).hide(mHistorySocialsFragment)
                        .commitAllowingStateLoss();
            } else {
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out)
                        .hide(mHistorySocialsFragment).show(mHistoryVideoFragment)
                        .commitAllowingStateLoss();
            }
            destroySocialMarker();
            drawVideoMarker();
            mShowVideoBtn.setBackgroundResource(R.drawable.btn_map_big_on);
            mVideoCountTv.setTextColor(Color.parseColor("#ffffff"));
            mShowSocialBtn.setBackgroundResource(R.drawable.btn_map_big_off);
            if (socialList.size() > 0) {
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
     * 发送评论
     */
    public void addNewComments(final int replayUserId, final String comment,
                               final String replayUserNickName, final String replayUserName,
                               final int replayUserGender, final String replayUserAvatar) {
        //发送评论
        String info = mDbWorkout.getWorkoutId() + ":" + AppEnum.dynamicType.WORKOUT
                + ":" + replayUserId + ":" + comment;

        DBUploadEntity uploadEntity = new DBUploadEntity(DBUploadEntity.TYPE_COMMENTS, URLConfig
                .URL_ADD_COMMENT, info, "", LocalApplication.getInstance().getLoginUser(mContext).getUserId());
        UploadData.createNewUploadData(mContext, uploadEntity);


        //改变列表
        DBEntityLocation entityLocation = locationList.get(locationList.size() - 1);
        DBUserEntity user = LocalApplication.getInstance().getLoginUser(mContext);

        GetSocialListResult.SocialsEntity result = new GetSocialListResult.SocialsEntity();
        result.intsocialtime = System.currentTimeMillis();
        result.socialtime = TimeUtils.NowTime();
        result.socialtype = 2;
        result.userid = user.userId;
        result.id = 0;
        result.longitude = entityLocation.longitude;
        result.latitude = entityLocation.latitude;
        result.comment = comment;
        result.mediatype = 1;
        result.mediaurl = "";
        result.length = 0;
        result.username = user.name;
        result.nickname = user.nickname;
        result.gender = user.gender;
        result.avatar = user.avatar;
        result.replyuserid = replayUserId;
        result.replyusername = replayUserName;
        result.replynickname = replayUserNickName;
        result.replygender = replayUserGender;
        result.replyavatar = replayUserAvatar;
        result.position = mDbWorkout.length;
        result.isSelect = false;
        result.readnow = false;
        ;

        socialList.add(0, result);
        mHistorySocialsFragment.updateSocialList(result);
        mSocialCountTv.setText(socialList.size() + "");
        if (socialList.size() > 0) {
            mSocialCountTv.setTextColor(Color.parseColor("#ffc445"));
        } else {
            mSocialCountTv.setTextColor(Color.parseColor("#ffffff"));
        }
        mCurrMarker.setPosition(new LatLng(entityLocation.latitude, entityLocation.longitude));
    }


}
