package com.app.pao.fragment.settings;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.main.MainActivityV2;
import com.app.pao.activity.main.ZoomBigImageActivity;
import com.app.pao.activity.settings.MypbActivity;
import com.app.pao.activity.settings.SystemSettingsActivity;
import com.app.pao.activity.settings.UserBasicInfoActivity;
import com.app.pao.activity.settings.UserLevelCardActivity;
import com.app.pao.activity.workout.HistoryInfoActivityV2;
import com.app.pao.activity.workout.HistoryListActivityV2;
import com.app.pao.activity.workout.MyClockListActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.MaidianData;
import com.app.pao.data.db.UserData;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.model.UserOptMedalEntity;
import com.app.pao.entity.network.GetPersonRecordResult;
import com.app.pao.entity.network.GetUserInfoResult;
import com.app.pao.entity.network.GetWxInviteResult;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.ui.widget.pulltozoomview.PullToZoomScrollViewEx;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.app.pao.utils.wx.WxShareManager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LY on 2016/5/11.
 */
public class LocalUserFragmentV2 extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "LocalUserFragmentV2";
    private static final int MSG_POST_ERROR = 1;
    private static final int MSG_POST_OK = 2;

    @ViewInject(R.id.sv_local_user_base)
    private PullToZoomScrollViewEx mScorllView;
    @ViewInject(R.id.ll_title)
    public LinearLayout mTitleLl;

    private View headView, zoomView, contentView;
    private BitmapUtils mBitmapUtils;

    /*headView*/
    private TextView mCardTypeTv;//卡片类型文本
    private TextView mHowToNextLevelTv;//目标达成文本(xxx/xxxx公里 升级)
    private TextView mFriendNumTv;//好友数量文本
    private TextView mGroupNumTv;//跑团数量文本
    private CircularImage mUserAvatarCi;//用户头像
    private LinearLayout mCardTypeLl;//卡片类型框，用于实现查看卡片详情的点击事件
    private View mRunProgressPb;//标注跑步距下一阶段进度条
    private TextView mNickNameTv;//用户昵称文本
    private LinearLayout mFriendLl;//好友布局
    private LinearLayout mGroupLl;//跑团布局
    private LinearLayout mLevelLl;//等级页面

    /*zoomView*/
    private LinearLayout mRunCardBgLl;//拉伸部分
    private ImageView mRunCardIv;//拉伸图片

    /*contentView*/
    private TextView mAlarmNumTv;//闹钟数量文本
    private TextView mTotalRunCountTv;//总跑步数量文本
    private TextView mTotalDistanceTv;//总里程文本
    private TextView mWeekAverageTv;//平均周跑量文本
    private TextView mTotalCalorieTv;//总消耗文本
    private LinearLayout mAlarmNumLl;//闹钟详情点击框
    private LinearLayout mNoAlarmLl;//没有闹钟显示框
    private LinearLayout mTotalRunCountLl;//总跑步详情点击框
    private LinearLayout mMyMedalLl;//我的奖牌点击框
    private LinearLayout mAllMedalLl;//所有奖牌显示框
    private LinearLayout mNoMedalLl;//没有奖牌显示框


    private int userId;
    private DBUserEntity mUserEntity;//用户信息
    private GetPersonRecordResult mPersonRecordEntity;//跑步信息
    private Typeface typeface;//用于设置字体类型

    private boolean mIsReadSql;//判断是数据库中读取的数据时不进行弹框

    //处理通过网络获取数据后，页面刷新
    private Handler mPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, (String) msg.obj);
            } else if (msg.what == MSG_POST_OK) {
                setUserEntitytoView();
            }
        }
    };

    public static LocalUserFragmentV2 newInstance() {
        LocalUserFragmentV2 fragment = new LocalUserFragmentV2();
        return fragment;
    }


    @Override
    @OnClick({R.id.ll_user_set, R.id.title_my_live, R.id.title_app_settings})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user_set:
                startActivity(UserBasicInfoActivity.class);
                break;
            case R.id.title_my_live:
                MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "",
                        AppEnum.MaidianType.SharePersonalLivingRoomInMine, TimeUtils.NowTime()));
                shareLiveRoom();
                break;
            case R.id.title_app_settings:
                startActivity(SystemSettingsActivity.class);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_local_user_v2;
    }

    @Override
    protected void initParams() {
        initDates();
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsReadSql = false;
        postGetUserRunInfo();
    }

    private void initDates() {
        headView = LayoutInflater.from(mContext).inflate(R.layout.header_local_user, null, false);
        zoomView = LayoutInflater.from(mContext).inflate(R.layout.zoom_local_user, null, false);
        contentView = LayoutInflater.from(mContext).inflate(R.layout.content_local_user, null, false);
        userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        mBitmapUtils = new BitmapUtils(mContext);
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
        //获取本地数据缓存，展现在屏幕上
        mUserEntity = LocalApplication.getInstance().getLoginUser(mContext);
        if (mUserEntity != null && !mUserEntity.getRunInfo().equals("")) {
            mPersonRecordEntity = JSON.parseObject(mUserEntity.getRunInfo(), GetPersonRecordResult.class);
            mIsReadSql = true;
        }
    }

    private void initViews() {
        mScorllView.setHeaderView(headView);
        mScorllView.setZoomView(zoomView);
        mScorllView.setScrollContentView(contentView);
//        mScorllView.setZoomEnabled(false);//用于设置Zoom部分是否可以实现阻尼效果，true为可以，false为不可以，默认为true
        mScorllView.setParallax(false);//用于设置Zoom部分和Content部分位置关联
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        final int mScreenHeight = localDisplayMetrics.heightPixels;
        final int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (5.0F * (mScreenWidth / 6.0F)));

        mScorllView.setHeaderLayoutParams(localObject);
        mScorllView.setShowTitleColorListener(new PullToZoomScrollViewEx.ShowTitleColorListener() {

            @Override
            public void scrollYChange(int y) {
                float alpha = y / 600.0f;
                mTitleLl.setAlpha(alpha);
            }
        });
        setHeaderUI();
        setZoomUI();
        setContentUi();
        if (mUserEntity != null && !mUserEntity.getRunInfo().equals("")) {
            setUserEntitytoView();
        }
    }


    /**
     * 关联Header部分的UI和点击事件
     */
    private void setHeaderUI() {
        mNickNameTv = (TextView) headView.findViewById(R.id.tv_nickname);
        mCardTypeTv = (TextView) headView.findViewById(R.id.tv_card_type);
        mHowToNextLevelTv = (TextView) headView.findViewById(R.id.tv_how_to_next_level);
        mFriendNumTv = (TextView) headView.findViewById(R.id.tv_friend_num);
        mGroupNumTv = (TextView) headView.findViewById(R.id.tv_group_num);
        mUserAvatarCi = (CircularImage) headView.findViewById(R.id.civ_user_avatar);
        mCardTypeLl = (LinearLayout) headView.findViewById(R.id.ll_card_type);
        mLevelLl = (LinearLayout) headView.findViewById(R.id.v_level);

        mRunProgressPb = headView.findViewById(R.id.v_level_progress);
        mFriendLl = (LinearLayout) headView.findViewById(R.id.ll_friend);
        mGroupLl = (LinearLayout) headView.findViewById(R.id.ll_group);
        mUserAvatarCi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("avatar", mUserEntity.getAvatar());
                startActivity(ZoomBigImageActivity.class, bundle);
            }
        });
        mCardTypeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("avatar", mUserEntity.getAvatar());
                bundle.putString("nickname", mUserEntity.getNickname());
                bundle.putFloat("totallength", mPersonRecordEntity.getTotallength());
                startActivity(UserLevelCardActivity.class, bundle);
            }
        });
        mFriendLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFriendList();
            }
        });
        mGroupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGroupList();
            }
        });
    }

    /**
     * 关联Zoom部分的UI和点击事件
     */
    private void setZoomUI() {
        mRunCardBgLl = (LinearLayout) zoomView.findViewById(R.id.ll_run_card);
        mRunCardIv = (ImageView) zoomView.findViewById(R.id.iv_run_card);
    }

    /**
     * 关联Content部分的UI和点击事件
     */
    private void setContentUi() {
        mAlarmNumTv = (TextView) contentView.findViewById(R.id.tv_user_alarm_num);
        mTotalRunCountTv = (TextView) contentView.findViewById(R.id.tv_total_run_count);
        mTotalDistanceTv = (TextView) contentView.findViewById(R.id.tv_user_total_distance);
        mTotalDistanceTv.setTypeface(typeface);
        mWeekAverageTv = (TextView) contentView.findViewById(R.id.tv_user_week_average_mileage);
        mWeekAverageTv.setTypeface(typeface);
        mTotalCalorieTv = (TextView) contentView.findViewById(R.id.tv_user_total_calorie);
        mTotalCalorieTv.setTypeface(typeface);
        mAlarmNumLl = (LinearLayout) contentView.findViewById(R.id.ll_user_alarm_num);
        mNoAlarmLl = (LinearLayout) contentView.findViewById(R.id.ll_no_alarm);
        mTotalRunCountLl = (LinearLayout) contentView.findViewById(R.id.ll_total_run_count);
        mMyMedalLl = (LinearLayout) contentView.findViewById(R.id.ll_my_medal);
        mAllMedalLl = (LinearLayout) contentView.findViewById(R.id.ll_obt_medal);
        mNoMedalLl = (LinearLayout) contentView.findViewById(R.id.ll_no_medal);
        mAlarmNumLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchClockAct();
            }
        });
        mTotalRunCountLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HistoryListActivityV2.class);
            }
        });
        mMyMedalLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPersonRecordEntity != null) {
                    Bundle b = new Bundle();
                    b.putSerializable("mPersonRecordEntity", mPersonRecordEntity);
                    startActivity(MypbActivity.class, b);
                }
            }
        });
    }

    /**
     * 将用户详情填入对应的UI使界面完整
     */
    private void setUserEntitytoView() {
        ImageUtils.loadUserImage(mUserEntity.getAvatar(), mUserAvatarCi);
        mNickNameTv.setText(mUserEntity.nickname);
        float length = mPersonRecordEntity.getTotallength();
        mCardTypeTv.setText(NumUtils.getCardTypeString(length) + "行者");
        int level = NumUtils.getTotalLengthLevel(length);
        int lastLevelLength = NumUtils.getLevelLength(level);
        int nextLevelLength = NumUtils.getNextLevelLength(level);
        if (level == 8) {
            mHowToNextLevelTv.setText("你已经是最强王者");
            mLevelLl.setVisibility(View.GONE);
        } else {
            int percent = ((((int) (length / 1000)) - lastLevelLength / 1000) * 100 / ((nextLevelLength - lastLevelLength) / 1000));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mRunProgressPb.getLayoutParams();
            lp.width = (int) (DeviceUtils.dpToPixel(142) * percent / 100);
            mRunProgressPb.setLayoutParams(lp);
            mHowToNextLevelTv.setText((((int) (length / 1000)) - lastLevelLength / 1000) + "/" + (nextLevelLength - lastLevelLength) / 1000 + "公里 升级");
        }

        mFriendNumTv.setText(mUserEntity.getFriendcount() + "");
        mGroupNumTv.setText(mUserEntity.getGroupcount() + "");
        int alarmNum = mUserEntity.getClockcount();
        if (alarmNum == 0) {
            mNoAlarmLl.setVisibility(View.VISIBLE);
            mAlarmNumLl.setVisibility(View.GONE);
        } else {
            mNoAlarmLl.setVisibility(View.GONE);
            mAlarmNumLl.setVisibility(View.VISIBLE);
            mAlarmNumTv.setText(mUserEntity.getClockcount() + "");
        }
//        mRunCardBgLl.setBackgroundResource(NumUtils.parseBgFromLength(length));
        mRunCardIv.setImageResource(NumUtils.parseBgFromLength(length));

        mTotalRunCountTv.setText(mPersonRecordEntity.getTotalcount() + "");
        mTotalDistanceTv.setText(NumUtils.retainTheDecimal(mPersonRecordEntity.getTotallength()));
        mWeekAverageTv.setText(NumUtils.retainTheDecimal(mPersonRecordEntity.getWeeklength()));
        mTotalCalorieTv.setText(NumUtils.retainTheDecimal(mPersonRecordEntity.getTotalcalorie() * 1000));
        updateView();
    }

    /**
     * 对奖牌部分的布局
     */
    private void updateView() {
        List<UserOptMedalEntity> mMedalList = new ArrayList<>();
        List<UserOptMedalEntity> mChangeMedalList = new ArrayList<>();
        //移除上次UI
        mAllMedalLl.removeAllViews();
        //将数据重新插入到list中，便于操作
        //最长距离
        if (mPersonRecordEntity.getLongest().getWorkoutid() != 0) {
            UserOptMedalEntity mMedalEntity = new UserOptMedalEntity(mPersonRecordEntity.getLongest(), UserOptMedalEntity.TYPE_LONGEST);
            mMedalList.add(mMedalEntity);
            if (medalHasChange(mPersonRecordEntity.getLongest()) && !mIsReadSql) {
                mChangeMedalList.add(mMedalEntity);
            }
        }
        //最长时间
        if (mPersonRecordEntity.getMaxduration().getWorkoutid() != 0) {
            UserOptMedalEntity mMedalEntity = new UserOptMedalEntity(mPersonRecordEntity.getMaxduration(), UserOptMedalEntity.TYPE_MAX_DURATION);
            mMedalList.add(mMedalEntity);
            if (medalHasChange(mPersonRecordEntity.getMaxduration()) && !mIsReadSql) {
                mChangeMedalList.add(mMedalEntity);
            }
        }
        //最快配速
        if (mPersonRecordEntity.getFastestpace().getWorkoutid() != 0) {
            UserOptMedalEntity mMedalEntity = new UserOptMedalEntity(mPersonRecordEntity.getFastestpace(), UserOptMedalEntity.TYPE_FASTEST_PACE);
            mMedalList.add(mMedalEntity);
            if (medalHasChange(mPersonRecordEntity.getFastestpace()) && !mIsReadSql) {
                mChangeMedalList.add(mMedalEntity);
            }
        }
        //5公里最快
        if (mPersonRecordEntity.getFastest5().getWorkoutid() != 0) {
            UserOptMedalEntity mMedalEntity = new UserOptMedalEntity(mPersonRecordEntity.getFastest5(), UserOptMedalEntity.TYPE_FASTEST_5);
            mMedalList.add(mMedalEntity);
            if (medalHasChange(mPersonRecordEntity.getFastest5()) && !mIsReadSql) {
                mChangeMedalList.add(mMedalEntity);
            }
        }
        //10公里最快
        if (mPersonRecordEntity.getFastest10().getWorkoutid() != 0) {
            UserOptMedalEntity mMedalEntity = new UserOptMedalEntity(mPersonRecordEntity.getFastest10(), UserOptMedalEntity.TYPE_FASTEST_10);
            mMedalList.add(mMedalEntity);
            if (medalHasChange(mPersonRecordEntity.getFastest10()) && !mIsReadSql) {
                mChangeMedalList.add(mMedalEntity);
            }
        }
        //半马最快
        if (mPersonRecordEntity.getFastesthalfmarathon().getWorkoutid() != 0) {
            UserOptMedalEntity mMedalEntity = new UserOptMedalEntity(mPersonRecordEntity.getFastesthalfmarathon(), UserOptMedalEntity.TYPE_FASTEST_HALF_MARATHON);
            mMedalList.add(mMedalEntity);
            if (medalHasChange(mPersonRecordEntity.getFastesthalfmarathon()) && !mIsReadSql) {
                mChangeMedalList.add(mMedalEntity);
            }
        }
        //全马最快
        if (mPersonRecordEntity.getFastestfullmarathon().getWorkoutid() != 0) {
            UserOptMedalEntity mMedalEntity = new UserOptMedalEntity(mPersonRecordEntity.getFastestfullmarathon(), UserOptMedalEntity.TYPE_FASTEST_FULL_MARATHON);
            mMedalList.add(mMedalEntity);
            if (medalHasChange(mPersonRecordEntity.getFastestfullmarathon()) && !mIsReadSql) {
                mChangeMedalList.add(mMedalEntity);
            }
        }

        //若数据为0 则无符合奖牌
        if (mMedalList.size() <= 0) {
            mNoMedalLl.setVisibility(View.VISIBLE);
            mAllMedalLl.setVisibility(View.GONE);
            return;
        } else {
            mNoMedalLl.setVisibility(View.GONE);
            mAllMedalLl.setVisibility(View.VISIBLE);
        }

        //遍历数组开始根据数据在页面上添加View
        LinearLayout mLineLl = null;
        for (int i = 0; i < mMedalList.size(); i++) {
            //添加居中横向容器
            if (i == 0 || i == 3 || i == 6) {
                mLineLl = new LinearLayout(mContext);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mLineLl.setGravity(Gravity.CENTER);
                mLineLl.setOrientation(LinearLayout.HORIZONTAL);
                mAllMedalLl.addView(mLineLl, lp);

                boolean addLine = false;
                if (mMedalList.size() % 3 == 0) {
                    if ((mMedalList.size() / 3 - 1) != i / 3) {
                        addLine = true;
                    }
                } else {
                    if (mMedalList.size() / 3 != i / 3) {
                        addLine = true;
                    }
                }
                if (addLine) {
                    View dividerV = new View(mContext);
                    dividerV.setBackgroundColor(Color.parseColor("#e7e7e7"));
                    LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    dividerV.setLayoutParams(dlp);
                    mAllMedalLl.addView(dividerV);
                }
            }
            //向横向居中容器中插入item；
            final UserOptMedalEntity mMedal = mMedalList.get(i);

            //初始化item布局参数
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.include_user_obt_medal, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.width = (int) (DeviceUtils.getScreenWidth() / 3);
            //初始化布局控件
            ImageView mMedalIv = (ImageView) v.findViewById(R.id.iv_medal);
            TextView mMedalNameTv = (TextView) v.findViewById(R.id.tv_medal_name);
            TextView mMedalValueTv = (TextView) v.findViewById(R.id.tv_medal_value);
            TextView mMedalUnitTv = (TextView) v.findViewById(R.id.tv_medal_unit);
            mMedalValueTv.setTypeface(typeface);

            //奖牌的点击事件
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (mMedal.getMedalType()) {
                        case UserOptMedalEntity.TYPE_LONGEST:
                            Bundle bundleLongest = new Bundle();
                            bundleLongest.putString("workoutname", mPersonRecordEntity.getLongest().getStarttime());
                            bundleLongest.putInt("workoutid", mPersonRecordEntity.getLongest().getWorkoutid());
                            startActivity(HistoryInfoActivityV2.class, bundleLongest);
                            break;
                        case UserOptMedalEntity.TYPE_MAX_DURATION:
                            Bundle bundleDuration = new Bundle();
                            bundleDuration.putString("workoutname", mPersonRecordEntity.getMaxduration().getStarttime());
                            bundleDuration.putInt("workoutid", mPersonRecordEntity.getMaxduration().getWorkoutid());
                            startActivity(HistoryInfoActivityV2.class, bundleDuration);
                            break;
                        case UserOptMedalEntity.TYPE_FASTEST_PACE:
                            Bundle bundlePace = new Bundle();
                            bundlePace.putString("workoutname", mPersonRecordEntity.getFastestpace().getStarttime());
                            bundlePace.putInt("workoutid", mPersonRecordEntity.getFastestpace().getWorkoutid());
                            startActivity(HistoryInfoActivityV2.class, bundlePace);
                            break;
                        case UserOptMedalEntity.TYPE_FASTEST_5:
                            Bundle bundle5Fast = new Bundle();
                            bundle5Fast.putString("workoutname", mPersonRecordEntity.getFastest5().getStarttime());
                            bundle5Fast.putInt("workoutid", mPersonRecordEntity.getFastest5().getWorkoutid());
                            startActivity(HistoryInfoActivityV2.class, bundle5Fast);
                            break;
                        case UserOptMedalEntity.TYPE_FASTEST_10:
                            Bundle bundle10Fast = new Bundle();
                            bundle10Fast.putString("workoutname", mPersonRecordEntity.getFastest10().getStarttime());
                            bundle10Fast.putInt("workoutid", mPersonRecordEntity.getFastest10().getWorkoutid());
                            startActivity(HistoryInfoActivityV2.class, bundle10Fast);
                            break;
                        case UserOptMedalEntity.TYPE_FASTEST_HALF_MARATHON:
                            Bundle bundleHalf = new Bundle();
                            bundleHalf.putString("workoutname", mPersonRecordEntity.getFastesthalfmarathon().getStarttime());
                            bundleHalf.putInt("workoutid", mPersonRecordEntity.getFastesthalfmarathon().getWorkoutid());
                            startActivity(HistoryInfoActivityV2.class, bundleHalf);
                            break;
                        case UserOptMedalEntity.TYPE_FASTEST_FULL_MARATHON:
                            Bundle bundleFull = new Bundle();
                            bundleFull.putString("workoutname", mPersonRecordEntity.getFastestfullmarathon().getStarttime());
                            bundleFull.putInt("workoutid", mPersonRecordEntity.getFastestfullmarathon().getWorkoutid());
                            startActivity(HistoryInfoActivityV2.class, bundleFull);
                            break;
                    }
                }
            });
            //初始化、整理显示数据
            String mMedalNameStr = "";
            String mMedalValueStr = "";
            String mMedalUnitStr = "";
            int mMedalRes = R.drawable.icon_no_medal;
            switch (mMedal.getMedalType()) {
                //最长距离
                case UserOptMedalEntity.TYPE_LONGEST:
                    mMedalNameStr = "最长距离";
                    mMedalValueStr = NumUtils.retainTheDecimal(mMedal.getUserMedal().getLength());
                    mMedalUnitStr = "公里";
                    if (mMedal.getUserMedal().getRanking() >= 90) {
                        mMedalRes = R.drawable.icon_gold_longest;
                    } else if (mMedal.getUserMedal().getRanking() >= 70) {
                        mMedalRes = R.drawable.icon_silver_longest;
                    } else if (mMedal.getUserMedal().getRanking() >= 40) {
                        mMedalRes = R.drawable.icon_copper_longest;
                    } else {
                        mMedalRes = R.drawable.icon_iron_longest;
                    }
                    break;
                //最长时间
                case UserOptMedalEntity.TYPE_MAX_DURATION:
                    mMedalNameStr = "最长时间";
                    mMedalValueStr = TimeUtils.getTimeWithFh(mMedal.getUserMedal().getDuration());
                    if (mMedal.getUserMedal().getRanking() >= 90) {
                        mMedalRes = R.drawable.icon_gold_max_duration;
                    } else if (mMedal.getUserMedal().getRanking() >= 70) {
                        mMedalRes = R.drawable.icon_silver_max_duration;
                    } else if (mMedal.getUserMedal().getRanking() >= 40) {
                        mMedalRes = R.drawable.icon_copper_max_duration;
                    } else {
                        mMedalRes = R.drawable.icon_iron_max_duration;
                    }
                    break;
                //最快配速
                case UserOptMedalEntity.TYPE_FASTEST_PACE:
                    mMedalNameStr = "最快配速";
                    mMedalValueStr = TimeUtils.getPaceWithFh(mMedal.getUserMedal().getPace());
                    mMedalUnitStr = "/公里";
                    if (mMedal.getUserMedal().getRanking() >= 90) {
                        mMedalRes = R.drawable.icon_gold_fastest_pace;
                    } else if (mMedal.getUserMedal().getRanking() >= 70) {
                        mMedalRes = R.drawable.icon_silver_fastest_pace;
                    } else if (mMedal.getUserMedal().getRanking() >= 40) {
                        mMedalRes = R.drawable.icon_copper_fastest_pace;
                    } else {
                        mMedalRes = R.drawable.icon_iron_fastest_pace;
                    }
                    break;
                //5公里最快
                case UserOptMedalEntity.TYPE_FASTEST_5:
                    mMedalNameStr = "5公里最快";
                    mMedalValueStr = TimeUtils.getTimeWithFh(mMedal.getUserMedal().getDuration());
                    if (mMedal.getUserMedal().getRanking() >= 90) {
                        mMedalRes = R.drawable.icon_gold_fastest_5;
                    } else if (mMedal.getUserMedal().getRanking() >= 70) {
                        mMedalRes = R.drawable.icon_silver_fastest_5;
                    } else if (mMedal.getUserMedal().getRanking() >= 40) {
                        mMedalRes = R.drawable.icon_copper_fastest_5;
                    } else {
                        mMedalRes = R.drawable.icon_iron_fastest_5;
                    }
                    break;
                //10公里最快
                case UserOptMedalEntity.TYPE_FASTEST_10:
                    mMedalNameStr = "10公里最快";
                    mMedalValueStr = TimeUtils.getTimeWithFh(mMedal.getUserMedal().getDuration());
                    if (mMedal.getUserMedal().getRanking() >= 90) {
                        mMedalRes = R.drawable.icon_gold_fastest_10;
                    } else if (mMedal.getUserMedal().getRanking() >= 70) {
                        mMedalRes = R.drawable.icon_silver_fastest_10;
                    } else if (mMedal.getUserMedal().getRanking() >= 40) {
                        mMedalRes = R.drawable.icon_copper_fastest_10;
                    } else {
                        mMedalRes = R.drawable.icon_iron_fastest_10;
                    }
                    break;
                //半马最快
                case UserOptMedalEntity.TYPE_FASTEST_HALF_MARATHON:
                    mMedalNameStr = "半马最快";
                    mMedalValueStr = TimeUtils.getTimeWithFh(mMedal.getUserMedal().getDuration());
                    if (mMedal.getUserMedal().getRanking() >= 90) {
                        mMedalRes = R.drawable.icon_gold_half_marathon;
                    } else if (mMedal.getUserMedal().getRanking() >= 70) {
                        mMedalRes = R.drawable.icon_silver_half_marathon;
                    } else if (mMedal.getUserMedal().getRanking() >= 40) {
                        mMedalRes = R.drawable.icon_copper_half_marathon;
                    } else {
                        mMedalRes = R.drawable.icon_iron_half_marathon;
                    }
                    break;
                //全马最快
                case UserOptMedalEntity.TYPE_FASTEST_FULL_MARATHON:
                    mMedalNameStr = "全马最快";
                    mMedalValueStr = TimeUtils.getTimeWithFh(mMedal.getUserMedal().getDuration());
                    if (mMedal.getUserMedal().getRanking() >= 90) {
                        mMedalRes = R.drawable.icon_gold_full_marathon;
                    } else if (mMedal.getUserMedal().getRanking() >= 70) {
                        mMedalRes = R.drawable.icon_silver_full_marathon;
                    } else if (mMedal.getUserMedal().getRanking() >= 40) {
                        mMedalRes = R.drawable.icon_copper_full_marathon;
                    } else {
                        mMedalRes = R.drawable.icon_iron_full_marathon;
                    }
                    break;
            }
            //初始显示，将数据显示
            mMedalIv.setImageResource(mMedalRes);
            mMedalNameTv.setText(mMedalNameStr);
            mMedalValueTv.setText(mMedalValueStr);
            mMedalUnitTv.setText(mMedalUnitStr);
            //将每个奖牌控件加入到横向居中的行 布局中显示
            mLineLl.addView(v, lp);
        }
        //若有改变的奖牌数量，弹框
//        if (mChangeMedalList.size() > 0 && !mIsReadSql) {
//            Bundle b = new Bundle();
//            b.putSerializable("changeList", (Serializable) mChangeMedalList);
//            startActivity(UserMedalChangeDialogActivity.class, b);
//        }
    }

    /**
     * 判断奖牌是否改变（降级）
     *
     * @param recordEntity
     * @return
     */
    private boolean medalHasChange(GetPersonRecordResult.MedalRecordEntity recordEntity) {
        boolean isChange = false;
        int mDRanking = recordEntity.getRanking() - recordEntity.getPreranking();
        if (mDRanking < 0) {
            if (recordEntity.getPreranking() >= 90) {
                if (Math.abs(mDRanking) > 20) {
                    isChange = true;
                }
            } else if (recordEntity.getPreranking() >= 70) {
                if (Math.abs(mDRanking) > 30) {
                    isChange = true;
                }
            } else if (recordEntity.getPreranking() >= 40) {
                if (Math.abs(mDRanking) > 40) {
                    isChange = true;
                }
            } else {
                //not to do
            }
        } else {
            isChange = false;
        }
        return isChange;
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
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mPersonRecordEntity = JSON.parseObject(Response, GetPersonRecordResult.class);
//                Log.v("LocalUserFragment", Response);
                mUserEntity.setRunInfo(Response);
                getUserInfo();
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
     * 获取用户个人信息
     */
    private void getUserInfo() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_USER_INFO;
        RequestParams params = RequestParamsBuild.buildGetUserInfoRequest(mContext, userId);
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
                GetUserInfoResult mUserInfo = JSON.parseObject(Response, GetUserInfoResult.class);
                mUserEntity.setClockcount(mUserInfo.getClockcount());
                mUserEntity.setGroupcount(mUserInfo.getRungroupcount());
                mUserEntity.setFriendcount(mUserInfo.getFriendcount());
                LocalApplication.getInstance().setLoginUser(mUserEntity);
                UserData.updateUser(mContext, mUserEntity);
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_OK);
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
     * 个人直播间分享
     */
    private void shareLiveRoom() {
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }
        mDialogBuilder.showProgressDialog(mContext, "正在获取分享信息..", false);
        // 发送请求
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_WEIXIN_PERSON_SHARE_URL;
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
                final WxShareManager share = new WxShareManager(LocalUserFragmentV2.this.getActivity());
                mDialogBuilder.setShareWxFriendDialog(mContext, "分享个人首页到");
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
//                share.startWxShareUrl(result.getText(), result.getTitle(), url);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelHandler();
        causeGC();
    }

    private void cancelHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_ERROR);
            mPostHandler.removeMessages(MSG_POST_OK);
        }
    }

    /**
     * 通知GC
     */
    private void causeGC() {
        mUserEntity = null;
        mPersonRecordEntity = null;
    }

    /**
     * 启动好友列表
     */
    private void launchFriendList() {
        MainActivityV2 activity = (MainActivityV2) getActivity();
        activity.setTab(MainActivityV2.TAB_FRIEND);
    }

    /**
     * 启动团列表
     */
    private void launchGroupList() {
        MainActivityV2 activity = (MainActivityV2) getActivity();
        activity.setTab(MainActivityV2.TAB_GROUP);
    }

    /**
     * 进入闹钟列表界面
     */
    private void launchClockAct() {
        MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "",
                AppEnum.MaidianType.LocalUserClock, TimeUtils.NowTime()));
        Intent intent = new Intent(mContext, MyClockListActivity.class);
        intent.putExtra("userid", mUserEntity.userId);
        startActivity(intent);
    }
}
